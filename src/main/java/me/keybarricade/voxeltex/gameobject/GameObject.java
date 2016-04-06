/******************************************************************************
 * Copyright (c) Tim Visee 2016. All rights reserved.                         *
 *                                                                            *
 * @author Tim Visee                                                          *
 * @website http://timvisee.com/                                              *
 *                                                                            *
 * Open Source != No Copyright                                                *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software"), *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * You should have received a copy of The MIT License (MIT) along with this   *
 * program. If not, see <http://opensource.org/licenses/MIT/>.                *
 ******************************************************************************/

package me.keybarricade.voxeltex.gameobject;

import me.keybarricade.voxeltex.component.AbstractComponent;
import me.keybarricade.voxeltex.component.drawable.DrawableComponentInterface;
import me.keybarricade.voxeltex.component.overlay.OverlayComponentInterface;
import me.keybarricade.voxeltex.global.MainCamera;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class GameObject extends AbstractGameObject {

    /**
     * Game object name.
     */
    private String name;

    /**
     * The transform instance of this object.
     */
    private Transform transform = new Transform(this);

    /**
     * The parent of this game object.
     */
    private AbstractGameObject parent = null;

    /**
     * The children of this game object.
     */
    private List<AbstractGameObject> children = new ArrayList<>();

    /**
     * The components on this game object.
     */
    private List<AbstractComponent> components = new ArrayList<>();

    /**
     * Float buffer for the rendering matrix.
     */
    private FloatBuffer fb = BufferUtils.createFloatBuffer(16);

    /**
     * View matrix cache.
     * This is used to optimize performance and object allocation at runtime.
     */
    private static final Matrix4f viewMatrixCache = new Matrix4f();

    /**
     * Constructor.
     *
     * @param name Game object name.
     */
    public GameObject(String name) {
        super(name);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Transform getTransform() {
        return this.transform;
    }

    @Override
    public AbstractGameObject getParent() {
        return this.parent;
    }

    @Override
    public boolean hasParent() {
        return getParent() != null;
    }

    @Override
    protected void setParent(AbstractGameObject parent) {
        this.parent = parent;
    }

    @Override
    public List<AbstractGameObject> getChildren() {
        return this.children;
    }

    @Override
    public boolean hasChildren() {
        return getChildCount(false) > 0;
    }

    @Override
    public int getChildCount(boolean recursive) {
        // Count the number of children if not recursive
        if(!recursive)
            return this.children.size();

        // Count the number of recursive children
        int count = 0;

        // Loop through all the children, and count
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0, size = this.children.size(); i < size; i++)
            count += this.children.get(i).getChildCount(true);

        // Return the number of recursive children
        return count;
    }

    @Override
    public void addChild(AbstractGameObject gameObject) {
        // Set the parent
        gameObject.setParent(this);

        // Set the scene
        gameObject.setScene(getScene());

        // Add the game object to the children
        this.children.add(gameObject);

        // Create the game object
        if(getScene() != null)
            gameObject.create();

        // Start the game object if the scene has started
        if(isSceneStarted())
            gameObject.start();
    }

    @Override
    public AbstractGameObject getChild(int i) {
        return this.children.get(i);
    }

    @Override
    public boolean removeChild(AbstractGameObject gameObject) {
        // Remove any game object
        if(!this.children.remove(gameObject))
            return false;

        // Reset the parent
        gameObject.setParent(null);

        // Return the result
        return true;
    }

    @Override
    public AbstractGameObject removeChild(int i) {
        // Get the child that will be removed
        AbstractGameObject child;

        // Remove the child by it's index, and make sure any child was removed
        if((child = this.children.remove(i)) == null)
            return null;

        // Reset the parent
        child.setParent(null);

        // Return the child
        return child;
    }

    @Override
    public List<AbstractComponent> getComponents() {
        return this.components;
    }

    @Override
    public boolean hasComponents() {
        return getComponentCount() > 0;
    }

    @Override
    public int getComponentCount() {
        return this.components.size();
    }

    @Override
    public void addComponent(AbstractComponent component) {
        // Add the component
        this.components.add(component);

        // Set the component owner
        component.setOwner(this);

        // Create the component
        if(getScene() != null)
            component.create();

        // Start the component if the scene is started
        if(isSceneStarted())
            component.start();
    }

    @Override
    public AbstractComponent getComponent(int i) {
        return this.components.get(i);
    }

    @Override
    public <T extends AbstractComponent> T getComponent(Class<T> componentType) {
        // Loop through all components to find an applicable one
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0, size = this.components.size(); i < size; i++) {
            // Check whether this component is valid
            if(componentType.isAssignableFrom(this.components.get(i).getClass()))
                //noinspection unchecked
                return (T) this.components.get(i);
        }

        // None found, return null
        return null;
    }

    @Override
    public boolean removeComponent(AbstractComponent component) {
        // Remove any component
        if(!this.components.remove(component))
            return false;

        // Destroy the component
        component.destroy();

        // Reset the owner
        component.setOwner(null);

        // Return the result
        return true;
    }

    @Override
    public AbstractComponent removeComponent(int i) {
        // Get the component that will be removed
        AbstractComponent component;

        // Remove the component by it's index, and make sure any component was removed
        if((component = this.components.remove(i)) == null)
            return null;

        // Destroy the component
        component.destroy();

        // Reset the owner
        component.setOwner(null);

        // Return the component
        return component;
    }

    @Override
    public void create() {
        // Create the children
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0, size = this.children.size(); i < size; i++)
            this.children.get(i).create();

        // Create all components
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0, size = this.components.size(); i < size; i++)
            this.components.get(i).create();
    }

    @Override
    public void start() {
        // Start the children
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0, size = this.children.size(); i < size; i++)
            this.children.get(i).start();

        // Start all components
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0, size = this.components.size(); i < size; i++)
            this.components.get(i).start();
    }

    @Override
    public synchronized void update() {
        // Update the transform
        this.transform.update();

        // Update all components
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0, size = this.components.size(); i < size; i++)
            if(this.components.get(i).isEnabled())
                this.components.get(i).update();

        // Update all children
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0, size = this.children.size(); i < size; i++)
            if(this.children.get(i).isEnabled())
                this.children.get(i).update();
    }

    @Override
    public void destroy() {
        // Destroy all components
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0, size = this.components.size(); i < size; i++)
            this.components.get(i).destroy();

        // Destroy all children
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0, size = this.children.size(); i < size; i++)
            this.children.get(i).destroy();
    }

    @Override
    public synchronized void draw() {
        // Define whether we started drawing
        boolean drawing = false;

        // Draw all drawable components and all children
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0, size = this.components.size(); i < size; i++) {
            // Make sure the component is drawable
            if(this.components.get(i) instanceof DrawableComponentInterface) {
                // Make sure the drawing mode is enabled
                if(!drawing) {
                    // Start the drawing process and set the flag
                    drawStart();
                    drawing = true;
                }

                // Draw the component if enabled
                if(this.components.get(i).isEnabled())
                    ((DrawableComponentInterface) this.components.get(i)).draw();
            }
        }

        // End the drawing process if it was enabled
        if(drawing)
            drawEnd();

        // Draw all children if enabled
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0, size = this.children.size(); i < size; i++)
            if(this.children.get(i).isEnabled())
                this.children.get(i).draw();
    }

    @Override
    public synchronized void drawOverlay() {
        // Draw all overlay components and all children
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0, size = this.components.size(); i < size; i++)
            // Make sure the component is drawable
            if(this.components.get(i) instanceof OverlayComponentInterface)
                // Draw the component overlay
                if(this.components.get(i).isEnabled())
                    ((OverlayComponentInterface) this.components.get(i)).drawOverlay();

        // Draw all children
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0, size = this.children.size(); i < size; i++)
            if(this.children.get(i).isEnabled())
                this.children.get(i).drawOverlay();
    }

    /**
     * Prepare and start the drawing process.
     */
    private synchronized void drawStart() {
        // Do not use the cached view matrix in multiple places at the same time
        synchronized(viewMatrixCache) {
            // Combine the world camera and game object matrix to construct the view matrix
            getTransform().addWorldMatrix(MainCamera.createCameraViewMatrix(viewMatrixCache));

            // Load the matrix to the GPU
            GL11.glLoadMatrixf(viewMatrixCache.get(fb));
        }
    }

    /**
     * End the current drawing process.
     */
    private synchronized void drawEnd() {
        // Pop the OpenGL matrix
        GL11.glPopMatrix();
    }
}

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

package me.keybarricade.voxeltex.component.overlay.gui;

import me.keybarricade.voxeltex.component.transform.HorizontalTransformAnchorType;
import me.keybarricade.voxeltex.component.transform.Rectangle;
import me.keybarricade.voxeltex.component.transform.RectangleTransform;
import me.keybarricade.voxeltex.component.transform.VerticalTransformAnchorType;
import me.keybarricade.voxeltex.global.Input;
import me.keybarricade.voxeltex.prefab.gui.GuiLabelPrefab;
import me.keybarricade.voxeltex.render.RenderOverlayHelper;
import me.keybarricade.voxeltex.util.Color;
import org.joml.Vector2f;

public class GuiButtonComponent extends AbstractGuiComponent {

    /**
     * Temporary rectangle variable, used to minimize object allocation at runtime to improve overall performance.
     */
    private final Rectangle tempRectangle = new Rectangle();

    /**
     * The GUI label prefab used to render the button text.
     */
    private GuiLabelPrefab label;

    /**
     * Button text.
     */
    private String text = "Button";

    /**
     * Constructor.
     */
    public GuiButtonComponent() { }

    /**
     * Constructor.
     *
     * @param text Button text.
     */
    public GuiButtonComponent(String text) {
        this.text = text;
    }

    @Override
    public void create() {
        // Create a label prefab as child to use for button text rendering
        this.label = new GuiLabelPrefab(getName() + "Label", this.text);

        // Configure the label's rectangle transform
        this.label.getRectangleTransform().setPosition(new Vector2f(8, 8));
        this.label.getRectangleTransform().setSize(new Vector2f(8, 8));
        this.label.getRectangleTransform().setAnchorPreset(HorizontalTransformAnchorType.STRETCH, VerticalTransformAnchorType.STRETCH);

        // Add the text renderer component to this object as child
        getOwner().addChild(this.label);
    }

    @Override
    public void drawOverlay() {
        // Define variables to store whether the button is hovered and/or pressed
        boolean hover = false;
        boolean pressed = false;

        // Get the X and Y coordinate of the cursor in overlay space
        float mouseX = Input.getMouseXOverlay();
        float mouseY = Input.getMouseYOverlay();

        // Synchronize to ensure we aren't using this temporary variable in multiple spots at the same time
        synchronized(this.tempRectangle) {
            // Get the transform
            // TODO: Buffer this
            getComponent(RectangleTransform.class).getOverlayRectangle(this.tempRectangle);

            // Check whether the button is hovered or pressed
            if(mouseX >= this.tempRectangle.getX() && mouseX <= this.tempRectangle.getX() + this.tempRectangle.getWidth() &&
                    mouseY >= this.tempRectangle.getY() && mouseY <= this.tempRectangle.getY() + this.tempRectangle.getHeight()) {
                hover = true;
                pressed = Input.isMouseButtonDown(0);
            }

            // Update the button visuals
            if(pressed) {
                RenderOverlayHelper.color(0.2f, 0.2f, 0.2f, .9f);
                this.label.setColor(Color.WHITE);
            } else if(hover) {
                RenderOverlayHelper.color(.6f, .6f, .6f, .7f);
                this.label.setColor(Color.BLACK);
            } else {
                RenderOverlayHelper.color(0.9f, 0.9f, 0.9f, .7f);
                this.label.setColor(Color.BLACK);
            }

            // Render the rectangle
            RenderOverlayHelper.renderRectangle(
                    this.tempRectangle.getX(), this.tempRectangle.getY(),
                    this.tempRectangle.getWidth(), this.tempRectangle.getHeight()
            );
        }
    }

    /**
     * Get the text on the button.
     *
     * @return Button text.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Set the text on the button
     *
     * @param text Button text.
     */
    public void setText(String text) {
        // Set the button text
        this.text = text;

        // Set the button text on the font component
        this.label.setText(text);
    }
}

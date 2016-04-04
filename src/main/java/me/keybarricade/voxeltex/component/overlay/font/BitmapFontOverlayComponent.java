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

package me.keybarricade.voxeltex.component.overlay.font;

import me.keybarricade.voxeltex.component.overlay.AbstractOverlayComponent;
import me.keybarricade.voxeltex.font.BitmapFont;
import me.keybarricade.voxeltex.font.BitmapFontManager;
import me.keybarricade.voxeltex.math.vector.Vector2fFactory;
import me.keybarricade.voxeltex.render.RenderOverlayHelper;
import me.keybarricade.voxeltex.util.Color;
import org.joml.Vector2f;

public class BitmapFontOverlayComponent extends AbstractOverlayComponent {

    /**
     * Bitmap font that is used.
     */
    // TODO: Make configurable in constructor!
    private BitmapFont font = BitmapFontManager.getDefault();

    /**
     * Text to render.
     */
    private String text = "";

    /**
     * Font color.
     */
    private Color color = Color.WHITE;

    /**
     * Rectangle position.
     */
    // TODO: Use a 2D transform object.
    private Vector2f position = Vector2fFactory.identity();

    /**
     * Rectangle size.
     */
    // TODO: Use a 2D transform object.
    private Vector2f size = Vector2fFactory.identity();

    /**
     * Constructor.
     *
     * @param text Text to render.
     */
    public BitmapFontOverlayComponent(String text) {
        this.text = text;
        this.font = BitmapFontManager.getDefault();

        // TODO: Configure the position and size!
        this.position.set(0.1f);
        this.size.set(0.4f, 0.4f);
    }

    /**
     * Constructor.
     *
     * @param text Text to render.
     * @param color Font color.
     */
    public BitmapFontOverlayComponent(String text, Color color) {
        this.text = text;
        this.font = BitmapFontManager.getDefault();
        this.color = color;

        this.position.set(0.1f);
        this.size.set(0.4f, 0.4f);
    }

    /**
     * Get the font color.
     *
     * @return Font color.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Set the font color.
     *
     * @param color Font color.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void drawOverlay() {
        Vector2f size = new Vector2f(0.04f, 0.05f);

        // Apply the font color to the material
        this.font.getMaterial().setColor(this.color);

        // Bind and render each character separately
        for(int i = 0; i < text.length(); i++) {
            // Get the current character
            final char c = text.charAt(i);

            // Calculate the width factor of the current character
            final float widthFactor = this.font.getFontWidths().getCharacterWidthFactor(c);

            // Bind the font material with the current character and the proper character width
            this.font.getMaterial().bind(c, widthFactor);

            // Calculate the character width offset
            final float characterWidthOffset = size.x * this.font.getFontWidths().getStringWidthFactor(text.substring(0, i));

            // Render the rectangle
            RenderOverlayHelper.renderRectangle(position.x + characterWidthOffset, position.y, size.x * widthFactor, size.y);
        }
    }

    /**
     * Get the text to render.
     *
     * @return Text.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Set the text to render.
     *
     * @param text Text.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the bitmap font used.
     *
     * @return Bitmap font.
     */
    public BitmapFont getFont() {
        return this.font;
    }

    /**
     * Set the bitmap font used.
     *
     * @param font Bitmap font.
     */
    public void setFont(BitmapFont font) {
        this.font = font;
    }

    /**
     * Get the position of the rectangle.
     *
     * @return Position.
     */
    public Vector2f getPosition() {
        return this.position;
    }

    /**
     * Set the position of the rectangle.
     *
     * @param position Position.
     */
    public void setPosition(Vector2f position) {
        this.position = position;
    }

    /**
     * Get the size of the rectangle.
     *
     * @return Size.
     */
    public Vector2f getSize() {
        return this.size;
    }

    /**
     * Set the size of the rectangle.
     *
     * @param size Size.
     */
    public void setSize(Vector2f size) {
        this.size = size;
    }
}

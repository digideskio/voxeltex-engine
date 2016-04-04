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

#version 120

// Texture and tiling data
uniform sampler2D texture;
uniform vec2 tiling = vec2(1.0, 1.0);
uniform vec2 tileSize = vec2(16.0, 16.0);
uniform vec2 tilePosition = vec2(1.0, 4.0);
uniform vec4 color = vec4(1);

// Fragment position data
varying vec4 position;

void main(void) {
    // Calculate the proper tile position
    vec2 tilePosMapped = vec2(tilePosition / tileSize);

    // Determine and set the fragment color
    // TODO: Make font color configurable!
    gl_FragColor = texture2D(texture, gl_TexCoord[0].st * tiling / tileSize + tilePosMapped) * color;
}

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

package me.keybarricade.voxeltex.ini;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class IniLoader {

    /**
     * Load an INI file from the given path.
     *
     * @param path Path.
     *
     * @return Loaded INI configuration.
     *
     * @throws IOException Throws if an error occurred.
     */
    public static IniConfig load(String path) throws IOException {
        // Parse and return the INI configuration from the given file path
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            return IniParser.parse(reader);
        }
    }

    /**
     * Load an INI file from the given file.
     *
     * @param file File.
     *
     * @return Loaded INI configuration.
     *
     * @throws IOException Throws if an error occurred.
     */
    public static IniConfig load(File file) throws IOException {
        // Parse and return the INI configuration from the given file path
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return IniParser.parse(reader);
        }
    }
}

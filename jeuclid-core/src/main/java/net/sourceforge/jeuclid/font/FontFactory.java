/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id: $ */

package net.sourceforge.jeuclid.font;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Abstract factory to be used to create instances of java.awt.Font.
 * The rationale behind this approach is that out-of-the box 
 * there is no way in java platform to load and <b>register</b>
 * and internal font. In other words, Font.createFont and Font's constructor
 * are not aware of each other.
 * <p>The abstract FontFactory just provides a centralized access and extention
 * point, delegating the actual functionality to subclasses.
 * <p>How a concrete subclass of FontFactory is identified is subject to change
 * in the future versions.
 * 
 * @author Ernest Mishkin
 * @version $Revision:   $
 */
public abstract class FontFactory {
    
    private static FontFactory instance = new DefaultFontFactory();
    
    /**
     * Return an instance of the currently configured concrete FontFactory.
     * @return an instance of FontFactory  
     */
    public static FontFactory getInstance() {
        return FontFactory.instance;
    }

    /**
     * Create a font object with specified properties.
     * Font name may refer to either 'built-in' or loaded externally 
     * and 'cached' font.
     * @param name font name or font family name
     * @param style font style
     * @param size font size
     * @return Font instance
     * @see java.awt.Font#Font(String, int, int)  
     */
    public abstract Font getFont(String name, int style, int size);
    
    /**
     * Load an external font from a file and 'register' (aka 'cache') it
     * for future use.
     * @param format font format (TTF or TYPE_1 currently supported by the platform)
     * @param fontFile file which contains the font
     * @return The newly created Font instance
     * @throws FontFormatException if font contained in the file 
     *         doesn't match the specified format
     * @throws IOException in case of problem while reading the file
     * @see java.awt.Font#createFont(int, File)
     */
    public abstract Font registerFont(int format, File fontFile) 
        throws IOException, FontFormatException;

    /**
     * Load an external font from a stream and 'register' (aka 'cache') it
     * for future use.
     * @param format font format (TTF or TYPE_1 currently supported by the platform)
     * @param fontStream file which contains the font
     * @return The newly created Font instance
     * @throws FontFormatException if font contained in the stream 
     *         doesn't match the specified format
     * @throws IOException in case of problem while reading the stream
     * @see java.awt.Font#createFont(int, InputStream)
     */
    public abstract Font registerFont(int format, InputStream fontStream) 
        throws IOException, FontFormatException;
}
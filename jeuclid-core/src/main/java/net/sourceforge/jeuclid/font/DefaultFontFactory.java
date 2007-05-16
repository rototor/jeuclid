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
import java.util.HashMap;
import java.util.Map;

/**
 * Concrete FontFactory implementation that does simple caching of Fonts 
 * loaded via {@link Font#createFont(int, File)} APIs.
 * 
 * @author Ernest Mishkin
 * @version $Revision:   $
 */
public class DefaultFontFactory extends FontFactory {
    
    private Map<String, Font> fontCache;
    
    DefaultFontFactory() {
        this.fontCache = new HashMap<String, Font>();
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
    public Font getFont(final String name, final int style, final int size) {
        Font font = this.fontCache.get(name);
        if (font == null) {
            font = new Font(name, style, size);
        } else {
            font = font.deriveFont(style, size);
        }
        return font;
    }
    
    /**
     * Load an external font from a file and 'register' (aka 'cache') it
     * for future use.
     * @param format font format (TTF or TYPE_1 currently supported by the platform)
     * @param fontFile file which contains the font
     * @throws FontFormatException if font contained in the file 
     *         doesn't match the specified format
     * @throws IOException in case of problem while reading the file
     * @see java.awt.Font#createFont(int, File)
     */
    public void registerFont(final int format, final File fontFile) 
        throws IOException, FontFormatException {
        
        this.cacheFont(Font.createFont(format, fontFile));
    }

    /**
     * Load an external font from a stream and 'register' (aka 'cache') it
     * for future use.
     * @param format font format (TTF or TYPE_1 currently supported by the platform)
     * @param fontStream file which contains the font
     * @throws FontFormatException if font contained in the stream 
     *         doesn't match the specified format
     * @throws IOException in case of problem while reading the stream
     * @see java.awt.Font#createFont(int, InputStream)
     */
    public void registerFont(final int format, final InputStream fontStream) 
        throws IOException, FontFormatException {
    
        this.cacheFont(Font.createFont(format, fontStream));
    }
    
    
    /**
     * Actually stores a font in the cache.
     * Uses font name and font family as keys.
     * @param font Font instance to cache 
     */
    protected void cacheFont(final Font font) {
        this.fontCache.put(font.getFontName(), font);
        this.fontCache.put(font.getFamily(), font);
    }
    
}

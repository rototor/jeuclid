/*
 * Copyright 2007 - 2008 JEuclid, http://jeuclid.sf.net
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

/* $Id$ */

package net.sourceforge.jeuclid.font;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.util.ClasspathResource;

/**
 * Concrete FontFactory implementation that does simple caching of Fonts
 * loaded via {@link Font#createFont(int, File)} APIs.
 * 
 * @version $Revision$
 */
public class DefaultFontFactory extends FontFactory {

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory
            .getLog(DefaultFontFactory.class);

    private final Map<String, Font> fontCache;

    DefaultFontFactory() {
        this.fontCache = new HashMap<String, Font>();
        this.autoloadFontsFromAWT();
        this.autoloadFontsFromClasspath();
    }

    private void autoloadFontsFromAWT() {
        final String[] fam = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (final String element : fam) {
            final Font f = new Font(element, 0, 12);
            this.cacheFont(f);
        }
    }

    @SuppressWarnings("unchecked")
    private void autoloadFontsFromClasspath() {
        final List<URL> fonts = ClasspathResource.getInstance()
                .listResourcesOfMimeType("application/x-font");
        for (final URL u : fonts) {
            try {
                try {
                    this.cacheFont(Font.createFont(Font.TRUETYPE_FONT, u
                            .openStream()));
                } catch (final FontFormatException e) {
                    try {
                        this.cacheFont(Font.createFont(Font.TYPE1_FONT, u
                                .openStream()));
                    } catch (final FontFormatException e1) {
                        DefaultFontFactory.LOGGER.warn(e.getMessage());
                    }
                }
            } catch (final IOException e) {
                DefaultFontFactory.LOGGER.warn(e.getMessage());
            }
        }

    }

    /**
     * Create a font object with specified properties. Font name may refer to
     * either 'built-in' or loaded externally and 'cached' font.
     * 
     * @param name
     *            font name or font family name
     * @param style
     *            font style
     * @param size
     *            font size
     * @return Font instance
     * @see java.awt.Font#Font(String, int, int)
     */
    @Override
    public Font getFont(final String name, final int style, final int size) {
        Font font = this.fontCache.get(name.toLowerCase(Locale.ENGLISH));
        if (font == null) {
            font = new Font(name, style, size);
        } else {
            font = font.deriveFont(style, size);
        }
        return font;
    }

    /** {@inheritDoc} */
    @Override
    public Font getFont(final List<String> preferredFonts,
            final int codepoint, final int style, final int size) {
        Font font = this.searchFontList(preferredFonts, codepoint, style,
                size);
        if (font == null) {
            font = this.searchFontList(this.fontCache.keySet(), codepoint,
                    style, size);
        }
        return font;
    }

    private Font searchFontList(final Collection<String> fontList,
            final int codepoint, final int style, final int size) {
        for (final String fontName : fontList) {
            final Font font = this.getFont(fontName, style, size);
            final String desiredFont = fontName.trim();
            if (((font.getFamily().equalsIgnoreCase(desiredFont)) || (font
                    .getFontName().equalsIgnoreCase(desiredFont)))
                    && (font.canDisplay(codepoint))) {
                return font;
            }
        }
        return null;
    }

    /**
     * Load an external font from a file and 'register' (aka 'cache') it for
     * future use.
     * 
     * @param format
     *            font format (TTF or TYPE_1 currently supported by the
     *            platform)
     * @param fontFile
     *            file which contains the font
     * @return The newly created Font instance
     * @throws FontFormatException
     *             if font contained in the file doesn't match the specified
     *             format
     * @throws IOException
     *             in case of problem while reading the file
     * @see java.awt.Font#createFont(int, File)
     */
    @Override
    public Font registerFont(final int format, final File fontFile)
            throws IOException, FontFormatException {

        return this.cacheFont(Font.createFont(format, fontFile));
    }

    /**
     * Load an external font from a stream and 'register' (aka 'cache') it for
     * future use.
     * 
     * @param format
     *            font format (TTF or TYPE_1 currently supported by the
     *            platform)
     * @param fontStream
     *            file which contains the font
     * @return The newly created Font instance
     * @throws FontFormatException
     *             if font contained in the stream doesn't match the specified
     *             format
     * @throws IOException
     *             in case of problem while reading the stream
     * @see java.awt.Font#createFont(int, InputStream)
     */
    @Override
    public Font registerFont(final int format, final InputStream fontStream)
            throws IOException, FontFormatException {

        return this.cacheFont(Font.createFont(format, fontStream));
    }

    /**
     * Actually stores a font in the cache. Uses font name and font family as
     * keys.
     * 
     * @param font
     *            Font instance to cache
     * @return the font instance that was cached
     */
    private Font cacheFont(final Font font) {
        this.fontCache.put(font.getFontName().trim().toLowerCase(
                Locale.ENGLISH).intern(), font);
        final String family = font.getFamily().trim().toLowerCase(
                Locale.ENGLISH).intern();
        // This is a safeguard. On Linux for DejaVu Sans Oblique we get:
        // Name: DejaVu Sans Oblique
        // Family: DejaVu Sans
        // For DejaVu Sans we get:
        // Name: DejaVu Sans
        // Family: DejaVu Sans
        // And of course we don't want the oblique font to override the
        // regular font...
        if (!this.fontCache.containsKey(family)) {
            this.fontCache.put(family, font);
        }
        return font;
    }

    /** {@inheritDoc} */
    @Override
    public Set<String> listFontNames() {
        return this.fontCache.keySet();
    }

}

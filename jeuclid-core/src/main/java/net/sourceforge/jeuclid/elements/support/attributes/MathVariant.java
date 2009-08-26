/*
 * Copyright 2002 - 2008 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.elements.support.attributes;

import java.awt.Font;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.elements.support.text.CharacterMapping;
import net.sourceforge.jeuclid.font.FontFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to represent and use MathVariants.
 * 
 * @version $Revision$
 */
public final class MathVariant implements Serializable {

    /**
     * Mathvariant constant. Bold style.
     */
    public static final MathVariant BOLD = new MathVariant(Font.BOLD,
            FontFamily.SERIF);

    /**
     * Mathvariant constant. Bold fraktur style.
     */
    public static final MathVariant BOLD_FRAKTUR = new MathVariant(Font.BOLD,
            FontFamily.FRAKTUR);

    /**
     * Mathvariant constant. Bold-italic style.
     */
    public static final MathVariant BOLD_ITALIC = new MathVariant(Font.BOLD
            | Font.ITALIC, FontFamily.SERIF);

    /**
     * Mathvariant constant. Bold sans-serif style.
     */
    public static final MathVariant BOLD_SANS_SERIF = new MathVariant(
            Font.BOLD, FontFamily.SANSSERIF);

    /**
     * Mathvariant constant. Bold script style.
     */
    public static final MathVariant BOLD_SCRIPT = new MathVariant(Font.BOLD,
            FontFamily.SCRIPT);

    /**
     * Mathvariant constant. Double struck style.
     */
    public static final MathVariant DOUBLE_STRUCK = new MathVariant(
            Font.PLAIN, FontFamily.DOUBLE_STRUCK);

    /**
     * Mathvariant constant. Fraktur style.
     */
    public static final MathVariant FRAKTUR = new MathVariant(Font.PLAIN,
            FontFamily.FRAKTUR);

    /**
     * Mathvariant constant. Italic style.
     */
    public static final MathVariant ITALIC = new MathVariant(Font.ITALIC,
            FontFamily.SERIF);

    /**
     * Mathvariant constant. Monospace style.
     */
    public static final MathVariant MONOSPACE = new MathVariant(Font.PLAIN,
            FontFamily.MONOSPACED);

    /**
     * Mathvariant constant. Normal style.
     */
    public static final MathVariant NORMAL = new MathVariant(Font.PLAIN,
            FontFamily.SERIF);

    /**
     * Mathvariant constant. Sans-serif style.
     */
    public static final MathVariant SANS_SERIF = new MathVariant(Font.PLAIN,
            FontFamily.SANSSERIF);

    /**
     * Mathvariant constant. Bold italic sans-serif style.
     */
    public static final MathVariant SANS_SERIF_BOLD_ITALIC = new MathVariant(
            Font.BOLD | Font.ITALIC, FontFamily.SANSSERIF);

    /**
     * Mathvariant constant. Italic sans-serif style.
     */
    public static final MathVariant SANS_SERIF_ITALIC = new MathVariant(
            Font.ITALIC, FontFamily.SANSSERIF);

    /**
     * Mathvariant constant. Script style.
     */
    public static final MathVariant SCRIPT = new MathVariant(Font.PLAIN,
            FontFamily.SCRIPT);

    private static final Map<String, MathVariant> ATTRIBUTEMAP = new HashMap<String, MathVariant>();

    private static final Map<FontFamily, Parameter> PARAMFORFONT = new HashMap<FontFamily, Parameter>();

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Logger for this class.
     */
    private static final Log LOGGER = LogFactory
            .getLog(CharacterMapping.class);

    private static final Set<Integer> WARNED = new HashSet<Integer>();

    private final int awtStyle;

    private final FontFamily fontFamily;

    /**
     * Creates a Mathvariant with the given AWT-Style and font-family.
     * 
     * @param awtstyle
     *            the awt Style
     * @param family
     *            the font family
     */
    public MathVariant(final int awtstyle, final FontFamily family) {
        this.awtStyle = awtstyle;
        this.fontFamily = family;
    };

    /**
     * Creates a Mathvariant object from an attribute value.
     * 
     * @param variant
     *            the string representation of the attribute value
     * @return a mathVariant object or null if none is found.
     */
    public static MathVariant stringToMathVariant(final String variant) {
        synchronized (MathVariant.ATTRIBUTEMAP) {
            // Needs to be initialized late due to chicken-egg problem.
            if (MathVariant.ATTRIBUTEMAP.isEmpty()) {
                MathVariant.ATTRIBUTEMAP.put("normal", MathVariant.NORMAL);
                MathVariant.ATTRIBUTEMAP.put("bold", MathVariant.BOLD);
                MathVariant.ATTRIBUTEMAP.put("italic", MathVariant.ITALIC);
                MathVariant.ATTRIBUTEMAP.put("bold-italic",
                        MathVariant.BOLD_ITALIC);
                MathVariant.ATTRIBUTEMAP.put("double-struck",
                        MathVariant.DOUBLE_STRUCK);
                MathVariant.ATTRIBUTEMAP.put("bold-fraktur",
                        MathVariant.BOLD_FRAKTUR);
                MathVariant.ATTRIBUTEMAP.put("script", MathVariant.SCRIPT);
                MathVariant.ATTRIBUTEMAP.put("bold-script",
                        MathVariant.BOLD_SCRIPT);
                MathVariant.ATTRIBUTEMAP.put("fraktur", MathVariant.FRAKTUR);
                MathVariant.ATTRIBUTEMAP.put("sans-serif",
                        MathVariant.SANS_SERIF);
                MathVariant.ATTRIBUTEMAP.put("bold-sans-serif",
                        MathVariant.BOLD_SANS_SERIF);
                MathVariant.ATTRIBUTEMAP.put("sans-serif-italic",
                        MathVariant.SANS_SERIF_ITALIC);
                MathVariant.ATTRIBUTEMAP.put("sans-serif-bold-italic",
                        MathVariant.SANS_SERIF_BOLD_ITALIC);
                MathVariant.ATTRIBUTEMAP.put("monospace",
                        MathVariant.MONOSPACE);
            }
            return MathVariant.ATTRIBUTEMAP.get(variant
                    .toLowerCase(Locale.ENGLISH));
        }
    }

    /**
     * Create a font for the given attributes.
     * 
     * @param size
     *            size of the font to create
     * @param codepoint
     *            a character that must exist in this font
     * @param context
     *            LayoutContext to use.
     * @param force
     *            if true will always return a font, otherwise will return
     *            null.
     * @return a font object.
     */
    @SuppressWarnings("unchecked")
    public Font createFont(final float size, final int codepoint,
            final LayoutContext context, final boolean force) {

        final Parameter theParam = MathVariant.PARAMFORFONT
                .get(this.fontFamily);
        final Font font = FontFactory.getInstance().getFont(
                (List<String>) context.getParameter(theParam), codepoint,
                this.awtStyle, size);
        if (force && font == null) {
            if (!MathVariant.WARNED.contains(codepoint)) {
                MathVariant.WARNED.add(codepoint);
                final String hexString = Integer.toHexString(codepoint);
                MathVariant.LOGGER
                        .warn("No font available to display character "
                                + hexString);
                MathVariant.LOGGER
                        .info("Find a font at  http://www.fileformat.info/info/unicode/char/"
                                + hexString
                                + "/fontsupport.htm or "
                                + "http://www.alanwood.net/unicode/search.html");
            }
            return FontFactory.getInstance().getFont(FontFactory.SANSSERIF,
                    this.awtStyle, size);
        }
        return font;
    }

    /**
     * @return the awtStyle
     */
    public int getAwtStyle() {
        return this.awtStyle;
    }

    /**
     * @return the fontFamily
     */
    public FontFamily getFontFamily() {
        return this.fontFamily;
    }

    static {
        MathVariant.PARAMFORFONT.put(FontFamily.SERIF, Parameter.FONTS_SERIF);
        MathVariant.PARAMFORFONT.put(FontFamily.SANSSERIF,
                Parameter.FONTS_SANSSERIF);
        MathVariant.PARAMFORFONT.put(FontFamily.MONOSPACED,
                Parameter.FONTS_MONOSPACED);
        MathVariant.PARAMFORFONT.put(FontFamily.SCRIPT,
                Parameter.FONTS_SCRIPT);
        MathVariant.PARAMFORFONT.put(FontFamily.FRAKTUR,
                Parameter.FONTS_FRAKTUR);
        MathVariant.PARAMFORFONT.put(FontFamily.DOUBLE_STRUCK,
                Parameter.FONTS_DOUBLESTRUCK);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result;
        if (this.fontFamily == null) {
            result = 0;
        } else {
            result = this.fontFamily.hashCode();
        }
        result = prime * result + this.awtStyle;
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final MathVariant other = (MathVariant) obj;
        if (this.awtStyle != other.awtStyle) {
            return false;
        }
        if (this.fontFamily == null) {
            if (other.fontFamily != null) {
                return false;
            }
        } else if (!this.fontFamily.equals(other.fontFamily)) {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();
        b.append('[');
        b.append(this.fontFamily);
        if (this.awtStyle > 0) {
            b.append(' ');
        }
        if ((this.awtStyle & Font.BOLD) > 0) {
            b.append('B');
        }
        if ((this.awtStyle & Font.ITALIC) > 0) {
            b.append('I');
        }
        b.append(']');
        return b.toString();
    }
}

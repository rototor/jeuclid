/*
 * Copyright 2002 - 2007 JEuclid, http://jeuclid.sf.net
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.ParameterKey;

/**
 * Class to represent and use MathVariants.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public final class MathVariant {

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

    private static final String AWT_SANSSERIF = "sansserif";

    private static final Map<FontFamily, ParameterKey> PARAMFORFONT = new HashMap<FontFamily, ParameterKey>();

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
     * @return a mathVariant object
     */
    public static MathVariant stringToMathVariant(final String variant) {
        // Needs to be inialized late due to chicken-egg problem.
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
            MathVariant.ATTRIBUTEMAP
                    .put("sans-serif", MathVariant.SANS_SERIF);
            MathVariant.ATTRIBUTEMAP.put("bold-sans-serif",
                    MathVariant.BOLD_SANS_SERIF);
            MathVariant.ATTRIBUTEMAP.put("sans-serif-italic",
                    MathVariant.SANS_SERIF_ITALIC);
            MathVariant.ATTRIBUTEMAP.put("sans-serif-bold-italic",
                    MathVariant.SANS_SERIF_BOLD_ITALIC);
            MathVariant.ATTRIBUTEMAP.put("monospace", MathVariant.MONOSPACE);
        }

        return (MathVariant) MathVariant.ATTRIBUTEMAP.get(variant
                .toLowerCase(Locale.ENGLISH));
    }

    /**
     * Create a font for the given attributes.
     * 
     * @param size
     *            size of the font to create
     * @param c
     *            a character that must exist in this font
     * @param base
     *            MathBase to use.
     * @return a font object.
     */
    public Font createFont(final float size, final char c, final MathBase base) {

        final ParameterKey theParam = MathVariant.PARAMFORFONT
                .get(this.fontFamily);
        final String paramValue = base.getParams().get(theParam);
        final String[] fontArray = paramValue.split(",");
        Font font = null;
        for (int i = 0; (i < fontArray.length) && (font == null); i++) {
            font = new Font(fontArray[i], this.awtStyle, (int) size);
            if (font.getFamily().equalsIgnoreCase(fontArray[i].trim())) {
                if (!font.canDisplay(c)) {
                    font = null;
                }
            } else {
                font = null;
            }
        }
        if (font == null) {
            font = new Font(MathVariant.AWT_SANSSERIF, this.awtStyle,
                    (int) size);
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
        MathVariant.PARAMFORFONT.put(FontFamily.SERIF,
                ParameterKey.FontsSerif);
        MathVariant.PARAMFORFONT.put(FontFamily.SANSSERIF,
                ParameterKey.FontsSanserif);
        MathVariant.PARAMFORFONT.put(FontFamily.MONOSPACED,
                ParameterKey.FontsMonospaced);
        MathVariant.PARAMFORFONT.put(FontFamily.SCRIPT,
                ParameterKey.FontsScript);
        MathVariant.PARAMFORFONT.put(FontFamily.FRAKTUR,
                ParameterKey.FontsFraktur);
        MathVariant.PARAMFORFONT.put(FontFamily.DOUBLE_STRUCK,
                ParameterKey.FontsDoublestruck);
    }
}

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

package net.sourceforge.jeuclid.element.attributes;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to represent and use MathVariants.
 * 
 * @author Max Berger
 */
public final class MathVariant {

    /**
     * Mathvariant constant. Normal style.
     */
    public static final MathVariant NORMAL = new MathVariant(Font.PLAIN,
            FontFamily.SERIF);

    /**
     * Mathvariant constant. Bold style.
     */
    public static final MathVariant BOLD = new MathVariant(Font.BOLD,
            FontFamily.SERIF);

    /**
     * Mathvariant constant. Italic style.
     */
    public static final MathVariant ITALIC = new MathVariant(Font.ITALIC,
            FontFamily.SERIF);

    /**
     * Mathvariant constant. Bold-italic style.
     */
    public static final MathVariant BOLD_ITALIC = new MathVariant(Font.BOLD
            | Font.ITALIC, FontFamily.SERIF);

    /**
     * Mathvariant constant. Double struck style.
     */
    public static final MathVariant DOUBLE_STRUCK = new MathVariant(
            Font.PLAIN, FontFamily.DOUBLE_STRUCK);

    /**
     * Mathvariant constant. Bold fraktur style.
     */
    public static final MathVariant BOLD_FRAKTUR = new MathVariant(Font.BOLD,
            FontFamily.FRAKTUR);

    /**
     * Mathvariant constant. Script style.
     */
    public static final MathVariant SCRIPT = new MathVariant(Font.PLAIN,
            FontFamily.SCRIPT);

    /**
     * Mathvariant constant. Bold script style.
     */
    public static final MathVariant BOLD_SCRIPT = new MathVariant(Font.BOLD,
            FontFamily.SCRIPT);

    /**
     * Mathvariant constant. Fraktur style.
     */
    public static final MathVariant FRAKTUR = new MathVariant(Font.PLAIN,
            FontFamily.FRAKTUR);

    /**
     * Mathvariant constant. Sans-serif style.
     */
    public static final MathVariant SANS_SERIF = new MathVariant(Font.PLAIN,
            FontFamily.SANSSERIF);

    /**
     * Mathvariant constant. Bold sans-serif style.
     */
    public static final MathVariant BOLD_SANS_SERIF = new MathVariant(
            Font.BOLD, FontFamily.SANSSERIF);

    /**
     * Mathvariant constant. Italic sans-serif style.
     */
    public static final MathVariant SANS_SERIF_ITALIC = new MathVariant(
            Font.ITALIC, FontFamily.SANSSERIF);

    /**
     * Mathvariant constant. Bold italic sans-serif style.
     */
    public static final MathVariant SANS_SERIF_BOLD_ITALIC = new MathVariant(
            Font.BOLD | Font.ITALIC, FontFamily.SANSSERIF);

    /**
     * Mathvariant constant. Monospace style.
     */
    public static final MathVariant MONOSPACE = new MathVariant(Font.PLAIN,
            FontFamily.MONOSPACED);

    private static final Map<String, MathVariant> ATTRIBUTEMAP = new HashMap<String, MathVariant>();

    private static final Map<FontFamily, String[]> KNOWNFONTS = new HashMap<FontFamily, String[]>();

    private static final String AWT_SANSSERIF = "sansserif";

    private final int awtStyle;

    private final FontFamily fontFamily;

    private MathVariant(final int awtstyle, final FontFamily family) {
        this.awtStyle = awtstyle;
        this.fontFamily = family;
    };

    /**
     * Create a font for the given attributes.
     * 
     * @param size
     *            size of the font to create
     * @param c
     *            a character that must exist in this font
     * @return a font object.
     */
    public Font createFont(final float size, final char c) {
        // Lazy initalization
        if (MathVariant.KNOWNFONTS.isEmpty()) {
            // TODO: These are just some available fonts! Add more!
            // TODO: Make user configurable.
            MathVariant.KNOWNFONTS.put(FontFamily.MONOSPACED,
                    new String[] { "monospaced", });
            MathVariant.KNOWNFONTS.put(FontFamily.SANSSERIF,
                    new String[] { MathVariant.AWT_SANSSERIF, });
            MathVariant.KNOWNFONTS.put(FontFamily.SCRIPT,
                    new String[] { "Savoye LET", "Brush Script MT",
                            "Zapfino", "Apple Chancery",
                            "Edwardian Script ITC", "Lucida Handwriting",
                            "Santa Fe LET", "Monotype Corsiva", });
            MathVariant.KNOWNFONTS.put(FontFamily.FRAKTUR, new String[] {
                    "FetteFraktur", "Fette Fraktur", "Euclid Fraktur",
                    "Lucida Blackletter", "Blackmoor LET", });
            MathVariant.KNOWNFONTS.put(FontFamily.SERIF,
                    new String[] { "serif" });
            MathVariant.KNOWNFONTS.put(FontFamily.DOUBLE_STRUCK,
                    new String[] { "Caslon Open Face", "Caslon Openface",
                            "Cloister Open Face", "Academy Engraved LET",
                            "Colonna MT", "Imprint MT Shadow", });
        }

        final String[] fontArray = (String[]) MathVariant.KNOWNFONTS
                .get(this.fontFamily);
        Font font = null;
        for (int i = 0; (i < fontArray.length) && (font == null); i++) {
            font = new Font(fontArray[i], this.awtStyle, (int) size);
            if (font.getFamily().equalsIgnoreCase(fontArray[i])) {
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
                .toLowerCase());
    }

}

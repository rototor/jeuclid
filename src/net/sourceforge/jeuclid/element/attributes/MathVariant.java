/*
 * Copyright 2002 - 2006 JEuclid, http://jeuclid.sf.net
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

    private static final String FONT_MONOSPACED = "Monospaced";

    private static final String FONT_SANSSERIF = "SansSerif";

    private static final String FONT_SCRIPT = "Script";

    private static final String FONT_FRAKTUR = "Fraktur";

    private static final String FONT_SERIF = "Serif";

    private static final String FONT_DOUBLESTRUCK = "Double-Struck";

    /**
     * Mathvariant constant. Normal style.
     */
    public static final MathVariant NORMAL = new MathVariant(Font.PLAIN,
            FONT_SERIF);

    /**
     * Mathvariant constant. Bold style.
     */
    public static final MathVariant BOLD = new MathVariant(Font.BOLD,
            FONT_SERIF);

    /**
     * Mathvariant constant. Italic style.
     */
    public static final MathVariant ITALIC = new MathVariant(Font.ITALIC,
            FONT_SERIF);

    /**
     * Mathvariant constant. Bold-italic style.
     */
    public static final MathVariant BOLD_ITALIC = new MathVariant(Font.BOLD
            | Font.ITALIC, FONT_SERIF);

    /**
     * Mathvariant constant. Double struck style.
     */
    public static final MathVariant DOUBLE_STRUCK = new MathVariant(Font.PLAIN,
            FONT_DOUBLESTRUCK);

    /**
     * Mathvariant constant. Bold fraktur style.
     */
    public static final MathVariant BOLD_FRAKTUR = new MathVariant(Font.BOLD,
            FONT_FRAKTUR);

    /**
     * Mathvariant constant. Script style.
     */
    public static final MathVariant SCRIPT = new MathVariant(Font.PLAIN,
            FONT_SCRIPT);

    /**
     * Mathvariant constant. Bold script style.
     */
    public static final MathVariant BOLD_SCRIPT = new MathVariant(Font.BOLD,
            FONT_SCRIPT);

    /**
     * Mathvariant constant. Fraktur style.
     */
    public static final MathVariant FRAKTUR = new MathVariant(Font.PLAIN,
            FONT_FRAKTUR);

    /**
     * Mathvariant constant. Sans-serif style.
     */
    public static final MathVariant SANS_SERIF = new MathVariant(Font.PLAIN,
            FONT_SANSSERIF);

    /**
     * Mathvariant constant. Bold sans-serif style.
     */
    public static final MathVariant BOLD_SANS_SERIF = new MathVariant(
            Font.BOLD, FONT_SANSSERIF);

    /**
     * Mathvariant constant. Italic sans-serif style.
     */
    public static final MathVariant SANS_SERIF_ITALIC = new MathVariant(
            Font.ITALIC, FONT_SANSSERIF);

    /**
     * Mathvariant constant. Bold italic sans-serif style.
     */
    public static final MathVariant SANS_SERIF_BOLD_ITALIC = new MathVariant(
            Font.BOLD | Font.ITALIC, FONT_SANSSERIF);

    /**
     * Mathvariant constant. Monospace style.
     */
    public static final MathVariant MONOSPACE = new MathVariant(Font.PLAIN,
            FONT_MONOSPACED);

    private static final Map attributeMap = new HashMap();

    private static final Map knownFonts = new HashMap();

    private final int awtStyle;

    private final String fontFamily;

    private MathVariant(int awtStyle, String family) {
        this.awtStyle = awtStyle;
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
    public Font createFont(float size, char c) {
        // Lazy initalization
        if (knownFonts.isEmpty()) {
            // TODO: These are just some available fonts! Add more!
            // TODO: Make user configurable.
            knownFonts.put(FONT_MONOSPACED, new String[] { "monospaced" });
            knownFonts.put(FONT_SANSSERIF, new String[] { "sansserif" });
            knownFonts.put(FONT_SCRIPT, new String[] { "Savoye LET",
                    "Brush Script MT", "Zapfino", "Apple Chancery",
                    "Edwardian Script ITC", "Lucida Handwriting",
                    "Santa Fe LET", "Monotype Corsiva" });
            knownFonts.put(FONT_FRAKTUR, new String[] { "FetteFraktur",
                    "Fette Fraktur", "Euclid Fraktur", "Lucida Blackletter",
                    "Blackmoor LET" });
            knownFonts.put(FONT_SERIF, new String[] { "serif" });
            knownFonts.put(FONT_DOUBLESTRUCK, new String[] {
                    "Caslon Open Face", "Caslon Openface",
                    "Cloister Open Face", "Academy Engraved LET", "Colonna MT",
                    "Imprint MT Shadow" });
        }

        String[] fontArray = (String[]) knownFonts.get(fontFamily);
        Font font = null;
        for (int i = 0; (i < fontArray.length) && (font == null); i++) {
            font = new Font(fontArray[i], awtStyle, (int) size);
            if (font.getFamily().equalsIgnoreCase(fontArray[i])) {
                if (!font.canDisplay(c)) {
                    font = null;
                }
            } else {
                font = null;
            }
        }
        if (font == null) {
            font = new Font("sansserif", awtStyle, (int) size);
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
    public static MathVariant stringToMathVariant(String variant) {
        // Needs to be inialized late due to chicken-egg problem.
        if (attributeMap.isEmpty()) {
            attributeMap.put("normal", NORMAL);
            attributeMap.put("bold", BOLD);
            attributeMap.put("italic", ITALIC);
            attributeMap.put("bold-italic", BOLD_ITALIC);
            attributeMap.put("double-struck", DOUBLE_STRUCK);
            attributeMap.put("bold-fraktur", BOLD_FRAKTUR);
            attributeMap.put("script", SCRIPT);
            attributeMap.put("bold-script", BOLD_SCRIPT);
            attributeMap.put("fraktur", FRAKTUR);
            attributeMap.put("sans-serif", SANS_SERIF);
            attributeMap.put("bold-sans-serif", BOLD_SANS_SERIF);
            attributeMap.put("sans-serif-italic", SANS_SERIF_ITALIC);
            attributeMap.put("sans-serif-bold-italic", SANS_SERIF_BOLD_ITALIC);
            attributeMap.put("monospace", MONOSPACE);
        }

        return (MathVariant) attributeMap.get(variant.toLowerCase());
    }

}

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

package net.sourceforge.jeuclid.elements.support.text;

import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.elements.support.attributes.FontFamily;
import net.sourceforge.jeuclid.elements.support.attributes.MathVariant;

/**
 * Utilities for String handling.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public final class StringUtil {

    private static final int LOWERCASE_START = 0x61;

    private static final int PLANE_1_START = 0x10000;

    private static final int PLANE_1_FRAKTUR_LOWER_START = 0x1d51E;

    private static final int PLANE_1_FRAKTUR_LOWER_END = 0x1d537;

    private static final int LOW_SURROGATE_START = 0xdc00;

    private static final int HIGH_SURROGATE_PLANE_SIZE = 0x400;

    private static final int HIGH_SURROGATE_START = 0xd800;

    private static final int HIGH_SURROGATE_END = 0xdbff;

    private static final Map<Integer, Integer> FRAKTUR_MAPPING = new HashMap<Integer, Integer>();

    private static final Map<Integer, Integer> SCRIPT_MAPPING = new HashMap<Integer, Integer>();

    private static final Map<Integer, Integer> DOUBLE_MAPPING = new HashMap<Integer, Integer>();

    private StringUtil() {
        // do nothing
    }

    /**
     * Converts a given String to an attributed string with the proper
     * variants set.
     * 
     * @param plainString
     *            the string to convert.
     * @param baseVariant
     *            variant to base on for regular characters
     * @param fontSize
     *            size of Font to use.
     * @param base
     *            MathBase to use.
     * @return an attributed string that has Textattribute.FONT set for all
     *         characters.
     */
    public static AttributedString convertStringtoAttributedString(
            final String plainString, final MathVariant baseVariant,
            final float fontSize, final MathBase base) {
        final StringBuilder builder = new StringBuilder();
        final List<MathVariant> variants = new Vector<MathVariant>();
        for (int i = 0; i < plainString.length(); i++) {
            int codePoint = plainString.charAt(i);
            if ((codePoint >= StringUtil.HIGH_SURROGATE_START)
                    && (codePoint <= StringUtil.HIGH_SURROGATE_END)
                    && (i < plainString.length() - 1)) {
                i++;
                codePoint = (codePoint - StringUtil.HIGH_SURROGATE_START)
                        * StringUtil.HIGH_SURROGATE_PLANE_SIZE
                        + (plainString.charAt(i) - StringUtil.LOW_SURROGATE_START)
                        + StringUtil.PLANE_1_START;
            }
            MathVariant variant = baseVariant;
            if (codePoint >= StringUtil.PLANE_1_START) {
                if ((codePoint >= StringUtil.PLANE_1_FRAKTUR_LOWER_START)
                        && (codePoint <= StringUtil.PLANE_1_FRAKTUR_LOWER_END)) {
                    codePoint = codePoint
                            - StringUtil.PLANE_1_FRAKTUR_LOWER_START
                            + StringUtil.LOWERCASE_START;
                    variant = MathVariant.FRAKTUR;
                }
                // TODO: There are many others to be mapped!
            }

            final int awtStyle = variant.getAwtStyle();
            final FontFamily fontFamily = variant.getFontFamily();
            if (FontFamily.FRAKTUR.equals(fontFamily)) {
                final Integer mapping = StringUtil.FRAKTUR_MAPPING
                        .get(codePoint);
                if (mapping != null) {
                    codePoint = mapping;
                    variant = new MathVariant(awtStyle, FontFamily.SANSSERIF);
                }
            } else if (FontFamily.SCRIPT.equals(fontFamily)) {
                final Integer mapping = StringUtil.SCRIPT_MAPPING
                        .get(codePoint);
                if (mapping != null) {
                    codePoint = mapping;
                    variant = new MathVariant(awtStyle, FontFamily.SANSSERIF);
                }
            } else if (FontFamily.DOUBLE_STRUCK.equals(fontFamily)) {
                final Integer mapping = StringUtil.DOUBLE_MAPPING
                        .get(codePoint);
                if (mapping != null) {
                    codePoint = mapping;
                    variant = new MathVariant(awtStyle, FontFamily.SANSSERIF);
                }

            }
            builder.append((char) codePoint);
            variants.add(variant);
        }

        final AttributedString aString = new AttributedString(builder
                .toString());

        for (int i = 0; i < builder.length(); i++) {
            final MathVariant variant = variants.get(i);
            aString.addAttribute(TextAttribute.FONT, variant.createFont(
                    fontSize, builder.charAt(i), base), i, i + 1);
        }
        return aString;
    }

    /**
     * Retrieves the real width from a given text layout.
     * 
     * @param layout
     *            the textlayout
     * @return width
     */
    public static float getWidthForTextLayout(final TextLayout layout) {
        final Rectangle2D r2d = layout.getBounds();
        float realWidth = (float) r2d.getWidth();
        final float xo = (float) r2d.getX();
        if (xo > 0) {
            realWidth += xo;
        }
        return Math.max(realWidth, layout.getAdvance());
    }

    static {
        // CHECKSTYLE:OFF

        // From: http://www.w3.org/TR/MathML2/fraktur.html
        StringUtil.FRAKTUR_MAPPING.put((int) 'C', 0x0212D);
        StringUtil.FRAKTUR_MAPPING.put((int) 'H', 0x0210C);
        StringUtil.FRAKTUR_MAPPING.put((int) 'I', 0x02111);
        StringUtil.FRAKTUR_MAPPING.put((int) 'R', 0x0211C);
        StringUtil.FRAKTUR_MAPPING.put((int) 'Z', 0x02128);

        // From: http://www.w3.org/TR/MathML2/script.html
        StringUtil.SCRIPT_MAPPING.put((int) 'B', 0x212C);
        StringUtil.SCRIPT_MAPPING.put((int) 'E', 0x2130);
        StringUtil.SCRIPT_MAPPING.put((int) 'e', 0x212F);
        StringUtil.SCRIPT_MAPPING.put((int) 'F', 0x2131);
        StringUtil.SCRIPT_MAPPING.put((int) 'g', 0x210A);
        StringUtil.SCRIPT_MAPPING.put((int) 'H', 0x210B);
        StringUtil.SCRIPT_MAPPING.put((int) 'I', 0x2110);
        StringUtil.SCRIPT_MAPPING.put((int) 'L', 0x2112);
        StringUtil.SCRIPT_MAPPING.put((int) 'M', 0x2133);
        StringUtil.SCRIPT_MAPPING.put((int) 'o', 0x2134);
        StringUtil.SCRIPT_MAPPING.put((int) 'R', 0x211B);

        // From: http://www.w3.org/TR/MathML2/double-struck.html
        StringUtil.DOUBLE_MAPPING.put((int) 'C', 0x2102);
        StringUtil.DOUBLE_MAPPING.put((int) 'H', 0x210D);
        StringUtil.DOUBLE_MAPPING.put((int) 'N', 0x2115);
        StringUtil.DOUBLE_MAPPING.put((int) 'P', 0x2119);
        StringUtil.DOUBLE_MAPPING.put((int) 'Q', 0x211A);
        StringUtil.DOUBLE_MAPPING.put((int) 'R', 0x211D);
        StringUtil.DOUBLE_MAPPING.put((int) 'Z', 0x2124);
        // CHECKSTYLE:ON
    }

}

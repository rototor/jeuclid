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

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;
import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.LayoutContext.Parameter;
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

    private static final int UPPERCASE_START = 0x41;

    private static final int NUM_CHARS = 26;

    private static final Map<Integer, CodePointAndVariant> HIGHPLANE_MAPPING = new HashMap<Integer, CodePointAndVariant>();

    private static final Map<Integer, Integer> FRAKTUR_MAPPING = new HashMap<Integer, Integer>();

    private static final Map<Integer, Integer> SCRIPT_MAPPING = new HashMap<Integer, Integer>();

    private static final Map<Integer, Integer> DOUBLE_MAPPING = new HashMap<Integer, Integer>();

    private StringUtil() {
        // do nothing
    }

    private static class CodePointAndVariant {
        private final int codePoint;

        private final MathVariant variant;

        protected CodePointAndVariant(final int icodePoint,
                final MathVariant ivariant) {
            this.codePoint = icodePoint;
            this.variant = ivariant;
        }

        /**
         * @return the codePoint
         */
        public final int getCodePoint() {
            return this.codePoint;
        }

        /**
         * @return the variant
         */
        public final MathVariant getVariant() {
            return this.variant;
        }

    }

    /**
     * Converts a given String to an attributed string with the proper
     * variants set.
     * 
     * @param inputString
     *            the string to convert.
     * @param baseVariant
     *            variant to base on for regular characters
     * @param fontSize
     *            size of Font to use.
     * @param context
     *            Layotu Context to use.
     * @return an attributed string that has Textattribute.FONT set for all
     *         characters.
     */
    public static AttributedString convertStringtoAttributedString(
            final String inputString, final MathVariant baseVariant,
            final float fontSize, final LayoutContext context) {
        final StringBuilder builder = new StringBuilder();
        final List<MathVariant> variants = new Vector<MathVariant>();
        final String plainString = CharConverter.convertLate(inputString);

        for (int i = 0; i < plainString.length(); i++) {
            if (!Character.isLowSurrogate(plainString.charAt(i))) {

                CodePointAndVariant cpav = new CodePointAndVariant(
                        plainString.codePointAt(i), baseVariant);

                cpav = StringUtil.mapHighCodepointToLowerCodepoints(cpav);
                cpav = StringUtil.mapVariantsToStandardCodepoints(cpav);

                final int codePoint = cpav.getCodePoint();
                final MathVariant variant = cpav.getVariant();

                builder.appendCodePoint(codePoint);
                variants.add(variant);
                if (Character.isSupplementaryCodePoint(codePoint)) {
                    variants.add(variant);
                }
            }
        }

        final AttributedString aString = new AttributedString(builder
                .toString());

        final int len = builder.length();

        for (int i = 0; i < len; i++) {
            final MathVariant variant = variants.get(i);
            aString.addAttribute(TextAttribute.FONT, variant.createFont(
                    fontSize, builder.charAt(i), context), i, i + 1);
        }
        return aString;
    }

    /**
     * Safely creates a Text Layout from an attributed string. Unlike the
     * TextLayout constructor, the String here may actually be empty.
     * 
     * @param g
     *            Graphics context.
     * @param aString
     *            an Attributed String
     * @param context
     *            Layout Context to use.
     * @return a TextLayout
     */
    public static TextLayout createTextLayoutFromAttributedString(
            final Graphics2D g, final AttributedString aString,
            final LayoutContext context) {
        // This is an evil and totally not-understandable workaround which
        // is essential to get Anti-aliasing on SUNS JDK 1.5
        // It is not needed for JDK >= 1.6 or OS X
        final FontRenderContext suggestedFontRenderContext = g
                .getFontRenderContext();
        final boolean antialiasing = (Boolean) context
                .getParameter(Parameter.ANTIALIAS);
        final FontRenderContext realFontRenderContext = new FontRenderContext(
                suggestedFontRenderContext.getTransform(), antialiasing, true);
        // TODO: We may want to turn off anti-aliasing below a certain
        // size threshold (such as 8pt)

        final TextLayout theLayout;
        if (aString.getIterator().first() != CharacterIterator.DONE) {
            theLayout = new TextLayout(aString.getIterator(),
                    realFontRenderContext);
        } else {
            theLayout = new TextLayout(" ", new Font("", 0, 0),
                    realFontRenderContext);
        }
        return theLayout;
    }

    /**
     * Maps the characters that have a default representation (such as the
     * double-struck N) in the Unicode lower plane to that character.
     * <p>
     * This is necessary as fonts for special math representations, such as
     * double-struck may not be available. However, the double-struck n is
     * very likely do be available in one of the default fonts.
     * 
     * @param cpav
     * @return
     */
    private static CodePointAndVariant mapVariantsToStandardCodepoints(
            final CodePointAndVariant cpav) {
        int codePoint = cpav.getCodePoint();
        MathVariant variant = cpav.getVariant();
        final int awtStyle = variant.getAwtStyle();
        final FontFamily fontFamily = variant.getFontFamily();
        if (FontFamily.FRAKTUR.equals(fontFamily)) {
            final Integer mapping = StringUtil.FRAKTUR_MAPPING.get(codePoint);
            if (mapping != null) {
                codePoint = mapping;
                variant = new MathVariant(awtStyle, FontFamily.SANSSERIF);
            }
        } else if (FontFamily.SCRIPT.equals(fontFamily)) {
            final Integer mapping = StringUtil.SCRIPT_MAPPING.get(codePoint);
            if (mapping != null) {
                codePoint = mapping;
                variant = new MathVariant(awtStyle, FontFamily.SANSSERIF);
            }
        } else if (FontFamily.DOUBLE_STRUCK.equals(fontFamily)) {
            final Integer mapping = StringUtil.DOUBLE_MAPPING.get(codePoint);
            if (mapping != null) {
                codePoint = mapping;
                variant = new MathVariant(awtStyle, FontFamily.SANSSERIF);
            }

        }
        return new CodePointAndVariant(codePoint, variant);

    }

    /**
     * Maps codepoints from the hi plane (> 0x10000) to a representation of
     * the same character in the lower plane, with the corresponding
     * mathvariant attribute.
     * <p>
     * This is necessary because font support for the high plane is almost
     * non-existing.
     * 
     * @param cpav
     * @return
     */
    private static CodePointAndVariant mapHighCodepointToLowerCodepoints(
            final CodePointAndVariant cpav) {
        final int codePoint = cpav.getCodePoint();
        final CodePointAndVariant mappedTo = StringUtil.HIGHPLANE_MAPPING
                .get(codePoint);
        if (mappedTo != null) {
            return mappedTo;
        } else {
            return cpav;
        }
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
        // Unfortunately this is necessary, although it does not look like it
        // makes a lot of sense.
        final float invisibleAdvance = layout.getAdvance()
                - layout.getVisibleAdvance();
        return realWidth + invisibleAdvance;
    }

    private static void addHighMapping(final int codePointStart,
            final MathVariant mapToVariant) {

        for (int i = 0; i < StringUtil.NUM_CHARS; i++) {
            StringUtil.HIGHPLANE_MAPPING.put(codePointStart + i,
                    new CodePointAndVariant(StringUtil.UPPERCASE_START + i,
                            mapToVariant));
        }
        for (int i = 0; i < StringUtil.NUM_CHARS; i++) {
            StringUtil.HIGHPLANE_MAPPING.put(codePointStart
                    + StringUtil.NUM_CHARS + i, new CodePointAndVariant(
                    StringUtil.LOWERCASE_START + i, mapToVariant));
        }
    }

    private static void initializeVariantToStandardMapping() {
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

    private static void initializeHighPlaneMappings() {
        // CHECKSTYLE:OFF
        StringUtil.addHighMapping(0x1D400, MathVariant.BOLD);
        StringUtil.addHighMapping(0x1D434, MathVariant.ITALIC);
        StringUtil.addHighMapping(0x1D468, MathVariant.BOLD_ITALIC);
        StringUtil.addHighMapping(0x1D49C, MathVariant.SCRIPT);
        StringUtil.addHighMapping(0x1D4D0, MathVariant.BOLD_SCRIPT);
        StringUtil.addHighMapping(0x1D504, MathVariant.FRAKTUR);
        StringUtil.addHighMapping(0x1D538, MathVariant.DOUBLE_STRUCK);
        StringUtil.addHighMapping(0x1D56C, MathVariant.BOLD_FRAKTUR);
        StringUtil.addHighMapping(0x1D5A0, MathVariant.SANS_SERIF);
        StringUtil.addHighMapping(0x1D5D4, MathVariant.BOLD_SANS_SERIF);
        StringUtil.addHighMapping(0x1D608, MathVariant.SANS_SERIF_ITALIC);
        StringUtil
                .addHighMapping(0x1D63C, MathVariant.SANS_SERIF_BOLD_ITALIC);
        StringUtil.addHighMapping(0x1D670, MathVariant.MONOSPACE);

        // TODO: Greek Mappings
        // TODO: Number mappings
        // CHECKSTYLE:ON
    }

    static {
        StringUtil.initializeVariantToStandardMapping();
        StringUtil.initializeHighPlaneMappings();
    }

}

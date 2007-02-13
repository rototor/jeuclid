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

package net.sourceforge.jeuclid.util;

import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.List;
import java.util.Vector;

import net.sourceforge.jeuclid.element.attributes.MathVariant;

/**
 * Utilities for String handling.
 * 
 * @author Max Berger
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
     * @return an attributed string that has Textattribute.FONT set for all
     *         characters.
     */
    public static AttributedString convertStringtoAttributedString(
            final String plainString, final MathVariant baseVariant,
            final float fontSize) {
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

            builder.append((char) codePoint);
            variants.add(variant);
        }

        final AttributedString aString = new AttributedString(builder
                .toString());

        for (int i = 0; i < builder.length(); i++) {
            final MathVariant variant = (MathVariant) variants.get(i);
            aString.addAttribute(TextAttribute.FONT, variant.createFont(
                    fontSize, builder.charAt(i)));
        }
        return aString;
    }
}

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

/* $Id: StringUtil.java,v 1.1.2.2 2007/02/08 18:57:39 maxberger Exp $ */

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
        // Should JEUclid go 1.5+ this should be replaced by StringBuilder
        final StringBuffer builder = new StringBuffer();
        final List<MathVariant> variants = new Vector<MathVariant>();
        for (int i = 0; i < plainString.length(); i++) {
            int codePoint = plainString.charAt(i);
            if ((codePoint >= 0xd800) && (codePoint <= 0xdbff)
                    && (i < plainString.length() - 1)) {
                i++;
                codePoint = (codePoint - 0xd800) * 0x400
                        + (plainString.charAt(i) - 0xdc00) + 0x10000;
            }
            // TODO: This should be passed in.
            MathVariant variant = baseVariant;
            if (codePoint >= 0x10000) {
                if ((codePoint >= 0x1d51E) && (codePoint <= 0x1d537)) {
                    codePoint = codePoint - 0x1d51E + 0x61;
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

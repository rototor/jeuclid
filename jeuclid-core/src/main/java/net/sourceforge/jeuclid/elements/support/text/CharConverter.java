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

package net.sourceforge.jeuclid.elements.support.text;

import java.util.HashMap;
import java.util.Map;

/**
 * class for char converting.
 * 
 * @version $Revision$
 */
public final class CharConverter {

    /**
     * Char equivalents to be mapped immediately before display.
     */
    private static final Map<Character, String> LATE_MAP_MAP = new HashMap<Character, String>();

    /**
     * Char equivalents to be mapped when parsing.
     */
    private static final Map<Character, String> EARLY_MAP_MAP = new HashMap<Character, String>(
            200);

    private CharConverter() {
        // Empty on purpose.
    }

    /**
     * @param string
     *            String for char replacing
     * @return result string
     */
    private static String actualConvert(final String string,
            final Map<Character, String> map) {
        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            final char orig = string.charAt(i);
            final String mapsTo = map.get(orig);
            if (mapsTo == null) {
                buffer.append(orig);
            } else {
                buffer.append(mapsTo);
            }
        }
        return buffer.toString();
    }

    /**
     * @param string
     *            String for char replacing
     * @return result string
     */
    public static String convertEarly(final String string) {
        return CharConverter.actualConvert(string,
                CharConverter.EARLY_MAP_MAP);
    }

    /**
     * @param string
     *            String for char replacing
     * @return result string
     */
    public static String convertLate(final String string) {
        return CharConverter
                .actualConvert(string, CharConverter.LATE_MAP_MAP);
    }

    // CHECKSTYLE:OFF
    // Too many statements, but this is initialization!
    static {
        CharConverter.LATE_MAP_MAP.put('\u2061', "");
        CharConverter.LATE_MAP_MAP.put('\u200b', "");
        CharConverter.LATE_MAP_MAP.put('\u2062', "");
        CharConverter.LATE_MAP_MAP.put('\u2148', "");
        /*
         * This maps UnderBar -> Overbar. The regular mapping of underbars
         * (0332) is a combining character, which produces incorrect text
         * metrics.
         * 
         * Underscore (_) should be used, but then the information about
         * strechting is lost.
         * 
         * OverBars are higher in the layout. However, UnderBars are usually
         * only used in underscripts, where this produces no problem.
         * 
         * TODO: Check if there are other combining characters among the
         * default entities and map them accordingly.
         */
        CharConverter.LATE_MAP_MAP.put('\u0332', "\u00AF");

        /*
         * These are created by OpenOffice formula < 2.2. See
         * http://www.openoffice.org/servlets/ReadMsg?list=dev&msgNo=543
         * 
         * These are mapping from the private area of the "starSymbol" (now
         * 'openSymbol') font.
         */
        CharConverter.EARLY_MAP_MAP.put('\uE080', "\u2031");
        CharConverter.EARLY_MAP_MAP.put('\uE081', "\uF613");
        CharConverter.EARLY_MAP_MAP.put('\uE083', "\u002B");
        CharConverter.EARLY_MAP_MAP.put('\uE084', "\u003C");
        CharConverter.EARLY_MAP_MAP.put('\uE085', "\u003E");
        CharConverter.EARLY_MAP_MAP.put('\uE086', "\ue425");
        CharConverter.EARLY_MAP_MAP.put('\uE087', "\ue421");
        CharConverter.EARLY_MAP_MAP.put('\uE089', "\u2208");
        CharConverter.EARLY_MAP_MAP.put('\uE08A', "\u0192");
        CharConverter.EARLY_MAP_MAP.put('\uE08B', "\u2026");
        CharConverter.EARLY_MAP_MAP.put('\uE08C', "\u2192");
        CharConverter.EARLY_MAP_MAP.put('\uE091', "\u0302");
        CharConverter.EARLY_MAP_MAP.put('\uE092', "\u030C");
        CharConverter.EARLY_MAP_MAP.put('\uE093', "\u0306");
        CharConverter.EARLY_MAP_MAP.put('\uE094', "\u0301");
        CharConverter.EARLY_MAP_MAP.put('\uE095', "\u0300");
        CharConverter.EARLY_MAP_MAP.put('\uE096', "\u0303");
        CharConverter.EARLY_MAP_MAP.put('\uE097', "\u0304");
        // Was: 20D7, but 2192 is more widely supported
        CharConverter.EARLY_MAP_MAP.put('\uE098', "\u2192");
        CharConverter.EARLY_MAP_MAP.put('\uE099', "\u02d9");
        CharConverter.EARLY_MAP_MAP.put('\uE09A', "\u0308");
        CharConverter.EARLY_MAP_MAP.put('\uE09B', "\u20DB");
        CharConverter.EARLY_MAP_MAP.put('\uE09C', "\u030A");
        CharConverter.EARLY_MAP_MAP.put('\uE09E', "\u0028");
        CharConverter.EARLY_MAP_MAP.put('\uE09F', "\u0029");
        CharConverter.EARLY_MAP_MAP.put('\uE0A2', "\u301A");
        CharConverter.EARLY_MAP_MAP.put('\uE0A3', "\u301B");
        CharConverter.EARLY_MAP_MAP.put('\uE0A4', "\u2373");
        CharConverter.EARLY_MAP_MAP.put('\uE0A8', "\u002F");
        CharConverter.EARLY_MAP_MAP.put('\uE0A9', "\\");
        CharConverter.EARLY_MAP_MAP.put('\uE0AA', "\u274F");
        CharConverter.EARLY_MAP_MAP.put('\uE0AC', "\u0393");
        CharConverter.EARLY_MAP_MAP.put('\uE0AD', "\u0394");
        CharConverter.EARLY_MAP_MAP.put('\uE0AE', "\u0398");
        CharConverter.EARLY_MAP_MAP.put('\uE0AF', "\u039b");
        CharConverter.EARLY_MAP_MAP.put('\uE0B0', "\u039e");
        CharConverter.EARLY_MAP_MAP.put('\uE0B1', "\u03A0");
        CharConverter.EARLY_MAP_MAP.put('\uE0B2', "\u03a3");
        CharConverter.EARLY_MAP_MAP.put('\uE0B3', "\u03a5");
        CharConverter.EARLY_MAP_MAP.put('\uE0B4', "\u03a6");
        CharConverter.EARLY_MAP_MAP.put('\uE0B5', "\u03a8");
        CharConverter.EARLY_MAP_MAP.put('\uE0B6', "\u03a9");
        CharConverter.EARLY_MAP_MAP.put('\uE0B7', "\u03b1");
        CharConverter.EARLY_MAP_MAP.put('\uE0B8', "\u03b2");
        CharConverter.EARLY_MAP_MAP.put('\uE0B9', "\u03b3");
        CharConverter.EARLY_MAP_MAP.put('\uE0BA', "\u03b4");
        CharConverter.EARLY_MAP_MAP.put('\uE0BB', "\u03b5");
        CharConverter.EARLY_MAP_MAP.put('\uE0BC', "\u03b6");
        CharConverter.EARLY_MAP_MAP.put('\uE0BD', "\u03b7");
        CharConverter.EARLY_MAP_MAP.put('\uE0BE', "\u03b8");
        CharConverter.EARLY_MAP_MAP.put('\uE0BF', "\u03b9");
        CharConverter.EARLY_MAP_MAP.put('\uE0C0', "\u03ba");
        CharConverter.EARLY_MAP_MAP.put('\uE0C1', "\u03bb");
        CharConverter.EARLY_MAP_MAP.put('\uE0C2', "\u03bc");
        CharConverter.EARLY_MAP_MAP.put('\uE0C3', "\u03bd");
        CharConverter.EARLY_MAP_MAP.put('\uE0C4', "\u03be");
        CharConverter.EARLY_MAP_MAP.put('\uE0C5', "\u03bf");
        CharConverter.EARLY_MAP_MAP.put('\uE0C6', "\u03c0");
        CharConverter.EARLY_MAP_MAP.put('\uE0C7', "\u03c1");
        CharConverter.EARLY_MAP_MAP.put('\uE0C8', "\u03c3");
        CharConverter.EARLY_MAP_MAP.put('\uE0C9', "\u03c4");
        CharConverter.EARLY_MAP_MAP.put('\uE0CA', "\u03c5");
        CharConverter.EARLY_MAP_MAP.put('\uE0CB', "\u03c6");
        CharConverter.EARLY_MAP_MAP.put('\uE0CC', "\u03c7");
        CharConverter.EARLY_MAP_MAP.put('\uE0CD', "\u03c8");
        CharConverter.EARLY_MAP_MAP.put('\uE0CE', "\u03c9");
        CharConverter.EARLY_MAP_MAP.put('\uE0CF', "\u03b5");
        CharConverter.EARLY_MAP_MAP.put('\uE0D0', "\u03d1");
        CharConverter.EARLY_MAP_MAP.put('\uE0D1', "\u03d6");
        CharConverter.EARLY_MAP_MAP.put('\uE0D2', "\u03f1");
        CharConverter.EARLY_MAP_MAP.put('\uE0D3', "\u03db");
        CharConverter.EARLY_MAP_MAP.put('\uE0D4', "\u2118");
        CharConverter.EARLY_MAP_MAP.put('\uE0D5', "\u2202");
        CharConverter.EARLY_MAP_MAP.put('\uE0D6', "\u2129");
        CharConverter.EARLY_MAP_MAP.put('\uE0D7', "\u2107");
        CharConverter.EARLY_MAP_MAP.put('\uE0D8', "\u2127");
        CharConverter.EARLY_MAP_MAP.put('\uE0D9', "\u22A4");
        CharConverter.EARLY_MAP_MAP.put('\uE0DA', "\u019B");
        CharConverter.EARLY_MAP_MAP.put('\uE0DB', "\u2190");
        CharConverter.EARLY_MAP_MAP.put('\uE0DC', "\u2191");
        CharConverter.EARLY_MAP_MAP.put('\uE0DD', "\u2193");

        // This set is generated by LaTeXMathML, and seems to be the set
        // supported via Mathematica 4.1 fonts in Firefox 2.0

        // SCRIPT CAPITALS
        CharConverter.EARLY_MAP_MAP.put('\uEF35', new String(
                new int[] { 0x1D49C }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF36', new String(
                new int[] { 0x1D49E }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF37', new String(
                new int[] { 0x1D49F }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF38', new String(
                new int[] { 0x1D4A2 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF39', new String(
                new int[] { 0x1D4A5 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF3A', new String(
                new int[] { 0x1D4A6 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF3B', new String(
                new int[] { 0x1D4A9 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF3C', new String(
                new int[] { 0x1D4AA }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF3D', new String(
                new int[] { 0x1D4AB }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF3E', new String(
                new int[] { 0x1D4AC }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF3F', new String(
                new int[] { 0x1D4AE }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF40', new String(
                new int[] { 0x1D4AF }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF41', new String(
                new int[] { 0x1D4B0 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF42', new String(
                new int[] { 0x1D4B1 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF43', new String(
                new int[] { 0x1D4B2 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF44', new String(
                new int[] { 0x1D4B3 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF45', new String(
                new int[] { 0x1D4B4 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF46', new String(
                new int[] { 0x1D4B5 }, 0, 1));

        // FRAKTUR
        CharConverter.EARLY_MAP_MAP.put('\uEF5D', new String(
                new int[] { 0x1D504 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF5E', new String(
                new int[] { 0x1D505 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF5F', new String(
                new int[] { 0x1D507 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF60', new String(
                new int[] { 0x1D508 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF61', new String(
                new int[] { 0x1D509 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF62', new String(
                new int[] { 0x1D50A }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF63', new String(
                new int[] { 0x1D50D }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF64', new String(
                new int[] { 0x1D50E }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF65', new String(
                new int[] { 0x1D50F }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF66', new String(
                new int[] { 0x1D510 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF67', new String(
                new int[] { 0x1D511 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF68', new String(
                new int[] { 0x1D512 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF69', new String(
                new int[] { 0x1D513 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF6A', new String(
                new int[] { 0x1D514 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF6B', new String(
                new int[] { 0x1D516 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF6C', new String(
                new int[] { 0x1D517 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF6D', new String(
                new int[] { 0x1D518 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF6E', new String(
                new int[] { 0x1D519 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF6F', new String(
                new int[] { 0x1D51A }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF70', new String(
                new int[] { 0x1D51B }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF71', new String(
                new int[] { 0x1D51C }, 0, 1));

        // DOUBLE_STRUCK CAPITALS
        CharConverter.EARLY_MAP_MAP.put('\uEF8C', new String(
                new int[] { 0x1D538 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF8D', new String(
                new int[] { 0x1D539 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF8E', new String(
                new int[] { 0x1D53B }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF8F', new String(
                new int[] { 0x1D53C }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF90', new String(
                new int[] { 0x1D53D }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF91', new String(
                new int[] { 0x1D53E }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF92', new String(
                new int[] { 0x1D540 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF93', new String(
                new int[] { 0x1D541 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF94', new String(
                new int[] { 0x1D542 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF95', new String(
                new int[] { 0x1D543 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF96', new String(
                new int[] { 0x1D544 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF97', new String(
                new int[] { 0x1D546 }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF98', new String(
                new int[] { 0x1D54A }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF99', new String(
                new int[] { 0x1D54B }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF9A', new String(
                new int[] { 0x1D54C }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF9B', new String(
                new int[] { 0x1D54D }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF9C', new String(
                new int[] { 0x1D54E }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF9D', new String(
                new int[] { 0x1D54F }, 0, 1));
        CharConverter.EARLY_MAP_MAP.put('\uEF9E', new String(
                new int[] { 0x1D550 }, 0, 1));
        // CHECKSTYLE:ON
    }
}

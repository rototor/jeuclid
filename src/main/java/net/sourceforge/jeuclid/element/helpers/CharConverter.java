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

package net.sourceforge.jeuclid.element.helpers;

/**
 * class for char converting.
 */
public class CharConverter {

    /**
     * Array for char equivalents
     */
    private static final String[][] MAP = {
        {"\u2061", ""},
        {"\u200b", ""},
        {"\u2062", ""},
        {"\u2148", "i"},
        /*
         * These are created by Openoffice formula. See
         * http://www.openoffice.org/servlets/ReadMsg?list=dev&msgNo=543
         * 
         * These are mapping from the private area of the "starSymbol" (now
         * "openSymbol") font.
         */        
        {"\uE080", "\u2031"},
        {"\uE081", "\uF613"},
        {"\uE083", "\u002B"},
        {"\uE084", "\u003C"},
        {"\uE085", "\u003E"},
        {"\uE086", "\ue425"},
        {"\uE087", "\ue421"},
        {"\uE089", "\u2208"},
        {"\uE08A", "\u0192"},
        {"\uE08B", "\u2026"},
        {"\uE08C", "\u2192"},
        {"\uE091", "\u0302"},
        {"\uE092", "\u030C"},
        {"\uE093", "\u0306"},
        {"\uE094", "\u0301"},
        {"\uE095", "\u0300"},
        {"\uE096", "\u0303"},
        {"\uE097", "\u0304"},
        {"\uE098", "\u20D7"},
        {"\uE099", "\u02d9"},
        {"\uE09A", "\u0308"},
        {"\uE09B", "\u20DB"},
        {"\uE09C", "\u030A"},
        {"\uE09E", "\u0028"},
        {"\uE09F", "\u0029"},
        {"\uE0A2", "\u301A"},
        {"\uE0A3", "\u301B"},
        {"\uE0A4", "\u2373"},
        {"\uE0A8", "\u002F"},
        {"\uE0A9", "\\"},
        {"\uE0AA", "\u274F"},
        {"\uE0AC", "\u0393"},
        {"\uE0AD", "\u0394"},
        {"\uE0AE", "\u0398"},
        {"\uE0AF", "\u039b"},
        {"\uE0B0", "\u039e"},
        {"\uE0B1", "\u03A0"},
        {"\uE0B2", "\u03a3"},
        {"\uE0B3", "\u03a5"},
        {"\uE0B4", "\u03a6"},
        {"\uE0B5", "\u03a8"},
        {"\uE0B6", "\u03a9"},
        {"\uE0B7", "\u03b1"},
        {"\uE0B8", "\u03b2"},
        {"\uE0B9", "\u03b3"},
        {"\uE0BA", "\u03b4"},
        {"\uE0BB", "\u03b5"},
        {"\uE0BC", "\u03b6"},
        {"\uE0BD", "\u03b7"},
        {"\uE0BE", "\u03b8"},
        {"\uE0BF", "\u03b9"},
        {"\uE0C0", "\u03ba"},
        {"\uE0C1", "\u03bb"},
        {"\uE0C2", "\u03bc"},
        {"\uE0C3", "\u03bd"},
        {"\uE0C4", "\u03be"},
        {"\uE0C5", "\u03bf"},
        {"\uE0C6", "\u03c0"},
        {"\uE0C7", "\u03c1"},
        {"\uE0C8", "\u03c3"},
        {"\uE0C9", "\u03c4"},
        {"\uE0CA", "\u03c5"},
        {"\uE0CB", "\u03c6"},
        {"\uE0CC", "\u03c7"},
        {"\uE0CD", "\u03c8"},
        {"\uE0CE", "\u03c9"},
        {"\uE0CF", "\u03b5"},
        {"\uE0D0", "\u03d1"},
        {"\uE0D1", "\u03d6"},
        {"\uE0D2", "\u03f1"},
        {"\uE0D3", "\u03db"},
        {"\uE0D4", "\u2118"},
        {"\uE0D5", "\u2202"},
        {"\uE0D6", "\u2129"},
        {"\uE0D7", "\u2107"},
        {"\uE0D8", "\u2127"},
        {"\uE0D9", "\u22A4"},
        {"\uE0DA", "\u019B"},
        {"\uE0DB", "\u2190"},
        {"\uE0DC", "\u2191"},
        {"\uE0DD", "\u2193"},
    };

    /**
     * @param string String for char replacing
     * @return reuslt string
     */
    public static String convert(String string) {
        StringBuffer buffer = new StringBuffer();

        int i, j;
        boolean touched;
        for (i = 0; i < string.length(); i++) {
            touched = false;
            for (j = 0; j < MAP.length; j++) {
                if (string.regionMatches(i, MAP[j][0], 0, MAP[j][0].length())) {
                    buffer.append(MAP[j][1]);
                    touched = true;
                    i += (MAP[j][0].length() - 1);
                }
            }
            if (!touched) {
                buffer.append(string.charAt(i));
            }
        }

        return buffer.toString();
    }
}
          

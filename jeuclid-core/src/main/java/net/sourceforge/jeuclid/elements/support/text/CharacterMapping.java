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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.jeuclid.elements.support.attributes.FontFamily;
import net.sourceforge.jeuclid.elements.support.attributes.MathVariant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @version $Revision$
 */
public final class CharacterMapping implements Serializable {

    private static final int POS_MAPS = 5;

    private static final int POS_DESCRIPTION = 1;

    private static final int POS_CODESTR = 0;

    private static final int HIGHPLANE_MATH_CHARS_START = 0x1D400;

    private static final int HIGHPLANE_START = 0x10000;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static transient CharacterMapping instance;

    /**
     * Logger for this class.
     */
    private static final Log LOGGER = LogFactory
            .getLog(CharacterMapping.class);

    private final Map<Integer, CodePointAndVariant> extractAttrs;

    private final Set<Integer> forceSet;

    private final Map<FontFamily, Map<Integer, Integer[]>> composeAttrs;

    /**
     * Default Constructor.
     */
    private CharacterMapping() {
        // Map<Integer, CodePointAndVariant> eAttrs;
        // Map<FontFamily, Map<Integer, Integer[]>> cAttrs;
        // this.extractAttrs = eAttrs;
        // this.composeAttrs = cAttrs;

        this.extractAttrs = new TreeMap<Integer, CodePointAndVariant>();
        this.forceSet = new TreeSet<Integer>();
        this.composeAttrs = new EnumMap<FontFamily, Map<Integer, Integer[]>>(
                FontFamily.class);
        this.loadUnicodeData();
    }

    private void loadUnicodeData() {
        final InputStream is = CharacterMapping.class
                .getResourceAsStream("/UnicodeData.txt");
        final BufferedReader r = new BufferedReader(new InputStreamReader(is));
        String s;
        try {
            while ((s = r.readLine()) != null) {
                final String[] c = s.split(";");
                if (c.length > CharacterMapping.POS_MAPS) {
                    this.process(c[CharacterMapping.POS_CODESTR],
                            c[CharacterMapping.POS_DESCRIPTION],
                            c[CharacterMapping.POS_MAPS]);
                }
            }
        } catch (final IOException e) {
            CharacterMapping.LOGGER.warn(e.getMessage());
        }
    }

    private void process(final String codestr, final String descr,
            final String mapsStr) {
        try {
            final int codepoint = Integer.parseInt(codestr, 16);
            if (!mapsStr.startsWith("<font> ")) {
                return;
            }
            final int mapsTo = Integer.parseInt(mapsStr.substring(7), 16);

            final int awtStyle = this.parseAwtStyle(descr);

            final FontFamily fam = this.parseFontFamily(descr);

            if (fam == null) {
                return;
            }

            final boolean force = (codepoint >= CharacterMapping.HIGHPLANE_MATH_CHARS_START)
                    && ((FontFamily.SANSSERIF.equals(fam)) || (FontFamily.SERIF
                            .equals(fam)));
            if (force) {
                this.forceSet.add(codepoint);
            }
            final CodePointAndVariant cpav = new CodePointAndVariant(mapsTo,
                    new MathVariant(awtStyle, fam));

            this.extractAttrs.put(codepoint, cpav);

            final Map<Integer, Integer[]> ffmap = this.getFFMap(fam);

            final Integer[] ia = this.getMapsTo(mapsTo, ffmap);

            ia[awtStyle] = codepoint;
        } catch (final NumberFormatException nfe) {
            CharacterMapping.LOGGER.debug(nfe.getMessage());
        }

    }

    private Integer[] getMapsTo(final int mapsTo,
            final Map<Integer, Integer[]> ffmap) {
        Integer[] ia = ffmap.get(mapsTo);
        if (ia == null) {
            ia = new Integer[Font.BOLD + Font.ITALIC + 1];
            ffmap.put(mapsTo, ia);
        }
        return ia;
    }

    private Map<Integer, Integer[]> getFFMap(final FontFamily fam) {
        Map<Integer, Integer[]> ffmap = this.composeAttrs.get(fam);
        if (ffmap == null) {
            ffmap = new TreeMap<Integer, Integer[]>();
            this.composeAttrs.put(fam, ffmap);
        }
        return ffmap;
    }

    private int parseAwtStyle(final String descr) {
        int awtStyle = Font.PLAIN;
        if (descr.contains("BOLD")) {
            awtStyle += Font.BOLD;
        }
        if (descr.contains("ITALIC")) {
            awtStyle += Font.ITALIC;
        }
        return awtStyle;
    }

    private FontFamily parseFontFamily(final String descr) {
        final FontFamily fam;
        if (descr.contains("DOUBLE-STRUCK")) {
            fam = FontFamily.DOUBLE_STRUCK;
        } else if (descr.contains("SCRIPT")) {
            fam = FontFamily.SCRIPT;
        } else if (descr.contains("BLACK-LETTER")
                || descr.contains("FRAKTUR")) {
            fam = FontFamily.FRAKTUR;
        } else if (descr.contains("SANS-SERIF")) {
            fam = FontFamily.SANSSERIF;
        } else if (descr.contains("MONOSPACE")) {
            fam = FontFamily.MONOSPACED;
        } else if (descr.contains("MATHEMATICAL")) {
            fam = FontFamily.SERIF;
        } else {
            fam = null;
        }
        return fam;
    }

    /**
     * Get the singleton instance of this class.
     * 
     * @return an instance of CharacterMapping.
     */
    public static synchronized CharacterMapping getInstance() {
        if (CharacterMapping.instance == null) {
            CharacterMapping m;
            try {
                final InputStream is = CharacterMapping.class
                        .getResourceAsStream("/charmap.ser");
                final ObjectInput oi = new ObjectInputStream(is);
                m = (CharacterMapping) oi.readObject();
                oi.close();
            } catch (final ClassNotFoundException cnfe) {
                m = null;
            } catch (final IllegalArgumentException e) {
                m = null;
            } catch (final IOException e) {
                m = null;
            } catch (final NullPointerException e) {
                m = null;
            }
            if (m == null) {
                CharacterMapping.instance = new CharacterMapping();
            } else {
                CharacterMapping.instance = m;
            }
        }
        return CharacterMapping.instance;
    }

    /**
     * Compose a new SERIF Unicode char. This function tries to compose the
     * given char into a SERIF char which shows the same characteristics at a
     * particular Unicode codepoint.
     * 
     * @param split
     *            the char which contains a coidepoint and variant.
     * @param forbidHighplane
     *            if the high plane is broken (e.g. on OS X).
     * @return a CodePointAndVariant representing the same char.
     */
    public CodePointAndVariant composeUnicodeChar(
            final CodePointAndVariant split, final boolean forbidHighplane) {
        final MathVariant splitVariant = split.getVariant();
        final Map<Integer, Integer[]> famList = this.composeAttrs
                .get(splitVariant.getFontFamily());
        if (famList == null) {
            return split;
        }
        final Integer[] aList = famList.get(split.getCodePoint());
        if (aList == null) {
            return split;
        }

        final int splitStyle = splitVariant.getAwtStyle();
        Integer to = aList[splitStyle];
        if (to != null) {
            if (forbidHighplane && to >= CharacterMapping.HIGHPLANE_START) {
                return split;
            }
            return new CodePointAndVariant(to, MathVariant.NORMAL);
        }
        if (splitStyle != 0) {
            to = aList[0];
        }
        if (to != null) {
            if (forbidHighplane && to >= CharacterMapping.HIGHPLANE_START) {
                return split;
            }
            return new CodePointAndVariant(to, new MathVariant(splitStyle,
                    FontFamily.SERIF));
        }
        return split;

    }

    /**
     * Extract the given char into variant and codepoint.
     * 
     * @param test
     *            the Unicode char to split up.
     * @return A {@link CodePointAndVariant} representing the same character
     *         with explicit variant.
     */
    public CodePointAndVariant extractUnicodeAttr(
            final CodePointAndVariant test) {
        final CodePointAndVariant mapsTo = this.extractAttrs.get(test
                .getCodePoint());
        if (mapsTo == null) {
            return test;
        }
        final MathVariant testVariant = test.getVariant();
        final int testStyle = testVariant.getAwtStyle();
        final int mapsToCodepoint = mapsTo.getCodePoint();
        if ((testStyle == Font.PLAIN)
                || (this.forceSet.contains(mapsToCodepoint))) {
            return mapsTo;
        }
        final MathVariant mapsToVariant = mapsTo.getVariant();
        return new CodePointAndVariant(mapsToCodepoint, new MathVariant(
                testStyle | mapsToVariant.getAwtStyle(), mapsToVariant
                        .getFontFamily()));
    }
}

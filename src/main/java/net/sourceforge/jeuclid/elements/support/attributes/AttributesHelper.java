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

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.JEuclidNode;
import net.sourceforge.jeuclid.elements.support.operatordict.OperatorDictionary;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class contains utility methods for working with elements attributes.
 * 
 * @author Unknown
 * @author Max Berger
 * @version $Revision$
 */
public final class AttributesHelper {

    /**
     * Constant for "Transparent" color.
     */
    public static final String COLOR_TRANSPARENT = "transparent";

    /**
     * Width of veryverythinmath space according to 3.3.4.2.
     */
    public static final String VERYVERYTHINMATHSPACE = "0.0555556em";

    /**
     * Width of verythinmath space according to 3.3.4.2.
     */
    public static final String VERYTHINMATHSPACE = "0.111111em";

    /**
     * Width of thinmath space according to 3.3.4.2.
     */
    public static final String THINMATHSPACE = "0.166667em";

    /**
     * Width of mediummath space according to 3.3.4.2.
     */
    public static final String MEDIUMMATHSPACE = "0.222222em";

    /**
     * Width of tickmath space according to 3.3.4.2.
     */
    public static final String THICKMATHSPACE = "0.277778em";

    /**
     * Width of verytickmath space according to 3.3.4.2.
     */
    public static final String VERYTHICKMATHSPACE = "0.333333em";

    /**
     * Width of veryverytickmath space according to 3.3.4.2.
     */
    public static final String VERYVERYTHICKMATHSPACE = "0.388889em";

    /**
     * Unit for pt.
     */
    public static final String PT = "pt";

    /**
     * Infinity. Should be reasonably large.
     */
    public static final String INFINITY = "9999999pt";

    private static final int HASHSHORT_ALPHA = 5;

    private static final int HASHSHORT_NO_ALPHA = 4;

    private static final int HASHLEN_ALPHA = 9;

    private static final int HASHLEN_NO_ALPHA = 7;

    private static final int HEXBASE = 16;

    private static final float MAX_HEXCHAR_AS_FLOAT = 15f;

    private static final int MAX_BYTE = 255;

    private static final float MAX_PERCENT_AS_FLOAT = 100.0f;

    private static final float MAX_BYTE_AS_FLOAT = 255f;

    private static final String PERCENT_SIGN = "%";

    /**
     * Value of EM (horizontal size).
     * <p>
     * Please note: This is a typical value, according to
     * http://kb.mozillazine.org/Em_vs._ex It is not dependend on the actual
     * font used, as it should be.
     */
    private static final double EM = 0.83888888888888888888;

    /**
     * Value of EX (vertical size).
     * <p>
     * Please note: This is a typical value, according to
     * http://kb.mozillazine.org/Em_vs._ex It is not dependend on the actual
     * font used, as it should be.
     */
    private static final double EX = 0.5;

    /**
     * Default DPI value for all Java apps.
     */
    private static final double DPI = 72.0;

    private static final double PERCENT = 0.01;

    private static final double CM_PER_INCH = 2.54;

    private static final double MM_PER_INCH = AttributesHelper.CM_PER_INCH * 10;

    private static final double PT_PER_PC = 12.0;

    private static final Map<String, String> SIZETRANSLATIONS = new HashMap<String, String>();

    private static final Map<String, Double> RELATIVE_UNITS = new HashMap<String, Double>();

    private static final Map<String, Double> ABSOLUTE_UNITS = new HashMap<String, Double>();

    private static final Map<String, Color> COLOR_MAPPINGS = new HashMap<String, Color>();

    /**
     * Logger for this class.
     */
    private static final Log LOGGER = LogFactory
            .getLog(AbstractJEuclidElement.class);

    /**
     * Private constructor: it's forbidden to create instance of this utility
     * class.
     */
    private AttributesHelper() {
    }

    /**
     * Translates size into pt.
     * 
     * @param sizeString
     *            string to convert
     * @param contextElement
     *            Element this size is relative to. This is usually the parent
     *            or the element itself.
     * @param defaultUnit
     *            default Unit to use in this context. May be px, pt, em, etc.
     * @return Translated value of the size attribute into Point (=Java
     *         Pixels).
     */
    public static float convertSizeToPt(final String sizeString,
            final JEuclidNode contextElement, final String defaultUnit) {
        if (sizeString == null) {
            return 0;
        }
        String tSize = sizeString.trim().toLowerCase();

        final String translatesTo = AttributesHelper.SIZETRANSLATIONS
                .get(tSize);
        if (translatesTo != null) {
            tSize = translatesTo;
        }
        if (tSize.endsWith(AttributesHelper.PERCENT_SIGN)) {
            // A nice trick because all other units are exactly 2 chars long.
            tSize += " ";
        }

        double retVal;
        try {

            final String unit;
            final double value;
            if (tSize.length() <= 2) {
                unit = defaultUnit;
                value = Double.parseDouble(tSize);
            } else {
                final int valueLen = tSize.length() - 2;
                unit = tSize.substring(valueLen);
                value = Double.parseDouble(tSize.substring(0, valueLen));
            }
            if (value == 0) {
                retVal = 0.0;
            } else if (AttributesHelper.RELATIVE_UNITS.containsKey(unit)) {
                retVal = value * contextElement.getFontsizeInPoint()
                        * AttributesHelper.RELATIVE_UNITS.get(unit);
            } else if (AttributesHelper.ABSOLUTE_UNITS.containsKey(unit)) {
                retVal = value * AttributesHelper.ABSOLUTE_UNITS.get(unit);
            } else if (defaultUnit.length() > 0) {
                retVal = AttributesHelper.convertSizeToPt(sizeString
                        + defaultUnit, contextElement, "");
            } else {
                retVal = Double.parseDouble(tSize);
                AttributesHelper.LOGGER.warn("Error Parsing attribute: "
                        + sizeString + " assuming " + retVal
                        + AttributesHelper.PT);
            }
        } catch (final NumberFormatException nfe) {
            retVal = 1.0;
            AttributesHelper.LOGGER.warn("Error Parsing number: "
                    + sizeString + " faling back to " + retVal
                    + AttributesHelper.PT);
        }
        return (float) retVal;
    }

    /**
     * Creates a color from a given string.
     * <p>
     * This function supports a wide variety of inputs.
     * <ul>
     * <li>#RGB (hex 0..f)</li>
     * <li>#RGBA (hex 0..f)</li>
     * <li>#RRGGBB (hex 00..ff)</li>
     * <li>#RRGGBBAA (hex 00..ff)</li>
     * <li>rgb(r,g,b) (0..255 or 0%..100%)</li>
     * <li>java.awt.Color[r=r,g=g,b=b] (0..255)</li>
     * <li>system-color(colorname)</li>
     * <li>transparent</li>
     * <li>colorname</li>
     * </ul>
     * 
     * @param value
     *            the string to parse.
     * @return a Color representing the string if possible
     * @param defaultValue
     *            a default color to use in case of failure.
     */
    public static Color stringToColor(final String value,
            final Color defaultValue) {
        if (value == null) {
            return null;
        }

        final String lowVal = value.toLowerCase();
        Color parsedColor = null;

        if (!AttributesHelper.COLOR_MAPPINGS.containsKey(lowVal)) {
            if (value.startsWith("#")) {
                parsedColor = AttributesHelper.parseWithHash(value);
            } else if (value.startsWith("rgb(")) {
                parsedColor = AttributesHelper.parseAsRGB(value);
            } else if (value.startsWith("java.awt.Color")) {
                parsedColor = AttributesHelper.parseAsJavaAWTColor(value);
            }

            if (parsedColor == null) {
                parsedColor = defaultValue;
            }

            AttributesHelper.COLOR_MAPPINGS.put(value, parsedColor);
        } else {
            parsedColor = AttributesHelper.COLOR_MAPPINGS.get(lowVal);
        }
        return parsedColor;
    }

    /**
     * Tries to parse the standard java.awt.Color toString output.
     * 
     * @param value
     *            the complete line
     * @return a color if possible
     * @see java.awt.Color#toString()
     */
    private static Color parseAsJavaAWTColor(final String value) {
        float red = 0.0f;
        float green = 0.0f;
        float blue = 0.0f;
        final int poss = value.indexOf("[");
        final int pose = value.indexOf("]");
        try {
            if (poss != -1 && pose != -1) {
                final StringTokenizer st = new StringTokenizer(value
                        .substring(poss + 1, pose), ",");
                if (st.hasMoreTokens()) {
                    final String str = st.nextToken().trim();
                    red = Float.parseFloat(str.substring(2))
                            / AttributesHelper.MAX_BYTE_AS_FLOAT;
                }
                if (st.hasMoreTokens()) {
                    final String str = st.nextToken().trim();
                    green = Float.parseFloat(str.substring(2))
                            / AttributesHelper.MAX_BYTE_AS_FLOAT;
                }
                if (st.hasMoreTokens()) {
                    final String str = st.nextToken().trim();
                    blue = Float.parseFloat(str.substring(2))
                            / AttributesHelper.MAX_BYTE_AS_FLOAT;
                } else {
                    throw new NumberFormatException();
                }
                if ((red < 0.0 || red > 1.0) || (green < 0.0 || green > 1.0)
                        || (blue < 0.0 || blue > 1.0)) {
                    throw new NumberFormatException();
                }
            } else {
                throw new NumberFormatException();
            }
        } catch (final NumberFormatException e) {
            return null;
        }
        return new Color(red, green, blue);
    }

    /**
     * Parse a color given with the rgb() function.
     * 
     * @param value
     *            the complete line
     * @return a color if possible
     */
    private static Color parseAsRGB(final String value) {
        Color parsedColor;
        final int poss = value.indexOf("(");
        final int pose = value.indexOf(")");
        if (poss != -1 && pose != -1) {
            final StringTokenizer st = new StringTokenizer(value.substring(
                    poss + 1, pose), ",");
            try {
                float red = 0.0f;
                float green = 0.0f;
                float blue = 0.0f;
                if (st.hasMoreTokens()) {
                    final String str = st.nextToken().trim();
                    if (str.endsWith(AttributesHelper.PERCENT_SIGN)) {
                        red = Float.parseFloat(str.substring(0,
                                str.length() - 1))
                                / AttributesHelper.MAX_PERCENT_AS_FLOAT;
                    } else {
                        red = Float.parseFloat(str)
                                / AttributesHelper.MAX_BYTE_AS_FLOAT;
                    }
                }
                if (st.hasMoreTokens()) {
                    final String str = st.nextToken().trim();
                    if (str.endsWith(AttributesHelper.PERCENT_SIGN)) {
                        green = Float.parseFloat(str.substring(0, str
                                .length() - 1))
                                / AttributesHelper.MAX_PERCENT_AS_FLOAT;
                    } else {
                        green = Float.parseFloat(str)
                                / AttributesHelper.MAX_BYTE_AS_FLOAT;
                    }
                }
                if (st.hasMoreTokens()) {
                    final String str = st.nextToken().trim();
                    if (str.endsWith(AttributesHelper.PERCENT_SIGN)) {
                        blue = Float.parseFloat(str.substring(0,
                                str.length() - 1))
                                / AttributesHelper.MAX_PERCENT_AS_FLOAT;
                    } else {
                        blue = Float.parseFloat(str)
                                / AttributesHelper.MAX_BYTE_AS_FLOAT;
                    }
                }
                if ((red < 0.0 || red > 1.0) || (green < 0.0 || green > 1.0)
                        || (blue < 0.0 || blue > 1.0)) {
                    parsedColor = null;
                } else {
                    parsedColor = new Color(red, green, blue);
                }
            } catch (final NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
        return parsedColor;
    }

    /**
     * parse a color given in the #.... format.
     * 
     * @param value
     *            the complete line
     * @return a color if possible
     */
    private static Color parseWithHash(final String value) {
        Color parsedColor = null;
        try {
            final int len = value.length();
            if ((len >= AttributesHelper.HASHSHORT_NO_ALPHA)
                    && (len <= AttributesHelper.HASHSHORT_ALPHA)) {
                // note: divide by 15 so F = FF = 1 and so on
                final float red = Integer.parseInt(value.substring(1, 2),
                        AttributesHelper.HEXBASE)
                        / AttributesHelper.MAX_HEXCHAR_AS_FLOAT;
                final float green = Integer.parseInt(value.substring(2, 3),
                        AttributesHelper.HEXBASE)
                        / AttributesHelper.MAX_HEXCHAR_AS_FLOAT;
                final float blue = Integer.parseInt(value.substring(3,
                        AttributesHelper.HASHSHORT_NO_ALPHA),
                        AttributesHelper.HEXBASE)
                        / AttributesHelper.MAX_HEXCHAR_AS_FLOAT;
                float alpha = 1.0f;
                if (len == AttributesHelper.HASHSHORT_ALPHA) {
                    alpha = Integer.parseInt(value
                            .substring(AttributesHelper.HASHSHORT_NO_ALPHA),
                            AttributesHelper.HEXBASE)
                            / AttributesHelper.MAX_HEXCHAR_AS_FLOAT;
                }
                parsedColor = new Color(red, green, blue, alpha);
            } else if ((len == AttributesHelper.HASHLEN_NO_ALPHA)
                    || (len == AttributesHelper.HASHLEN_ALPHA)) {
                final int red = Integer.parseInt(value.substring(1, 3),
                        AttributesHelper.HEXBASE);
                final int green = Integer.parseInt(value.substring(3, 5),
                        AttributesHelper.HEXBASE);
                final int blue = Integer.parseInt(value.substring(5,
                        AttributesHelper.HASHLEN_NO_ALPHA),
                        AttributesHelper.HEXBASE);
                int alpha = AttributesHelper.MAX_BYTE;
                if (len == AttributesHelper.HASHLEN_ALPHA) {
                    alpha = Integer.parseInt(value
                            .substring(AttributesHelper.HASHLEN_NO_ALPHA),
                            AttributesHelper.HEXBASE);
                }
                parsedColor = new Color(red, green, blue, alpha);
            } else {
                throw new NumberFormatException();
            }
        } catch (final NumberFormatException e) {
            return null;
        }
        return parsedColor;
    }

    /**
     * Creates a re-parsable string representation of the given color.
     * <p>
     * First, the color will be converted into the sRGB colorspace. It will
     * then be printed as #rrggbb, or as #rrrggbbaa if an alpha value is
     * present.
     * 
     * @param color
     *            the color to represent.
     * @return a re-parsable string representadion.
     */
    public static String colorTOsRGBString(final Color color) {
        if (color == null) {
            return AttributesHelper.COLOR_TRANSPARENT;
        }
        final StringBuffer sbuf = new StringBuffer(10);
        sbuf.append('#');
        String s = Integer.toHexString(color.getRed());
        if (s.length() == 1) {
            sbuf.append('0');
        }
        sbuf.append(s);
        s = Integer.toHexString(color.getGreen());
        if (s.length() == 1) {
            sbuf.append('0');
        }
        sbuf.append(s);
        s = Integer.toHexString(color.getBlue());
        if (s.length() == 1) {
            sbuf.append('0');
        }
        sbuf.append(s);
        if (color.getAlpha() != AttributesHelper.MAX_BYTE) {
            s = Integer.toHexString(color.getAlpha());
            if (s.length() == 1) {
                sbuf.append('0');
            }
            sbuf.append(s);
        }
        return sbuf.toString();

    }

    // CHECKSTYLE:OFF
    static {

        // Mostly taken from 2.4.4.2
        AttributesHelper.SIZETRANSLATIONS.put(
                OperatorDictionary.NAME_VERYVERYTHINMATHSPACE,
                AttributesHelper.VERYVERYTHINMATHSPACE);
        AttributesHelper.SIZETRANSLATIONS.put(
                OperatorDictionary.NAME_VERYTHINMATHSPACE,
                AttributesHelper.VERYTHINMATHSPACE);
        AttributesHelper.SIZETRANSLATIONS.put(
                OperatorDictionary.NAME_THINMATHSPACE,
                AttributesHelper.THINMATHSPACE);
        AttributesHelper.SIZETRANSLATIONS.put(
                OperatorDictionary.NAME_MEDIUMMATHSPACE,
                AttributesHelper.MEDIUMMATHSPACE);
        AttributesHelper.SIZETRANSLATIONS.put(
                OperatorDictionary.NAME_THICKMATHSPACE,
                AttributesHelper.THICKMATHSPACE);
        AttributesHelper.SIZETRANSLATIONS.put(
                OperatorDictionary.NAME_VERYTHICKMATHSPACE,
                AttributesHelper.VERYTHICKMATHSPACE);
        AttributesHelper.SIZETRANSLATIONS.put(
                OperatorDictionary.NAME_VERYVERYTHICKMATHSPACE,
                AttributesHelper.VERYVERYTHICKMATHSPACE);
        AttributesHelper.SIZETRANSLATIONS.put(
                OperatorDictionary.NAME_INFINITY, AttributesHelper.INFINITY);
        AttributesHelper.SIZETRANSLATIONS.put("small", "68%");
        AttributesHelper.SIZETRANSLATIONS.put("normal", "100%");
        AttributesHelper.SIZETRANSLATIONS.put("big", "147%");

        // For mfrac, as of 3.3.2.2
        AttributesHelper.SIZETRANSLATIONS.put("thin", "0.5");
        AttributesHelper.SIZETRANSLATIONS.put("medium", "1");
        AttributesHelper.SIZETRANSLATIONS.put("thick", "2");

        AttributesHelper.SIZETRANSLATIONS.put("null", "0");

        AttributesHelper.RELATIVE_UNITS.put("em", AttributesHelper.EM);
        AttributesHelper.RELATIVE_UNITS.put("ex", AttributesHelper.EX);
        AttributesHelper.RELATIVE_UNITS.put("% ", AttributesHelper.PERCENT);

        AttributesHelper.ABSOLUTE_UNITS.put("px", 1.0);
        AttributesHelper.ABSOLUTE_UNITS.put("in", AttributesHelper.DPI);
        AttributesHelper.ABSOLUTE_UNITS.put("cm", AttributesHelper.DPI
                / AttributesHelper.CM_PER_INCH);
        AttributesHelper.ABSOLUTE_UNITS.put("mm", AttributesHelper.DPI
                / AttributesHelper.MM_PER_INCH);
        AttributesHelper.ABSOLUTE_UNITS.put(AttributesHelper.PT, 1.0);
        AttributesHelper.ABSOLUTE_UNITS.put("pc", AttributesHelper.PT_PER_PC);

        // Defined in 3.2.2.2
        AttributesHelper.COLOR_MAPPINGS.put("aqua", new Color(0, 255, 255));
        AttributesHelper.COLOR_MAPPINGS.put("black", Color.BLACK);
        AttributesHelper.COLOR_MAPPINGS.put("blue", Color.BLUE);
        AttributesHelper.COLOR_MAPPINGS
                .put("fuchsia", new Color(255, 0, 255));
        AttributesHelper.COLOR_MAPPINGS.put("gray", Color.GRAY);
        AttributesHelper.COLOR_MAPPINGS.put("green", Color.GREEN);
        AttributesHelper.COLOR_MAPPINGS.put("lime", new Color(0, 255, 0));
        AttributesHelper.COLOR_MAPPINGS.put("maroon", new Color(128, 0, 0));
        AttributesHelper.COLOR_MAPPINGS.put("navy", new Color(0, 0, 128));
        AttributesHelper.COLOR_MAPPINGS.put("olive", new Color(128, 128, 0));
        AttributesHelper.COLOR_MAPPINGS.put("purple", new Color(128, 0, 128));
        AttributesHelper.COLOR_MAPPINGS.put("red", Color.RED);
        AttributesHelper.COLOR_MAPPINGS.put("silver",
                new Color(192, 192, 192));
        AttributesHelper.COLOR_MAPPINGS.put("teal", new Color(0, 128, 128));
        AttributesHelper.COLOR_MAPPINGS.put("white", Color.WHITE);
        AttributesHelper.COLOR_MAPPINGS.put("yellow", Color.YELLOW);

        // Additional colors
        AttributesHelper.COLOR_MAPPINGS.put(
                AttributesHelper.COLOR_TRANSPARENT, null);
        AttributesHelper.COLOR_MAPPINGS.put("null", null);
        AttributesHelper.COLOR_MAPPINGS.put("", null);
    }
    // CHECKSTYLE:ON
}

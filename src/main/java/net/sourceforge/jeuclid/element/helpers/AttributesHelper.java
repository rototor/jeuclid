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

package net.sourceforge.jeuclid.element.helpers;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.generic.MathNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class contains utility methods for working with elements attributes.
 * 
 * @author Unknown
 * @author Max Berger
 */

public final class AttributesHelper {

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
    private static final String INFINITY = "9999999pt";

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
            .getLog(AbstractMathElement.class);

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
            final MathNode contextElement, final String defaultUnit) {
        if (sizeString == null) {
            return 0;
        }
        String tSize = sizeString.trim().toLowerCase();

        final String translatesTo = AttributesHelper.SIZETRANSLATIONS
                .get(tSize);
        if (translatesTo != null) {
            tSize = translatesTo;
        }
        if (tSize.endsWith("%")) {
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
     * Converts a given String to a color.
     * 
     * @param value
     *            the stringValue
     * @param defaultValue
     *            a default color to use in case of failure.
     * @return java.awt.Color for this string.
     */
    public static Color stringToColor(final String value,
            final Color defaultValue) {

        if (value == null) {
            return defaultValue;
        }

        Color retVal = AttributesHelper.COLOR_MAPPINGS.get(value
                .toLowerCase());

        if (retVal == null) {
            try {
                if ((value.startsWith("#")) && (value.length() == 4)) {
                    retVal = Color.decode("#" + value.charAt(1) + "0"
                            + value.charAt(2) + "0" + value.charAt(3) + "0");
                } else {
                    retVal = Color.decode(value);
                }
                AttributesHelper.COLOR_MAPPINGS.put(value, retVal);
            } catch (final NumberFormatException nfe) {
                retVal = defaultValue;
            }
        }
        return retVal;
    }

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
        AttributesHelper.COLOR_MAPPINGS.put("transparent", null);
        AttributesHelper.COLOR_MAPPINGS.put("null", null);
        AttributesHelper.COLOR_MAPPINGS.put("", null);
    }

}

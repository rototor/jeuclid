/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.test;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;

import net.sourceforge.jeuclid.Constants;
import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.Display;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.elements.generic.DocumentElement;
import net.sourceforge.jeuclid.elements.generic.MathImpl;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test for {@link net.sourceforge.jeuclid.context.Parameter} enum.
 * 
 * @version $Revision$
 */
public class LayoutContextParamTest {
    /**
     * Default Constructor.
     */
    public LayoutContextParamTest() {
        // Empty on purpose
    }

    /**
     * Tests implementation of {@link Parameter#valid(Object)} method.
     */
    @Test
    public void testValid() {
        Assert.assertTrue(Parameter.DEBUG.valid(Boolean.TRUE));
        Assert.assertFalse(Parameter.DEBUG.valid("true"));
        Assert.assertTrue(Parameter.DISPLAY.valid(Display.BLOCK));
        Assert.assertFalse(Parameter.DISPLAY.valid(null));
        Assert.assertTrue(Parameter.FONTS_SERIF.valid(Collections
                .singletonList("Arial")));
        Assert.assertFalse(Parameter.FONTS_SERIF.valid("Courier"));
        Assert.assertTrue(Parameter.MATHBACKGROUND.valid(Color.RED));
        Assert.assertTrue(Parameter.MATHBACKGROUND.valid(null));
        Assert.assertTrue(Parameter.MATHSIZE.valid(Float.valueOf(1)));
        Assert.assertFalse(Parameter.MATHSIZE.valid(Integer.valueOf(1)));
        Assert.assertTrue(Parameter.SCRIPTLEVEL.valid(Integer.valueOf(1)));
        Assert.assertFalse(Parameter.SCRIPTLEVEL.valid(Float.valueOf(1)));
    }

    /**
     * Tests implementation of {@link Parameter#fromString(String)} method.
     */
    @Test
    public void testFromString() {
        Assert.assertEquals(Parameter.DEBUG.fromString("true"), Boolean.TRUE);
        Assert.assertEquals(Parameter.DEBUG.fromString(null), null);
        Assert.assertEquals(Parameter.DISPLAY.fromString("BLOCK"),
                Display.BLOCK);
        try {
            Parameter.DISPLAY.fromString("foo");
            Assert.fail();
        } catch (final IllegalArgumentException e) {
        }
        Assert.assertEquals(Parameter.FONTS_SERIF.fromString("Arial"),
                Collections.singletonList("arial"));
        Assert.assertEquals(
                Parameter.FONTS_SERIF.fromString("Arial,Helvetica"), Arrays
                        .asList(new String[] { "arial", "helvetica" }));
        Assert.assertEquals(Parameter.FONTS_SERIF.fromString("Foo, Bar"),
                Arrays.asList(new String[] { "foo", "bar" }));
        Assert.assertEquals(Parameter.MATHCOLOR.fromString("RED"), Color.RED);
        try {
            Parameter.MATHCOLOR.fromString("violet");
            Assert.fail();
        } catch (final IllegalArgumentException e) {
        }
        Assert.assertEquals(Parameter.MATHCOLOR.fromString("rgb(100,100,100)"),
                new Color(100, 100, 100));
        Assert.assertEquals(Parameter.MATHSIZE.fromString("0.5"), Float
                .valueOf(0.5f));
        Assert.assertEquals(Parameter.SCRIPTLEVEL.fromString("1"), Integer
                .valueOf(1));
    }

    /**
     * Tests implementation of {@link Parameter#toString(String)} method.
     */
    @Test
    public void testToString() {
        Assert.assertEquals(Parameter.DEBUG.toString(Boolean.TRUE), "true");
        Assert.assertEquals(Parameter.DEBUG.toString(null), null);
        Assert.assertEquals(Parameter.DISPLAY.toString(Display.INLINE),
                "INLINE");
        Assert.assertEquals(Parameter.FONTS_SERIF.toString(Collections
                .singletonList("font")), "font");
        Assert.assertEquals(Parameter.FONTS_SERIF.toString(Arrays
                .asList(new String[] { "f1", "f2" })), "f1, f2");
        Assert.assertEquals(Parameter.MATHCOLOR.toString(Color.RED), "red");
        Assert.assertEquals(Parameter.MATHCOLOR.toString(null), null);
        Assert.assertEquals(
                Parameter.MATHCOLOR.toString(new Color(32, 32, 32)), "#202020");
        Assert.assertEquals(Parameter.MATHSIZE.toString(Float.valueOf(0.5f)),
                "0.5");
        Assert.assertEquals(Parameter.SCRIPTLEVEL.toString(Integer.valueOf(2)),
                "2");
    }

    /**
     * Test parameter passing from String.
     */
    @Test
    public void testParamFromOldNSProgrammatically() {
        final MathImpl mi = new MathImpl(MathImpl.ELEMENT,
                new DocumentElement());
        final LayoutContext defaultContext = LayoutContextImpl
                .getDefaultLayoutContext();
        final LayoutContext testContext1 = mi.getChildLayoutContext(0,
                defaultContext);
        Assert.assertEquals(testContext1.getParameter(Parameter.MATHSIZE),
                12.0f);
        mi.setAttributeNS(Constants.NS_OLD_JEUCLID_EXT, "jeuclid:fontSize",
                "13");
        Assert.assertEquals(testContext1.getParameter(Parameter.MATHSIZE),
                13.0f);
        // Case sensitive!
        mi.setAttributeNS(Constants.NS_OLD_JEUCLID_EXT, "jeuclid:fontsize",
                "14");
        Assert.assertEquals(testContext1.getParameter(Parameter.MATHSIZE),
                13.0f);
    }

    /**
     * Test parameter passing from String.
     */
    @Test
    public void testParamFromNSProgrammatically() {
        final MathImpl mi = new MathImpl(MathImpl.ELEMENT,
                new DocumentElement());
        final LayoutContext defaultContext = LayoutContextImpl
                .getDefaultLayoutContext();
        final LayoutContext testContext1 = mi.getChildLayoutContext(0,
                defaultContext);
        Assert.assertEquals(testContext1.getParameter(Parameter.MATHSIZE),
                12.0f);
        mi.setAttributeNS(Constants.NS_JEUCLID_EXT, "jeuclid:fontSize", "13");
        Assert.assertEquals(testContext1.getParameter(Parameter.MATHSIZE),
                13.0f);
        // Case sensitive!
        mi.setAttributeNS(Constants.NS_JEUCLID_EXT, "jeuclid:fontsize", "14");
        Assert.assertEquals(testContext1.getParameter(Parameter.MATHSIZE),
                13.0f);
    }
}

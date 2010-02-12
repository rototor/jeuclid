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

package net.sourceforge.jeuclid.test;

import net.sourceforge.jeuclid.elements.support.text.CharConverter;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @version $Revision$
 */
public class CharacterConverterTest {
    /**
     * Default Constructor.
     */
    public CharacterConverterTest() {
        // Empty on purpose
    }

    /**
     * Tests late character conversion.
     * @throws Exception if the test fails.
     */
    @Test
    public void testLate() throws Exception {
        Assert.assertEquals(CharConverter.convertLate("abcdef"), "abcdef");
        Assert.assertEquals(CharConverter
                .convertLate("x\u2061\u200b\u2062\u2148x"), "xx");
        Assert.assertEquals(CharConverter.convertLate("\u0332"), "\u00AF");
    }

    /**
     * Tests early character conversion.
     * @throws Exception if the test fails.
     */
    @Test
    public void testEarly() throws Exception {
        Assert.assertEquals(CharConverter.convertEarly("abcdef"), "abcdef");
        Assert.assertEquals(CharConverter.convertEarly("x\uE080x"), "x\u2031x");
    }
}

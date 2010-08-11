/*
 * Copyright 2010 - 2010 JEuclid, http://jeuclid.sf.net
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

import net.sourceforge.jeuclid.elements.support.text.StringUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link StringUtil}.
 *
 * @version $Revision$
 */
public class StringUtilTest {

    /** Default constructor. */
    public StringUtilTest() {
        // nothing to do.
    }

    /**
     * Test for {@link StringUtil#countDisplayableCharacters(String)}.
     */
    @Test
    public void testCountDisplayableCharacters() {
        Assert.assertEquals(StringUtil.countDisplayableCharacters(null), 0);
        Assert.assertEquals(StringUtil.countDisplayableCharacters(""), 0);
        Assert.assertEquals(StringUtil.countDisplayableCharacters("a"), 1);
        Assert.assertEquals(StringUtil.countDisplayableCharacters("abc"), 3);

        // High surrogate
        Assert.assertEquals(
                StringUtil.countDisplayableCharacters("\uD834\uDD1E"), 1);

        // Combining mark
        Assert.assertEquals(StringUtil.countDisplayableCharacters("i\u0302"), 1);

    }
}

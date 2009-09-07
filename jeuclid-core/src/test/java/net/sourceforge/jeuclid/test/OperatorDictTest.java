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

package net.sourceforge.jeuclid.test;

import net.sourceforge.jeuclid.elements.support.operatordict.OperatorDictionary;
import net.sourceforge.jeuclid.elements.support.operatordict.OperatorDictionary2;
import net.sourceforge.jeuclid.elements.support.operatordict.OperatorDictionary3;
import net.sourceforge.jeuclid.elements.support.operatordict.UnknownAttributeException;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the OperatorDict class.
 * 
 * @version $Revision$
 */
public class OperatorDictTest {
    /**
     * Default Constructor.
     */
    public OperatorDictTest() {
        // Empty on purpose
    }

    /**
     * Test various operators for correct default values.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testAttrs2() throws Exception {
        final OperatorDictionary opDict = OperatorDictionary2.getInstance();
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u002d", "infix",
                "lspace"), "mediummathspace");
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u002d", "prefix",
                "lspace"), "0em");
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u002d",
                "postfix", "lspace"), "verythinmathspace");
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u0028", "prefix",
                "lspace"), "0em");
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u0028", "infix",
                "lspace"), "0em");
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u0028",
                "postfix", "lspace"), "0em");
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u2063", "infix",
                "rspace"), "0em");
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u2063",
                "postfix", "rspace"), "0em");
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u2063", "infix",
                "fence"), "false");
        try {
            opDict.getDefaultAttributeValue("\u2063", "infix", "foobar");
            Assert.fail("foobar should throw UnknownAttributeException!");
        } catch (final UnknownAttributeException uae) {
            // Ignore
        }
    }

    /**
     * Test various operators for correct default values.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testAttrs3() throws Exception {
        final OperatorDictionary opDict = OperatorDictionary3.getInstance();
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u002d", "infix",
                "lspace"), "mediummathspace");
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u002d", "prefix",
                "lspace"), "mediummathspace");
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u002d",
                "postfix", "lspace"), "mediummathspace");
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u0028", "prefix",
                "lspace"), "0em");
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u0028", "infix",
                "lspace"), "0em");
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u0028",
                "postfix", "lspace"), "0em");
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u2063", "infix",
                "rspace"), "0em");
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u2063",
                "postfix", "rspace"), "0em");
        Assert.assertEquals(opDict.getDefaultAttributeValue("\u2063", "infix",
                "fence"), "false");
        try {
            opDict.getDefaultAttributeValue("\u2063", "infix", "foobar");
            Assert.fail("foobar should throw UnknownAttributeException!");
        } catch (final UnknownAttributeException uae) {
            // Ignore
        }
    }
}

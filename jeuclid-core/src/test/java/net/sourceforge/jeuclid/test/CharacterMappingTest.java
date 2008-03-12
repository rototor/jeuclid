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

/* $Id: CharacterMappingTest.java $ */

package net.sourceforge.jeuclid.test;

import java.awt.Font;

import net.sourceforge.jeuclid.elements.support.attributes.FontFamily;
import net.sourceforge.jeuclid.elements.support.attributes.MathVariant;
import net.sourceforge.jeuclid.elements.support.text.CharacterMapping;
import net.sourceforge.jeuclid.elements.support.text.CodePointAndVariant;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @version $Revision: 000 $
 */
public class CharacterMappingTest {
    /**
     * Default Constructor.
     */
    public CharacterMappingTest() {
        // Empty on purpose
    }

    @Test
    public void testMappings() throws Exception {
        final CharacterMapping cMap = CharacterMapping.getInstance();
        final CodePointAndVariant test = new CodePointAndVariant(0x1D555,
                MathVariant.ITALIC);
        Assert.assertEquals(test, new CodePointAndVariant(0x1d555,
                MathVariant.ITALIC));
        final CodePointAndVariant split = cMap.extractUnicodeAttr(test);
        Assert.assertEquals(split, new CodePointAndVariant(0x64,
                new MathVariant(Font.ITALIC, FontFamily.DOUBLE_STRUCK)));
        final CodePointAndVariant fin = cMap.composeUnicodeChar(split, false);
        Assert.assertEquals(fin, new CodePointAndVariant(0x2146,
                MathVariant.NORMAL));
        final CodePointAndVariant fina = cMap.composeUnicodeChar(split, true);
        Assert.assertEquals(fina, new CodePointAndVariant(0x2146,
                MathVariant.NORMAL));

        final CodePointAndVariant test2 = new CodePointAndVariant(0x1D555,
                MathVariant.BOLD);
        Assert.assertEquals(test2, new CodePointAndVariant(0x1d555,
                MathVariant.BOLD));
        final CodePointAndVariant split2 = cMap.extractUnicodeAttr(test2);
        Assert.assertEquals(split2, new CodePointAndVariant(0x64,
                new MathVariant(Font.BOLD, FontFamily.DOUBLE_STRUCK)));
        final CodePointAndVariant fin2 = cMap.composeUnicodeChar(split2,
                false);
        Assert.assertEquals(fin2, new CodePointAndVariant(0x1d555,
                MathVariant.BOLD));
        final CodePointAndVariant fin2a = cMap.composeUnicodeChar(split2,
                true);
        Assert.assertEquals(fin2a, new CodePointAndVariant(0x64,
                new MathVariant(Font.BOLD, FontFamily.DOUBLE_STRUCK)));
    }
}

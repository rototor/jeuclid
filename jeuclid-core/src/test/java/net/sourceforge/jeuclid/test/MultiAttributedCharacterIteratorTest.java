/*
 * Copyright 2009 - 2009 JEuclid, http://jeuclid.sf.net
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

import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import net.sourceforge.jeuclid.elements.support.text.MultiAttributedCharacterIterator;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests {@link MultiAttributedCharacterIterator}
 * 
 * @version $Revision $
 */
public class MultiAttributedCharacterIteratorTest {

    /**
     * test without attributes.
     * 
     * @throws Exception
     *             if the tests fail.
     */
    @Test
    public void testNoAttribs() throws Exception {
        final AttributedString as1 = new AttributedString("Test");
        final AttributedString as2 = new AttributedString("Cont");
        final MultiAttributedCharacterIterator maci = new MultiAttributedCharacterIterator();
        maci.appendAttributedCharacterIterator(as1.getIterator());
        maci.appendAttributedCharacterIterator(as2.getIterator());

        Assert.assertTrue(maci.getAllAttributeKeys().isEmpty());
        Assert.assertTrue(maci.getAttributes().isEmpty());
        Assert.assertEquals(maci.getEndIndex(), 8);

        final AttributedCharacterIterator nac1 = new AttributedString(maci)
                .getIterator();
        final AttributedCharacterIterator nac2 = new AttributedString(
                "TestCont").getIterator();

        for (int i = nac1.getBeginIndex(); i < nac1.getEndIndex(); i++) {
            nac1.setIndex(i);
            nac2.setIndex(i);
            Assert.assertEquals(nac1.current(), nac2.current());
        }

    }
}

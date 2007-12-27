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

import java.awt.HeadlessException;

import net.sourceforge.jeuclid.swing.JMathComponent;
import net.sourceforge.jeuclid.swing.MathComponentUI;
import net.sourceforge.jeuclid.swing.MathComponentUI16;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the MathComponentUI16
 * 
 * @version $Revision$
 */
public class MathComponentUI16Test {

    @Test
    public void testComponentUI16() throws Exception {
        try {
            final JMathComponent jm = new JMathComponent();
            final MathComponentUI mcui = jm.getUI();
            Assert.assertTrue(mcui instanceof MathComponentUI16);
        } catch (final HeadlessException he) {
            // Ignore to make test work on servers.
        }
    }

}

/*
 * Copyright 2007- 2007 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import net.sourceforge.jeuclid.DOMMathBuilder;
import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.MathRow;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.mathml.MathMLMathElement;

/**
 * Various tests for the DOM model.
 * 
 * @author Max Berger
 */
public class DOMModelTests {

    /**
     * Tests is the "id" attribute works.
     * 
     * @throws Exception
     *             if anything goes wrong.
     */
    @Test
    public void testID() throws Exception {
        Document docWithID = ViewerTests
                .getDocument("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
                        + "<mrow id='abc'><mn>1</mn></mrow></math>");

        MathMLMathElement rootElement = new DOMMathBuilder(docWithID,
                new MathBase(MathBase.getDefaultParameters()))
                .getMathRootElement();
        assertNull(rootElement.getId());
        // System.out.println(rootElement);
        // System.out.println(rootElement.getFirstChild());
        // TODO: This should not be needed twice
        // TODO: This should use the w3c DOM interface instead 
        MathRow row = (MathRow) rootElement.getFirstChild().getFirstChild();
        assertNotNull(row);
        assertEquals(row.getId(), "abc");

    }
}

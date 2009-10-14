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

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sourceforge.jeuclid.ResourceEntityResolver;
import net.sourceforge.jeuclid.awt.MathComponent;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Tests the AWT math component.
 * 
 * @version $Revision$
 */
public class MathComponentTest {

    /**
     * Test string with xml header.
     */
    private static String test = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
            + "<mrow><munderover><mo>&#x0222B;</mo><mn>1</mn><mi>x</mi></munderover>"
            + "<mfrac><mi>dt</mi><mi>t</mi></mfrac></mrow></math>";

    /**
     * Tests if AWT component starts up.
     * @throws Exception if the test fails.
     */    
    @Test
    public void testAWTComponent() throws Exception {
        Document document;

        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        final DocumentBuilder parser = documentBuilderFactory
                .newDocumentBuilder();
        parser.setEntityResolver(new ResourceEntityResolver());
        document = parser.parse(new InputSource(new StringReader(
                MathComponentTest.test)));

        try {
            final Frame frame = new Frame("Test MathComponent");
            frame.setLayout(new BorderLayout());
            final MathComponent component = new MathComponent();
            component.setDocument(document);
            component.setDebug(false);
            frame.add(component, BorderLayout.CENTER);
            frame.setVisible(true);
            frame.pack();
            frame.invalidate();
            frame.validate();
            frame.dispose();
        } catch (final HeadlessException he) {
            // Ignore to make test work on servers.
        }
    }

}

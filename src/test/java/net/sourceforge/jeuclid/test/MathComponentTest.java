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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.File;
import java.io.StringReader;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.awt.MathComponent;
import net.sourceforge.jeuclid.util.MathMLParserSupport;
import net.sourceforge.jeuclid.util.ResourceEntityResolver;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Tests the AWT math component.
 * 
 * @author Unknown, Max Berger
 * 
 */
public class MathComponentTest {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(MathComponentTest.class);

    /**
     * Test string with xml header.
     */
    private static String test = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
            + "<mrow><munderover><mo>&#x0222B;</mo><mn>1</mn><mi>x</mi></munderover>"
            + "<mfrac><mi>dt</mi><mi>t</mi></mfrac></mrow></math>";

    /**
     * Main method.
     * 
     * @param args
     *            Command line arguments
     */
    public static void main(String[] args) {
        try {
            Document document;
            if (args.length > 0) {
                document = MathMLParserSupport.parseFile(new File(args[0]));
            } else {
                final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                        .newInstance();
                final DocumentBuilder parser = documentBuilderFactory
                        .newDocumentBuilder();
                parser.setEntityResolver(new ResourceEntityResolver());
                document = parser
                        .parse(new InputSource(new StringReader(test)));
            }
            Frame frame = new Frame("Test MathComponent");

            frame.setLayout(new BorderLayout());
            MathComponent component = new MathComponent();
            component.setDocument(document);
            component.setDebug(false);
            frame.add(component, BorderLayout.CENTER);
            frame.setVisible(true);
            frame.pack();
            // frame.invalidate();
            // frame.validate();

            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    System.exit(0);
                }
            });

        } catch (org.xml.sax.SAXException e) {
            logger.fatal("SAXException:" + e.getMessage(),e);
        } catch (java.io.IOException e) {
            logger.fatal("IOException:" + e.getMessage(),e);
        } catch (ParserConfigurationException e) {
            logger.fatal("ParserConfigurationException:" + e.getMessage(),e);
        }
    }

}

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

import java.awt.Font;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.jeuclid.DOMMathBuilder;
import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.util.ResourceEntityResolver;
import static org.testng.Assert.*;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A JUnit Test case for MathBase.
 * 
 * @author unknown, Max Berger
 */
public class MathBaseTest {

	/**
	 * Checks if Arial Unicode is installed. Should be obsoleted.
	 */
	@Test
	public void testArialUnicode() {
		final String font1Name = "Arial Unicode MS";
		Font font1 = new Font(font1Name, Font.PLAIN, 30);

		assertTrue(font1.getName().equals(font1Name),
				"Arial Unicode is not installed on this system");

	}

	public static Document loadDocument(String name)
			throws ParserConfigurationException, SAXException, IOException {
		final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		final DocumentBuilder parser = documentBuilderFactory
				.newDocumentBuilder();
		parser.setEntityResolver(new ResourceEntityResolver());

		Document document = null;
		System.out.println("reading:" + name);
		final InputSource source = new InputSource(MathBaseTest.class
				.getResourceAsStream(name));
		document = parser.parse(source);
		System.out.println(name + " loaded");
		return document;
	}

	/**
	 * Tests the examples at resources/test/exampleX.mml.
	 * 
	 * @throws Exception
	 *             if an error occurs.
	 */
	@Test
	public void testEmbeddedExamples() throws Exception {
		for (int example = 1; example <= 7; example++) {
			String exName = "example" + example + ".mml";
			Document document = loadDocument(exName);
			MathBase base = new MathBase(MathBase.getDefaultParameters());
			new DOMMathBuilder(document, base);
		}
	}

}

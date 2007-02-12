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

package net.sourceforge.jeuclid;

import java.util.Stack;

import net.sourceforge.jeuclid.element.MathIdentifier;
import net.sourceforge.jeuclid.element.MathNumber;
import net.sourceforge.jeuclid.element.MathOperator;
import net.sourceforge.jeuclid.element.MathOver;
import net.sourceforge.jeuclid.element.MathPhantom;
import net.sourceforge.jeuclid.element.MathRoot;
import net.sourceforge.jeuclid.element.MathRootElement;
import net.sourceforge.jeuclid.element.MathRow;
import net.sourceforge.jeuclid.element.MathSqrt;
import net.sourceforge.jeuclid.element.MathSub;
import net.sourceforge.jeuclid.element.MathSubSup;
import net.sourceforge.jeuclid.element.MathSup;
import net.sourceforge.jeuclid.element.MathTable;
import net.sourceforge.jeuclid.element.MathTableData;
import net.sourceforge.jeuclid.element.MathTableRow;
import net.sourceforge.jeuclid.element.MathText;
import net.sourceforge.jeuclid.element.MathUnder;
import net.sourceforge.jeuclid.element.MathUnderOver;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.helpers.AttributeMap;
import net.sourceforge.jeuclid.element.helpers.SAXAttributeMap;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;

/**
 * The generator for creating a MathElement tree.
 * 
 * @author <a href="mailto:stephan@vern.chem.tu-berlin.de">Stephan Michels</a>
 * @author Max Berger
 */
public class SAXMathBuilder implements ContentHandler {
    private MathRootElement rootElement;

    private Stack stack;

    private final MathBase m_base;

    /**
     * default constructor.
     */
    public SAXMathBuilder() {
        m_base = new MathBase(MathBase.getDefaultParameters());
    }

    /**
     * Returns the created math root element.
     * 
     * @return Math root element
     */
    public MathRootElement getMathRootElement() {
        return rootElement;
    }

    /**
     * Receive notification of character data.
     * 
     * @param ch
     *            The characters from the XML document.
     * @param start
     *            The start position in the array.
     * @param length
     *            The number of characters to read from the array.
     */
    public void characters(char[] ch, int start, int length) {
        if (!stack.empty()) {
            ((AbstractMathElement) stack.peek()).addText(new String(ch, start,
                    length));
        }
    }

    /**
     * Receive notification of the end of a document.
     */
    public void endDocument() {
    }

    /**
     * Receive notification of the end of an element.
     * 
     * @param namespaceURI
     *            The Namespace URI, or the empty string if the element has no
     *            Namespace URI or if Namespace processing is not being
     *            performed.
     * @param localName
     *            The local name (without prefix), or the empty string if
     *            Namespace processing is not being performed.
     * @param qName
     *            The qualified XML 1.0 name (with prefix), or the empty string
     *            if qualified names are not available.
     */
    public void endElement(String namespaceURI, String localName, String qName) {
        if (!stack.empty()) {
            AbstractMathElement element = (AbstractMathElement) stack.pop();

            if (stack.empty()) {
                try {
                    rootElement = (MathRootElement) element;
                } catch (ClassCastException e) {
                    System.err.println("ClassCastException MathRootElement"
                            + e.getMessage());
                }
            }
        }
    }

    /**
     * Receive notification of the beginning of a document.
     */
    public void startDocument() {
        rootElement = null;
        stack = new Stack();
    }

    private AbstractMathElement elementFromName(String localName,
            AttributeMap attributes) {
        if (localName.equals(MathRootElement.ELEMENT)) {
            MathRootElement element = new MathRootElement(m_base);
            return element;
        }
        if (localName.equals(MathPhantom.ELEMENT)) {
            return new MathPhantom(m_base);
        }
        if (localName.equals(MathSup.ELEMENT)) {
            return new MathSup(m_base);
        }
        if (localName.equals(MathSub.ELEMENT)) {
            return new MathSub(m_base);
        }
        if (localName.equals(MathSubSup.ELEMENT)) {
            return new MathSubSup(m_base);
        }
        if (localName.equals(MathUnder.ELEMENT)) {
            return new MathUnder(m_base);
        }
        if (localName.equals(MathOver.ELEMENT)) {
            return new MathOver(m_base);
        }
        if (localName.equals(MathUnderOver.ELEMENT)) {
            return new MathUnderOver(m_base);
        }
        if (localName.equals(MathSqrt.ELEMENT)) {
            return new MathSqrt(m_base);
        }
        if (localName.equals(MathRoot.ELEMENT)) {
            return new MathRoot(m_base);
        }
        if (localName.equals(MathTable.ELEMENT)) {
            return new MathTable(m_base);
        }
        if (localName.equals(MathTableRow.ELEMENT)) {
            return new MathTableRow(m_base);
        }
        if (localName.equals(MathTableData.ELEMENT)) {
            return new MathTableData(m_base);
        }
        if (localName.equals(MathOperator.ELEMENT)) {
            MathOperator element = new MathOperator(m_base);
            element.setStretchy(attributes.getBoolean(
                    MathOperator.ATTRIBUTE_STRETCHY, true));
            return element;
        }
        if (localName.equals(MathIdentifier.ELEMENT)) {
            return new MathIdentifier(m_base);
        }
        if (localName.equals(MathNumber.ELEMENT)) {
            return new MathNumber(m_base);
        }
        if (localName.equals(MathText.ELEMENT)) {
            return new MathText(m_base);
        }
        return new MathRow(m_base);
    }

    /**
     * Receive notification of the beginning of an element.
     * 
     * @param namespaceURI
     *            The Namespace URI, or the empty string if the element has no
     *            Namespace URI or if Namespace processing is not being
     *            performed.
     * @param localName
     *            The local name (without prefix), or the empty string if
     *            Namespace processing is not being performed.
     * @param qName
     *            The qualified name (with prefix), or the empty string if
     *            qualified names are not available.
     * @param attributes
     *            The attributes attached to the element. If there are no
     *            attributes, it shall be an empty Attributes object.
     */
    public void startElement(String namespaceURI, String localName,
            String qName, Attributes attributes) {
        if ((stack.empty()) && (!localName.equals(MathRootElement.ELEMENT))) {
            return;
        }

        AbstractMathElement element = elementFromName(localName,
                new SAXAttributeMap(attributes));

        if (!stack.empty()) {
            ((AbstractMathElement) stack.peek()).addMathElement(element);
        }

        stack.push(element);
    }

    /**
     * End the scope of a prefix-URI mapping.
     * 
     * @param prefix
     *            Prefix
     */
    public void endPrefixMapping(String prefix) {
    }

    /**
     * Receive notification of ignorable whitespace in element content.
     * 
     * @param ch
     *            Space char
     * @param start
     *            Start position
     * @param length
     *            Length
     */
    public void ignorableWhitespace(char[] ch, int start, int length) {
    }

    /**
     * Receive notification of a processing instruction.
     * 
     * @param target
     *            Target
     * @param data
     *            Data
     */
    public void processingInstruction(String target, String data) {
    }

    /**
     * Receive an object for locating the origin of SAX document events.
     * 
     * @param locator
     *            Locator
     */
    public void setDocumentLocator(Locator locator) {
    }

    /**
     * Receive notification of a skipped entity.
     * 
     * @param name
     *            Entity name
     */
    public void skippedEntity(String name) {
    }

    /**
     * Begin the scope of a prefix-URI Namespace mapping.
     * 
     * @param prefix
     *            Prefix
     * @param uri
     *            Uri
     */
    public void startPrefixMapping(String prefix, String uri) {
    }

}

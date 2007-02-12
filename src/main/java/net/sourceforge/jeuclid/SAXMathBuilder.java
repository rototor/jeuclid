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

import net.sourceforge.jeuclid.element.MathDocumentElement;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.helpers.AttributeMap;
import net.sourceforge.jeuclid.element.helpers.SAXAttributeMap;

import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLElement;
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
    private MathDocumentElement rootElement;

    private Stack<Node> stack;

    private final MathBase mbase;

    /**
     * Logger for this class.
     */
    // unused
    // private static final Log LOGGER =
    // LogFactory.getLog(SAXMathBuilder.class);
    /**
     * default constructor.
     */
    public SAXMathBuilder() {
        this.mbase = new MathBase(MathBase.getDefaultParameters());
    }

    /**
     * Returns the created math root element.
     * 
     * @return Math root element
     */
    public MathDocumentElement getMathRootElement() {
        return this.rootElement;
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
    public void characters(final char[] ch, final int start, final int length) {
        if (!this.stack.empty()) {
            ((AbstractMathElement) this.stack.peek()).addText(new String(ch,
                    start, length));
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
     *            The qualified XML 1.0 name (with prefix), or the empty
     *            string if qualified names are not available.
     */
    public void endElement(final String namespaceURI, final String localName,
            final String qName) {
        if (!this.stack.empty()) {
            final Node element = this.stack.pop();

            if (element instanceof AbstractMathElement) {
                final AbstractMathElement aElement = (AbstractMathElement) element;
                aElement.eventElementComplete();
            }
        }
    }

    /**
     * Receive notification of the beginning of a document.
     */
    public void startDocument() {
        this.rootElement = null;
        this.stack = new Stack<Node>();
        this.rootElement = new MathDocumentElement(this.mbase);
        this.stack.push(this.rootElement);
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
    public void startElement(final String namespaceURI,
            final String localName, final String qName,
            final Attributes attributes) {
        if (this.stack.empty()) {
            return;
        }

        final AttributeMap aMap = new SAXAttributeMap(attributes);
        final MathMLElement element = MathElementFactory.elementFromName(
                localName, aMap, this.mbase);

        if (!this.stack.empty()) {
            this.stack.peek().appendChild(element);
        }

        this.stack.push(element);
    }

    /**
     * End the scope of a prefix-URI mapping.
     * 
     * @param prefix
     *            Prefix
     */
    public void endPrefixMapping(final String prefix) {
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
    public void ignorableWhitespace(final char[] ch, final int start,
            final int length) {
    }

    /**
     * Receive notification of a processing instruction.
     * 
     * @param target
     *            Target
     * @param data
     *            Data
     */
    public void processingInstruction(final String target, final String data) {
    }

    /**
     * Receive an object for locating the origin of SAX document events.
     * 
     * @param locator
     *            Locator
     */
    public void setDocumentLocator(final Locator locator) {
    }

    /**
     * Receive notification of a skipped entity.
     * 
     * @param name
     *            Entity name
     */
    public void skippedEntity(final String name) {
    }

    /**
     * Begin the scope of a prefix-URI Namespace mapping.
     * 
     * @param prefix
     *            Prefix
     * @param uri
     *            Uri
     */
    public void startPrefixMapping(final String prefix, final String uri) {
    }

}

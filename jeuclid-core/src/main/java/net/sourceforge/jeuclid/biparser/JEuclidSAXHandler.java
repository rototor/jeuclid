/*
 * Copyright 2009 - 2010 JEuclid, http://jeuclid.sf.net
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

/* $Id $ */

package net.sourceforge.jeuclid.biparser;

import java.io.StringReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * this class is used for SAX parsing. it builds a BiTree out of a text while
 * parsing
 * 
 * @version $Revision$
 * 
 */
public class JEuclidSAXHandler extends DefaultHandler {

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory
            .getLog(JEuclidSAXHandler.class);

    /** stores characters while parsing (text of TextNodes). */
    private StringBuffer textBuffer;

    /** locater for X&Y-position in inputtext. */
    private Locator locator;

    /** BiTreeCreationHelper. */
    private final BiTreeCreationHelper treeHelper;

    /** inputtext to parse. */
    private final String content;

    /** current position in inputtext. */
    private int position;

    /** previous position in inputtext. */
    private int previousPosition;

    /** last line (y-position) in inputtext. */
    private int lastLine;

    /** last column (x-position) in inputtext. */
    private int lastColumn;

    /** BiTree to construct. */
    private final BiTree tree;

    /**
     * create a new SAX-Handler for parsing and creating a BiTree.
     * 
     * @param c
     *            inputtext to parse
     * @param t
     *            BiTree to construct
     */
    public JEuclidSAXHandler(final String c, final BiTree t) {
        this.position = 0;
        this.previousPosition = 0;
        this.lastLine = 1;
        this.lastColumn = 1;

        this.content = c;
        this.tree = t;
        this.treeHelper = new BiTreeCreationHelper();
    }

    /**
     * set the document locator.
     * 
     * @param l
     *            locator
     */
    @Override
    public final void setDocumentLocator(final Locator l) {
        this.locator = l;
    }

    /**
     * stop resolving of entities (dtd).
     * 
     * @param publicId
     *            publicId
     * @param systemId
     *            systemId
     * @return empty InputSource
     */
    @Override
    public final InputSource resolveEntity(final String publicId,
            final String systemId) {
        return new InputSource(new StringReader(""));
    }

    // ===========================================================
    // SAX DocumentHandler methods
    // ===========================================================
    /**
     * start document.
     */
    @Override
    public final void startDocument() {
        JEuclidSAXHandler.LOGGER.debug("SAX start document, length="
                + this.content.length());
    }

    /**
     * end document.
     * 
     * @throws SAXException
     *             if a sax parse exception occurs
     */
    @Override
    public final void endDocument() throws SAXException {
        this.tree.setRoot(this.treeHelper.getRoot());
        JEuclidSAXHandler.LOGGER.debug("SAX end document");
    }

    /**
     * start element, called at end of every new open tag.
     * 
     * @param namespaceURI
     *            namespace
     * @param sName
     *            simple name
     * @param qName
     *            qulified name
     * @param attrs
     *            attributes of node
     * @throws SAXException
     *             if a sax parse exception occurs
     */
    @Override
    public final void startElement(final String namespaceURI,
            final String sName, final String qName, final Attributes attrs)
            throws SAXException {

        int startPosition;
        int length;
        // element name
        String eName;

        eName = sName;
        if ("".equals(eName)) {
            // not namespaceAware
            eName = qName;
        }

        // get current position in inputtext
        this.contentPosition();

        // get startposition of tag
        startPosition = this.content.lastIndexOf("<" + eName,
                this.position - 1);

        if (this.textBuffer == null) {
            length = 0;
        } else {
            length = this.textBuffer.length();
        }

        JEuclidSAXHandler.LOGGER.debug("tag-start=" + startPosition
                + " tag-end=" + this.position + " buffer="
                + (startPosition - this.previousPosition) + " textbuffer="
                + length);

        // create a EmptyNode if text is before this element
        if (startPosition - this.previousPosition > 0) {
            JEuclidSAXHandler.LOGGER.debug("empty length="
                    + (startPosition - this.previousPosition));

            this.treeHelper.createEmtpyNode(startPosition
                    - this.previousPosition);
            this.textBuffer = null;
        }

        this.printElement(namespaceURI, eName, true, startPosition, attrs);

        // create new BiNode
        this.treeHelper.createBiNode(startPosition, this.position
                - startPosition, namespaceURI, eName, attrs);
    }

    /**
     * end element, called at end of every close tag.
     * 
     * @param namespaceURI
     *            namespace
     * @param sName
     *            simple name
     * @param qName
     *            qulified name
     */
    @Override
    public final void endElement(final String namespaceURI,
            final String sName, final String qName) {
        // element name
        String eName = sName;
        // text of a TextNode before close tag
        String text;
        // length of TextNode of EmptyNode
        int textLength;
        final String apo = "'";

        if ("".equals(eName)) {
            // not namespaceAware
            eName = qName;
        }

        // get current position in inputtext (end-position of close tag)
        this.contentPosition();

        // length of text before close tag
        textLength = this.content.lastIndexOf("</", this.position - 1)
                - this.previousPosition;

        // create a new TextNode
        if (this.textBuffer != null && this.textBuffer.length() > 0
                && this.treeHelper.allowNewTextNode()) {

            text = this.textBuffer.toString();
            this.treeHelper.createTextNode(textLength, text);
            this.textBuffer = null;

            JEuclidSAXHandler.LOGGER.debug(apo
                    + text.replaceAll(this.nl(), "#") + apo);

        } else if (!this.treeHelper.allowNewTextNode() && textLength > 0) {
            // or create a new EmptyNode
            this.treeHelper.createEmtpyNode(textLength);
        }

        /** close current BiNode in tree (set length of node) */
        this.treeHelper.closeBiNode(this.position);

        this.printElement(namespaceURI, eName, false, this.position, null);
    }

    /**
     * concat characters while parsing.
     * 
     * @param buf
     *            inputtext
     * @param offset
     *            offset of characters to inputtext
     * @param len
     *            number of characters
     * @throws SAXException
     *             if a sax parse exception occurs
     */
    @Override
    public final void characters(final char[] buf, final int offset,
            final int len) throws SAXException {
        final String s = new String(buf, offset, len);

        if (this.textBuffer == null) {
            this.textBuffer = new StringBuffer(s);
        } else {
            this.textBuffer.append(s);
        }
    }

    // ===========================================================
    // Utility Methods ...
    // ===========================================================
    /**
     * calculate current position in inputtext.
     */
    private void contentPosition() {
        final int line = this.locator.getLineNumber();
        final int column = this.locator.getColumnNumber();
        int l;

        this.previousPosition = this.position;

        JEuclidSAXHandler.LOGGER.debug("old line=" + this.lastLine);
        for (l = this.lastLine; l < line; l = l + 1) {
            this.position = 1 + this.content
                    .indexOf(this.nl(), this.position);
            // System.out.println(" position = " + position + " ");
        }

        if (this.lastLine == line) {
            // tag is in same line as previous
            this.position += column - this.lastColumn;
        } else {
            // position += column - 1;
            // position += column - nl().length();
            this.position += column - 2 + this.nl().length();
        }

        this.lastLine = line;
        this.lastColumn = column;
        JEuclidSAXHandler.LOGGER.debug(" - new line=" + this.lastLine
                + " - old pos=" + this.previousPosition + " new pos="
                + this.position);
    }

    /**
     * print information about an elment.
     * 
     * @param namespaceURI
     *            namespace
     * @param name
     *            of tag
     * @param open
     *            if true output an open tag, else close tag
     * @param pos
     *            position of tag
     * @param attrs
     *            attributes of tag
     * 
     */
    private void printElement(final String namespaceURI, final String name,
            final boolean open, final int pos, final Attributes attrs) {
        final StringBuffer sb = new StringBuffer(32);

        sb.append(this.position());
        sb.append(" - ");
        sb.append(pos);

        if (open) {
            sb.append(" <");
        } else {
            sb.append(" </");
        }

        sb.append(name);

        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i = i + 1) {
                // Attr name
                String aName = attrs.getLocalName(i);

                if ("".equals(aName)) {
                    aName = attrs.getQName(i);
                }

                sb.append(' ');
                sb.append(aName + "=\"" + attrs.getValue(i) + "\"");
            }
        }

        if (namespaceURI != null && namespaceURI.length() > 0) {
            sb.append(' ');
            sb.append(namespaceURI);
        }
        sb.append('>');
        sb.append(this.nl());

        JEuclidSAXHandler.LOGGER.debug(sb.toString());
    }

    /**
     * get newline character.
     * 
     * @return newline
     */
    private String nl() {
        // workaround for some problems with OS dependency
        return "\n";
        // return System.getProperty("line.separator");
    }

    /**
     * print current x/y-position.
     * 
     * @return current x/y-position
     */
    private String position() {
        final int line = this.locator.getLineNumber();
        final int column = this.locator.getColumnNumber();
        final StringBuffer sb = new StringBuffer();
        final int dez = 10;

        if (line < dez) {
            sb.append('0');
        }

        sb.append(line);
        sb.append('/');
        if (column < dez) {
            sb.append('0');
        }

        sb.append(column);
        sb.append(':');

        return sb.toString();
    }
}

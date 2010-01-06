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


package net.sourceforge.jeuclid.biparser;

import java.io.StringReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * this class is used for SAX parsing.
 * it builds a BiTree out of a text while parsing
 *
 */
public class JEuclidSAXHandler extends DefaultHandler {

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
     * @param c inputtext to parse
     * @param t BiTree to construct
     */
    public JEuclidSAXHandler(final String c, final BiTree t) {
        position = 0;
        previousPosition = 0;
        lastLine = 1;
        lastColumn = 1;

        content = c;
        tree = t;
        treeHelper = new BiTreeCreationHelper();
    }

    /**
     * set the document locator.
     * @param l locator
     */
    @Override
    public final void setDocumentLocator(final Locator l) {
        locator = l;
    }

    /**
     * stop resolving of entities (dtd).
     * @param publicId publicId
     * @param systemId systemId
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
        debug("SAX start document, length=" + content.length() + nl());
    }

    /**
     * end document.
     * @throws SAXException if a sax parse exception occurs
     */
    @Override
    public final void endDocument() throws SAXException {
        tree.setRoot(treeHelper.getRoot());
        debug("SAX end document" + nl());
    }

    /**
     * start element, called at end of every new open tag.
     * @param namespaceURI namespace
     * @param sName simple name
     * @param qName qulified name
     * @param attrs attributes of node
     * @throws SAXException if a sax parse exception occurs
     */
    @Override
    public final void startElement(final String namespaceURI,
            final String sName, final String qName, final Attributes attrs)
            throws SAXException {

        int startPosition;
        int length;
        String eName;                   // element name

        eName = sName;
        if ("".equals(eName)) {
            eName = qName;              // not namespaceAware
        }

        contentPosition();              // get current position in inputtext

        // get startposition of tag
        startPosition = content.lastIndexOf("<" + eName, position - 1);

        if (textBuffer == null) {
            length = 0;
        } else {
            length = textBuffer.length();
        }

        debug("tag-start=" + startPosition + " tag-end=" + position
                + " buffer=" + (startPosition - previousPosition)
                + " textbuffer=" + length + nl());

        // create a EmptyNode if text is before this element
        if (startPosition - previousPosition > 0) {
            debug("empty length=" + (startPosition - previousPosition) + nl());

            treeHelper.createEmtpyNode(startPosition - previousPosition);
            textBuffer = null;
        }

        printElement(namespaceURI, eName, true, startPosition, attrs);

        // create new BiNode
        treeHelper.createBiNode(startPosition, position - startPosition,
                namespaceURI, eName, attrs);
    }

    /**
     * end element, called at end of every close tag.
     * @param namespaceURI namespace
     * @param sName simple name
     * @param qName qulified name
     */
    @Override
    public final void endElement(final String namespaceURI, final String sName,
            final String qName) {
        String eName = sName;       // element name
        String text;                // text of a TextNode before close tag
        int textLength;             // length of TextNode of EmptyNode

        if ("".equals(eName)) {
            eName = qName;          // not namespaceAware
        }

        // get current position in inputtext (end-position of close tag)
        contentPosition();

        // length of text before close tag
        textLength = content.lastIndexOf("</", position - 1) - previousPosition;

        // create a new TextNode
        if (textBuffer != null && textBuffer.length() > 0
                && treeHelper.allowNewTextNode()) {

            text = new String(textBuffer);
            treeHelper.createTextNode(textLength, text);
            textBuffer = null;

            debug("'" + text.replaceAll(nl(), "#") + "'" + nl());

        } else if (!treeHelper.allowNewTextNode() && textLength > 0) {
            // or create a new EmptyNode
            treeHelper.createEmtpyNode(textLength);
        }

        /** close current BiNode in tree (set length of node) */
        treeHelper.closeBiNode(position);

        printElement(namespaceURI, eName, false, position, null);
    }

    /**
     * concat characters while parsing.
     * @param buf inputtext
     * @param offset offset of characters to inputtext
     * @param len number of characters
     * @throws SAXException if a sax parse exception occurs
     */
    @Override
    public final void characters(final char[] buf, final int offset,
            final int len) throws SAXException {
        final String s = new String(buf, offset, len);

        if (textBuffer == null) {
            textBuffer = new StringBuffer(s);
        } else {
            textBuffer.append(s);
        }
    }

    // ===========================================================
    // Utility Methods ...
    // ===========================================================
    /**
     * calculate current position in inputtext.
     */
    private void contentPosition() {
        final int line = locator.getLineNumber();
        final int column = locator.getColumnNumber();
        int l;

        previousPosition = position;

        debug("old line=" + lastLine);
        for (l = lastLine; l < line; l++) {
            position = 1 + content.indexOf(nl(), position);
            System.out.println(" position = " + position + " ");
        }

        if (lastLine == line) {
            // tag is in same line as previous
            position += (column - lastColumn);
        } else {
            //position += column - 1;
            //position += column - nl().length();
            position += column - 2 + nl().length();
        }

        lastLine = line;
        lastColumn = column;
        debug(" - new line=" + lastLine + " - old pos="
                + previousPosition + " new pos=" + position + nl());
    }

    /**
     * print information about an elment.
     * @param namespaceURI namespace
     * @param name of tag
     * @param open if true output an open tag, else close tag
     * @param pos position of tag
     * @param attrs attributes of tag
     *
     */
    private void printElement(final String namespaceURI, final String name,
            final boolean open, final int pos, final Attributes attrs) {
        final StringBuffer sb = new StringBuffer(32);

        sb.append(position());
        sb.append(" - ");
        sb.append(pos);

        if (open) {
            sb.append(" <");
        } else {
            sb.append(" </");
        }

        sb.append(name);

        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String aName = attrs.getLocalName(i); // Attr name

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
        sb.append(nl());

        debug(sb.toString());
    }

    /**
     * output a debug message if debugging is enabled.
     * @param message string to ouput
     */
    private void debug(final String message) {
        final boolean debug = true;

        if (!debug) {
            return;
        }

        System.out.print(message);
        System.out.flush();
    }

    /**
     * get newline character.
     * @return newline
     */
    private String nl() {
        //workaround for some problems with OS dependency
        return "\n";
        //return System.getProperty("line.separator");
    }

    /**
     * print current x/y-position.
     * @return current x/y-position
     */
    private String position() {
        final int line = locator.getLineNumber();
        final int column = locator.getColumnNumber();
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

package net.sourceforge.jeuclid.biparser;

/*
 * Copyright (c) 2006 Sun Microsystems, Inc.  All rights reserved.  U.S.
 * Government Rights - Commercial software.  Government users are subject
 * to the Sun Microsystems, Inc. standard license agreement and
 * applicable provisions of the FAR and its supplements.  Use is subject
 * to license terms.
 *
 * This distribution may include materials developed by third parties.
 * Sun, Sun Microsystems, the Sun logo, Java and J2EE are trademarks
 * or registered trademarks of Sun Microsystems, Inc. in the U.S. and
 * other countries.
 *
 * Copyright (c) 2006 Sun Microsystems, Inc. Tous droits reserves.
 *
 * Droits du gouvernement americain, utilisateurs gouvernementaux - logiciel
 * commercial. Les utilisateurs gouvernementaux sont soumis au contrat de
 * licence standard de Sun Microsystems, Inc., ainsi qu'aux dispositions
 * en vigueur de la FAR (Federal Acquisition Regulations) et des
 * supplements a celles-ci.  Distribue par des licences qui en
 * restreignent l'utilisation.
 *
 * Cette distribution peut comprendre des composants developpes par des
 * tierces parties. Sun, Sun Microsystems, le logo Sun, Java et J2EE
 * sont des marques de fabrique ou des marques deposees de Sun
 * Microsystems, Inc. aux Etats-Unis et dans d'autres pays.
 */

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class JEuclidSAXHandler extends DefaultHandler {

    private StringBuffer textBuffer;
    private Locator locator;
    private BiTree tree;
    private String content;
    private int position;
    private int previousPosition;
    private int lastLine;
    private int lastColumn;
    private boolean debug = false;

    public JEuclidSAXHandler(String content, BiTree tree) {
        position = 0;
        previousPosition = 0;
        lastLine = 1;
        lastColumn = 1;

        this.content = content;
        this.tree = tree;
    }

    // this will be called when XML-parser starts reading
    // XML-data; here we save reference to current position in XML:
    @Override
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    // ===========================================================
    // SAX DocumentHandler methods
    // ===========================================================
    @Override
    public void startDocument() {

        debug("SAX start document, length=" + content.length() + nl());
    }

    @Override
    public void endDocument() throws SAXException {

        debug("SAX end document" + nl());
    }

    @Override
    public void startElement(String namespaceURI, String sName, // simple name
            String qName, // qualified name
            Attributes attrs) throws SAXException {

        int startPosition;
        String eName; // element name

        eName = sName; // element name
        if ("".equals(eName)) {
            eName = qName; // not namespaceAware
        }

        contentPosition();

        // get startposition of tag
        startPosition = content.lastIndexOf("<" + eName, position);

        debug("tag-start="+startPosition+" tag-end="+position+ " buffer="+(startPosition-previousPosition)+
                " textbuffer="+(textBuffer==null?0:textBuffer.length())+nl());

        // unnecessary text
        if (previousPosition == 0 || startPosition-previousPosition > 0) {
            tree.newEmtpyNode(previousPosition, startPosition - previousPosition);

            debug("empty length=" + (startPosition - previousPosition) + nl());
            textBuffer = null;
        }
        
        printElement(namespaceURI, eName, true, startPosition, attrs);

        // new node
        tree.newElement(startPosition, position - startPosition, namespaceURI, eName, attrs);
    }

    @Override
    public void endElement(String namespaceURI, String sName, String qName) {

        String eName = sName; // element name
        String text;

        if ("".equals(eName)) {
            eName = qName; // not namespaceAware
        }

        contentPosition();      // calc end-position of close tag

        // textnode
        if (textBuffer != null && textBuffer.length() > 0 && tree.allowNewTextNode()) {

            text = new String(textBuffer);
            tree.newTextNode(previousPosition, content.lastIndexOf("</", position) - previousPosition, text);
            textBuffer = null;

            debug("'" + text.replaceAll(System.getProperty("line.separator"), "#") + "'" + nl());

        }  // empty - textnode
        else if (!tree.allowNewTextNode()) {
            tree.newEmtpyNode(previousPosition, content.lastIndexOf("</", position) - previousPosition);
        }

        tree.closeElement(position);

        printElement(namespaceURI, eName, false, position, null);
    }

    @Override
    public void characters(char[] buf, int offset, int len) throws SAXException {
        String s = new String(buf, offset, len);

        if (textBuffer == null) {
            textBuffer = new StringBuffer(s);
        } else {
            textBuffer.append(s);
        }
    }

    public BiTree getTree() {
        return tree;
    }

// ===========================================================
// Utility Methods ...
// ===========================================================
    private int contentPosition() {
        int line = locator.getLineNumber();
        int column = locator.getColumnNumber();
        int l;

        previousPosition = position;

        debug("old line=" + lastLine);
        for (l = lastLine; l <
                line; l++) {
            position = 1 + content.indexOf(System.getProperty("line.separator"), position);
        }

        if (lastLine == line) {
            position += (column - lastColumn);  // tag is in same line as previous
        } else {
            position += column - 1;
        }

        lastLine = line;
        lastColumn =
                column;
        debug(" - new line=" + lastLine + " - old pos=" + previousPosition + " new pos=" + position + nl());

        return position;
    }

    private void printElement(String namespaceURI, String s, boolean open, int position, Attributes attrs) {
        StringBuffer sb = new StringBuffer(position());

        sb.append(" - ");
        sb.append(position);

        if (open) {
            sb.append(" <");
        } else {
            sb.append(" </");
        }

        sb.append(s);

        if (attrs != null) {
            for (int i = 0; i <
                    attrs.getLength(); i++) {
                String aName = attrs.getLocalName(i); // Attr name

                if ("".equals(aName)) {
                    aName = attrs.getQName(i);
                }

                sb.append(" ");
                sb.append(aName + "=\"" + attrs.getValue(i) + "\"");
            }
        }

        sb.append(" ");
        sb.append(namespaceURI);
        sb.append(">");
        sb.append(System.getProperty("line.separator"));

        debug(sb.toString());
    }

// Wrap I/O exceptions in SAX exceptions, to
// suit handler signature requirements
    private void debug(String s) {
        if (!debug) {
            return;
        }
        System.out.print(s);
        System.out.flush();
    }

// Start a new line
    private String nl() {
        return System.getProperty("line.separator");
    }

    private String position() {
        int line = locator.getLineNumber();
        int column = locator.getColumnNumber();
        StringBuffer sb = new StringBuffer();

        if (line < 10) {
            sb.append("0");
        }

        sb.append(line);
        sb.append("/");
        if (column < 10) {
            sb.append("0");
        }

        sb.append(column);
        sb.append(":");

        return sb.toString();
    }
}

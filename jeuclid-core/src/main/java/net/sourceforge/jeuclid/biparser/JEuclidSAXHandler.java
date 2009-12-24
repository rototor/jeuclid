package net.sourceforge.jeuclid.biparser;

import java.io.StringReader;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 * this class is used for SAX parsing, it builds a BiTree out of a text while parsing
 *
 * @author dominik
 */
public class JEuclidSAXHandler extends DefaultHandler {

    /** stores characters while parsing (text of TextNodes) */
    private StringBuffer textBuffer;
    /** locater for X&Y-position in inputtext */
    private Locator locator;
    /** result BiTree */
    private BiTree tree;
    /** inputtext to parse */
    private String content;
    /** current position in inputtext */
    private int position;
    /** previous position in inputtext */
    private int previousPosition;
    /** last line (y-position) in inputtext */
    private int lastLine;
    /** last column (x-position) in inputtext */
    private int lastColumn;
    /** set true for debugging */
    private boolean debug = false;

    /**
     * create a new SAX-Handler for parsing and creating a BiTree
     * @param content inputtext to parse
     * @param tree BiTree for constructing
     */
    public JEuclidSAXHandler(String content, BiTree tree) {
        position = 0;
        previousPosition = 0;
        lastLine = 1;
        lastColumn = 1;

        this.content = content;
        this.tree = tree;
    }

    /**
     * set the document locator
     * @param locator
     */
    @Override
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    /**
     * stop resolving of entities (dtd)
     * @param publicId
     * @param systemId
     * @return empty InputSource
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        return new InputSource(new StringReader(""));
    }

    // ===========================================================
    // SAX DocumentHandler methods
    // ===========================================================

    /**
     * start document
     */
    @Override
    public void startDocument() {
        debug("SAX start document, length=" + content.length() + nl());
    }

    /**
     * end document
     */
    @Override
    public void endDocument() throws SAXException {
        debug("SAX end document" + nl());
    }

    /**
     * start element, called at end of every new open tag (e.g. <tag>)
     * @param namespaceURI
     * @param sName simple name
     * @param qName qulified name
     * @param attrs attributes of node
     */
    @Override
    public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws SAXException {
        int startPosition;
        String eName;                   // element name

        eName = sName;
        if ("".equals(eName)) {
            eName = qName;              // not namespaceAware
        }

        contentPosition();              // get current position in inputtext

        // get startposition of tag
        startPosition = content.lastIndexOf("<" + eName, position - 1);

        debug("tag-start=" + startPosition + " tag-end=" + position + " buffer=" + (startPosition - previousPosition) +
                " textbuffer=" + (textBuffer == null ? 0 : textBuffer.length()) + nl());

        // create a EmptyNode if text is before this element
        if (startPosition - previousPosition > 0) {
            debug("empty length=" + (startPosition - previousPosition) + nl());

            tree.createEmtpyNode(startPosition - previousPosition);
            textBuffer = null;
        }

        printElement(namespaceURI, eName, true, startPosition, attrs);

        // create new BiNode
        tree.createBiNode(startPosition, position - startPosition, namespaceURI, eName, attrs);
    }

    /**
     * end element, called at end of every close tag (e.g. </tag>)
     * @param namespaceURI
     * @param sName simple name
     * @param qName qulified name
     */
    @Override
    public void endElement(String namespaceURI, String sName, String qName) {
        String eName = sName;       // element name
        String text;                // text of a TextNode before close tag
        int textLength;             // length of TextNode of EmptyNode

        if ("".equals(eName)) {
            eName = qName;          // not namespaceAware
        }

        contentPosition();              // get current position in inputtext (end-position of close tag)

        // length of text before close tag
        textLength = content.lastIndexOf("</", position - 1) - previousPosition;

        // create a new TextNode
        if (textBuffer != null && textBuffer.length() > 0 && tree.allowNewTextNode()) {

            text = new String(textBuffer);
            tree.createTextNode(textLength, text);
            textBuffer = null;

            debug("'" + text.replaceAll(System.getProperty("line.separator"), "#") + "'" + nl());

        } // or create a new EmptyNode
        else if (!tree.allowNewTextNode() && textLength > 0) {
            tree.createEmtpyNode(textLength);
        }

        /** close current BiNode in tree (set length of node) */
        tree.closeBiNode(position);

        printElement(namespaceURI, eName, false, position, null);
    }

    /**
     * concat characters while parsing
     * @param buf inputtext
     * @param offset offset of characters to inputtext
     * @param len number of characters
     */
    @Override
    public void characters(char[] buf, int offset, int len) throws SAXException {
        String s = new String(buf, offset, len);

        if (textBuffer == null) {
            textBuffer = new StringBuffer(s);
        } else {
            textBuffer.append(s);
        }
    }

    /**
     * get the (final-) BiTree
     * @return BiTree
     */
    public BiTree getTree() {
        return tree;
    }

    // ===========================================================
    // Utility Methods ...
    // ===========================================================

    /**
     * calculate current position in inputtext
     */
    private void contentPosition() {
        int line = locator.getLineNumber();
        int column = locator.getColumnNumber();
        int l;

        previousPosition = position;

        debug("old line=" + lastLine);
        for (l = lastLine; l < line; l++) {
            position = 1 + content.indexOf(System.getProperty("line.separator"), position);
        }

        if (lastLine == line) {
            position += (column - lastColumn);  // tag is in same line as previous
        } else {
            position += column - 1;
        }

        lastLine = line;
        lastColumn = column;
        debug(" - new line=" + lastLine + " - old pos=" + previousPosition + " new pos=" + position + nl());
    }

    /**
     * print information about an elment
     * @param namespaceURI
     * @param name of tag
     * @param open if true output an open tag, else close tag
     * @param position position of tag
     * @param attrs attributes of tag
     *
     */
    private void printElement(String namespaceURI, String name, boolean open, int position, Attributes attrs) {
        StringBuffer sb = new StringBuffer(position());

        sb.append(" - ");
        sb.append(position);

        if (open) {
            sb.append(" <");
        } else {
            sb.append(" </");
        }

        sb.append(name);

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

        if (namespaceURI != null && namespaceURI.length() > 0) {
            sb.append(" ");
            sb.append(namespaceURI);
        }
        sb.append(">");
        sb.append(System.getProperty("line.separator"));

        debug(sb.toString());
    }

    /**
     * output a debug message if debugging is enabled
     * @param message string to ouput
     */
    private void debug(String message) {
        if (!debug) {
            return;
        }
        System.out.print(message);
        System.out.flush();
    }

    /**
     * get newline character
     * @return newline
     */
    private String nl() {
        return System.getProperty("line.separator");
    }

    /**
     * print current x/y-position
     * @return current x/y-position
     */
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

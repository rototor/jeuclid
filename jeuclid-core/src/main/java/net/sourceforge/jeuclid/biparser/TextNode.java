package net.sourceforge.jeuclid.biparser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * this class is used to store specific information about a text node.
 * the node cannot have a child nor a sibling
 *
 * @author dominik
 */
public class TextNode extends ABiNode {

    /** DOM-info: text of node. */
    private String text;

    /**
     * creates a new TextNode, constructor does not create a DOM-node.
     * @param length length of child
     * @param t DOM-info
     */
    public TextNode(final int length, final String t) {
        setLength(length);
        text = t;
    }

    /**
     * get the type of node.
     * @return TEXT
     */
    @Override
    public final BiType getType() {
        return BiType.TEXT;
    }

     /**
     * insert characters in TextNode, always reparse parent node.
     * {@inheritDoc}
     */
    @Override
    public final void insert(final BiTree biTree, final int offset,
            final int length, final int totalOffset)
            throws ReparseException {
        throw new ReparseException();
    }

     /**
     * remove characters in TextNode, always reparse parent node.
     * {@inheritDoc}
     */
    @Override
    public final void remove(final BiTree biTree, final int offset,
            final int length, final int totalOffset)
            throws ReparseException  {
        throw new ReparseException();
    }

    /**
     * forward insert/remove to sibling not allowed at a TextNode.
     */
    @Override
    protected final void forwardToSibling(final boolean insert,
            final BiTree biTree, final int offset, final int length,
            final int totalOffset)
            throws ReparseException {
        throw new UnsupportedOperationException("forwardToSibling " +
                "at textnode not allowed");
    }

    /**
     * get the text of TextNode.
     * @return text of TextNode
     */
    public String getText() {
        if (text == null) {
           if (getNode() == null) {
                return null;
            } else {
                return getNode().getTextContent();
            }
        } else {
            return text;
        }
    }

    /**
     * create a DOM-textnode.
     * @param doc Document to create DOM-tree
     * @return DOM-textnode
     */
    @Override
    public Node createDOMSubtree(final Document doc) {
        Text textNode;

        textNode = doc.createTextNode(text);
        text = null;

        setNode(textNode);
        return textNode;
    }

    /** {@inheritDoc} */
    @Override
    public int searchNode(final Node node, final int totalOffset) {
        return super.searchNode(node, totalOffset);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(32);
        final String nl = System.getProperty("line.separator");

        sb.append("[TEXT length:");
        sb.append(getLength());
        sb.append(" '");
        sb.append(getText().replaceAll(nl, "#"));
        sb.append("']");

        return sb.toString();
    }

    @Override
    public String toString(final int level) {
        final StringBuffer sb = new StringBuffer();
        final String nl = System.getProperty("line.separator");

        sb.append(formatLength());
        sb.append(':');
        for (int i = 0; i <= level; i++) {
            sb.append(' ');
        }

        sb.append('\'');
        sb.append(getText().replaceAll(nl, "#"));
        sb.append('\'');

        return sb.toString();
    }
}

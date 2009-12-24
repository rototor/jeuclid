package net.sourceforge.jeuclid.biparser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * this class is used to store specific information about a text node
 * the node cannot have a child nor a sibling
 *
 * @author dominik
 */
public class TextNode extends ABiNode {

    /** DOM-info: text of node */
    private String text;

    /**
     * creates a new TextNode, constructor does not create a DOM-node
     * @param childOffset offset to child from node begin (length of open tag)
     * @param text DOM-info
     */
    public TextNode(int length, String text) {
        setLength(length);
        this.text = text;
    }

    /**
     * get parent of TextNode, same as previous
     * @return parent of TextNode
     */
    @Override
    public BiNode getParent() {
        return (BiNode) getPrevious();
    }

    /**
     * get the type of node
     * @return TEXT
     */
    @Override
    public BiType getType() {
        return BiType.TEXT;
    }

     /**
     * insert characters in TextNode, always reparse parent node
     * {@inheritDoc}
     */
    @Override
    public void insert(BiTree biTree, int offset, int length, int totalOffset) throws ReparseException {
        throw new ReparseException();
    }

     /**
     * remove characters in TextNode, always reparse parent node
     * {@inheritDoc}
     */
    @Override
    public void remove(BiTree biTree, int offset, int length, int totalOffset) throws ReparseException  {
        throw new ReparseException();
    }

    /**
     * forward insert/remove to sibling not allowed at a TextNode
     */
    @Override
    protected void forwardToSibling(boolean insert, BiTree biTree, int offset, int length, int totalOffset) throws ReparseException {
        throw new UnsupportedOperationException("forwardToSibling at textnode not allowed");
    }

    /**
     * add a sibling not allowed at a TextNode
     */
    @Override
    public void addSibling(ABiNode abiNode) {
        throw new UnsupportedOperationException("addSibling at textnode not allowed");
    }

    /**
     * get the text of TextNode
     * @return text of TextNode
     */
    public String getText() {
        if (text != null) {
            return text;
        } else if (getNode() != null) {
            return getNode().getTextContent();
        } else {
            return null;
        }
    }

    /**
     * create a DOM-textnode
     * @param doc Document to create DOM-tree
     * @return DOM-textnode
     */
    @Override
    public Node createDOMSubtree(Document doc) {
        Text textNode;

        textNode = doc.createTextNode(text);
        text = null;

        setNode(textNode);
        return textNode;
    }

    /** {@inheritDoc} */
    @Override
    public int searchNode(Node node, int totalOffset) {
        return super.searchNode(node, totalOffset);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[TEXT ");

        sb.append("length:");
        sb.append(getLength());
        sb.append(" '");
        sb.append(getText().replaceAll(System.getProperty("line.separator"), "#"));
        sb.append("']");

        return sb.toString();
    }

    @Override
    public String toString(int level) {
        StringBuffer sb = new StringBuffer(formatLength());

        sb.append(":");
        for (int i = 0; i <= level; i++) {
            sb.append(" ");
        }

        sb.append("'");
        sb.append(getText().replaceAll(System.getProperty("line.separator"), "#"));
        sb.append("'");

        return sb.toString();
    }
}

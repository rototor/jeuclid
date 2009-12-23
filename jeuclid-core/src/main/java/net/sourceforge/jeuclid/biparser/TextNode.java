package net.sourceforge.jeuclid.biparser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class TextNode extends ABiNode {

    private String text;

    public TextNode(int length, String text) {
        setLength(length);
        this.text = text;
    }

    @Override
    public Node createDOMSubtree(Document doc) {
        Text textNode;

        textNode = doc.createTextNode(text);

        text = null;

        setNode(textNode);
        return textNode;
    }

    @Override
    public BiNode getParent() {
        return (BiNode) getPrevious();
    }

    @Override
    public BiType getType() {
        return BiType.TEXT;
    }

    @Override
    public void insert(BiTree biTree, int offset, int length, int totalOffset) throws ReparseException {
        throw new ReparseException();
    }

    @Override
    public void remove(BiTree biTree, int offset, int length, int totalOffset) throws ReparseException  {
        throw new ReparseException();
    }

    @Override
    protected void forwardToSibling(boolean insert, BiTree biTree, int offset, int length, int totalOffset) throws ReparseException {
        throw new UnsupportedOperationException("forwardToSibling at textnode not allowed");
    }

    @Override
    public void addSibling(ABiNode abiNode) {
        throw new UnsupportedOperationException("addSibling at textnode not allowed");
    }

    @Override
    public ABiNode getSibling() {
        return null;
    }

    public String getText() {
        if (text != null) {
            return text;
        } else if (getNode() != null) {
            return getNode().getTextContent();
        } else {
            return null;
        }
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

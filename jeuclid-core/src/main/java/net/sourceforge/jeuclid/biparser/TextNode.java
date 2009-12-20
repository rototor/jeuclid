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
    public ABiNode getParent() {
        return getPrevious();
    }

    @Override
    public Type getType() {
        return Type.TEXT;
    }

    private void updateText(String text, int change) {
        getNode().setTextContent(text);
        changeLengthRec(change);
    }

    @Override
    public void insert(BiTree biTree, int offset, int length, int totalOffset) {
        setTotalOffset(totalOffset);

        System.out.println("insert " + toString() + " offset=" + offset + " length=" + length);

        getNode().setTextContent(biTree.getText().substring(totalOffset, biTree.getText().indexOf("</", totalOffset)));
        changeLengthRec(length);
    }

    private void remove() {
        BiNode parent;

        System.out.println("remove text node");

        parent = (BiNode) getParent();
        parent.getNode().removeChild(getNode());        // remove DOM node

        parent.setChild(null);                          // remove from bitree
        parent.changeLengthRec(-getLength());
    }

    @Override
    public void remove(BiTree biTree, int offset, int length, int totalOffset) {
        setTotalOffset(totalOffset);

        System.out.println("remove " + toString() + " offset=" + offset + " length=" + length);

        if (offset == 0 && length == getLength()) {     // remove this node

            this.remove();

        } else {                                        // change text & length

            getNode().setTextContent(biTree.getText().substring(totalOffset, biTree.getText().indexOf("</", totalOffset)));
            changeLengthRec(-length);
        }
    }

    @Override
    public ABiNode addSibling(ABiNode abiNode) {
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

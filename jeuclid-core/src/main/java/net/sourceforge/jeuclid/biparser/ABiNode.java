package net.sourceforge.jeuclid.biparser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class ABiNode {

    private ABiNode previous;
    private ABiNode sibling;
    private Node node;
    /** total offset to root (not always correct) */
    private int totalOffset;
    /** maximum length, only for formatted output (debugging) */
    private static int maxLength = 3;
    private int length;

    public enum Type {

        NODE, EMPTY, TEXT
    };

    public int getTotalOffset() {
        return totalOffset;
    }

    public void setTotalOffset(int totalOffset) {
        this.totalOffset = totalOffset;
    }

    public ABiNode getPrevious() {
        return previous;
    }

    public ABiNode getParent() {
        if (previous == null) {
            return null;                     // root
        }

        if (previous.sibling == this) {      // check if previous isn't a "real parent"
            return previous.getParent();
        } else {                             // previous is "real parent"
            return previous;
        }
    }

    public ABiNode getSibling() {
        return sibling;
    }

    public ABiNode setSibling(ABiNode sibling) {
        if (sibling != null) {
            sibling.previous = this;        
        }
        this.sibling = sibling;

        return this.sibling;
    }

    public void setPrevious(ABiNode previous) {
        this.previous = previous;
    }

    /**
     * @param offset to parent
     */
    public ABiNode addSibling(ABiNode abiNode) {
        if (sibling == null) {                   // 2nd child
            if (abiNode != null) {
                abiNode.previous = this;
            }
            sibling = abiNode;
        } else {                                 // 3rd - nth child
            sibling.addSibling(abiNode);
        }

        if (abiNode != null && abiNode.getType() == Type.NODE) {
            return abiNode;
        } else {
            return this;
        }
    }

    public int getLength() {
        return length;
    }

    public void changeLengthRec(int change) {
        length += change;

        if (getParent() != null) {
            getParent().changeLengthRec(change);
        }
    }

    public void setLength(int length) {
        this.length = length;
    }

    abstract public Type getType();

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    abstract public void insert(BiTree biTree, int offset, int length, int totalOffset);

    abstract public void remove(BiTree biTree, int offset, int length, int totalOffset);

    abstract public Node createDOMSubtree(Document doc);

    @Override
    abstract public String toString();

    abstract public String toString(int level);

    public String formatLength() {
        int i;
        StringBuffer sb = new StringBuffer();

        for (i = 1; i <= maxLength && 1 > getLength() / (Math.pow(10, maxLength - i)); i++) {
            if (i == 1 && getLength() == 0) {
                continue;
            }
            sb.append(" ");
        }

        sb.append(length);

        return sb.toString();
    }
}

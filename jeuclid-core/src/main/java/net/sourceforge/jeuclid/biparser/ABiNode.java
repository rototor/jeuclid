package net.sourceforge.jeuclid.biparser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * this class is used to store typcial information about a xml-node
 *
 * @author dominik
 */
public abstract class ABiNode {

    /** previous node, null if node is root */
    private ABiNode previous;
    /** sibling node, can be null */
    private ABiNode sibling;
    /** reference to node in DOM-tree */
    private Node node;
    /** length of node in characters */
    private int length;

    /**
     * get previous node, null if node is root
     * @return previous
     */
    public ABiNode getPrevious() {
        return previous;
    }

    /**
     * set previous for this node
     * @param previous previous node for this node
     */
    public void setPrevious(ABiNode previous) {
        this.previous = previous;
    }

    /**
     * get parent node, null if node is root
     * @return parent
     */
    public BiNode getParent() {
        if (previous == null) {
            return null;                     // root
        }

        if (previous.sibling == this) {      // check if previous isn't a "real parent"
            return previous.getParent();
        } else {                             // previous is "real parent"
            return (BiNode) previous;
        }
    }

    /**
     * get sibling node, can be null
     * @return sibling
     */
    public ABiNode getSibling() {
        return sibling;
    }

    /**
     * set sibling for this node, and set previous of sibling to this
     * @param sibling new sibling for this node
     */
    public void setSibling(ABiNode sibling) {
        if (sibling != null) {
            sibling.previous = this;
        }

        this.sibling = sibling;
    }

    /**
     * add sibling to a node, not possible at a textnode
     * if node already has a sibling, forward to sibling
     * @param sibling new sibling for this node
     */
    public void addSibling(ABiNode sibling) {
        if (this.sibling == null) {                 // 2nd child
            setSibling(sibling);
        } else {                                    // forward(3rd - nth child)
            this.sibling.addSibling(sibling);
        }
    }

    /**
     * get reference to node in DOM-tree
     * @return node in DOM-tree
     */
    public Node getNode() {
        return node;
    }

    /**
     * set reference to node in DOM-tree
     * @param node reference in DOM-tree
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * get length of node (number of characters)
     * @return length of node
     */
    public int getLength() {
        return length;
    }

    /**
     * set length of node
     * @param length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * change length of node and recursive of all parents
     * @param change changevalue (can be positive or negative)
     */
    public void changeLengthRec(int change) {
        if (change == 0) {
            return;
        }

        length += change;

        if (getParent() != null) {
            getParent().changeLengthRec(change);
        }
    }

    /**
     * get the type of node, can be BiNode, EmptyNode or TextNode
     * @return type of node
     */
    abstract public BiType getType();

    /**
     * insert characters to node
     * @param biTree reference to BiTree to which this node contains
     * @param offset position to insert characters
     * @param length number of characters to insert
     * @param totalOffset offset of node to begin of text
     * @throws ReparseException
     *
     */
    abstract public void insert(BiTree biTree, int offset, int length, int totalOffset) throws ReparseException;

    /**
     * remove characters from node
     * @param biTree reference to BiTree to which this node contains
     * @param offset position to remove characters
     * @param length number of characters to remove
     * @param totalOffset offset of node to begin of text
     * @throws ReparseException
     *
     */
    abstract public void remove(BiTree biTree, int offset, int length, int totalOffset) throws ReparseException;

    /**
     * helper method to insert or remove characters
     * @param insert if true call insert-method else remove-method
     * @param biTree reference to BiTree to which this node contains
     * @param offset position to insert/remove characters
     * @param length number of characters to insert/remove
     * @param totalOffset offset of node to begin of text
     * @throws ReparseException
     */
    protected void forwardToSibling(boolean insert, BiTree biTree, int offset, int length, int totalOffset) throws ReparseException {
        if (getSibling() == null) {
            throw new ReparseException();   // reparsing
        } else {
            if (insert) {
                getSibling().insert(biTree, offset, length, totalOffset);
            } else {
                getSibling().remove(biTree, offset, length, totalOffset);
            }
        }
    }

    /**
     * create a DOM-tree from node
     * @param doc Document to create DOM-tree
     * @return root of DOM-tree
     */
    abstract public Node createDOMSubtree(Document doc);

     /**
     * search a DOM node in this node
     * if nodes are equal return offset to begin of inputtext, else -1
     * @param node DOM node to search for
     * @param totalOffset offset of node to begin of inputtext
     * @return position of node in inputtext
     */
    public int searchNode(Node node, int totalOffset) {
        if (this.node != null && this.node == node) {
            return totalOffset;
        }

        return -1;
    }

    @Override
    abstract public String toString();

    abstract public String toString(int level);

    /**
     * helper method for outputting the length of node
     */
    public String formatLength() {
        int i;
        int maxLength = 3;
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

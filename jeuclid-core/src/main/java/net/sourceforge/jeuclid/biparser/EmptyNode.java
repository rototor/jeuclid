package net.sourceforge.jeuclid.biparser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * this class is used to store specific information about a empty node
 * the node cannot have children, but a sibling
 *
 * @author dominik
 */
public class EmptyNode extends ABiNode {

    /**
     * create a new EmptyNode
     * @param length of EmptyNode
     */
    public EmptyNode(int length) {
        setLength(length);
    }

    /**
     * get the type of node
     * @return EMPTY
     */
    @Override
    public BiType getType() {
        return BiType.EMPTY;
    }

    /** 
     * insert characters in EmptyNode, reparse if characters contain '<' or '>'
     * else change length of EmptyNode
     * {@inheritDoc}
     */
    @Override
    public void insert(BiTree biTree, int offset, int length, int totalOffset) throws ReparseException {
        int position;
        String insert;

        // System.out.println("insert " + toString() + " offset=" + offset + " length=" + length);

        if (offset <= getLength()) {            // start position in this node

            position = totalOffset + offset;
            insert = biTree.getText().substring(position, position + length);

            if (insert.contains("<") || insert.contains(">")) {
                throw new ReparseException();                       // reparsing
            }

            changeLengthRec(length);

        } else {                                // start position outside this node
            forwardToSibling(true, biTree, offset - getLength(), length, totalOffset + getLength());
        }
    }

    /**
     * remove characters from EmptyNode, reparse if length gets 0
     * {@inheritDoc}
     */
    @Override
    public void remove(BiTree biTree, int offset, int length, int totalOffset) throws ReparseException {
        // System.out.println("remove " + toString() + " offset=" + offset + " length=" + length);

        if (offset <= getLength()) {            // start position in this node

            if (offset == 0 && length >= getLength()) {     // remove this node
                throw new ReparseException();

            } else {                                        // change length
                if (offset + length <= getLength()) {       // end position in this node
                    changeLengthRec(-length);

                } else {                                    // end position outside this node
                    changeLengthRec(offset - getLength());

                    // forward remainder to sibling
                    forwardToSibling(false, biTree, 0, offset + length - getLength(), totalOffset + getLength());
                }
            }
        } else {                                // start position outside this node
            forwardToSibling(false, biTree, offset - getLength(), length, totalOffset + getLength());
        }
    }

    /**
     * don't create a DOM-tree from EmptyNode (EmptyNode has no DOM node)
     * @param doc Document to create DOM-tree
     * @return null
     */
    @Override
    public Node createDOMSubtree(Document doc) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public int searchNode(Node node, int totalOffset) {
        // forward to sibling
        if (getSibling() != null) {
            return getSibling().searchNode(node, totalOffset + getLength());
        }

        return -1;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[EMTPY ");

        sb.append("length: ");
        sb.append(getLength());
        sb.append("]");

        return sb.toString();
    }

    @Override
    public String toString(
            int level) {
        StringBuffer sb = new StringBuffer(formatLength());
        String nl = System.getProperty("line.separator");

        sb.append(":");
        for (int i = 0; i <=
                level; i++) {
            sb.append(" ");
        }

        sb.append("EMTPY");

        if (getSibling() != null) {
            sb.append(nl);
            sb.append(getSibling().toString(level));
        }

        return sb.toString();
    }
}

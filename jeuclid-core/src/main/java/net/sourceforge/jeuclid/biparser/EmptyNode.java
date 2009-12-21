package net.sourceforge.jeuclid.biparser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class EmptyNode extends ABiNode {

    // Node
    public EmptyNode(int length) {
        setLength(length);
    }

    @Override
    public Node createDOMSubtree(Document doc) {
        return null;
    }

    @Override
    public Type getType() {
        return Type.EMPTY;
    }

    @Override
    public void insert(BiTree biTree, int offset, int length, int totalOffset) {
        setTotalOffset(totalOffset);

        System.out.println("insert " + toString() + " offset=" + offset + " length=" + length);

        if (offset <= getLength()) {            // start position in this node

            changeLengthRec(length);

        } else {                                // start position outside this node
            getSibling().insert(biTree, offset - getLength(), length, totalOffset + getLength());   // forward to sibling
        }
    }

    private void remove(BiTree biTree) {
        System.out.println("remove empty node");

        if (getParent() == null) {
            biTree.setRoot(getSibling());                   // node is root
        } else {
            getParent().changeLengthRec(-getLength());

            if (getPrevious() == getParent()) {             // node is 1st child
                ((BiNode) getParent()).setChild(getSibling());
            } else {                                        // node is nth child
                getPrevious().setSibling(getSibling());
            }
        }
    }

    @Override
    public void remove(BiTree biTree, int offset, int length, int totalOffset) {
        setTotalOffset(totalOffset);

        System.out.println("remove " + toString() + " offset=" + offset + " length=" + length);

        if (offset <= getLength()) {            // start position in this node

            if (offset == 0 && length >= getLength()) {     // remove this node

                this.remove(biTree);

                // forward remainder to sibling
                if (length > getLength()) {
                    getSibling().remove(biTree, 0, length - getLength(), totalOffset);
                }

            } else {                                        // change length

                if (offset + length <= getLength()) {       // end position in this node

                    changeLengthRec(-length);

                } else {                                    // end position outside this node

                    changeLengthRec(offset - getLength());

                    // forward remainder to sibling
                    getSibling().remove(biTree, 0, offset + length - getLength(), totalOffset + getLength());
                }
            }
        } else {                                // start position outside this node

            if (getSibling() == null) {
                // ??
                // new empty node
            } else {
                getSibling().remove(biTree, offset - getLength(), length, totalOffset + getLength());   // forward to sibling
            }
        }
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

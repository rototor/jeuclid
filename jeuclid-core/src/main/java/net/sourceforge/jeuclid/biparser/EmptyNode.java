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
    public BiType getType() {
        return BiType.EMPTY;
    }

    @Override
    public void insert(BiTree biTree, int offset, int length, int totalOffset) throws ReparseException {
        int position;
        String insert;

        // // System.out.println("insert " + toString() + " offset=" + offset + " length=" + length);

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

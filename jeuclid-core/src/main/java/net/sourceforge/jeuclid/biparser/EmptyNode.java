package net.sourceforge.jeuclid.biparser;


public class EmptyNode extends ABiNode {

    // Node
    public EmptyNode(int length) {
        super(null);
        setLength(length);
    }

    @Override
    public Type getType() {
        return Type.EMPTY;
    }

    @Override
    public ABiNode getABiNodeAt(int offset, int length, int totalOffset) {
//        System.out.println("getABi Node offset=" + offset + " with length="+length+" at empty node with length=" + this.length);

        setTotalOffset(totalOffset);

        if (offset <= getLength()) {            // start position in this node
            if (offset+length <= getLength()) {
                return this;                    // end position in this node
            }
                        
            return getParent();                 // end position in following node(s)
        } else {                                // start position outside this node
            if (getSibling() != null) {
                return getSibling().getABiNodeAt(offset-getLength(), length, totalOffset+getLength());   // forward to sibling
            } else {
                return null;                    // forward to parent
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
    public String toString(int level) {
        StringBuffer sb = new StringBuffer(formatLength());
        String nl = System.getProperty("line.separator");

        sb.append(":");
        for (int i = 0; i <= level; i++) {
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

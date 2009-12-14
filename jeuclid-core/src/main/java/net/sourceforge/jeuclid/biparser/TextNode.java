package net.sourceforge.jeuclid.biparser;


import org.w3c.dom.Node;

public class TextNode extends ABiNode {

    // text
    public TextNode(int length, Node node) {
        super(node);
        setLength(length);
    }

    @Override
    public ABiNode getParent() {
        return getPrevious();
    }

    @Override
    public Type getType() {
        return Type.TEXT;
    }

    public void updateText(String text, int change) {
        getNode().setTextContent(text);
        changeLengthRec(change);
    }

    @Override
    public ABiNode getABiNodeAt(int offset, int length, int totalOffset) {
//        System.out.println("getABi Node offset=" + offset + " with length="+length+" at text node '" + getText()+"' with length="+this.length);

        setTotalOffset(totalOffset);

        if (offset <= getLength()) {            // start position in this node
            if (offset+length <= getLength()) {
                return this;                    // end position in this node
            }

            return getParent();                 // end position in following node(s)
        } else {                                
            return null;                        // position is not in this node
        }
    }

    @Override
    public ABiNode addSibling(ABiNode abiNode) {
       throw new UnsupportedOperationException("addSibling at textnode not allowed");
    }

    @Override
    public ABiNode getSibling() {
       throw new UnsupportedOperationException("getSibling at textnode not allowed");
    }

    public String getText() {
        return getNode().getTextContent();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[TEXT ");

        sb.append("length:" );
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

package net.sourceforge.jeuclid.biparser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;

public class BiNode extends ABiNode {

    private ABiNode child;
    private int childOffset;
    private boolean invalid;
    /** dom infos */
    String namespaceURI;
    String eName;
    Attributes attrs;

    // Node
    public BiNode(int childOffset, String namespaceURI, String eName, Attributes attrs) {
        this.childOffset = childOffset;
        this.namespaceURI = namespaceURI;
        this.eName = eName;
        this.attrs = attrs;
    }

    @Override
    public Node createDOMSubtree(Document doc) {
        Element element;
        int i;
        String aName;
        ABiNode tmp;
        Node childNode;

        element = doc.createElementNS(namespaceURI, eName);

        // add attributes
        if (attrs != null) {
            for (i = 0; i < attrs.getLength(); i++) {
                aName = attrs.getLocalName(i); // Attr name

                if ("".equals(aName)) {
                    aName = attrs.getQName(i);
                }

                element.setAttribute(aName, attrs.getValue(i));
            }
        }

        if (child != null) {
            tmp = child;

            while (tmp != null) {
                childNode = tmp.createDOMSubtree(doc);

                if (childNode != null) {
                    element.appendChild(childNode);
                }

                tmp = tmp.getSibling();
            }
        }

        namespaceURI = null;
        eName = null;
        attrs = null;

        setNode(element);
        return element;
    }

    public String getNodeName() {
        if (eName != null) {
            return eName;
        } else if (getNode() != null) {
            return getNode().getNodeName();
        } else {
            return null;
        }
    }

    /**
     * @param offset to parent
     */
    public ABiNode addChild(ABiNode abiNode) {
        if (child == null) {                                    // 1st child
            return setChild(abiNode);
        } else {                                                // 2nd - nth child
            if (abiNode.getType() == Type.TEXT) {
                throw new RuntimeException("addChild: cannot add textnode");
            }

            child.addSibling(abiNode);

            if (abiNode.getType() == Type.NODE) {
                return abiNode;
            } else {
                return this;
            }
        }
    }

    public ABiNode setChild(ABiNode child) {
        if (child != null) {
            child.setPrevious(this);
        }

        this.child = child;
        return child;
    }

    public boolean hasChild() {
        return child != null;
    }

    public ABiNode getChild() {
        return child;
    }

    @Override
    public Type getType() {
        return Type.NODE;
    }

    @Override
    public void insert(BiTree biTree, int offset, int length, int totalOffset) {
        setTotalOffset(totalOffset);

        System.out.println("insert " + toString() + " offset=" + offset + " length=" + length);

        // ---------------- SIBLING ----------------
        if (offset >= getLength()) {
            System.out.println("forward to sibling");

            getSibling().insert(biTree, offset - getLength(), length, totalOffset + getLength());   // forward to sibling

        } // ---------------- CHILDREN ----------------
        else if (offset >= childOffset && offset <= childOffset + getLengthOfChildren()) {
            System.out.println("forward to child");

            child.insert(biTree, offset - childOffset, length, totalOffset + childOffset);
        } else {

            System.out.println("start position in tags");

            if (invalid == true) {
                reparse(biTree, biTree.getText().substring(totalOffset, totalOffset+getLength()+length));
            } else {
                addInvalidTextNode(biTree, length);
            }
        }
    }

    @Override
    public void remove(BiTree biTree, int offset, int length, int totalOffset) {
        setTotalOffset(totalOffset);

        System.out.println("remove " + toString() + " offset=" + offset + " length=" + length);

        // ---------------- SIBLING ----------------
        if (offset >= getLength()) {
            System.out.println("forward to sibling");

            getSibling().remove(biTree, offset - getLength(), length, totalOffset + getLength());   // forward to sibling

        } // ---------------- CHILDREN ----------------
        else if (offset >= childOffset && offset + length <= childOffset + getLengthOfChildren()) {
            System.out.println("forward to child");

            child.remove(biTree, offset - childOffset, length, totalOffset + childOffset);
        } else {

            System.out.println("start & end positions in tags");

            if (invalid == true) {
                reparse(biTree, biTree.getText().substring(totalOffset, totalOffset+getLength()-length));
            } else {
                addInvalidTextNode(biTree, -length);
            }
        }
    }

    private void addInvalidTextNode(BiTree biTree, int length) {
        Node parent;
        Element element;

        // add INVALID-textnode to DOM tree
        element = ((Document)biTree.getDocument()).createElement("mi");
        element.setAttribute("mathcolor", "#F00");
        element.appendChild(((Document)biTree.getDocument()).createTextNode("#"));

        // insert INVALID-textnode, remove invalid subtree in DOM tree
        parent = getNode().getParentNode();
        parent.insertBefore(element, getNode());
        parent.removeChild(getNode());

        setNode(element);

        // remove bi-subtree
        child = null;
        invalid = true;
        changeLengthRec(length);

        System.out.println("add invalid: parebt"+getParent());
    }

    private void reparse(BiTree biTree, String text) {
        BiTree treePart;
        Node domValid;
        BiNode parent;

        System.out.println("INVALID TAG - reparse '"+text.replaceAll("\n", "#")+"'");

        treePart = SAXBiParser.getInstance().parse(text);

        if (treePart != null) {
            parent = (BiNode) getParent();

            treePart.getRoot().addSibling(getSibling());

            if (getPrevious() == parent) {             // invalid node is 1st child
                ((BiNode)getParent()).setChild(treePart.getRoot());
            } else {                                        // 2nd - nth child
                getPrevious().setSibling(treePart.getRoot());
            }

            domValid = treePart.getRoot().createDOMSubtree((Document) biTree.getDocument());

            parent.getNode().insertBefore(domValid, getNode());
            parent.getNode().removeChild(getNode());

            parent.changeLengthRec(treePart.getRoot().getLength() - getLength());

            invalid = false;
        }
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public int getLengthOfChildren() {
        int length = 0;
        ABiNode childTmp;

        if (child != null) {
            length += child.getLength();            // length of first child

            childTmp = child.getSibling();
            while (childTmp != null) { // length of 2nd - nth children
                length += childTmp.getLength();
                childTmp =
                        childTmp.getSibling();
            }

        }

        return length;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[" + (invalid == false ? "" : "INVALID ") + "NODE ");

        sb.append("length: ");
        sb.append(getLength());
        sb.append(" <");
        sb.append(getNodeName());
        sb.append(">]");

        return sb.toString();
    }

    @Override
    public String toString(int level) {
        StringBuffer sb = new StringBuffer(formatLength());
        String nl = System.getProperty("line.separator");

        sb.append(":");
        for (int i = 0; i <=
                level; i++) {
            sb.append(" ");
        }

        if (invalid == true) {
            sb.append("INVALID ");
        }

        if (invalid == false) {
            sb.append("<");
            sb.append(getNodeName());
            sb.append(">");
            
            sb.append(" tag: ");
            if (childOffset < 100) {
                sb.append("0");
                if (childOffset < 10) {
                    sb.append("0");
                }

            }
            sb.append(childOffset);
        }

        sb.append(nl);

        if (child != null) {
            sb.append(child.toString(level + 1));
            if (getSibling() != null) {
                sb.append(nl);
            }

        }

        if (getSibling() != null) {
            sb.append(getSibling().toString(level));
        }

        return sb.toString();
    }
}

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
        boolean reparseResult;

        setTotalOffset(totalOffset);

        System.out.println("insert " + toString() + " offset=" + offset + " length=" + length);

        // ---------------- SIBLING ----------------
        if (offset >= getLength()) {
            System.out.println("forward to sibling");

            if (getSibling() == null) {
                // ??
                // new empty node
            } else {
                getSibling().insert(biTree, offset - getLength(), length, totalOffset + getLength());   // forward to sibling
            }

        } // ---------------- CHILDREN ----------------
        else if (!invalid && offset >= childOffset && offset <= childOffset + getLengthOfChildren()) {
            System.out.println("forward to child");

            // new textchild
            if (child == null) {
                // reparse ??
            } else {
                child.insert(biTree, offset - childOffset, length, totalOffset + childOffset);
            }
        } // ---------------- THIS ----------------
        else {

            System.out.println("start position in tags, invalid=" + invalid);

            reparseResult = reparse(biTree, biTree.getText().substring(totalOffset, totalOffset + getLength() + length), length);

            if (invalid == false && reparseResult == false) {
                addInvalidTextNode(biTree);
            }
        }
    }

    @Override
    public void remove(BiTree biTree, int offset, int length, int totalOffset) {
        boolean reparseResult;

        setTotalOffset(totalOffset);

        System.out.println("remove " + toString() + " offset=" + offset + " length=" + length);

        // ---------------- remove node ----------------
        if (offset == 0 && length >= getLength()) {

            this.remove(biTree);

            // concat empty nodes
            if (getPrevious() != null && getSibling() != null &&
                    getPrevious().getType() == ABiNode.Type.EMPTY &&
                    getSibling().getType() == ABiNode.Type.EMPTY) {


                System.out.println("concat empty nodes " + getPrevious() + " / " + getSibling());

                getPrevious().setLength(getSibling().getLength() + getPrevious().getLength());
                getPrevious().setSibling(getSibling().getSibling());

                // forward remainder to 'new sibling'
                if (length > getLength()) {
                    getPrevious().remove(biTree, 0, length - getLength(), totalOffset);
                }
            } else {
                // forward remainder to sibling
                if (length > getLength()) {
                    getSibling().remove(biTree, 0, length - getLength(), totalOffset);
                }
            }
        } // ---------------- SIBLING ----------------
        else if (offset >= getLength()) {
            System.out.println("forward to sibling");

            // new textchild
            if (getSibling() == null) {
                // reparse ??
            } else {
                getSibling().remove(biTree, offset - getLength(), length, totalOffset + getLength());   // forward to sibling
            }
        } // ---------------- CHILDREN ----------------
        else if (!invalid && offset >= childOffset && offset + length <= childOffset + getLengthOfChildren()) {
            child.remove(biTree, offset - childOffset, length, totalOffset + childOffset);
        } // ---------------- THIS ----------------
        else {

            System.out.println("start & end positions in tags");

            reparseResult = reparse(biTree, biTree.getText().substring(totalOffset, totalOffset + getLength() - length), -length);

            if (invalid == false && reparseResult == false) {
                addInvalidTextNode(biTree);
            }
        }
    }

    private void remove(BiTree biTree) {
        System.out.println("remove node " + toString() + " parent=" + getParent() + " previous=" + getPrevious() + " sibling=" + getSibling());

        if (getParent() == null) {          // node is root

            if (getPrevious() == null) {        // no emtpy text on left side
                biTree.setRoot(getSibling());
            } else {                            // empty text on left side
                getPrevious().setSibling(getSibling());
            }

        } else {
            getParent().changeLengthRec(-getLength());

            if (getPrevious() == getParent()) {      // node is 1st child
                ((BiNode) getParent()).setChild(getSibling());
            } else {                                        // node is 2nd - nth child
                getPrevious().setSibling(getSibling());
            }
        }
    }

    private void addInvalidTextNode(BiTree biTree) {
        Document doc;
        Element element;

        // create INVALID-textnode in DOM tree
        doc = (Document) biTree.getDocument();
        element = doc.createElement("mi");
        element.setAttribute("mathcolor", "#F00");
        element.appendChild(doc.createTextNode("#"));

        if (getNode().getParentNode() == null) {
            biTree.getDocument().replaceChild(element, getNode());
        } else {
            getNode().getParentNode().replaceChild(element, getNode());
        }

        // remove bi-subtree
        setNode(element);
        child = null;
        invalid = true;
    }

    private boolean reparse(BiTree biTree, String text, int length) {
        BiTree treePart;
        Node domValid;
        BiNode parent;

        System.out.println("INVALID TAG - reparse '" + text.replaceAll("\n", "#") + "'");

        treePart = SAXBiParser.getInstance().parse(text);

        // parse successfull
        if (treePart != null) {

            System.out.println("reparse successfull");

            parent = (BiNode) getParent();

            domValid = treePart.getRoot().createDOMSubtree((Document) biTree.getDocument());

            treePart.getRoot().addSibling(getSibling());

            if (parent == null) {          // node is root

                if (getPrevious() == null) {    // no emtpy text
                    biTree.setRoot(treePart.getRoot());
                } else {                        // empty text on left side of root
                    getPrevious().setSibling(treePart.getRoot());
                }

                // replace invalid DOM node
                biTree.getDocument().replaceChild(domValid, getNode());

            } else {
                if (getPrevious() == parent) {                      // invalid node is 1st child
                    ((BiNode) getParent()).setChild(treePart.getRoot());
                } else {                                            // 2nd - nth child
                    getPrevious().setSibling(treePart.getRoot());
                }

                // replace invalid DOM node
                parent.getNode().replaceChild(domValid, getNode());
                parent.changeLengthRec(length);
            }

            invalid = false;
            return true;
        } else {
            System.out.println("reparse not successfull");

            changeLengthRec(length);
            return false;
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

            childTmp =
                    child.getSibling();
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

        if (!invalid) {
            sb.append(" <");
            sb.append(getNodeName());
            sb.append(">");
        }

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

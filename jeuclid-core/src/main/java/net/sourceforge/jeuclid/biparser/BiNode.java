package net.sourceforge.jeuclid.biparser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;

/**
 * this class is used to store specific information about a composite xml-node
 * the node can have one child, many attributes and can be invalid
 *
 * @author dominik
 */
public class BiNode extends ABiNode {

    /** child node */
    private ABiNode child;
    /** offset to child from node begin (length of open tag) */
    private int childOffset;
    /** if false, node is a valid xml-node */
    private boolean invalid;
    /** DOM-info: namespaceURI */
    private String namespaceURI;
    /** DOM-info: tag-name */
    private String eName;
    /** DOM-info: attributes of node */
    private Attributes attrs;

    /**
     * creates a new node with size 0, must be set afterwards,
     * constructor does not create a DOM-node */
    public BiNode(int childOffset, String namespaceURI, String eName, Attributes attrs) {
        this.childOffset = childOffset;
        this.namespaceURI = namespaceURI;
        this.eName = eName;
        this.attrs = attrs;
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
            if (abiNode.getType() == BiType.TEXT) {
                throw new RuntimeException("addChild: cannot add textnode");
            }

            child.addSibling(abiNode);

            if (abiNode.getType() == BiType.NODE) {
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
    public BiType getType() {
        return BiType.NODE;
    }

    @Override
    public void insert(BiTree biTree, int offset, int length, int totalOffset) throws ReparseException {
        // System.out.println("insert " + toString() + " offset=" + offset + " length=" + length);

        // ---------------- end of this or SIBLING ----------------
        if (offset >= getLength()) {

            // reparse if node is invalid and start position is at node-end
            if (offset == getLength() && invalid) {
                throw new ReparseException();
            }

            // forward to sibling
            forwardToSibling(true, biTree, offset - getLength(), length, totalOffset + getLength());

        } // ---------------- CHILDREN ----------------
        else if (child != null && !invalid && offset >= childOffset && offset <= childOffset + getLengthOfChildren()) {
            try {
                child.insert(biTree, offset - childOffset, length, totalOffset + childOffset);
            } catch (ReparseException ex) {
                parseAndReplace(biTree, biTree.getText().substring(totalOffset, totalOffset + getLength() + length), length);
            }
        } // ---------------- before THIS ----------------
        else if (offset == 0) {
            throw new ReparseException();
        } // ---------------- THIS ----------------
        else {
            parseAndReplace(biTree, biTree.getText().substring(totalOffset, totalOffset + getLength() + length), length);
        }
    }

    @Override
    public void remove(BiTree biTree, int offset, int length, int totalOffset) throws ReparseException {
        // System.out.println("remove " + toString() + " offset=" + offset + " length=" + length);

        // ---------------- REMOVE THIS ----------------
        if (offset == 0 && length >= getLength()) {
            throw new ReparseException();

        } // ---------------- SIBLING ----------------
        else if (offset >= getLength()) {
            forwardToSibling(false, biTree, offset - getLength(), length, totalOffset + getLength());

        } // ---------------- CHILDREN ----------------
        else if (child != null && !invalid && offset >= childOffset && offset + length <= childOffset + getLengthOfChildren()) {
            try {
                child.remove(biTree, offset - childOffset, length, totalOffset + childOffset);
            } catch (ReparseException ex) {
                parseAndReplace(biTree, biTree.getText().substring(totalOffset, totalOffset + getLength() - length), -length);
            }
        } // ---------------- THIS ----------------
        else {
            parseAndReplace(biTree, biTree.getText().substring(totalOffset, totalOffset + getLength() - length), -length);
        }
    }

    private void makeInvalidNode(BiTree biTree) {
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

    private void parseAndReplace(BiTree biTree, String text, int length) throws ReparseException {
        BiTree treePart;
        Node domValid;
        BiNode parent;

        treePart = SAXBiParser.getInstance().parse(text);

        // parse successfull
        if (treePart != null) {

            parent = (BiNode) getParent();
            domValid = treePart.getDOMTree((Document) biTree.getDocument());
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
        } else {
            // if node and previous or node and sibling are invalid - reparse parent
            if ((getPrevious() != null && getPrevious().getType() == BiType.NODE && ((BiNode) getPrevious()).invalid) ||
                    (getSibling() != null && getSibling().getType() == BiType.NODE && ((BiNode) getSibling()).invalid)) {
                throw new ReparseException();
            }

            if (invalid == false) {
                makeInvalidNode(biTree);
            }

            changeLengthRec(length);
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
    public Node createDOMSubtree(Document doc) {
        int i;
        String aName;
        Node childNode;
        Element element;
        ABiNode tmp;

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

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[" + (invalid == false ? "" : "INVALID ") + "NODE ");

        sb.append("length: ");
        sb.append(getLength());

        if (!invalid) {
            sb.append(" <");
            sb.append(getNodeName());
            sb.append("> tag: ");
            sb.append(childOffset);
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

package net.sourceforge.jeuclid.biparser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;

/**
 * this class is used to store specific information about a composite xml-node.
 * the node can have one child, many attributes and can be invalid
 *
 * @author dominik
 */
public class BiNode extends ABiNode {

    /** child node. */
    private ABiNode child;
    /** offset to child from node begin (length of open tag). */
    private int childOffset;
    /** if false, node is a valid xml-node. */
    private boolean invalid;
    /** DOM-info: namespaceURI. */
    private String namespaceURI;
    /** DOM-info: tag-name. */
    private String eName;
    /** DOM-info: attributes of node. */
    private Attributes attrs;

    /**
     * creates a new BiNode, length must be set afterwards.
     * constructor does not create a DOM-node
     * @param co offset to child from node begin (length of open tag)
     * @param ns DOM-info
     * @param n DOM-info
     * @param a DOM-info
     */
    public BiNode(final int co, final String ns, final String n,
            final Attributes a) {
        childOffset = co;
        namespaceURI = ns;
        eName = n;
        attrs = a;
    }

    /**
     * get the name of the node (tagname).
     * @return nodename
     */
    public final String getNodeName() {
        if (eName == null) {
            if (getNode() == null) {
                return null;
            } else {
                return getNode().getNodeName();
            }
        } else {
            return eName;
        }
    }

    /**
     * add a child to this node, if node has already a child, forward to child.
     * @param c new child for this node
     */
    public final void addChild(final ABiNode c) {
        if (child == null) {                    // 1st child
            setChild(c);
        } else {                                // 2nd - nth child
            child.addSibling(c);
        }
    }

    /**
     * get the child of the node.
     * @return child
     */
    public final ABiNode getChild() {
        return child;
    }

    /**
     * set child for this node.
     * @param c new child for this node
     */
    public final void setChild(final ABiNode c) {
        if (c != null) {
            setPrevious(this);
        }

        child = c;
    }

    /**
     * set the childOffset for this node.
     * @param c new childOffset for this node
     */
    public final void setChildOffset(final int c) {
        childOffset = c;
    }

    /**
     * get the type of node.
     * @return NODE
     */
    @Override
    public final BiType getType() {
        return BiType.NODE;
    }

    /** {@inheritDoc} */
    @Override
    public final void insert(final BiTree biTree, final int offset,
            final int length, final int totalOffset)
            throws ReparseException {
        // System.out.println("insert " + toString() + " offset=" + offset +
        // " length=" + length);

        // ---------------- end of this or SIBLING ----------------
        if (offset >= getLength()) {

            // reparse if node is invalid and start position is at node-end
            if (offset == getLength() && invalid) {
                throw new ReparseException();
            }

            // forward to sibling
            forwardToSibling(true, biTree, offset - getLength(), length,
                    totalOffset + getLength());

        } // ---------------- CHILDREN ----------------
        else if (child != null && !invalid && offset >= childOffset &&
                offset <= childOffset + getLengthOfChildren()) {
            try {
                child.insert(biTree, offset - childOffset, length,
                        totalOffset + childOffset);
            } catch (ReparseException ex) {
                parseAndReplace(biTree, biTree.getText().substring(totalOffset,
                        totalOffset + getLength() + length), length);
            }
        } // ---------------- before THIS ----------------
        else if (offset == 0) {
            throw new ReparseException();
        } // ---------------- THIS ----------------
        else {
            parseAndReplace(biTree, biTree.getText().substring(totalOffset,
                    totalOffset + getLength() + length), length);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void remove(final BiTree biTree, final int offset,
            final int length, final int totalOffset) throws ReparseException {
        // System.out.println("remove " + toString() + " offset=" +
        // offset + " length=" + length);

        // ---------------- REMOVE THIS ----------------
        if (offset == 0 && length >= getLength()) {
            throw new ReparseException();

        } // ---------------- SIBLING ----------------
        else if (offset >= getLength()) {
            forwardToSibling(false, biTree, offset - getLength(), length,
                    totalOffset + getLength());

        } // ---------------- CHILDREN ----------------
        else if (child != null && !invalid && offset >= childOffset &&
                offset + length <= childOffset + getLengthOfChildren()) {
            try {
                child.remove(biTree, offset - childOffset, length,
                        totalOffset + childOffset);
            } catch (ReparseException ex) {
                parseAndReplace(biTree, biTree.getText().substring(totalOffset,
                        totalOffset + getLength() - length), -length);
            }
        } // ---------------- THIS ----------------
        else {
            parseAndReplace(biTree, biTree.getText().substring(totalOffset,
                    totalOffset + getLength() - length), -length);
        }
    }

    /**
     * set the node as invalid, remove all children.
     * replace node in DOM-tree with a red '#'
     * @param doc Document to insert the invalid mark '#'
     */
    private void makeInvalidNode(final Document doc) {
        Element element;

        // create INVALID-textnode in DOM tree
        element = doc.createElement("mi");
        element.setAttribute("mathcolor", "#F00");
        element.appendChild(doc.createTextNode("#"));

        if (getNode().getParentNode() == null) {
            doc.replaceChild(element, getNode());
        } else {
            getNode().getParentNode().replaceChild(element, getNode());
        }

        // remove bi-subtree
        setNode(element);
        child = null;
        invalid = true;
    }

    /**
     * try to parse the text, if valid replace this node with parsed tree.
     * else replace this node with invalid mark '#'
     * @param biTree reference to BiTree to which this node contains
     * @param text to parse
     * @param length change length of this node
     */
    private void parseAndReplace(final BiTree biTree, final String text,
            final int length)
            throws ReparseException {
        BiTree treePart;
        Node domValid;
        BiNode parent;

        treePart = SAXBiParser.getInstance().parse(text);

        // parse successfull
        if (treePart == null) {
            // if node & previous or node & sibling are invalid - reparse parent
            if ((getPrevious() != null &&
                    getPrevious().getType() == BiType.NODE &&
                    ((BiNode) getPrevious()).invalid) ||
                    (getSibling() != null &&
                    getSibling().getType() == BiType.NODE &&
                    ((BiNode) getSibling()).invalid)) {
                throw new ReparseException();
            }

            if (!invalid) {
                makeInvalidNode((Document) biTree.getDocument());
            }

            changeLengthRec(length);

        } else {
            
            parent = (BiNode) getParent();
            domValid = treePart.getDOMTree((Document) biTree.getDocument());
            treePart.getRoot().addSibling(getSibling());

            if (parent == null) {          // node is root

                if (getPrevious() == null) { // no emtpy text
                    biTree.setRoot(treePart.getRoot());
                } else {                     // empty text on left side of root
                    getPrevious().setSibling(treePart.getRoot());
                }

                // replace invalid DOM node
                biTree.getDocument().replaceChild(domValid, getNode());

            } else {
                if (getPrevious() == parent) {      // invalid node is 1st child
                    ((BiNode) getParent()).setChild(treePart.getRoot());
                } else {                            // 2nd - nth child
                    getPrevious().setSibling(treePart.getRoot());
                }

                // replace invalid DOM node
                parent.getNode().replaceChild(domValid, getNode());
                parent.changeLengthRec(length);
            }

            invalid = false;
        }
    }

    /**
     * calculate the length of all children.
     * @return length of children
     */
    public int getLengthOfChildren() {
        int length = 0;
        ABiNode childTmp;

        if (child != null) {
            length += child.getLength();        // length of first child

            childTmp = child.getSibling();
            while (childTmp != null) {          // length of 2nd - nth children
                length += childTmp.getLength();
                childTmp = childTmp.getSibling();
            }

        }

        return length;
    }

    /**
     * create a DOM-tree from node and all children (recursive).
     * @param doc Document to create DOM-tree
     * @return root of DOM-tree
     */
    @Override
    public final Node createDOMSubtree(final Document doc) {
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

        // create DOM-tree of children
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

    /** {@inheritDoc} */
    @Override
    public final int searchNode(final Node node, final int totalOffset) {
        int result;

        // check if node is this
        result = super.searchNode(node, totalOffset);
        if (result >= 0) {
            return result;
        }

        // forward to child
        if (child != null) {
            result = child.searchNode(node, totalOffset + childOffset);
            if (result >= 0) {
                return result;
            }
        }

        // forward to sibling
        if (getSibling() != null) {
            return getSibling().searchNode(node, totalOffset + getLength());
        }

        return -1;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(32);

        sb.append('[');
        sb.append(invalid ? "INVALID " : "");
        sb.append("NODE length: ");
        sb.append(getLength());

        if (!invalid) {
            sb.append(" <");
            sb.append(getNodeName());
            sb.append("> tag: ");
            sb.append(childOffset);
        }

        sb.append(']');

        return sb.toString();
    }

    @Override
    public String toString(final int level) {
        final StringBuffer sb = new StringBuffer(32);
        final String nl = System.getProperty("line.separator");

        sb.append(formatLength());
        sb.append(':');
        for (int i = 0; i <= level; i++) {
            sb.append(' ');
        }

        if (invalid) {
            sb.append("INVALID ");
        }

        if (!invalid) {
            sb.append('<');
            sb.append(getNodeName());
            sb.append("> tag: ");
            if (childOffset < 100) {
                sb.append('0');
                if (childOffset < 10) {
                    sb.append('0');
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

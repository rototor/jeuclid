package net.sourceforge.jeuclid.biparser;

import java.util.ArrayList;

import net.sourceforge.jeuclid.elements.generic.DocumentElement;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;

/**
 * this class if for creating a BiTree with ABiNodes while parsing a text.
 */
public class BiTree {

    /** document (DOM-tree). */
    private Document doc;
    /** current position in tree. */
    private ABiNode currentBiTree;
    /** root of tree. */
    private ABiNode root;
    /** save positions of open tags. */
    private final ArrayList<Integer> startPositions;
    /** text of tree. */
    private String text;

    /**
     * create a new BiTree.
     */
    public BiTree() {
        startPositions = new ArrayList<Integer>();
    }

    /**
     * create a new DOM tree from bitree and save it.
     */
    public final void createDOMTree() {
        Node subtree;

        doc = new DocumentElement();

        subtree = getDOMTree(doc);

        if (subtree != null) {
            doc.appendChild(subtree);
        }
    }

    /**
     * create a dom tree from bitree and return root.
     * @param d document to create DOM tree
     * @return root of DOM tree
     */
    public final Node getDOMTree(final Document d) {
        Node treeRoot;

        if (root.getType() == BiType.EMPTY) {
            treeRoot = root.getSibling().createDOMSubtree(d);
        } else {
            treeRoot = root.createDOMSubtree(d);
        }

        return treeRoot;
    }

    /**
     * get root of BiTree.
     * @return root of BiTree
     */
    public final ABiNode getRoot() {
        return root;
    }

    /**
     * create and append a new BiNode at current position in BiTree.
     * @param totalOffset of node in text
     * @param childOffset position of first child (length of open tag)
     * @param namespaceURI namespace
     * @param eName name of node
     * @param attrs attributes of node
     */
    public final void createBiNode(final int totalOffset, final int childOffset,
            final String namespaceURI, final String eName,
            final Attributes attrs) {

        BiNode biNode;

        startPositions.add(totalOffset);

        // new root node
        if (root == null) {
            root = new BiNode(childOffset, namespaceURI, eName, attrs);
            currentBiTree = root;
        } else {
            biNode = new BiNode(childOffset, namespaceURI, eName, attrs);

            // append child (only possible at start
            if (currentBiTree.getType() == BiType.EMPTY) {
                currentBiTree.addSibling(biNode);
            } else { // add child (default case)
                ((BiNode) currentBiTree).addChild(biNode);
            }

            currentBiTree = biNode;
        }
    }

    /**
     * close BiNode (set length of node).
     * @param length length of node
     */
    public final void closeBiNode(final int length) {
        BiNode parent;

        currentBiTree.setLength(length
                - startPositions.get(startPositions.size() - 1));

        // move current position to parent
        parent = currentBiTree.getParent();
        if (parent != null) {
            currentBiTree = parent;
        }

        startPositions.remove(startPositions.size() - 1);
    }

    /**
     * check if currentposition in BiTree allows a TextNode as child.
     * @return true if a TextNode is allowed
     */
    public final boolean allowNewTextNode() {
        return ((BiNode) currentBiTree).getChild() == null;
    }

    /**
     * create a new TextNode at current position.
     * @param length length of TextNode
     * @param t text of TextNode
     */
    public final void createTextNode(final int length, final String t) {
        ((BiNode) currentBiTree).addChild(new TextNode(length, t));
    }

    /**
     * create a new EmptyNode at current position in BiTree.
     * @param length length of EmtpyNode
     */
    public final void createEmtpyNode(final int length) {
        // EmptyNode is new root
        if (root == null) {
            root = new EmptyNode(length);
            currentBiTree = root;
        } else {
            ((BiNode) currentBiTree).addChild(new EmptyNode(length));
        }
    }

    /**
     * insert characters into BiTree.
     * @param offset insert position in text
     * @param length number of characters to insert
     * @param t text where characters were inserted
     * @throws ReparseException if a sax parse exception occurs
     */
    public final void insert(final int offset, final int length,
            final String t) throws ReparseException {
        text = t;
        root.insert(this, offset, length, 0);
    }

    /**
     * remove characters from BiTree.
     * @param offset remove position in text
     * @param length number of characters to remove
     * @param t text where characters were removed
     * @throws ReparseException
     */
    public final void remove(final int offset, final int length,
            final String t) throws ReparseException {
        text = t;
        root.remove(this, offset, length, 0);
    }

    /**
     * set a new root in BiTree.
     * @param root new root of BiTree
     */
    public final void setRoot(final ABiNode r) {

        if (r == null) {
            doc = null;
        } else {
            root.setPrevious(null);
        }

        root = r;
    }

    /**
     * get text of BiTree.
     * @return text of BiTree
     */
    public String getText() {
        return text;
    }

    /**
     * get document of DOM Tree.
     * @return document of DOM Tree
     */
    public Node getDocument() {
        return doc;
    }

    /**
     * search a DOM node in BiTree and return position of node.
     * if node is not found return -1
     * @param node DOM node to search for
     * @return position of node in inputtext
     */
    public int searchNode(final Node node) {
        if (root == null) {
            return -1;
        } else {
            return root.searchNode(node, 0);
        }
    }

    /**
     * get a formatted output of BiTree
     * @return formatted output of BiTree
     */
    @Override
    public String toString() {
        if (root == null) {
            return "root is null";
        } else {
            return root.toString(0);
        }
    }

    /**
     * get formatted output of DOM Tree (for debugging)
     * @return formatted ouput of DOM Tree
     */
    public String toStringDOM() {
        return toStringDOM(0, doc.getDocumentElement());
    }

    private String toStringDOM(final int level, final Node n) {
        int i;
        NodeList nl;
        final StringBuilder sb = new StringBuilder(128);

        if (n == null) {
            return "node is null";
        }

        for (i = 0; i < level; i++) {
            sb.append(" ");
        }

        sb.append(" ");
        if (n.getNodeType() == 3) {
            sb.append(n.getTextContent());
        } else {
            sb.append(n.getNodeName());
        }
        sb.append("\n");

        nl = n.getChildNodes();
        for (i = 0; i < nl.getLength(); i++) {
            sb.append(toStringDOM(level + 1, nl.item(i)));
        }

        return sb.toString();
    }
}

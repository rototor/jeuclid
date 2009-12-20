package net.sourceforge.jeuclid.biparser;

import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.sourceforge.jeuclid.elements.generic.DocumentElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;

public class BiTree {

    private Document doc;
    private ABiNode currentBiTree;
    private ABiNode root;
    /** save positions of open tags */
    private ArrayList<Integer> startPositions;
    private String text;

    public BiTree() {
        startPositions = new ArrayList<Integer>();
    }

    public void createDOMTree() {
        Node rootChild;

        doc = new DocumentElement();
/*
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;

        try {
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(BiTree.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        doc = docBuilder.newDocument();*/

        if (root.getType() == ABiNode.Type.EMPTY) {
            rootChild = root.getSibling().createDOMSubtree(doc);
        } else {
            rootChild = root.createDOMSubtree(doc);
        }

        if (rootChild != null) {
            doc.appendChild(rootChild);
        }
    }

    public ABiNode getRoot() {
        return root;
    }

    public void newElement(int offset, int childOffset, String namespaceURI, String eName, Attributes attrs) {

        /*   TODO???
        if ((namespaceURI == null || namespaceURI.equals("")) && doc != null && doc.getFirstChild() != null) {
        namespaceURI = doc.getFirstChild().getNamespaceURI();
        } */

        startPositions.add(offset);

        if (root == null) {
            root = new BiNode(childOffset, namespaceURI, eName, attrs);     // new root node
            currentBiTree = root;
        } else {
            if (currentBiTree.getType() == ABiNode.Type.EMPTY) {        // node is empty node, only possible at start
                currentBiTree = currentBiTree.addSibling(new BiNode(childOffset, namespaceURI, eName, attrs));
            } else {                                                    // node (default case)
                currentBiTree = ((BiNode) currentBiTree).addChild(new BiNode(childOffset, namespaceURI, eName, attrs));
            }
        }
    }

    public void closeElement(int end) {
        ABiNode parent;
        ((BiNode) currentBiTree).setLength(end - startPositions.get(startPositions.size() - 1));

        parent = currentBiTree.getParent();
        if (parent != null) {
            currentBiTree = parent;
        }

        startPositions.remove(startPositions.size() - 1);
    }

    public boolean allowNewTextNode() {
        return !((BiNode) currentBiTree).hasChild();
    }

    public void newTextNode(int offset, int nodeLength, String text) {
        ((BiNode) currentBiTree).addChild(new TextNode(nodeLength, text));
    }

    public void newEmtpyNode(int offset, int length) {
        if (root == null) {                               // root
            root = new EmptyNode(length);
            currentBiTree = root;
        } else {
            ((BiNode) currentBiTree).addChild(new EmptyNode(length));
        }
    }

    public void insert(int offset, int length, String text) {
        this.text = text;
        root.insert(this, offset, length, 0);
    }

    public void remove(int offset, int length, String text) {
        this.text = text;
        root.remove(this, offset, length, 0);
    }

    public void setRoot(ABiNode root) {

        // check if root
        if (root == null) {
            doc = null;
        }

        this.root = root;
    }

    public String getText() {
        return text;
    }

    public Node getDocument() {
        return doc;
    }

    @Override
    public String toString() {
        return root.toString(0);
    }

    public String toStringDOM() {
        return toStringDOM(0, doc.getDocumentElement());
    }

    private String toStringDOM(int level, Node n) {
        int i;
        NodeList nl;
        StringBuilder sb = new StringBuilder();

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

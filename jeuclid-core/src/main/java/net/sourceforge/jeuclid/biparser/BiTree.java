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
    private Node currentDOM;
    private ABiNode currentBiTree;
    private ABiNode root;
    private ArrayList<Integer> startPositions;
    private String text;

    public BiTree() {
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
        currentDOM = doc;

        startPositions = new ArrayList<Integer>();
    }

    public void newElement(int offset, int childOffset, String namespaceURI, String eName, Attributes attrs) {
        Element element;
        int i;

        startPositions.add(offset);
        
    /*   TODO???
        if ((namespaceURI == null || namespaceURI.equals("")) && doc != null && doc.getFirstChild() != null) {
            namespaceURI = doc.getFirstChild().getNamespaceURI();
        } */

        element = doc.createElementNS(namespaceURI, eName);

        // add attributes
        if (attrs != null) {
            for (i = 0; i < attrs.getLength(); i++) {
                String aName = attrs.getLocalName(i); // Attr name

                if ("".equals(aName)) {
                    aName = attrs.getQName(i);
                }

                element.setAttribute(aName, attrs.getValue(i));
            }
        }
        
        if (root == null) {                               // root
            root = new BiNode(childOffset, element);
            currentBiTree = root;
        } else {
            if (currentBiTree.getType() == ABiNode.Type.EMPTY) {        // node is empty node, only possible at start
                currentBiTree = currentBiTree.addSibling(new BiNode(childOffset, element));
            } else {                                                    // node (default case)
                currentBiTree = ((BiNode) currentBiTree).addChild(new BiNode(childOffset, element));
            }
        }

        currentDOM.appendChild(element);
        currentDOM = element;
    }

    public void closeElement(int end) {
        ABiNode parent;
        ((BiNode) currentBiTree).setLength(end - startPositions.get(startPositions.size() - 1));

        parent = currentBiTree.getParent();
        if (parent != null) {
            currentBiTree = parent;
        }

        currentDOM = currentDOM.getParentNode();
        startPositions.remove(startPositions.size() - 1);
    }

    public boolean allowNewTextNode() {
        return !currentDOM.hasChildNodes();
    }

    public void newTextNode(int offset, int nodeLength, String text) {
        Text t;

        // DOM
        t = doc.createTextNode(text);
        currentDOM.appendChild(t);

        // BiTree
        ((BiNode) currentBiTree).addChild(new TextNode(nodeLength, t));
    }

    public Node createInvalidNode() {
        Element element;

        element = doc.createElement("mi");
        element.setAttribute("mathcolor", "#F00");
        element.appendChild(doc.createTextNode("#"));

        return element;
    }

    public void newEmtpyNode(int offset, int length) {
        if (root == null) {                               // root
            root = new EmptyNode(length);
            currentBiTree = root;
        } else {
            ((BiNode) currentBiTree).addChild(new EmptyNode(length));
        }
    }

    /**
     * insert a text at offset
     */
    public void insert(int offset, String text) {
        int pos;
        ABiNode node;
        ABiNode oldRoot;
        String oldText, newText;
        Text t;

        node = root.getABiNodeAt(offset, 0, 0);
/*
        System.out.println("-- insert at " + node);

        if (node == null) {         // new root empty node
            oldRoot = root;
            root = new EmptyNode(text.length());
            root.addSibling(oldRoot);
        } else {

            // change text in a textnode
            if (node.getType() == BiNode.Type.TEXT) {
                pos = offset - node.getTotalOffset();
                oldText = ((TextNode) node).getText();
                newText = oldText.substring(0, pos) + text + oldText.substring(pos);

                ((TextNode) node).updateText(newText, text.length());  // TOTO maybe incorret length
            } // change empty node length
            else if (node.getType() == BiNode.Type.EMPTY) {
                ((EmptyNode) node).changeLengthRec(text.length());     // TOTO maybe incorret length
            } // position in node or new child of node
            else {

                /*
                if (((BiNode)node).hasChild()) {    //

                } else {                            // add new text node
                // DOM
                t = doc.createTextNode(text);
                node.getNode().appendChild(t);

                // BiTree
                ((BiNode) node).addChild(new TextNode(text.length(), t));
                }
            }
        }*/
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
    
    /**
     * remove text  at offset
     */
    public void remove(int offset, int length, String text) {
        int pos;
        ABiNode node;
        String oldText, newText;

        //node = root.getABiNodeAt(offset, length, 0);

        this.text = text;
        root.remove(this, offset, length, 0);

        //System.out.println("-- remove at " + node);
/*
        if (node != null) {
            if (node.getType() == BiNode.Type.NODE) {
            } else {
                // remove text in a textnode
                if (node.getType() == BiNode.Type.TEXT) {


                    pos = offset - node.getTotalOffset();
                    oldText = ((TextNode) node).getText();
                    newText = oldText.substring(0, pos) + oldText.substring(pos + length);

                    ((TextNode) node).updateText(newText, -length);  // TOTO maybe incorret length

                } // change empty node length
                else if (node.getType() == BiNode.Type.EMPTY) {

                    if (node.getLength() - length == 0) {    // remove empty node

                        if (node.getParent() == null) {     // empty node is root
                            root = node.getSibling();
                        } else {                            // any other empty node
                            node.getParent().changeLengthRec(-length);

                            if (node.getPrevious() == node.getParent()) {   // empty node is 1st child
                                ((BiNode) node.getParent()).setChild(node.getSibling());
                            } else {                                        // empty node nth child
                                node.getPrevious().setSibling(node.getSibling());
                            }
                        }
                    } else {                                // change size of emtpy node
                        ((EmptyNode) node).changeLengthRec(-length);     // TODO maybe incorret length
                    }
                }
            }
        } else {        // remove root ......
            throw new RuntimeException("todo... Null at remove");
        }*/
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

        for(i=0; i<level; i++) {
            sb.append(" ");
        }

        sb.append(n.getNodeType());
        sb.append(" ");
        sb.append(n.getNodeName());
        sb.append("\n");

        nl = n.getChildNodes();
        for (i=0; i<nl.getLength(); i++) {
            sb.append(toStringDOM(level+1, nl.item(i)));
        }

        return sb.toString();
    }
}

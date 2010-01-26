/*
 * Copyright 2002 - 2007 JEuclid, http://jeuclid.sf.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package net.sourceforge.jeuclid.biparser;

import net.sourceforge.jeuclid.elements.generic.DocumentElement;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * this class if for creating a BiTree with ABiNodes while parsing a text.
 *
 * @version $Revision$
 */
public class BiTree {

    /** document (DOM-tree). */
    private Document doc;
    /** root of tree. */
    private AbstractBiNode root;
    /** text of tree. */
    private String text;

    /**
     * create a new instance of BiTree.
     */
    public BiTree() {

    }

    /**
     * create a new DOM tree from bitree and save it.
     */
    public final void createDOMTree() {
        Node subtree;

        this.doc = new DocumentElement();

        subtree = this.getDOMTree(this.doc);

        if (subtree != null) {
            this.doc.appendChild(subtree);
        }
    }

    /**
     * create a dom tree from bitree and return root.
     * @param d document to create DOM tree
     * @return root of DOM tree
     */
    public final Node getDOMTree(final Document d) {
        Node treeRoot;

        if (this.root.getType() == BiType.EMPTY) {
            treeRoot = this.root.getSibling().createDOMSubtree(d);
        } else {
            treeRoot = this.root.createDOMSubtree(d);
        }

        return treeRoot;
    }

    /**
     * get root of BiTree.
     * @return root of BiTree
     */
    public final AbstractBiNode getRoot() {
        return this.root;
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
        this.text = t;
        this.root.insert(this, offset, length, 0);
    }

    /**
     * remove characters from BiTree.
     * @param offset remove position in text
     * @param length number of characters to remove
     * @param t text where characters were removed
     * @throws ReparseException if a sax parse exception occurs
     */
    public final void remove(final int offset, final int length,
            final String t) throws ReparseException {
        this.text = t;
        this.root.remove(this, offset, length, 0);
    }

    /**
     * set a new root in BiTree.
     * @param r new root of BiTree
     */
    public final void setRoot(final AbstractBiNode r) {

        if (r == null) {
            this.doc = null;
        } else {
            r.setPrevious(null);
        }

        this.root = r;
    }

    /**
     * get text of BiTree.
     * @return text of BiTree
     */
    public String getText() {
        return this.text;
    }

    /**
     * get document of DOM Tree.
     * @return document of DOM Tree
     */
    public Node getDocument() {
        return this.doc;
    }

    /**
     * search a DOM node in BiTree and return position of node.
     * if node is not found return null
     * @param node DOM node to search for
     * @return search result of node in inputtext
     */
    public SearchResult searchNode(final Node node) {
        if (this.root == null) {
            return null;
        } else {
            return this.root.searchNode(node, 0);
        }
    }

    /**
     * get a formatted output of BiTree.
     * @return formatted output of BiTree
     */
    @Override
    public String toString() {
        if (this.root == null) {
            return "root is null";
        } else {
            return root.toString(0);
        }
    }

    /**
     * get formatted output of DOM Tree (for debugging).
     * @return formatted ouput of DOM Tree
     */
    public String toStringDOM() {
        return toStringDOM(0, this.doc.getDocumentElement());
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
            sb.append(this.toStringDOM(level + 1, nl.item(i)));
        }

        return sb.toString();
    }
}

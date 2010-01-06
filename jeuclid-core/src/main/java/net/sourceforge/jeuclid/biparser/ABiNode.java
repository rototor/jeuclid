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

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * this class is used to store typcial information about a xml-node.
 *
 */
public abstract class ABiNode {

    /** previous node, null if node is root. */
    private ABiNode previous;
    /** sibling node, can be null. */
    private ABiNode sibling;
    /** reference to node in DOM-tree. */
    private Node node;
    /** length of node in characters. */
    private int length;

    /**
     * get previous node, null if node is root.
     * @return previous
     */
    public final ABiNode getPrevious() {
        return previous;
    }

    /**
     * set previous for this node.
     * @param prev  previous node for this node
     */
    public final void setPrevious(final ABiNode prev) {
        previous = prev;
    }

    /**
     * get parent node for this node.
     * @return parent
     */
    public final BiNode getParent() {
        if (previous == null) {
            return null;                     // root
        }

        // check if previous isn't a "real parent"
        if (previous.sibling == this) {
            return previous.getParent();
        } else {                             // previous is "real parent"
            return (BiNode) previous;
        }
    }

    /**
     * get sibling node, can be null.
     * @return sibling
     */
    public final ABiNode getSibling() {
        return sibling;
    }

    /**
     * set sibling for this node and set previous of sibling to this.
     * @param sibl new sibling for this node
     */
    public final void setSibling(final ABiNode sibl) {
        if (sibl != null) {
            sibl.previous = this;
        }

        sibling = sibl;
    }

    /**
     * add sibling to a node, not possible at a textnode.
     * if node already has a sibling, forward to sibling.
     * @param sibl new sibling for this node
     */
    public final void addSibling(final ABiNode sibl) {
        if (sibling == null) {                 // 2nd child
            setSibling(sibl);
        } else {                                    // forward(3rd - nth child)
            sibling.addSibling(sibl);
        }
    }

    /**
     * get reference to node in DOM-tree.
     * @return node in DOM-tree
     */
    public final Node getNode() {
        return node;
    }

    /**
     * set reference to node in DOM-tree.
     * @param n reference in DOM-tree
     */
    public final void setNode(final Node n) {
        node = n;
    }

    /**
     * get length of node (number of characters).
     * @return length of node
     */
    public final int getLength() {
        return length;
    }

    /**
     * set length of node.
     * @param len to set
     */
    public final void setLength(final int len) {
        length = len;
    }

    /**
     * change length of node and recursive of all parents.
     * @param change changevalue (can be positive or negative)
     */
    public final void changeLengthRec(final int change) {
        if (change == 0) {
            return;
        }

        length += change;

        if (getParent() != null) {
            getParent().changeLengthRec(change);
        }
    }

    /**
     * get the type of node, can be BiNode, EmptyNode or TextNode.
     * @return type of node
     */
    abstract BiType getType();

    /**
     * insert characters to node.
     * @param biTree reference to BiTree to which this node contains
     * @param offset position to insert characters
     * @param length number of characters to insert
     * @param totalOffset offset of node to begin of text
     * @throws ReparseException if a reparse at upper level is needed
     *
     */
    abstract public void insert(BiTree biTree, int offset, int length,
            int totalOffset)
            throws ReparseException;

    /**
     * remove characters from node.
     * @param biTree reference to BiTree to which this node contains
     * @param offset position to remove characters
     * @param length number of characters to remove
     * @param totalOffset offset of node to begin of text
     * @throws ReparseException if a reparse at upper level is needed
     *
     */
    abstract public void remove(BiTree biTree, int offset, int length,
            int totalOffset)
            throws ReparseException;

    /**
     * helper method to insert or remove characters.
     * @param insert if true call insert-method else remove-method
     * @param biTree reference to BiTree to which this node contains
     * @param offset position to insert/remove characters
     * @param length number of characters to insert/remove
     * @param totalOffset offset of node to begin of text
     * @throws ReparseException if a reparse at upper level is needed
     */
    protected void forwardToSibling(final boolean insert, final BiTree biTree,
            final int offset, final int length, final int totalOffset)
            throws ReparseException {

        if (getSibling() == null) {
            throw new ReparseException();   // reparsing
        } else {
            if (insert) {
                getSibling().insert(biTree, offset, length, totalOffset);
            } else {
                getSibling().remove(biTree, offset, length, totalOffset);
            }
        }
    }

    /**
     * create a DOM-tree from node.
     * @param doc Document to create DOM-tree
     * @return root of DOM-tree
     */
    abstract public Node createDOMSubtree(Document doc);

    /**
     * search a DOM node in this node.
     * if nodes are equal return offset to begin of inputtext, else null
     * @param n DOM node to search for
     * @param totalOffset offset of node to begin of inputtext
     * @return position of node in inputtext
     */
    public SearchResult searchNode(final Node n, final int totalOffset) {
        if (node != null && node.equals(n)) {
            return new SearchResult(totalOffset,this.length);
        }

        return null;
    }

    @Override
    abstract public String toString();

    abstract public String toString(int level);

    /**
     * helper method for outputting the length of node.
     */
    public String formatLength() {
        int i;
        final int max = 3;
        final StringBuffer sb = new StringBuffer();

        for (i = 1; i <= max && 1 > getLength() / (Math.pow(10, max - i));
                i++) {

            if (i == 1 && getLength() == 0) {
                continue;
            }
            sb.append(' ');
        }

        sb.append(length);

        return sb.toString();
    }
}

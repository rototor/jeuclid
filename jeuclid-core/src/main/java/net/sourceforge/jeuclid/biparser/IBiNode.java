/*
 * Copyright 2009 - 2010 JEuclid, http://jeuclid.sf.net
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

/* $Id $ */

package net.sourceforge.jeuclid.biparser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * this interface is used to store typcial information about a xml-node.
 * 
 * @version $Revision$
 * 
 */
public interface IBiNode {

    /**
     * get previous node, null if node is root.
     * 
     * @return previous
     */
    IBiNode getPrevious();

    /**
     * set previous for this node.
     * 
     * @param prev
     *            previous node for this node
     */
    void setPrevious(final IBiNode prev);

    /**
     * get parent node for this node.
     * 
     * @return parent
     */
    BiNode getParent();

    /**
     * get sibling node, can be null.
     * 
     * @return sibling
     */
    IBiNode getSibling();

    /**
     * set sibling for this node and set previous of sibling to this.
     * 
     * @param sibl
     *            new sibling for this node
     */
    void setSibling(final IBiNode sibl);

    /**
     * add sibling to a node, not possible at a textnode. if node already has a
     * sibling, forward to sibling.
     * 
     * @param sibl
     *            new sibling for this node
     */
    void addSibling(final IBiNode sibl);

    /**
     * get reference to node in DOM-tree.
     * 
     * @return node in DOM-tree
     */
    Node getNode();

    /**
     * set reference to node in DOM-tree.
     * 
     * @param n
     *            reference in DOM-tree
     */
    void setNode(final Node n);

    /**
     * get length of node (number of characters).
     * 
     * @return length of node
     */
    int getLength();

    /**
     * set length of node.
     * 
     * @param len
     *            to set
     */
    void setLength(final int len);

    /**
     * change length of node and recursive of all parents.
     * 
     * @param change
     *            changevalue (can be positive or negative)
     */
    void changeLengthRec(final int change);

    /**
     * get the type of node, can be BiNode, EmptyNode or TextNode.
     * 
     * @return type of node
     */
    BiType getType();

    /**
     * insert characters to node.
     * 
     * @param biTree
     *            reference to BiTree to which this node contains
     * @param offset
     *            position to insert characters
     * @param len
     *            number of characters to insert
     * @param totalOffset
     *            offset of node to begin of text
     * @throws ReparseException
     *             if a reparse at upper level is needed
     * 
     */
    void insert(BiTree biTree, int offset, int len, int totalOffset)
            throws ReparseException;

    /**
     * remove characters from node.
     * 
     * @param biTree
     *            reference to BiTree to which this node contains
     * @param offset
     *            position to remove characters
     * @param len
     *            number of characters to remove
     * @param totalOffset
     *            offset of node to begin of text
     * @throws ReparseException
     *             if a reparse at upper level is needed
     * 
     */
    void remove(BiTree biTree, int offset, int len, int totalOffset)
            throws ReparseException;

    /**
     * helper method to insert or remove characters.
     * 
     * @param insert
     *            if true call insert-method else remove-method
     * @param biTree
     *            reference to BiTree to which this node contains
     * @param offset
     *            position to insert/remove characters
     * @param len
     *            number of characters to insert/remove
     * @param totalOffset
     *            offset of node to begin of text
     * @throws ReparseException
     *             if a reparse at upper level is needed
     */
    void forwardToSibling(final boolean insert, final BiTree biTree,
            final int offset, final int len, final int totalOffset)
            throws ReparseException;

    /**
     * create a DOM-tree from node.
     * 
     * @param doc
     *            Document to create DOM-tree
     * @return root of DOM-tree
     */
    Node createDOMSubtree(Document doc);

    /**
     * search a DOM node in this node. if nodes are equal return offset to begin
     * of inputtext, else null
     * 
     * @param n
     *            DOM node to search for
     * @param totalOffset
     *            offset of node to begin of inputtext
     * @return position of node in inputtext
     */
    SearchResult searchNode(final Node n, final int totalOffset);

    /**
     * print biNode.
     * 
     * @param level
     *            level of recursion tree
     * @return biNode
     */
    String toString(int level);

    /**
     * helper method for outputting the length of node.
     * 
     * @return formatted output of length
     */
    String formatLength();
}

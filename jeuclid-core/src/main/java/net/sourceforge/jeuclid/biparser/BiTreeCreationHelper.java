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

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

/**
 * this class if for creating a BiTree with ABiNodes while parsing a text.
 * 
 * @version $Revision$
 */
public class BiTreeCreationHelper {

    /** current position in tree. */
    private IBiNode currentBiNode;
    /** root of tree. */
    private IBiNode root;
    /** save positions of open tags. */
    private final List<Integer> startPositions;

    /**
     * create a new BiTreeHelper. get result (tree of ABiNodes) with getRoot()
     */
    public BiTreeCreationHelper() {
        this.startPositions = new ArrayList<Integer>();
    }

    /**
     * get root of BiTree.
     * 
     * @return root of BiTree
     */
    public final IBiNode getRoot() {
        return this.root;
    }

    /**
     * create and append a new BiNode at current position in BiTree.
     * 
     * @param totalOffset
     *            of node in text
     * @param childOffset
     *            position of first child (length of open tag)
     * @param namespaceURI
     *            namespace
     * @param eName
     *            name of node
     * @param attrs
     *            attributes of node
     */
    public final void createBiNode(final int totalOffset,
            final int childOffset, final String namespaceURI,
            final String eName, final Attributes attrs) {

        BiNode biNode;

        this.startPositions.add(totalOffset);

        // new root node
        if (this.root == null) {
            this.root = new BiNode(childOffset, namespaceURI, eName, attrs);
            this.currentBiNode = this.root;
        } else {
            biNode = new BiNode(childOffset, namespaceURI, eName, attrs);

            // append child (only possible at start
            if (this.currentBiNode.getType() == BiType.EMPTY) {
                this.currentBiNode.addSibling(biNode);
            } else {
                // add child (default case)
                ((BiNode) this.currentBiNode).addChild(biNode);
            }

            this.currentBiNode = biNode;
        }
    }

    /**
     * close BiNode (set length of node).
     * 
     * @param length
     *            length of node
     */
    public final void closeBiNode(final int length) {
        BiNode parent;
        final int last = this.startPositions.size() - 1;

        this.currentBiNode.setLength(length - this.startPositions.get(last));

        // move current position to parent
        parent = this.currentBiNode.getParent();
        if (parent != null) {
            this.currentBiNode = parent;
        }

        this.startPositions.remove(last);
    }

    /**
     * check if currentposition in BiTree allows a TextNode as child.
     * 
     * @return true if a TextNode is allowed
     */
    public final boolean allowNewTextNode() {
        return ((BiNode) this.currentBiNode).getChild() == null;
    }

    /**
     * create a new TextNode at current position.
     * 
     * @param length
     *            length of TextNode
     * @param t
     *            text of TextNode
     */
    public final void createTextNode(final int length, final String t) {
        ((BiNode) this.currentBiNode).addChild(new TextNode(length, t));
    }

    /**
     * create a new EmptyNode at current position in BiTree.
     * 
     * @param length
     *            length of EmtpyNode
     */
    public final void createEmtpyNode(final int length) {
        // EmptyNode is new root
        if (this.root == null) {
            this.root = new EmptyNode(length);
            this.currentBiNode = this.root;
        } else {
            ((BiNode) this.currentBiNode).addChild(new EmptyNode(length));
        }
    }
}

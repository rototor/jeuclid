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

import java.util.ArrayList;

import org.xml.sax.Attributes;

/**
 * this class if for creating a BiTree with ABiNodes while parsing a text.
 */
public class BiTreeCreationHelper {

    /** current position in tree. */
    private ABiNode currentBiNode;
    /** root of tree. */
    private ABiNode root;
    /** save positions of open tags. */
    private final ArrayList<Integer> startPositions;

    /**
     * create a new BiTreeHelper.
     * get result (tree of ABiNodes) with getRoot()
     */
    public BiTreeCreationHelper() {
        startPositions = new ArrayList<Integer>();
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
            currentBiNode = root;
        } else {
            biNode = new BiNode(childOffset, namespaceURI, eName, attrs);

            // append child (only possible at start
            if (currentBiNode.getType() == BiType.EMPTY) {
                currentBiNode.addSibling(biNode);
            } else { // add child (default case)
                ((BiNode) currentBiNode).addChild(biNode);
            }

            currentBiNode = biNode;
        }
    }

    /**
     * close BiNode (set length of node).
     * @param length length of node
     */
    public final void closeBiNode(final int length) {
        BiNode parent;
        final int last = startPositions.size() - 1;

        currentBiNode.setLength(length - startPositions.get(last));

        // move current position to parent
        parent = currentBiNode.getParent();
        if (parent != null) {
            currentBiNode = parent;
        }

        startPositions.remove(last);
    }

    /**
     * check if currentposition in BiTree allows a TextNode as child.
     * @return true if a TextNode is allowed
     */
    public final boolean allowNewTextNode() {
        return ((BiNode) currentBiNode).getChild() == null;
    }

    /**
     * create a new TextNode at current position.
     * @param length length of TextNode
     * @param t text of TextNode
     */
    public final void createTextNode(final int length, final String t) {
        ((BiNode) currentBiNode).addChild(new TextNode(length, t));
    }

    /**
     * create a new EmptyNode at current position in BiTree.
     * @param length length of EmtpyNode
     */
    public final void createEmtpyNode(final int length) {
        // EmptyNode is new root
        if (root == null) {
            root = new EmptyNode(length);
            currentBiNode = root;
        } else {
            ((BiNode) currentBiNode).addChild(new EmptyNode(length));
        }
    }
}

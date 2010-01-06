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
 * this class is used to store specific information about a empty node.
 * the node cannot have children, but a sibling
 *
 */
public class EmptyNode extends ABiNode {

    /**
     * create a new EmptyNode.
     * @param length of EmptyNode
     */
    public EmptyNode(final int length) {
        setLength(length);
    }

    /**
     * get the type of node.
     * @return EMPTY
     */
    @Override
    public final BiType getType() {
        return BiType.EMPTY;
    }

    /**
     * insert characters in EmptyNode, reparse if characters contain '<' or '>'.
     * else change length of EmptyNode
     * {@inheritDoc}
     */
    @Override
    public final void insert(final BiTree biTree, final int offset,
            final int length, final int totalOffset) throws ReparseException {
        int position;
        String insert;

      //   System.out.println("insert " + toString() + " offset=" +
       //  offset + " length=" + length);

        if (offset <= getLength()) {            // start position in this node

            position = totalOffset + offset;
            insert = biTree.getText().substring(position, position + length);

            if (insert.contains("<") || insert.contains(">")) {
                throw new ReparseException();                       // reparsing
            }

            changeLengthRec(length);

        } else {                             // start position outside this node
            forwardToSibling(true, biTree, offset - getLength(), length,
                    totalOffset + getLength());
        }
    }

    /**
     * remove characters from EmptyNode, reparse if length gets 0
     * {@inheritDoc}
     */
    @Override
    public final void remove(final BiTree biTree, final int offset,
            final int length, final int totalOffset) throws ReparseException {
        // System.out.println("remove " + toString() + " offset=" +
        // offset + " length=" + length);

        if (offset <= getLength()) {            // start position in this node

            if (offset == 0 && length >= getLength()) {     // remove this node
                throw new ReparseException();

            } else {                                        // change length
                // end position in this node
                if (offset + length <= getLength()) {
                    changeLengthRec(-length);

                } else { // end position outside this node
                    changeLengthRec(offset - getLength());

                    // forward remainder to sibling
                    forwardToSibling(false, biTree, 0,
                            offset + length - getLength(),
                            totalOffset + getLength());
                }
            }
        } else {                             // start position outside this node
            forwardToSibling(false, biTree, offset - getLength(), length,
                    totalOffset + getLength());
        }
    }

    /**
     * don't create a DOM-tree from EmptyNode (EmptyNode has no DOM node)
     * @param doc Document to create DOM-tree
     * @return null
     */
    @Override
    public final Node createDOMSubtree(final Document doc) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public final SearchResult searchNode(final Node node, final int totalOffset) {
        // forward to sibling
        if (getSibling() != null) {
            return getSibling().searchNode(node, totalOffset + getLength());
        }

        return null;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(32);

        sb.append("[EMTPY length: ");
        sb.append(getLength());
        sb.append(']');

        return sb.toString();
    }

    @Override
    public String toString(final int level) {
        final StringBuffer sb = new StringBuffer(32);
        final String nl = System.getProperty("line.separator");

        sb.append(formatLength());
        sb.append(':');
        for (int i = 0; i <=
                level; i++) {
            sb.append(' ');
        }

        sb.append("EMTPY");

        if (getSibling() != null) {
            sb.append(nl);
            sb.append(getSibling().toString(level));
        }

        return sb.toString();
    }
}

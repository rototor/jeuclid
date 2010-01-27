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
import org.w3c.dom.Text;

/**
 * this class is used to store specific information about a text node.
 * the node cannot have a child nor a sibling
 *
 * @version $Revision$
 */
public final class TextNode extends AbstractBiNode {

    /** DOM-info: text of node. */
    private String text;
    /** new line. */
    private final String nl = System.getProperty("line.separator");
    /** ladder #. */
    private final String ladder = "#";


    /**
     * creates a new TextNode, constructor does not create a DOM-node.
     * @param length length of child
     * @param t DOM-info
     */
    public TextNode(final int length, final String t) {
        this.setLength(length);
        this.text = t;
    }

    /**
     * get the type of node.
     * @return TEXT
     */
    @Override
    public BiType getType() {
        return BiType.TEXT;
    }

     /**
     * insert characters in TextNode, always reparse parent node.
     * {@inheritDoc}
     */
    @Override
    public void insert(final BiTree biTree, final int offset,
            final int length, final int totalOffset)
            throws ReparseException {
        throw new ReparseException();
    }

     /**
     * remove characters in TextNode, always reparse parent node.
     * {@inheritDoc}
     */
    @Override
    public void remove(final BiTree biTree, final int offset,
            final int length, final int totalOffset)
            throws ReparseException  {
        throw new ReparseException();
    }

    /**
     * forward insert/remove to sibling not allowed at a TextNode.
     * {@inheritDoc}
     */
    @Override
    public void forwardToSibling(final boolean insert,
            final BiTree biTree, final int offset, final int length,
            final int totalOffset)
            throws ReparseException {
        throw new UnsupportedOperationException("forwardToSibling "
                + "at textnode not allowed");
    }

    /**
     * get the text of TextNode.
     * @return text of TextNode
     */
    public String getText() {
        if (this.text == null) {
           if (this.getNode() == null) {
                return null;
            } else {
                return this.getNode().getTextContent();
            }
        } else {
            return this.text;
        }
    }

    /**
     * create a DOM-textnode.
     * @param doc Document to create DOM-tree
     * @return DOM-textnode
     */
    @Override
    public Node createDOMSubtree(final Document doc) {
        Text textNode;

        textNode = doc.createTextNode(this.text);
        this.text = null;

        this.setNode(textNode);
        return textNode;
    }

    /** {@inheritDoc} */
    @Override
    public SearchResult searchNode(final Node node, final int totalOffset) {
        return super.searchNode(node, totalOffset);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(32);

        sb.append("[TEXT length:");
        sb.append(this.getLength());
        sb.append(" '");
        sb.append(this.getText().replaceAll(this.nl, this.ladder));
        sb.append("']");

        return sb.toString();
    }

    @Override
    public String toString(final int level) {
        final StringBuffer sb = new StringBuffer();

        sb.append(this.formatLength());
        sb.append(':');
        for (int i = 0; i <= level; i++) {
            sb.append(' ');
        }

        sb.append('\'');
        sb.append(this.getText().replaceAll(this.nl, this.ladder));
        sb.append('\'');

        return sb.toString();
    }
}

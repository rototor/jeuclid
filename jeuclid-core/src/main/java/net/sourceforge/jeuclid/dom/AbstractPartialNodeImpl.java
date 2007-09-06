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

/* $Id$ */

package net.sourceforge.jeuclid.dom;

import java.util.List;
import java.util.Vector;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

/**
 * Partial implementation of org.w3c.dom.Node
 * <p>
 * This implements only the functions necessary for MathElements. Feel free to
 * implement whatever functions you need.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public abstract class AbstractPartialNodeImpl implements Node {

    private static final String COULD_NOT_FIND_NODE = "Could not find node: ";

    private final List<Node> children = new Vector<Node>();

    private Node parent;

    /**
     * @see org.w3c.dom.NodeList
     */
    public static class NodeList implements org.w3c.dom.NodeList {
        private final List<Node> children;

        /**
         * default constructor.
         * 
         * @param childs
         *            list of children.
         */
        protected NodeList(final List<Node> childs) {
            this.children = childs;
        }

        /** {@inheritDoc} */
        public final int getLength() {
            return this.children.size();
        }

        /** {@inheritDoc} */
        public final Node item(final int index) {
            return this.children.get(index);
        }
    }

    /** {@inheritDoc} */
    public final String lookupNamespaceURI(final String prefix) {
        throw new UnsupportedOperationException("lookupNamespaceURI");
    }

    /** {@inheritDoc} */
    public final void normalize() {
        throw new UnsupportedOperationException("normalize");
    }

    /** {@inheritDoc} */
    public final org.w3c.dom.NodeList getChildNodes() {
        return new NodeList(this.children);
    }

    /** {@inheritDoc} */
    public final Node getFirstChild() {
        try {
            return this.children.get(0);
        } catch (final IndexOutOfBoundsException e) {
            return null;
        }
    }

    /** {@inheritDoc} */
    public final boolean isSameNode(final Node other) {
        return this.equals(other);
    }

    /** {@inheritDoc} */
    public Node appendChild(final Node newChild) {
        if (newChild instanceof AbstractPartialNodeImpl) {
            this.children.add(newChild);
            ((AbstractPartialNodeImpl) newChild).parent = this;
            return newChild;
        } else {
            throw new IllegalArgumentException(
                    "Can only add children of type "
                            + AbstractPartialNodeImpl.class.getName()
                            + " to " + this.getClass().getName());
        }
    }

    /** {@inheritDoc} */
    public String getTextContent() {
        final StringBuilder builder = new StringBuilder();
        for (final Node n : this.children) {
            builder.append(n.getTextContent());
        }
        return builder.toString();
    }

    /** {@inheritDoc} */
    public void setTextContent(final String newTextContent) {
        this.children.clear();
        if (newTextContent != null && newTextContent.length() > 0) {
            this.children.add(new PartialTextImpl(newTextContent));
        }
    }

    /** {@inheritDoc} */
    public final Node cloneNode(final boolean deep) {
        throw new UnsupportedOperationException("cloneNode");
    }

    /** {@inheritDoc} */
    public String getNodeValue() {
        return null;
    }

    /** {@inheritDoc} */
    public final String getNamespaceURI() {
        return null;
    }

    /** {@inheritDoc} */
    public final Node getParentNode() {
        return this.parent;
    }

    /** {@inheritDoc} */
    public final String getBaseURI() {
        throw new UnsupportedOperationException("getBaseURI");
    }

    /** {@inheritDoc} */
    public final String getPrefix() {
        throw new UnsupportedOperationException("getPrefix");
    }

    /** {@inheritDoc} */
    public NamedNodeMap getAttributes() {
        return null;
    }

    /** {@inheritDoc} */
    public final boolean hasChildNodes() {
        return !this.children.isEmpty();
    }

    /** {@inheritDoc} */
    public final boolean isDefaultNamespace(final String namespaceURI) {
        throw new UnsupportedOperationException("isDefaultNamespace");
    }

    /** {@inheritDoc} */
    public Node replaceChild(final Node newChild, final Node oldChild) {
        // TODO: If newChild is already in the tree, it is supposed to be
        // removed.
        for (int i = 0; i < this.children.size(); i++) {
            final Node oldChildAtIndex = this.children.get(i);
            if (oldChildAtIndex.equals(oldChild)) {
                this.children.set(i, newChild);
                return oldChildAtIndex;
            }
        }
        throw new DOMException(DOMException.NOT_FOUND_ERR,
                AbstractPartialNodeImpl.COULD_NOT_FIND_NODE + oldChild);
    }

    /** {@inheritDoc} */
    public final Node insertBefore(final Node newChild, final Node refChild) {
        throw new UnsupportedOperationException("insertBefore");
    }

    /** {@inheritDoc} */
    public final boolean isEqualNode(final Node arg) {
        throw new UnsupportedOperationException("isEqualNode");
    }

    /** {@inheritDoc} */
    public final Node getNextSibling() {
        Node retval;
        try {
            final List<Node> parentsChildren = ((AbstractPartialNodeImpl) this.parent).children;
            retval = parentsChildren.get(parentsChildren.indexOf(this) + 1);
        } catch (final NullPointerException ne) {
            retval = null;
        } catch (final IndexOutOfBoundsException iobe) {
            retval = null;
        }
        return retval;
    }

    /** {@inheritDoc} */
    public final boolean hasAttributes() {
        return this.getAttributes().getLength() > 0;
    }

    /** {@inheritDoc} */
    public final short compareDocumentPosition(final Node other) {
        throw new UnsupportedOperationException("compareDocumentPosition");
    }

    /** {@inheritDoc} */
    public final Node removeChild(final Node oldChild) {
        for (int i = 0; i < this.children.size(); i++) {
            final Node oldChildAtIndex = this.children.get(i);
            if (oldChildAtIndex.equals(oldChild)) {
                this.children.remove(i);
                return oldChildAtIndex;
            }
        }
        throw new DOMException(DOMException.NOT_FOUND_ERR,
                AbstractPartialNodeImpl.COULD_NOT_FIND_NODE + oldChild);

    }

    /** {@inheritDoc} */
    public final Object getFeature(final String feature, final String version) {
        throw new UnsupportedOperationException("getFeature");
    }

    /** {@inheritDoc} */
    public final Node getLastChild() {
        final List<Node> theChildren = this.children;
        final int size = theChildren.size();
        if (size == 0) {
            return null;
        } else {
            return theChildren.get(size - 1);
        }
    }

    /** {@inheritDoc} */
    public String getLocalName() {
        return null;
    }

    /** {@inheritDoc} */
    public final Document getOwnerDocument() {
        throw new UnsupportedOperationException("getOwnerDocument");
    }

    /** {@inheritDoc} */
    public final Node getPreviousSibling() {
        throw new UnsupportedOperationException("getPreviousSibling");
    }

    /** {@inheritDoc} */
    public final boolean isSupported(final String feature,
            final String version) {
        throw new UnsupportedOperationException("isSupported");
    }

    /** {@inheritDoc} */
    public final String lookupPrefix(final String namespaceURI) {
        throw new UnsupportedOperationException("lookupPrefix");
    }

    /** {@inheritDoc} */
    public final void setNodeValue(final String nodeValue) {
        throw new UnsupportedOperationException("setNodeValue");
    }

    /** {@inheritDoc} */
    public final void setPrefix(final String prefix) {
        throw new UnsupportedOperationException("setPrefix");
    }

    /** {@inheritDoc} */
    public final Object getUserData(final String key) {
        throw new UnsupportedOperationException("getUserData");
    }

    /** {@inheritDoc} */
    public final Object setUserData(final String key, final Object data,
            final UserDataHandler handler) {
        throw new UnsupportedOperationException("setUserData");
    }

    /**
     * Convenience method for accessing children. The result should be treated
     * as read-only!
     * 
     * @return a List&lt;Node&gt;
     */
    public List<Node> getChildren() {
        return this.children;
    }
}

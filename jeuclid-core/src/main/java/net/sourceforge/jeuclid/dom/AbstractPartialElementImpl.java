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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

/**
 * Partial implementation of org.w3c.dom.Node.
 * <p>
 * This implements only the functions necesessay for MathElements. Feel free
 * to implement whatever functions you need.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public abstract class AbstractPartialElementImpl extends
        AbstractPartialNodeImpl implements Element {

    private final Map<String, String> attributes = new HashMap<String, String>();

    /** Partial implementation of Attr. */
    public static class AttrImpl extends AbstractPartialNodeImpl implements
            Attr {

        private final String name;

        private final String value;

        private final Element owner;

        /**
         * Create a new AttrImpl Element.
         * 
         * @param nam
         *            name of the Attribute.
         * @param val
         *            value of the attribute.
         * @param own
         *            owner for the attribute.
         */
        protected AttrImpl(final String nam, final String val,
                final Element own) {
            this.name = nam;
            this.value = val;
            this.owner = own;
        }

        /** {@inheritDoc} */
        public String getName() {
            return this.name;
        }

        /** {@inheritDoc} */
        public Element getOwnerElement() {
            return this.owner;
        }

        /** {@inheritDoc} */
        public TypeInfo getSchemaTypeInfo() {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        public boolean getSpecified() {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        public String getValue() {
            return this.value;
        }

        /** {@inheritDoc} */
        @Override
        public final String getNodeValue() {
            return this.value;
        }

        /** {@inheritDoc} */
        public boolean isId() {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        public void setValue(final String val) {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        public String getNodeName() {
            return this.name;
        }

        /** {@inheritDoc} */
        public short getNodeType() {
            return Node.ATTRIBUTE_NODE;
        }
    }

    /** Partial Implementation for an NodeMap describing Attributes. */
    public static class AttributeNodeMap implements NamedNodeMap {

        private final Map<String, String> attributes;

        private final Element owner;

        /**
         * Creates a new AttributeNodeMap.
         * 
         * @param attrs
         *            A Map containing attributes and values.
         * @param parent
         *            the node these attributes belong to.
         */
        protected AttributeNodeMap(final Map<String, String> attrs,
                final Element parent) {
            this.attributes = attrs;
            this.owner = parent;
        }

        /** {@inheritDoc} */
        public int getLength() {
            return this.attributes.size();
        }

        /** {@inheritDoc} */
        public Node getNamedItem(final String name) {
            final String value = this.attributes.get(name);
            if (value == null) {
                return null;
            } else {
                return new AttrImpl(name, value, this.owner);
            }
        }

        /** {@inheritDoc} */
        public Node getNamedItemNS(final String namespaceURI,
                final String localName) {
            return this.getNamedItem(localName);
        }

        /** {@inheritDoc} */
        public Node item(final int index) {

            final Iterator<Entry<String, String>> it = this.attributes
                    .entrySet().iterator();
            for (int i = 0; i < index; i++) {
                it.next();
            }
            final Entry<String, String> found = it.next();
            return new AttrImpl(found.getKey(), found.getValue(), this.owner);
        }

        /** {@inheritDoc} */
        public Node removeNamedItem(final String name) {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        public Node removeNamedItemNS(final String namespaceURI,
                final String localName) {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        public Node setNamedItem(final Node arg) {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        public Node setNamedItemNS(final Node arg) {
            throw new UnsupportedOperationException();
        }
    }

    /** {@inheritDoc} */
    public final String getAttribute(final String name) {
        return this.attributes.get(name);
    }

    /** {@inheritDoc} */
    public final String getAttributeNS(final String namespaceURI,
            final String localName) {
        return this.attributes.get(localName);
    }

    /** {@inheritDoc} */
    public void setAttribute(final String name, final String value) {
        this.attributes.put(name, value);
    }

    /** {@inheritDoc} */
    public final Attr getAttributeNode(final String name) {
        throw new UnsupportedOperationException("getAttributeNode");
    }

    /** {@inheritDoc} */
    public final Attr getAttributeNodeNS(final String namespaceURI,
            final String localName) {
        throw new UnsupportedOperationException("getAttributeNodeNS");
    }

    /** {@inheritDoc} */
    public final org.w3c.dom.NodeList getElementsByTagName(final String name) {
        throw new UnsupportedOperationException("getElementsByTagName");
    }

    /** {@inheritDoc} */
    public final org.w3c.dom.NodeList getElementsByTagNameNS(
            final String namespaceURI, final String localName) {
        throw new UnsupportedOperationException("getElementsByTagNameNS");
    }

    /** {@inheritDoc} */
    public final TypeInfo getSchemaTypeInfo() {
        throw new UnsupportedOperationException("getSchemaTypeInfo");
    }

    /** {@inheritDoc} */
    public final boolean hasAttribute(final String name) {
        throw new UnsupportedOperationException("hasAttribute");
    }

    /** {@inheritDoc} */
    public final boolean hasAttributeNS(final String namespaceURI,
            final String localName) {
        throw new UnsupportedOperationException("hasAttributeNS");
    }

    /** {@inheritDoc} */
    public final void removeAttribute(final String name) {
        throw new UnsupportedOperationException("removeAttribute");
    }

    /** {@inheritDoc} */
    public final void removeAttributeNS(final String namespaceURI,
            final String localName) {
        throw new UnsupportedOperationException("removeAttributeNS");
    }

    /** {@inheritDoc} */
    public final Attr removeAttributeNode(final Attr oldAttr) {
        throw new UnsupportedOperationException("removeAttributeNode");
    }

    /** {@inheritDoc} */
    public final void setAttributeNS(final String namespaceURI,
            final String qualifiedName, final String value) {
        throw new UnsupportedOperationException("setAttributeNS");
    }

    /** {@inheritDoc} */
    public final Attr setAttributeNode(final Attr newAttr) {
        throw new UnsupportedOperationException("setAttributeNode");
    }

    /** {@inheritDoc} */
    public final Attr setAttributeNodeNS(final Attr newAttr) {
        throw new UnsupportedOperationException("setAttributeNodeNS");
    }

    /** {@inheritDoc} */
    public final void setIdAttribute(final String name, final boolean isId) {
        throw new UnsupportedOperationException("setIdAttribute");
    }

    /** {@inheritDoc} */
    public final void setIdAttributeNS(final String namespaceURI,
            final String localName, final boolean isId) {
        throw new UnsupportedOperationException("setIdAttributeNS");
    }

    /** {@inheritDoc} */
    public final void setIdAttributeNode(final Attr idAttr, final boolean isId) {
        throw new UnsupportedOperationException("setIdAttributeNode");
    }

    /** {@inheritDoc} */
    public final String getNodeName() {
        return this.getTagName();
    }

    /** {@inheritDoc} */
    @Override
    public NamedNodeMap getAttributes() {
        return new AttributeNodeMap(this.attributes, this);
    }

    /** {@inheritDoc} */
    @Override
    public final String getLocalName() {
        return this.getTagName();
    }

    /** {@inheritDoc} */
    public final short getNodeType() {
        return Node.ELEMENT_NODE;
    }

    /** {@inheritDoc} */
    @Override
    public final String toString() {
        final StringBuilder builder = new StringBuilder(this.getTagName());
        for (final Map.Entry<String, String> attrs : this.attributes
                .entrySet()) {
            builder.append(" " + attrs.getKey() + "='" + attrs.getValue()
                    + "'");
        }
        return builder.toString();
    }

}

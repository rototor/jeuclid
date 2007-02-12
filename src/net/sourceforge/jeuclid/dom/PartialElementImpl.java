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

/* $Id: PartialElementImpl.java,v 1.1.2.2 2007/02/01 13:52:09 maxberger Exp $ */

package net.sourceforge.jeuclid.dom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

/**
 * Partial implementation of org.w3c.dom.Node.
 * <p>
 * This implements only the functions necesessay for MathElements. Feel free to
 * implement whatever functions you need.
 * 
 * @author Max Berger
 */
public class PartialElementImpl implements Element {

    private final List children = new Vector();

    private final Map attributes = new HashMap();

    private String textContent = "";

    private Element parent;

    /**
     * @see org.w3c.dom.NodeList
     */
    public class NodeList implements org.w3c.dom.NodeList {
        private final List children;

        /**
         * default constructor.
         * 
         * @param childs
         *            list of children.
         */
        protected NodeList(List childs) {
            this.children = childs;
        }

        /** {@inheritDoc} */
        public int getLength() {
            return this.children.size();
        }

        /** {@inheritDoc} */
        public Node item(int index) {
            return (Node) this.children.get(index);
        }
    }

    /** {@inheritDoc} */
    public org.w3c.dom.NodeList getChildNodes() {
        return new NodeList(children);
    }

    /** {@inheritDoc} */
    public Node getFirstChild() {
        try {
            return (Element) children.get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /** {@inheritDoc} */
    public boolean isSameNode(Node other) {
        return this.equals(other);
    }

    /** {@inheritDoc} */
    public Node appendChild(Node newChild) {
        if (newChild instanceof PartialElementImpl) {
            PartialElementImpl pelement = (PartialElementImpl) newChild;
            this.children.add(pelement);
            pelement.parent = this;
            return pelement;
        } else {
            throw new IllegalArgumentException("Can only add children of type "
                    + getClass().getName() + " to " + getClass().getName());
        }
    }

    /** {@inheritDoc} */
    public Node getParentNode() {
        return this.parent;
    }

    /** {@inheritDoc} */
    public String getAttribute(String name) {
        return (String) attributes.get(name);
    }

    /** {@inheritDoc} */
    public String getAttributeNS(String namespaceURI, String localName) {
        return (String) attributes.get(localName);
    }

    /** {@inheritDoc} */
    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }

    /** {@inheritDoc} */
    public String getTextContent() throws DOMException {
        return this.textContent;
    }

    /** {@inheritDoc} */
    public void setTextContent(String newTextContent) throws DOMException {
        this.textContent = newTextContent;
    }

    /** {@inheritDoc} */
    public Node cloneNode(boolean deep) {
        throw new UnsupportedOperationException("cloneNode");
    }

    /** {@inheritDoc} */
    public Attr getAttributeNode(String name) {
        throw new UnsupportedOperationException("getAttributeNode");
    }

    /** {@inheritDoc} */
    public Attr getAttributeNodeNS(String namespaceURI, String localName)
            throws DOMException {
        throw new UnsupportedOperationException("getAttributeNodeNS");
    }

    /** {@inheritDoc} */
    public org.w3c.dom.NodeList getElementsByTagName(String name) {
        throw new UnsupportedOperationException("getElementsByTagName");
    }

    /** {@inheritDoc} */
    public org.w3c.dom.NodeList getElementsByTagNameNS(String namespaceURI,
            String localName) throws DOMException {
        throw new UnsupportedOperationException("getElementsByTagNameNS");
    }

    /** {@inheritDoc} */
    public TypeInfo getSchemaTypeInfo() {
        throw new UnsupportedOperationException("getSchemaTypeInfo");
    }

    /** {@inheritDoc} */
    public String getTagName() {
        throw new UnsupportedOperationException("getTagName");
    }

    /** {@inheritDoc} */
    public boolean hasAttribute(String name) {
        throw new UnsupportedOperationException("hasAttribute");
    }

    /** {@inheritDoc} */
    public boolean hasAttributeNS(String namespaceURI, String localName)
            throws DOMException {
        throw new UnsupportedOperationException("hasAttributeNS");
    }

    /** {@inheritDoc} */
    public void removeAttribute(String name) throws DOMException {
        throw new UnsupportedOperationException("removeAttribute");
    }

    /** {@inheritDoc} */
    public void removeAttributeNS(String namespaceURI, String localName)
            throws DOMException {
        throw new UnsupportedOperationException("removeAttributeNS");
    }

    /** {@inheritDoc} */
    public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
        throw new UnsupportedOperationException("removeAttributeNode");
    }

    /** {@inheritDoc} */
    public void setAttributeNS(String namespaceURI, String qualifiedName,
            String value) throws DOMException {
        throw new UnsupportedOperationException("setAttributeNS");
    }

    /** {@inheritDoc} */
    public Attr setAttributeNode(Attr newAttr) throws DOMException {
        throw new UnsupportedOperationException("setAttributeNode");
    }

    /** {@inheritDoc} */
    public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
        throw new UnsupportedOperationException("setAttributeNodeNS");
    }

    /** {@inheritDoc} */
    public void setIdAttribute(String name, boolean isId) throws DOMException {
        throw new UnsupportedOperationException("setIdAttribute");
    }

    /** {@inheritDoc} */
    public void setIdAttributeNS(String namespaceURI, String localName,
            boolean isId) throws DOMException {
        throw new UnsupportedOperationException("setIdAttributeNS");
    }

    /** {@inheritDoc} */
    public void setIdAttributeNode(Attr idAttr, boolean isId)
            throws DOMException {
        throw new UnsupportedOperationException("setIdAttributeNode");
    }

    /** {@inheritDoc} */
    public short compareDocumentPosition(Node other) throws DOMException {
        throw new UnsupportedOperationException("compareDocumentPosition");
    }

    /** {@inheritDoc} */
    public NamedNodeMap getAttributes() {
        throw new UnsupportedOperationException("getAttributes");
    }

    /** {@inheritDoc} */
    public String getBaseURI() {
        throw new UnsupportedOperationException("getBaseURI");
    }

    /** {@inheritDoc} */
    public Object getFeature(String feature, String version) {
        throw new UnsupportedOperationException("getFeature");
    }

    /** {@inheritDoc} */
    public Node getLastChild() {
        throw new UnsupportedOperationException("getLastChild");
    }

    /** {@inheritDoc} */
    public String getLocalName() {
        throw new UnsupportedOperationException("getLocalName");
    }

    /** {@inheritDoc} */
    public String getNamespaceURI() {
        throw new UnsupportedOperationException("getNamespaceURI");
    }

    /** {@inheritDoc} */
    public Node getNextSibling() {
        throw new UnsupportedOperationException("getNextSibling");
    }

    /** {@inheritDoc} */
    public String getNodeName() {
        throw new UnsupportedOperationException("getNodeName");
    }

    /** {@inheritDoc} */
    public short getNodeType() {
        throw new UnsupportedOperationException("getNodeType");
    }

    /** {@inheritDoc} */
    public String getNodeValue() throws DOMException {
        throw new UnsupportedOperationException("getNodeValue");
    }

    /** {@inheritDoc} */
    public Document getOwnerDocument() {
        throw new UnsupportedOperationException("getOwnerDocument");
    }

    /** {@inheritDoc} */
    public String getPrefix() {
        throw new UnsupportedOperationException("getPrefix");
    }

    /** {@inheritDoc} */
    public Node getPreviousSibling() {
        throw new UnsupportedOperationException("getPreviousSibling");
    }

    /** {@inheritDoc} */
    public Object getUserData(String key) {
        throw new UnsupportedOperationException("getUserData");
    }

    /** {@inheritDoc} */
    public boolean hasAttributes() {
        throw new UnsupportedOperationException("hasAttributes");
    }

    /** {@inheritDoc} */
    public boolean hasChildNodes() {
        throw new UnsupportedOperationException("hasChildNodes");
    }

    /** {@inheritDoc} */
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        throw new UnsupportedOperationException("insertBefore");
    }

    /** {@inheritDoc} */
    public boolean isDefaultNamespace(String namespaceURI) {
        throw new UnsupportedOperationException("isDefaultNamespace");
    }

    /** {@inheritDoc} */
    public boolean isEqualNode(Node arg) {
        throw new UnsupportedOperationException("isEqualNode");
    }

    /** {@inheritDoc} */
    public boolean isSupported(String feature, String version) {
        throw new UnsupportedOperationException("isSupported");
    }

    /** {@inheritDoc} */
    public String lookupNamespaceURI(String prefix) {
        throw new UnsupportedOperationException("lookupNamespaceURI");
    }

    /** {@inheritDoc} */
    public String lookupPrefix(String namespaceURI) {
        throw new UnsupportedOperationException("lookupPrefix");
    }

    /** {@inheritDoc} */
    public void normalize() {
        throw new UnsupportedOperationException("normalize");
    }

    /** {@inheritDoc} */
    public Node removeChild(Node oldChild) throws DOMException {
        throw new UnsupportedOperationException("removeChild");
    }

    /** {@inheritDoc} */
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        throw new UnsupportedOperationException("replaceChild");
    }

    /** {@inheritDoc} */
    public void setNodeValue(String nodeValue) throws DOMException {
        throw new UnsupportedOperationException("setNodeValue");
    }

    /** {@inheritDoc} */
    public void setPrefix(String prefix) throws DOMException {
        throw new UnsupportedOperationException("setPrefix");
    }

    /** {@inheritDoc} */
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        throw new UnsupportedOperationException("setUserData");
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        // TODO: Should also output tagName
        return attributes.toString();
    }

}

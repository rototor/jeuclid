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

/* $Id: AbstractPartialDocumentImpl.java,v 1.1.2.1 2007/02/05 21:54:03 maxberger Exp $ */

package net.sourceforge.jeuclid.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * Partial implementation of org.w3c.dom.Document.
 * <p>
 * This implements only the functions necesessay for MathElements. Feel free
 * to implement whatever functions you need.
 * 
 * @author Max Berger
 */
public abstract class AbstractPartialDocumentImpl extends
        AbstractPartialNodeImpl implements Document {

    /** {@inheritDoc} */
    public final Node adoptNode(final Node source) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final Attr createAttribute(final String name) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final Attr createAttributeNS(final String namespaceURI,
            final String qualifiedName) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final CDATASection createCDATASection(final String data) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final Comment createComment(final String data) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final DocumentFragment createDocumentFragment() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final Element createElement(final String tagName) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final Element createElementNS(final String namespaceURI,
            final String qualifiedName) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final EntityReference createEntityReference(final String name) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final ProcessingInstruction createProcessingInstruction(
            final String target, final String data) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final Text createTextNode(final String data) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final DocumentType getDoctype() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final Element getDocumentElement() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final String getDocumentURI() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final DOMConfiguration getDomConfig() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final Element getElementById(final String elementId) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final DOMImplementation getImplementation() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final String getInputEncoding() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final boolean getStrictErrorChecking() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final String getXmlEncoding() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final boolean getXmlStandalone() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final String getXmlVersion() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final Node importNode(final Node importedNode, final boolean deep) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final void normalizeDocument() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final Node renameNode(final Node n, final String namespaceURI,
            final String qualifiedName) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final void setDocumentURI(final String documentURI) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final void setStrictErrorChecking(final boolean strictErrorChecking) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final void setXmlStandalone(final boolean xmlStandalone) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final void setXmlVersion(final String xmlVersion) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final org.w3c.dom.NodeList getElementsByTagName(
            final String tagname) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final org.w3c.dom.NodeList getElementsByTagNameNS(
            final String namespaceURI, final String localName) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */

    public final String getNodeName() {
        return "#document";
    }

}

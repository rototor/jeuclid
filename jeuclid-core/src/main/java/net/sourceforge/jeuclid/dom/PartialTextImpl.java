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

import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Partial implementation of org.w3c.dom.Text.
 * <p>
 * This implements only the functions necessary for MathElements. Feel free to
 * implement whatever functions you need.
 * 
 * @version $Revision$
 */
public class PartialTextImpl extends AbstractEventTargetImpl implements Text {

    private String content;

    /**
     * Constructor.
     * 
     * @param text
     *            text content for this node.
     */
    public PartialTextImpl(final String text) {
        this.content = text;
    }

    /** {@inheritDoc} */
    public String getWholeText() {
        return this.content;
    }

    /** {@inheritDoc} */
    public boolean isElementContentWhitespace() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public Text replaceWholeText(final String newContent) {
        this.content = newContent;
        return this;
    }

    /** {@inheritDoc} */
    public Text splitText(final int offset) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public void appendData(final String arg) {
        this.content = this.content + arg;
    }

    /** {@inheritDoc} */
    public void deleteData(final int offset, final int count) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public String getData() {
        return this.content;
    }

    /** {@inheritDoc} */
    public int getLength() {
        return this.content.length();
    }

    /** {@inheritDoc} */
    public void insertData(final int offset, final String arg) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public void replaceData(final int offset, final int count,
            final String arg) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public void setData(final String data) {
        this.content = data;
    }

    /** {@inheritDoc} */
    public String substringData(final int offset, final int count) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public String getNodeName() {
        return "#text";
    }

    /** {@inheritDoc} */
    public short getNodeType() {
        return Node.TEXT_NODE;
    }

    /** {@inheritDoc} */
    @Override
    public String getNodeValue() {
        return this.content;
    }

    /** {@inheritDoc} */
    @Override
    public void setTextContent(final String newTextContent) {
        this.content = newTextContent;
    }

    /** {@inheritDoc} */
    @Override
    public String getTextContent() {
        return this.content;
    }

}

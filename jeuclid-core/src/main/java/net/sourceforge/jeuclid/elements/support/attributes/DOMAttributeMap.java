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

package net.sourceforge.jeuclid.elements.support.attributes;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Attributes derived from DOM.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public class DOMAttributeMap extends AbstractAttributeMap {

    private final NamedNodeMap namedNodeMap;

    /**
     * Creates a new AttributesMap based on DOM attributes.
     * 
     * @param attributeMap
     *            the DOM attributes.
     */
    public DOMAttributeMap(final NamedNodeMap attributeMap) {
        this.namedNodeMap = attributeMap;
    }

    /** {@inheritDoc} */
    @Override
    protected String getAttribute(final String attrName) {
        final Node valueNode = this.namedNodeMap.getNamedItem(attrName);
        if (valueNode == null) {
            return null;
        } else {
            return valueNode.getNodeValue();
        }
    }

    /** {@inheritDoc} */
    @Override
    protected String getAttributeNS(final String namespace,
            final String attrName) {
        final Node valueNode = this.namedNodeMap.getNamedItemNS(namespace,
                attrName);
        if (valueNode == null) {
            return null;
        } else {
            return valueNode.getNodeValue();
        }
    }

    /** {@inheritDoc} */
    public Map<String, String> getAsMap() {
        final Map<String, String> m = new HashMap<String, String>();
        for (int i = 0; i < this.namedNodeMap.getLength(); i++) {
            final Node n = this.namedNodeMap.item(i);
            final String localName = n.getLocalName();
            if (localName != null) {
                m.put(localName, n.getNodeValue());
            } else {
                m.put(n.getNodeName(), n.getNodeValue());
            }
        }
        return m;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return this.getAsMap().toString();
    }

}

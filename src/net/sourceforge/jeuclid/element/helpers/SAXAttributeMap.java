/*
 * Copyright 2002 - 2006 JEuclid, http://jeuclid.sf.net
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

/* $Id: SAXAttributeMap.java,v 1.3 2006/08/19 19:31:11 maxberger Exp $ */

package net.sourceforge.jeuclid.element.helpers;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

/**
 * Attributes derived from SAX.
 * 
 * @author Max Berger
 */
public class SAXAttributeMap extends AbstractAttributeMap {

    final Attributes attributes;

    /**
     * Creates a new AttributeMap based on SAX attributes.
     * 
     * @param attr
     *            the SAX attributes.
     */
    public SAXAttributeMap(Attributes attr) {
        this.attributes = attr;
    }

    /** {@inheritDoc} */
    protected String getAttribute(String attrName) {
        return this.attributes.getValue(attrName);
    }

    /** {@inheritDoc} */
    protected String getAttributeNS(String namespace, String attrName) {
        return this.attributes.getValue(namespace, attrName);
    }

    /** {@inheritDoc} */
    public Map getAsMap() {
        final Map m = new HashMap();
        for (int i = 0; i < this.attributes.getLength(); i++) {
            m.put(this.attributes.getLocalName(i), this.attributes.getValue(i));
        }
        return m;
    }

}

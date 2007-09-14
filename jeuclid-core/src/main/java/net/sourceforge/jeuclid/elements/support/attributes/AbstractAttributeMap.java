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

/* $Id$ */

package net.sourceforge.jeuclid.elements.support.attributes;

/**
 * Generic class for reading and parsing attributes.
 * 
 * @version $Revision$
 */
public abstract class AbstractAttributeMap implements AttributeMap {

    private static final String MATHMLNAMESPACE = "http://www.w3.org/1998/Math/MathML";

    /**
     * Override this method to get an attribute value without a namespace.
     * 
     * @param attrName
     *            name of the attribute
     * @return value of the attribute
     */
    protected abstract String getAttribute(String attrName);

    /**
     * Override this method to get an attribtue with a namespace.
     * 
     * @param namespace
     *            the namespace
     * @param attrName
     *            the attribtue name
     * @return value of the attribute.
     */
    protected abstract String getAttributeNS(String namespace, String attrName);

    /** {@inheritDoc} */
    public boolean hasAttribute(final String attrName) {
        return this.getString(attrName) != null;
    }

    /** {@inheritDoc} */
    public String getString(final String attrName) {
        String attrValue = this.getAttributeNS(
                AbstractAttributeMap.MATHMLNAMESPACE, attrName);
        if (attrValue == null) {
            attrValue = this.getAttribute(attrName);
        }
        return attrValue;
    }

    /** {@inheritDoc} */
    public String getString(final String attrName, final String defaultValue) {
        String attrValue = this.getString(attrName);
        if (attrValue == null) {
            attrValue = defaultValue;
        }
        return attrValue;
    }

    /** {@inheritDoc} */
    public boolean getBoolean(final String attrName,
            final boolean defaultValue) {
        final String strValue = this.getString(attrName, Boolean
                .toString(defaultValue));
        return Boolean.parseBoolean(strValue);
    }

}

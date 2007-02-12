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

/* $Id: AbstractAttributeMap.java,v 1.1.2.1 2007/02/02 09:57:05 maxberger Exp $ */

package net.sourceforge.jeuclid.element.helpers;

/**
 * Generic class for reading and parsing attributes.
 * 
 * @author Max Berger
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
    public boolean hasAttribute(String attrName) {
        return this.getString(attrName) != null;
    }

    /** {@inheritDoc} */
    public String getString(String attrName) {
        String attrValue = this.getAttributeNS(MATHMLNAMESPACE, attrName);
        if (attrValue == null) {
            attrValue = this.getAttribute(attrName);
        }
        return attrValue;
    }

    /** {@inheritDoc} */
    public String getString(String attrName, String defaultValue) {
        String attrValue = this.getString(attrName);
        if (attrValue == null) {
            attrValue = defaultValue;
        }
        return attrValue;
    }

    /** {@inheritDoc} */
    public boolean getBoolean(String attrName, boolean defaultValue) {
        final String strValue = this.getString(attrName, Boolean
                .toString(defaultValue));
        return new Boolean(strValue).booleanValue();
    }

}

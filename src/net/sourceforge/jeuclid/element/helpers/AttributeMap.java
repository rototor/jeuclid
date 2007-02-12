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

package net.sourceforge.jeuclid.element.helpers;

import java.util.Map;

/**
 * Generic interface to access XML attributes.
 * 
 * @author Max Berger
 */
public interface AttributeMap {

    /**
     * return true if an attribute is present.
     * 
     * @param attrName
     *            name of the attribute to look for.
     * @return true if attrName is present.
     */
    boolean hasAttribute(String attrName);

    /**
     * retrieve the value of the attribute as String.
     * 
     * @param attrName
     *            name of the attribute
     * @return the value of the attribute as String.
     */
    String getString(String attrName);

    /**
     * retrieve the value of the attribute as String.
     * 
     * @param attrName
     *            name of the attribute
     * @param defaultValue
     *            value to use if unset.
     * @return the value of the attribute as String.
     */
    String getString(String attrName, String defaultValue);

    /**
     * retrieve the value of the attribute as boolean.
     * 
     * @param attrName
     *            name of the attribute.
     * @param defaultValue
     *            value to use if unset.
     * @return a boolean value.
     */
    boolean getBoolean(String attrName, boolean defaultValue);

    /**
     * retrieve the attributes as map.
     * 
     * @return a Map&ltString,String&gt;
     */
    Map getAsMap();

}
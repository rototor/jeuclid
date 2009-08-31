/*
 * Copyright 2009 - 2009 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.testsuite;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 * Implements a Namespace context which matches the namespaces used in html+mml
 * transformation.
 * 
 * @version $Revision$
 */
public class JEuclidNamespaceContext implements NamespaceContext {

    private final Map<String, String> prefixToNs = new TreeMap<String, String>();
    private final Map<String, String> nsToPrefix = new TreeMap<String, String>();

    /**
     * Default Constructor.
     */
    JEuclidNamespaceContext() {
        this.addMapping("html", "http://www.w3.org/1999/xhtml");
    }

    private void addMapping(final String prefix, final String ns) {
        this.prefixToNs.put(prefix, ns);
        this.nsToPrefix.put(ns, prefix);
    }

    /** {@inheritDoc} */
    public String getNamespaceURI(final String prefix) {
        final String ns = this.prefixToNs.get(prefix);
        if (ns == null) {
            return XMLConstants.NULL_NS_URI;
        } else {
            return ns;
        }
    }

    /** {@inheritDoc} */
    public String getPrefix(final String namespace) {
        final String prefix = this.nsToPrefix.get(namespace);
        return prefix;
    }

    /** {@inheritDoc} */
    public Iterator<String> getPrefixes(final String namespace) {
        return Collections.singletonList(this.getPrefix(namespace)).iterator();
    }

}

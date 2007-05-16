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

package net.sourceforge.jeuclid.fop;

// FOP
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.XMLObj;

/**
 * Catch all MathML objects as default element.
 * 
 * @version $Revision$
 */
public class MathMLObj extends XMLObj {

    /**
     * Default constructor.
     * 
     * @param parent
     *            Parent node in FO Tree
     */
    public MathMLObj(final FONode parent) {
        super(parent);
    }

    /** {@inheritDoc} */
    @Override
    public String getNamespaceURI() {
        return MathMLElementMapping.NAMESPACE;
    }

    /** {@inheritDoc} */
    @Override
    public String getNormalNamespacePrefix() {
        return "mathml";
    }
}

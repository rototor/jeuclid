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

/* $Id: Mprescripts.java 718 2008-04-28 18:46:38Z maxberger $ */

package euclid.elements.presentation.script;


import org.w3c.dom.Node;

import euclid.elements.AbstractInvisibleJEuclidElement;

/**
 * This class represent the empty elements mprescripts.
 * 
 * @version $Revision: 718 $
 */
public final class Mprescripts extends AbstractInvisibleJEuclidElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mprescripts";

    private static final long serialVersionUID = 1L;

    /**
     * Creates a math element.
     */
    public Mprescripts() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Mprescripts();
    }
}

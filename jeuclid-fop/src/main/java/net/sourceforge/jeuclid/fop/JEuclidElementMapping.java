/*
 * Copyright 2007 - 2008 JEuclid, http://jeuclid.sf.net
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

/* 
 * Please note: This file was originally taken from the Apache FOP project,
 * available at http://xmlgraphics.apache.org/fop/ It is therefore
 * partially copyright (c) 1999-2007 The Apache Software Foundation.
 * 
 * Parts of the contents are heavily inspired by work done for Barcode4J by
 * Jeremias Maerki, available at http://barcode4j.sf.net/
 */

package net.sourceforge.jeuclid.fop;

import java.util.HashMap;

import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;

import org.apache.fop.fo.ElementMapping;
import org.apache.fop.fo.FONode;
import org.w3c.dom.DOMImplementation;

/**
 * This class provides the element mapping for FOP.
 * 
 * @version $Revision$
 */
public class JEuclidElementMapping extends ElementMapping {

    /** Main constructor. */
    public JEuclidElementMapping() {
        this.namespaceURI = AbstractJEuclidElement.URI;
    }

    /** {@inheritDoc} */
    @Override
    public DOMImplementation getDOMImplementation() {
        return ElementMapping.getDefaultDOMImplementation();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    protected void initialize() {
        if (this.foObjs == null) {
            this.foObjs = new HashMap();
            this.foObjs.put("math", new ME());
            this.foObjs.put(ElementMapping.DEFAULT, new MathMLMaker());

        }
    }

    static final class MathMLMaker extends ElementMapping.Maker {

        private MathMLMaker() {
        }

        @Override
        public FONode make(final FONode parent) {
            return new JEuclidObj(parent);
        }
    }

    static final class ME extends ElementMapping.Maker {

        private ME() {
        }

        @Override
        public FONode make(final FONode parent) {
            return new JEuclidElement(parent);
        }
    }

}

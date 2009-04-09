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

package euclid.elements.presentation.token;


import org.w3c.dom.Node;

import euclid.elements.AbstractJEuclidElement;

/**
 * This class presents a mathematical identifier, like "x".
 * 
 * @version $Revision$
 */
public final class Mi extends AbstractTokenWithStandardLayout {
    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mi";

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public Mi() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Mi();
    }

    /** {@inheritDoc} */
    @Override
    public void changeHook() {
        super.changeHook();
        if (this.getText().length() == 1) {
            this.setDefaultMathAttribute(
                    AbstractJEuclidElement.ATTR_MATHVARIANT, "italic");
        } else {
            this.setDefaultMathAttribute(
                    AbstractJEuclidElement.ATTR_MATHVARIANT, "normal");
        }
    }

}

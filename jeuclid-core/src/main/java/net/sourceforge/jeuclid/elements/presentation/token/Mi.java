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

package net.sourceforge.jeuclid.elements.presentation.token;

import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;

import org.w3c.dom.Node;

/**
 * This class presents a mathematical idenifier, like "x".
 * 
 * @version $Revision$
 */
public class Mi extends AbstractTokenWithStandardLayout {
    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mi";

    /**
     * Default constructor.
     */
    public Mi() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void changeHook(final Node origin) {
        super.changeHook(origin);
        if (this.getText().length() == 1) {
            this.setDefaultMathAttribute(
                    AbstractJEuclidElement.ATTR_MATHVARIANT, "italic");
        } else {
            this.setDefaultMathAttribute(
                    AbstractJEuclidElement.ATTR_MATHVARIANT, "normal");
        }
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Mi.ELEMENT;
    }
}

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

package net.sourceforge.jeuclid.element;

import java.util.List;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractRootElement;
import net.sourceforge.jeuclid.element.generic.MathElement;
import net.sourceforge.jeuclid.element.helpers.ElementListSupport;

/**
 * This class presents a mathematical square root.
 * 
 * @author Unknown
 * @author Max Berger
 */
public class MathSqrt extends AbstractRootElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "msqrt";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathSqrt(final MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathSqrt.ELEMENT;
    }

    /** {@inheritDoc} */
    @Override
    protected List<MathElement> getContent() {
        return ElementListSupport.createListOfChildren(this);
    }

    /** {@inheritDoc} */
    @Override
    protected MathElement getLeft() {
        return new MathSpace(this.getMathBase());
    }
}

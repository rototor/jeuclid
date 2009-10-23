/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.elements.presentation.script;

import net.sourceforge.jeuclid.Constants;
import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.InlineLayoutContext;
import net.sourceforge.jeuclid.context.RelativeScriptlevelLayoutContext;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;

import org.apache.batik.dom.AbstractDocument;

/**
 * Base class for msub, msup, msubsup, and mmultiscripts.
 * 
 * @version $Revision$
 */
public abstract class AbstractScriptElement extends AbstractJEuclidElement {

    /** attribute for subscriptshift. */
    public static final String ATTR_SUBSCRIPTSHIFT = "subscriptshift";

    /** attribute for superscriptshift. */
    public static final String ATTR_SUPERSCRIPTSHIFT = "superscriptshift";

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public AbstractScriptElement(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);

        this.setDefaultMathAttribute(AbstractScriptElement.ATTR_SUBSCRIPTSHIFT,
                Constants.ZERO);
        this.setDefaultMathAttribute(
                AbstractScriptElement.ATTR_SUPERSCRIPTSHIFT, Constants.ZERO);
    }

    /**
     * @return attribute subscriptshift.
     */
    public String getSubscriptshift() {
        return this.getMathAttribute(AbstractScriptElement.ATTR_SUBSCRIPTSHIFT);
    }

    /**
     * @param subscriptshift
     *            new value for subscriptshift
     */
    public void setSubscriptshift(final String subscriptshift) {
        this.setAttribute(AbstractScriptElement.ATTR_SUBSCRIPTSHIFT,
                subscriptshift);
    }

    /**
     * @return attribtue superscriptshift
     */
    public String getSuperscriptshift() {
        return this
                .getMathAttribute(AbstractScriptElement.ATTR_SUPERSCRIPTSHIFT);
    }

    /**
     * @param superscriptshift
     *            new value for superscriptshift
     */
    public void setSuperscriptshift(final String superscriptshift) {
        this.setAttribute(AbstractScriptElement.ATTR_SUPERSCRIPTSHIFT,
                superscriptshift);
    }

    /** {@inheritDoc} */
    @Override
    public LayoutContext getChildLayoutContext(final int childNum,
            final LayoutContext context) {
        final LayoutContext now = this.applyLocalAttributesToContext(context);
        if (childNum == 0) {
            return now;
        } else {
            return new RelativeScriptlevelLayoutContext(
                    new InlineLayoutContext(now), 1);
        }
    }

}

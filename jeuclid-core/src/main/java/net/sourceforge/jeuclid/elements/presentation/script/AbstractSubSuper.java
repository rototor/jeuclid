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

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutView;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.mathml.MathMLScriptElement;

/**
 * Generic support for all elements that have a subscript or a superscript
 * attribute.
 * <p>
 * Supported elements: msub, msup, msubsup.
 * 
 * @version $Revision$
 */
public abstract class AbstractSubSuper extends AbstractScriptElement implements
        MathMLScriptElement {

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public AbstractSubSuper(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);
    }

    /** {@inheritDoc} */
    public abstract JEuclidElement getBase();

    /** {@inheritDoc} */
    public abstract JEuclidElement getSuperscript();

    /** {@inheritDoc} */
    public abstract JEuclidElement getSubscript();

    /** {@inheritDoc} */
    @Override
    public boolean hasChildPostscripts(final JEuclidElement child,
            final LayoutContext context) {
        return child.isSameNode(this.getBase());
    }

    /** {@inheritDoc} */
    @Override
    protected void layoutStageInvariant(final LayoutView view,
            final LayoutInfo info, final LayoutStage stage,
            final LayoutContext context) {
        ScriptSupport.layout(view, info, stage, this
                .applyLocalAttributesToContext(context), this, this.getBase(),
                this.getSubscript(), this.getSuperscript(), this
                        .getSubscriptshift(), this.getSuperscriptshift());
    }
}

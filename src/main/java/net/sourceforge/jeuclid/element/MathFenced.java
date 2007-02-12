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

/* $Id: MathFenced.java,v 1.1.2.3 2007/02/06 09:33:53 maxberger Exp $ */

package net.sourceforge.jeuclid.element;

import java.util.List;
import java.util.Vector;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractElementWithDelegates;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.helpers.OperatorDictionary;

import org.w3c.dom.mathml.MathMLFencedElement;

/**
 * The class represents the mfenced element.
 * 
 * @author AH
 * @author Unknown
 * @author Max Berger
 */
public class MathFenced extends AbstractElementWithDelegates implements
        MathMLFencedElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mfenced";

    /**
     * Creates a new MathFenced object.
     * 
     * @param base
     *            The base for the math element tree
     */

    public MathFenced(final MathBase base) {
        super(base);
        this.setOpen("(");
        this.setClose(")");
        this.setSeparators(",");
    }

    /**
     * @return opening delimiter
     */
    public String getOpen() {
        return this.getMathAttribute("open");
    }

    /**
     * Set the opening delimiter.
     * 
     * @param open
     *            Delimiter
     */
    public void setOpen(final String open) {
        this.setAttribute("open", open);
    }

    /**
     * @return Return the closing delimiter
     */
    public String getClose() {
        return this.getMathAttribute("close");
    }

    /**
     * Set the closing delimiter.
     * 
     * @param close
     *            New close delimeter
     */
    public void setClose(final String close) {
        this.setAttribute("close", close);
    }

    /**
     * Return the separators.
     * 
     * @return separators
     */
    public String getSeparators() {
        return this.getMathAttribute("separators");
    }

    /**
     * Set the separators.
     * 
     * @param separators
     *            New separators
     */
    public void setSeparators(final String separators) {
        this.setAttribute("separators", separators);
    }

    /** {@inheritDoc} */
    @Override
    protected List<AbstractMathElement> createDelegates() {

        final List<AbstractMathElement> retVal = new Vector<AbstractMathElement>();

        final MathOperator opOpen = new MathOperator(this.getMathBase());
        opOpen.setFence(true);
        opOpen.setStretchy(true);
        opOpen.setRSpace("0.2em");
        opOpen.setLSpace("0.2em");
        opOpen.setSymmetric(false);
        opOpen.setForm(OperatorDictionary.VALUE_PREFIX);
        opOpen.addText(this.getOpen());

        retVal.add(opOpen);

        for (int i = 0; i < this.getMathElementCount(); i++) {
            retVal.add(this.getMathElement(i));

            if (i < (this.getMathElementCount() - 1)) {
                final MathOperator opSep = new MathOperator(this.m_base);
                opSep.setSeparator(true);
                final String sep = this.getSeparators();
                if (i < sep.length()) {
                    opSep.addText(String.valueOf(sep.charAt(i)));
                } else {
                    opSep.addText(String
                            .valueOf(sep.charAt(sep.length() - 1)));
                }
                retVal.add(opSep);
            }

        }
        final MathOperator opClose = new MathOperator(this.getMathBase());
        opClose.setFence(true);
        opClose.setRSpace("0.2em");
        opClose.setLSpace("0.2em");
        opClose.setStretchy(true);
        opClose.setSymmetric(false);
        opClose.setForm(OperatorDictionary.VALUE_POSTFIX);
        opClose.addText(this.getClose());
        retVal.add(opClose);

        return retVal;
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return ELEMENT;
    }

}

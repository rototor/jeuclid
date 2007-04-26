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

package net.sourceforge.jeuclid.elements.presentation.general;

import java.util.List;
import java.util.Vector;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.elements.AbstractElementWithDelegates;
import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.elements.presentation.token.Mo;
import net.sourceforge.jeuclid.elements.support.operatordict.OperatorDictionary;

import org.w3c.dom.mathml.MathMLFencedElement;

/**
 * The class represents the mfenced element.
 * 
 * @author AH
 * @author Unknown
 * @author Max Berger
 * @version $Revision$
 */
public class Mfenced extends AbstractElementWithDelegates implements
        MathMLFencedElement {

    /** The separators attribute. */
    public static final String ATTR_SEPARATORS = "separators";

    /** The close attribute. */
    public static final String ATTR_CLOSE = "close";

    /** The open attribute. */
    public static final String ATTR_OPEN = "open";

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mfenced";

    private static final String FENCE_SPACE = "0.2em";

    /**
     * Creates a new MathFenced object.
     * 
     * @param base
     *            The base for the math element tree
     */

    public Mfenced(final MathBase base) {
        super(base);
        this.setDefaultMathAttribute(Mfenced.ATTR_OPEN, "(");
        this.setDefaultMathAttribute(Mfenced.ATTR_CLOSE, ")");
        this.setDefaultMathAttribute(Mfenced.ATTR_SEPARATORS, ",");
    }

    /**
     * @return opening delimiter
     */
    public String getOpen() {
        return this.getMathAttribute(Mfenced.ATTR_OPEN);
    }

    /**
     * Set the opening delimiter.
     * 
     * @param open
     *            Delimiter
     */
    public void setOpen(final String open) {
        this.setAttribute(Mfenced.ATTR_OPEN, open);
    }

    /**
     * @return Return the closing delimiter
     */
    public String getClose() {
        return this.getMathAttribute(Mfenced.ATTR_CLOSE);
    }

    /**
     * Set the closing delimiter.
     * 
     * @param close
     *            New close delimeter
     */
    public void setClose(final String close) {
        this.setAttribute(Mfenced.ATTR_CLOSE, close);
    }

    /**
     * Return the separators.
     * 
     * @return separators
     */
    public String getSeparators() {
        return this.getMathAttribute(Mfenced.ATTR_SEPARATORS);
    }

    /**
     * Set the separators.
     * 
     * @param separators
     *            New separators
     */
    public void setSeparators(final String separators) {
        this.setAttribute(Mfenced.ATTR_SEPARATORS, separators);
    }

    /** {@inheritDoc} */
    @Override
    protected List<JEuclidElement> createDelegates() {

        final List<JEuclidElement> retVal = new Vector<JEuclidElement>();

        final Mo opOpen = new Mo(this.getMathBase());
        opOpen.setFence(MathBase.TRUE);
        opOpen.setStretchy(MathBase.TRUE);
        opOpen.setRspace(Mfenced.FENCE_SPACE);
        opOpen.setLspace(Mfenced.FENCE_SPACE);
        opOpen.setSymmetric(MathBase.FALSE);
        opOpen.setForm(OperatorDictionary.FORM_PREFIX);
        opOpen.addText(this.getOpen());

        retVal.add(opOpen);

        for (int i = 0; i < this.getMathElementCount(); i++) {
            retVal.add(this.getMathElement(i));

            if (i < (this.getMathElementCount() - 1)) {
                final Mo opSep = new Mo(this.getMathBase());
                opSep.setSeparator(MathBase.TRUE);
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
        final Mo opClose = new Mo(this.getMathBase());
        opClose.setFence(MathBase.TRUE);
        opClose.setRspace(Mfenced.FENCE_SPACE);
        opClose.setLspace(Mfenced.FENCE_SPACE);
        opClose.setStretchy(MathBase.TRUE);
        opClose.setSymmetric(MathBase.FALSE);
        opClose.setForm(OperatorDictionary.FORM_POSTFIX);
        opClose.addText(this.getClose());
        retVal.add(opClose);

        return retVal;
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Mfenced.ELEMENT;
    }

}

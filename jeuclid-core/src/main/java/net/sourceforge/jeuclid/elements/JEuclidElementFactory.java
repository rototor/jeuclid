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

package net.sourceforge.jeuclid.elements;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.elements.generic.Annotation;
import net.sourceforge.jeuclid.elements.generic.MathImpl;
import net.sourceforge.jeuclid.elements.generic.Semantics;
import net.sourceforge.jeuclid.elements.presentation.enlivening.Maction;
import net.sourceforge.jeuclid.elements.presentation.general.Menclose;
import net.sourceforge.jeuclid.elements.presentation.general.Merror;
import net.sourceforge.jeuclid.elements.presentation.general.Mfenced;
import net.sourceforge.jeuclid.elements.presentation.general.Mfrac;
import net.sourceforge.jeuclid.elements.presentation.general.Mpadded;
import net.sourceforge.jeuclid.elements.presentation.general.Mphantom;
import net.sourceforge.jeuclid.elements.presentation.general.Mroot;
import net.sourceforge.jeuclid.elements.presentation.general.Mrow;
import net.sourceforge.jeuclid.elements.presentation.general.Msqrt;
import net.sourceforge.jeuclid.elements.presentation.general.Mstyle;
import net.sourceforge.jeuclid.elements.presentation.script.Mmultiscripts;
import net.sourceforge.jeuclid.elements.presentation.script.Mover;
import net.sourceforge.jeuclid.elements.presentation.script.Mprescripts;
import net.sourceforge.jeuclid.elements.presentation.script.Msub;
import net.sourceforge.jeuclid.elements.presentation.script.Msubsup;
import net.sourceforge.jeuclid.elements.presentation.script.Msup;
import net.sourceforge.jeuclid.elements.presentation.script.Munder;
import net.sourceforge.jeuclid.elements.presentation.script.Munderover;
import net.sourceforge.jeuclid.elements.presentation.table.Maligngroup;
import net.sourceforge.jeuclid.elements.presentation.table.Malignmark;
import net.sourceforge.jeuclid.elements.presentation.table.Mlabeledtr;
import net.sourceforge.jeuclid.elements.presentation.table.Mtable;
import net.sourceforge.jeuclid.elements.presentation.table.Mtd;
import net.sourceforge.jeuclid.elements.presentation.table.Mtr;
import net.sourceforge.jeuclid.elements.presentation.token.Mglyph;
import net.sourceforge.jeuclid.elements.presentation.token.Mi;
import net.sourceforge.jeuclid.elements.presentation.token.Mn;
import net.sourceforge.jeuclid.elements.presentation.token.Mo;
import net.sourceforge.jeuclid.elements.presentation.token.Ms;
import net.sourceforge.jeuclid.elements.presentation.token.Mspace;
import net.sourceforge.jeuclid.elements.presentation.token.Mtext;
import net.sourceforge.jeuclid.elements.support.attributes.AttributeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.mathml.MathMLElement;

/**
 * Creates MathElements from given element strings.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public final class JEuclidElementFactory {

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory
            .getLog(JEuclidElementFactory.class);

    private JEuclidElementFactory() {
        // Empty on purpose
    }

    /**
     * Factory for MathML Elements.
     * 
     * @param localName
     *            name of the element without namespaces.
     * @param aMap
     *            Attributes for this element.
     * @param base
     *            mathbase to attach element to.
     * @return A new MathElement for this tag name.
     */
    public static MathMLElement elementFromName(final String localName,
            final AttributeMap aMap, final MathBase base) {
        MathMLElement element;
        if (localName.equals(MathImpl.ELEMENT)) {
            element = new MathImpl(base);
        } else if (localName.equals(Mfenced.ELEMENT)) {
            element = new Mfenced(base);
        } else if (localName.equals(Mfrac.ELEMENT)) {
            element = new Mfrac(base);
        } else if (localName.equals(Menclose.ELEMENT)) {
            element = new Menclose(base);
        } else if (localName.equals(Mphantom.ELEMENT)) {
            element = new Mphantom(base);
        } else if (localName.equals(Msup.ELEMENT)) {
            element = new Msup(base);
        } else if (localName.equals(Msub.ELEMENT)) {
            element = new Msub(base);
        } else if (localName.equals(Mmultiscripts.ELEMENT)) {
            element = new Mmultiscripts(base);
        } else if (localName.equals(Mprescripts.ELEMENT)) {
            element = new Mprescripts(base);
        } else if (localName.equals(Msubsup.ELEMENT)) {
            element = new Msubsup(base);
        } else if (localName.equals(Munder.ELEMENT)) {
            element = new Munder(base);
        } else if (localName.equals(Mover.ELEMENT)) {
            element = new Mover(base);
        } else if (localName.equals(Munderover.ELEMENT)) {
            element = new Munderover(base);
        } else if (localName.equals(Mspace.ELEMENT)) {
            element = new Mspace(base);
        } else if (localName.equals(Ms.ELEMENT)) {
            element = new Ms(base);
        } else if (localName.equals(Mstyle.ELEMENT)) {
            element = new Mstyle(base);
        } else if (localName.equals(Msqrt.ELEMENT)) {
            element = new Msqrt(base);
        } else if (localName.equals(Mroot.ELEMENT)) {
            element = new Mroot(base);
        } else if (localName.equals(Mtable.ELEMENT)) {
            element = new Mtable(base);
        } else if (localName.equals(Mtr.ELEMENT)) {
            element = new Mtr(base);
        } else if (localName.equals(Mlabeledtr.ELEMENT)) {
            element = new Mlabeledtr(base);
        } else if (localName.equals(Mtd.ELEMENT)) {
            element = new Mtd(base);
        } else if (localName.equals(Mo.ELEMENT)) {
            element = new Mo(base);
        } else if (localName.equals(Mi.ELEMENT)) {
            element = new Mi(base);
        } else if (localName.equals(Mn.ELEMENT)) {
            element = new Mn(base);
        } else if (localName.equals(Mtext.ELEMENT)) {
            element = new Mtext(base);
        } else if (localName.equals(Mrow.ELEMENT)) {
            element = new Mrow(base);
        } else if (localName.equals(Maligngroup.ELEMENT)) {
            element = new Maligngroup(base);
        } else if (localName.equals(Malignmark.ELEMENT)) {
            element = new Malignmark(base);
        } else if (localName.equals(Semantics.ELEMENT)) {
            element = new Semantics(base);
        } else if (localName.equals(Annotation.ELEMENT)) {
            element = new Annotation(base);
        } else if (localName.equals(Mpadded.ELEMENT)) {
            element = new Mpadded(base);
        } else if (localName.equals(Merror.ELEMENT)) {
            element = new Merror(base);
        } else if (localName.equals(Maction.ELEMENT)) {
            element = new Maction(base);
        } else if (localName.equals(Mglyph.ELEMENT)) {
            element = new Mglyph(base);
        } else {
            JEuclidElementFactory.LOGGER.info("Unsupported element: "
                    + localName);
            element = new Mrow(base);
        }

        ((AbstractJEuclidElement) element).setMathAttributes(aMap);

        return element;
    }

}

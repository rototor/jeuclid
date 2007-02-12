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

/* $Id: MathElementFactory.java,v 1.1.2.2 2007/02/12 08:46:47 maxberger Exp $ */

package net.sourceforge.jeuclid;

import net.sourceforge.jeuclid.element.MathAlignGroup;
import net.sourceforge.jeuclid.element.MathAlignMark;
import net.sourceforge.jeuclid.element.MathAnnotation;
import net.sourceforge.jeuclid.element.MathEnclose;
import net.sourceforge.jeuclid.element.MathFenced;
import net.sourceforge.jeuclid.element.MathFrac;
import net.sourceforge.jeuclid.element.MathIdentifier;
import net.sourceforge.jeuclid.element.MathLabeledTableRow;
import net.sourceforge.jeuclid.element.MathMathElement;
import net.sourceforge.jeuclid.element.MathMultiScripts;
import net.sourceforge.jeuclid.element.MathNumber;
import net.sourceforge.jeuclid.element.MathOperator;
import net.sourceforge.jeuclid.element.MathOver;
import net.sourceforge.jeuclid.element.MathPhantom;
import net.sourceforge.jeuclid.element.MathPreScripts;
import net.sourceforge.jeuclid.element.MathRoot;
import net.sourceforge.jeuclid.element.MathRow;
import net.sourceforge.jeuclid.element.MathSemantics;
import net.sourceforge.jeuclid.element.MathSpace;
import net.sourceforge.jeuclid.element.MathSqrt;
import net.sourceforge.jeuclid.element.MathString;
import net.sourceforge.jeuclid.element.MathStyle;
import net.sourceforge.jeuclid.element.MathSub;
import net.sourceforge.jeuclid.element.MathSubSup;
import net.sourceforge.jeuclid.element.MathSup;
import net.sourceforge.jeuclid.element.MathTable;
import net.sourceforge.jeuclid.element.MathTableData;
import net.sourceforge.jeuclid.element.MathTableRow;
import net.sourceforge.jeuclid.element.MathText;
import net.sourceforge.jeuclid.element.MathUnder;
import net.sourceforge.jeuclid.element.MathUnderOver;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.helpers.AttributeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.mathml.MathMLElement;

/**
 * Creates MathElements from given element strings.
 * 
 * @author Max Berger
 */
public final class MathElementFactory {

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory
            .getLog(MathElementFactory.class);

    private MathElementFactory() {
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
        if (localName.equals(MathMathElement.ELEMENT)) {
            element = new MathMathElement(base);
        } else if (localName.equals(MathFenced.ELEMENT)) {
            element = new MathFenced(base);
        } else if (localName.equals(MathFrac.ELEMENT)) {
            element = new MathFrac(base);
        } else if (localName.equals(MathEnclose.ELEMENT)) {
            element = new MathEnclose(base);
        } else if (localName.equals(MathPhantom.ELEMENT)) {
            element = new MathPhantom(base);
        } else if (localName.equals(MathSup.ELEMENT)) {
            element = new MathSup(base);
        } else if (localName.equals(MathSub.ELEMENT)) {
            element = new MathSub(base);
        } else if (localName.equals(MathMultiScripts.ELEMENT)) {
            element = new MathMultiScripts(base);
        } else if (localName.equals(MathPreScripts.ELEMENT)) {
            element = new MathPreScripts(base);
        } else if (localName.equals(MathSubSup.ELEMENT)) {
            element = new MathSubSup(base);
        } else if (localName.equals(MathUnder.ELEMENT)) {
            element = new MathUnder(base);
        } else if (localName.equals(MathOver.ELEMENT)) {
            element = new MathOver(base);
        } else if (localName.equals(MathUnderOver.ELEMENT)) {
            element = new MathUnderOver(base);
        } else if (localName.equals(MathSpace.ELEMENT)) {
            element = new MathSpace(base);
        } else if (localName.equals(MathString.ELEMENT)) {
            element = new MathString(base);
        } else if (localName.equals(MathStyle.ELEMENT)) {
            element = new MathStyle(base);
        } else if (localName.equals(MathSqrt.ELEMENT)) {
            element = new MathSqrt(base);
        } else if (localName.equals(MathRoot.ELEMENT)) {
            element = new MathRoot(base);
        } else if (localName.equals(MathTable.ELEMENT)) {
            element = new MathTable(base);
        } else if (localName.equals(MathTableRow.ELEMENT)) {
            element = new MathTableRow(base);
        } else if (localName.equals(MathLabeledTableRow.ELEMENT)) {
            element = new MathLabeledTableRow(base);
        } else if (localName.equals(MathTableData.ELEMENT)) {
            element = new MathTableData(base);
        } else if (localName.equals(MathOperator.ELEMENT)) {
            element = new MathOperator(base);
        } else if (localName.equals(MathIdentifier.ELEMENT)) {
            element = new MathIdentifier(base);
        } else if (localName.equals(MathNumber.ELEMENT)) {
            element = new MathNumber(base);
        } else if (localName.equals(MathText.ELEMENT)) {
            element = new MathText(base);
        } else if (localName.equals(MathRow.ELEMENT)) {
            element = new MathRow(base);
        } else if (localName.equals(MathAlignGroup.ELEMENT)) {
            element = new MathAlignGroup(base);
        } else if (localName.equals(MathAlignMark.ELEMENT)) {
            element = new MathAlignMark(base);
        } else if (localName.equals(MathSemantics.ELEMENT)) {
            element = new MathSemantics(base);
        } else if (localName.equals(MathAnnotation.ELEMENT)) {
            element = new MathAnnotation(base);
        } else {
            LOGGER.info("Unsupported element: " + localName);
            element = new MathRow(base);
        }

        ((AbstractMathElement) element).setMathAttributes(aMap);

        return element;
    }

}

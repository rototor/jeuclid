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

package net.sourceforge.jeuclid;

import net.sourceforge.jeuclid.element.MathAlignGroup;
import net.sourceforge.jeuclid.element.MathAlignMark;
import net.sourceforge.jeuclid.element.MathAnnotation;
import net.sourceforge.jeuclid.element.MathEnclose;
import net.sourceforge.jeuclid.element.MathFenced;
import net.sourceforge.jeuclid.element.MathFrac;
import net.sourceforge.jeuclid.element.MathIdentifier;
import net.sourceforge.jeuclid.element.MathLabeledTableRow;
import net.sourceforge.jeuclid.element.MathMultiScripts;
import net.sourceforge.jeuclid.element.MathNumber;
import net.sourceforge.jeuclid.element.MathOperator;
import net.sourceforge.jeuclid.element.MathOver;
import net.sourceforge.jeuclid.element.MathPhantom;
import net.sourceforge.jeuclid.element.MathPreScripts;
import net.sourceforge.jeuclid.element.MathRoot;
import net.sourceforge.jeuclid.element.MathRootElement;
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
import net.sourceforge.jeuclid.element.helpers.DOMAttributeMap;
import net.sourceforge.jeuclid.element.helpers.OperatorDictionary;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The builder for creating a MathElement tree.
 * 
 * @author <a href="mailto:stephan@vern.chem.tu-berlin.de">Stephan Michels</a>
 * @author Max Berger
 */
public class DOMMathBuilder {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(DOMMathBuilder.class);

    /**
     * Reference to the root element of the math elements tree.
     */
    private MathRootElement rootElement;

    private MathBase m_base;

    /**
     * Constructs a builder.
     * 
     * @param document
     *            The MathML document.
     * @param logger
     *            Logger of the document.
     * @param mathBase
     *            Math base
     */
    public DOMMathBuilder(Document document, MathBase mathBase) {

        m_base = mathBase;

        Element documentElement = document.getDocumentElement();

        rootElement = new MathRootElement(m_base);
        mathBase.setRootElement(rootElement);
        AttributeMap attributes = new DOMAttributeMap(documentElement
                .getAttributes());
        traverse(documentElement, rootElement, null);
        rootElement.eventInitSpecificAttributes(attributes);
        rootElement.eventAllElementsComplete();
    }

    /**
     * Returns the root element of a math tree.
     * 
     * @return Root element.
     */
    public MathRootElement getMathRootElement() {
        return rootElement;
    }

    /**
     * Creates a MathElement through traversing the DOM tree.
     * 
     * @param node
     *            Current element of the DOM tree.
     * @param parent
     *            Current element of the MathElement tree.
     * @param alignmentScope
     *            Alignment scope of elements.
     */
    private void traverse(Node node, AbstractMathElement parent,
            MathTableData alignmentScope) {
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return;
        }
        String tagname = node.getNodeName();
        int posSeparator = -1;

        if ((posSeparator = tagname.indexOf(":")) >= 0) {
            tagname = tagname.substring(posSeparator + 1);
        }
        AbstractMathElement element;
        AttributeMap attributes = new DOMAttributeMap(node.getAttributes());

        if (tagname.equals(MathRootElement.ELEMENT)) {
            element = new MathRow(m_base);
        } else if (tagname.equals(MathFenced.ELEMENT)) {
            element = new MathFenced(m_base);
        } else if (tagname.equals(MathFrac.ELEMENT)) {
            element = new MathFrac(m_base);
            if (attributes.hasAttribute("linethickness")) {
                ((MathFrac) element).setLinethickness(attributes.getString(
                        "linethickness", "2px"));
            }
            ((MathFrac) element).setBevelled(attributes.getBoolean("bevelled",
                    false));
        } else if (tagname.equals(MathSup.ELEMENT)) {
            element = new MathSup(m_base);
        } else if (tagname.equals(MathSub.ELEMENT)) {
            element = new MathSub(m_base);
        } else if (tagname.equals(MathSubSup.ELEMENT)) {
            element = new MathSubSup(m_base);
        } else if (tagname.equals(MathMultiScripts.ELEMENT)) {
            element = new MathMultiScripts(m_base);
        } else if (tagname.equals(MathPreScripts.ELEMENT)) {
            element = new MathPreScripts(m_base);
        } else if (tagname.equals(MathUnder.ELEMENT)) {
            element = new MathUnder(m_base);
            ((MathUnder) element).setAccentUnder(attributes.getBoolean(
                    "accentunder", false));
        } else if (tagname.equals(MathOver.ELEMENT)) {
            element = new MathOver(m_base);
            ((MathOver) element).setAccent(attributes.getBoolean("accent",
                    false));
        } else if (tagname.equals(MathUnderOver.ELEMENT)) {
            element = new MathUnderOver(m_base);
            ((MathUnderOver) element).setAccent(attributes.getBoolean("accent",
                    false));
            ((MathUnderOver) element).setAccentUnder(attributes.getBoolean(
                    "accentunder", false));
        } else if (tagname.equals(MathSpace.ELEMENT)) {
            element = new MathSpace(m_base);
            MathSpace space = (MathSpace) element;
            if (attributes.hasAttribute("height")) {
                space.setSpaceHeight(attributes.getString("height", "0"));
            }
            if (attributes.hasAttribute("width")) {
                space.setSpaceWidth(attributes.getString("width", "0"));
            }
            if (attributes.hasAttribute("depth")) {
                space.setDepth(attributes.getString("depth", "0"));
            }
        } else if (tagname.equals(MathSqrt.ELEMENT)) {
            element = new MathSqrt(m_base);
        } else if (tagname.equals(MathString.ELEMENT)) {
            element = new MathString(m_base);

            ((MathString) element).setLQuote(attributes.getString("lquote",
                    "\""));
            ((MathString) element).setRQuote(attributes.getString("rquote",
                    "\""));
        } else if (tagname.equals(MathRoot.ELEMENT)) {
            element = new MathRoot(m_base);
        } else if (tagname.equals(MathTable.ELEMENT)) {
            element = new MathTable(m_base);
            MathTable tabl = (MathTable) element;

            if (attributes.hasAttribute("groupalign")) {
                tabl.setGroupAlign(attributes.getString("groupalign", ""));
            }
            if (attributes.hasAttribute("rowspacing")) {
                tabl.setRowspacing(attributes.getString("rowspacing", ""));
            }
            if (attributes.hasAttribute("columnpacing")) {
                tabl.setColumnspacing(attributes.getString("columnpacing", ""));
            }
            if (attributes.hasAttribute("framespacing")) {
                tabl.setFramespacing(attributes.getString("framespacing", ""));
            }
            if (attributes.hasAttribute("align")) {
                tabl.setAlign(attributes.getString("align", "axis"));
            }

        } else if (tagname.equals(MathTableRow.ELEMENT)) {
            element = new MathTableRow(m_base);
            if (attributes.hasAttribute("groupalign")) {
                ((MathTableRow) element).setGroupAlign(attributes.getString(
                        "groupalign", ""));
            }
        } else if (tagname.equals(MathLabeledTableRow.ELEMENT)) {
            element = new MathLabeledTableRow(m_base);
        } else if (tagname.equals(MathEnclose.ELEMENT)) {
            element = new MathEnclose(m_base);
            if (attributes.hasAttribute("notation")) {
                ((MathEnclose) element).setNotation(attributes.getString(
                        "notation", ""));
            }
        } else if (tagname.equals(MathTableData.ELEMENT)) {
            element = new MathTableData(m_base);
            alignmentScope = (MathTableData) element;
            if (attributes.hasAttribute("groupalign")) {
                ((MathTableData) element).setGroupAlign(attributes.getString(
                        "groupalign", ""));
            }
        } else if (tagname.equals(MathOperator.ELEMENT)) {
            MathOperator mo = new MathOperator(m_base);
            element = mo;
            mo.setMoveableLimits(attributes.getBoolean("movablelimits", false));
            if (attributes.hasAttribute("rspace")) {
                mo.setRSpace(attributes.getString("rspace", ""));
            }
            if (attributes.hasAttribute("lspace")) {
                mo.setLSpace(attributes.getString("lspace", ""));
            }
            if (attributes.hasAttribute("form")) {
                String form = attributes.getString("form", "uknown");
                if (form.equals("uknown")) {
                    mo.setForm(MathOperator.FORM_UKNOWN);
                } else if (form.equals("infix")) {
                    mo.setForm(OperatorDictionary.VALUE_INFIX);
                } else if (form.equals("postfix")) {
                    mo.setForm(OperatorDictionary.VALUE_POSTFIX);
                } else if (form.equals("prefix")) {
                    mo.setForm(OperatorDictionary.VALUE_PREFIX);
                }
            }
        } else if (tagname.equals(MathPhantom.ELEMENT)) {
            element = new MathPhantom(m_base);
        } else if (tagname.equals(MathRow.ELEMENT)) {
            element = new MathRow(m_base);
        } else if (tagname.equals(MathStyle.ELEMENT)) {
            element = new MathStyle(m_base);
            if (attributes.hasAttribute("scriptsizemultiplier")) {
                float scrm = Float.valueOf(
                        attributes.getString("scriptsizemultiplier", String
                                .valueOf(element.getScriptSizeMultiplier())))
                        .floatValue();
                element.setScriptSizeMultiplier(scrm);
            }
        } else if (tagname.equals(MathIdentifier.ELEMENT)) {
            element = new MathIdentifier(m_base);
        } else if (tagname.equals(MathNumber.ELEMENT)) {
            element = new MathNumber(m_base);
        } else if (tagname.equals(MathText.ELEMENT)) {
            element = new MathText(m_base);
        } else if (tagname.equals(MathAlignGroup.ELEMENT)) {
            element = new MathAlignGroup(m_base);
            if (alignmentScope != null) {
                alignmentScope.addAlignGroupElement((MathAlignGroup) element);
            }
        } else if (tagname.equals(MathAlignMark.ELEMENT)) {
            element = new MathAlignMark(m_base);
            if (alignmentScope != null) {
                alignmentScope.addAlignMarkElement((MathAlignMark) element);
            }
        } else if (tagname.equals(MathSemantics.ELEMENT)) {
            element = new MathSemantics(m_base);
        } else if (tagname.equals(MathAnnotation.ELEMENT)) {
            element = new MathAnnotation(m_base);
        } else {
            if (!tagname.equals(AbstractMathElement.ELEMENT)) {
                logger.warn("Warning: element " + tagname
                        + " is not implemented.");
            }
            element = new MathRow(m_base);
        }

        if (parent instanceof MathLabeledTableRow) {
            if (((MathLabeledTableRow) parent).labelIgnored) {
                parent.addMathElement(element);
            } else {
                ((MathLabeledTableRow) parent).labelIgnored = true;
            }
        } else {
            parent.addMathElement(element);
        }

        if (attributes.hasAttribute("mathbackground")) {
            // element.setMathBackground(attributes.getColor("mathbackground",
            // null));
        }

        NodeList childs = node.getChildNodes();
        MathTableData prevScope = null;

        if (tagname.equals(MathTableData.ELEMENT)) {
            prevScope = alignmentScope;
            alignmentScope = (MathTableData) element;
        }

        for (int i = 0; i < childs.getLength(); i++) {
            if (childs.item(i).getNodeType() == Node.ELEMENT_NODE) {
                traverse(childs.item(i), element, alignmentScope);
            } else if (childs.item(i).getNodeType() == Node.TEXT_NODE) {
                element.addText(childs.item(i).getNodeValue());
            } else if (childs.item(i).getNodeType() == Node.ENTITY_REFERENCE_NODE
                    && childs.item(i).hasChildNodes()) {
                String entityValue = childs.item(i).getFirstChild()
                        .getNodeValue();
                if (entityValue != null) {
                    element.addText(entityValue);
                }
            }
        }

        element.eventInitSpecificAttributes(attributes);
        element.eventElementComplete();
        if (prevScope != null) {
            alignmentScope = prevScope;
        }

    }

}

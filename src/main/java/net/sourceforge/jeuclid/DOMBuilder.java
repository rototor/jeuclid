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

import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.JEuclidElementFactory;
import net.sourceforge.jeuclid.elements.generic.DocumentElement;
import net.sourceforge.jeuclid.elements.presentation.general.Mstyle;
import net.sourceforge.jeuclid.elements.presentation.table.Maligngroup;
import net.sourceforge.jeuclid.elements.presentation.table.Malignmark;
import net.sourceforge.jeuclid.elements.presentation.table.Mlabeledtr;
import net.sourceforge.jeuclid.elements.presentation.table.Mtd;
import net.sourceforge.jeuclid.elements.support.attributes.AttributeMap;
import net.sourceforge.jeuclid.elements.support.attributes.DOMAttributeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The builder for creating a MathElement tree.
 * 
 * @author <a href="mailto:stephan@vern.chem.tu-berlin.de">Stephan Michels</a>
 * @author Max Berger
 * @version $Revision$
 */
public class DOMBuilder {
    /**
     * Logger for this class
     */
    // unused
    // private static final Log LOGGER =
    // LogFactory.getLog(DOMMathBuilder.class);
    /**
     * Reference to the root element of the math elements tree.
     */
    private DocumentElement rootElement;

    private MathBase mbase;

    /**
     * Constructs a builder.
     * 
     * @param document
     *            The MathML document.
     * @param mathBase
     *            Math base
     */
    public DOMBuilder(final Document document, final MathBase mathBase) {

        this.mbase = mathBase;

        final Element documentElement = document.getDocumentElement();

        this.rootElement = new DocumentElement(this.mbase);
        mathBase.setRootElement(this.rootElement);

        this.traverse(documentElement, this.rootElement, null);
        // TODO: When changeTracking is updated to be disabled during initial
        // buildup, this is the place to trigger first changeEvents
    }

    /**
     * Returns the root element of a math tree.
     * 
     * @return Root element.
     */
    public DocumentElement getMathRootElement() {
        return this.rootElement;
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
    private void traverse(final Node node, final Node parent,
            Mtd alignmentScope) {
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return;
        }
        String tagname = node.getNodeName();
        int posSeparator = -1;

        if ((posSeparator = tagname.indexOf(":")) >= 0) {
            tagname = tagname.substring(posSeparator + 1);
        }
        final AttributeMap attributes = new DOMAttributeMap(node
                .getAttributes());

        final AbstractJEuclidElement element = (AbstractJEuclidElement) JEuclidElementFactory
                .elementFromName(tagname, attributes, this.mbase);

        // TODO: All theses should be handled within the appropriate class
        if (tagname.equals(Mtd.ELEMENT)) {
            alignmentScope = (Mtd) element;
        } else if (tagname.equals(Mstyle.ELEMENT)) {
            if (attributes.hasAttribute("scriptsizemultiplier")) {
                final float scrm = Float.valueOf(
                        attributes.getString("scriptsizemultiplier", String
                                .valueOf(element.getScriptSizeMultiplier())))
                        .floatValue();
                element.setScriptSizeMultiplier(scrm);
            }
        } else if (tagname.equals(Maligngroup.ELEMENT)) {
            if (alignmentScope != null) {
                alignmentScope.addAlignGroupElement((Maligngroup) element);
            }
        } else if (tagname.equals(Malignmark.ELEMENT)) {
            if (alignmentScope != null) {
                alignmentScope.addAlignMarkElement((Malignmark) element);
            }
        }
        // end of TODO

        if (parent instanceof Mlabeledtr) {
            if (((Mlabeledtr) parent).labelIgnored) {
                parent.appendChild(element);
            } else {
                ((Mlabeledtr) parent).labelIgnored = true;
            }
        } else {
            parent.appendChild(element);
        }

        final NodeList childs = node.getChildNodes();
        Mtd prevScope = null;

        if (tagname.equals(Mtd.ELEMENT)) {
            prevScope = alignmentScope;
            alignmentScope = (Mtd) element;
        }

        for (int i = 0; i < childs.getLength(); i++) {
            final Node childNode = childs.item(i);
            final short childNodeType = childNode.getNodeType();
            if (childNodeType == Node.ELEMENT_NODE) {
                this.traverse(childNode, element, alignmentScope);
            } else if (childNodeType == Node.TEXT_NODE) {
                element.addText(childNode.getNodeValue());
            } else if (childNodeType == Node.ENTITY_REFERENCE_NODE
                    && childNode.hasChildNodes()) {
                final String entityValue = childNode.getFirstChild()
                        .getNodeValue();
                if (entityValue != null) {
                    element.addText(entityValue);
                }
            }
        }

        if (prevScope != null) {
            alignmentScope = prevScope;
        }

    }

}

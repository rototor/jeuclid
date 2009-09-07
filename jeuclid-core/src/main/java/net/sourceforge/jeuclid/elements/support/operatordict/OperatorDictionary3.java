/*
 * Copyright 2009 - 2009 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.elements.support.operatordict;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.sourceforge.jeuclid.Constants;
import net.sourceforge.jeuclid.parser.Parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Implements an operator dictionary based on the MathML 3 spec.
 * 
 * @version $Revision$
 */
public final class OperatorDictionary3 extends AbstractOperatorDictionary
        implements Serializable {

    /**
     * Logger for this class.
     */
    private static final Log LOGGER = LogFactory
            .getLog(OperatorDictionary3.class);

    private static final String NO_SPACE = "0em";

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * MathML dictionary resource.
     */
    private static final String DICTIONARY_FILE = "/net/sourceforge/jeuclid/appendixc.xml";

    /**
     * MathML dictionary serialized resource.
     */
    private static final String DICTIONARY_SERIALIZED = "/net/sourceforge/jeuclid/appendixc.ser";

    /**
     * The instance of the Dictionary
     */
    private static OperatorDictionary instance;

    private class PersonalNamespaceContext implements NamespaceContext {

        public PersonalNamespaceContext() {
        }

        /** {@inheritDoc} */
        public String getNamespaceURI(final String prefix) {
            final String retVal;
            if ("html".equals(prefix)) {
                retVal = "http://www.w3.org/1999/xhtml";
            } else if ("xml".equals(prefix)) {
                retVal = XMLConstants.XML_NS_URI;
            } else {
                retVal = XMLConstants.NULL_NS_URI;
            }
            return retVal;
        }

        /** {@inheritDoc} */
        // This method isn't necessary for XPath processing.
        public String getPrefix(final String uri) {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        // This method isn't necessary for XPath processing either.
        public Iterator<String> getPrefixes(final String uri) {
            throw new UnsupportedOperationException();
        }

    }

    private OperatorDictionary3() {
        // nothing to do.
    }

    /**
     * Get the for singleton instance.
     * 
     * @return an instance of OperatorDictionary.
     */
    public static OperatorDictionary getInstance() {
        synchronized (OperatorDictionary3.class) {
            if (OperatorDictionary3.instance == null) {
                final OperatorDictionary newDict = AbstractOperatorDictionary
                        .deserialize(OperatorDictionary3.DICTIONARY_SERIALIZED);
                if (newDict == null) {
                    OperatorDictionary3.instance = new OperatorDictionary3();
                } else {
                    OperatorDictionary3.instance = newDict;
                }
            }
        }
        return OperatorDictionary3.instance;
    }

    /** {@inheritDoc} */
    @Override
    protected void initializeFromXML(
            final Map<OperatorAttribute, Map<String, Map<OperatorForm, String>>> dict) {
        try {
            final Document doc = this.loadDocument();
            final XPath xpath = this.createXPath();
            this.extractValuesFromDocument(doc, xpath, dict);
        } catch (final SAXException e) {
            OperatorDictionary3.LOGGER.warn(
                    "XML Could not be parsed in operator dictionary", e);
        } catch (final IOException e) {
            OperatorDictionary3.LOGGER.warn(
                    "IO error reading operator dictionary", e);
        } catch (final XPathExpressionException e) {
            OperatorDictionary3.LOGGER.warn(
                    "XPath error in operator dictionary", e);
        }
    }

    /**
     * @param doc
     * @param xpath
     * @throws XPathExpressionException
     */
    private void extractValuesFromDocument(
            final Document doc,
            final XPath xpath,
            final Map<OperatorAttribute, Map<String, Map<OperatorForm, String>>> dict)
            throws XPathExpressionException {
        final XPathExpression expr = xpath
                .compile("//html:table[@class='sortable']/html:tbody/html:tr");

        final XPathExpression operatorExpr = xpath.compile("html:th[2]/text()");
        final XPathExpression formExpr = xpath.compile("html:th[4]/text()");
        final XPathExpression lspaceExpr = xpath.compile("html:td[2]/text()");
        final XPathExpression rspaceExpr = xpath.compile("html:td[3]/text()");
        final XPathExpression minsizeExpr = xpath.compile("html:td[4]/text()");
        final XPathExpression propertiesExpr = xpath
                .compile("html:td[5]/text()");

        final NodeList result = (NodeList) expr.evaluate(doc,
                XPathConstants.NODESET);

        for (int i = 0; i < result.getLength(); i++) {
            final Node trNode = result.item(i);
            final String operator = (String) operatorExpr.evaluate(trNode,
                    XPathConstants.STRING);
            final String formStr = (String) formExpr.evaluate(trNode,
                    XPathConstants.STRING);
            final OperatorForm form = OperatorForm.parseOperatorForm(formStr);
            final String lspace = (String) lspaceExpr.evaluate(trNode,
                    XPathConstants.STRING);
            final String rspace = (String) rspaceExpr.evaluate(trNode,
                    XPathConstants.STRING);
            final String minsize = (String) minsizeExpr.evaluate(trNode,
                    XPathConstants.STRING);
            final String propertiesString = (String) propertiesExpr.evaluate(
                    trNode, XPathConstants.STRING);

            this.addAttr(operator, form, OperatorAttribute.LSPACE, this
                    .intToSpace(lspace), dict);
            this.addAttr(operator, form, OperatorAttribute.RSPACE, this
                    .intToSpace(rspace), dict);
            if (minsize.length() > 0) {
                this.addAttr(operator, form, OperatorAttribute.MINSIZE,
                        minsize, dict);
            }
            this.addProperties(operator, form, propertiesString, dict);
        }
    }

    /**
     * @return
     * @throws SAXException
     * @throws IOException
     */
    private Document loadDocument() throws SAXException, IOException {
        final InputStream is = OperatorDictionary3.class
                .getResourceAsStream(OperatorDictionary3.DICTIONARY_FILE);
        final Document doc = Parser.getInstance().getDocumentBuilder()
                .parse(is);
        return doc;
    }

    /**
     * @return
     */
    private XPath createXPath() {
        final XPathFactory factory = XPathFactory.newInstance();
        final XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new PersonalNamespaceContext());
        return xpath;
    }

    /**
     * @param operator
     * @param form
     * @param propertiesString
     */
    private void addProperties(
            final String operator,
            final OperatorForm form,
            final String propertiesString,
            final Map<OperatorAttribute, Map<String, Map<OperatorForm, String>>> dict) {
        final String[] properties = propertiesString.split(" ");
        for (final String property : properties) {
            if (property.length() > 0) {
                try {
                    final OperatorAttribute oa = OperatorAttribute
                            .parseOperatorAttribute(property);
                    this.addAttr(operator, form, oa, Constants.TRUE, dict);
                } catch (final UnknownAttributeException uae) {
                    OperatorDictionary3.LOGGER.warn(
                            "Unkown Attribute when reading operator dictionary: "
                                    + property, uae);
                }
            }
        }
    }

    /**
     * @param operator
     * @param form
     * @param lspace
     * @param intToSpace
     */
    private void addAttr(
            final String operator,
            final OperatorForm form,
            final OperatorAttribute attribute,
            final String value,
            final Map<OperatorAttribute, Map<String, Map<OperatorForm, String>>> dict) {

        Map<String, Map<OperatorForm, String>> innerMap1 = dict.get(attribute);
        if (innerMap1 == null) {
            innerMap1 = new HashMap<String, Map<OperatorForm, String>>();
            dict.put(attribute, innerMap1);
        }

        Map<OperatorForm, String> innerMap2 = innerMap1.get(operator);
        if (innerMap2 == null) {
            innerMap2 = new EnumMap<OperatorForm, String>(OperatorForm.class);
            innerMap1.put(operator, innerMap2);
        }

        innerMap2.put(form, value);
    }

    /**
     * @param lspace
     * @return
     */
    private String intToSpace(final String spaceInt) {
        String retVal;
        try {
            final int i = Integer.parseInt(spaceInt);
            switch (i) {
            case 0:
                retVal = OperatorDictionary3.NO_SPACE;
                break;
            case 1:
                retVal = OperatorDictionary.NAME_VERYVERYTHINMATHSPACE;
                break;
            case 2:
                retVal = OperatorDictionary.NAME_VERYTHINMATHSPACE;
                break;
            case 3:
                retVal = OperatorDictionary.NAME_THINMATHSPACE;
                break;
            case 4:
                retVal = OperatorDictionary.NAME_MEDIUMMATHSPACE;
                break;
            case 5:
                retVal = OperatorDictionary.NAME_THICKMATHSPACE;
                break;
            case 6:
                retVal = OperatorDictionary.NAME_VERYTHICKMATHSPACE;
                break;
            case 7:
                retVal = OperatorDictionary.NAME_VERYVERYTHICKMATHSPACE;
                break;
            default:
                retVal = OperatorDictionary3.NO_SPACE;
            }
        } catch (final NumberFormatException e) {
            retVal = OperatorDictionary3.NO_SPACE;
        }
        return retVal;
    }

}

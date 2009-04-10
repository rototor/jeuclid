/*
 * Copyright 2002 - 2008 JEuclid, http://jeuclid.sf.net
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

package euclid.elements.support.operatordict;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import euclid.elements.presentation.token.Mo;

/**
 * Read default values of operators from xml file.
 * 
 * @version $Revision$
 */
public final class OperatorDictionary implements Serializable {

    /**
     * name for VERYVERYTHINMATHSPACE size of math space.
     */
    public static final String NAME_VERYVERYTHINMATHSPACE = "veryverythinmathspace";

    /**
     * name for VERYTHINMATHSPACE size of math space.
     */
    public static final String NAME_VERYTHINMATHSPACE = "verythinmathspace";

    /**
     * name for THINMATHSPACE size of math space.
     */
    public static final String NAME_THINMATHSPACE = "thinmathspace";

    /**
     * name for MEDIUMMATHSPACE size of math space.
     */
    public static final String NAME_MEDIUMMATHSPACE = "mediummathspace";

    /**
     * name for THICKMATHSPACE size of math space.
     */
    public static final String NAME_THICKMATHSPACE = "thickmathspace";

    /**
     * name for VERYTHICKMATHSPACE size of math space.
     */
    public static final String NAME_VERYTHICKMATHSPACE = "verythickmathspace";

    /**
     * name for VERYVERYTHICKMATHSPACE size of math space.
     */
    public static final String NAME_VERYVERYTHICKMATHSPACE = "veryverythickmathspace";

    /**
     * name for INFINITY size of math space.
     */
    public static final String NAME_INFINITY = "infinity";

    /** Form value for prefix. */
    public static final String FORM_PREFIX = "prefix";

    /** form value for infix. */
    public static final String FORM_INFIX = "infix";

    /** form value for postfix. */
    public static final String FORM_POSTFIX = "postfix";

    /**
     * This value is returned, when default value of operator attribute
     * doesn't exist in this dictionary so far.
     */
    public static final String VALUE_UNKNOWN = "NULL";

    /**
     * Logger for this class.
     */
    private static final Log LOGGER = LogFactory
            .getLog(OperatorDictionary.class);

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * MathML dictionary resource.
     */
    private static final String DICTIONARY_FILE = "/moDictionary.xml"; // "/moDictionary.xml";

    /**
     * The instance of the Dictionary
     */
    private static OperatorDictionary instance;

    private final Map<OperatorAttribute, Map<String, Map<OperatorForm, String>>> dict;

    private OperatorDictionary() {
        this.dict = new EnumMap<OperatorAttribute, Map<String, Map<OperatorForm, String>>>(
                OperatorAttribute.class);
        this.initializeFromXML();
    }

    /**
     * Get the for singleton instance.
     * 
     * @return an instance of OperatorDictionary.
     */
    public static synchronized OperatorDictionary getInstance() {
        if (OperatorDictionary.instance == null) {
            OperatorDictionary newDict = null;
            try {
                final InputStream is = OperatorDictionary.class
                        .getResourceAsStream("/moDictionar2.ser");
                final ObjectInput oi = new ObjectInputStream(is);
                newDict = (OperatorDictionary) oi.readObject();
                oi.close();
            } catch (final ClassNotFoundException cnfe) {
                newDict = null;
            } catch (final IllegalArgumentException e) {
                newDict = null;
            } catch (final IOException e) {
                newDict = null;
            } catch (final NullPointerException e) {
                newDict = null;
            }
            if (newDict == null) {
                OperatorDictionary.instance = new OperatorDictionary();
            } else {
                OperatorDictionary.instance = newDict;
            }
        }
        return OperatorDictionary.instance;
    }

    /**
     * Initializes Dictionary.
     */
    private void initializeFromXML() {
        try {
            final InputStream dictInput = OperatorDictionary.class
                    .getResourceAsStream(OperatorDictionary.DICTIONARY_FILE);
            final SAXParserFactory factory = SAXParserFactory.newInstance();
            final XMLReader reader = factory.newSAXParser().getXMLReader();
            reader.setContentHandler(new DictionaryReader());
            reader.parse(new InputSource(dictInput));
        } catch (final ParserConfigurationException e) {
            OperatorDictionary.LOGGER.warn("Cannot get SAXParser:"
                    + e.getMessage());
        } catch (final SAXException e) {
            OperatorDictionary.LOGGER
                    .warn("SAXException while parsing dictionary:"
                            + e.getMessage());
        } catch (final IOException e) {
            OperatorDictionary.LOGGER.warn(
                    "Read error while accessing XML dictionary", e);
        }
    }

    /**
     * Determines default value of the operator attribute.
     * 
     * @param operator
     *            operator character
     * @param form
     *            form string
     * @param attributeName
     *            name of attribute
     * @return VALUE_UNKOWN or value from dict.
     * @throws UnknownAttributeException
     *             Raised, if wrong attributeName was provided.
     */
    public String getDefaultAttributeValue(final String operator,
            final String form, final String attributeName)
            throws UnknownAttributeException {
        final OperatorForm intForm = OperatorForm.parseOperatorForm(form);
        return this.getDefaultAttributeValue(operator, intForm,
                OperatorAttribute.parseOperatorAttribute(attributeName));
    }

    /**
     * Determines default value of the operator attribute.
     * 
     * @param operator
     *            Operator character.
     * @param form
     *            Form value
     * @param attributeName
     *            Name of the attribute.
     * @return Default value (VALUE_UNKNOWN, if default value has not been
     *         provided yet).
     * @throws UnknownAttributeException
     *             Raised, if wrong attributeName was provided.
     */
    private String getDefaultAttributeValue(final String operator,
            final OperatorForm form, final OperatorAttribute attribute) {

        final Map<String, Map<OperatorForm, String>> opForAttr = this.dict
                .get(attribute);
        if (opForAttr == null) {
            return attribute.getDefaultValue();
        }
        final Map<OperatorForm, String> valuesPerForm = opForAttr
                .get(operator);
        String retVal;
        if (valuesPerForm == null) {
            retVal = attribute.getDefaultValue();
        } else {
            retVal = valuesPerForm.get(form);
            if (retVal == null) {
                retVal = valuesPerForm.get(OperatorForm.INFIX);
            }
            if (retVal == null) {
                retVal = valuesPerForm.get(OperatorForm.POSTFIX);
            }
            if (retVal == null) {
                retVal = valuesPerForm.get(OperatorForm.PREFIX);
            }
            if (retVal == null) {
                retVal = attribute.getDefaultValue();
            }
        }
        return retVal;
    }

    /**
     * The DictionaryReader reads dictionary XML file and initializes
     * Dictionary fields.
     */
    private class DictionaryReader extends DefaultHandler {
        private static final String ELEMENT_ELEMENT = "element";

        private String currentOperator;

        private OperatorForm currentFormIndex;

        private Map<OperatorAttribute, String> currentEntry;

        public DictionaryReader() {
            // makes findbugs happy
            this.currentEntry = null;
        }

        @Override
        public void startDocument() throws SAXException {
            // nothing to do.
        }

        @Override
        public void endDocument() throws SAXException {
            // nothing to do.
        }

        @Override
        public void startElement(final String uri, final String localName,
                final String rawName, final Attributes attlist)
                throws SAXException {

            if (rawName
                    .equals(OperatorDictionary.DictionaryReader.ELEMENT_ELEMENT)) {
                this.currentEntry = new TreeMap<OperatorAttribute, String>();
                final String form = attlist.getValue(Mo.ATTR_FORM);
                if (form == null) {
                    // it is impossible because "form" is required attribute
                    // for the dictionary.
                    OperatorDictionary.LOGGER
                            .fatal("Error in dictionary, attribute 'form' is required attribute for the dictionary");
                    this.currentFormIndex = OperatorForm.INFIX;
                } else {
                    this.currentFormIndex = OperatorForm
                            .parseOperatorForm(form);
                }
                for (int i = 0; i < attlist.getLength(); i++) {
                    final String attName = attlist.getQName(i);
                    final String attValue = attlist.getValue(i);
                    if (!attName.equals(Mo.ATTR_FORM)) {
                        try {
                            this.currentEntry.put(OperatorAttribute
                                    .parseOperatorAttribute(attName),
                                    attValue);
                        } catch (final UnknownAttributeException e) {
                            OperatorDictionary.LOGGER.fatal(e.getMessage());
                        }
                    }
                }
            }
        }

        @Override
        public void endElement(final String uri, final String localName,
                final String rawName) throws SAXException {
            if (rawName
                    .equals(OperatorDictionary.DictionaryReader.ELEMENT_ELEMENT)) {

                for (final Map.Entry<OperatorAttribute, String> attributeValues : this.currentEntry
                        .entrySet()) {
                    final OperatorAttribute attribute = attributeValues
                            .getKey();
                    final String value = attributeValues.getValue();
                    Map<String, Map<OperatorForm, String>> mapForAttr = OperatorDictionary.this.dict
                            .get(attribute);
                    if (mapForAttr == null) {
                        mapForAttr = new TreeMap<String, Map<OperatorForm, String>>();
                        OperatorDictionary.this.dict.put(attribute,
                                mapForAttr);
                    }
                    Map<OperatorForm, String> valueForForm = mapForAttr
                            .get(this.currentOperator);
                    if (valueForForm == null) {
                        valueForForm = new EnumMap<OperatorForm, String>(
                                OperatorForm.class);
                        mapForAttr.put(this.currentOperator, valueForForm);
                    }
                    valueForForm.put(this.currentFormIndex, value);
                }
            }
            this.currentEntry = null;
            this.currentOperator = null;
        }

        @Override
        public void characters(final char[] data, final int start,
                final int length) throws SAXException {
            if (this.currentEntry != null) {
                final char[] temp = new char[length];
                System.arraycopy(data, start, temp, 0, length);
                if (this.currentOperator == null) {
                    this.currentOperator = new String(temp);
                } else {
                    this.currentOperator += new String(temp);
                }
                this.currentOperator = this.currentOperator.trim();
            }
        }
    }

}

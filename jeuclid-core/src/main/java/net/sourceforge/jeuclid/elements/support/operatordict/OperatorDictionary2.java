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

package net.sourceforge.jeuclid.elements.support.operatordict;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import net.sourceforge.jeuclid.elements.presentation.token.Mo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Read default values of operators from xml file.
 * 
 * @version $Revision$
 */
public final class OperatorDictionary2 extends AbstractOperatorDictionary
        implements Serializable {

    /**
     * Logger for this class.
     */
    private static final Log LOGGER = LogFactory
            .getLog(OperatorDictionary2.class);

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * MathML dictionary resource.
     */
    private static final String DICTIONARY_FILE = "/net/sourceforge/jeuclid/moDictionary.xml";

    /**
     * MathML dictionary serialized resource.
     */
    private static final String DICTIONARY_SERIALIZED = "/net/sourceforge/jeuclid/moDictionary.ser";

    /**
     * The instance of the Dictionary
     */
    private static OperatorDictionary instance;

    private OperatorDictionary2() {
        // everything is done in superclass.
    }

    /**
     * Get the for singleton instance.
     * 
     * @return an instance of OperatorDictionary.
     */
    public static OperatorDictionary getInstance() {
        synchronized (OperatorDictionary2.class) {
            if (OperatorDictionary2.instance == null) {
                final OperatorDictionary newDict = AbstractOperatorDictionary
                        .deserialize(OperatorDictionary2.DICTIONARY_SERIALIZED);
                if (newDict == null) {
                    OperatorDictionary2.instance = new OperatorDictionary2();
                } else {
                    OperatorDictionary2.instance = newDict;
                }
            }
        }
        return OperatorDictionary2.instance;
    }

    /** {@inheritDoc} */
    @Override
    protected void initializeFromXML(
            final Map<OperatorAttribute, Map<String, Map<OperatorForm, String>>> dict) {
        InputStream dictInput = null;
        try {
            dictInput = OperatorDictionary2.class
                    .getResourceAsStream(OperatorDictionary2.DICTIONARY_FILE);
            final SAXParserFactory factory = SAXParserFactory.newInstance();
            final XMLReader reader = factory.newSAXParser().getXMLReader();
            reader.setContentHandler(new DictionaryReader(dict));
            reader.parse(new InputSource(dictInput));
        } catch (final ParserConfigurationException e) {
            OperatorDictionary2.LOGGER.warn("Cannot get SAXParser:"
                    + e.getMessage());
        } catch (final SAXException e) {
            OperatorDictionary2.LOGGER
                    .warn("SAXException while parsing dictionary:"
                            + e.getMessage());
        } catch (final IOException e) {
            OperatorDictionary2.LOGGER.warn(
                    "Read error while accessing XML dictionary", e);
        } finally {
            if (dictInput != null) {
                try {
                    dictInput.close();
                } catch (final IOException io) {
                    OperatorDictionary2.LOGGER.warn(
                            "Error closing XML dictionary", io);
                }
            }
        }
    }

    /**
     * The DictionaryReader reads dictionary XML file and initializes Dictionary
     * fields.
     */
    private class DictionaryReader extends DefaultHandler {
        private static final String ELEMENT_ELEMENT = "element";

        private String currentOperator;

        private OperatorForm currentFormIndex;

        private Map<OperatorAttribute, String> currentEntry;
        private final Map<OperatorAttribute, Map<String, Map<OperatorForm, String>>> dict;

        public DictionaryReader(
                final Map<OperatorAttribute, Map<String, Map<OperatorForm, String>>> d) {
            // makes findbugs happy
            this.dict = d;
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
                    .equals(OperatorDictionary2.DictionaryReader.ELEMENT_ELEMENT)) {
                this.currentEntry = new TreeMap<OperatorAttribute, String>();
                final String form = attlist.getValue(Mo.ATTR_FORM);
                if (form == null) {
                    // it is impossible because "form" is required attribute
                    // for the dictionary.
                    OperatorDictionary2.LOGGER
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
                                    .parseOperatorAttribute(attName), attValue);
                        } catch (final UnknownAttributeException e) {
                            OperatorDictionary2.LOGGER.fatal(e.getMessage());
                        }
                    }
                }
            }
        }

        @Override
        public void endElement(final String uri, final String localName,
                final String rawName) throws SAXException {
            if (rawName
                    .equals(OperatorDictionary2.DictionaryReader.ELEMENT_ELEMENT)) {

                for (final Map.Entry<OperatorAttribute, String> attributeValues : this.currentEntry
                        .entrySet()) {
                    final OperatorAttribute attribute = attributeValues
                            .getKey();
                    final String value = attributeValues.getValue();
                    Map<String, Map<OperatorForm, String>> mapForAttr = this.dict
                            .get(attribute);
                    if (mapForAttr == null) {
                        mapForAttr = new TreeMap<String, Map<OperatorForm, String>>();
                        this.dict.put(attribute, mapForAttr);
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

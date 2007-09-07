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

package net.sourceforge.jeuclid.elements.support.operatordict;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import net.sourceforge.jeuclid.Constants;
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
 * @author Unkown
 * @author Max Berger
 * @version $Revision$
 */
public class OperatorDictionary {

    /**
     * MathML dictionary resource.
     */
    public static final String DICTIONARY_FILE = "/moDictionary.xml";

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

    /**
     * Value for PREFIX.
     */
    public static final int VALUE_PREFIX = 100;

    /** Form value for prefix. */
    public static final String FORM_PREFIX = "prefix";

    /**
     * Value for INFIX.
     */
    public static final int VALUE_INFIX = 101;

    /** form value for infix. */
    public static final String FORM_INFIX = "infix";

    /**
     * Value for POSTFIX.
     */
    public static final int VALUE_POSTFIX = 102;

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
     * Hashtable that contains dictionary itself. Where a key = (value of
     * operator)+ (type of form) and the two-dimensional array as a value. The
     * array contains name-value pairs of attributes that have to be applied
     * for the key. For example: <element form="infix" fence="true"
     * lspace="0em" rspace="0em">+</element> key =+101(101 - because constant
     * value for form 'infix' is 101), and values: { {"lspace",
     * "thickmathspace"} {"rspace", "thickmathspace"} }
     */
    private static Map<String, String[][]> dictionary;

    /**
     * flag - is we read dictionary from modictionary.xml
     */
    private static boolean isDictionaryRead;

    /**
     * The instance of the Dictionary
     */
    private static OperatorDictionary instance;

    /**
     * Array of default values of operators attributes.
     */
    private static final String[][] ATTRIBUTE_DEFAULT_VALUES = {
            { Mo.ATTR_FORM, OperatorDictionary.FORM_INFIX },
            { "fence", Constants.FALSE }, { "separator", Constants.FALSE },
            { "lspace", OperatorDictionary.NAME_THICKMATHSPACE },
            { "rspace", OperatorDictionary.NAME_THICKMATHSPACE },
            { "stretchy", Constants.FALSE }, { "symmetric", Constants.TRUE },
            { "maxsize", OperatorDictionary.NAME_INFINITY },
            { "minsize", "1" }, { "largeop", Constants.FALSE },
            { "movablelimits", Constants.FALSE },
            { "accent", Constants.FALSE }, };

    OperatorDictionary() throws DictionaryException {
        this.initialize();
    }

    /**
     * Initializes Dictionary
     */
    private void initialize() throws DictionaryException {
        try {
            final InputStream dictInput = OperatorDictionary.class
                    .getResourceAsStream(OperatorDictionary.DICTIONARY_FILE);
            final SAXParserFactory factory = SAXParserFactory.newInstance();
            final XMLReader reader = factory.newSAXParser().getXMLReader();
            reader.setContentHandler(new DictionaryReader());
            reader.parse(new InputSource(dictInput));
        } catch (final ParserConfigurationException e) {
            throw new DictionaryException("Cannot get SAXParser:"
                    + e.getMessage());
        } catch (final SAXException e) {
            throw new DictionaryException(
                    "SAXException while parsing dictionary:" + e.getMessage());
        } catch (final IOException e) {
            throw new DictionaryException(
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
     * @see #getDefaultAttributeValue(String, int, String)
     */
    public static String getDefaultAttributeValue(final String operator,
            final String form, final String attributeName)
            throws UnknownAttributeException {
        final int intForm;
        if (OperatorDictionary.FORM_INFIX.equalsIgnoreCase(form)) {
            intForm = OperatorDictionary.VALUE_INFIX;
        } else if (OperatorDictionary.FORM_POSTFIX.equalsIgnoreCase(form)) {
            intForm = OperatorDictionary.VALUE_POSTFIX;
        } else if (OperatorDictionary.FORM_PREFIX.equalsIgnoreCase(form)) {
            intForm = OperatorDictionary.VALUE_PREFIX;
        } else {
            intForm = OperatorDictionary.VALUE_INFIX;
        }
        return OperatorDictionary.getDefaultAttributeValue(operator, intForm,
                attributeName);
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
    public static String getDefaultAttributeValue(final String operator,
            final int form, final String attributeName)
            throws UnknownAttributeException {

        OperatorDictionary.loadDictIfNeeded();
        String retVal = null;
        if (OperatorDictionary.instance != null) {
            retVal = OperatorDictionary.getAttrFromDict(operator, form,
                    attributeName);
        }
        if (retVal == null) {
            retVal = OperatorDictionary.getDefaultValue(attributeName);
        }
        if (retVal != null) {
            return retVal;
        } else {
            throw new UnknownAttributeException(attributeName);
        }
    }

    private static String getAttrFromDict(final String operator,
            final int form, final String attributeName) {
        String retVal = null;
        final String[][] attribute = OperatorDictionary
                .readDictValueWithForm(operator, form);
        if (attribute == null) {
            retVal = OperatorDictionary.VALUE_UNKNOWN;
        } else {
            for (final String[] element : attribute) {
                if (element[0].equals(attributeName)) {
                    retVal = element[1];
                    break;
                }
            }
        }
        return retVal;
    }

    private static String[][] readDictValueWithForm(final String operator,
            final int form) {
        /*
         * If the operator does not occur in the dictionary with the specified
         * form, the renderer should use one of the forms that is available
         * there, in the order of preference: infix, postfix, prefix; if no
         * forms are available for the given mo element content, the renderer
         * should use the defaults given in parentheses in the table of
         * attributes for mo.
         */
        String[][] attr = OperatorDictionary.dictionary.get(operator + form);
        if (attr == null && form != OperatorDictionary.VALUE_INFIX) {
            attr = OperatorDictionary.dictionary.get(operator
                    + OperatorDictionary.VALUE_INFIX);
        }
        if (attr == null && form != OperatorDictionary.VALUE_POSTFIX) {
            attr = OperatorDictionary.dictionary.get(operator
                    + OperatorDictionary.VALUE_POSTFIX);
        }
        if (attr == null && form != OperatorDictionary.VALUE_PREFIX) {
            attr = OperatorDictionary.dictionary.get(operator
                    + OperatorDictionary.VALUE_PREFIX);
        }
        return attr;
    }

    private static void loadDictIfNeeded() {
        if (!OperatorDictionary.isDictionaryRead) {
            try {
                OperatorDictionary.isDictionaryRead = true;
                OperatorDictionary.instance = new OperatorDictionary();
            } catch (final DictionaryException e) {
                OperatorDictionary.LOGGER.error(e.getMessage(), e);

            }
        }
    }

    /**
     * Gets default value of the attribute. For internal use only.
     * 
     * @param attributeName
     *            the name of the attribute.
     * @return Default value of the attribute.
     */
    private static String getDefaultValue(final String attributeName) {
        for (final String[] element : OperatorDictionary.ATTRIBUTE_DEFAULT_VALUES) {
            if (attributeName.equalsIgnoreCase(element[0])) {
                return element[1];
            }
        }
        return null;
    }

    /**
     * The DictionaryReader reads dictionary XML file and initializes
     * Dictionary fields.
     */
    private static class DictionaryReader extends DefaultHandler {
        private static final String ELEMENT_ELEMENT = "element";

        /**
         * Logger for this class.
         */
        private static final Log LOGGER = LogFactory
                .getLog(OperatorDictionary.DictionaryReader.class);

        private String currentOperator;

        private int currentFormIndex;

        private int currentLength;

        private String[][] currentEntry;

        public DictionaryReader() {
            // Empty on purpose.
        }

        @Override
        public void startDocument() throws SAXException {
            OperatorDictionary.dictionary = new HashMap<String, String[][]>();
        }

        @Override
        public void endDocument() throws SAXException {
        }

        @Override
        public void startElement(final String uri, final String localName,
                final String rawName, final Attributes attlist)
                throws SAXException {

            if (rawName
                    .equals(OperatorDictionary.DictionaryReader.ELEMENT_ELEMENT)) {
                // attlist.getLength() - 1 - because we don't include form
                // attribute
                this.currentLength = attlist.getLength() - 1;
                // [currentLength -number of atrribute][2 - attribute name,
                // value of attribute]
                this.currentEntry = new String[this.currentLength][2];
                final String form = attlist.getValue(Mo.ATTR_FORM);
                if (form == null) {
                    // it is impossibhle because "form" is required attribute
                    // for the dictionary.
                    // todo: what is here?
                    OperatorDictionary.DictionaryReader.LOGGER
                            .fatal("Error in dictionary, attribute 'form' is required attribute for the dictionary");
                    this.currentFormIndex = OperatorDictionary.VALUE_INFIX;
                } else if (form.equals(OperatorDictionary.FORM_PREFIX)) {
                    this.currentFormIndex = OperatorDictionary.VALUE_PREFIX;
                } else if (form.equals(OperatorDictionary.FORM_INFIX)) {
                    this.currentFormIndex = OperatorDictionary.VALUE_INFIX;
                } else if (form.equals(OperatorDictionary.FORM_POSTFIX)) {
                    this.currentFormIndex = OperatorDictionary.VALUE_POSTFIX;
                }
                int index = 0;
                for (int i = 0; i < this.currentLength + 1; i++) {
                    final String attName = attlist.getQName(i);
                    final String attValue = attlist.getValue(i);
                    if (!attName.equals(Mo.ATTR_FORM)) {
                        this.currentEntry[index][0] = attName;
                        this.currentEntry[index][1] = attValue;
                        index++;
                    }
                }
            }
        }

        @Override
        public void endElement(final String uri, final String localName,
                final String rawName) throws SAXException {
            if (rawName
                    .equals(OperatorDictionary.DictionaryReader.ELEMENT_ELEMENT)) {
                final String key = this.currentOperator
                        + this.currentFormIndex;
                final Object existedEntry = OperatorDictionary.dictionary
                        .get(key);
                if (existedEntry == null) {
                    OperatorDictionary.dictionary.put(key, this.currentEntry);
                } else {
                    OperatorDictionary.DictionaryReader.LOGGER
                            .error("Objects " + key
                                    + " exists twice in operator dictionary!");
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

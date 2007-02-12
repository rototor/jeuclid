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

package net.sourceforge.jeuclid.element.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Read default values of operators from xml file.
 */
public class OperatorDictionary {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(OperatorDictionary.class);

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

    /**
     * Value for INFIX.
     */
    public static final int VALUE_INFIX = 101;

    /**
     * Value for POSTFIX.
     */
    public static final int VALUE_POSTFIX = 102;

    /**
     * This value is returned, when default value of operator attribute doesn't
     * exist in this dictionary so far.
     */
    public static final String VALUE_UNKNOWN = "NULL";

    /**
     * Hashtable that contains dictionary itself. Where a key = (value of
     * operator)+ (type of form) and the two-dimensional array as a value. The
     * array contains name-value pairs of attributes that have to be applied for
     * the key. For example: <element form="infix" fence="true" lspace="0em"
     * rspace="0em">+</element> key =+101(101 - because constant value for form
     * 'infix' is 101), and values: { {"lspace", "thickmathspace"} {"rspace",
     * "thickmathspace"} }
     */
    private static Map<String, String[][]> dictionary;

    /**
     * flag - is we read dictionary from modictionary.xml
     */
    private static boolean isDictionaryRead = false;

    /**
     * The instance of the Dictionary
     */
    private static OperatorDictionary instance = null;

    /**
     * Array of default values of operators attributes.
     */
    private static String[][] attributeDefValues = { { "form", "infix" },
            { "fence", "false" }, { "separator", "false" },
            { "lspace", "thickmathspace" }, { "rspace", "thickmathspace" },
            { "stretchy", "false" }, { "symmetric", "true" },
            { "maxsize", "infinity" }, { "minsize", "1" },
            { "largeop", "false" }, { "movablelimits", "false" },
            { "accent", "false" } };

    OperatorDictionary() throws DictionaryException {
        try {
            initialize();
        } catch (ParserConfigurationException e) {
            throw new DictionaryException("Cannot get SAXParser:"
                    + e.getMessage());
        } catch (SAXException e) {
            throw new DictionaryException(
                    "SAXException during parsing dictionary:" + e.getMessage());
        } catch (IOException e) {
            throw new DictionaryException(
                    "The problems with dictionary XML reading", e);
        }
    }

    /**
     * Initializes Dictionary
     */
    private void initialize() throws ParserConfigurationException,
            SAXException, IOException {
        final InputStream dictInput = OperatorDictionary.class
                .getResourceAsStream(DICTIONARY_FILE);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(new DictionaryReader());
        reader.parse(new InputSource(dictInput));
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
    public static String getDefaultAttributeValue(String operator, int form,
            String attributeName) throws UnknownAttributeException {

        if (!isDictionaryRead) {
            try {
                isDictionaryRead = true;
                instance = new OperatorDictionary();
            } catch (DictionaryException e) {
                logger.error(e.getMessage(), e);

            }
        }
        if (instance != null) {
            /*
             * If the operator does not occur in the dictionary with the
             * specified form, the renderer should use one of the forms that is
             * available there, in the order of preference: infix, postfix,
             * prefix; if no forms are available for the given mo element
             * content, the renderer should use the defaults given in
             * parentheses in the table of attributes for mo.
             */
            Object attr = OperatorDictionary.dictionary.get(operator + form);
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
            if (attr == null) {
                return VALUE_UNKNOWN;
            }
            String[][] attribute = (String[][]) attr;
            for (int i = 0; i < attribute.length; i++) {
                if (attribute[i][0].equals(attributeName)) {
                    return attribute[i][1];
                }
            }
        }
        String result = getDefaultValue(attributeName);
        if (result != null) {
            return result;
        } else {
            throw new UnknownAttributeException(attributeName);
        }
    }

    /**
     * Gets default value of the attribute. For internal use only.
     * 
     * @param attributeName
     *            the name of the attribute.
     * @return Default value of the attribute.
     */
    private static String getDefaultValue(String attributeName) {
        for (int i = 0; i < attributeDefValues.length; i++) {
            if (attributeName.equalsIgnoreCase(attributeDefValues[i][0])) {
                return attributeDefValues[i][1];
            }
        }
        return null;
    }

    /**
     * The DictionaryReader reads dictionary XML file and initializes Dictionary
     * fields.
     */
    private class DictionaryReader extends DefaultHandler {
        /**
         * Logger for this class
         */
        private final Log logger = LogFactory.getLog(DictionaryReader.class);

        private String currentOperator;

        private int currentFormIndex;

        private int currentLength;

        private String[][] currentEntry;

        public void startDocument() throws SAXException {
            dictionary = new HashMap<String, String[][]>();
        }

        public void endDocument() throws SAXException {
        }

        public void startElement(String uri, String localName, String rawName,
                Attributes attlist) throws SAXException {

            if (rawName.equals("element")) {
                // attlist.getLength() - 1 - because we don't include form
                // attribute
                currentLength = attlist.getLength() - 1;
                // [currentLength -number of atrribute][2 - attribute name,
                // value of attribute]
                currentEntry = new String[currentLength][2];
                final String form = attlist.getValue("form");
                if (form == null) {
                    // it is impossibhle because "form" is required attribute
                    // for the dictionary.
                    // todo: what is here?
                    logger
                            .fatal("Error in dictionary, attribute 'form' is required attribute for the dictionary");
                }
                if (form.equals("prefix")) {
                    currentFormIndex = OperatorDictionary.VALUE_PREFIX;
                } else if (form.equals("infix")) {
                    currentFormIndex = OperatorDictionary.VALUE_INFIX;
                } else if (form.equals("postfix")) {
                    currentFormIndex = OperatorDictionary.VALUE_POSTFIX;
                }
                int index = 0;
                for (int i = 0; i < currentLength + 1; i++) {
                    final String attName = attlist.getQName(i);
                    final String attValue = attlist.getValue(i);
                    if (!attName.equals("form")) {
                        currentEntry[index][0] = attName;
                        currentEntry[index][1] = attValue;
                        index++;
                    }
                }
            }
        }

        public void endElement(String uri, String localName, String rawName)
                throws SAXException {
            if (rawName.equals("element")) {
                String key = currentOperator + currentFormIndex;
                Object existedEntry = dictionary.get(key);
                if (existedEntry == null) {
                    dictionary.put(key, currentEntry);
                } else {
                    // TODO what todo if such object already exists?
                }
            }
            currentEntry = null;
            currentOperator = null;
        }

        public void characters(char[] data, int start, int length)
                throws SAXException {
            if (currentEntry != null) {
                char[] temp = new char[length];
                System.arraycopy(data, start, temp, 0, length);
                if (currentOperator == null) {
                    currentOperator = new String(temp);
                } else {
                    currentOperator += new String(temp);
                }
                currentOperator = currentOperator.trim();
            }
        }
    }
}

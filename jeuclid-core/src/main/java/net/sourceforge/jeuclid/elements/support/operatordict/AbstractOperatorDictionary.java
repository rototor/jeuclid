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
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 * Read default values of operators from xml file.
 * 
 * @version $Revision$
 */
public abstract class AbstractOperatorDictionary implements OperatorDictionary,
        Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final Map<OperatorAttribute, Map<String, Map<OperatorForm, String>>> dict;

    /**
     * Default constructor.
     */
    protected AbstractOperatorDictionary() {
        this.dict = new EnumMap<OperatorAttribute, Map<String, Map<OperatorForm, String>>>(
                OperatorAttribute.class);
        this.initializeFromXML(this.dict);
    }

    /**
     * Get the for singleton instance.
     * 
     * @param path
     *            path for the serialized object.
     * @return an instance of OperatorDictionary.
     */
    protected static OperatorDictionary deserialize(final String path) {
        OperatorDictionary newDict = null;
        try {
            final InputStream is = AbstractOperatorDictionary.class
                    .getResourceAsStream(path);
            final ObjectInput oi = new ObjectInputStream(is);
            newDict = (AbstractOperatorDictionary) oi.readObject();
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
        return newDict;
    }

    /**
     * Initializes Dictionary.
     * 
     * @param d
     *            the dictionary to initialize.
     */
    protected abstract void initializeFromXML(
            Map<OperatorAttribute, Map<String, Map<OperatorForm, String>>> d);

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
        final Map<OperatorForm, String> valuesPerForm = opForAttr.get(operator);
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

}

/*
 * Copyright 2008 - 2008 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.context.typewrapper;

import java.lang.reflect.InvocationTargetException;

/**
 * Converting String to Enum and vice versa is easy with help of Enum class.
 * 
 * @version $Revision$
 */
public final class EnumTypeWrapper extends AbstractSimpleTypeWrapper {
    private static final String FAILED_TO_RETRIEVE_VALUES_OF_ENUM_CLASS = "Failed to retrieve values of enum class ";

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Simple constructor.
     * 
     * @param valueType
     *            an enum class
     */
    private EnumTypeWrapper(final Class<? extends Enum<?>> valueType) {
        super(valueType);
    }

    /**
     * @return the singleton instance.
     * @param valueType
     *            an enum class
     */
    public static TypeWrapper getInstance(
            final Class<? extends Enum<?>> valueType) {
        return new EnumTypeWrapper(valueType);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Object fromString(final String value) {
        if (value == null) {
            return null;
        }
        return Enum.valueOf((Class) this.getValueType(), value);
    }

    /**
     * Retrieves values of the enum type being wrapped.
     * 
     * @return array of possible enum values
     */
    public Object[] values() {
        try {
            return (Object[]) this.getValueType().getMethod("values").invoke(
                    null);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(
                    EnumTypeWrapper.FAILED_TO_RETRIEVE_VALUES_OF_ENUM_CLASS
                            + this.getValueType(), e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(
                    EnumTypeWrapper.FAILED_TO_RETRIEVE_VALUES_OF_ENUM_CLASS
                            + this.getValueType(), e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(
                    EnumTypeWrapper.FAILED_TO_RETRIEVE_VALUES_OF_ENUM_CLASS
                            + this.getValueType(), e);
        }
    }
}

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
 * Converting String to Numbers and vice versa is also straightforward.
 * 
 * @version $Revision$
 */
public final class NumberTypeWrapper extends AbstractSimpleTypeWrapper {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Simple constructor.
     * 
     * @param valueType
     *            a subclass of Number
     */
    public NumberTypeWrapper(final Class<? extends Number> valueType) {
        super(valueType);
    }

    /**
     * @param valueType
     *            a subclass of Number
     * @return the singleton instance.
     */
    public static TypeWrapper getInstance(
            final Class<? extends Number> valueType) {
        return new NumberTypeWrapper(valueType);
    }

    /** {@inheritDoc} */
    @Override
    public Object fromString(final String value) {
        if (value == null) {
            return null;
        }
        try {
            return this.getValueType().getConstructor(
                    new Class[] { String.class }).newInstance(
                    new Object[] { value });
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException(TypeWrapper.FAILED_TO_CONVERT
                    + value + TypeWrapper.TO + this.getValueType(), e);
        } catch (final IllegalAccessException e) {
            throw new IllegalArgumentException(TypeWrapper.FAILED_TO_CONVERT
                    + value + TypeWrapper.TO + this.getValueType(), e);
        } catch (final InstantiationException e) {
            throw new IllegalArgumentException(TypeWrapper.FAILED_TO_CONVERT
                    + value + TypeWrapper.TO + this.getValueType(), e);
        } catch (final InvocationTargetException e) {
            throw new IllegalArgumentException(TypeWrapper.FAILED_TO_CONVERT
                    + value + TypeWrapper.TO + this.getValueType(), e);
        }
    }
}

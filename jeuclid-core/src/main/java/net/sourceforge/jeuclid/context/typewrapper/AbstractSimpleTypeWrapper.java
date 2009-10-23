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

/**
 * Basic (and simple) implementation of TypeWrapper. Maintains an instance of
 * type being wrapped as well as provides reasonable default implementations
 * for all the operations.
 * 
 * @version $Revision$
 */
public abstract class AbstractSimpleTypeWrapper implements TypeWrapper {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** the class instance being wrapped */
    private final Class<?> valueType;

    /**
     * @param valType
     *            a Class object
     */
    protected AbstractSimpleTypeWrapper(final Class<?> valType) {
        this.valueType = valType;
    }

    /** {@inheritDoc} */
    public Class<?> getValueType() {
        return this.valueType;
    }

    /** {@inheritDoc} */
    public boolean valid(final Object o) {
        return this.valueType.isInstance(o);
    }

    /** {@inheritDoc} */
    public Object fromString(final String value) {
        if (value == null) {
            return null;
        }
        throw new IllegalArgumentException(TypeWrapper.FAILED_TO_CONVERT
                + value + TypeWrapper.TO + this.valueType);
    }

    /** {@inheritDoc} */
    public String toString(final Object value) {
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }
}

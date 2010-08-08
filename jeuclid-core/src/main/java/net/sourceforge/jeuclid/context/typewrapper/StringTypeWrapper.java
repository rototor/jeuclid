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
 * Converting String to String is trivial...
 * 
 * @version $Revision$
 */
public final class StringTypeWrapper extends AbstractSimpleTypeWrapper {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final TypeWrapper INSTANCE = new StringTypeWrapper();

    /** Simple c'tor. */
    private StringTypeWrapper() {
        super(String.class);
    }

    /**
     * @return the singleton instance.
     */
    public static TypeWrapper getInstance() {
        return StringTypeWrapper.INSTANCE;
    }

    /** {@inheritDoc} */
    @Override
    public Object fromString(final String value) {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final Object value) {
        return (String) value;
    }
}

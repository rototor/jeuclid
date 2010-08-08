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

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;

import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;

/**
 * Color is converted to String and back by using existing APIs in
 * {@link AttributesHelper}.
 * 
 * @version $Revision$
 */
public final class ColorTypeWrapper extends AbstractSimpleTypeWrapper {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final TypeWrapper INSTANCE = new ColorTypeWrapper();

    /** Simple constructor. */
    private ColorTypeWrapper() {
        super(Color.class);
    }

    /**
     * @return the singleton instance.
     */
    public static TypeWrapper getInstance() {
        return ColorTypeWrapper.INSTANCE;
    }

    /** {@inheritDoc} */
    @Override
    public Object fromString(final String value) {
        final Color color = AttributesHelper.stringToColor(value, null);
        if (color == null) {
            throw new IllegalArgumentException('<' + value
                    + "> is not a valid color representation");
        } else {
            return color;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final Object value) {
        if (value == null) {
            return null;
        }
        String retVal = null;

        // Do some reflection magic to find standard color names
        try {
            for (final Field field : Color.class.getFields()) {
                if (Modifier.isStatic(field.getModifiers())
                        && field.get(null) == value) {
                    retVal = field.getName().toLowerCase(Locale.ENGLISH);
                    break;
                }
            }
        } catch (final IllegalAccessException e) {
            retVal = null;
        }
        if (retVal == null) {
            retVal = AttributesHelper.colorTOsRGBString((Color) value);
        }
        return retVal;
    }
}

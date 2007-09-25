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

package net.sourceforge.jeuclid;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.jeuclid.context.Display;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;

/**
 * @version $Revision$
 */
public interface LayoutContext {
    /**
     * Possible parameters for the LayoutContext.
     */
    public static enum Parameter {
        /**
         * Display style (Display).
         */
        DISPLAY(new EnumTypeWrapper(Display.class), false),
        /**
         * Font size (Float) used for the output. Defaults to 12.0pt. Please
         * Note: You may also want to set SCRIPTMINZISE.
         */
        MATHSIZE(new NumberTypeWrapper(Float.class), false),
        /**
         * Font size (Float) for smallest script used. Defaults to 8.0pt.
         */
        SCRIPTMINSIZE(new NumberTypeWrapper(Float.class), false),
        /** Script size multiplier (Float), defaults to 0.71. */
        SCRIPTSIZEMULTIPLIER(new NumberTypeWrapper(Float.class), false),
        /** Script level (Integer), defaults to 0. */
        SCRIPTLEVEL(new NumberTypeWrapper(Integer.class), false),
        /**
         * Minimum font size for which anti-alias is turned on. Defaults to
         * 10.0pt
         */
        ANTIALIAS_MINSIZE(new NumberTypeWrapper(Float.class), false),
        /**
         * Debug mode (Boolean). If true, elements will have borders drawn
         * around them.
         */
        DEBUG(new BooleanTypeWrapper(), false),
        /**
         * Anti-Alias mode (Boolean) for rendering.
         */
        ANTIALIAS(new BooleanTypeWrapper(), false),
        /**
         * Default foreground color (Color). See 3.2.2.2
         */
        MATHCOLOR(new ColorTypeWrapper(), false),
        /**
         * Default background color (Color), may be null. See 3.2.2.2
         */
        MATHBACKGROUND(new ColorTypeWrapper(), true),
        /**
         * List&lt;String&gt; of font families for sans-serif.
         * 
         * @see Parameter
         */
        FONTS_SANSSERIF(new ListTypeWrapper(), false),
        /**
         * List&lt;String&gt; of font families for serif.
         * 
         * @see Parameter
         */
        FONTS_SERIF(new ListTypeWrapper(), false),
        /**
         * List&lt;String&gt; of font families for monospaced.
         * 
         * @see Parameter
         */
        FONTS_MONOSPACED(new ListTypeWrapper(), false),
        /**
         * CList&lt;String&gt; of font families for script.
         * 
         * @see Parameter
         */
        FONTS_SCRIPT(new ListTypeWrapper(), false),
        /**
         * List&lt;String&gt; of font families for fraktur.
         * 
         * @see Parameter
         */
        FONTS_FRAKTUR(new ListTypeWrapper(), false),
        /**
         * List&lt;String&gt; of font families for double-struck.
         * 
         * @see Parameter
         */
        FONTS_DOUBLESTRUCK(new ListTypeWrapper(), false);
        
        private TypeWrapper typeWrapper;
        private boolean nullAllowed;
        
        private Parameter(final TypeWrapper aTypeWrapper, final boolean nullIsAllowed) {
            this.typeWrapper = aTypeWrapper;
            this.nullAllowed = nullIsAllowed;
        }

        /**
         * Checks if the object is of a valid type for this parameter.
         * 
         * @param o
         *            the object to check
         * @return true if the parameter can be set.
         */
        public boolean valid(final Object o) {
            return o == null && this.nullAllowed || this.typeWrapper.valid(o);
        }
        
        /**
         * Attempts to convert a parameter value expressed as string 
         * into an instance of the appropriate (for this parameter) type.
         * 
         *  @param value parameter value as string
         *  @return parameter value as an instance of the proper type
         */
        public Object fromString(final String value) {
            return this.typeWrapper.fromString(value);
        }
        
        /**
         * Attempts to convert a parameter value expressed as an object
         * of the appropriate (for this parameter) type into a string representation.
         * @param value parameter value as object
         * @return parameter value as string
         */
        public String toString(final Object value) {
            return this.typeWrapper.toString(value);
        }

        
        /**
         * Incapsulates information about a parameter's value type and 
         * how values should be converted between strings and 
         * the appropriate object instances.
         * <p>This allows elimination of an additional "evil" if-elseif...else" pattern.  
         */
        public static interface TypeWrapper {
            /**
             * @return the class instance being wrapped
             */
            Class getValueType();
            /**
             * Checks if the object is of a valid type for this type info.
             * @param o the object to check
             * @return true if the parameter can be set.
             */
            boolean valid(Object o);
            /**
             * Attempts to convert a parameter value expressed as string 
             * into an instance of the appropriate (for this parameter) type.
             * @param value parameter value as string
             * @return parameter value as an instance of the proper type
             */
            Object fromString(String value);
            /**
             * Attempts to convert a parameter value expressed as an object
             * of the appropriate (for this parameter) type into a string representation.
             * @param value parameter value as object
             * @return parameter value as string
             */
            String toString(Object value);
        }
        
        /**
         * Basic (and simple) implementation of TypeWrapper.
         * Maintains an instance of type being wrapped as well as 
         * provides reasonable default implementations for all the operations.  
         */
        public static class SimpleTypeWrapper implements TypeWrapper {
            /** the class instance being wrapped */
            private final Class valueType;
            /**
             * @param valType a Class object
             */
            protected SimpleTypeWrapper(final Class valType) {
                this.valueType = valType;
            }
            /** {@inheritDoc} */
            public Class getValueType() {
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
                throw new IllegalArgumentException("Don't know how to convert <" + value 
                        + "> to " + this.valueType);
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
        
        
        /**
         * Converting String to String is trivial... 
         */
        public static class StringTypeWrapper extends SimpleTypeWrapper {
            /** Simple c'tor. */
            public StringTypeWrapper() {
                super(String.class);
            }
            /** {@inheritDoc} */
            public Object fromString(final String value) {
                return value;
            }
            /** {@inheritDoc} */
            public String toString(final Object value) {
                return (String) value;
            }
        }

        /**
         * Converting String to Numbers and vice versa is also straightforward. 
         */
        public static class NumberTypeWrapper extends SimpleTypeWrapper {
            /** 
             * Simple constructor.
             * @param valueType a subclass of Number  
             */
            public NumberTypeWrapper(final Class<? extends Number> valueType) {
                super(valueType);
            }
            /** {@inheritDoc} */
            public Object fromString(final String value) {
                if (value == null) {
                    return null;
                }
                try {
                    return this.getValueType().getConstructor(new Class[] {String.class})
                        .newInstance(new Object[] {value});
                } catch (Exception e) {
                    throw new IllegalArgumentException("Failed to convert <" + value
                            + "> to " + this.getValueType(), e);
                }
            }
        }

        /**
         * Converting String to Boolean and vice versa is also straightforward. 
         */
        public static class BooleanTypeWrapper extends SimpleTypeWrapper {
            /** Simple constructor. */
            public BooleanTypeWrapper() {
                super(Boolean.class);
            }
            /** {@inheritDoc} */
            public Object fromString(final String value) {
                if (value == null) {
                    return null;
                } else {
                    return Boolean.valueOf(value);
                }
            }
        }

        /**
         * Color is converted to String and back by using existing APIs in 
         * {@link AttributesHelper}.
         */
        public static class ColorTypeWrapper extends SimpleTypeWrapper {
            /** Simple constructor. */
            public ColorTypeWrapper() {
                super(Color.class);
            }
            /** {@inheritDoc} */
            public Object fromString(final String value) {
                final Color color = AttributesHelper.stringToColor(value, null);
                if (color != null) {
                    return color;
                } else {
                    throw new IllegalArgumentException('<' + value
                            + "> is not a valid color representation");
                }
            }
            /** {@inheritDoc} */
            public String toString(final Object value) {
                if (value == null) {
                    return null;
                }
                try {
                    String retVal = null;
                    for (Field field : Color.class.getFields()) {
                        if (Modifier.isStatic(field.getModifiers()) 
                            && field.get(null) == value) {
                            retVal = field.getName().toLowerCase();
                            break;
                        }
                    }
                    if (retVal == null) {
                        retVal = AttributesHelper.colorTOsRGBString((Color) value);
                    }
                    return retVal;
                } catch (Exception e) {
                    throw new IllegalArgumentException("<" + value
                            + "> is not a valid color name", e);
                }
            }
        }

        /**
         * List is converted to String and back by using comma-separated representation. 
         */
        public static class ListTypeWrapper extends SimpleTypeWrapper {
            /** separator to be used when converting to string or parsing string. */
            public static final String SEPARATOR = ",";
            /** Simple constructor. */
            public ListTypeWrapper() {
                super(List.class);
            }
            /** {@inheritDoc} */
            public Object fromString(final String value) {
                if (value == null) {
                    return null;
                } else {
                    final String whitespace = "\\s*";
                    return Arrays.asList(value.split(whitespace + SEPARATOR + whitespace));
                }
            }
            /** {@inheritDoc} */
            public String toString(final Object value) {
                if (value == null) {
                    return null;
                } else {
                    final StringBuilder sb = new StringBuilder();
                    for (String s : (List<String>) value) {
                        if (sb.length() > 0) {
                            sb.append(SEPARATOR);
                        }
                        sb.append(s);
                    }
                    return sb.toString();
                }
            }
        }

        /**
         * Converting String to Enum and vice versa is easy with help of Enum class. 
         */
        public static class EnumTypeWrapper extends SimpleTypeWrapper {
            /** 
             * Simple constructor. 
             * @param valueType an enum class 
             */
            public EnumTypeWrapper(final Class<? extends Enum> valueType) {
                super(valueType);
            }
            /** {@inheritDoc} */
            public Object fromString(final String value) {
                if (value == null) {
                    return null;
                }
                final Object o = Enum.valueOf(this.getValueType(), value);
                if (o == null) {
                    throw new IllegalArgumentException("<" + value 
                            + "> is not a valid instance of enum type " + this.getValueType());
                } else {
                    return o;
                }
            }
        }

    }

    
    /**
     * Retrieve a layout parameter.
     * 
     * @param which
     *            the parameter to retrieve
     * @return current value for this parameter. Please note: Some parameters
     *         may be null.
     */
    Object getParameter(LayoutContext.Parameter which);

}

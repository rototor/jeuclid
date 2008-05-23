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

package net.sourceforge.jeuclid;

import java.awt.Color;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        DISPLAY(new EnumTypeWrapper(Display.class), false, "display",
                "display style"),

        /**
         * Font size (Float) used for the output. Defaults to 12.0pt. Please
         * Note: You may also want to set SCRIPTMINZISE.
         */
        MATHSIZE(new NumberTypeWrapper(Float.class), false, "fontSize",
                "font size used for the output (mathsize)"),

        /**
         * Font size (Float) for smallest script used. Defaults to 8.0pt.
         */
        SCRIPTMINSIZE(new NumberTypeWrapper(Float.class), false,
                "scriptMinSize", "font size to be used for smallest script"),

        /** Script size multiplier (Float), defaults to 0.71. */
        SCRIPTSIZEMULTIPLIER(new NumberTypeWrapper(Float.class), false,
                "scriptSizeMult", "script size multiplier"),

        /** Script level (Integer), defaults to 0. */
        SCRIPTLEVEL(new NumberTypeWrapper(Integer.class), false,
                "scriptLevel", "script level"),

        /**
         * Minimum font size for which anti-alias is turned on. Defaults to
         * 10.0pt
         */
        ANTIALIAS_MINSIZE(new NumberTypeWrapper(Float.class), false,
                "antiAliasMinSize",
                "minimum font size for which anti-alias is turned on"),

        /**
         * Debug mode (Boolean). If true, elements will have borders drawn
         * around them.
         */
        DEBUG(new BooleanTypeWrapper(), false, "debug",
                "debug mode - if on, elements will have borders drawn around them"),

        /**
         * Anti-Alias mode (Boolean) for rendering.
         */
        ANTIALIAS(new BooleanTypeWrapper(), false, "antiAlias",
                "anti-alias mode"),

        /**
         * Default foreground color (Color). See 3.2.2.2
         */
        MATHCOLOR(new ColorTypeWrapper(), false, "foregroundColor",
                "default foreground color (mathcolor)"),

        /**
         * Default background color (Color), may be null. See 3.2.2.2
         */
        MATHBACKGROUND(new ColorTypeWrapper(), true, "backgroundColor",
                "default background color (mathbackground)"),

        /**
         * List&lt;String&gt; of font families for sans-serif.
         * 
         * @see Parameter
         */
        FONTS_SANSSERIF(new TLIListTypeWrapper(), false, "fontsSansSerif",
                "list of font families for Sans-Serif"),

        /**
         * List&lt;String&gt; of font families for serif.
         * 
         * @see Parameter
         */
        FONTS_SERIF(new TLIListTypeWrapper(), false, "fontsSerif",
                "list of font families for Serif"),

        /**
         * List&lt;String&gt; of font families for monospaced.
         * 
         * @see Parameter
         */
        FONTS_MONOSPACED(new TLIListTypeWrapper(), false, "fontsMonospaced",
                "list of font families for Monospaced"),

        /**
         * CList&lt;String&gt; of font families for script.
         * 
         * @see Parameter
         */
        FONTS_SCRIPT(new TLIListTypeWrapper(), false, "fontsScript",

        "list of font families for Script"),
        /**
         * List&lt;String&gt; of font families for fraktur.
         * 
         * @see Parameter
         */
        FONTS_FRAKTUR(new TLIListTypeWrapper(), false, "fontsFraktur",
                "list of font families for Fraktur"),

        /**
         * List&lt;String&gt; of font families for double-struck.
         * 
         * @see Parameter
         */
        FONTS_DOUBLESTRUCK(new TLIListTypeWrapper(), false,
                "fontsDoublestruck",
                "list of font families for Double-Struck"),

        /**
         * If true, &lt;mfrac&gt; element will NEVER increase children's
         * scriptlevel (in violation of the spec); otherwise it will behave
         * with accordance to the spec.
         */
        MFRAC_KEEP_SCRIPTLEVEL(
                new BooleanTypeWrapper(),
                false,
                "mfracKeepScriptLevel",
                "if true, <mfrac> element will NEVER increase children's scriptlevel (in violation of the spec)");

        private final TypeWrapper typeWrapper;

        private final boolean nullAllowed;

        private final String optionName;

        private final String optionDesc;

        private Parameter(final TypeWrapper aTypeWrapper,
                final boolean nullIsAllowed, final String oName,
                final String oDesc) {
            this.typeWrapper = aTypeWrapper;
            this.nullAllowed = nullIsAllowed;
            this.optionName = oName;
            this.optionDesc = oDesc;
        }

        /**
         * @return TypeWrapper instance used for this parameter
         */
        public TypeWrapper getTypeWrapper() {
            return this.typeWrapper;
        }

        /**
         * @return user-friendly option name associated with this parameter
         */
        public String getOptionName() {
            return this.optionName;
        }

        /**
         * @return user-friendly option name associated with this parameter
         */
        public String getOptionDesc() {
            return this.optionDesc;
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
         * Attempts to convert a parameter value expressed as string into an
         * instance of the appropriate (for this parameter) type.
         * 
         * @param value
         *            parameter value as string
         * @return parameter value as an instance of the proper type
         */
        public Object fromString(final String value) {
            return this.typeWrapper.fromString(value);
        }

        /**
         * Attempts to convert a parameter value expressed as an object of the
         * appropriate (for this parameter) type into a string representation.
         * 
         * @param value
         *            parameter value as object
         * @return parameter value as string
         */
        public String toString(final Object value) {
            return this.typeWrapper.toString(value);
        }

        /**
         * Encapsulates information about a parameter's value type and how
         * values should be converted between strings and the appropriate
         * object instances.
         * <p>
         * This allows elimination of an additional "evil" if-elseif...else"
         * pattern.
         */
        public static interface TypeWrapper extends Serializable {
            /**
             * @return the class instance being wrapped
             */
            Class<?> getValueType();

            /**
             * Checks if the object is of a valid type for this type info.
             * 
             * @param o
             *            the object to check
             * @return true if the parameter can be set.
             */
            boolean valid(Object o);

            /**
             * Attempts to convert a parameter value expressed as string into
             * an instance of the appropriate (for this parameter) type.
             * 
             * @param value
             *            parameter value as string
             * @return parameter value as an instance of the proper type
             */
            Object fromString(String value);

            /**
             * Attempts to convert a parameter value expressed as an object of
             * the appropriate (for this parameter) type into a string
             * representation.
             * 
             * @param value
             *            parameter value as object
             * @return parameter value as string
             */
            String toString(Object value);
        }

        /**
         * Basic (and simple) implementation of TypeWrapper. Maintains an
         * instance of type being wrapped as well as provides reasonable
         * default implementations for all the operations.
         */
        public static class SimpleTypeWrapper implements
                LayoutContext.Parameter.TypeWrapper {
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
            protected SimpleTypeWrapper(final Class<?> valType) {
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
                throw new IllegalArgumentException(
                        "Don't know how to convert <" + value + "> to "
                                + this.valueType);
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
        public static class StringTypeWrapper extends
                LayoutContext.Parameter.SimpleTypeWrapper {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            /** Simple c'tor. */
            public StringTypeWrapper() {
                super(String.class);
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

        /**
         * Converting String to Numbers and vice versa is also
         * straightforward.
         */
        public static class NumberTypeWrapper extends
                LayoutContext.Parameter.SimpleTypeWrapper {
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
                } catch (final Exception e) {
                    throw new IllegalArgumentException("Failed to convert <"
                            + value + "> to " + this.getValueType(), e);
                }
            }
        }

        /**
         * Converting String to Boolean and vice versa is also
         * straightforward.
         */
        public static class BooleanTypeWrapper extends
                LayoutContext.Parameter.SimpleTypeWrapper {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            /** Simple constructor. */
            public BooleanTypeWrapper() {
                super(Boolean.class);
            }

            /** {@inheritDoc} */
            @Override
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
        public static class ColorTypeWrapper extends
                LayoutContext.Parameter.SimpleTypeWrapper {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            /** Simple constructor. */
            public ColorTypeWrapper() {
                super(Color.class);
            }

            /** {@inheritDoc} */
            @Override
            public Object fromString(final String value) {
                final Color color = AttributesHelper.stringToColor(value,
                        null);
                if (color != null) {
                    return color;
                } else {
                    throw new IllegalArgumentException('<' + value
                            + "> is not a valid color representation");
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
                            retVal = field.getName().toLowerCase(
                                    Locale.ENGLISH);
                            break;
                        }
                    }
                } catch (final IllegalAccessException e) {
                    retVal = null;
                }
                if (retVal == null) {
                    retVal = AttributesHelper
                            .colorTOsRGBString((Color) value);
                }
                return retVal;
            }
        }

        /**
         * List is converted to String and back by using comma-separated
         * representation.
         * 
         * Strings are Stored Trimmed, Lower-cased, Interned.
         */
        public static class TLIListTypeWrapper extends
                LayoutContext.Parameter.SimpleTypeWrapper {

            /**
             * separator to be used when converting to string or parsing
             * string.
             */
            public static final String SEPARATOR = ",";

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            /** Simple constructor. */
            public TLIListTypeWrapper() {
                super(List.class);
            }

            /** {@inheritDoc} */
            @Override
            public Object fromString(final String value) {
                if (value == null) {
                    return null;
                } else {
                    final String whitespace = "\\s*";
                    final String[] strList = value
                            .split(whitespace
                                    + LayoutContext.Parameter.TLIListTypeWrapper.SEPARATOR
                                    + whitespace);
                    final List<String> retVal = new ArrayList<String>(
                            strList.length);
                    for (final String str : strList) {
                        retVal.add(str.trim().toLowerCase(Locale.ENGLISH)
                                .intern());
                    }
                    return retVal;
                }
            }

            /** {@inheritDoc} */
            @Override
            public String toString(final Object value) {
                if (value == null) {
                    return null;
                } else {
                    final StringBuilder b = new StringBuilder();
                    boolean first = true;
                    for (final Object o : (List<?>) value) {
                        if (first) {
                            first = false;
                        } else {
                            b
                                    .append(LayoutContext.Parameter.TLIListTypeWrapper.SEPARATOR);
                            b.append(' ');
                        }
                        b.append(o);
                    }
                    return b.toString();
                }
            }
        }

        /**
         * Converting String to Enum and vice versa is easy with help of Enum
         * class.
         */
        public static class EnumTypeWrapper extends
                LayoutContext.Parameter.SimpleTypeWrapper {
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
            public EnumTypeWrapper(final Class<? extends Enum<?>> valueType) {
                super(valueType);
            }

            /** {@inheritDoc} */
            @SuppressWarnings("unchecked")
            @Override
            public Object fromString(final String value) {
                if (value == null) {
                    return null;
                }
                final Object o = Enum.valueOf((Class) this.getValueType(),
                        value);
                return o;
            }

            /**
             * Retrieves values of the enum type being wrapped.
             * 
             * @return array of possible enum values
             */
            public Object[] values() {
                try {
                    return (Object[]) this.getValueType().getMethod("values")
                            .invoke(null);
                } catch (final InvocationTargetException e) {
                    throw new RuntimeException(
                            LayoutContext.Parameter.EnumTypeWrapper.FAILED_TO_RETRIEVE_VALUES_OF_ENUM_CLASS
                                    + this.getValueType());
                } catch (final IllegalAccessException e) {
                    throw new RuntimeException(
                            LayoutContext.Parameter.EnumTypeWrapper.FAILED_TO_RETRIEVE_VALUES_OF_ENUM_CLASS
                                    + this.getValueType());
                } catch (final NoSuchMethodException e) {
                    throw new RuntimeException(
                            LayoutContext.Parameter.EnumTypeWrapper.FAILED_TO_RETRIEVE_VALUES_OF_ENUM_CLASS
                                    + this.getValueType());
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

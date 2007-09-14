/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.swing;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import javax.swing.ImageIcon;

/**
 * This is the class providing bean information about the swing component
 * {@link JMathComponent}. This class might be useful for applications using
 * JEuclid in their application. With help of this class they can query useful
 * information about JMathComponent when the application uses it as a bean.
 * 
 * @version $Revision$
 */
public class JMathComponentBeanInfo extends SimpleBeanInfo {
    private static final Class<JMathComponent> BEANCLASS = JMathComponent.class;

    private final Image icoColor16 = new ImageIcon(
            JMathComponentBeanInfo.BEANCLASS
                    .getResource("/icons/jeuclid_16x16.png")).getImage();

    private final Image icoColor32 = new ImageIcon(
            JMathComponentBeanInfo.BEANCLASS
                    .getResource("/icons/jeuclid_32x32.png")).getImage();

    private final Image icoBw16 = new ImageIcon(
            JMathComponentBeanInfo.BEANCLASS
                    .getResource("/icons/jeuclid_16x16_bw.png")).getImage();

    private final Image icoBw32 = new ImageIcon(
            JMathComponentBeanInfo.BEANCLASS
                    .getResource("/icons/jeuclid_32x32_bw.png")).getImage();

    /**
     * Default Constructor.
     */
    public JMathComponentBeanInfo() {
        // Empty on purpose.
    }

    /** {@inheritDoc} */
    @Override
    public Image getIcon(final int iconKind) {
        final Image retVal;
        switch (iconKind) {
        case BeanInfo.ICON_COLOR_16x16:
            retVal = this.icoColor16;
            break;
        case BeanInfo.ICON_COLOR_32x32:
            retVal = this.icoColor32;
            break;
        case BeanInfo.ICON_MONO_16x16:
            retVal = this.icoBw16;
            break;
        case BeanInfo.ICON_MONO_32x32:
            retVal = this.icoBw32;
            break;
        default:
            return this.icoColor32;
        }
        return retVal;
    }

    /** {@inheritDoc} */
    @Override
    public BeanDescriptor getBeanDescriptor() {
        final BeanDescriptor beanDescriptor = new BeanDescriptor(
                JMathComponentBeanInfo.BEANCLASS);
        beanDescriptor.setName("JEuclid");
        beanDescriptor.setDisplayName("JEuclid Bean");
        beanDescriptor
                .setShortDescription("The JEuclid project creates the possibility to "
                        + "display MathML content. "
                        + "This Bean supports rendering MathML content as a Swing component.");
        return beanDescriptor;
    }

    /** {@inheritDoc} */
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            final PropertyDescriptor propertyFontSize = this
                    .createPropertyDescriptor("fontSize", "Font size",
                            "This will modify the font size of the displayed MathML elements");

            final PropertyDescriptor propertyContent = this
                    .createPropertyDescriptor("content", "Content",
                            "The XML content for the JEuclid Bean");

            final PropertyDescriptor fgContent = this
                    .createPropertyDescriptor("foreground",
                            "Foreground Color",
                            "Foreground color if not specified within the document");

            final PropertyDescriptor bgContent = this
                    .createPropertyDescriptor("background",
                            "Background Color",
                            "Background color for this component");

            final PropertyDescriptor opaqueContent = this
                    .createPropertyDescriptor("opaque", "Opaque",
                            "If true, will always overpaint the background");

            final PropertyDescriptor fontsContent1 = this
                    .createPropertyDescriptor("fontsSerif", "Serif Fonts",
                            "Fonts to use for Serif characters (the default font)");
            final PropertyDescriptor fontsContent2 = this
                    .createPropertyDescriptor("fontsSanserif",
                            "Sans-Serif Fonts",
                            "Fonts to use for Sans-Serif characters");
            final PropertyDescriptor fontsContent3 = this
                    .createPropertyDescriptor("fontsMonospaced",
                            "Monospaced Fonts",
                            "Fonts to use for Monospaced characters");
            final PropertyDescriptor fontsContent4 = this
                    .createPropertyDescriptor("fontsScript", "Script Fonts",
                            "Fonts to use for Script characters");
            final PropertyDescriptor fontsContent5 = this
                    .createPropertyDescriptor("fontsFraktur",
                            "Fraktur Fonts",
                            "Fonts to use for Fraktur characters");
            final PropertyDescriptor fontsContent6 = this
                    .createPropertyDescriptor("fontsDoublestruck",
                            "Double-Struck Fonts",
                            "Fonts to use for Double-Struck characters");

            final PropertyDescriptor vAlign = this
                    .createPropertyDescriptor("verticalAlignment",
                            "Vertical Alignment",
                            "Vertical alignment, as defined by javax.swing.JLabel#getHorizontalAlignment");

            final PropertyDescriptor hAlign = this
                    .createPropertyDescriptor(
                            "horizontalAlignment",
                            "Horizontal Alignment",
                            "Horizontal alignment, as defined by javax.swing.JLabel#getHorizontalAlignment");
            final PropertyDescriptor border = this.createPropertyDescriptor(
                    "border", "Border", "Swing Border Property");

            return new PropertyDescriptor[] { propertyContent,
                    propertyFontSize, fgContent, bgContent, opaqueContent,
                    fontsContent1, fontsContent2, fontsContent3,
                    fontsContent4, fontsContent5, fontsContent6, vAlign,
                    hAlign, border, };
        } catch (final IntrospectionException ex) {
            // should never occur as we know which classes/methods can be used
            // in this BeanInfo class
            return super.getPropertyDescriptors();
        }
    }

    private PropertyDescriptor createPropertyDescriptor(
            final String attribute, final String displayName,
            final String description) throws IntrospectionException {
        final PropertyDescriptor propertyFontSize = new PropertyDescriptor(
                attribute, JMathComponentBeanInfo.BEANCLASS);
        propertyFontSize.setDisplayName(displayName);
        propertyFontSize.setShortDescription(description);
        return propertyFontSize;
    }
}

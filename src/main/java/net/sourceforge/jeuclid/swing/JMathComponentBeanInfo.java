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
 * @author Matthias Hanisch
 * @author Max Berger
 */
public class JMathComponentBeanInfo extends SimpleBeanInfo {
    private static final Class beanClass = JMathComponent.class;

    private final Image icoColor16 = new ImageIcon(
            JMathComponentBeanInfo.beanClass
                    .getResource("/icons/jeuclid_16x16.png")).getImage();

    private final Image icoColor32 = new ImageIcon(
            JMathComponentBeanInfo.beanClass
                    .getResource("/icons/jeuclid_32x32.png")).getImage();

    private final Image icoBw16 = new ImageIcon(
            JMathComponentBeanInfo.beanClass
                    .getResource("/icons/jeuclid_16x16_bw.png")).getImage();

    private final Image icoBw32 = new ImageIcon(
            JMathComponentBeanInfo.beanClass
                    .getResource("/icons/jeuclid_32x32_bw.png")).getImage();

    /** {@inheritDoc} */
    @Override
    public Image getIcon(final int iconKind) {
        switch (iconKind) {
        case BeanInfo.ICON_COLOR_16x16:
            return this.icoColor16;
        case BeanInfo.ICON_COLOR_32x32:
            return this.icoColor32;
        case BeanInfo.ICON_MONO_16x16:
            return this.icoBw16;
        case BeanInfo.ICON_MONO_32x32:
            return this.icoBw32;
        default:
            return this.icoColor32;
        }
    }

    /** {@inheritDoc} */
    @Override
    public BeanDescriptor getBeanDescriptor() {
        final BeanDescriptor beanDescriptor = new BeanDescriptor(
                JMathComponentBeanInfo.beanClass);
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
            final PropertyDescriptor propertyFontSize = new PropertyDescriptor(
                    "fontSize", JMathComponentBeanInfo.beanClass);
            propertyFontSize.setDisplayName("Font size");
            propertyFontSize
                    .setShortDescription("This will modify the font size of the displayed MathML elements");

            final PropertyDescriptor propertyContent = new PropertyDescriptor(
                    "content", JMathComponentBeanInfo.beanClass);
            propertyContent.setDisplayName("Content");
            propertyContent
                    .setShortDescription("The XML content for the JEuclid Bean");

            final PropertyDescriptor fgContent = new PropertyDescriptor(
                    "foreground", JMathComponentBeanInfo.beanClass);
            fgContent.setDisplayName("Foreground Color");
            fgContent
                    .setShortDescription("Foreground color if not specified within the document");

            final PropertyDescriptor bgContent = new PropertyDescriptor(
                    "background", JMathComponentBeanInfo.beanClass);
            bgContent.setDisplayName("Background Color");
            bgContent
                    .setShortDescription("Background color if not specified within the document");

            return new PropertyDescriptor[] { propertyContent,
                    propertyFontSize, fgContent, bgContent, };
        } catch (final IntrospectionException ex) {
            // should never occur as we know which classes/methods can be used
            // in this BeanInfo class
            return super.getPropertyDescriptors();
        }
    }
}

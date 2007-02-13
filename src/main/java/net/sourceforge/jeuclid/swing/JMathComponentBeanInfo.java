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

/* $Id: JMathComponent.java 6 2007-02-12 16:48:16Z maxberger $ */

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
 */
public class JMathComponentBeanInfo extends SimpleBeanInfo {
    private static final Class beanClass = JMathComponent.class;

    private final Image icoColor16 = new ImageIcon(
            JMathComponentBeanInfo.beanClass.getResource("/jeuclid.png"))
            .getImage();

    /** {@inheritDoc} */
    @Override
    public Image getIcon(final int iconKind) {
        switch (iconKind) {
        case BeanInfo.ICON_COLOR_16x16:
            return this.icoColor16;
        case BeanInfo.ICON_COLOR_32x32:
            return this.icoColor16.getScaledInstance(32, 32,
                    java.awt.Image.SCALE_FAST);
        case BeanInfo.ICON_MONO_16x16:
        case BeanInfo.ICON_MONO_32x32:
        default:
            return super.getIcon(iconKind);
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
                .setShortDescription("The JEuclid is a project, which creates the possibility to "
                        + "display MathML content. It is primary a Transformer/Serializer "
                        + "for the Cocoon project, and creates GIF images or converts the "
                        + "MathML content to SVG.");
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

            return new PropertyDescriptor[] { propertyContent,
                    propertyFontSize, };
        } catch (final IntrospectionException ex) {
            // should never occur as we know which classes/methods can be used
            // in this BeanInfo class
            return super.getPropertyDescriptors();
        }
    }
}

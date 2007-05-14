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

/* $Id: Jthis.mathComponent.java 214 2007-05-12 03:45:53 +0000 (Sat, 12 May 2007) eputrycz $ */

package net.sourceforge.jeuclid.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.Defense;
import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.ParameterKey;

import org.w3c.dom.Document;

/**
 * See http://today.java.net/pub/a/today/2007/02/22/how-to-write-custom-swing-component.html
 * for details.
 * @author putrycze
 * @version $Revision: 214 $
 * 
 */
public class MathComponentUI extends ComponentUI implements
        PropertyChangeListener {

    private JMathComponent mathComponent;

    /**
     * Reference to the MathBase class.
     */
    private MathBase base;

    /**
     * Creates a new UI.
     *
     */
    public MathComponentUI() {
        // nothing to do
    }
    
    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics g, final JComponent c) {
        System.out.println("painting:" + this.mathComponent.getContent());
        final Graphics2D g2 = (Graphics2D) g;
        final Color back = this.getRealBackgroundColor();
        final int width = this.mathComponent.getWidth();
        final int height = this.mathComponent.getHeight();
        if (back != null) {
            g.setColor(back);
            g.fillRect(0, 0, width, height);
        }
        if (this.base != null) {
            final float xo;
            if ((this.mathComponent.getHorizontalAlignment() == SwingConstants.LEADING)
                    || (this.mathComponent.getHorizontalAlignment() == SwingConstants.LEFT)) {
                xo = 0.0f;
            } else if ((this.mathComponent.getHorizontalAlignment() == SwingConstants.TRAILING)
                    || (this.mathComponent.getHorizontalAlignment() == SwingConstants.RIGHT)) {
                xo = width - this.base.getWidth(g2);
            } else {
                xo = (width - this.base.getWidth(g2)) / 2.0f;
            }
            final float yo;
            if (this.mathComponent.getVerticalAlignment() == SwingConstants.TOP) {
                yo = 0.0f;
            } else if (this.mathComponent.getVerticalAlignment() == SwingConstants.BOTTOM) {
                yo = height - this.base.getHeight(g2);
            } else {
                yo = (height - this.base.getHeight(g2)) / 2.0f;
            }
            this.base.paint((Graphics2D) g, xo, yo);
        }

    }

    private Color getRealBackgroundColor() {
        Color back = this.mathComponent.getBackground();
        if (this.mathComponent.isOpaque()) {
            if (back == null) {
                back = Color.WHITE;
            }
            // Remove Alpha
            back = new Color(back.getRGB());
        }
        return back;
    }

    /** {@inheritDoc} */
    @Override
    public void installUI(final JComponent c) {
        this.mathComponent = (JMathComponent) c;
        c.addPropertyChangeListener(this);
        this.installDefaults(this.mathComponent);
    }


    /**
     * Configures the default properties from L&F.
     * @param c the component
     */
    protected void installDefaults(final JMathComponent c) {
        //LookAndFeel.installColorsAndFont(c, "Label.background", "Label.foreground", "Label.font");
        LookAndFeel.installProperty(c, "opaque", Boolean.FALSE);
     }    
    
    /** {@inheritDoc} */
    @Override
    public void uninstallUI(final JComponent c) {
        this.mathComponent = null;
    }

    /** {@inheritDoc} */    
    public void propertyChange(final PropertyChangeEvent evt) {
        final String name = evt.getPropertyName();
        if (name.equals("document") || name.equals("property")) {
            final JMathComponent jc = (JMathComponent) evt.getSource();
            this.redo((Document) evt.getNewValue(), jc.getParameters());   
            //jc.repaint();
        }
    }

    private void redo(final Document doc, final Map<ParameterKey, String> parameters) {
        if (doc != null) {
            this.base = new MathBase(parameters);
            new DOMBuilder(doc, this.base);
        } else {
            this.base = null;
        }        
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getMinimumSize(final JComponent c) {
        final Dimension d = getPreferredSize(c);
        if (this.base == null || c.getGraphics() == null) {
            // return new Dimension(1, 1);
            return d;
        } else {
            final Graphics2D g2d = (Graphics2D) c.getGraphics();
            Defense.notNull(g2d, "g2d");
            return new Dimension((int) Math.ceil(this.base.getWidth(g2d)),
                    (int) Math.ceil(this.base.getHeight(g2d)));
        }
    }

}

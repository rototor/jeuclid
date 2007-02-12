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

/* $Id: MathRootElement.java,v 1.13.2.5 2007/01/31 22:50:26 maxberger Exp $ */

package net.sourceforge.jeuclid.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathContainer;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;

import org.w3c.dom.mathml.MathMLMathElement;

/**
 * The root element for creating a MathElement tree.
 * 
 * @author Unknown
 * @author Max Berger
 */
public class MathRootElement extends AbstractMathContainer implements
        MathMLMathElement {
    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "math";

    private boolean m_debug = false;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathRootElement(MathBase base) {
        super(base);
    }

    /**
     * Set the type of equation.
     * 
     * @param display
     *            INLINE|BLOCK
     */
    public void setDisplay(String display) {
        this.setAttribute("display", display);
    }

    /**
     * Returns the display.
     * 
     * @return Display display
     */
    public String getDisplay() {
        return this.getMathAttribute("display");
    }

    /**
     * Enables, or disables the debug display.
     * 
     * @param debug
     *            Debug display
     */
    public void setDebug(boolean debug) {
        m_debug = debug;
    }

    /**
     * Indicates, if the debug display is enabled.
     * 
     * @return True, if the debug display is enabled
     */
    public boolean isDebug() {
        return m_debug;
    }

    /**
     * Paints this component and all of its elements.
     * 
     * @param g
     *            The graphics context to use for painting
     */
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(hints);
        if (isDebug()) {
            g2.setColor(Color.blue);
            g2.drawLine(0, 0, getWidth(g) - 1, 0);
            g2.drawLine(getWidth(g) - 1, 0, getWidth(g) - 1, getHeight(g) - 1);
            g2.drawLine(0, 0, 0, getHeight(g) - 1);
            g2.drawLine(0, getHeight(g) - 1, getWidth(g) - 1, getHeight(g) - 1);

            g2.setColor(Color.cyan);
            g2.drawLine(0, getHeight(g) / 2, getWidth(g) - 1, getHeight(g) / 2);

            g2.setColor(Color.black);
        }
        paint(g2, 0, getAscentHeight(g2));
    }

    /**
     * Paints this component and all of its elements.
     * 
     * @param g
     *            The graphics context to use for painting
     * @param posX
     *            The first left position for painting
     * @param posY
     *            The position of the baseline
     */
    public void paint(Graphics g, int posX, int posY) {
        super.paint(g, posX, posY);
        int pos = posX;
        AbstractMathElement child;

        for (int i = 0; i < getMathElementCount(); i++) {
            child = getMathElement(i);
            child.paint(g, pos, posY);
            pos += child.getWidth(g);
        }
    }

    /** {@inheritDoc} */
    public int calculateWidth(Graphics g) {
        return (getMathElement(0) == null) ? 0
                : getMathElement(0).getWidth(g) + 1;
    }

    /** {@inheritDoc} */
    public int calculateAscentHeight(Graphics g) {
        return getMathElement(0) == null ? 0 : getMathElement(0)
                .getAscentHeight(g);
    }

    /** {@inheritDoc} */
    public int calculateDescentHeight(Graphics g) {
        return getMathElement(0) == null ? 0 : getMathElement(0)
                .getDescentHeight(g);
    }

    /** {@inheritDoc} */
    protected boolean isChildBlock(AbstractMathElement child) {
        return "block".equalsIgnoreCase(this.getDisplay());
    }

    /** {@inheritDoc} */
    public String getMacros() {
        return this.getMathAttribute("macros");
    }

    /** {@inheritDoc} */
    public void setMacros(String macros) {
        this.setAttribute("macros", macros);
    }

}

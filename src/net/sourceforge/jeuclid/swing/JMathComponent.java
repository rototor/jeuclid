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

/* $Id: JMathComponent.java,v 1.7.2.8 2007/02/01 15:46:29 maxberger Exp $ */

package net.sourceforge.jeuclid.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;

import javax.swing.JComponent;

import net.sourceforge.jeuclid.DOMMathBuilder;
import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.util.ParameterKey;

import org.w3c.dom.Document;

/**
 * A class for displaying MathML content in a Swing Component.
 * 
 * @author Unknown, Max Berger
 * @see net.sourceforge.jeuclid.awt.MathComponent
 */

public class JMathComponent extends JComponent {
    /**
     * Logger for this class
     */
    // Currently unused. Enable when used.
    // private static final Log logger =
    // LogFactory.getLog(JMathComponent.class);
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Reference to the MathBase class.
     */
    private MathBase base = null;

    private boolean debug = false;

    private Document document = null;

    private Map<ParameterKey, String> parameters = MathBase
            .getDefaultParameters();

    /**
     * Sets a generic parameter. Please see {@link ParameterKey} for a list of
     * possible values.
     * 
     * @param key
     *            the parameter to set.
     * @param value
     *            the value to set it to.
     */
    public final void setParameter(ParameterKey key, String value) {
        this.parameters.put(key, value);
        this.redo();
    }

    /**
     * Default constructor.
     */
    public JMathComponent() {
        this.setOpaque(false);
    }

    /**
     * @return the document
     */
    public Document getDocument() {
        return this.document;
    }

    /**
     * Gets the mininimum size of this component.
     * 
     * @return A dimension object indicating this component's minimum size.
     */
    @Override
    public Dimension getMinimumSize() {
        if (this.base == null) {
            return new Dimension(1, 1);
        } else {
            return new Dimension(this.base.getWidth(getGraphics()), this.base
                    .getHeight(getGraphics()));
        }
    }

    /**
     * Gets the preferred size of this component.
     * 
     * @return A dimension object indicating this component's preferred size.
     */
    @Override
    public Dimension getPreferredSize() {
        return this.getMinimumSize();
    }

    /**
     * Paints this component.
     * 
     * @param g
     *            The graphics context to use for painting.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.base != null) {
            this.base.paint(g);
        }
    }

    private void redo() {
        if (this.document != null) {
            this.base = new MathBase(parameters);
            new DOMMathBuilder(this.document, this.base);
            this.base.setDebug(this.debug);
        } else {
            this.base = null;
        }
        this.repaint();
        this.revalidate();
    }

    /**
     * Enables, or disables the debug mode.
     * 
     * @param debug
     *            Debug mode.
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
        this.redo();
    }

    /**
     * @param document
     *            the document to set
     */
    public void setDocument(Document document) {
        this.document = document;
        this.redo();
    }

    /**
     * @return the fontSize
     */
    public float getFontSize() {
        return Float.parseFloat(this.parameters.get(ParameterKey.FontSize));
    }

    /**
     * sets the font size used.
     * 
     * @param fontSize
     *            the font size.
     */
    public void setFontSize(float fontSize) {
        this.parameters.put(ParameterKey.FontSize, Float.toString(fontSize));
        this.redo();
    }

}

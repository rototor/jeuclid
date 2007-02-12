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

/* $Id: MathComponent.java,v 1.9.2.5 2007/01/31 22:50:23 maxberger Exp $ */

package net.sourceforge.jeuclid.awt;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;
import java.util.logging.Logger;

import net.sourceforge.jeuclid.DOMMathBuilder;
import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.util.ParameterKey;

import org.w3c.dom.Document;

/**
 * A class for displaying MathML content in a AWT Component.
 * 
 * @author Unknown, Max Berger
 * @see net.sourceforge.jeuclid.swing.JMathComponent
 */
public class MathComponent extends Component {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(MathComponent.class
            .getName());

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Reference to the MathBase class.
     */
    private MathBase base = null;

    private boolean debug = false;

    private Document document = null;

    private Map<ParameterKey, String> parameters = MathBase
            .getDefaultParameters();

    public final void setParameters(Map<ParameterKey, String> parameters) {
        this.parameters = parameters;
    }

    /**
     * Default constructor.
     */
    public MathComponent() {
        // do nothing.
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
    public Dimension getPreferredSize() {
        return this.getMinimumSize();
    }

    /**
     * Paints this component.
     * 
     * @param g
     *            The graphics context to use for painting.
     */
    public void paint(Graphics g) {
        super.paint(g);
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

}

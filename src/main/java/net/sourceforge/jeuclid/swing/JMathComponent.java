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

package net.sourceforge.jeuclid.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Map;

import javax.swing.JComponent;
import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.jeuclid.DOMMathBuilder;
import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.util.MathMLParserSupport;
import net.sourceforge.jeuclid.util.ParameterKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
    private static final Log LOGGER = LogFactory.getLog(JMathComponent.class);

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Reference to the MathBase class.
     */
    private MathBase base = null;

    private boolean debug = false;

    private Document document = null;

    private final Map<ParameterKey, String> parameters = MathBase
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
    public final void setParameter(final ParameterKey key, final String value) {
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
            final Graphics2D g2d = (Graphics2D) this.getGraphics();
            return new Dimension(this.base.getWidth(g2d), this.base
                    .getHeight(g2d));
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
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (this.base != null) {
            this.base.paint((Graphics2D) g);
        }
    }

    private void redo() {
        if (this.document != null) {
            this.base = new MathBase(this.parameters);
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
    public void setDebug(final boolean debug) {
        this.debug = debug;
        this.redo();
    }

    /**
     * @param document
     *            the document to set
     */
    public void setDocument(final Document document) {
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
    public void setFontSize(final float fontSize) {
        this.parameters.put(ParameterKey.FontSize, Float.toString(fontSize));
        this.redo();
    }

    /**
     * Set the content from a String containing the MathML content.
     * 
     * @param contentString
     *            the content to set.
     */
    public void setContent(final String contentString) {
        try {
            this.setDocument(MathMLParserSupport.parseString(contentString));
        } catch (SAXException e) {
            LOGGER.warn(e);
            this.setDocument(null);
        } catch (ParserConfigurationException e) {
            LOGGER.warn(e);
            this.setDocument(null);
        } catch (IOException e) {
            LOGGER.warn(e);
            this.setDocument(null);
        }
    }

}

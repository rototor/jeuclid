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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.swing.JComponent;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sourceforge.jeuclid.DOMMathBuilder;
import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.helpers.AttributesHelper;
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
    private MathBase base;

    private boolean debug;

    private Document document;

    private final Map<ParameterKey, String> parameters = MathBase
            .getDefaultParameters();

    /**
     * Default constructor.
     */
    public JMathComponent() {
        this.setOpaque(false);
    }

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
     * @return the document
     */
    public Document getDocument() {
        return this.document;
    }

    /**
     * Retrieve the MathBase object currently used for rendering.
     * 
     * @return the MathBase object.
     */
    public MathBase getMathBase() {
        return this.base;
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
     * @param dbg
     *            Debug mode.
     */
    public void setDebug(final boolean dbg) {
        this.debug = dbg;
        this.redo();
    }

    /**
     * @param doc
     *            the document to set
     */
    public void setDocument(final Document doc) {
        this.document = doc;
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

    /** {@inheritDoc} */
    @Override
    public void setForeground(final Color fg) {
        super.setForeground(fg);
        this.parameters.put(ParameterKey.ForegroundColor, "" + fg.getRGB());
        this.redo();
    }

    /** {@inheritDoc} */
    @Override
    public Color getForeground() {
        return AttributesHelper.stringToColor(this.parameters
                .get(ParameterKey.ForegroundColor), Color.BLACK);
    }

    /** {@inheritDoc} */
    @Override
    public void setBackground(final Color c) {
        super.setBackground(c);
        this.parameters.put(ParameterKey.BackgroundColor, "" + c.getRGB());
        this.redo();
    }

    /** {@inheritDoc} */
    @Override
    public Color getBackground() {
        return AttributesHelper.stringToColor(this.parameters
                .get(ParameterKey.BackgroundColor), null);
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
        } catch (final SAXException e) {
            JMathComponent.LOGGER.warn(e);
            this.setDocument(null);
        } catch (final ParserConfigurationException e) {
            JMathComponent.LOGGER.warn(e);
            this.setDocument(null);
        } catch (final IOException e) {
            JMathComponent.LOGGER.warn(e);
            this.setDocument(null);
        }
    }

    /**
     * Tries to return the content as a String. WARNING: This is currently
     * only partially supported! The most notable examples are all text nodes,
     * which are currently missing.
     * 
     * @return the content string.
     */
    public String getContent() {
        final StringWriter writer = new StringWriter();
        try {
            final Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();
            final DOMSource source = new DOMSource(this.getDocument());
            final StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
        } catch (final TransformerException e) {
            JMathComponent.LOGGER.warn(e);
        }
        return writer.toString();
    }

    /**
     * @return The list for sansserif.
     */
    public String getFontsSanserif() {
        return this.parameters.get(ParameterKey.FontsSanserif);
    }

    /**
     * @param newFonts
     *            new list for sansserif (comman seraparated).
     */
    public void setFontsSanserif(final String newFonts) {
        this.parameters.put(ParameterKey.FontsSanserif, newFonts);
        this.redo();
    }

    /**
     * @return The list for serif.
     */
    public String getFontsSerif() {
        return this.parameters.get(ParameterKey.FontsSerif);
    }

    /**
     * @param newFonts
     *            new list for serif (comman seraparated).
     */
    public void setFontsSerif(final String newFonts) {
        this.parameters.put(ParameterKey.FontsSerif, newFonts);
        this.redo();
    }

    /**
     * @return The list for monospaced.
     */
    public String getFontsMonospaced() {
        return this.parameters.get(ParameterKey.FontsMonospaced);
    }

    /**
     * @param newFonts
     *            new list for Monospaced (comman seraparated).
     */
    public void setFontsMonospaced(final String newFonts) {
        this.parameters.put(ParameterKey.FontsMonospaced, newFonts);
        this.redo();
    }

    /**
     * @return The list for Script.
     */
    public String getFontsScript() {
        return this.parameters.get(ParameterKey.FontsScript);
    }

    /**
     * @param newFonts
     *            new list for Script (comman seraparated).
     */
    public void setFontsScript(final String newFonts) {
        this.parameters.put(ParameterKey.FontsScript, newFonts);
        this.redo();
    }

    /**
     * @return The list for Fraktur.
     */
    public String getFontsFraktur() {
        return this.parameters.get(ParameterKey.FontsFraktur);
    }

    /**
     * @param newFonts
     *            new list for Fraktur (comman seraparated).
     */
    public void setFontsFraktur(final String newFonts) {
        this.parameters.put(ParameterKey.FontsFraktur, newFonts);
        this.redo();
    }

    /**
     * @return The list for Doublestruck.
     */
    public String getFontsDoublestruck() {
        return this.parameters.get(ParameterKey.FontsDoublestruck);
    }

    /**
     * @param newFonts
     *            new list for Doublestruck (comman seraparated).
     */
    public void setFontsDoublestruck(final String newFonts) {
        this.parameters.put(ParameterKey.FontsDoublestruck, newFonts);
        this.redo();
    }

}

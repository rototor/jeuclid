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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.Defense;
import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.ParameterKey;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Displays MathML content in a Swing Component.
 * <p>
 * There are two properties which expose the actual content, accessible though
 * {@link #getDocument()} / {@link #setDocument(Document)} for content already
 * available as a DOM model, and {@link #getContent()} and
 * {@link #setContent(String)} for content available as a String.
 * <p>
 * This class exposes most of the rendering parameters as standard bean
 * attributes. If you need to set additional attributes, you may use the
 * {@link #setParameter(ParameterKey, String)} function.
 * <p>
 * Please use only the attributes exposed through the attached
 * {@link JMathComponentBeanInfo} class. Additional attributes, such as
 * {@link #getFont()} and {@link #setFont(Font)} are provided for Swing
 * compatibility, but they may not work exactly as expected.
 * 
 * @see net.sourceforge.jeuclid.awt.MathComponent
 * @author Unknown
 * @author Max Berger
 * @version $Revision$
 */
public class JMathComponent extends JComponent {

    private static final String DEFAULT_DOCUMENT = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
            + "<!DOCTYPE math PUBLIC \"-//W3C//DTD MathML 2.0//EN\" \"http://www.w3.org/TR/MathML2/dtd/mathml2.dtd\">\n"
            + "<math mode=\"display\">\n"
            + "    <mrow>\n"
            + "        <munderover>\n"
            + "            <mo>&#x0222B;</mo>\n"
            + "            <mn>1</mn>\n"
            + "            <mi>x</mi>\n"
            + "        </munderover>\n"
            + "        <mfrac>\n"
            + "            <mi>dt</mi>\n"
            + "            <mi>t</mi>\n"
            + "        </mfrac>\n" + "    </mrow>\n" + "</math>";

    private static final String FONT_SEPARATOR = ",";

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(JMathComponent.class);

    /** */
    private static final long serialVersionUID = 1L;

    private int horizontalAlignment = SwingConstants.CENTER;

    private int verticalAlignment = SwingConstants.CENTER;

    /**
     * Reference to the MathBase class.
     */
    private MathBase base;

    private Document document;

    private final Map<ParameterKey, String> parameters = MathBase
            .getDefaultParameters();

    /**
     * Default constructor.
     */
    public JMathComponent() {
        this.setOpaque(false);
        this.fontCompat();
        this.setContent(JMathComponent.DEFAULT_DOCUMENT);
    }

    /**
     * Sets a generic JEuclid rendering parameter. Please see
     * {@link ParameterKey} for a list of possible values.
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
     * Gets the minimum size of this component.
     * 
     * @return A dimension object indicating this component's minimum size.
     */
    @Override
    public Dimension getMinimumSize() {
        if (this.base == null || this.getGraphics() == null) {
            return new Dimension(1, 1);
        } else {
            final Graphics2D g2d = (Graphics2D) this.getGraphics();
            Defense.notNull(g2d, "g2d");
            return new Dimension((int) Math.ceil(this.base.getWidth(g2d)),
                    (int) Math.ceil(this.base.getHeight(g2d)));
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
        final Graphics2D g2 = (Graphics2D) g;
        final Color back = this.getRealBackgroundColor();
        final int width = this.getWidth();
        final int height = this.getHeight();
        if (back != null) {
            g.setColor(back);
            g.fillRect(0, 0, width, height);
        }
        if (this.base != null) {
            final float xo;
            if (this.horizontalAlignment == SwingConstants.LEADING
                    || this.horizontalAlignment == SwingConstants.LEFT) {
                xo = 0.0f;
            } else if (this.horizontalAlignment == SwingConstants.TRAILING
                    || this.horizontalAlignment == SwingConstants.RIGHT) {
                xo = width - this.base.getWidth(g2);
            } else {
                xo = (width - this.base.getWidth(g2)) / 2.0f;
            }
            final float yo;
            if (this.verticalAlignment == SwingConstants.TOP) {
                yo = 0.0f;
            } else if (this.verticalAlignment == SwingConstants.BOTTOM) {
                yo = height - this.base.getHeight(g2);
            } else {
                yo = (height - this.base.getHeight(g2)) / 2.0f;
            }
            this.base.paint((Graphics2D) g, xo, yo);
        }
    }

    private Color getRealBackgroundColor() {
        Color back = this.getBackground();
        if (this.isOpaque()) {
            if (back == null) {
                back = Color.WHITE;
            }
            // Remove Alpha
            back = new Color(back.getRGB());
        }
        return back;
    }

    private void reval() {
        this.repaint();
        this.revalidate();
    }

    private void redo() {
        if (this.document != null) {
            this.base = new MathBase(this.parameters);
            new DOMBuilder(this.document, this.base);
        } else {
            this.base = null;
        }
        this.reval();
    }

    /**
     * Enables, or disables the debug mode.
     * 
     * @param dbg
     *            Debug mode.
     */
    public void setDebug(final boolean dbg) {
        this.parameters.put(ParameterKey.DebugMode, Boolean.toString(dbg));
        this.redo();
    }

    /**
     * @param doc
     *            the document to set
     */
    public void setDocument(final Document doc) {
        final Document oldValue = this.document;
        this.firePropertyChange("document", oldValue, doc);
        this.document = doc;
        if (doc == null || oldValue == null || doc != oldValue) {
            this.redo();
        }
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
        this.fontCompat();
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
        this.reval();
    }

    /** {@inheritDoc} */
    @Override
    public void setOpaque(final boolean opaque) {
        super.setOpaque(opaque);
        this.reval();
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
            throw new RuntimeException(e);
        } catch (final ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tries to return the content as a String.
     * <p>
     * This transforms the internal DOM tree back into a string, which may is
     * not guaranteed to be the literally same as the original content.
     * However, it will represent the same XML document.
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
     * Font list for Sans-Serif. Please see {@link ParameterKey#FontsSanserif}
     * for an explanation of this parameter.
     * 
     * @return The list for sansserif.
     * @see ParameterKey#FontsSanserif
     */
    public String getFontsSanserif() {
        return this.parameters.get(ParameterKey.FontsSanserif);
    }

    /**
     * Font list for Sans-Serif. Please see {@link ParameterKey#FontsSanserif}
     * for an explanation of this parameter.
     * 
     * @param newFonts
     *            new list for sansserif (comma seraparated).
     * @see ParameterKey#FontsSanserif
     */
    public void setFontsSanserif(final String newFonts) {
        this.parameters.put(ParameterKey.FontsSanserif, newFonts);
        this.redo();
    }

    /**
     * Font list for Serif (the default MathML font). Please see
     * {@link ParameterKey#FontsSerif} for an explanation of this parameter.
     * 
     * @return The list for serif.
     * @see ParameterKey#FontsSerif
     */
    public String getFontsSerif() {
        return this.parameters.get(ParameterKey.FontsSerif);
    }

    /**
     * Font list for Serif (the default MathML font). Please see
     * {@link ParameterKey#FontsSerif} for an explanation of this parameter.
     * 
     * @param newFonts
     *            new list for serif (comma seraparated).
     * @see ParameterKey#FontsSerif
     */
    public void setFontsSerif(final String newFonts) {
        this.parameters.put(ParameterKey.FontsSerif, newFonts);
        this.fontCompat();
        this.redo();
    }

    /**
     * Font list for Monospaced. Please see
     * {@link ParameterKey#FontsMonospaced} for an explanation of this
     * parameter.
     * 
     * @return The list for monospaced.
     * @see ParameterKey#FontsMonospaced
     */
    public String getFontsMonospaced() {
        return this.parameters.get(ParameterKey.FontsMonospaced);
    }

    /**
     * Font list for Monospaced. Please see
     * {@link ParameterKey#FontsMonospaced} for an explanation of this
     * parameter.
     * 
     * @param newFonts
     *            new list for Monospaced (comma seraparated).
     * @see ParameterKey#FontsMonospaced
     */
    public void setFontsMonospaced(final String newFonts) {
        this.parameters.put(ParameterKey.FontsMonospaced, newFonts);
        this.redo();
    }

    /**
     * Font list for Script. Please see {@link ParameterKey#FontsScript} for
     * an explanation of this parameter.
     * 
     * @return The list for Script.
     * @see ParameterKey#FontsScript
     */
    public String getFontsScript() {
        return this.parameters.get(ParameterKey.FontsScript);
    }

    /**
     * Font list for Script. Please see {@link ParameterKey#FontsScript} for
     * an explanation of this parameter.
     * 
     * @param newFonts
     *            new list for Script (comma seraparated).
     * @see ParameterKey#FontsScript
     */
    public void setFontsScript(final String newFonts) {
        this.parameters.put(ParameterKey.FontsScript, newFonts);
        this.redo();
    }

    /**
     * Font list for Fraktur. Please see {@link ParameterKey#FontsFraktur} for
     * an explanation of this parameter.
     * 
     * @return The list for Fraktur.
     * @see ParameterKey#FontsFraktur
     */
    public String getFontsFraktur() {
        return this.parameters.get(ParameterKey.FontsFraktur);
    }

    /**
     * Font list for Fraktur. Please see {@link ParameterKey#FontsFraktur} for
     * an explanation of this parameter.
     * 
     * @param newFonts
     *            new list for Fraktur (comma seraparated).
     * @see ParameterKey#FontsFraktur
     */
    public void setFontsFraktur(final String newFonts) {
        this.parameters.put(ParameterKey.FontsFraktur, newFonts);
        this.redo();
    }

    /**
     * Font list for Doublestruck. Please see
     * {@link ParameterKey#FontsDoublestruck} for an explanation of this
     * parameter.
     * 
     * @return The list for Doublestruck.
     * @see ParameterKey#FontsDoublestruck
     */
    public String getFontsDoublestruck() {
        return this.parameters.get(ParameterKey.FontsDoublestruck);
    }

    /**
     * Font list for Doublestruck. Please see
     * {@link ParameterKey#FontsDoublestruck} for an explanation of this
     * parameter.
     * 
     * @param newFonts
     *            new list for Doublestruck (comma seraparated).
     * @see ParameterKey#FontsDoublestruck
     */
    public void setFontsDoublestruck(final String newFonts) {
        this.parameters.put(ParameterKey.FontsDoublestruck, newFonts);
        this.redo();
    }

    /**
     * Font emulator for standard component behaviour.
     * <p>
     * Emulates the standard setFont function by setting the font Size and
     * adding the font to the front of the serif font list.
     * <p>
     * Please use the separate setters if possible.
     * 
     * @param f
     *            font to set.
     * @see #setFontSize(float)
     * @see #setFontsSerif(String)
     * @deprecated
     */
    @Deprecated
    @Override
    public void setFont(final Font f) {
        super.setFont(f);
        this.setFontSize(f.getSize2D());
        this.setFontsSerif(f.getFamily() + JMathComponent.FONT_SEPARATOR
                + this.getFontsSerif());
    }

    /**
     * Provide compatiblity for standard get/setFont() operations.
     */
    private void fontCompat() {
        final String fontName = this.getFontsSerif().split(
                JMathComponent.FONT_SEPARATOR)[0];
        final float fontSize = this.getFontSize();
        super.setFont(new Font(fontName, 0, (int) fontSize));
    }

    /**
     * Horizontal alignment, as defined by
     * {@link javax.swing.JLabel#getHorizontalAlignment()}.
     * <p>
     * Supported are: {@link SwingConstants#LEADING},
     * {@link SwingConstants#LEFT}, {@link SwingConstants#CENTER},
     * {@link SwingConstants#TRAILING}, {@link SwingConstants#RIGHT}.
     * 
     * @return the horizontalAlignment
     * @see javax.swing.JLabel#getHorizontalAlignment()
     */
    public int getHorizontalAlignment() {
        return this.horizontalAlignment;
    }

    /**
     * Horizontal alignment, as defined by
     * {@link javax.swing.JLabel#setHorizontalAlignment(int)}.
     * <p>
     * Supported are: {@link SwingConstants#LEADING},
     * {@link SwingConstants#LEFT}, {@link SwingConstants#CENTER},
     * {@link SwingConstants#TRAILING}, {@link SwingConstants#RIGHT}.
     * 
     * @param hAlignment
     *            the horizontalAlignment to set
     * @see javax.swing.JLabel#setHorizontalAlignment(int)
     */
    public void setHorizontalAlignment(final int hAlignment) {
        this.horizontalAlignment = hAlignment;
    }

    /**
     * Vertical alignment, as defined by
     * {@link javax.swing.JLabel#getVerticalAlignment()}.
     * <p>
     * Supported are: {@link SwingConstants#TOP},
     * {@link SwingConstants#CENTER}, {@link SwingConstants#BOTTOM}.
     * 
     * @return the verticalAlignment
     * @see javax.swing.JLabel#getVerticalAlignment()
     */
    public int getVerticalAlignment() {
        return this.verticalAlignment;
    }

    /**
     * Vertical alignment, as defined by
     * {@link javax.swing.JLabel#setVerticalAlignment(int)}.
     * <p>
     * Supported are: {@link SwingConstants#TOP},
     * {@link SwingConstants#CENTER}, {@link SwingConstants#BOTTOM}.
     * 
     * @param vAlignment
     *            the verticalAlignment to set
     * @see javax.swing.JLabel#setVerticalAlignment(int)
     */
    public void setVerticalAlignment(final int vAlignment) {
        this.verticalAlignment = vAlignment;
    }

}

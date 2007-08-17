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
import java.io.IOException;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.MathMLSerializer;
import net.sourceforge.jeuclid.ParameterKey;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;

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
public class JMathComponent extends JComponent implements SwingConstants {

    private static final String DEFAULT_DOCUMENT = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
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
    // currently unused.
    // private static final Log LOGGER =
    // LogFactory.getLog(JMathComponent.class);
    /** */
    private static final long serialVersionUID = 1L;

    private static final String UI_CLASS_ID = "MathComponentUI";

    private Document document;

    private int horizontalAlignment = SwingConstants.CENTER;

    private final Map<ParameterKey, String> parameters = MathBase
            .getDefaultParameters();

    private int verticalAlignment = SwingConstants.CENTER;

    /**
     * Default constructor.
     */
    public JMathComponent() {
        this.updateUI();
        this.fontCompat();
        this.setContent(JMathComponent.DEFAULT_DOCUMENT);
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
     * Tries to return the content as a String.
     * <p>
     * This transforms the internal DOM tree back into a string, which may is
     * not guaranteed to be the literally same as the original content.
     * However, it will represent the same XML document.
     * 
     * @return the content string.
     */
    public String getContent() {
        return MathMLSerializer.serializeDocument(this.getDocument(), false,
                false);
    }

    /**
     * @return the document
     */
    public Document getDocument() {
        return this.document;
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
     * @return the fontSize
     */
    public float getFontSize() {
        return Float.parseFloat(this.parameters.get(ParameterKey.FontSize));
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
     * Font list for Serif (the default MathML font). Please see
     * {@link ParameterKey#FontsSerif} for an explanation of this parameter.
     * 
     * @return The list for serif.
     * @see ParameterKey#FontsSerif
     */
    public String getFontsSerif() {
        return this.parameters.get(ParameterKey.FontsSerif);
    }

    /** {@inheritDoc} */
    @Override
    public Color getForeground() {
        return AttributesHelper.stringToColor(this.parameters
                .get(ParameterKey.ForegroundColor), Color.BLACK);
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
     * Gets the preferred size of this component.
     * 
     * @return A dimension object indicating this component's preferred size.
     */
    @Override
    public Dimension getPreferredSize() {
        return this.getMinimumSize();
    }

    /**
     * @return the UI implementation.
     */
    public MathComponentUI getUI() {
        return (MathComponentUI) this.ui;
    }

    /**
     * @return The default UI class
     */
    @Override
    public String getUIClassID() {
        return JMathComponent.UI_CLASS_ID;
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

    private void reval() {
        this.repaint();
        this.revalidate();
    }

    /** {@inheritDoc} */
    @Override
    public void setBackground(final Color c) {
        super.setBackground(c);
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
     * Enables, or disables the debug mode.
     * 
     * @param dbg
     *            Debug mode.
     */
    public void setDebug(final boolean dbg) {
        this.parameterChange(ParameterKey.DebugMode, Boolean.toString(dbg));
    }

    /**
     * @param doc
     *            the document to set
     */
    public void setDocument(final Document doc) {
        final Document oldValue = this.document;
        this.firePropertyChange("document", oldValue, doc);
        this.document = doc;
        if (doc != oldValue) {
            this.revalidate();
            this.repaint();
        }
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
     * Font list for Doublestruck. Please see
     * {@link ParameterKey#FontsDoublestruck} for an explanation of this
     * parameter.
     * 
     * @param newFonts
     *            new list for Doublestruck (comma seraparated).
     * @see ParameterKey#FontsDoublestruck
     */
    public void setFontsDoublestruck(final String newFonts) {
        this.parameterChange(ParameterKey.FontsDoublestruck, newFonts);
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
        this.parameterChange(ParameterKey.FontsFraktur, newFonts);
    }

    private void parameterChange(final ParameterKey key, final String newValue) {
        final String oldValue = this.parameters.get(key);
        this.parameters.put(key, newValue);
        this.firePropertyChange(key.name(), oldValue, this.parameters
                .get(key));
        this.revalidate();
        this.repaint();
    }

    /**
     * sets the font size used.
     * 
     * @param fontSize
     *            the font size.
     */
    public void setFontSize(final float fontSize) {
        this.parameterChange(ParameterKey.FontSize, Float.toString(fontSize));
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
        this.parameterChange(ParameterKey.FontsMonospaced, newFonts);
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
        this.parameterChange(ParameterKey.FontsSanserif, newFonts);
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
        this.parameterChange(ParameterKey.FontsScript, newFonts);
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
        this.parameterChange(ParameterKey.FontsSerif, newFonts);
        this.fontCompat();
    }

    /** {@inheritDoc} */
    @Override
    public void setForeground(final Color fg) {
        super.setForeground(fg);
        final String colorAsHex = AttributesHelper.colorTOsRGBString(fg);
        this.parameterChange(ParameterKey.ForegroundColor, colorAsHex);
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

    /** {@inheritDoc} */
    @Override
    public void setOpaque(final boolean opaque) {
        super.setOpaque(opaque);
        this.reval();
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
        this.parameterChange(key, value);
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

    /** {@inheritDoc} */
    @Override
    public void updateUI() {
        if (UIManager.get(this.getUIClassID()) != null) {
            this.setUI(UIManager.getUI(this));
        } else {
            this.setUI(new MathComponentUI());
        }
    }

    /**
     * @return the parameters
     */
    public Map<ParameterKey, String> getParameters() {
        return this.parameters;
    }

}

/*
 * Copyright 2002 - 2008 JEuclid, http://jeuclid.sf.net
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
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.MathMLSerializer;
import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.biparser.BiTree;
import net.sourceforge.jeuclid.biparser.JEuclidSAXHandler;
import net.sourceforge.jeuclid.biparser.ReparseException;
import net.sourceforge.jeuclid.biparser.SAXBiParser;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.elements.generic.DocumentElement;
import net.sourceforge.jeuclid.elements.support.ClassLoaderSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Displays MathML content in a Swing Component.
 * <p>
 * There are two properties which expose the actual content, accessible though
 * {@link #getDocument()} / {@link #setDocument(org.w3c.dom.Node)} for
 * content already available as a DOM model, and {@link #getContent()} and
 * {@link #setContent(String)} for content available as a String.
 * <p>
 * This class exposes most of the rendering parameters as standard bean
 * attributes. If you need to set additional attributes, you may use the
 * {@link #setParameter(Parameter, Object)} function.
 * <p>
 * Please use only the attributes exposed through the attached
 * {@link JMathComponentBeanInfo} class. Additional attributes, such as
 * {@link #getFont()} and {@link #setFont(Font)} are provided for Swing
 * compatibility, but they may not work exactly as expected.
 * 
 * @see net.sourceforge.jeuclid.awt.MathComponent
 * @version $Revision$
 */
public final class JMathComponent extends JComponent implements SwingConstants {

    private static final String FONT_SEPARATOR = ",";

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(JMathComponent.class);

    /** */
    private static final long serialVersionUID = 1L;

    private static String uiClassId;

    private static Class<?> mathComponentUIClass;

    private Node document;

    private BiTree biTree;

    private int horizontalAlignment = SwingConstants.CENTER;

    private final MutableLayoutContext parameters = new LayoutContextImpl(
            LayoutContextImpl.getDefaultLayoutContext());

    private int verticalAlignment = SwingConstants.CENTER;

    /**
     * Default constructor.
     */
    public JMathComponent() {
        this.updateUI();
        this.fontCompat();
        this.setDocument(new DocumentElement());
    }

    /**
     * Provide compatibility for standard get/setFont() operations.
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
     * not guaranteed to be the literally same as the original content. However,
     * it will represent the same XML document.
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
    public Node getDocument() {
        return this.document;
    }

    private static String join(final List<String> list) {
        boolean first = true;
        final StringBuilder b = new StringBuilder();
        for (final String s : list) {
            if (first) {
                first = false;
            } else {
                b.append(JMathComponent.FONT_SEPARATOR);
            }
            b.append(s);
        }
        return b.toString();
    }

    /**
     * Font list for Doublestruck. Please see
     * {@link Parameter#FontsDoublestruck} for an explanation of this parameter.
     * 
     * @return The list for Doublestruck.
     * @see Parameter#FontsDoublestruck
     */
    @SuppressWarnings("unchecked")
    public String getFontsDoublestruck() {
        return JMathComponent.join((List<String>) this.parameters
                .getParameter(Parameter.FONTS_DOUBLESTRUCK));
    }

    /**
     * Font list for Fraktur. Please see {@link Parameter#FontsFraktur} for an
     * explanation of this parameter.
     * 
     * @return The list for Fraktur.
     * @see Parameter#FontsFraktur
     */
    @SuppressWarnings("unchecked")
    public String getFontsFraktur() {
        return JMathComponent.join((List<String>) this.parameters
                .getParameter(Parameter.FONTS_FRAKTUR));
    }

    /**
     * @return the fontSize
     */
    public float getFontSize() {
        return (Float) this.parameters.getParameter(Parameter.MATHSIZE);
    }

    /**
     * Font list for Monospaced. Please see {@link Parameter#FontsMonospaced}
     * for an explanation of this parameter.
     * 
     * @return The list for monospaced.
     * @see Parameter#FontsMonospaced
     */
    @SuppressWarnings("unchecked")
    public String getFontsMonospaced() {
        return JMathComponent.join((List<String>) this.parameters
                .getParameter(Parameter.FONTS_MONOSPACED));
    }

    /**
     * Font list for Sans-Serif. Please see {@link Parameter#FontsSanserif} for
     * an explanation of this parameter.
     * 
     * @return The list for sansserif.
     * @see Parameter#FontsSanserif
     */
    @SuppressWarnings("unchecked")
    public String getFontsSanserif() {
        return JMathComponent.join((List<String>) this.parameters
                .getParameter(Parameter.FONTS_SANSSERIF));
    }

    /**
     * Font list for Script. Please see {@link Parameter#FontsScript} for an
     * explanation of this parameter.
     * 
     * @return The list for Script.
     * @see Parameter#FontsScript
     */
    @SuppressWarnings("unchecked")
    public String getFontsScript() {
        return JMathComponent.join((List<String>) this.parameters
                .getParameter(Parameter.FONTS_SCRIPT));
    }

    /**
     * Font list for Serif (the default MathML font). Please see
     * {@link Parameter#FontsSerif} for an explanation of this parameter.
     * 
     * @return The list for serif.
     * @see Parameter#FontsSerif
     */
    @SuppressWarnings("unchecked")
    public String getFontsSerif() {
        return JMathComponent.join((List<String>) this.parameters
                .getParameter(Parameter.FONTS_SERIF));
    }

    /** {@inheritDoc} */
    @Override
    public Color getForeground() {
        return (Color) this.parameters.getParameter(Parameter.MATHCOLOR);
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
        return JMathComponent.uiClassId;
    }

    /**
     * Vertical alignment, as defined by
     * {@link javax.swing.JLabel#getVerticalAlignment()}.
     * <p>
     * Supported are: {@link SwingConstants#TOP}, {@link SwingConstants#CENTER},
     * {@link SwingConstants#BOTTOM}.
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
        long start, end;

        start = System.nanoTime();
        biTree = SAXBiParser.getInstance().parse(contentString);

        // parse finished
        if (biTree != null) {
            biTree.createDOMTree();       // create DOM tree

            end = System.nanoTime();

            JMathComponent.LOGGER.info(" -- parse="+((end-start)/1000000d)+"[ms]");
      //      JMathComponent.LOGGER.info(biTree);
            
            //JMathComponent.LOGGER.info(MathMLSerializer.serializeDocument(biTree.getDocument(), true, false));
            //JMathComponent.LOGGER.info(printTreeRec(biTree.getDocument(), 0));

        //    JMathComponent.LOGGER.info(biTree.toString());

            this.setDocument(biTree.getDocument());
        } else {

            // can be if text is to long (> 4096 chars)
            //throw new RuntimeException("SAX Parse problem");
            /*
            // ----------- old ------------
            JMathComponent.LOGGER.info(" ---- setDocument with old DOM parser -----");
            try {
                Node n = MathMLParserSupport.parseString(contentString);

                JMathComponent.LOGGER.info(MathMLSerializer.serializeDocument(n, true, false));
                JMathComponent.LOGGER.info(printTreeRec(n, 0));
                
                end = System.nanoTime();

            JMathComponent.LOGGER.info(" -- parse="+((end-start)/1000000d)+"[ms]");

                this.setDocument(n);
            } catch (final SAXException e) {
                throw new RuntimeException(e);
            } catch (final ParserConfigurationException e) {
                throw new RuntimeException(e);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }*/
        }
    }

    private String printTreeRec(Node n, int level) {
        int i;
        StringBuffer sb = new StringBuffer();

        for(i=0; i<level; i++) {
            sb.append(" ");
        }

        sb.append("name='"+n.getNodeName()+"' "+n.getNamespaceURI());

        if (n.getChildNodes() != null) {
            sb.append(" childs="+n.getChildNodes().getLength());

        }

        if (n.getNodeType()==Node.TEXT_NODE) {
            sb.append(" text='"+n.getTextContent().replaceAll("\n", "#")+"'");
        }
        sb.append("\n");
        
        if (n.getChildNodes() != null) {
            NodeList nl = n.getChildNodes();
            for(i=0; i<nl.getLength(); i++) {
                sb.append(printTreeRec(nl.item(i), level+1));
            }            
        }

        return sb.toString();
    }

    /**
     * Set the content from a String containing the MathML content.
     *
     * @param contentString
     *            the content to set.
     */
    public void setContent(DocumentEvent documentEvent, String text) {
        long start, end;
        DocumentEvent.EventType type;

        if (biTree == null || biTree.getRoot() == null) {
            setContent(text);
        } else {

            start = System.nanoTime();
            type = documentEvent.getType();

            if (type == DocumentEvent.EventType.INSERT) {
                try {
                    biTree.insert(documentEvent.getOffset(), documentEvent.getLength(), text);
                } catch (ReparseException ex) {
                    setContent(text);
                }
            } else if (type == DocumentEvent.EventType.REMOVE) {
                try {
                    biTree.remove(documentEvent.getOffset(), documentEvent.getLength(), text);
                } catch (ReparseException ex) {
                    setContent(text);
                }
            } else {

                // change event ????
                throw new RuntimeException("change event.............");
            }

            end = System.nanoTime();
            JMathComponent.LOGGER.info(" -- parse="+((end-start)/1000000d)+"[ms]");
       //     JMathComponent.LOGGER.info(biTree);
      //      JMathComponent.LOGGER.info(biTree.toStringDOM());

            this.setDocument(biTree.getDocument());
        }
    }

    /**
     * Enables, or disables the debug mode.
     * 
     * @param dbg
     *            Debug mode.
     */
    public void setDebug(final boolean dbg) {
        this.setParameter(Parameter.DEBUG, dbg);
    }

    /**
     * @param doc
     *            the document to set
     */
    public void setDocument(final Node doc) {
        final Node oldValue = this.document;
        this.firePropertyChange("document", oldValue, doc);
        this.document = doc;
       // if (doc != oldValue) {
            this.revalidate();
            this.repaint();
        //}
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

    private List<String> splitFonts(final String list) {
        return Arrays.asList(list.split(JMathComponent.FONT_SEPARATOR));
    }

    /**
     * Font list for Doublestruck. Please see
     * {@link Parameter#FONTS_DOUBLESTRUCK} for an explanation of this
     * parameter.
     * 
     * @param newFonts
     *            new list for Doublestruck (comma seraparated).
     * @see Parameter#FONTS_DOUBLESTRUCK
     */
    public void setFontsDoublestruck(final String newFonts) {
        this.setParameter(Parameter.FONTS_DOUBLESTRUCK, this
                .splitFonts(newFonts));
    }

    /**
     * Font list for Fraktur. Please see {@link Parameter#FONTS_FRAKTUR} for an
     * explanation of this parameter.
     * 
     * @param newFonts
     *            new list for Fraktur (comma seraparated).
     * @see Parameter#FONTS_FRAKTUR
     */
    public void setFontsFraktur(final String newFonts) {
        this.setParameter(Parameter.FONTS_FRAKTUR, this.splitFonts(newFonts));
    }

    /**
     * Sets a generic rendering parameter.
     * 
     * @param key
     *            Key for the parameter
     * @param newValue
     *            newValue
     */
    public void setParameter(final Parameter key, final Object newValue) {
        this.setParameters(Collections.singletonMap(key, newValue));
    }

    /**
     * Sets generic rendering parameters.
     * 
     * @param newValues
     *            map of parameter keys to new values
     */
    public void setParameters(final Map<Parameter, Object> newValues) {
        for (final Map.Entry<Parameter, Object> entry : newValues.entrySet()) {
            final Parameter key = entry.getKey();
            final Object oldValue = this.parameters.getParameter(key);
            this.parameters.setParameter(key, entry.getValue());
            this.firePropertyChange(key.name(), oldValue, this.parameters
                    .getParameter(key));
        }
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
        this.setParameter(Parameter.MATHSIZE, fontSize);
    }

    /**
     * Font list for Monospaced. Please see {@link Parameter#FONTS_MONOSPACED}
     * for an explanation of this parameter.
     * 
     * @param newFonts
     *            new list for Monospaced (comma seraparated).
     * @see Parameter#FONTS_MONOSPACED
     */
    public void setFontsMonospaced(final String newFonts) {
        this
                .setParameter(Parameter.FONTS_MONOSPACED, this
                        .splitFonts(newFonts));
    }

    /**
     * Font list for Sans-Serif. Please see {@link Parameter#FONTS_SANSSERIF}
     * for an explanation of this parameter.
     * 
     * @param newFonts
     *            new list for sansserif (comma seraparated).
     * @see Parameter#FONTS_SANSSERIF
     */
    public void setFontsSanserif(final String newFonts) {
        this.setParameter(Parameter.FONTS_SANSSERIF, this.splitFonts(newFonts));
    }

    /**
     * Font list for Script. Please see {@link Parameter#FONTS_SCRIPT} for an
     * explanation of this parameter.
     * 
     * @param newFonts
     *            new list for Script (comma seraparated).
     * @see Parameter#FONTS_SCRIPT
     */
    public void setFontsScript(final String newFonts) {
        this.setParameter(Parameter.FONTS_SCRIPT, this.splitFonts(newFonts));
    }

    /**
     * Font list for Serif (the default MathML font). Please see
     * {@link Parameter#FONTS_SERIF} for an explanation of this parameter.
     * 
     * @param newFonts
     *            new list for serif (comma seraparated).
     * @see Parameter#FONTS_SERIF
     */
    public void setFontsSerif(final String newFonts) {
        this.setParameter(Parameter.FONTS_SERIF, this.splitFonts(newFonts));
        this.fontCompat();
    }

    /** {@inheritDoc} */
    @Override
    public void setForeground(final Color fg) {
        super.setForeground(fg);
        this.setParameter(Parameter.MATHCOLOR, fg);
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
     * Vertical alignment, as defined by
     * {@link javax.swing.JLabel#setVerticalAlignment(int)}.
     * <p>
     * Supported are: {@link SwingConstants#TOP}, {@link SwingConstants#CENTER},
     * {@link SwingConstants#BOTTOM}.
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
        if (UIManager.get(this.getUIClassID()) == null) {
            try {
                this
                        .setUI((MathComponentUI) JMathComponent.mathComponentUIClass
                                .newInstance());
            } catch (final InstantiationException e) {
                JMathComponent.LOGGER.warn(e.getMessage());
            } catch (final IllegalAccessException e) {
                JMathComponent.LOGGER.warn(e.getMessage());
            }
        } else {
            this.setUI(UIManager.getUI(this));
        }
    }

    /**
     * @return the parameters
     */
    public MutableLayoutContext getParameters() {
        return this.parameters;
    }

    /** {@inheritDoc} */
    @Override
    public void setSize(final int width, final int height) {
        // TODO Auto-generated method stub
        super.setSize(width, height);
    }

    static {
        Class<?> uiClass;
        String id;
        try {
            uiClass = ClassLoaderSupport.getInstance().loadClass(
                    "net.sourceforge.jeuclid.swing.MathComponentUI16");
            id = "MathComponentUI16";
        } catch (final ClassNotFoundException t) {
            uiClass = MathComponentUI.class;
            id = "MathComponentUI";
        }
        JMathComponent.uiClassId = id;
        JMathComponent.mathComponentUIClass = uiClass;
    }

}

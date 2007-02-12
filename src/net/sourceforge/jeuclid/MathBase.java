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

/* $Id: MathBase.java,v 1.10.2.7 2007/01/31 22:50:25 maxberger Exp $ */

package net.sourceforge.jeuclid;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.jeuclid.element.MathRootElement;
import net.sourceforge.jeuclid.util.ParameterKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The base for management a MathElements tree.
 * 
 * @author Max Berger
 * @author <a href="mailto:stephan@vern.chem.tu-berlin.de">Stephan Michels</a>
 * @author <a href="mailto:sielaff@vern.chem.tu-berlin.de">Marco Sielaff</a>
 */
public class MathBase {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(MathBase.class);

    /**
     * Inline mathematical expression.
     */
    public static final int INLINE = 0;

    /**
     * Non inline mathematical expression.
     */
    public static final int DISPLAY = 1;

    /**
     * Debug flag.
     */
    private boolean m_debug = false;

    /**
     * Reference to the root element of mathelements tree.
     */
    private MathRootElement rootElement;

    private Map<ParameterKey, String> _params;

    /**
     * Gets the height of the ascender.
     * 
     * @return Ascent height
     * @param g
     *            Graphics context to use.
     */
    public int getAscender(Graphics g) {
        return rootElement.getAscentHeight(g);
    }

    /**
     * @return ascent height.
     * @deprecated use {@link #getAscender(Graphics)}
     */
    public int getAscender() {
        final Graphics g = new BufferedImage(1, 1, +BufferedImage.TYPE_INT_RGB)
                .createGraphics();
        return this.getAscender(g);
    }

    /**
     * Returns the height of the descender.
     * 
     * @return Descent height
     * @param g
     *            Graphics context to use.
     */
    public int getDescender(Graphics g) {
        return rootElement.getDescentHeight(g);
    }

    /**
     * @return descent height.
     * @deprecated use {@link #getDescender(Graphics)}
     */
    public int getDescender() {
        final Graphics g = new BufferedImage(1, 1, +BufferedImage.TYPE_INT_RGB)
                .createGraphics();
        return this.getDescender(g);
    }

    /**
     * Default constructor.
     */
    public MathBase(Map<ParameterKey, String> params) {
        // do nothing
        _params = params;
    }

    /**
     * Simple constructor. Creates a MathBase.
     * 
     * @param font
     *            Font for rendering
     * @deprecated
     */
    public MathBase(Font font) {

    }

    /**
     * 
     * @return is Inline
     * @deprecated
     */
    public boolean isInline() {
        return rootElement == null ? false : "inline"
                .equalsIgnoreCase(rootElement.getDisplay());
    }

    /**
     * Set the root element of a math tree.
     * 
     * @param element
     *            Root element of a math tree
     */
    public void setRootElement(MathRootElement element) {
        if (element == null) {
            return;
        }

        rootElement = element;

        rootElement.setMathBase(this);

        rootElement.setDebug(isDebug());
    }

    /**
     * Enables, or disables the debug mode.
     * 
     * @param debug
     *            Debug mode flag.
     */
    public void setDebug(boolean debug) {
        m_debug = debug;
        if (rootElement != null) {
            rootElement.setDebug(debug);
        }
    }

    /**
     * Indicates, weither the debug mode is enabled.
     * 
     * @return True, if the debug mode is enabled
     */
    public boolean isDebug() {
        return m_debug;
    }

    /**
     * Paints this component and all of its elements.
     * 
     * @param g
     *            The graphics context to use for painting.
     */
    public void paint(Graphics g) {
        if (rootElement != null) {
            rootElement.paint(g);
        }
    }

    /**
     * Return the current width of this component.
     * 
     * @return Width
     * @param g
     *            Graphics context to use.
     */
    public int getWidth(Graphics g) {
        if (rootElement != null) {
            return rootElement.getWidth(g);
        }

        return 0;
    }

    /**
     * @return the height
     * @deprecated use {@link #getHeight(Graphics)}
     */
    public int getHeight() {
        final Graphics g = new BufferedImage(1, 1, +BufferedImage.TYPE_INT_RGB)
                .createGraphics();
        return this.getHeight(g);
    }

    /**
     * @deprecated use {@link #getWidth(Graphics)}
     * @return width
     */
    public int getWidth() {
        final Graphics g = new BufferedImage(1, 1, +BufferedImage.TYPE_INT_RGB)
                .createGraphics();
        return this.getWidth(g);

    }

    /**
     * Return the current height of this component.
     * 
     * @return Height
     * @param g
     *            Graphics context to use.
     */
    public int getHeight(Graphics g) {
        if (rootElement != null) {
            return rootElement.getHeight(g);
        }
        return 0;
    }

    /**
     * @return the fontSize
     */
    public float getFontSize() {
        return Float.parseFloat(getParams().get(ParameterKey.FontSize));
    }

    public Map<ParameterKey, String> getParams() {
        return _params;
    }

    public static Map<ParameterKey, String> getDefaultParameters() {
        Map<ParameterKey, String> params = new HashMap<ParameterKey, String>();
        params.put(ParameterKey.FontSize, "12");
        return params;
    }

}

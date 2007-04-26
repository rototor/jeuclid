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

package net.sourceforge.jeuclid;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.jeuclid.elements.generic.DocumentElement;

/**
 * The base for management a MathElements tree.
 * 
 * @author Max Berger
 * @author <a href="mailto:stephan@vern.chem.tu-berlin.de">Stephan Michels</a>
 * @author <a href="mailto:sielaff@vern.chem.tu-berlin.de">Marco Sielaff</a>
 * @version $Revision$
 */
public class MathBase {
    /**
     * Logger for this class. unused.
     */
    // private static final Log LOGGER = LogFactory.getLog(MathBase.class);
    /**
     * Inline mathematical expression.
     */
    public static final int INLINE = 0;

    /**
     * Non inline mathematical expression.
     */
    public static final int DISPLAY = 1;

    /**
     * Default font size.
     */
    public static final float DEFAULT_FONTSIZE = 12.0f;

    /** Constant for string "true". */
    public static final String TRUE = Boolean.TRUE.toString();

    /** Constant for string "false". */
    public static final String FALSE = Boolean.FALSE.toString();

    /**
     * Reference to the root element of mathelements tree.
     */
    private DocumentElement rootElement;

    private final Map<ParameterKey, String> renderParams;

    /**
     * Default constructor.
     * 
     * @param params
     *            Rendering parameters.
     * @see ParameterKey
     * @see #getDefaultParameters()
     */
    public MathBase(final Map<ParameterKey, String> params) {
        this.renderParams = MathBase.getDefaultParameters();
        if (params != null) {
            this.renderParams.putAll(params);
        }
    }

    /**
     * Gets the height of the ascender.
     * 
     * @return Ascent height
     * @param g
     *            Graphics2D context to use.
     */
    public float getAscender(final Graphics2D g) {
        return this.rootElement.getAscentHeight(g);
    }

    /**
     * Returns the height of the descender.
     * 
     * @return Descent height
     * @param g
     *            Graphics2D context to use.
     */
    public float getDescender(final Graphics2D g) {
        return this.rootElement.getDescentHeight(g);
    }

    /**
     * Set the root element of a math tree.
     * 
     * @param element
     *            Root element of a math tree
     */
    public void setRootElement(final DocumentElement element) {
        if (element == null) {
            return;
        }
        this.rootElement = element;
        this.rootElement.setMathBase(this);
    }

    /**
     * @return the Document element associated with this mathbase.
     */
    public DocumentElement getRootElement() {
        return this.rootElement;
    }

    /**
     * Enables, or disables the debug mode.
     * 
     * @param debug
     *            Debug mode flag.
     */
    public void setDebug(final boolean debug) {
        this.renderParams
                .put(ParameterKey.DebugMode, Boolean.toString(debug));
    }

    /**
     * Indicates, weither the debug mode is enabled.
     * 
     * @return True, if the debug mode is enabled
     */
    public boolean isDebug() {
        return Boolean.parseBoolean(this.renderParams
                .get(ParameterKey.DebugMode));
    }

    /**
     * Paints this component and all of its elements.
     * 
     * @param g
     *            The graphics context to use for painting.
     * @param x
     *            x-offset
     * @param y
     *            y-offset
     */
    public void paint(final Graphics2D g, final float x, final float y) {
        if (this.rootElement != null) {
            final RenderingHints hints = g.getRenderingHints();
            if (Boolean.parseBoolean(this.renderParams
                    .get(ParameterKey.AntiAlias))) {
                hints.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON));
            }
            hints.add(new RenderingHints(RenderingHints.KEY_STROKE_CONTROL,
                    RenderingHints.VALUE_STROKE_NORMALIZE));
            hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY));
            g.setRenderingHints(hints);
            this.rootElement.paint(g, x, y
                    + this.rootElement.getAscentHeight(g));
        }
    }

    /**
     * Paints the componet and all of its elements into the top-right corner.
     * 
     * @param g
     *            The graphics context to use for painting.
     * @see #paint(Graphics2D, float, float)
     */
    public void paint(final Graphics2D g) {
        this.paint(g, 0, 0);
    }

    /**
     * Return the current width of this component.
     * 
     * @return Width
     * @param g
     *            Graphics2D context to use.
     */
    public float getWidth(final Graphics2D g) {
        if (this.rootElement != null) {
            return this.rootElement.getWidth(g);
        }

        return 0f;
    }

    /**
     * Return the current height of this component.
     * 
     * @return Height
     * @param g
     *            Graphics2D context to use.
     */
    public float getHeight(final Graphics2D g) {
        if (this.rootElement != null) {
            return this.rootElement.getAscentHeight(g)
                    + this.rootElement.getDescentHeight(g);
        }
        return 0f;
    }

    /**
     * @return the fontSize
     */
    public float getFontSize() {
        return Float.parseFloat(this.getParams().get(ParameterKey.FontSize));
    }

    /**
     * Retrieves the current set of parametes.
     * 
     * @return The current set of rendering parameters.
     */
    public Map<ParameterKey, String> getParams() {
        return this.renderParams;
    }

    /**
     * Provides a reasonable set of default parameters.
     * 
     * @return a set that can be used in {@link #MathBase(Map)}
     */
    public static Map<ParameterKey, String> getDefaultParameters() {
        final Map<ParameterKey, String> params = new HashMap<ParameterKey, String>();
        params.put(ParameterKey.FontSize, Float
                .toString(MathBase.DEFAULT_FONTSIZE));
        params.put(ParameterKey.DebugMode, MathBase.FALSE);
        params.put(ParameterKey.OutFileType, "image/png");
        params.put(ParameterKey.AntiAlias, MathBase.TRUE);
        params.put(ParameterKey.ForegroundColor, "black");
        params.put(ParameterKey.BackgroundColor, "transparent");

        // CHECKSTYLE:OFF
        final String symbolCatchFonts = "OpenSymbol," + "Standard Symbols L,"
                + "Symbol," + "Webdings," + "Wingdings," + "Wingdings 2,"
                + "Wingdings 3," + "Arial Unicode MS," + "DejaVu Sans";
        params.put(ParameterKey.FontsSanserif, "Verdana," + "Helvetica,"
                + "Arial," + "Arial Unicode MS," + "Lucida Sans Unicode,"
                + "Lucida Sans," + "Lucida Grande," + "DejaVu Sans,"
                + "Bitstream Vera Sans," + "Luxi Sans," + "FreeSans,"
                + "sansserif," + symbolCatchFonts);
        params.put(ParameterKey.FontsSerif, "Constantina," + "Times,"
                + "Times New Roman," + "Lucida Bright," + "DejaVu Serif,"
                + "Bitstream Vera Serif," + "Luxi Serif," + "FreeSerif,"
                + "serif," + symbolCatchFonts);
        params.put(ParameterKey.FontsMonospaced, "Andale Mono," + "Courier,"
                + "Courier Mono," + "Courier New,"
                + "Lucida Sans Typewriter," + "DejaVu Sans Mono,"
                + "Bitstream Vera Sans Mono," + "Luxi Mono," + "FreeMono,"
                + "monospaced," + symbolCatchFonts);
        params.put(ParameterKey.FontsScript, "Savoye LET,"
                + "Brush Script MT," + "Zapfino," + "Apple Chancery,"
                + "Edwardian Script ITC," + "Lucida Handwriting,"
                + "Monotype Corsiva," + "Santa Fe LET," + symbolCatchFonts);
        params
                .put(ParameterKey.FontsFraktur, "FetteFraktur,"
                        + "Fette Fraktur," + "Euclid Fraktur,"
                        + "Lucida Blackletter," + "Blackmoor LET,"
                        + symbolCatchFonts);
        params.put(ParameterKey.FontsDoublestruck, "Caslon Open Face,"
                + "Caslon Openface," + "Cloister Open Face,"
                + "Academy Engraved LET," + "Colonna MT,"
                + "Imprint MT Shadow," + symbolCatchFonts);
        // CHECKSTYLE:ON
        return params;
    }
}

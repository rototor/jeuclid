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

import net.sourceforge.jeuclid.LayoutContext.Parameter;
import net.sourceforge.jeuclid.elements.generic.DocumentElement;

/**
 * Keeps a MathML Tree and its Rendering attributes.
 * <p>
 * This is the main class for MathML handling. It stores a MathML Tree and its
 * rendering attributes.
 * <p>
 * To obtain a renderable MathML tree, create an instance of this class, and
 * fill its tree with the help of {@link DOMBuilder}.
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

    /** Constant for zero-value (0). */
    public static final String VALUE_ZERO = "0";

    /**
     * Reference to the root element of mathelements tree.
     */
    private DocumentElement rootElement;

    private final LayoutContext layoutContext;

    /**
     * Default constructor.
     * <p>
     * Allocates a new MathBase with the given rendering parameters. You may
     * use {@link #getDefaultParameters()} to obtain a default set of
     * rendering parameters.
     * <p>
     * The root element will initially be empty. You may use
     * {@link DOMBuilder} or {@link SAXBuilder} to fill it.
     * 
     * @param context
     *            Rendering parameters.
     * @see LayoutContext
     * @see #getDefaultParameters()
     */
    public MathBase(final LayoutContext context) {
        this.layoutContext = context;
        this.rootElement = new DocumentElement(this);
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
            if ((Boolean) (this.layoutContext
                    .getParameter(Parameter.ANTIALIAS))) {
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
     * Paints the component and all of its elements into the top-right corner.
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
        final float realWidth;
        if (this.rootElement != null) {
            realWidth = this.rootElement.getWidth(g);
        } else {
            realWidth = 0f;
        }
        return Math.max(1.0f, realWidth);
    }

    /**
     * Return the current height of this component.
     * 
     * @return Height
     * @param g
     *            Graphics2D context to use.
     */
    public float getHeight(final Graphics2D g) {
        final float realHeight;
        if (this.rootElement != null) {
            realHeight = this.rootElement.getAscentHeight(g)
                    + this.rootElement.getDescentHeight(g);
        } else {
            realHeight = 0f;
        }
        return Math.max(1.0f, realHeight);
    }

    /**
     * Retrieves the current set of parameters.
     * 
     * @return The current set of rendering parameters.
     */
    public LayoutContext getLayoutContext() {
        return this.layoutContext;
    }

}

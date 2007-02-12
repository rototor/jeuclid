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

/* $Id: MathText.java,v 1.1.2.3 2007/02/10 22:57:21 maxberger Exp $ */

package net.sourceforge.jeuclid.element;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.util.StringUtil;

/**
 * This class presents text in a equation and contains some utility methods.
 * 
 */
public class MathText extends AbstractMathElement {
    /**
     * Logger for this class. Unused.
     */
    // private static final Log logger = LogFactory.getLog(MathText.class);
    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mtext";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathText(MathBase base) {
        super(base);
    }

    /**
     * Paints this element.
     * 
     * @param g
     *            The graphics context to use for painting.
     * @param posX
     *            The first left position for painting.
     * @param posY
     *            The position of the baseline.
     */
    public void paint(Graphics2D g, int posX, int posY) {
        super.paint(g, posX, posY);
        // Left here for testing purposes.
        // g.drawString(getText(),posX,posY);

        if (getText().length() > 0) {
            this.produceTextLayout(g).draw(g, posX, posY);
        }
    }

    private TextLayout produceTextLayout(Graphics2D g2d) {
        TextLayout layout = new TextLayout(StringUtil
                .convertStringtoAttributedString(getText(),
                        getMathvariantAsVariant(), getFontsizeInPoint())
                .getIterator(), g2d.getFontRenderContext());
        return layout;
    }

    /** {@inheritDoc} */
    public int calculateWidth(Graphics2D g) {
        if (getText().equals("")) {
            return 0;
        } else {
            return (int) this.produceTextLayout(g).getAdvance();
        }
    }

    /** {@inheritDoc} */
    public int calculateAscentHeight(Graphics2D g) {
        if (getText().equals("")) {
            return g.getFontMetrics().getAscent();
        } else {
            return (int) this.produceTextLayout(g).getAscent();
        }
    }

    /** {@inheritDoc} */
    public int calculateDescentHeight(Graphics2D g) {
        if (getText().equals("")) {
            return g.getFontMetrics().getDescent();
        } else {
            return (int) this.produceTextLayout(g).getDescent();
        }
    }

    /**
     * Utility method calculates descent height of the line.
     * 
     * @param font
     *            Current font.
     * @param chars
     *            Array of characrets.
     * @return Value of descent height.
     * @param g
     *            Graphics2D context to use.
     */
    public static int getCharsMaxDescentHeight(Graphics2D g, final Font font,
            final char[] chars) {
        int result = 0;

        GlyphVector gv = font.createGlyphVector((new FontRenderContext(
                new AffineTransform(), true, false)), chars);
        int descHeight = 0;
        Rectangle2D gr = null;
        for (int i = 0; i < chars.length; i++) {
            gr = gv.getGlyphMetrics(i).getBounds2D();
            descHeight = (int) Math.round(gr.getHeight() + gr.getY() + 0.5);
            if (descHeight < 0) {
                descHeight = 0;
            }
            result = Math.max(result, descHeight);
        }

        return result;
    }

    /**
     * Utility method calculates ascent height of the line.
     * 
     * @param font
     *            Current font.
     * @param chars
     *            Array of characrets.
     * @return Value of ascent height.
     * @param g
     *            Graphics2D context to use.
     */
    public static int getCharsMaxAscentHeight(Graphics2D g, Font font,
            char[] chars) {
        int result = 0;

        GlyphVector gv = font.createGlyphVector((new FontRenderContext(
                new AffineTransform(), true, false)), chars);
        int ascHeight = 0;
        Rectangle2D gr = null;
        for (int i = 0; i < chars.length; i++) {
            gr = gv.getGlyphMetrics(i).getBounds2D();
            if (gr.getY() < 0) {
                ascHeight = (int) Math.round(-gr.getY() + 0.5);
            } else {
                ascHeight = 0;
            }
            result = Math.max(result, ascHeight);
        }
        return result;
    }

    /**
     * Utility method converts String object to the array of characters.
     * 
     * @param text
     *            String with text.
     * @return Array of characters.
     */
    public static char[] getChars(String text) {
        char[] result = new char[text.length()];
        text.getChars(0, text.length(), result, 0);
        return result;
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return ELEMENT;
    }

}

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

import org.w3c.dom.mathml.MathMLPresentationToken;

/**
 * This class presents text in a equation and contains some utility methods.
 * 
 */
public class MathText extends AbstractMathElement implements
        MathMLPresentationToken {
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
    public MathText(final MathBase base) {
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
    @Override
    public void paint(final Graphics2D g, final int posX, final int posY) {
        super.paint(g, posX, posY);
        // Left here for testing purposes.
        // g.drawString(getText(),posX,posY);

        if (this.getText().length() > 0) {
            this.produceTextLayout(g).draw(g, posX, posY);
        }
    }

    private TextLayout produceTextLayout(final Graphics2D g2d) {
        final TextLayout layout = new TextLayout(StringUtil
                .convertStringtoAttributedString(this.getText(),
                        this.getMathvariantAsVariant(),
                        this.getFontsizeInPoint()).getIterator(), g2d
                .getFontRenderContext());
        return layout;
    }

    /** {@inheritDoc} */
    @Override
    public int calculateWidth(final Graphics2D g) {
        if (this.getText().equals("")) {
            return 0;
        } else {
            return (int) this.produceTextLayout(g).getAdvance();
        }
    }

    /** {@inheritDoc} */
    @Override
    public int calculateAscentHeight(final Graphics2D g) {
        if (this.getText().equals("")) {
            return g.getFontMetrics().getAscent();
        } else {
            return (int) this.produceTextLayout(g).getAscent();
        }
    }

    /** {@inheritDoc} */
    @Override
    public int calculateDescentHeight(final Graphics2D g) {
        if (this.getText().equals("")) {
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
    public static int getCharsMaxDescentHeight(final Graphics2D g,
            final Font font, final char[] chars) {
        int result = 0;

        final GlyphVector gv = font.createGlyphVector(new FontRenderContext(
                new AffineTransform(), true, false), chars);
        int descHeight = 0;
        Rectangle2D gr = null;
        for (int i = 0; i < chars.length; i++) {
            gr = gv.getGlyphMetrics(i).getBounds2D();
            descHeight = (int) Math.ceil(gr.getHeight() + gr.getY());
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
    public static int getCharsMaxAscentHeight(final Graphics2D g,
            final Font font, final char[] chars) {
        int result = 0;

        final GlyphVector gv = font.createGlyphVector(new FontRenderContext(
                new AffineTransform(), true, false), chars);
        int ascHeight = 0;
        Rectangle2D gr = null;
        for (int i = 0; i < chars.length; i++) {
            gr = gv.getGlyphMetrics(i).getBounds2D();
            if (gr.getY() < 0) {
                ascHeight = (int) Math.ceil(-gr.getY());
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
    public static char[] getChars(final String text) {
        final char[] result = new char[text.length()];
        text.getChars(0, text.length(), result, 0);
        return result;
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathText.ELEMENT;
    }

}

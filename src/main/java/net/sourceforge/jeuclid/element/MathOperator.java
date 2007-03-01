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
import java.awt.Polygon;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.generic.MathElement;
import net.sourceforge.jeuclid.element.helpers.AttributesHelper;
import net.sourceforge.jeuclid.element.helpers.OperatorDictionary;
import net.sourceforge.jeuclid.element.helpers.UnknownAttributeException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class presents a math operator, like "(" or "*".
 * 
 * @author Unkown
 * @author Max Berger
 */
public class MathOperator extends AbstractMathElement {

    /** Attribute for min size. */
    public static final String ATTR_MINSIZE = "minsize";

    /** Attribute for max size. */
    public static final String ATTR_MAXSIZE = "maxsize";

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mo";

    /**
     * Multiplier for increasing size of mo with attribute largop = true.
     */
    public static final float LARGEOP_CORRECTOR_INLINE = (float) 1.2;

    /**
     * Multiplier for increasing size of mo with attribute largop = true.
     */
    public static final float LARGEOP_CORRECTOR_BLOCK = (float) 1.5;

    /**
     * Attribute name of the stretchy property.
     */
    public static final String ATTRIBUTE_STRETCHY = "stretchy";

    /**
     * Attribute name of the largeop property.
     */
    public static final String ATTRIBUTE_LARGEOP = "largeop";

    /**
     * Attribute name of the symmetric property.
     */
    public static final String ATTRIBUTE_SYMMETRIC = "symmetric";

    /**
     * Horizontal delimiters.
     */
    public static final String HOR_DELIMITERS = "\uFE37\uFE38\u005F\u00AF\u2190\u2192\u2194\u0333\u033F";

    /**
     * Vertical delimiters.
     */
    // PG 30.03.05 public static final String VER_DELIMITERS = "[{()}]|";
    public static final String VER_DELIMITERS = "[{()}]|\u2223\u2225\u2329\u232A";

    /**
     * Left curly bracket character.
     */
    public static final char LEFT_CURLY_BRACKET_CHAR = '\u007B';

    /**
     * Right curly bracket character.
     */
    public static final char RIGHT_CURLY_BRACKET_CHAR = '\u007D';

    /**
     * Left squary bracket character.
     */
    public static final char LEFT_SQUARE_BRACKET_CHAR = '\u005B';

    /**
     * Right squary bracket character.
     */
    public static final char RIGHT_SQUARE_BRACKET_CHAR = '\u005D';

    /**
     * Left paranthesis character.
     */
    public static final char LEFT_PARENTHESIS_CHAR = '\u0028';

    /**
     * Right paranthesis character.
     */
    public static final char RIGHT_PARENTHESIS_CHAR = '\u0029';

    /**
     * Top curly bracket character.
     */
    public static final char TOP_CURLY_BRACKET_CHAR = '\uFE37';

    /**
     * Bottom curly bracket character.
     */
    public static final char BOTTOM_CURLY_BRACKET_CHAR = '\uFE38';

    /**
     * Summation character.
     */
    public static final char SUM_CHAR = '\u2211';

    /**
     * Product character.
     */
    public static final char PRODUCT_CHAR = '\u220F';

    /**
     * Arrows characters. TO DO: this row should be extended!
     */
    public static final String ARROWS = "\u2192\u21D2\u21D0\u2190\u21D4\u2194\u2191\u21D1\u2193\u21D3\u21A6";

    /**
     * Value for not initialized type of operator.
     */
    public static final int FORM_UKNOWN = -1;

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(MathOperator.class);

    private static final float FONT_SCALAR = 100.0f;

    /**
     * Variable with stretchy property value.
     */
    private boolean m_stretchy = true;

    /**
     * Type of operator (prefix, infix or postfix).
     */
    private int m_form = MathOperator.FORM_UKNOWN;

    /**
     * Value of fence property.
     */
    private boolean m_fence = false;

    /**
     * Value of separator property.
     */
    private boolean m_separator = false;

    /**
     * Detirminate how get lspace: from dictionary or attr setted by direct
     * assignment.
     */
    private boolean isLSpaceFromDict = true;

    /**
     * Value of lspace property.
     */
    private String m_lspace = "";

    /**
     * Detirminate how get rspace: from dictionary or attr setted by direct
     * assignment.
     */
    private boolean isRSpaceFromDict = true;

    /**
     * Value of rspace property.
     */
    private String m_rspace = "";

    /**
     * Value of symmetric property.
     */
    private boolean m_symmetric = true;

    /**
     * Value of largeop property.
     */
    private boolean m_largeop = false;

    /**
     * Value of moveablelimits property.
     */
    private boolean m_moveablelimits = false;

    /**
     * Value of accent property.
     */
    private boolean m_accent = false;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathOperator(final MathBase base) {
        super(base);
        this.setDefaultMathAttribute(MathOperator.ATTR_MAXSIZE, "infinity");
        this.setDefaultMathAttribute(MathOperator.ATTR_MINSIZE, "1");
    }

    /**
     * Sets form of the operator.
     * 
     * @param form
     *            Form of the operator.
     */
    public void setForm(final int form) {
        this.m_form = form;
    }

    /**
     * Gets form of the operator.
     * 
     * @return Operators form.
     */
    public int getForm() {
        return this.m_form;
    }

    /**
     * Sets value of fence property of the operator.
     * 
     * @param fence
     *            Flag of fence value.
     */
    public void setFence(final boolean fence) {
        this.m_fence = fence;
    }

    /**
     * Gets value of fence property of the operator.
     * 
     * @return Flag of fence property.
     */
    public boolean getFence() {
        return this.m_fence;
    }

    /**
     * Sets value of separator property of the operator.
     * 
     * @param separator
     *            Flag of fence value.
     */
    public void setSeparator(final boolean separator) {
        this.m_separator = separator;
    }

    /**
     * Gets value of separator property of the operator.
     * 
     * @return Flag of separator property.
     */
    public boolean getSeparator() {
        return this.m_separator;
    }

    /**
     * Sets value of lspace property of the operator.
     * 
     * @param lspace
     *            Flag of lspace value.
     */
    public void setLSpace(final String lspace) {
        this.isLSpaceFromDict = false;
        this.m_lspace = lspace;
    }

    /**
     * Gets value of lspace property of the operator.
     * 
     * @return Flag of lspace property.
     * @param g
     *            Graphics2D context to use.
     */
    public double getLSpace(final Graphics2D g) {
        if (this.isLSpaceFromDict) {
            try {
                final String s = OperatorDictionary.getDefaultAttributeValue(
                        this.getText(), this.m_form, "lspace");
                return AttributesHelper.convertSizeToPt(s, this,
                        AttributesHelper.PT);
            } catch (final UnknownAttributeException e) {
                MathOperator.LOGGER
                        .error("Unknown attribute name: lspace", e);
                return 0;
            }
        } else {
            return AttributesHelper.convertSizeToPt(this.m_lspace, this,
                    AttributesHelper.PT);
        }
    }

    /**
     * @return Multiplier for increasing size of mo whith attribute largop =
     *         true
     */
    public float getLargeOpCorrector() {
        if (this.isChildBlock(null)) {
            return MathOperator.LARGEOP_CORRECTOR_BLOCK;
        } else {
            return MathOperator.LARGEOP_CORRECTOR_INLINE;
        }
    }

    /**
     * Sets value of rspace property of the operator.
     * 
     * @param rspace
     *            Flag of rspace value.
     */
    public void setRSpace(final String rspace) {
        this.isRSpaceFromDict = false;
        this.m_rspace = rspace;
    }

    /**
     * Gets value of rspace property of the operator.
     * 
     * @return Flag of rspace property.
     * @param g
     *            Graphics2D context to use.
     */
    public double getRSpace(final Graphics2D g) {
        if (this.isRSpaceFromDict) {
            try {
                final String s = OperatorDictionary.getDefaultAttributeValue(
                        this.getText(), this.m_form, "rspace");
                return AttributesHelper.convertSizeToPt(s, this,
                        AttributesHelper.PT);
            } catch (final UnknownAttributeException e) {
                MathOperator.LOGGER
                        .error("Unknown attribute name: rspace", e);
                return 0;
            }
        } else {
            return AttributesHelper.convertSizeToPt(this.m_rspace, this,
                    AttributesHelper.PT);
        }
    }

    /**
     * Enables, or disables if the operator should fit his size to the size of
     * the container.
     * 
     * @param stretchy
     *            True, if the operater should fit this size
     */
    public void setStretchy(final boolean stretchy) {
        this.m_stretchy = stretchy;
    }

    /**
     * Returns value of stretchy property.
     * 
     * @return Stretchy flag.
     */
    public boolean getStretchy() {
        return this.m_stretchy || this.getFence();
    }

    /**
     * Sets value of symmetric property.
     * 
     * @param symmetric
     *            Symmetric flag.
     */
    public void setSymmetric(final boolean symmetric) {
        this.m_symmetric = symmetric;
        this.recalculateSize();
    }

    /**
     * Gets value of symmetric property.
     * 
     * @return Symmetric flag.
     */
    public boolean getSymmetric() {
        return this.m_symmetric;
    }

    private void setRealMathSize(final Graphics2D g) {
        final String currentSize = new Float(super.getMathsizeInPoint())
                .toString();
        String mathSize = currentSize;
        if (AttributesHelper.convertSizeToPt(currentSize, this,
                AttributesHelper.PT) > (AttributesHelper.convertSizeToPt(this
                .getMaxsize(), this, AttributesHelper.PT))) {
            mathSize = this.getMaxsize();
        }
        if (AttributesHelper.convertSizeToPt(currentSize, this,
                AttributesHelper.PT) < (AttributesHelper.convertSizeToPt(this
                .getMinsize(), this, AttributesHelper.PT))) {
            mathSize = this.getMinsize();
        }
        if (!mathSize.equals(currentSize)) {
            super.setMathsize(mathSize);
        }
        g.setFont(this.getFont());
    }

    /**
     * Sets value of maxsize property.
     * 
     * @param maxsize
     *            Maxsize value.
     */
    public void setMaxsize(final String maxsize) {
        this.setAttribute(MathOperator.ATTR_MAXSIZE, maxsize);
    }

    /**
     * Gets value of maxsize property.
     * 
     * @return Maxsize value.
     */
    public String getMaxsize() {
        return this.getMathAttribute(MathOperator.ATTR_MAXSIZE);
    }

    /**
     * Sets value of minsize property.
     * 
     * @param minsize
     *            Minsize value.
     */
    public void setMinsize(final String minsize) {
        this.setAttribute(MathOperator.ATTR_MINSIZE, minsize);
    }

    /**
     * Gets value of minsize property.
     * 
     * @return Minsize value.
     */
    public String getMinsize() {
        return this.getMathAttribute(MathOperator.ATTR_MINSIZE);
    }

    /**
     * Indicates, operater should be drawn larger than normal or not.
     * 
     * @param largeop
     *            True, if the operater should be drawn larger than normal
     */
    public void setLargeOp(final boolean largeop) {
        this.m_largeop = largeop;
        this.recalculateSize();
    }

    /**
     * Gets value of largeop property.
     * 
     * @return Largeop value.
     */
    public boolean getLargeOp() {
        return this.m_largeop;
    }

    /**
     * Sets value of moveablelimits property.
     * 
     * @param moveablelimits
     *            Moveablelimits flag.
     */
    public void setMoveableLimits(final boolean moveablelimits) {
        this.m_moveablelimits = moveablelimits;
    }

    /**
     * Gets value of moveablelimits property.
     * 
     * @return Moveablelimits flag.
     */
    public boolean getMoveableLimits() {
        return this.m_moveablelimits && (!this.isChildBlock(null));
    }

    /**
     * Sets value of accent property.
     * 
     * @param accent
     *            Accent flag.
     */
    public void setAccent(final boolean accent) {
        this.m_accent = accent;
    }

    /**
     * Gets value of accent property.
     * 
     * @return Accent flag.
     */
    public boolean getAccent() {
        return this.m_accent;
    }

    /**
     * Paints a delimiter.
     * 
     * @param g
     *            The graphics context to use for painting.
     * @param posX
     *            The first left position for painting.
     * @param posY
     *            The position of the baseline.
     * @param delimiter
     *            The char of the delimiter.
     * @param vertical
     *            True, if delimiter should be vertically stretched, false if
     *            horizontally.
     */

    private void paintDelimiter(final Graphics2D g, final int posX, int posY,
            final char delimiter, boolean vertical) {
        int height, width;

        height = this.getHeight(g);
        if (!vertical) {
            height = height - 2;
            posY = posY - 1;
        }
        width = (int) (this.getWidth(g) - this.getRSpace(g) - this
                .getLSpace(g));
        final Font font = g.getFont().deriveFont(
                this.getFontsizeInPoint() * MathOperator.FONT_SCALAR);
        final GlyphVector gv = font.createGlyphVector(g
                .getFontRenderContext(), new char[] { delimiter });
        final Rectangle2D gbounds = gv.getGlyphMetrics(0).getBounds2D();
        final double glyphWidth = gbounds.getWidth()
                / MathOperator.FONT_SCALAR;
        final double glyphHeight = gbounds.getHeight()
                / MathOperator.FONT_SCALAR;
        final double ascent = gbounds.getY() / MathOperator.FONT_SCALAR;
        final double left = gbounds.getX() / MathOperator.FONT_SCALAR;

        double yScale;
        double xScale;
        if (vertical) {
            yScale = height / glyphHeight;
            xScale = 1;
        } else {
            xScale = Math.max(1, width / glyphWidth);
            yScale = 1;
        }
        final AffineTransform transform = g.getTransform();
        final AffineTransform prevTransform = g.getTransform();
        double y = posY + this.getDescentHeight(g);
        y = y - (ascent + glyphHeight) * yScale;
        double x = posX - left * xScale;
        if (vertical) {
            x = x + (width - glyphWidth) / 2;
        }
        transform.translate(x, y);
        transform.scale(xScale, yScale);

        g.setTransform(transform);
        g.drawString(String.valueOf(delimiter), 0, 0);
        g.setTransform(prevTransform);
    }

    /**
     * Gets the used font. Everything regardes font, processed by MathBase
     * object.
     * 
     * @return Font Font object.
     */
    @Override
    public Font getFont() {
        final Font font = super.getFont();
        if (this.getLargeOp()) {
            return this.getMathvariantAsVariant().createFont(
                    this.getFontsizeInPoint() * this.getLargeOpCorrector(),
                    'A');
        } else {
            // TODO: This should use the default font size
            if (this.getStretchy() && this.isVerticalDelimeter()
                    && font.getSize() > this.mbase.getFontSize()) {
                return this.getMathvariantAsVariant().createFont(
                        this.mbase.getFontSize(), 'A');
            } else {
                return font;
            }
        }
    }

    private boolean isVerticalDelimeter() {
        return this.getText().length() == 1
                && (MathOperator.VER_DELIMITERS.indexOf(this.getText()
                        .charAt(0)) >= 0 || this.getFence());
    }

    private boolean isHorizontalDelimeter() {
        return this.getText().length() == 1
                && (MathOperator.HOR_DELIMITERS.indexOf(this.getText()
                        .charAt(0)) >= 0);
    }

    /**
     * Paints this element.
     * 
     * @param g
     *            The graphics context to use for painting
     * @param posX
     *            The first left position for painting
     * @param posY
     *            The position of the baseline
     */
    @Override
    public void paint(final Graphics2D g, int posX, final int posY) {
        super.paint(g, posX, posY);
        this.setRealMathSize(g);
        posX = (int) (posX + this.getLSpace(g));
        if (this.getText().length() == 0) {
            return;
        }
        final char firstChar = this.getText().charAt(0);

        if (this.getText().length() == 1 && this.getStretchy()) {
            if (this.isVerticalDelimeter()) {
                this.getParent().setCalculatingSize(true);
                final int ascent = this.getParent().calculateAscentHeight(g);
                final int descent = this.getParent()
                        .calculateDescentHeight(g);
                this.getParent().setCalculatingSize(false);
                final int height = ascent + descent;
                if (height <= this.getFontMetrics(g).getHeight()) {
                    g.drawString(this.getText(), posX, posY);
                } else {
                    this.paintDelimiter(g, posX, posY, firstChar, true);
                }
            } else if ((MathOperator.HOR_DELIMITERS.indexOf(firstChar) >= 0)
                    && (this.getParent() instanceof MathOver
                            || this.getParent() instanceof MathUnderOver || this
                            .getParent() instanceof MathUnder)) {
                final int width = (int) (this.getWidth(g) - this.getLSpace(g) - this
                        .getRSpace(g));
                final Polygon rp = new Polygon();
                rp.addPoint(posX + width, posY - this.getHeight(g) / 2);
                rp.addPoint(posX + width
                        - (Math.round(this.getFont().getSize() / 3)), posY);
                rp.addPoint(posX + width
                        - (Math.round(this.getFont().getSize() / 3)), posY
                        - this.getHeight(g));
                final Polygon lp = new Polygon();
                lp.addPoint(posX, posY - this.getHeight(g) / 2);
                lp.addPoint(
                        posX + (Math.round(this.getFont().getSize() / 3)),
                        posY);
                lp.addPoint(
                        posX + (Math.round(this.getFont().getSize() / 3)),
                        posY - this.getHeight(g));
                switch (MathOperator.HOR_DELIMITERS.indexOf(firstChar)) {
                case 0: // overbracket
                    this.paintDelimiter(g, posX, posY, firstChar, false);
                    break;
                case 1: // underbracket
                    this.paintDelimiter(g, posX, posY, firstChar, false);
                    break;
                case 2: // underbar
                    g.fillRect(posX, posY - (this.getFont().getSize() / 12),
                            width, (this.getFont().getSize() / 12 + 1));
                    break;
                case 3: // overbar
                    g.fillRect(posX, posY - this.getHeight(g), width, (this
                            .getFont().getSize() / 12 + 1));
                    break;
                case 4: // leftarrow
                    g.fillPolygon(lp);
                    g
                            .fillRect(
                                    posX
                                            + (Math.round(this.getFont()
                                                    .getSize() / 3)), Math
                                            .round(posY - this.getHeight(g)
                                                    * 2 / 3), width
                                            - (Math.round(this.getFont()
                                                    .getSize() / 3)), (this
                                            .getFont().getSize() / 12 + 1));
                    break;
                case 5: // rightarrow
                    g.fillPolygon(rp);
                    g.fillRect(posX, Math.round(posY - this.getHeight(g) * 2
                            / 3), width
                            - (Math.round(this.getFont().getSize() / 3)),
                            (this.getFont().getSize() / 12 + 1));
                    break;
                case 6: // leftrightarrow
                    g.fillPolygon(rp);
                    g.fillPolygon(lp);
                    g
                            .fillRect(
                                    posX
                                            + (Math.round(this.getFont()
                                                    .getSize() / 3)), Math
                                            .round(posY - this.getHeight(g)
                                                    * 2 / 3), width
                                            - (Math.round(this.getFont()
                                                    .getSize() / 3 * 2)),
                                    (this.getFont().getSize() / 12 + 1));
                    break;
                case 7: // double underbar
                    g.fillRect(posX, posY - this.getHeight(g), width, (this
                            .getFont().getSize() / 12 + 1));
                    g.fillRect(posX, posY - (this.getFont().getSize() / 12),
                            width, (this.getFont().getSize() / 12 + 1));
                    break;
                case 8: // double overbar
                    g.fillRect(posX, posY - this.getHeight(g), width, (this
                            .getFont().getSize() / 12 + 1));
                    g.fillRect(posX, posY - (this.getFont().getSize() / 12),
                            width, (this.getFont().getSize() / 12 + 1));
                    break;
                default:
                    this.paintDelimiter(g, posX, posY, firstChar, false);
                }

            } else {
                if (this.getLargeOp()) {
                    final int y = posY
                            + this.getDescentHeight(g)
                            - MathText.getCharsMaxDescentHeight(g, this
                                    .getFont(), MathText.getChars(this
                                    .getText()));
                    g.drawString(this.getText(), posX, y);
                } else {
                    g.drawString(this.getText(), posX, posY);
                }
            }
        } else {
            if (this.getLargeOp()) {
                final int y = posY
                        + this.getDescentHeight(g)
                        - MathText.getCharsMaxDescentHeight(g,
                                this.getFont(), MathText.getChars(this
                                        .getText()));
                g.drawString(this.getText(), posX, y);
            } else {
                g.drawString(this.getText(), posX, posY);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public int calculateWidth(final Graphics2D g) {
        int result = 0;
        this.setRealMathSize(g);

        if (this.getText().length() != 1) {
            result = this.getFontMetrics(g).stringWidth(this.getText());
        } else {
            if (this.isHorizontalDelimeter()
                    && (this.getParent() instanceof MathOver
                            || this.getParent() instanceof MathUnderOver || this
                            .getParent() instanceof MathUnder)) {
                if (this.getParent().isCalculatingSize()) {
                    result = -1;
                } else {
                    this.getParent().setCalculatingSize(true);
                    final int res = Math.max(this.getParent().getWidth(g),
                            this.getFontMetrics(g)
                                    .stringWidth(this.getText()));
                    this.getParent().setCalculatingSize(false);
                    result = res;
                }
            } else {
                result = this.getFontMetrics(g).stringWidth(this.getText());
            }
        }

        if (result >= 0) {
            result = result + (int) (this.getLSpace(g) + this.getRSpace(g));
        }
        return result;
    }

    /**
     * Get a glyph vector of the current font.
     * 
     * @param theChar
     *            Character, which glyph was requested.
     * @return Glyph vector object.
     */
    private GlyphVector getGlyphVector(final char theChar) {
        return this.getFont().createGlyphVector(
                new FontRenderContext(new AffineTransform(), true, false),
                new char[] { theChar });
    }

    /** {@inheritDoc} */
    @Override
    public int calculateAscentHeight(final Graphics2D g) {
        this.setRealMathSize(g);
        int result = 0;
        if (this.getText().length() != 1) {
            result = MathText.getCharsMaxAscentHeight(g, this.getFont(),
                    MathText.getChars(this.getText()));
        } else {
            final char firstChar = this.getText().charAt(0);

            if (this.isVerticalDelimeter()) {
                if (!this.getStretchy()) {
                    result = MathText.getCharsMaxAscentHeight(g, this
                            .getFont(), new char[] { firstChar });
                } else {
                    if (this.getParent().isCalculatingSize()) {
                        result = 0;
                    } else {
                        this.getParent().setCalculatingSize(true);
                        int dh = this.getParent().getDescentHeight(g);
                        int ah = this.getParent().getAscentHeight(g);
                        this.getParent().setCalculatingSize(false);
                        if (this.getSymmetric()) {
                            dh = dh + this.getMiddleShift(g);
                            ah = ah - this.getMiddleShift(g);
                            dh = Math.max(dh, ah);
                            ah = dh + this.getMiddleShift(g);
                            dh = dh - this.getMiddleShift(g);
                        }
                        int size = (int) AttributesHelper.convertSizeToPt(
                                this.getMaxsize(), this, AttributesHelper.PT);
                        if (ah + dh > size) {
                            ah = (ah - this.getMiddleShift(g)) * size
                                    / (ah + dh) + this.getMiddleShift(g);
                        } else {
                            size = (int) AttributesHelper.convertSizeToPt(
                                    this.getMinsize(), this,
                                    AttributesHelper.PT);
                            if (ah + dh < size) {
                                ah = (ah - this.getMiddleShift(g)) * size
                                        / (ah + dh) + this.getMiddleShift(g);
                            }
                        }
                        result = ah;
                    }
                }
            } else if (MathOperator.HOR_DELIMITERS.indexOf(firstChar) >= 0) {
                final double lh = Math.max(this.getFont().getSize() / 12, 1);
                final Rectangle2D rect = this.getGlyphVector(firstChar)
                        .getGlyphMetrics(0).getBounds2D();
                switch (MathOperator.HOR_DELIMITERS.indexOf(firstChar)) {
                case 0: // overbracket
                    result = (int) rect.getHeight();
                    break;
                case 1: // underbracket
                    result = (int) rect.getHeight();
                    break;
                case 2: // underbar
                    result = (int) lh;
                    break;
                case 3: // overbar
                    result = (int) lh;
                    break;
                case 4: // leftarrow
                    result = (int) rect.getHeight() + 2;
                    break;
                case 5: // rightarrow
                    result = (int) rect.getHeight() + 2;
                    break;
                case 6: // leftrightarrow
                    result = (int) rect.getHeight() + 2;
                    break;
                case 7: // double underbar
                    result = (int) (lh * 3);
                    break;
                case 8: // double overbar
                    result = (int) (lh * 3);
                    break;
                default:
                    result = (int) rect.getHeight();
                }
            } else {
                result = MathText.getCharsMaxAscentHeight(g, this.getFont(),
                        MathText.getChars(this.getText()));
            }
        }

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public int calculateDescentHeight(final Graphics2D g) {
        this.setRealMathSize(g);
        int result = 0;
        if (this.getText().length() != 1) {
            result = MathText.getCharsMaxDescentHeight(g, this.getFont(),
                    MathText.getChars(this.getText()));
        } else {
            final char firstChar = this.getText().charAt(0);
            if (MathOperator.VER_DELIMITERS.indexOf(this.getText().charAt(0)) >= 0) {
                if (!this.getStretchy()) {
                    result = MathText.getCharsMaxDescentHeight(g, this
                            .getFont(), new char[] { firstChar });
                } else {
                    if (this.getParent().isCalculatingSize()) {
                        result = 0;
                    } else {
                        this.getParent().setCalculatingSize(true);
                        int dh = this.getParent().getDescentHeight(g);
                        int ah = this.getParent().getAscentHeight(g);
                        this.getParent().setCalculatingSize(false);
                        if (this.getSymmetric()) {
                            dh = dh + this.getMiddleShift(g);
                            ah = ah - this.getMiddleShift(g);
                            dh = Math.max(dh, ah);
                            ah = dh + this.getMiddleShift(g);
                            dh = dh - this.getMiddleShift(g);
                        }
                        int size = (int) AttributesHelper.convertSizeToPt(
                                this.getMaxsize(), this, AttributesHelper.PT);
                        if (ah + dh > size) {
                            dh = (dh + this.getMiddleShift(g)) * size
                                    / (ah + dh) - this.getMiddleShift(g);
                        } else {
                            size = (int) AttributesHelper.convertSizeToPt(
                                    this.getMinsize(), this,
                                    AttributesHelper.PT);
                            if (ah + dh < size) {
                                dh = (dh + this.getMiddleShift(g)) * size
                                        / (ah + dh) - this.getMiddleShift(g);
                            }
                        }
                        result = dh;
                    }
                }
            } else if (MathOperator.HOR_DELIMITERS.indexOf(firstChar) >= 0) {
                result = 0;
            } else {
                result = MathText.getCharsMaxDescentHeight(g, this.getFont(),
                        MathText.getChars(this.getText()));
            }
        }
        return result;
    }

    /**
     * Method determines, weither this operator should be stretchable by its
     * context.
     * 
     * @param parent
     *            Parent element of the operator to be analyzed.
     * @return True, if operator should be stretched.
     */
    public static boolean isStretchyByContext(final MathElement parent) {
        boolean result = false;

        if (parent instanceof MathTableData || parent instanceof MathRow) {
            result = true;
        }

        return result;
    }

    /**
     * This method get boolean value of the attribute from dictionary.
     * 
     * @param attrname
     *            Name of the attribute.
     */
    private boolean getBooleanFromDictionary(final String attrname,
            final boolean defvalue) {
        boolean def = false;
        try {
            final String attr = OperatorDictionary.getDefaultAttributeValue(
                    this.getText(), this.getForm(), attrname);
            if (attr.equals(OperatorDictionary.VALUE_UNKNOWN)) {
                def = defvalue;
            } else {
                def = (attr.equals("true") ? true : false);
            }
        } catch (final Exception e) {
            MathOperator.LOGGER
                    .error("Unknown attribute name:" + attrname, e);
        }
        return def;
    }

    private boolean boolForAttr(final String attrName, final boolean defValue) {
        final String attrValue = this.getMathAttribute(attrName);
        if (attrValue == null) {
            return this.getBooleanFromDictionary(attrName, defValue);
        } else {
            return Boolean.parseBoolean(attrValue);
        }
    }

    /**
     * Calculates parameters based on given attributes and default values for
     * the enclosed operator.
     */
    @Override
    public void eventElementComplete() {
        this.setLargeOp(this.boolForAttr(MathOperator.ATTRIBUTE_LARGEOP,
                false));
        this.setSymmetric(this.boolForAttr(MathOperator.ATTRIBUTE_SYMMETRIC,
                true));
        this.setStretchy(this.boolForAttr(MathOperator.ATTRIBUTE_STRETCHY,
                true));
    }

    /** {@inheritDoc} */
    @Override
    public void eventAllElementsComplete() {

        if (this.getForm() == MathOperator.FORM_UKNOWN) {
            /*
             * -If the operator is the first argument in an mrow of length
             * (i.e. number of arguments) greater than one(ignoring all
             * space-like arguments (see Section 3.2.7) in the determination
             * of both the length and the first argument), the prefix form is
             * used; -if it is the last argument in an mrow of length greater
             * than one (ignoring all space-like arguments), the postfix form
             * is used; - in all other cases, including when the operator is
             * not part of an mrow, the infix form is used.
             */
            final MathElement parent = this.getParent();
            if (parent != null && (parent instanceof MathRow)) {
                final int index = parent.getIndexOfMathElement(this);
                if (index == 0 && parent.getMathElementCount() > 0) {
                    this.m_form = OperatorDictionary.VALUE_PREFIX;
                } else {
                    if (index == (parent.getMathElementCount() - 1)
                            && parent.getMathElementCount() > 0) {
                        this.m_form = OperatorDictionary.VALUE_POSTFIX;
                    } else {
                        this.m_form = OperatorDictionary.VALUE_INFIX;
                    }
                }
            } else {
                this.m_form = OperatorDictionary.VALUE_INFIX;
            }
            // TODO Exception for embellished operators (in use form)
        }
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathOperator.ELEMENT;
    }

}

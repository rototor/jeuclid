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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.helpers.AttributeMap;
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
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(MathOperator.class);

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mo";

    /**
     * Multiplier for increasing size of mo whith attribute largop = true.
     */
    public static final float LARGEOP_CORRECTOR_INLINE = (float) 1.2;

    /**
     * Multiplier for increasing size of mo whith attribute largop = true.
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
     * Variable with stretchy property value.
     */
    private boolean m_stretchy = true;

    /**
     * Value for not initialized type of operator.
     */
    public static final int FORM_UKNOWN = -1;

    /**
     * Type of operator (prefix, infix or postfix).
     */
    private int m_form = FORM_UKNOWN;

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
    public MathOperator(MathBase base) {
        super(base);
        this.setMaxsize("infinity");
        this.setMinsize("1");
    }

    /**
     * Sets form of the operator.
     * 
     * @param form
     *            Form of the operator.
     */
    public void setForm(int form) {
        m_form = form;
    }

    /**
     * Gets form of the operator.
     * 
     * @return Operators form.
     */
    public int getForm() {
        return m_form;
    }

    /**
     * Sets value of fence property of the operator.
     * 
     * @param fence
     *            Flag of fence value.
     */
    public void setFence(boolean fence) {
        m_fence = fence;
    }

    /**
     * Gets value of fence property of the operator.
     * 
     * @return Flag of fence property.
     */
    public boolean getFence() {
        return m_fence;
    }

    /**
     * Sets value of separator property of the operator.
     * 
     * @param separator
     *            Flag of fence value.
     */
    public void setSeparator(boolean separator) {
        m_separator = separator;
    }

    /**
     * Gets value of separator property of the operator.
     * 
     * @return Flag of separator property.
     */
    public boolean getSeparator() {
        return m_separator;
    }

    /**
     * Sets value of lspace property of the operator.
     * 
     * @param lspace
     *            Flag of lspace value.
     */
    public void setLSpace(String lspace) {
        isLSpaceFromDict = false;
        m_lspace = lspace;
    }

    /**
     * Gets value of lspace property of the operator.
     * 
     * @return Flag of lspace property.
     * @param g
     *            Graphics context to use.
     */
    public double getLSpace(Graphics g) {
        if (isLSpaceFromDict) {
            try {
                String s = OperatorDictionary.getDefaultAttributeValue(
                        getText(), m_form, "lspace");
                return AttributesHelper.getPixels(s, g.getFontMetrics());
            } catch (UnknownAttributeException e) {
                logger.error("Unknown attribute name: lspace", e);
                return 0;
            }
        } else {
            return AttributesHelper.getPixels(m_lspace, g.getFontMetrics());
        }
    }

    /**
     * @return Multiplier for increasing size of mo whith attribute largop =
     *         true
     */
    public float getLargeOpCorrector() {
        if (this.isChildBlock(null)) {
            return LARGEOP_CORRECTOR_BLOCK;
        } else {
            return LARGEOP_CORRECTOR_INLINE;
        }
    }

    /**
     * Sets value of rspace property of the operator.
     * 
     * @param rspace
     *            Flag of rspace value.
     */
    public void setRSpace(String rspace) {
        isRSpaceFromDict = false;
        m_rspace = rspace;
    }

    /**
     * Gets value of rspace property of the operator.
     * 
     * @return Flag of rspace property.
     * @param g
     *            Graphics context to use.
     */
    public double getRSpace(Graphics g) {
        if (isRSpaceFromDict) {
            try {
                String s = OperatorDictionary.getDefaultAttributeValue(
                        getText(), m_form, "rspace");
                return AttributesHelper.getPixels(s, g.getFontMetrics());
            } catch (UnknownAttributeException e) {
                logger.error("Unknown attribute name: rspace", e);
                return 0;
            }
        } else {
            return AttributesHelper.getPixels(m_rspace, g.getFontMetrics());
        }
    }

    /**
     * Enables, or disables if the operator should fit his size to the size of
     * the container.
     * 
     * @param stretchy
     *            True, if the operater should fit this size
     */
    public void setStretchy(boolean stretchy) {
        m_stretchy = stretchy;
    }

    /**
     * Returns value of stretchy property.
     * 
     * @return Stretchy flag.
     */
    public boolean getStretchy() {
        return m_stretchy || getFence();
    }

    /**
     * Sets value of symmetric property.
     * 
     * @param symmetric
     *            Symmetric flag.
     */
    public void setSymmetric(boolean symmetric) {
        m_symmetric = symmetric;
        recalculateSize();
    }

    /**
     * Gets value of symmetric property.
     * 
     * @return Symmetric flag.
     */
    public boolean getSymmetric() {
        return m_symmetric;
    }

    private void setRealMathSize(Graphics g) {
        String currentSize = new Float(super.getMathsizeInPoint()).toString();
        String mathSize = currentSize;
        if (AttributesHelper.getPixels(currentSize, getFontMetrics(g)) > (AttributesHelper
                .getPixels(getMaxsize(), getFontMetrics(g)))) {
            mathSize = getMaxsize();
        }
        if (AttributesHelper.getPixels(currentSize, getFontMetrics(g)) < (AttributesHelper
                .getPixels(getMinsize(), getFontMetrics(g)))) {
            mathSize = getMinsize();
        }
        if (!mathSize.equals(currentSize)) {
            super.setMathsize(mathSize);
        }

    }

    /**
     * Sets value of maxsize property.
     * 
     * @param maxsize
     *            Maxsize value.
     */
    public void setMaxsize(String maxsize) {
        this.setAttribute("maxsize", maxsize);
    }

    /**
     * Gets value of maxsize property.
     * 
     * @return Maxsize value.
     */
    public String getMaxsize() {
        return this.getMathAttribute("maxsize");
    }

    /**
     * Sets value of minsize property.
     * 
     * @param minsize
     *            Minsize value.
     */
    public void setMinsize(String minsize) {
        this.setAttribute("minsize", minsize);
    }

    /**
     * Gets value of minsize property.
     * 
     * @return Minsize value.
     */
    public String getMinsize() {
        return this.getMathAttribute("minsize");
    }

    /**
     * Indicates, operater should be drawn larger than normal or not.
     * 
     * @param largeop
     *            True, if the operater should be drawn larger than normal
     */
    public void setLargeOp(boolean largeop) {
        m_largeop = largeop;
        recalculateSize();
    }

    /**
     * Gets value of largeop property.
     * 
     * @return Largeop value.
     */
    public boolean getLargeOp() {
        return m_largeop;
    }

    /**
     * Sets value of moveablelimits property.
     * 
     * @param moveablelimits
     *            Moveablelimits flag.
     */
    public void setMoveableLimits(boolean moveablelimits) {
        m_moveablelimits = moveablelimits;
    }

    /**
     * Gets value of moveablelimits property.
     * 
     * @return Moveablelimits flag.
     */
    public boolean getMoveableLimits() {
        return m_moveablelimits && (!this.isChildBlock(null));
    }

    /**
     * Sets value of accent property.
     * 
     * @param accent
     *            Accent flag.
     */
    public void setAccent(boolean accent) {
        m_accent = accent;
    }

    /**
     * Gets value of accent property.
     * 
     * @return Accent flag.
     */
    public boolean getAccent() {
        return m_accent;
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

    private void paintDelimiter(Graphics g, int posX, int posY, char delimiter,
            boolean vertical) {
        int height, width;

        height = getHeight(g);
        if (!vertical) {
            height = height - 2;
            posY = posY - 1;
        }
        width = (int) (getWidth(g) - getRSpace(g) - getLSpace(g));
        Graphics2D g2d = (Graphics2D) g;
        Font font = g.getFont().deriveFont(this.getFontsizeInPoint() * 100);
        GlyphVector gv = font.createGlyphVector(g2d.getFontRenderContext(),
                new char[] { delimiter });
        Rectangle2D gbounds = gv.getGlyphMetrics(0).getBounds2D();
        double glyphWidth = gbounds.getWidth() / 100;
        double glyphHeight = gbounds.getHeight() / 100;
        double ascent = gbounds.getY() / 100;
        double left = gbounds.getX() / 100;

        double yScale, xScale;
        if (vertical) {
            yScale = (height / glyphHeight);
            xScale = 1;
        } else {
            xScale = Math.max(1, width / glyphWidth);
            yScale = 1;
        }
        AffineTransform transform = g2d.getTransform();
        AffineTransform prevTransform = g2d.getTransform();
        double y = posY + getDescentHeight(g);
        y = y - (ascent + glyphHeight) * yScale;
        double x = posX - left * xScale;
        if (vertical) {
            x = x + (width - glyphWidth) / 2;
        }
        transform.translate(x, y);
        transform.scale(xScale, yScale);

        g2d.setTransform(transform);
        g2d.drawString(String.valueOf(delimiter), 0, 0);
        g2d.setTransform(prevTransform);
    }

    /**
     * Gets the used font. Everything regardes font, processed by MathBase
     * object.
     * 
     * @return Font Font object.
     */
    public Font getFont() {
        Font font = super.getFont();
        if (getLargeOp()) {
            return getMathvariantAsVariant().createFont(
                    this.getFontsizeInPoint() * getLargeOpCorrector(), 'A');
        } else {
            // TODO: This should use the default font size
            if (getStretchy() && isVerticalDelimeter()
                    && font.getSize() > m_base.getFontSize()) {
                return getMathvariantAsVariant().createFont(
                        m_base.getFontSize(), 'A');
            } else {
                return font;
            }
        }
    }

    private boolean isVerticalDelimeter() {
        return getText().length() == 1
                && (VER_DELIMITERS.indexOf(getText().charAt(0)) >= 0 || getFence());
    }

    private boolean isHorizontalDelimeter() {
        return getText().length() == 1
                && (HOR_DELIMITERS.indexOf(getText().charAt(0)) >= 0);
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
    public void paint(Graphics g, int posX, int posY) {
        super.paint(g, posX, posY);
        setRealMathSize(g);
        g.setFont(getFont());
        posX = (int) (posX + getLSpace(g));
        if (getText().length() == 0) {
            return;
        }
        char firstChar = getText().charAt(0);

        if (getText().length() == 1 && getStretchy()) {
            if (isVerticalDelimeter()) {
                getParent().setCalculatingSize(true);
                int ascent = getParent().calculateAscentHeight(g);
                int descent = getParent().calculateDescentHeight(g);
                getParent().setCalculatingSize(false);
                int height = ascent + descent;
                if (height <= getFontMetrics(g).getHeight()) {
                    g.drawString(getText(), posX, posY);
                } else {
                    paintDelimiter(g, posX, posY, firstChar, true);
                }
            } else if ((HOR_DELIMITERS.indexOf(firstChar) >= 0)
                    && (getParent() instanceof MathOver
                            || getParent() instanceof MathUnderOver || getParent() instanceof MathUnder)) {
                int width = (int) (getWidth(g) - getLSpace(g) - getRSpace(g));
                Polygon rp = new Polygon();
                rp.addPoint(posX + width, posY - getHeight(g) / 2);
                rp.addPoint(posX + width
                        - (int) (Math.round(getFont().getSize() / 3)), posY);
                rp.addPoint(posX + width
                        - (int) (Math.round(getFont().getSize() / 3)), posY
                        - getHeight(g));
                Polygon lp = new Polygon();
                lp.addPoint(posX, posY - getHeight(g) / 2);
                lp.addPoint(posX + (int) (Math.round(getFont().getSize() / 3)),
                        posY);
                lp.addPoint(posX + (int) (Math.round(getFont().getSize() / 3)),
                        posY - getHeight(g));
                switch (HOR_DELIMITERS.indexOf(firstChar)) {
                case 0: // overbracket
                    paintDelimiter(g, posX, posY, firstChar, false);
                    break;
                case 1: // underbracket
                    paintDelimiter(g, posX, posY, firstChar, false);
                    break;
                case 2: // underbar
                    g.fillRect(posX, posY - (int) (getFont().getSize() / 12),
                            width, (int) (getFont().getSize() / 12 + 1));
                    break;
                case 3: // overbar
                    g.fillRect(posX, posY - (int) getHeight(g), width,
                            ((int) getFont().getSize() / 12 + 1));
                    break;
                case 4: // leftarrow
                    g.fillPolygon(lp);
                    g.fillRect(posX + (Math.round(getFont().getSize() / 3)),
                            Math.round(posY - getHeight(g) * 2 / 3), width
                                    - (Math.round(getFont().getSize() / 3)),
                            (int) (getFont().getSize() / 12 + 1));
                    break;
                case 5: // rightarrow
                    g.fillPolygon(rp);
                    g
                            .fillRect(posX, Math.round(posY - getHeight(g) * 2
                                    / 3), width
                                    - (int) (Math
                                            .round(getFont().getSize() / 3)),
                                    (int) (getFont().getSize() / 12 + 1));
                    break;
                case 6: // leftrightarrow
                    g.fillPolygon(rp);
                    g.fillPolygon(lp);
                    g.fillRect(posX
                            + (int) (Math.round(getFont().getSize() / 3)), Math
                            .round(posY - getHeight(g) * 2 / 3), width
                            - (int) (Math.round(getFont().getSize() / 3 * 2)),
                            (int) (getFont().getSize() / 12 + 1));
                    break;
                case 7: // double underbar
                    g.fillRect(posX, posY - (int) getHeight(g), width,
                            ((int) getFont().getSize() / 12 + 1));
                    g.fillRect(posX, posY - (int) (getFont().getSize() / 12),
                            width, (int) (getFont().getSize() / 12 + 1));
                    break;
                case 8: // double overbar
                    g.fillRect(posX, posY - (int) getHeight(g), width,
                            ((int) getFont().getSize() / 12 + 1));
                    g.fillRect(posX, posY - (int) (getFont().getSize() / 12),
                            width, (int) (getFont().getSize() / 12 + 1));
                    break;
                default:
                    paintDelimiter(g, posX, posY, firstChar, false);
                }

            } else {
                if (getLargeOp()) {
                    int y = posY
                            + getDescentHeight(g)
                            - MathText.getCharsMaxDescentHeight(g, getFont(),
                                    MathText.getChars(getText()));
                    g.drawString(getText(), posX, y);
                } else {
                    g.drawString(getText(), posX, posY);
                }
            }
        } else {
            if (getLargeOp()) {
                int y = posY
                        + getDescentHeight(g)
                        - MathText.getCharsMaxDescentHeight(g, getFont(),
                                MathText.getChars(getText()));
                g.drawString(getText(), posX, y);
            } else {
                g.drawString(getText(), posX, posY);
            }
        }
    }

    /** {@inheritDoc} */
    public int calculateWidth(Graphics g) {
        int result = 0;
        setRealMathSize(g);

        if (getText().length() != 1) {
            result = getFontMetrics(g).stringWidth(getText());
        } else {
            if (isHorizontalDelimeter()
                    && (getParent() instanceof MathOver
                            || getParent() instanceof MathUnderOver || getParent() instanceof MathUnder)) {
                if (getParent().isCalculatingSize()) {
                    result = -1;
                } else {
                    getParent().setCalculatingSize(true);
                    int res = Math.max(getParent().getWidth(g), getFontMetrics(
                            g).stringWidth(getText()));
                    getParent().setCalculatingSize(false);
                    result = res;
                }
            } else {
                result = getFontMetrics(g).stringWidth(getText());
            }
        }

        if (result >= 0) {
            result = result + (int) (getLSpace(g) + getRSpace(g));
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
    private GlyphVector getGlyphVector(char theChar) {
        return getFont().createGlyphVector(
                new FontRenderContext(new AffineTransform(), true, false),
                new char[] { theChar });
    }

    /** {@inheritDoc} */
    public int calculateAscentHeight(Graphics g) {
        setRealMathSize(g);
        int result = 0;
        if (getText().length() != 1) {
            result = MathText.getCharsMaxAscentHeight(g, getFont(), MathText
                    .getChars(getText()));
        } else {
            char firstChar = getText().charAt(0);

            if (isVerticalDelimeter()) {
                if (!getStretchy()) {
                    result = MathText.getCharsMaxAscentHeight(g, getFont(),
                            new char[] { firstChar });
                } else {
                    if (getParent().isCalculatingSize()) {
                        result = 0;
                    } else {
                        getParent().setCalculatingSize(true);
                        int dh = getParent().getDescentHeight(g);
                        int ah = getParent().getAscentHeight(g);
                        getParent().setCalculatingSize(false);
                        if (getSymmetric()) {
                            dh = dh + getMiddleShift(g);
                            ah = ah - getMiddleShift(g);
                            dh = Math.max(dh, ah);
                            ah = dh + getMiddleShift(g);
                            dh = dh - getMiddleShift(g);
                        }
                        int size = AttributesHelper.getPixels(getMaxsize(),
                                getFontMetrics(g));
                        if (ah + dh > size) {
                            ah = (ah - getMiddleShift(g)) * size / (ah + dh)
                                    + getMiddleShift(g);
                        } else {
                            size = AttributesHelper.getPixels(getMinsize(),
                                    getFontMetrics(g));
                            if (ah + dh < size) {
                                ah = (ah - getMiddleShift(g)) * size
                                        / (ah + dh) + getMiddleShift(g);
                            }
                        }
                        result = ah;
                    }
                }
            } else if (HOR_DELIMITERS.indexOf(firstChar) >= 0) {
                double lh = Math.max(getFont().getSize() / 12, 1);
                Rectangle2D rect = getGlyphVector(firstChar).getGlyphMetrics(0)
                        .getBounds2D();
                switch (HOR_DELIMITERS.indexOf(firstChar)) {
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
                result = MathText.getCharsMaxAscentHeight(g, getFont(),
                        MathText.getChars(getText()));
            }
        }

        return result;
    }

    /** {@inheritDoc} */
    public int calculateDescentHeight(Graphics g) {
        setRealMathSize(g);
        int result = 0;
        if (getText().length() != 1) {
            result = MathText.getCharsMaxDescentHeight(g, getFont(), MathText
                    .getChars(getText()));
        } else {
            char firstChar = getText().charAt(0);
            if (VER_DELIMITERS.indexOf(getText().charAt(0)) >= 0) {
                if (!getStretchy()) {
                    result = MathText.getCharsMaxDescentHeight(g, getFont(),
                            new char[] { firstChar });
                } else {
                    if (getParent().isCalculatingSize()) {
                        result = 0;
                    } else {
                        getParent().setCalculatingSize(true);
                        int dh = getParent().getDescentHeight(g);
                        int ah = getParent().getAscentHeight(g);
                        getParent().setCalculatingSize(false);
                        if (getSymmetric()) {
                            dh = dh + getMiddleShift(g);
                            ah = ah - getMiddleShift(g);
                            dh = Math.max(dh, ah);
                            ah = dh + getMiddleShift(g);
                            dh = dh - getMiddleShift(g);
                        }
                        int size = AttributesHelper.getPixels(getMaxsize(),
                                getFontMetrics(g));
                        if (ah + dh > size) {
                            dh = (dh + getMiddleShift(g)) * size / (ah + dh)
                                    - getMiddleShift(g);
                        } else {
                            size = AttributesHelper.getPixels(getMinsize(),
                                    getFontMetrics(g));
                            if (ah + dh < size) {
                                dh = (dh + getMiddleShift(g)) * size
                                        / (ah + dh) - getMiddleShift(g);
                            }
                        }
                        result = dh;
                    }
                }
            } else if (HOR_DELIMITERS.indexOf(firstChar) >= 0) {
                result = 0;
            } else {
                result = MathText.getCharsMaxDescentHeight(g, getFont(),
                        MathText.getChars(getText()));
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
    public static boolean isStretchyByContext(AbstractMathElement parent) {
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
    private boolean getBooleanFromDictionary(String attrname, boolean defvalue) {
        boolean def = false;
        try {
            String attr = OperatorDictionary.getDefaultAttributeValue(
                    getText(), getForm(), attrname);
            if (attr.equals(OperatorDictionary.VALUE_UNKNOWN)) {
                def = defvalue;
            } else {
                def = (attr.equals("true") ? true : false);
            }
        } catch (Exception e) {
            logger.error("Unknown attribute name:" + attrname, e);
        }
        return def;
    }

    /**
     * Currently this method initializes only stretchy and largeop properties.
     * 
     * @param attributes
     *            Attributes of the element.
     */
    public void eventInitSpecificAttributes(AttributeMap attributes) {
        super.eventInitSpecificAttributes(attributes);

        boolean def = getBooleanFromDictionary(MathOperator.ATTRIBUTE_LARGEOP,
                false);
        setLargeOp(attributes.getBoolean(MathOperator.ATTRIBUTE_LARGEOP, def));

        def = getBooleanFromDictionary(MathOperator.ATTRIBUTE_SYMMETRIC, true);
        setSymmetric(attributes.getBoolean(MathOperator.ATTRIBUTE_SYMMETRIC,
                def));

        def = getBooleanFromDictionary(MathOperator.ATTRIBUTE_STRETCHY, true);
        setStretchy(attributes.getBoolean(MathOperator.ATTRIBUTE_STRETCHY, def));
    }

    /** {@inheritDoc} */
    public void eventAllElementsComplete() {

        if (getForm() == MathOperator.FORM_UKNOWN) {
            /*
             * -If the operator is the first argument in an mrow of length (i.e.
             * number of arguments) greater than one(ignoring all space-like
             * arguments (see Section 3.2.7) in the determination of both the
             * length and the first argument), the prefix form is used; -if it
             * is the last argument in an mrow of length greater than one
             * (ignoring all space-like arguments), the postfix form is used; -
             * in all other cases, including when the operator is not part of an
             * mrow, the infix form is used.
             */
            AbstractMathElement parent = this.getParent();
            if (parent != null && (parent instanceof MathRow)) {
                int index = parent.getIndexOfMathElement(this);
                if (index == 0 && parent.getMathElementCount() > 0) {
                    m_form = OperatorDictionary.VALUE_PREFIX;
                } else {
                    if (index == (parent.getMathElementCount() - 1)
                            && parent.getMathElementCount() > 0) {
                        m_form = OperatorDictionary.VALUE_POSTFIX;
                    } else {
                        m_form = OperatorDictionary.VALUE_INFIX;
                    }
                }
            } else {
                m_form = OperatorDictionary.VALUE_INFIX;
            }
            // TODO Exception for embellished operators (in use form)
        }
    }

}

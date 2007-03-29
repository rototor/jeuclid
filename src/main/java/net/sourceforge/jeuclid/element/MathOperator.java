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

import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.dom.ChangeTrackingInterface;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.generic.MathElement;
import net.sourceforge.jeuclid.element.helpers.AttributesHelper;
import net.sourceforge.jeuclid.element.helpers.OperatorDictionary;
import net.sourceforge.jeuclid.element.helpers.UnknownAttributeException;
import net.sourceforge.jeuclid.util.StringUtil;

import org.w3c.dom.mathml.MathMLOperatorElement;

/**
 * This class presents a math operator, like "(" or "*".
 * 
 * @author Unkown
 * @author Max Berger
 */
public class MathOperator extends AbstractMathElement implements
        MathMLOperatorElement {

    /** Attribute for form. */
    public static final String ATTR_FORM = "form";

    /** Attribute for separator. */
    public static final String ATTR_SEPARATOR = "separator";

    /** Attribute for lspace. */
    public static final String ATTR_LSPACE = "lspace";

    /** Attribute for rspace. */
    public static final String ATTR_RSPACE = "rspace";

    /** Attribute for min size. */
    public static final String ATTR_MINSIZE = "minsize";

    /** Attribute for max size. */
    public static final String ATTR_MAXSIZE = "maxsize";

    /** Attribute for moveable limits. */
    public static final String ATTR_MOVEABLELIMITS = "moveablelimits";

    /** Attribute for accent. */
    public static final String ATTR_ACCENT = "accent";

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
    public static final String ATTR_STRETCHY = "stretchy";

    /**
     * Attribute name of the largeop property.
     */
    public static final String ATTR_LARGEOP = "largeop";

    /**
     * Attribute name of the symmetric property.
     */
    public static final String ATTR_SYMMETRIC = "symmetric";

    /**
     * Attribute name of the fence property.
     */
    public static final String ATTR_FENCE = "fence";

    /**
     * Horizontal delimiters.
     */
    public static final String HOR_DELIMITERS = "\uFE37\uFE38\u005F\u00AF\u2190\u2192\u2194\u0333\u033F";

    /**
     * Vertical delimiters.
     */
    public static final String VER_DELIMITERS = "[{()}]|\u2223\u2225\u2329\u232A";

    /**
     * Logger for this class
     */
    // unused
    // private static final Log LOGGER =
    // LogFactory.getLog(MathOperator.class);
    /** horizontal scale factor. */
    private float calcScaleX = 1.0f;

    private float calcScaleY = 1.0f;

    private float calcBaselineShift;

    /**
     * Creates a mathoperator element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathOperator(final MathBase base) {
        super(base);
        // CHECKSTYLE:OFF
        this.setDefaultMathAttribute(MathOperator.ATTR_FORM, "infix");
        this.setDefaultMathAttribute(MathOperator.ATTR_FENCE, "false");
        this.setDefaultMathAttribute(MathOperator.ATTR_SEPARATOR, "false");
        this.setDefaultMathAttribute(MathOperator.ATTR_LSPACE,
                AttributesHelper.THICKMATHSPACE);
        this.setDefaultMathAttribute(MathOperator.ATTR_RSPACE,
                AttributesHelper.THICKMATHSPACE);
        this.setDefaultMathAttribute(MathOperator.ATTR_STRETCHY, "false");
        this.setDefaultMathAttribute(MathOperator.ATTR_SYMMETRIC, "true");
        this.setDefaultMathAttribute(MathOperator.ATTR_MAXSIZE,
                AttributesHelper.INFINITY);
        this.setDefaultMathAttribute(MathOperator.ATTR_MINSIZE, "1");
        this.setDefaultMathAttribute(MathOperator.ATTR_LARGEOP, "false");
        this.setDefaultMathAttribute(MathOperator.ATTR_MOVEABLELIMITS,
                "false");
        this.setDefaultMathAttribute(MathOperator.ATTR_ACCENT, "false");
        // CHECKSTYLE:ON
    }

    /**
     * Gets value of lspace property of the operator.
     * 
     * @return Flag of lspace property.
     */
    private float getLspaceAsFloat() {
        return AttributesHelper.convertSizeToPt(this.getLspace(), this,
                AttributesHelper.PT);

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
     * Gets value of rspace property of the operator.
     * 
     * @return Flag of rspace property.
     */
    private float getRspaceAsFloat() {
        return AttributesHelper.convertSizeToPt(this.getRspace(), this,
                AttributesHelper.PT);
    }

    private boolean isFence() {
        return Boolean.parseBoolean(this.getFence());
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

    private boolean isVerticalDelimeter() {
        return this.getText().length() == 1
                && (MathOperator.VER_DELIMITERS.indexOf(this.getText()
                        .charAt(0)) >= 0 || this.isFence());
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
    public void paint(final Graphics2D g, final int posX, final int posY) {
        super.paint(g, posX, posY);
        this.calculateSpecs(g);

        if (this.getText().length() > 0) {
            final TextLayout theLayout = this.produceUnstrechtedLayout(g);
            final AffineTransform saveAt = g.getTransform();
            g.translate(this.getLspaceAsFloat() + posX, posY
                    + this.calcBaselineShift);
            g.transform(AffineTransform.getScaleInstance(this.calcScaleX,
                    this.calcScaleY));
            theLayout.draw(g, 0, 0);
            g.setTransform(saveAt);
        }
    }

    private TextLayout produceUnstrechtedLayout(final Graphics2D g) {
        float fontSizeInPoint = this.getFontsizeInPoint();
        if (Boolean.parseBoolean(this.getLargeop())) {
            fontSizeInPoint *= this.getLargeOpCorrector();
        }
        final TextLayout theLayout = new TextLayout(StringUtil
                .convertStringtoAttributedString(this.getText(),
                        this.getMathvariantAsVariant(), fontSizeInPoint,
                        this.mbase).getIterator(), g.getFontRenderContext());
        return theLayout;
    }

    private void calculateSpecs(final Graphics2D g) {

        if (Boolean.parseBoolean(this.getStretchy())) {
            final Rectangle2D textBounds = this.produceUnstrechtedLayout(g)
                    .getBounds();
            if (this.isVerticalDelimeter()) {
                this.getParent().setCalculatingSize(true);
                final float ascent = this.getParent()
                        .calculateAscentHeight(g);
                final float descent = this.getParent()
                        .calculateDescentHeight(g);
                this.getParent().setCalculatingSize(false);
                final float realheight = (float) textBounds.getHeight();
                final float targetheight = Math.max(realheight, ascent
                        + descent);

                // TODO: use minsize / maxsize

                this.calcScaleY = targetheight / realheight;

                final float realDescent = (float) ((textBounds.getY() + textBounds
                        .getHeight()) * this.calcScaleY);
                this.calcBaselineShift = descent - realDescent;
            } else {
                this.calcScaleY = 1.0f;
                this.calcBaselineShift = 0.0f;
            }

            if (this.isHorizontalDelimeter()) {
                final float realwidth = (float) (textBounds.getWidth() + textBounds
                        .getX());
                MathElement m = (MathElement) this.getNextSibling();
                float targetwidth = realwidth;
                while (m != null) {
                    targetwidth = Math.max(targetwidth, m.getWidth(g));
                    m = (MathElement) m.getNextSibling();
                }
                this.calcScaleX = targetwidth / realwidth;
            } else {
                this.calcScaleX = 1.0f;
            }
        } else {
            this.calcScaleX = 1.0f;
            this.calcScaleY = 1.0f;
            this.calcBaselineShift = 0.0f;
        }
    }

    /** {@inheritDoc} */
    @Override
    public int calculateWidth(final Graphics2D g) {
        final float space = this.getLspaceAsFloat() + this.getRspaceAsFloat();
        if (this.getText().equals("")) {
            return (int) space;
        } else {
            final double scaleFactor;
            if (this.getParent().isCalculatingSize()) {
                scaleFactor = 1.0f;
            } else {
                this.calculateSpecs(g);
                scaleFactor = this.calcScaleX;
            }
            final Rectangle2D r2d = this.produceUnstrechtedLayout(g)
                    .getBounds();
            return (int) Math.ceil((r2d.getWidth() + r2d.getX())
                    * scaleFactor + space);
        }

    }

    /** {@inheritDoc} */
    @Override
    public int calculateAscentHeight(final Graphics2D g) {
        if (this.getText().equals("")) {
            return g.getFontMetrics().getAscent();
        } else {

            final double scaleFactor;
            if (this.getParent().isCalculatingSize()) {
                scaleFactor = 1.0f;
            } else {
                this.calculateSpecs(g);
                scaleFactor = this.calcScaleY;
            }

            // TextLayout.getAscent returns the max ascent for this font,
            // not the one for the actual content!
            final Rectangle2D textBounds = this.produceUnstrechtedLayout(g)
                    .getBounds();
            return (int) Math.ceil(-textBounds.getY() * scaleFactor
                    - this.calcBaselineShift);
        }

    }

    private double descentWithoutScaleFactor(final Graphics2D g) {
        final Rectangle2D textBounds = this.produceUnstrechtedLayout(g)
                .getBounds();
        return textBounds.getY() + textBounds.getHeight();
    }

    /** {@inheritDoc} */
    @Override
    public int calculateDescentHeight(final Graphics2D g) {

        if (this.getText().equals("")) {
            return g.getFontMetrics().getDescent();
        } else {

            final double scaleFactor;
            if (this.getParent().isCalculatingSize()) {
                scaleFactor = 1.0f;
            } else {
                this.calculateSpecs(g);
                scaleFactor = this.calcScaleY;
            }
            return (int) Math.ceil(this.descentWithoutScaleFactor(g)
                    * scaleFactor + this.calcBaselineShift);
        }

    }

    /** {@inheritDoc} */
    @Override
    protected void changeHook() {
        super.changeHook();
        this.detectFormParameter();
        this.loadAttributeFromDictionary(MathOperator.ATTR_LARGEOP, "false");
        this.loadAttributeFromDictionary(MathOperator.ATTR_SYMMETRIC, "true");
        this.loadAttributeFromDictionary(MathOperator.ATTR_STRETCHY, "false");
        this.loadAttributeFromDictionary(MathOperator.ATTR_FENCE, "false");
        this.loadAttributeFromDictionary(MathOperator.ATTR_LSPACE,
                AttributesHelper.THICKMATHSPACE);
        this.loadAttributeFromDictionary(MathOperator.ATTR_RSPACE,
                AttributesHelper.THICKMATHSPACE);
        // TODO: Load all.

        if (this.isFence()) {
            this.setDefaultMathAttribute(MathOperator.ATTR_STRETCHY, "true");
        }
        if (!this.isChildBlock(null)) {
            // TODO: Check if this logic is correct.
            this.setDefaultMathAttribute(MathOperator.ATTR_MOVEABLELIMITS,
                    "false");
        }

        final MathElement parent = this.getParent();
        if (parent instanceof ChangeTrackingInterface) {
            ((ChangeTrackingInterface) parent).addListener(this);
        }
    }

    private void loadAttributeFromDictionary(final String attrname,
            final String defvalue) {
        String attr;
        try {
            attr = OperatorDictionary.getDefaultAttributeValue(
                    this.getText(), this.getForm(), attrname);
        } catch (final UnknownAttributeException e) {
            attr = defvalue;
        }
        if (attr.equals(OperatorDictionary.VALUE_UNKNOWN)) {
            attr = defvalue;
        }
        this.setDefaultMathAttribute(attrname, attr);

    }

    private void detectFormParameter() {
        final String form;
        final MathElement parent = this.getParent();
        if (parent != null && (parent instanceof MathRow)) {
            final int index = parent.getIndexOfMathElement(this);
            if (index == 0 && parent.getMathElementCount() > 0) {
                form = OperatorDictionary.FORM_PREFIX;
            } else {
                if (index == (parent.getMathElementCount() - 1)
                        && parent.getMathElementCount() > 0) {
                    form = OperatorDictionary.FORM_POSTFIX;
                } else {
                    form = OperatorDictionary.FORM_INFIX;
                }
            }
        } else {
            form = OperatorDictionary.FORM_INFIX;
        }
        this.setDefaultMathAttribute(MathOperator.ATTR_FORM, form);
        // TODO: Exception for embelished operators
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathOperator.ELEMENT;
    }

    /** {@inheritDoc} */
    public String getLargeop() {
        return this.getMathAttribute(MathOperator.ATTR_LARGEOP);
    }

    /** {@inheritDoc} */
    public String getLspace() {
        return this.getMathAttribute(MathOperator.ATTR_LSPACE);
    }

    /** {@inheritDoc} */
    public String getMovablelimits() {
        return this.getMathAttribute(MathOperator.ATTR_MOVEABLELIMITS);
    }

    /** {@inheritDoc} */
    public String getRspace() {
        return this.getMathAttribute(MathOperator.ATTR_RSPACE);
    }

    /** {@inheritDoc} */
    public void setAccent(final String accent) {
        this.setAttribute(MathOperator.ATTR_ACCENT, accent);
    }

    /** {@inheritDoc} */
    public void setFence(final String fence) {
        this.setAttribute(MathOperator.ATTR_FENCE, fence);
    }

    /** {@inheritDoc} */
    public void setForm(final String form) {
        this.setAttribute(MathOperator.ATTR_FORM, form);
    }

    /** {@inheritDoc} */
    public void setLargeop(final String largeop) {
        this.setAttribute(MathOperator.ATTR_LARGEOP, largeop);
    }

    /** {@inheritDoc} */
    public void setLspace(final String lspace) {
        this.setAttribute(MathOperator.ATTR_LSPACE, lspace);
    }

    /** {@inheritDoc} */
    public void setMovablelimits(final String movablelimits) {
        this.setAttribute(MathOperator.ATTR_MOVEABLELIMITS, movablelimits);
    }

    /** {@inheritDoc} */
    public void setRspace(final String rspace) {
        this.setAttribute(MathOperator.ATTR_RSPACE, rspace);
    }

    /** {@inheritDoc} */
    public void setSeparator(final String separator) {
        this.setAttribute(MathOperator.ATTR_SEPARATOR, separator);
    }

    /** {@inheritDoc} */
    public void setStretchy(final String stretchy) {
        this.setAttribute(MathOperator.ATTR_STRETCHY, stretchy);
    }

    /** {@inheritDoc} */
    public void setSymmetric(final String symmetric) {
        this.setAttribute(MathOperator.ATTR_SYMMETRIC, symmetric);
    }

    /** {@inheritDoc} */
    public String getFence() {
        return this.getMathAttribute(MathOperator.ATTR_FENCE);
    }

    /** {@inheritDoc} */
    public String getForm() {
        return this.getMathAttribute(MathOperator.ATTR_FORM);
    }

    /** {@inheritDoc} */
    public String getSeparator() {
        return this.getMathAttribute(MathOperator.ATTR_SEPARATOR);
    }

    /** {@inheritDoc} */
    public String getStretchy() {
        return this.getMathAttribute(MathOperator.ATTR_STRETCHY);
    }

    /** {@inheritDoc} */
    public String getAccent() {
        return this.getMathAttribute(MathOperator.ATTR_ACCENT);
    }

    /** {@inheritDoc} */
    public String getSymmetric() {
        return this.getMathAttribute(MathOperator.ATTR_SYMMETRIC);
    }

}

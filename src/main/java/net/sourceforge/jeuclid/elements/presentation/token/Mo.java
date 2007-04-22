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

package net.sourceforge.jeuclid.elements.presentation.token;

import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.dom.ChangeTrackingInterface;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.elements.presentation.general.Mrow;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;
import net.sourceforge.jeuclid.elements.support.operatordict.OperatorDictionary;
import net.sourceforge.jeuclid.elements.support.operatordict.UnknownAttributeException;
import net.sourceforge.jeuclid.elements.support.text.StringUtil;

import org.w3c.dom.mathml.MathMLOperatorElement;
import org.w3c.dom.mathml.MathMLUnderOverElement;

/**
 * This class presents a math operator, like "(" or "*".
 * 
 * @author Unknown
 * @author Max Berger
 * @version $Revision$
 */
public class Mo extends AbstractJEuclidElement implements
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

    /** Wrong attribute name for movable limits. */
    public static final String ATTR_MOVEABLEWRONG = "moveablelimits";

    /** Attribute for moveable limits. */
    public static final String ATTR_MOVABLELIMITS = "movablelimits";

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
    public Mo(final MathBase base) {
        super(base);
        // CHECKSTYLE:OFF
        this.setDefaultMathAttribute(Mo.ATTR_FORM, "infix");
        this.setDefaultMathAttribute(Mo.ATTR_FENCE, "false");
        this.setDefaultMathAttribute(Mo.ATTR_SEPARATOR, "false");
        this.setDefaultMathAttribute(Mo.ATTR_LSPACE,
                AttributesHelper.THICKMATHSPACE);
        this.setDefaultMathAttribute(Mo.ATTR_RSPACE,
                AttributesHelper.THICKMATHSPACE);
        this.setDefaultMathAttribute(Mo.ATTR_STRETCHY, "false");
        this.setDefaultMathAttribute(Mo.ATTR_SYMMETRIC, "true");
        this.setDefaultMathAttribute(Mo.ATTR_MAXSIZE,
                AttributesHelper.INFINITY);
        this.setDefaultMathAttribute(Mo.ATTR_MINSIZE, "1");
        this.setDefaultMathAttribute(Mo.ATTR_LARGEOP, "false");
        this.setDefaultMathAttribute(Mo.ATTR_MOVABLELIMITS, "false");
        this.setDefaultMathAttribute(Mo.ATTR_ACCENT, "false");
        // CHECKSTYLE:ON
    }

    /**
     * Gets value of lspace property of the operator.
     * 
     * @return Flag of lspace property.
     */
    private float getLspaceAsFloat() {
        // TODO: decide if this is necessary
        // if (this.getParent().isChildBlock(this)) {
        return AttributesHelper.convertSizeToPt(this.getLspace(), this,
                AttributesHelper.PT);
        // } else {
        // return 0.0f;
        // }
    }

    /**
     * @return Multiplier for increasing size of mo whith attribute largop =
     *         true
     */
    public float getLargeOpCorrector() {
        if (this.isChildBlock(null)) {
            return Mo.LARGEOP_CORRECTOR_BLOCK;
        } else {
            return Mo.LARGEOP_CORRECTOR_INLINE;
        }
    }

    /**
     * Gets value of rspace property of the operator.
     * 
     * @return Flag of rspace property.
     */
    private float getRspaceAsFloat() {
        // TODO: Decide if this is necessary
        // if (this.getParent().isChildBlock(this)) {
        return AttributesHelper.convertSizeToPt(this.getRspace(), this,
                AttributesHelper.PT);
        // } else {
        // return 0.0f;
        // }
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
        this.setAttribute(Mo.ATTR_MAXSIZE, maxsize);
    }

    /**
     * Gets value of maxsize property.
     * 
     * @return Maxsize value.
     */
    public String getMaxsize() {
        return this.getMathAttribute(Mo.ATTR_MAXSIZE);
    }

    /**
     * Sets value of minsize property.
     * 
     * @param minsize
     *            Minsize value.
     */
    public void setMinsize(final String minsize) {
        this.setAttribute(Mo.ATTR_MINSIZE, minsize);
    }

    /**
     * Gets value of minsize property.
     * 
     * @return Minsize value.
     */
    public String getMinsize() {
        return this.getMathAttribute(Mo.ATTR_MINSIZE);
    }

    private boolean isVerticalDelimeter() {
        return this.getText().length() == 1
                && (Mo.VER_DELIMITERS.indexOf(this.getText().charAt(0)) >= 0 || this
                        .isFence());
    }

    private boolean isHorizontalDelimeter() {
        return this.getText().length() == 1
                && (Mo.HOR_DELIMITERS.indexOf(this.getText().charAt(0)) >= 0);
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
    public void paint(final Graphics2D g, final float posX, final float posY) {
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
                        this.getMathBase()).getIterator(), g
                .getFontRenderContext());
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

            final JEuclidElement parent = this.getParent();
            if ((this.isHorizontalDelimeter())
                    && (parent instanceof MathMLUnderOverElement)) {
                final float realwidth = (float) (textBounds.getWidth() + textBounds
                        .getX());

                final MathMLUnderOverElement muo = (MathMLUnderOverElement) parent;
                final JEuclidElement base = (JEuclidElement) muo.getBase();
                parent.setCalculatingSize(true);
                final float targetwidth = base.getWidth(g);
                parent.setCalculatingSize(false);

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
    public float calculateWidth(final Graphics2D g) {
        final float space = this.getLspaceAsFloat() + this.getRspaceAsFloat();
        if (this.getText().equals("")) {
            return space;
        } else {
            final float scaleFactor;
            if (this.getParent().isCalculatingSize()) {
                scaleFactor = 1.0f;
            } else {
                this.calculateSpecs(g);
                scaleFactor = this.calcScaleX;
            }
            return (float) StringUtil.getWidthForTextLayout(this
                    .produceUnstrechtedLayout(g))
                    * scaleFactor + space;
        }

    }

    /** {@inheritDoc} */
    @Override
    public float getXCenter(final Graphics2D g) {
        return (this.getWidth(g) - this.getRspaceAsFloat() + this
                .getLspaceAsFloat()) / 2.0f;

    }

    /** {@inheritDoc} */
    @Override
    public float calculateAscentHeight(final Graphics2D g) {
        if (this.getText().equals("")) {
            return g.getFontMetrics().getAscent();
        } else {

            final float scaleFactor;
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
            return (float) (-textBounds.getY() * scaleFactor - this.calcBaselineShift);
        }

    }

    private float descentWithoutScaleFactor(final Graphics2D g) {
        final Rectangle2D textBounds = this.produceUnstrechtedLayout(g)
                .getBounds();
        return (float) (textBounds.getY() + textBounds.getHeight());
    }

    /** {@inheritDoc} */
    @Override
    public float calculateDescentHeight(final Graphics2D g) {

        if (this.getText().equals("")) {
            return g.getFontMetrics().getDescent();
        } else {

            final float scaleFactor;
            if (this.getParent().isCalculatingSize()) {
                scaleFactor = 1.0f;
            } else {
                this.calculateSpecs(g);
                scaleFactor = this.calcScaleY;
            }
            return this.descentWithoutScaleFactor(g) * scaleFactor
                    + this.calcBaselineShift;
        }

    }

    /** {@inheritDoc} */
    @Override
    protected void changeHook() {
        super.changeHook();
        this.detectFormParameter();
        this.loadAttributeFromDictionary(Mo.ATTR_LARGEOP, "false");
        this.loadAttributeFromDictionary(Mo.ATTR_SYMMETRIC, "true");
        this.loadAttributeFromDictionary(Mo.ATTR_STRETCHY, "false");
        this.loadAttributeFromDictionary(Mo.ATTR_FENCE, "false");
        this.loadAttributeFromDictionary(Mo.ATTR_LSPACE,
                AttributesHelper.THICKMATHSPACE);
        this.loadAttributeFromDictionary(Mo.ATTR_RSPACE,
                AttributesHelper.THICKMATHSPACE);
        this.loadAttributeFromDictionary(Mo.ATTR_MOVABLELIMITS, "false");

        // TODO: Load all.

        if (this.isFence()) {
            this.setDefaultMathAttribute(Mo.ATTR_STRETCHY, "true");
        }

        final JEuclidElement parent = this.getParent();
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
        final JEuclidElement parent = this.getParent();
        if (parent != null && (parent instanceof Mrow)) {
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
        this.setDefaultMathAttribute(Mo.ATTR_FORM, form);
        // TODO: Exception for embelished operators
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Mo.ELEMENT;
    }

    /** {@inheritDoc} */
    public String getLargeop() {
        return this.getMathAttribute(Mo.ATTR_LARGEOP);
    }

    /** {@inheritDoc} */
    public String getLspace() {
        return this.getMathAttribute(Mo.ATTR_LSPACE);
    }

    /** {@inheritDoc} */
    public String getMovablelimits() {
        final String wrongAttr = this.getMathAttribute(Mo.ATTR_MOVEABLEWRONG);
        if (wrongAttr != null) {
            return wrongAttr;
        } else {
            return this.getMathAttribute(Mo.ATTR_MOVABLELIMITS);
        }
    }

    /** {@inheritDoc} */
    public String getRspace() {
        return this.getMathAttribute(Mo.ATTR_RSPACE);
    }

    /** {@inheritDoc} */
    public void setAccent(final String accent) {
        this.setAttribute(Mo.ATTR_ACCENT, accent);
    }

    /** {@inheritDoc} */
    public void setFence(final String fence) {
        this.setAttribute(Mo.ATTR_FENCE, fence);
    }

    /** {@inheritDoc} */
    public void setForm(final String form) {
        this.setAttribute(Mo.ATTR_FORM, form);
    }

    /** {@inheritDoc} */
    public void setLargeop(final String largeop) {
        this.setAttribute(Mo.ATTR_LARGEOP, largeop);
    }

    /** {@inheritDoc} */
    public void setLspace(final String lspace) {
        this.setAttribute(Mo.ATTR_LSPACE, lspace);
    }

    /** {@inheritDoc} */
    public void setMovablelimits(final String movablelimits) {
        this.setAttribute(Mo.ATTR_MOVABLELIMITS, movablelimits);
    }

    /** {@inheritDoc} */
    public void setRspace(final String rspace) {
        this.setAttribute(Mo.ATTR_RSPACE, rspace);
    }

    /** {@inheritDoc} */
    public void setSeparator(final String separator) {
        this.setAttribute(Mo.ATTR_SEPARATOR, separator);
    }

    /** {@inheritDoc} */
    public void setStretchy(final String stretchy) {
        this.setAttribute(Mo.ATTR_STRETCHY, stretchy);
    }

    /** {@inheritDoc} */
    public void setSymmetric(final String symmetric) {
        this.setAttribute(Mo.ATTR_SYMMETRIC, symmetric);
    }

    /** {@inheritDoc} */
    public String getFence() {
        return this.getMathAttribute(Mo.ATTR_FENCE);
    }

    /** {@inheritDoc} */
    public String getForm() {
        return this.getMathAttribute(Mo.ATTR_FORM);
    }

    /** {@inheritDoc} */
    public String getSeparator() {
        return this.getMathAttribute(Mo.ATTR_SEPARATOR);
    }

    /** {@inheritDoc} */
    public String getStretchy() {
        return this.getMathAttribute(Mo.ATTR_STRETCHY);
    }

    /** {@inheritDoc} */
    public String getAccent() {
        return this.getMathAttribute(Mo.ATTR_ACCENT);
    }

    /** {@inheritDoc} */
    public String getSymmetric() {
        return this.getMathAttribute(Mo.ATTR_SYMMETRIC);
    }

}

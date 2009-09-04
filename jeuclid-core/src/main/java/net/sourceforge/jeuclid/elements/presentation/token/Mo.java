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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedString;

import net.sourceforge.jeuclid.Constants;
import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.Display;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.elements.presentation.general.Mrow;
import net.sourceforge.jeuclid.elements.support.GraphicsSupport;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;
import net.sourceforge.jeuclid.elements.support.operatordict.OperatorDictionary;
import net.sourceforge.jeuclid.elements.support.operatordict.OperatorDictionary2;
import net.sourceforge.jeuclid.elements.support.operatordict.UnknownAttributeException;
import net.sourceforge.jeuclid.elements.support.text.StringUtil;
import net.sourceforge.jeuclid.elements.support.text.StringUtil.TextLayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutView;
import net.sourceforge.jeuclid.layout.TextObject;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.events.DOMCustomEvent;
import org.w3c.dom.Node;
import org.w3c.dom.events.CustomEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.mathml.MathMLOperatorElement;
import org.w3c.dom.mathml.MathMLUnderOverElement;

/**
 * This class presents a math operator, like "(" or "*".
 * 
 * @version $Revision$
 */

// CHECKSTYLE:OFF
// Class Fan-out is to high. However, this is required due to complexity of
// mo.
public final class Mo extends AbstractJEuclidElement implements
        MathMLOperatorElement, EventListener {
    // CHECKSTYLE:ON

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

    /** Attribute for movable limits. */
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
     * Event name for operator events.
     */
    public static final String MOEVENT = "MOEvent";

    /**
     * Horizontal delimiters.
     * 
     * @todo Check the uncommented ones, possibly add more / remove some?
     */
    public static final String HOR_DELIMITERS = /* _ */"\u005F"
            + /* OverBar */"\u00AF" + /* UnderBar */"\u0332" + "\u0333"
            + "\u033F" + "\u2190" + "\u2192" + "\u2194"
            + /* OverBracket */"\u23B4" + /* UnderBracket */"\u23B5"
            + /* OverParenthesis */"\uFE35" + /* UnderParenthesis */"\uFE36"
            + /* OverBrace */"\uFE37" + /* UnderBrace */"\uFE38"
            + /* Frown */"\u2322";

    /**
     * Vertical delimiters.
     * 
     * @todo Add others (?)
     */
    public static final String VER_DELIMITERS = "[{()}]|"
            + /* Up Arrow */"\u2191" + /* Down Arrow */"\u2193" + /*
                                                                   * Up Arrow
                                                                   * Down Arrow
                                                                   */"\u21C5"
            + /* Up Arrow Up Arrow */"\u21C8" + /* Down Down Arrows */"\u21CA"
            + /* Down Arrow Up Arrow */"\u21F5" + "\u2223\u2225\u2329\u232A";

    private static final long serialVersionUID = 1L;

    private final OperatorDictionary opDict;

    private boolean inChangeHook;

    /**
     * Logger for this class
     */
    // unused
    // private static final Log LOGGER =
    // LogFactory.getLog(MathOperator.class);
    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public Mo(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);

        this.setDefaultMathAttribute(Mo.ATTR_FORM,
                OperatorDictionary.FORM_INFIX);
        this.setDefaultMathAttribute(Mo.ATTR_FENCE, Constants.FALSE);
        this.setDefaultMathAttribute(Mo.ATTR_SEPARATOR, Constants.FALSE);
        this.setDefaultMathAttribute(Mo.ATTR_LSPACE,
                AttributesHelper.THICKMATHSPACE);
        this.setDefaultMathAttribute(Mo.ATTR_RSPACE,
                AttributesHelper.THICKMATHSPACE);
        this.setDefaultMathAttribute(Mo.ATTR_STRETCHY, Constants.FALSE);
        this.setDefaultMathAttribute(Mo.ATTR_SYMMETRIC, Constants.TRUE);
        this
                .setDefaultMathAttribute(Mo.ATTR_MAXSIZE,
                        AttributesHelper.INFINITY);
        this.setDefaultMathAttribute(Mo.ATTR_MINSIZE, "1");
        this.setDefaultMathAttribute(Mo.ATTR_LARGEOP, Constants.FALSE);
        this.setDefaultMathAttribute(Mo.ATTR_MOVABLELIMITS, Constants.FALSE);
        this.setDefaultMathAttribute(Mo.ATTR_ACCENT, Constants.FALSE);
        this.opDict = OperatorDictionary2.getInstance();
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Mo(this.nodeName, this.ownerDocument);
    }

    /**
     * Gets value of lspace property of the operator.
     * 
     * @return Flag of lspace property.
     */
    private float getLspaceAsFloat(final LayoutContext now) {
        if (((Integer) now.getParameter(Parameter.SCRIPTLEVEL)) > 0) {
            return 0.0f;
        } else {
            return AttributesHelper.convertSizeToPt(this.getLspace(), now,
                    AttributesHelper.PT);
        }
    }

    /**
     * @param now
     *            applied layout context.
     * @return Multiplier for increasing size of mo whith attribute largop =
     *         true
     */
    public float getLargeOpCorrector(final LayoutContext now) {
        if (Display.BLOCK.equals(now.getParameter(Parameter.DISPLAY))) {
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
    private float getRspaceAsFloat(final LayoutContext now) {
        if (((Integer) now.getParameter(Parameter.SCRIPTLEVEL)) > 0) {
            return 0.0f;
        } else {
            return AttributesHelper.convertSizeToPt(this.getRspace(), now,
                    AttributesHelper.PT);
        }
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
        return (this.getText().length() == 1)
                && ((Mo.VER_DELIMITERS.indexOf(this.getText().charAt(0)) >= 0) || this
                        .isFence());
    }

    private boolean isHorizontalDelimeter() {
        return (this.getText().length() == 1)
                && (Mo.HOR_DELIMITERS.indexOf(this.getText().charAt(0)) >= 0);
    }

    private TextLayout produceUnstrechtedLayout(final Graphics2D g,
            final LayoutContext now) {
        assert g != null : "Graphics2d is null in produceUnstrechtedLayout";
        float fontSizeInPoint = GraphicsSupport.getFontsizeInPoint(now);
        if (Boolean.parseBoolean(this.getLargeop())) {
            fontSizeInPoint *= this.getLargeOpCorrector(now);
        }

        final String theText = this.getText();
        final AttributedString aString = StringUtil
                .convertStringtoAttributedString(theText, this
                        .getMathvariantAsVariant(), fontSizeInPoint, now);
        final TextLayout theLayout = StringUtil
                .createTextLayoutFromAttributedString(g, aString, now);
        return theLayout;
    }

    /** {@inheritDoc} */
    @Override
    public void changeHook() {
        super.changeHook();
        if (!this.inChangeHook) {
            this.inChangeHook = true;
            this.detectFormParameter();
            this.loadAttributeFromDictionary(Mo.ATTR_LARGEOP, Constants.FALSE);
            this.loadAttributeFromDictionary(Mo.ATTR_SYMMETRIC, Constants.TRUE);
            this.loadAttributeFromDictionary(Mo.ATTR_STRETCHY, Constants.FALSE);
            this.loadAttributeFromDictionary(Mo.ATTR_FENCE, Constants.FALSE);
            this.loadAttributeFromDictionary(Mo.ATTR_LSPACE,
                    AttributesHelper.THICKMATHSPACE);
            this.loadAttributeFromDictionary(Mo.ATTR_RSPACE,
                    AttributesHelper.THICKMATHSPACE);
            this.loadAttributeFromDictionary(Mo.ATTR_MOVABLELIMITS,
                    Constants.FALSE);
            // TODO: Load all.

            JEuclidElement parent = this.getParent();
            while (parent != null) {
                if (parent instanceof EventTarget) {
                    ((EventTarget) parent).addEventListener(
                            "DOMSubtreeModified", this, false);
                }
                if (parent instanceof Mrow) {
                    parent = null;
                } else {
                    parent = parent.getParent();
                }
            }

            if (this.isFence()) {
                this.setDefaultMathAttribute(Mo.ATTR_STRETCHY, Constants.TRUE);
            }
            final CustomEvent evt = new DOMCustomEvent();
            evt.initCustomEventNS(null, Mo.MOEVENT, true, false, null);
            this.dispatchEvent(evt);
            this.inChangeHook = false;
        }
    }

    private void loadAttributeFromDictionary(final String attrname,
            final String defvalue) {
        String attr;
        try {
            attr = this.opDict.getDefaultAttributeValue(this.getText(), this
                    .getForm(), attrname);
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
        if ((parent != null) && (parent instanceof Mrow)) {
            final int index = parent.getIndexOfMathElement(this);
            if ((index == 0) && (parent.getMathElementCount() > 0)) {
                form = OperatorDictionary.FORM_PREFIX;
            } else {
                if ((index == (parent.getMathElementCount() - 1))
                        && (parent.getMathElementCount() > 0)) {
                    form = OperatorDictionary.FORM_POSTFIX;
                } else {
                    form = OperatorDictionary.FORM_INFIX;
                }
            }
        } else {
            form = OperatorDictionary.FORM_INFIX;
        }
        this.setDefaultMathAttribute(Mo.ATTR_FORM, form);
        // TODO: Exception for embellished operators
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
        final String wrongAttr = this.getMathAttribute(Mo.ATTR_MOVEABLEWRONG,
                false);
        if (wrongAttr == null) {
            return this.getMathAttribute(Mo.ATTR_MOVABLELIMITS);
        } else {
            return wrongAttr;
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

    /** {@inheritDoc} */
    @Override
    public void layoutStage1(final LayoutView view, final LayoutInfo info,
            final LayoutStage childMinStage, final LayoutContext context) {
        final LayoutContext now = this.applyLocalAttributesToContext(context);
        final Graphics2D g = view.getGraphics();
        final TextLayout t = this.produceUnstrechtedLayout(g, now);

        final StringUtil.TextLayoutInfo tli = StringUtil.getTextLayoutInfo(t,
                true);
        final float ascent = tli.getAscent();
        final float descent = tli.getDescent();
        final float xOffset = tli.getOffset();
        final float contentWidth = tli.getWidth() + xOffset;
        final JEuclidElement parent = this.getParent();
        float lspace = this.getLspaceAsFloat(now);
        float rspace = this.getRspaceAsFloat(now);
        if ((parent != null) && (parent.hasChildPostscripts(this, context))) {
            rspace = 0.0f;
        } else {
            rspace = this.getRspaceAsFloat(now);
        }
        if ((parent != null) && (parent.hasChildPrescripts(this))) {
            lspace = 0.0f;
        } else {
            lspace = this.getLspaceAsFloat(now);
        }

        info.setAscentHeight(ascent, LayoutStage.STAGE1);
        info.setDescentHeight(descent, LayoutStage.STAGE1);
        info.setHorizontalCenterOffset(lspace + contentWidth / 2.0f,
                LayoutStage.STAGE1);
        info.setWidth(lspace + contentWidth + rspace, LayoutStage.STAGE1);
        if (Boolean.parseBoolean(this.getStretchy())
                || this.isVerticalDelimeter() || this.isHorizontalDelimeter()) {
            info.setLayoutStage(LayoutStage.STAGE1);
        } else {
            info.setGraphicsObject(new TextObject(t, lspace + tli.getOffset(),
                    0, null, (Color) now.getParameter(Parameter.MATHCOLOR)));
            info.setLayoutStage(LayoutStage.STAGE2);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void layoutStage2(final LayoutView view, final LayoutInfo info,
            final LayoutContext context) {
        final LayoutContext now = this.applyLocalAttributesToContext(context);

        final Graphics2D g = view.getGraphics();
        final TextLayout t = this.produceUnstrechtedLayout(g, now);

        final float calcScaleY;
        final float calcScaleX;
        final float calcBaselineShift;
        final boolean stretchVertically = this.isVerticalDelimeter();
        JEuclidElement parent = this.getParent();
        while ((((parent instanceof MathMLUnderOverElement) && stretchVertically) || ((parent instanceof Mrow) && (parent
                .getMathElementCount() == 1)))
                && (parent.getParent() != null)) {
            parent = parent.getParent();
        }
        final LayoutInfo parentInfo = view.getInfo(parent);
        final TextLayoutInfo textLayoutInfo = StringUtil.getTextLayoutInfo(t,
                true);
        if (parentInfo == null) {
            calcScaleX = 1.0f;
            calcScaleY = 1.0f;
            calcBaselineShift = 0.0f;
        } else {
            if (stretchVertically) {
                final float[] yf = this.calcYScaleFactorAndBaselineShift(info,
                        parentInfo, textLayoutInfo, now);
                calcScaleY = yf[0];
                calcBaselineShift = yf[1];
            } else {
                calcScaleY = 1.0f;
                calcBaselineShift = 0.0f;
            }
            calcScaleX = this
                    .calcXScaleFactor(info, parentInfo, textLayoutInfo);
        }
        info.setGraphicsObject(new TextObject(t, this.getLspaceAsFloat(now)
                + textLayoutInfo.getOffset() * calcScaleX, calcBaselineShift,
                AffineTransform.getScaleInstance(calcScaleX, calcScaleY),
                (Color) now.getParameter(Parameter.MATHCOLOR)));
        info.setLayoutStage(LayoutStage.STAGE2);
    }

    private float calcXScaleFactor(final LayoutInfo info,
            final LayoutInfo parentInfo, final TextLayoutInfo textLayoutInfo) {
        final float calcScaleX;
        final float stretchWidth = parentInfo.getStretchWidth();
        if ((this.isHorizontalDelimeter()) && (stretchWidth > 0.0f)) {
            final float realwidth = textLayoutInfo.getWidth();
            if (realwidth > 0) {
                calcScaleX = stretchWidth / realwidth;
                info.setWidth(stretchWidth, LayoutStage.STAGE2);
                info.setHorizontalCenterOffset(stretchWidth / 2.0f,
                        LayoutStage.STAGE2);
            } else {
                calcScaleX = 1.0f;
            }
        } else {
            calcScaleX = 1.0f;
        }
        return calcScaleX;
    }

    private float[] calcYScaleFactorAndBaselineShift(final LayoutInfo info,
            final LayoutInfo parentInfo, final TextLayoutInfo textLayoutInfo,
            final LayoutContext now) {
        final float calcScaleY;
        final float calcBaselineShift;
        final float targetNAscent = parentInfo.getStretchAscent();
        final float targetNDescent = parentInfo.getStretchDescent();

        final float targetNHeight = targetNAscent + targetNDescent;

        final float realDescent = textLayoutInfo.getDescent();
        final float realHeight = textLayoutInfo.getAscent() + realDescent;

        // TODO: MaxSize / MinSize could also be inherited from MStyle.
        final float maxSize = AttributesHelper.parseRelativeSize(this
                .getMaxsize(), now, realHeight);
        final float minSize = AttributesHelper.parseRelativeSize(this
                .getMinsize(), now, realHeight);
        final float targetHeight = Math.max(Math.min(targetNHeight, maxSize),
                minSize);
        final float targetDescent = targetHeight / targetNHeight
                * (targetNHeight / 2.0f)
                - (targetNHeight / 2.0f - targetNDescent);

        if (realHeight > 0.0f) {
            calcScaleY = targetHeight / realHeight;
        } else {
            calcScaleY = 1.0f;
        }
        final float realDescentScaled = realDescent * calcScaleY;
        calcBaselineShift = targetDescent - realDescentScaled;

        info.setDescentHeight(targetDescent, LayoutStage.STAGE2);
        info.setAscentHeight(targetHeight - targetDescent, LayoutStage.STAGE2);
        return new float[] { calcScaleY, calcBaselineShift };
    }

    /** {@inheritDoc} */
    public void handleEvent(final Event evt) {
        this.changeHook();
    }
}

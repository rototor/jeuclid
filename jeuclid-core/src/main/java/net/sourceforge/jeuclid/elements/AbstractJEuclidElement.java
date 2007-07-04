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

package net.sourceforge.jeuclid.elements;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.ParameterKey;
import net.sourceforge.jeuclid.dom.AbstractChangeTrackingElement;
import net.sourceforge.jeuclid.dom.PartialTextImpl;
import net.sourceforge.jeuclid.elements.generic.MathImpl;
import net.sourceforge.jeuclid.elements.presentation.general.Mrow;
import net.sourceforge.jeuclid.elements.presentation.table.Mtable;
import net.sourceforge.jeuclid.elements.presentation.table.Mtr;
import net.sourceforge.jeuclid.elements.presentation.token.Mo;
import net.sourceforge.jeuclid.elements.presentation.token.Mtext;
import net.sourceforge.jeuclid.elements.support.attributes.AttributeMap;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;
import net.sourceforge.jeuclid.elements.support.attributes.MathVariant;
import net.sourceforge.jeuclid.elements.support.text.CharConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLMathElement;
import org.w3c.dom.mathml.MathMLNodeList;

/**
 * The basic class for all math elements. Every element class inherits from
 * this class. It provides basic functionality for drawing.
 * 
 * @author Unknown
 * @author Max Berger
 * @version $Revision$
 */
// CHECKSTYLE:OFF
public abstract class AbstractJEuclidElement extends
// CHECKSTYLE:ON
        AbstractChangeTrackingElement implements JEuclidElement {

    /** Constant for mathvariant attribute. */
    public static final String ATTR_MATHVARIANT = "mathvariant";

    /** Constant for mathcolor attribute. */
    public static final String ATTR_MATHCOLOR = "mathcolor";

    /** Constant for mathsize attribute. */
    public static final String ATTR_MATHSIZE = "mathsize";

    /** Constant for fontfamily attribute. */
    public static final String ATTR_DEPRECATED_FONTFAMILY = "fontfamily";

    /** Constant for fontstyle attribute. */
    public static final String ATTR_DEPRECATED_FONTSTYLE = "fontstyle";

    /** Constant for fontweight attribute. */
    public static final String ATTR_DEPRECATED_FONTWEIGHT = "fontweight";

    /** Constant for fontsize attribute. */
    public static final String ATTR_DEPRECATED_FONTSIZE = "fontsize";

    /** Constant for color attribute. */
    public static final String ATTR_DEPRECATED_COLOR = "color";

    /** Constant for background attribute. */
    public static final String ATTR_DEPRECATED_BACKGROUND = "background";

    /** Constant for class attribute. */
    public static final String ATTR_CLASS = "class";

    /** Constant for style attribute. */
    public static final String ATTR_STYLE = "style";

    /** Constant for id attribute. */
    public static final String ATTR_ID = "id";

    /** Constant for href attribute. */
    public static final String ATTR_HREF = "xlink:href";

    /** Constant for xref attribute. */
    public static final String ATTR_XREF = "xref";

    /** The mathbackground attribute. */
    public static final String ATTR_MATHBACKGROUND = "mathbackground";

    /** value for top alignment. */
    public static final String ALIGN_TOP = "top";

    /** value for bottom alignment. */
    public static final String ALIGN_BOTTOM = "bottom";

    /** value for center alignment. */
    public static final String ALIGN_CENTER = "center";

    /** value for baseline alignment. */
    public static final String ALIGN_BASELINE = "baseline";

    /** value for axis alignment. */
    public static final String ALIGN_AXIS = "axis";

    /** value for left alignment. */
    public static final String ALIGN_LEFT = "left";

    /** value for right alignment. */
    public static final String ALIGN_RIGHT = "right";

    /**
     * largest value for all trivial spaces (= spaces that can be ignored /
     * shortened).
     */
    public static final int TRIVIAL_SPACE_MAX = 0x20;

    /**
     * The URI from MathML.
     */
    public static final String URI = "http://www.w3.org/1998/Math/MathML";

    private static final float MIDDLE_SHIFT = 0.38f;

    private static final float DEFAULT_SCIPTSIZEMULTIPLIER = 0.71f;

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory
            .getLog(AbstractJEuclidElement.class);

    private static final Set<String> DEPRECATED_ATTRIBUTES = new HashSet<String>();

    /**
     * Reference to the MathBase object, which controls all font and metrics
     * computing.
     */
    private MathBase mbase;

    /**
     * flag - true when runing calculationg of the element.
     */
    private boolean calculatingSize;

    /**
     * Value of calculated height of the element .
     */
    private float calculatedHeight = -1;

    /**
     * Value of calculated width of the element .
     */
    private float calculatedWidth = -1;

    /**
     * Value of calculated ascent height of the element
     */
    private float calculatedAscentHeight = -1;

    /**
     * Value of calculated descent height of the element
     */
    private float calculatedDescentHeight = -1;

    /**
     * Value of calculated height of the element
     */
    private float calculatedStretchHeight = -1;

    /**
     * Value of calculated ascent height of the element
     */
    private float calculatedStretchAscentHeight = -1;

    /**
     * Value of calculated descent height of the element .
     */
    private float calculatedStretchDescentHeight = -1;

    private float lastPaintedX = -1;

    private float lastPaintedY = -1;

    /**
     * Reference to the element acting as parent if there is no parent.
     */
    private JEuclidElement fakeParent;

    private final Map<String, String> defaultMathAttributes = new HashMap<String, String>();

    /**
     * Variable of "scriptsize" attribute, default value is 0.71.
     */
    private float mscriptsizemultiplier = AbstractJEuclidElement.DEFAULT_SCIPTSIZEMULTIPLIER;

    /**
     * This variable is intended to keep the value of vertical shift of the
     * line. Actually this value is stored in the top-level element of the
     * line. This value affects only elements with enlarged parts (such as
     * "msubsup", "munderover", etc.)
     */
    private float globalLineCorrecter;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */

    public AbstractJEuclidElement(final MathBase base) {
        this.setMathBase(base);
    }

    /**
     * Reset element sizes.
     */
    protected void recalculateSize() {
        this.calculatedHeight = -1;
        this.calculatedAscentHeight = -1;
        this.calculatedDescentHeight = -1;
        this.calculatedStretchHeight = -1;
        this.calculatedStretchAscentHeight = -1;
        this.calculatedStretchDescentHeight = -1;
        this.calculatedWidth = -1;
    }

    /** {@inheritDoc} */
    @Override
    protected void changeHook() {
        super.changeHook();
        this.recalculateSize();
    }

    /**
     * Gets the size of the actual font used (including scriptsizemultiplier).
     * 
     * @return size of the current font.
     */
    public float getFontsizeInPoint() {
        final float scriptMultiplier = (float) Math.pow(this
                .getScriptSizeMultiplier(), this.getAbsoluteScriptLevel());
        float size = this.getMathsizeInPoint() * scriptMultiplier;

        // This results in a size 8 for a default size of 12.
        // TODO: This should use scriptminsize (3.3.4.2)
        final float minSize = this.mbase.getFontSize() * 0.66f;
        if (size < minSize) {
            size = minSize;
        }
        return size;
    }

    /**
     * Gets the used font. Everything regardes font, processed by MathBase
     * object.
     * 
     * @return Font Font object.
     */
    public Font getFont() {
        final String content = this.getText();
        final char aChar;
        if (content.length() > 0) {
            aChar = content.charAt(0);
        } else {
            aChar = 'A';
        }
        return this.getMathvariantAsVariant().createFont(
                this.getFontsizeInPoint(), aChar, this.mbase);

    }

    /** {@inheritDoc} */
    public MathVariant getMathvariantAsVariant() {
        final String mv = this.getMathvariant();
        MathVariant variant = null;
        if (mv != null) {
            variant = MathVariant.stringToMathVariant(mv);
        }
        if (variant == null) {
            // TODO: Not all elements inherit MathVariant!
            final JEuclidElement parent = this.getParent();
            if (parent != null) {
                variant = parent.getMathvariantAsVariant();
            } else {
                // TODO: This is NOT ALWAYS the default variant
                variant = MathVariant.NORMAL;
            }
        }
        return variant;
    }

    /**
     * Retrieve the absolute script level. For most items this will ask the
     * parent item.
     * 
     * @return the absolute script level.
     * @see #getInheritedScriptlevel()
     */
    protected int getAbsoluteScriptLevel() {
        return this.getInheritedScriptlevel();
    }

    /**
     * Retrieves the scriptlevel from the parent node.
     * 
     * @return the scriptlevel of the parent node
     */
    protected int getInheritedScriptlevel() {
        final JEuclidElement parent = this.getParent();
        if (parent == null) {
            return 0;
        } else {
            return parent.getScriptlevelForChild(this);
        }
    }

    /** {@inheritDoc} */
    public int getScriptlevelForChild(final JEuclidElement child) {
        return this.getAbsoluteScriptLevel();
    }

    // /**
    // * Setting size of child of the element.
    // *
    // * @param child
    // * Child element
    // * @param childpos
    // * Position of the child element
    // */
    //
    // private void setChildSize(AbstractMathElement child, int childpos) {
    //
    // float childSize = (float) Math.pow(getScriptSizeMultiplier(), child
    // .getAbsoluteScriptLevel());
    //
    // child.multipleFont(m_font, (float) Math.pow(getScriptSizeMultiplier(),
    // child.getAbsoluteScriptLevel()));
    // System.out.println(child.toString() + " "
    // + child.getAbsoluteScriptLevel() + " "
    // + getScriptSizeMultiplier() + " " + childSize);
    //
    // child.setScriptSizeMultiplier(getScriptSizeMultiplier());
    //
    // if (this instanceof MathMultiScripts) {
    // if (childpos > 0) {
    // child.multipleFont(m_font, getScriptSizeMultiplier());
    // child.setDisplayStyle(false);
    // child.setInheritSisplayStyle(false);
    // }
    // } else if (this instanceof MathOver) {
    // if (childpos == 1) {
    // if (((getMathElement(0) instanceof MathOperator) && ((MathOperator)
    // getMathElement(0))
    // .getMoveableLimits())
    // || (!((MathOver) this).getAccent())) {
    // child.multipleFont(m_font, getScriptSizeMultiplier());
    // child.setDisplayStyle(false);
    // child.setInheritSisplayStyle(false);
    // }
    // }
    // } else if (this instanceof MathUnder) {
    // if (childpos == 1) {
    // if (((getMathElement(0) instanceof MathOperator) && ((MathOperator)
    // getMathElement(0))
    // .getMoveableLimits())
    // || (!((MathUnder) this).getAccentUnder())) {
    // child.multipleFont(m_font, getScriptSizeMultiplier());
    // child.setDisplayStyle(false);
    // child.setInheritSisplayStyle(false);
    // }
    // }
    // } else if (this instanceof MathUnderOver) {
    // if (childpos > 0) {
    // if (((getMathElement(0) instanceof MathOperator) && ((MathOperator)
    // getMathElement(0))
    // .getMoveableLimits())
    // || ((childpos == 1) && (!((MathUnderOver) this)
    // .getAccentUnder()))
    // || ((childpos == 2) && (!((MathUnderOver) this)
    // .getAccent()))) {
    // child.multipleFont(m_font, getScriptSizeMultiplier());
    // child.setDisplayStyle(false);
    // child.setInheritSisplayStyle(false);
    // }
    // }
    // } else if (this instanceof MathRoot) {
    // if (childpos == 1) {
    // child.multipleFont(m_font, (float) Math.pow(
    // getScriptSizeMultiplier(), 2));
    // child.setDisplayStyle(false);
    // child.setInheritSisplayStyle(false);
    // }
    // } else if (this instanceof MathSub) {
    // if (childpos == 1) {
    // child.multipleFont(m_font, getScriptSizeMultiplier());
    // child.setDisplayStyle(false);
    // child.setInheritSisplayStyle(false);
    // }
    // } else if (this instanceof MathSup) {
    // if (childpos == 1) {
    // child.multipleFont(m_font, getScriptSizeMultiplier());
    // child.setDisplayStyle(false);
    // child.setInheritSisplayStyle(false);
    // }
    // } else if (this instanceof MathSubSup) {
    // if (childpos > 0) {
    // child.multipleFont(m_font, getScriptSizeMultiplier());
    // child.setDisplayStyle(false);
    // child.setInheritSisplayStyle(false);
    // }
    // } else if (this instanceof MathStyle) {
    // // child.multipleFont(m_font, (float) Math.pow(
    // // getScriptSizeMultiplier(), ((MathStyle) this)
    // // .getScriptlevel()));
    // } else {
    // child.setFont(m_font);
    // }
    // }

    /**
     * Add a math element as a child.
     * 
     * @param child
     *            Math element object.
     */
    public final void addMathElement(final MathMLElement child) {
        if (child != null) {
            this.appendChild(child);
            if (child instanceof AbstractJEuclidElement) {
                ((AbstractJEuclidElement) child).setMathBase(this
                        .getMathBase());
            }
        }
    }

    /** {@inheritDoc} */
    public JEuclidElement getMathElement(final int index) {
        final org.w3c.dom.NodeList childList = this.getChildNodes();
        if (index >= 0 && index < childList.getLength()) {
            return (JEuclidElement) childList.item(index);
        }
        return null;
    }

    /** {@inheritDoc} */
    public void setMathElement(final int index, final MathMLElement newElement) {
        final org.w3c.dom.NodeList childList = this.getChildNodes();
        while (childList.getLength() < index) {
            this.addMathElement(new Mtext(this.mbase));
        }
        if (childList.getLength() == index) {
            this.addMathElement(newElement);
        } else {
            this.replaceChild(newElement, childList.item(index));
        }
    }

    /** {@inheritDoc} */
    public int getIndexOfMathElement(final JEuclidElement element) {
        final org.w3c.dom.NodeList childList = this.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            if (childList.item(i).equals(element)) {
                return i;
            }
        }
        return -1;
    }

    /** {@inheritDoc} */
    public int getMathElementCount() {
        return this.getChildNodes().getLength();
    }

    /**
     * Add the content of a String to this element.
     * 
     * @param text
     *            String with text of this object.
     */
    public void addText(final String text) {

        Node textNode = this.getLastChild();
        if (!(textNode instanceof Text)) {
            textNode = new PartialTextImpl("");
            this.appendChild(textNode);
        }

        final StringBuilder newText = new StringBuilder();
        if (this.getTextContent() != null) {
            newText.append(textNode.getTextContent());
        }

        // As seen in 2.4.6
        if (text != null) {
            newText.append(text.trim());
        }

        for (int i = 0; i < newText.length() - 1; i++) {
            if (newText.charAt(i) <= AbstractJEuclidElement.TRIVIAL_SPACE_MAX
                    && newText.charAt(i + 1) <= AbstractJEuclidElement.TRIVIAL_SPACE_MAX) {
                newText.deleteCharAt(i);
                // CHECKSTYLE:OFF
                // This is intentional
                i--;
                // CHECKSTYLE:ON
            }
        }

        final String toSet = CharConverter.convertEarly(newText.toString());
        if (toSet.length() > 0) {
            textNode.setTextContent(toSet);
        } else {
            this.removeChild(textNode);
        }

    }

    /**
     * Returns the text content of this element.
     * 
     * @return Text content.
     */
    public String getText() {
        final String theText = this.getTextContent();
        if (theText == null) {
            return "";
        } else {
            return theText;
        }
    }

    /**
     * Sets the base for this element.
     * 
     * @param base
     *            Math base object.
     */

    public void setMathBase(final MathBase base) {
        this.mbase = base;
        final org.w3c.dom.NodeList childList = this.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            final Node node = childList.item(i);
            if (node instanceof AbstractJEuclidElement) {
                ((AbstractJEuclidElement) node).setMathBase(base);
            }
        }
    }

    /** {@inheritDoc} */
    public MathBase getMathBase() {
        return this.mbase;
    }

    /** {@inheritDoc} */
    public void setFakeParent(final JEuclidElement parent) {
        this.fakeParent = parent;
    }

    /** {@inheritDoc} */
    public JEuclidElement getParent() {
        final Node parentNode = this.getParentNode();
        JEuclidElement theParent = null;
        if (parentNode instanceof JEuclidElement) {
            theParent = (JEuclidElement) this.getParentNode();
        }
        if (theParent == null) {
            return this.fakeParent;
        } else {
            return theParent;
        }
    }

    /**
     * Sets value of mathvariant attribute (style of the element).
     * 
     * @param mathvariant
     *            Value of mathvariant.
     */
    public void setMathvariant(final String mathvariant) {
        this.setAttribute(AbstractJEuclidElement.ATTR_MATHVARIANT,
                mathvariant);
    }

    /**
     * Returns value of mathvariant attribute (style of the element).
     * 
     * @return Value of mathvariant.
     */
    public String getMathvariant() {
        // TODO: Support deprecated name
        return this.getMathAttribute(AbstractJEuclidElement.ATTR_MATHVARIANT);
    }

    /**
     * Gets value of scriptsize attribute.
     * 
     * @return Value of scriptsize attribute.
     */
    public float getScriptSizeMultiplier() {
        return this.mscriptsizemultiplier;
    }

    /**
     * Sets value of scriptsize attribute.
     * 
     * @param scriptsizemultiplier
     *            Value of scriptsize attribute.
     */
    public void setScriptSizeMultiplier(final float scriptsizemultiplier) {
        this.mscriptsizemultiplier = scriptsizemultiplier;
    }

    /**
     * Gets the font metrics of the used font.
     * 
     * @return Font metrics.
     * @param g
     *            Graphics2D context to use.
     */
    public FontMetrics getFontMetrics(final Graphics2D g) {
        return g.getFontMetrics(this.getFont());
    }

    /**
     * Sets value of math color attribute.
     * 
     * @param mathcolor
     *            Color object.
     */
    public void setMathcolor(final String mathcolor) {
        this.setAttribute(AbstractJEuclidElement.ATTR_MATHCOLOR, mathcolor);
    }

    /**
     * Returns value of mathcolor attribute.
     * 
     * @return Color as string.
     */
    public String getMathcolor() {
        String color;
        color = this.getMathAttribute(AbstractJEuclidElement.ATTR_MATHCOLOR);
        if (color == null) {
            color = this
                    .getMathAttribute(AbstractJEuclidElement.ATTR_DEPRECATED_COLOR);
        }
        return color;
    }

    /**
     * Retrieve the mathsize attribute.
     * 
     * @return the mathsize attribute.
     */
    public String getMathsize() {
        String size;
        size = this.getMathAttribute(AbstractJEuclidElement.ATTR_MATHSIZE);
        if (size == null) {
            size = this
                    .getMathAttribute(AbstractJEuclidElement.ATTR_DEPRECATED_FONTSIZE);
        }
        return size;

    }

    /**
     * Sets mathsize to a new value.
     * 
     * @param mathsize
     *            value of mathsize.
     */
    public void setMathsize(final String mathsize) {
        this.setAttribute(AbstractJEuclidElement.ATTR_MATHSIZE, mathsize);
    }

    /** {@inheritDoc} */
    public float getMathsizeInPoint() {

        final String msize = this.getMathsize();

        JEuclidNode relativeToElement = null;
        if (this.getParent() != null) {
            relativeToElement = this.getParent();
        } else {
            relativeToElement = this.mbase.getRootElement();
        }
        if (msize == null) {
            return relativeToElement.getMathsizeInPoint();
        }
        return AttributesHelper.convertSizeToPt(msize, relativeToElement,
                AttributesHelper.PT);
    }

    /**
     * Sets default values for math attributes. Default values are returned
     * through getMathAttribute, but not stored in the actual DOM tree. This
     * is necessary to support proper serialization.
     * 
     * @param key
     *            the attribute to set.
     * @param value
     *            value of the attribute.
     */
    protected void setDefaultMathAttribute(final String key,
            final String value) {
        this.defaultMathAttributes.put(key, value);
    }

    /**
     * retrieve an attribute from the MathML or default namespace.
     * 
     * @param attrName
     *            the name of the attribute
     * @return attribtue value
     */
    protected String getMathAttribute(final String attrName) {
        String attrValue;
        attrValue = this.getAttributeNS(AbstractJEuclidElement.URI, attrName);
        if (attrValue == null) {
            attrValue = this.getAttribute(attrName);
        }
        if (attrValue == null) {
            attrValue = this.defaultMathAttributes.get(attrName);
        }
        return attrValue;
    }

    /**
     * Returns value of mathbackground attribute.
     * 
     * @return Color as string.
     */
    public String getMathbackground() {
        String color;
        color = this
                .getMathAttribute(AbstractJEuclidElement.ATTR_MATHBACKGROUND);
        if (color == null) {
            color = this
                    .getMathAttribute(AbstractJEuclidElement.ATTR_DEPRECATED_BACKGROUND);
        }
        return color;
    }

    /**
     * Sets the value of the machbackground attribute.
     * 
     * @param mathbackground
     *            a string to be used as background color.
     */
    public void setMathbackground(final String mathbackground) {
        this.setAttribute(AbstractJEuclidElement.ATTR_MATHBACKGROUND,
                mathbackground);
    }

    /** {@inheritDoc} */
    public Color getForegroundColor() {
        final String colorString = this.getMathcolor();
        Color theColor;
        if (colorString == null) {
            if (this.getParent() != null) {
                theColor = this.getParent().getForegroundColor();
            } else {
                theColor = AttributesHelper.stringToColor(this.mbase
                        .getParams().get(ParameterKey.ForegroundColor),
                        Color.BLACK);
            }
        } else {
            theColor = AttributesHelper.stringToColor(colorString,
                    Color.BLACK);
        }
        return theColor;
    }

    /** {@inheritDoc} */
    public Color getBackgroundColor() {
        final String colorString = this.getMathbackground();
        final Color theColor;
        if (colorString == null) {
            if (this.getParent() != null) {
                // For height debugging purposes, this is left here.
                // theColor = this.getParent().getBackgroundColor();
                theColor = null;
            } else {
                theColor = AttributesHelper.stringToColor(this.mbase
                        .getParams().get(ParameterKey.BackgroundColor), null);
            }
        } else {
            theColor = AttributesHelper.stringToColor(colorString, null);
        }
        return theColor;
    }

    /**
     * Paints a border around this element as debug information.
     * 
     * @param g
     *            The graphics context to use for painting
     * @param posX
     *            The first left position for painting
     * @param posY
     *            The position of the baseline
     */
    public void debug(final Graphics2D g, final float posX, final float posY) {
        g.setColor(Color.blue);
        g.draw(new Line2D.Float(posX, posY - this.getAscentHeight(g), posX
                + this.getWidth(g), posY - this.getAscentHeight(g)));
        g.draw(new Line2D.Float(posX + this.getWidth(g), posY
                - this.getAscentHeight(g), posX + this.getWidth(g), posY
                + this.getDescentHeight(g)));
        g.draw(new Line2D.Float(posX, posY + this.getDescentHeight(g), posX
                + this.getWidth(g), posY + this.getDescentHeight(g)));
        g.draw(new Line2D.Float(posX, posY - this.getAscentHeight(g), posX,
                posY + this.getDescentHeight(g)));
        g.setColor(Color.red);
        g.draw(new Line2D.Float(posX, posY, posX + this.getWidth(g), posY));
        g.setColor(Color.black);
    }

    /** {@inheritDoc} */
    public void setGlobalLineCorrector(final float corrector) {
        if (this.getParent() == null) {
            return;
        }

        // if this is a top-element of the row
        if (this instanceof Mtr || this instanceof Mrow
                && this.getParent() instanceof Mtable
                || this.getParent() instanceof MathImpl) {
            if (this.globalLineCorrecter < corrector) {
                this.globalLineCorrecter = corrector;
            }
        } else {
            this.getParent().setGlobalLineCorrector(corrector);
        }
    }

    /** {@inheritDoc} */
    public float getGlobalLineCorrector() {
        final float retVal;
        if (this.getParent() == null) {
            retVal = 0;
        } else

        // if this is a top-element of the line, it contains the correct
        // number
        if (this instanceof Mtr || this instanceof Mrow
                && this.getParent() instanceof Mtable
                || this.getParent() instanceof MathImpl) {
            retVal = this.globalLineCorrecter;
        } else {
            retVal = this.getParent().getGlobalLineCorrector();
        }
        return retVal;
    }

    /** {@inheritDoc} */
    public float getWidth(final Graphics2D g) {
        if (this.calculatedWidth < 0) {
            this.calculatedWidth = this.calculateWidth(g);
        }
        return this.calculatedWidth;
    }

    /**
     * Caculates width of the element.
     * 
     * @return Width of the element.
     * @param g
     *            Graphics2D context to use.
     */
    public abstract float calculateWidth(Graphics2D g);

    /** {@inheritDoc} */
    public float getHeight(final Graphics2D g) {
        if (this.calculatingSize || this.getParent() != null
                && this.getParent().isCalculatingSize()) {
            if (this.calculatedStretchHeight < 0) {
                this.calculatedStretchHeight = this.calculateHeight(g);
            }
            return this.calculatedStretchHeight;
        } else {
            if (this.calculatedHeight < 0) {
                this.calculatedHeight = this.calculateHeight(g);
            }
            return this.calculatedHeight;
        }
    }

    /**
     * Calculates the current height of the element.
     * 
     * @return Height of the element.
     * @param g
     *            Graphics2D context to use.
     */
    public float calculateHeight(final Graphics2D g) {
        return this.getAscentHeight(g) + this.getDescentHeight(g);
    }

    /** {@inheritDoc} */
    public float getAscentHeight(final Graphics2D g) {
        if (this.calculatingSize || this.getParent() != null
                && this.getParent().isCalculatingSize()) {
            if (this.calculatedStretchAscentHeight < 0) {
                this.calculatedStretchAscentHeight = this
                        .calculateAscentHeight(g);
            }
            return this.calculatedStretchAscentHeight;
        } else {
            if (this.calculatedAscentHeight < 0) {
                this.calculatedAscentHeight = this.calculateAscentHeight(g);
            }
            return this.calculatedAscentHeight;
        }
    }

    /** {@inheritDoc} */
    public abstract float calculateAscentHeight(Graphics2D g);

    /** {@inheritDoc} */
    public float getDescentHeight(final Graphics2D g) {
        if (this.calculatingSize || this.getParent() != null
                && this.getParent().isCalculatingSize()) {
            if (this.calculatedStretchDescentHeight < 0) {
                this.calculatedStretchDescentHeight = this
                        .calculateDescentHeight(g);
            }
            return this.calculatedStretchDescentHeight;
        } else {
            if (this.calculatedDescentHeight < 0) {
                this.calculatedDescentHeight = this.calculateDescentHeight(g);
            }
            return this.calculatedDescentHeight;
        }
    }

    /** {@inheritDoc} */
    public abstract float calculateDescentHeight(Graphics2D g);

    /**
     * Returns the distance of the baseline and the middleline.
     * 
     * @return Distance baseline - middleline.
     * @param g
     *            Graphics2D context to use.
     */
    public float getMiddleShift(final Graphics2D g) {
        return this.getFontMetrics(g).getAscent()
                * AbstractJEuclidElement.MIDDLE_SHIFT;
    }

    /** {@inheritDoc} */
    public void setMathAttributes(final AttributeMap attributes) {
        final Map<String, String> attrsAsMap = attributes.getAsMap();
        for (final Map.Entry<String, String> e : attrsAsMap.entrySet()) {
            final String attrName = e.getKey();
            if (AbstractJEuclidElement.DEPRECATED_ATTRIBUTES
                    .contains(attrName)) {
                AbstractJEuclidElement.LOGGER
                        .warn("Deprecated attribute for " + this.getTagName()
                                + ": " + attrName);
            }
            this.setAttribute(attrName, e.getValue());
        }
        this.recalculateSize();
    }

    /** {@inheritDoc} */
    public boolean isCalculatingSize() {
        return this.calculatingSize;
    }

    /** {@inheritDoc} */
    public void setCalculatingSize(final boolean ncalculatingSize) {
        this.calculatingSize = ncalculatingSize;
    }

    /** {@inheritDoc} */
    public String getClassName() {
        return this.getAttribute(AbstractJEuclidElement.ATTR_CLASS);
    }

    /** {@inheritDoc} */
    public void setClassName(final String className) {
        this.setAttribute(AbstractJEuclidElement.ATTR_CLASS, className);
    }

    /** {@inheritDoc} */
    public String getMathElementStyle() {
        return this.getAttribute(AbstractJEuclidElement.ATTR_STYLE);
    }

    /** {@inheritDoc} */
    public void setMathElementStyle(final String mathElementStyle) {
        this
                .setAttribute(AbstractJEuclidElement.ATTR_STYLE,
                        mathElementStyle);
    }

    /** {@inheritDoc} */
    public String getId() {
        return this.getAttribute(AbstractJEuclidElement.ATTR_ID);
    }

    /** {@inheritDoc} */
    public void setId(final String id) {
        this.setAttribute(AbstractJEuclidElement.ATTR_ID, id);
    }

    /** {@inheritDoc} */
    public String getXref() {
        return this.getAttribute(AbstractJEuclidElement.ATTR_XREF);
    }

    /** {@inheritDoc} */
    public void setXref(final String xref) {
        this.setAttribute(AbstractJEuclidElement.ATTR_XREF, xref);
    }

    /** {@inheritDoc} */
    public String getHref() {
        return this.getAttribute(AbstractJEuclidElement.ATTR_HREF);
    }

    /** {@inheritDoc} */
    public void setHref(final String href) {
        this.setAttribute(AbstractJEuclidElement.ATTR_HREF, href);
    }

    /** {@inheritDoc} */
    public MathMLMathElement getOwnerMathElement() {
        JEuclidElement node = this.getParent();
        while (node != null) {
            if (node instanceof MathMLMathElement) {
                return (MathMLMathElement) node;
            }
            node = node.getParent();
        }
        return null;
    }

    /** {@inheritDoc} */
    public boolean isChildBlock(final JEuclidElement child) {
        final JEuclidElement parent = this.getParent();
        if (parent != null) {
            return parent.isChildBlock(this);
        } else {
            return true;
        }
    }

    /** {@inheritDoc} */
    public boolean hasChildPrescripts(final JEuclidElement child) {
        return false;
    }

    /** {@inheritDoc} */
    public boolean hasChildPostscripts(final JEuclidElement child) {
        return false;
    }

    /** {@inheritDoc} */
    public void paint(final Graphics2D g, final float posX, final float posY) {
        this.lastPaintedX = posX;
        this.lastPaintedY = posY;
        if (this.getBackgroundColor() != null) {
            g.setColor(this.getBackgroundColor());
            g.fill(new Rectangle2D.Float(posX,
                    posY - this.getAscentHeight(g), this.getWidth(g), this
                            .getHeight(g)));
        }

        if (this.getMathBase().isDebug()) {
            this.debug(g, posX, posY);
        }
        g.setColor(this.getForegroundColor());
        g.setFont(this.getFont());

    }

    /** {@inheritDoc} */
    public float getPaintedPosX() {
        return this.lastPaintedX;
    }

    /** {@inheritDoc} */
    public float getPaintedPosY() {
        return this.lastPaintedY;
    }

    /**
     * Returns the children as a MathML NodeList.
     * 
     * @return a list of children
     */
    public MathMLNodeList getContents() {
        return (MathMLNodeList) this.getChildNodes();
    }

    /** {@inheritDoc} */
    public float getXCenter(final Graphics2D g) {
        return this.getWidth(g) / 2.0f;
    }

    {
        AbstractJEuclidElement.DEPRECATED_ATTRIBUTES
                .add(AbstractJEuclidElement.ATTR_DEPRECATED_COLOR);
        AbstractJEuclidElement.DEPRECATED_ATTRIBUTES
                .add(AbstractJEuclidElement.ATTR_DEPRECATED_BACKGROUND);
        AbstractJEuclidElement.DEPRECATED_ATTRIBUTES
                .add(AbstractJEuclidElement.ATTR_DEPRECATED_FONTSIZE);
        AbstractJEuclidElement.DEPRECATED_ATTRIBUTES
                .add(AbstractJEuclidElement.ATTR_DEPRECATED_FONTWEIGHT);
        AbstractJEuclidElement.DEPRECATED_ATTRIBUTES
                .add(AbstractJEuclidElement.ATTR_DEPRECATED_FONTSTYLE);
        AbstractJEuclidElement.DEPRECATED_ATTRIBUTES
                .add(AbstractJEuclidElement.ATTR_DEPRECATED_FONTFAMILY);

        AbstractJEuclidElement.DEPRECATED_ATTRIBUTES
                .add(Mo.ATTR_MOVEABLEWRONG);
    }
}

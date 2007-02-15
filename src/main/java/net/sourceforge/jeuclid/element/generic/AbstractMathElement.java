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

package net.sourceforge.jeuclid.element.generic;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.dom.AbstractChangeTrackingElement;
import net.sourceforge.jeuclid.element.MathMathElement;
import net.sourceforge.jeuclid.element.MathRow;
import net.sourceforge.jeuclid.element.MathTable;
import net.sourceforge.jeuclid.element.MathTableRow;
import net.sourceforge.jeuclid.element.MathText;
import net.sourceforge.jeuclid.element.attributes.MathVariant;
import net.sourceforge.jeuclid.element.helpers.AttributeMap;
import net.sourceforge.jeuclid.element.helpers.AttributesHelper;
import net.sourceforge.jeuclid.element.helpers.CharConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLMathElement;
import org.w3c.dom.mathml.MathMLNodeList;

/**
 * The basic class for all math elements. Every element class inherits from
 * this class. It provides basic functionality for drawing.
 * 
 * @author Unknown
 * @author Max Berger
 */
public abstract class AbstractMathElement extends
        AbstractChangeTrackingElement implements MathElement {

    /** The mathbackground attribute. */
    public static final String ATTR_MATHBACKGROUND = "mathbackground";

    /**
     * The URI from MathML.
     */
    public static final String URI = "http://www.w3.org/1998/Math/MathML";

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory
            .getLog(AbstractMathElement.class);

    private static final Set<String> DEPRECATED_ATTRIBUTES = new HashSet<String>();

    /**
     * flag - true when runing calculationg of the element.
     */
    private boolean calculatingSize;

    /**
     * Value of calculated height of the element .
     */
    private int calculatedHeight = -1;

    /**
     * Value of calculated width of the element .
     */
    private int calculatedWidth = -1;

    /**
     * Value of calculated ascent height of the element
     */
    private int calculatedAscentHeight = -1;

    /**
     * Value of calculated descent height of the element
     */
    private int calculatedDescentHeight = -1;

    /**
     * Value of calculated height of the element
     */
    private int calculatedStretchHeight = -1;

    /**
     * Value of calculated ascent height of the element
     */
    private int calculatedStretchAscentHeight = -1;

    /**
     * Value of calculated descent height of the element .
     */
    private int calculatedStretchDescentHeight = -1;

    private int lastPaintedX = -1;

    private int lastPaintedY = -1;

    /**
     * Reference to the MathBase object, which controls all font and metrics
     * computing.
     */
    protected MathBase m_base;

    /**
     * Reference to the element acting as parent if there is no parent.
     */
    private MathElement fakeParent;

    /**
     * Variable of "scriptsize" attribute, default value is 0.71.
     */
    private float m_scriptsizemultiplier = (float) 0.71;

    /**
     * This variable is intended to keep the value of vertical shift of the
     * line. Actually this value is stored in the top-level element of the
     * line. This value affects only elements with enlarged parts (such as
     * "msubsup", "munderover", etc.)
     */
    private int globalLineCorrecter = 0;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */

    public AbstractMathElement(final MathBase base) {
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
    protected void setChanged(final boolean hasChanged) {
        super.setChanged(hasChanged);
        if (hasChanged) {
            this.recalculateSize();
        }
    }

    /**
     * Gets the size of the actual font used (including scriptsizemultiplier).
     * 
     * @return size of the current font.
     */
    protected float getFontsizeInPoint() {
        final float scriptMultiplier = (float) Math.pow(this
                .getScriptSizeMultiplier(), this.getAbsoluteScriptLevel());
        float size = this.getMathsizeInPoint() * scriptMultiplier;

        // This results in a size 8 for a default size of 12.
        // TODO: This should use scriptminsize (3.3.4.2)
        final float minSize = this.m_base.getFontSize() * 2 / 3;
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
        // TODO: Use actual character.
        return this.getMathvariantAsVariant().createFont(
                this.getFontsizeInPoint(), 'A');

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
            final MathElement parent = this.getParent();
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
        final MathElement parent = this.getParent();
        if (parent == null) {
            return 0;
        } else {
            return parent.getScriptlevelForChild(this);
        }
    }

    /** {@inheritDoc} */
    public int getScriptlevelForChild(final MathElement child) {
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
            if (child instanceof AbstractMathElement) {
                ((AbstractMathElement) child).setMathBase(this.getMathBase());
            }
        }
    }

    /** {@inheritDoc} */
    public MathElement getMathElement(final int index) {
        final org.w3c.dom.NodeList childList = this.getChildNodes();
        if ((index >= 0) && (index < childList.getLength())) {
            return (MathElement) childList.item(index);
        }
        return null;
    }

    /** {@inheritDoc} */
    public void setMathElement(final int index, final MathMLElement newElement) {
        final org.w3c.dom.NodeList childList = this.getChildNodes();
        while (childList.getLength() < index) {
            this.addMathElement(new MathText(this.m_base));
        }
        if (childList.getLength() == index) {
            this.addMathElement(newElement);
        } else {
            this.replaceChild(newElement, childList.item(index));
        }
    }

    /** {@inheritDoc} */
    public int getIndexOfMathElement(final MathElement element) {
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

        final StringBuffer newText = new StringBuffer();
        if (this.getTextContent() != null) {
            newText.append(this.getTextContent());
        }
        if (text != null) {
            newText.append(text);
        }

        for (int i = 0; i < (newText.length() - 1); i++) {
            if (Character.isWhitespace(newText.charAt(i))
                    && Character.isWhitespace(newText.charAt(i + 1))) {
                newText.deleteCharAt(i + 1);
            }
        }

        final String toSet = CharConverter.convert(newText.toString().trim());

        if (toSet.length() > 0) {
            this.setTextContent(toSet);
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
        this.m_base = base;
        final org.w3c.dom.NodeList childList = this.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            ((AbstractMathElement) childList.item(i)).setMathBase(base);
        }
    }

    /**
     * Gets the math base.
     * 
     * @return Math base object.
     */
    public MathBase getMathBase() {
        return this.m_base;
    }

    /** {@inheritDoc} */
    public void setFakeParent(final MathElement parent) {
        this.fakeParent = parent;
    }

    /** {@inheritDoc} */
    public MathElement getParent() {
        final Node parentNode = this.getParentNode();
        MathElement theParent = null;
        if (parentNode instanceof MathElement) {
            theParent = (MathElement) this.getParentNode();
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
        this.setAttribute("mathvariant", mathvariant);
    }

    /**
     * Returns value of mathvariant attribute (style of the element).
     * 
     * @return Value of mathvariant.
     */
    public String getMathvariant() {
        // TODO: Support deprecated name
        return this.getMathAttribute("mathvariant");
    }

    /**
     * Gets value of scriptsize attribute.
     * 
     * @return Value of scriptsize attribute.
     */
    public float getScriptSizeMultiplier() {
        return this.m_scriptsizemultiplier;
    }

    /**
     * Sets value of scriptsize attribute.
     * 
     * @param scriptsizemultiplier
     *            Value of scriptsize attribute.
     */
    public void setScriptSizeMultiplier(final float scriptsizemultiplier) {
        this.m_scriptsizemultiplier = scriptsizemultiplier;
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
        this.setAttribute("mathcolor", mathcolor);
    }

    /**
     * Returns value of mathcolor attribute.
     * 
     * @return Color as string.
     */
    public String getMathcolor() {
        String color;
        color = this.getMathAttribute("mathcolor");
        if (color == null) {
            color = this.getMathAttribute("color");
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
        size = this.getMathAttribute("mathsize");
        if (size == null) {
            size = this.getMathAttribute("fontsize");
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
        this.setAttribute("mathsize", mathsize);
    }

    /** {@inheritDoc} */
    public float getMathsizeInPoint() {

        final String msize = this.getMathsize();

        float relativeToSize;
        if (this.getParent() != null) {
            relativeToSize = this.getParent().getMathsizeInPoint();
        } else {
            relativeToSize = this.m_base.getFontSize();
        }
        if (msize == null) {
            return relativeToSize;
        }
        return AttributesHelper.getFontSize(msize, relativeToSize);
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
        attrValue = this.getAttributeNS(AbstractMathElement.URI, attrName);
        if (attrValue == null) {
            attrValue = this.getAttribute(attrName);
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
                .getMathAttribute(AbstractMathElement.ATTR_MATHBACKGROUND);
        if (color == null) {
            color = this.getMathAttribute("background");
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
        this.setAttribute(AbstractMathElement.ATTR_MATHBACKGROUND,
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
                theColor = Color.BLACK;
            }
        } else {
            theColor = this.stringToColor(colorString, Color.BLACK);
        }
        return theColor;
    }

    private Color stringToColor(String value, final Color defaultValue) {

        if ((value != null) && (value.length() > 0)) {
            if (value.equalsIgnoreCase("Black")) {
                value = "#000000";
            } else if (value.equalsIgnoreCase("Green")) {
                value = "#008000";
            } else if (value.equalsIgnoreCase("Silver")) {
                value = "#C0C0C0";
            } else if (value.equalsIgnoreCase("Lime")) {
                value = "#00FF00";
            } else if (value.equalsIgnoreCase("Gray")) {
                value = "#808080";
            } else if (value.equalsIgnoreCase("Olive")) {
                value = "#808000";
            } else if (value.equalsIgnoreCase("White")) {
                value = "#FFFFFF";
            } else if (value.equalsIgnoreCase("Yellow")) {
                value = "#FFFF00";
            } else if (value.equalsIgnoreCase("Maroon")) {
                value = "#800000";
            } else if (value.equalsIgnoreCase("Navy")) {
                value = "#000080";
            } else if (value.equalsIgnoreCase("Red")) {
                value = "#FF0000";
            } else if (value.equalsIgnoreCase("Blue")) {
                value = "#0000FF";
            } else if (value.equalsIgnoreCase("Purple")) {
                value = "#800080";
            } else if (value.equalsIgnoreCase("Teal")) {
                value = "#008080";
            } else if (value.equalsIgnoreCase("Fuchsia")) {
                value = "#FF00FF";
            } else if (value.equalsIgnoreCase("Aqua")) {
                value = "#00FFFF";
            }

            if ((value.startsWith("#")) && (value.length() == 4)) {
                value = "#" + value.charAt(1) + "0" + value.charAt(2) + "0"
                        + value.charAt(3) + "0";
            }

            try {
                return new Color((Integer.decode(value)).intValue());
            } catch (final NumberFormatException nfe) {
                // Todo: Add waring, but only once!
                // m_log.warn("Cannot parse color: " + value);
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /** {@inheritDoc} */
    public Color getBackgroundColor() {
        final String colorString = this.getMathbackground();
        Color theColor;
        if (colorString == null) {
            if (this.getParent() != null) {
                theColor = this.getParent().getBackgroundColor();
            } else {
                theColor = null;
            }
        } else {
            theColor = this.stringToColor(colorString, null);
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
    public void debug(final Graphics2D g, final int posX, final int posY) {
        g.setColor(Color.blue);
        g.drawLine(posX, posY - this.getAscentHeight(g), posX
                + this.getWidth(g), posY - this.getAscentHeight(g));
        g.drawLine(posX + this.getWidth(g), posY - this.getAscentHeight(g),
                posX + this.getWidth(g), posY + this.getDescentHeight(g));
        g.drawLine(posX, posY + this.getDescentHeight(g), posX
                + this.getWidth(g), posY + this.getDescentHeight(g));
        g.drawLine(posX, posY - this.getAscentHeight(g), posX, posY
                + this.getDescentHeight(g));
        g.setColor(Color.red);
        g.drawLine(posX, posY, posX + this.getWidth(g), posY);
        g.setColor(Color.black);
    }

    /** {@inheritDoc} */
    public void setGlobalLineCorrector(final int corrector) {
        if (this.getParent() == null) {
            return;
        }

        // if this is a top-element of the row
        if (this instanceof MathTableRow
                || (this instanceof MathRow && this.getParent() instanceof MathTable)
                || (this.getParent() instanceof MathMathElement)) {
            if (this.globalLineCorrecter < corrector) {
                this.globalLineCorrecter = corrector;
            }
        } else {
            this.getParent().setGlobalLineCorrector(corrector);
        }
    }

    /** {@inheritDoc} */
    public int getGlobalLineCorrector() {
        if (this.getParent() == null) {
            return 0;
        }

        // if this is a top-element of the line, it contains the correct
        // number
        if (this instanceof MathTableRow
                || (this instanceof MathRow && this.getParent() instanceof MathTable)
                || this.getParent() instanceof MathMathElement) {
            return this.globalLineCorrecter;
        } else {
            return this.getParent().getGlobalLineCorrector();
        }
    }

    /** {@inheritDoc} */
    public int getWidth(final Graphics2D g) {
        if (this.calculatedWidth == -1) {
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
    abstract public int calculateWidth(Graphics2D g);

    /** {@inheritDoc} */
    public int getHeight(final Graphics2D g) {
        if (this.calculatingSize
                || ((this.getParent() != null) && (this.getParent()
                        .isCalculatingSize()))) {
            if (this.calculatedStretchHeight == -1) {
                this.calculatedStretchHeight = this.calculateHeight(g);
            }
            return this.calculatedStretchHeight;
        } else {
            if (this.calculatedHeight == -1) {
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
    public int calculateHeight(final Graphics2D g) {
        return this.getAscentHeight(g) + this.getDescentHeight(g);
    }

    /** {@inheritDoc} */
    public int getAscentHeight(final Graphics2D g) {
        if (this.calculatingSize
                || ((this.getParent() != null) && (this.getParent()
                        .isCalculatingSize()))) {
            if (this.calculatedStretchAscentHeight == -1) {
                this.calculatedStretchAscentHeight = this
                        .calculateAscentHeight(g);
            }
            return this.calculatedStretchAscentHeight;
        } else {
            if (this.calculatedAscentHeight == -1) {
                this.calculatedAscentHeight = this.calculateAscentHeight(g);
            }
            return this.calculatedAscentHeight;
        }
    }

    /** {@inheritDoc} */
    abstract public int calculateAscentHeight(Graphics2D g);

    /** {@inheritDoc} */
    public int getDescentHeight(final Graphics2D g) {
        if (this.calculatingSize
                || ((this.getParent() != null) && (this.getParent()
                        .isCalculatingSize()))) {
            if (this.calculatedStretchDescentHeight == -1) {
                this.calculatedStretchDescentHeight = this
                        .calculateDescentHeight(g);
            }
            return this.calculatedStretchDescentHeight;
        } else {
            if (this.calculatedDescentHeight == -1) {
                this.calculatedDescentHeight = this.calculateDescentHeight(g);
            }
            return this.calculatedDescentHeight;
        }
    }

    /** {@inheritDoc} */
    abstract public int calculateDescentHeight(Graphics2D g);

    /**
     * Returns the distance of the baseline and the middleline.
     * 
     * @return Distance baseline - middleline.
     * @param g
     *            Graphics2D context to use.
     */
    public int getMiddleShift(final Graphics2D g) {
        return (int) (this.getFontMetrics(g).getAscent() * 0.38);
    }

    /** {@inheritDoc} */
    public void eventElementComplete() {
    }

    /** {@inheritDoc} */
    public void eventAllElementsComplete() {
        final org.w3c.dom.NodeList childList = this.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            ((MathElement) childList.item(i)).eventAllElementsComplete();
        }
    }

    /** {@inheritDoc} */
    public void setMathAttributes(final AttributeMap attributes) {
        final Map attrsAsMap = attributes.getAsMap();
        for (final Iterator i = attrsAsMap.entrySet().iterator(); i.hasNext();) {
            final Map.Entry e = (Map.Entry) i.next();
            final String attrName = (String) e.getKey();
            if (AbstractMathElement.DEPRECATED_ATTRIBUTES.contains(attrName)) {
                AbstractMathElement.LOGGER.warn("Deprecated attribute for "
                        + this.getTagName() + ": " + attrName);
            }
            this.setAttribute(attrName, (String) e.getValue());
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
        return this.getAttribute("class");
    }

    /** {@inheritDoc} */
    public void setClassName(final String className) {
        this.setAttribute("class", className);
    }

    /** {@inheritDoc} */
    public String getMathElementStyle() {
        return this.getAttribute("style");
    }

    /** {@inheritDoc} */
    public void setMathElementStyle(final String mathElementStyle) {
        this.setAttribute("style", mathElementStyle);
    }

    /** {@inheritDoc} */
    public String getId() {
        return this.getAttribute("id");
    }

    /** {@inheritDoc} */
    public void setId(final String id) {
        this.setAttribute("id", id);
    }

    /** {@inheritDoc} */
    public String getXref() {
        return this.getAttribute("xref");
    }

    /** {@inheritDoc} */
    public void setXref(final String xref) {
        this.setAttribute("xref", xref);
    }

    /** {@inheritDoc} */
    public String getHref() {
        return this.getAttribute("xlink:href");
    }

    /** {@inheritDoc} */
    public void setHref(final String href) {
        this.setAttribute("xlink:href", href);
    }

    /** {@inheritDoc} */
    public MathMLMathElement getOwnerMathElement() {
        MathElement node = this.getParent();
        while (node != null) {
            if (node instanceof MathMLMathElement) {
                return (MathMLMathElement) node;
            }
            node = node.getParent();
        }
        return null;
    }

    /** {@inheritDoc} */
    public boolean isChildBlock(final MathElement child) {
        final MathElement parent = this.getParent();
        if (parent != null) {
            return parent.isChildBlock(this);
        } else {
            return true;
        }
    }

    /** {@inheritDoc} */
    public void paint(final Graphics2D g, final int posX, final int posY) {
        this.lastPaintedX = posX;
        this.lastPaintedY = posY;
        if (this.getBackgroundColor() != null) {
            g.setColor(this.getBackgroundColor());
            g.fillRect(posX, posY - this.getAscentHeight(g),
                    this.getWidth(g), this.getHeight(g));
        }

        if (this.getMathBase().isDebug()) {
            this.debug(g, posX, posY);
        }
        g.setColor(this.getForegroundColor());
        g.setFont(this.getFont());

    }

    /** {@inheritDoc} */
    public int getPaintedPosX() {
        return this.lastPaintedX;
    }

    /** {@inheritDoc} */
    public int getPaintedPosY() {
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

    {
        AbstractMathElement.DEPRECATED_ATTRIBUTES.add("color");
        AbstractMathElement.DEPRECATED_ATTRIBUTES.add("background");
        AbstractMathElement.DEPRECATED_ATTRIBUTES.add("fontsize");
        AbstractMathElement.DEPRECATED_ATTRIBUTES.add("fontweight");
        AbstractMathElement.DEPRECATED_ATTRIBUTES.add("fontstyle");
        AbstractMathElement.DEPRECATED_ATTRIBUTES.add("fontfamily");
    }
}

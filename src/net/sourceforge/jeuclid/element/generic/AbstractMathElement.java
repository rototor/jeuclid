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

/* $Id: AbstractMathElement.java,v 1.3.2.10 2007/01/31 22:50:27 maxberger Exp $ */

package net.sourceforge.jeuclid.element.generic;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.dom.ChangeTrackingElement;
import net.sourceforge.jeuclid.element.MathRootElement;
import net.sourceforge.jeuclid.element.MathRow;
import net.sourceforge.jeuclid.element.MathTable;
import net.sourceforge.jeuclid.element.MathTableRow;
import net.sourceforge.jeuclid.element.attributes.MathVariant;
import net.sourceforge.jeuclid.element.helpers.AttributeMap;
import net.sourceforge.jeuclid.element.helpers.AttributesHelper;
import net.sourceforge.jeuclid.element.helpers.CharConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLMathElement;

/**
 * The basic class for all math elements. Every element class inherits from this
 * class. It provides basic functionality for drawing.
 * 
 * @author Unknown
 * @author Max Berger
 */
public abstract class AbstractMathElement extends ChangeTrackingElement
        implements MathMLElement, DisplayableNode {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(AbstractMathElement.class);

    /**
     * flag - true when runing calculationg of the element.
     */
    private boolean calculatingSize = false;

    /**
     * The name of the element.
     */
    public static final String ELEMENT = "none";

    /**
     * The URI from MathML.
     */
    public static final String URI = "http://www.w3.org/1998/Math/MathML";

    private static final Set DEPRECATED_ATTRIBUTES = new HashSet();

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
    private AbstractMathElement fakeParent;

    /**
     * Variable of "scriptsize" attribute, default value is 0.71.
     */
    private float m_scriptsizemultiplier = (float) 0.71;

    /**
     * This variable is intended to keep the value of vertical shift of the
     * line. Actually this value is stored in the top-level element of the line.
     * This value affects only elements with enlarged parts (such as "msubsup",
     * "munderover", etc.)
     */
    private int globalLineCorrecter = 0;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */

    public AbstractMathElement(MathBase base) {
        setMathBase(base);
    }

    /**
     * Reset element sizes.
     */
    protected void recalculateSize() {
        calculatedHeight = -1;
        calculatedAscentHeight = -1;
        calculatedDescentHeight = -1;
        calculatedStretchHeight = -1;
        calculatedStretchAscentHeight = -1;
        calculatedStretchDescentHeight = -1;
        calculatedWidth = -1;
    }

    /** {@inheritDoc} */
    protected void setChanged(boolean hasChanged) {
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
        float scriptMultiplier = (float) Math.pow(getScriptSizeMultiplier(),
                this.getAbsoluteScriptLevel());
        float size = this.getMathsizeInPoint() * scriptMultiplier;

        // This results in a size 8 for a default size of 12.
        // TODO: This should use scriptminsize (3.3.4.2)
        float minSize = m_base.getFontSize() * 2 / 3;
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
        return getMathvariantAsVariant().createFont(this.getFontsizeInPoint(),
                'A');

    }

    /**
     * Gets the current mathvariant.
     * 
     * @return the current MathVariant
     */
    protected MathVariant getMathvariantAsVariant() {
        String mv = this.getMathvariant();
        MathVariant variant = null;
        if (mv != null) {
            variant = MathVariant.stringToMathVariant(mv);
        }
        if (variant == null) {
            // TODO: Not all elements inherit MathVariant!
            AbstractMathElement parent = this.getParent();
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
        AbstractMathElement parent = this.getParent();
        if (parent == null) {
            return 0;
        } else {
            return parent.getScriptlevelForChild(this);
        }
    }

    /**
     * Retrieves the scriptlevel for a certain child. Some attributes increase
     * the scriptlevel for some of their children.
     * 
     * @param child
     *            element node of the child.
     * @return the scriptlevel for this particular child.
     */
    protected int getScriptlevelForChild(AbstractMathElement child) {
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
    public final void addMathElement(AbstractMathElement child) {
        if (child != null) {
            this.appendChild(child);
            child.setMathBase(getMathBase());
        }
    }

    /**
     * Gets a child from this element.
     * 
     * @param index
     *            Index of the child.
     * @return The child MathElement object.
     */
    public AbstractMathElement getMathElement(int index) {
        org.w3c.dom.NodeList childList = this.getChildNodes();
        if ((index >= 0) && (index < childList.getLength())) {
            return (AbstractMathElement) childList.item(index);
        }
        return null;
    }

    /**
     * Gets index of child element.
     * 
     * @param element
     *            Child element.
     * @return Index of the element, -1 if element was not found
     */
    public int getIndexOfMathElement(AbstractMathElement element) {
        org.w3c.dom.NodeList childList = this.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            if (childList.item(i).equals(element)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the count of childs from this element.
     * 
     * @return Count of childs.
     */
    public int getMathElementCount() {
        return this.getChildNodes().getLength();
    }

    /**
     * Add the content of a String to this element.
     * 
     * @param text
     *            String with text of this object.
     */
    public void addText(String text) {

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

        String toSet = CharConverter.convert(newText.toString().trim());

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

    public void setMathBase(MathBase base) {
        m_base = base;
        org.w3c.dom.NodeList childList = this.getChildNodes();
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
        return m_base;
    }

    /**
     * Sets the parent of this element.
     * 
     * @param parent
     *            Parent element
     */
    public void setFakeParent(AbstractMathElement parent) {
        this.fakeParent = parent;
    }

    /**
     * Returns parent of this element.
     * 
     * @return Parent element.
     */
    public AbstractMathElement getParent() {
        final AbstractMathElement theParent = (AbstractMathElement) this
                .getParentNode();
        if (theParent == null) {
            return fakeParent;
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
    public void setMathvariant(String mathvariant) {
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
        return m_scriptsizemultiplier;
    }

    /**
     * Sets value of scriptsize attribute.
     * 
     * @param scriptsizemultiplier
     *            Value of scriptsize attribute.
     */
    public void setScriptSizeMultiplier(float scriptsizemultiplier) {
        m_scriptsizemultiplier = scriptsizemultiplier;
    }

    /**
     * Gets the font metrics of the used font.
     * 
     * @return Font metrics.
     * @param g
     *            Graphics context to use.
     */
    public FontMetrics getFontMetrics(Graphics g) {
        return g.getFontMetrics(getFont());
    }

    /**
     * Sets value of math color attribute.
     * 
     * @param mathcolor
     *            Color object.
     */
    public void setMathcolor(String mathcolor) {
        this.setAttribute("mathcolor", mathcolor);
    }

    /**
     * Returns value of mathcolor attribute.
     * 
     * @return Color as string.
     */
    public String getMathcolor() {
        String color;
        color = getMathAttribute("mathcolor");
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
        size = getMathAttribute("mathsize");
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
    public void setMathsize(String mathsize) {
        this.setAttribute("mathsize", mathsize);
    }

    /**
     * Get the actual mathsize in points.
     * 
     * @return mathsize in points.
     */
    protected float getMathsizeInPoint() {

        String msize = this.getMathsize();

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
    protected String getMathAttribute(String attrName) {
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
        color = this.getMathAttribute("mathbackground");
        if (color == null) {
            color = this.getMathAttribute("background");
        }
        return color;
    }

    /**
     * Gets the color that this element is supposed to use for rendering its
     * foreground elements.
     * 
     * @return a color.
     */
    protected Color getForegroundColor() {
        final String colorString = this.getMathcolor();
        Color theColor;
        if (colorString == null) {
            if (this.getParent() != null) {
                theColor = this.getParent().getForegroundColor();
            } else {
                theColor = Color.BLACK;
            }
        } else {
            theColor = stringToColor(colorString, Color.BLACK);
        }
        return theColor;
    }

    private Color stringToColor(String value, Color defaultValue) {

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
            } catch (NumberFormatException nfe) {
                // Todo: Add waring, but only once!
                // m_log.warn("Cannot parse color: " + value);
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Returns background color of the element.
     * 
     * @return Color object.
     */
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
            theColor = stringToColor(colorString, null);
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
    public void debug(Graphics g, int posX, int posY) {
        g.setColor(Color.blue);
        g.drawLine(posX, posY - getAscentHeight(g), posX + getWidth(g), posY
                - getAscentHeight(g));
        g.drawLine(posX + getWidth(g), posY - getAscentHeight(g), posX
                + getWidth(g), posY + getDescentHeight(g));
        g.drawLine(posX, posY + getDescentHeight(g), posX + getWidth(g), posY
                + getDescentHeight(g));
        g.drawLine(posX, posY - getAscentHeight(g), posX, posY
                + getDescentHeight(g));
        g.setColor(Color.red);
        g.drawLine(posX, posY, posX + getWidth(g), posY);
        g.setColor(Color.black);
    }

    /**
     * Sets value of the vertical shift for the specific elements in the line.
     * This applies to "munderover", "msubsup", "mover", etc.. In case such
     * elements containes enlarged operator, other elements on the right should
     * be positioned in the center of the line regarding such elements. Value of
     * the shift is stored in the top-level element of the line.
     * 
     * @param corrector
     *            Value of corrector.
     * @see #getGlobalLineCorrector()
     */
    public void setGlobalLineCorrector(int corrector) {
        if (getParent() == null) {
            return;
        }

        // if this is a top-element of the row
        if (this instanceof MathTableRow
                || (this instanceof MathRow && getParent() instanceof MathTable)
                || (getParent() instanceof MathRootElement)) {
            if (globalLineCorrecter < corrector) {
                globalLineCorrecter = corrector;
            }
        } else {
            getParent().setGlobalLineCorrector(corrector);
        }
    }

    /**
     * Returns value of the vertical shift for the specific elements in the
     * line. This applies to "munderover", "msubsup", "mover", etc.. In case
     * such elements containes enlarged operator, other elements on the right
     * should be positioned in the center of the line. Value of the shift is
     * stored in the top-level element of the line.
     * 
     * @return Value of the corrector of the line.
     * @see #setGlobalLineCorrector(int)
     */
    public int getGlobalLineCorrector() {
        if (getParent() == null) {
            return 0;
        }

        // if this is a top-element of the line, it contains the correct number
        if (this instanceof MathTableRow
                || (this instanceof MathRow && getParent() instanceof MathTable)
                || getParent() instanceof MathRootElement) {
            return globalLineCorrecter;
        } else {
            return getParent().getGlobalLineCorrector();
        }
    }

    /** {@inheritDoc} */
    public int getWidth(Graphics g) {
        if (calculatedWidth == -1) {
            calculatedWidth = calculateWidth(g);
        }
        return calculatedWidth;
    }

    /**
     * Caculates width of the element.
     * 
     * @return Width of the element.
     * @param g
     *            Graphics context to use.
     */
    abstract public int calculateWidth(Graphics g);

    /** {@inheritDoc} */
    public int getHeight(Graphics g) {
        if (calculatingSize
                || ((getParent() != null) && (getParent().calculatingSize))) {
            if (calculatedStretchHeight == -1) {
                calculatedStretchHeight = calculateHeight(g);
            }
            return calculatedStretchHeight;
        } else {
            if (calculatedHeight == -1) {
                calculatedHeight = calculateHeight(g);
            }
            return calculatedHeight;
        }
    }

    /**
     * Calculates the current height of the element.
     * 
     * @return Height of the element.
     * @param g
     *            Graphics context to use.
     */
    public int calculateHeight(Graphics g) {
        return getAscentHeight(g) + getDescentHeight(g);
    }

    /** {@inheritDoc} */
    public int getAscentHeight(Graphics g) {
        if (calculatingSize
                || ((getParent() != null) && (getParent().calculatingSize))) {
            if (calculatedStretchAscentHeight == -1) {
                calculatedStretchAscentHeight = calculateAscentHeight(g);
            }
            return calculatedStretchAscentHeight;
        } else {
            if (calculatedAscentHeight == -1) {
                calculatedAscentHeight = calculateAscentHeight(g);
            }
            return calculatedAscentHeight;
        }
    }

    /**
     * Returns the current height of the upper part (over the base line).
     * 
     * @return Height of the upper part.
     * @param g
     *            Graphics context to use.
     */
    abstract public int calculateAscentHeight(Graphics g);

    /** {@inheritDoc} */
    public int getDescentHeight(Graphics g) {
        if (calculatingSize
                || ((getParent() != null) && (getParent().calculatingSize))) {
            if (calculatedStretchDescentHeight == -1) {
                calculatedStretchDescentHeight = calculateDescentHeight(g);
            }
            return calculatedStretchDescentHeight;
        } else {
            if (calculatedDescentHeight == -1) {
                calculatedDescentHeight = calculateDescentHeight(g);
            }
            return calculatedDescentHeight;
        }
    }

    /**
     * Calculates descent height (under the base line) of the element.
     * 
     * @return Descent height value.
     * @param g
     *            Graphics context to use.
     */
    abstract public int calculateDescentHeight(Graphics g);

    /**
     * Returns the distance of the baseline and the middleline.
     * 
     * @return Distance baseline - middleline.
     * @param g
     *            Graphics context to use.
     */
    public int getMiddleShift(Graphics g) {
        return (int) ((float) getFontMetrics(g).getAscent() * 0.38);
    }

    /**
     * Methos is called, when all content of the element is known (i.e.
     * structure of the element, child elements). Warning: reference to the
     * mathbase class is still null here, so all related content (font, logger,
     * etc.) will be unavailable.
     */
    public void eventElementComplete() {
    }

    /**
     * This method is called, when all content of the element is known. In this
     * method elements are supposed to make all necessary size pre-calculations,
     * content examination and other font-related preparations.
     */
    public void eventAllElementsComplete() {
        org.w3c.dom.NodeList childList = this.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            ((AbstractMathElement) childList.item(i))
                    .eventAllElementsComplete();
        }
    }

    /**
     * This method is called, when all content of the element is known. (i.e.
     * child elements and text value). In this method is supposed to initialize
     * all specific attributes for current type of math element.
     * 
     * @param attributes
     *            List of attribute names and values.
     */
    public void eventInitSpecificAttributes(AttributeMap attributes) {
        Map attrsAsMap = attributes.getAsMap();
        for (Iterator i = attrsAsMap.entrySet().iterator(); i.hasNext();) {
            final Map.Entry e = (Map.Entry) i.next();
            final String attrName = (String) e.getKey();
            if (DEPRECATED_ATTRIBUTES.contains(attrName)) {
                // TODO: Add name of element
                logger.warn("Deprecated attribute " + attrName);
            }
            this.setAttribute(attrName, (String) e.getValue());
        }
        this.recalculateSize();
    }

    /**
     * @return the calculatingSize
     */
    public boolean isCalculatingSize() {
        return calculatingSize;
    }

    /**
     * @param calculatingSize
     *            the calculatingSize to set
     */
    public void setCalculatingSize(boolean calculatingSize) {
        this.calculatingSize = calculatingSize;
    }

    /** {@inheritDoc} */
    public String getClassName() {
        return this.getAttribute("class");
    }

    /** {@inheritDoc} */
    public void setClassName(String className) {
        this.setAttribute("class", className);
    }

    /** {@inheritDoc} */
    public String getMathElementStyle() {
        return this.getAttribute("style");
    }

    /** {@inheritDoc} */
    public void setMathElementStyle(String mathElementStyle) {
        this.setAttribute("style", mathElementStyle);
    }

    /** {@inheritDoc} */
    public String getId() {
        return this.getAttribute("id");
    }

    /** {@inheritDoc} */
    public void setId(String id) {
        this.setAttribute("id", id);
    }

    /** {@inheritDoc} */
    public String getXref() {
        return this.getAttribute("xref");
    }

    /** {@inheritDoc} */
    public void setXref(String xref) {
        this.setAttribute("xref", xref);
    }

    /** {@inheritDoc} */
    public String getHref() {
        return this.getAttribute("xlink:href");
    }

    /** {@inheritDoc} */
    public void setHref(String href) {
        this.setAttribute("xlink:href", href);
    }

    /** {@inheritDoc} */
    public MathMLMathElement getOwnerMathElement() {
        AbstractMathElement node = this.getParent();
        while (node != null) {
            if (node instanceof MathMLMathElement) {
                return (MathMLMathElement) node;
            }
            node = node.getParent();
        }
        return null;
    }

    /**
     * Returns true if the child should be displayed as a block (not inline).
     * Roughly corresponds to the "displaystyle" property (3.2.5.9)
     * 
     * @param child
     *            child to test
     * @return true if child is block.
     */
    protected boolean isChildBlock(AbstractMathElement child) {
        AbstractMathElement parent = this.getParent();
        if (parent != null) {
            return parent.isChildBlock(this);
        } else {
            return true;
        }
    }

    /** {@inheritDoc} */
    public void paint(Graphics g, int posX, int posY) {
        lastPaintedX = posX;
        lastPaintedY = posY;
        if (getBackgroundColor() != null) {
            g.setColor(getBackgroundColor());
            g.fillRect(posX, posY - getAscentHeight(g), getWidth(g),
                    getHeight(g));
        }

        if (getMathBase().isDebug()) {
            debug(g, posX, posY);
        }
        g.setColor(getForegroundColor());
        g.setFont(getFont());

    }

    /** {@inheritDoc} */
    public int getPaintedPosX() {
        return lastPaintedX;
    }

    /** {@inheritDoc} */
    public int getPaintedPosY() {
        return lastPaintedY;
    }

    {
        DEPRECATED_ATTRIBUTES.add("color");
        DEPRECATED_ATTRIBUTES.add("background");
        DEPRECATED_ATTRIBUTES.add("fontsize");
        DEPRECATED_ATTRIBUTES.add("fontweight");
        DEPRECATED_ATTRIBUTES.add("fontstyle");
        DEPRECATED_ATTRIBUTES.add("fontfamily");
    }
}

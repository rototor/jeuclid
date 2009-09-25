/*
 * Copyright 2002 - 2009 JEuclid, http://jeuclid.sf.net
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.StyleAttributeLayoutContext;
import net.sourceforge.jeuclid.elements.presentation.token.Mo;
import net.sourceforge.jeuclid.elements.presentation.token.Mtext;
import net.sourceforge.jeuclid.elements.support.ElementListSupport;
import net.sourceforge.jeuclid.elements.support.GraphicsSupport;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;
import net.sourceforge.jeuclid.elements.support.attributes.MathVariant;
import net.sourceforge.jeuclid.elements.support.text.TextContent;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutView;
import net.sourceforge.jeuclid.layout.LayoutableNode;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.GenericElementNS;
import org.apache.batik.dom.events.DOMMutationEvent;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLMathElement;
import org.w3c.dom.mathml.MathMLNodeList;

/**
 * The basic class for all math elements. Every element class inherits from this
 * class. It provides basic functionality for drawing.
 * 
 * @version $Revision$
 */
// CHECKSTYLE:OFF
public abstract class AbstractJEuclidElement extends
// CHECKSTYLE:ON
        GenericElementNS implements JEuclidElement {

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

    // /**
    // * Logger for this class
    // */
    // private static final Log LOGGER = LogFactory
    // .getLog(AbstractJEuclidElement.class);

    private static final Set<String> DEPRECATED_ATTRIBUTES = new HashSet<String>();

    /**
     * Reference to the element acting as parent if there is no parent.
     */
    private JEuclidElement fakeParent;

    private final Map<String, String> defaultMathAttributes = new HashMap<String, String>();

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public AbstractJEuclidElement(final String qname,
            final AbstractDocument odoc) {
        super(AbstractJEuclidElement.URI, qname, odoc);
    }

    /**
     * Constructor to explicitly set the namespace.
     * 
     * @param nsUri
     *            Namespace URI.
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public AbstractJEuclidElement(final String nsUri, final String qname,
            final AbstractDocument odoc) {
        super(nsUri, qname, odoc);
    }

    /**
     * Gets the used font. Everything regardes font, processed by MathBase
     * object.
     * 
     * @param context
     *            LayoutContext to use.
     * @return Font Font object.
     */
    public Font getFont(final LayoutContext context) {
        final String content = this.getText();
        final char aChar;
        if (content.length() > 0) {
            aChar = content.charAt(0);
        } else {
            aChar = 'A';
        }
        return this.getMathvariantAsVariant().createFont(
                GraphicsSupport.getFontsizeInPoint(context), aChar,
                this.applyLocalAttributesToContext(context), true);

    }

    /** {@inheritDoc} */
    public MathVariant getMathvariantAsVariant() {
        // TODO: Support deprecated variant names
        String setMv = this.getMathAttribute(
                AbstractJEuclidElement.ATTR_MATHVARIANT, false);

        JEuclidElement parent = this.getParent();
        while ((setMv == null) && (parent != null)) {
            // element is not set, try to inherit
            if (parent instanceof AbstractJEuclidElement) {
                setMv = ((AbstractJEuclidElement) parent).getMathAttribute(
                        AbstractJEuclidElement.ATTR_MATHVARIANT, false);
            }
            parent = parent.getParent();
        }
        if (setMv == null) {
            setMv = this.defaultMathAttributes
                    .get(AbstractJEuclidElement.ATTR_MATHVARIANT);
        }
        MathVariant variant;
        if (setMv == null) {
            variant = MathVariant.NORMAL;
        } else {
            variant = MathVariant.stringToMathVariant(setMv);
            if (variant == null) {
                variant = MathVariant.NORMAL;
            }
        }
        return variant;
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
        }
    }

    /**
     * Gets a child from this element.
     * <p>
     * Please note, that unlike the MathML DOM model functions this function
     * uses a 0-based index.
     * 
     * @param index
     *            Index of the child (0-based).
     * @return The child MathElement object.
     */
    protected JEuclidElement getMathElement(final int index) {
        final List<Node> childList = ElementListSupport
                .createListOfChildren(this);
        int count = 0;
        for (final Node n : childList) {
            if (n instanceof JEuclidElement) {
                if (count == index) {
                    return (JEuclidElement) n;
                }
                count++;
            }
        }
        for (; count < index; count++) {
            this.appendChild(this.ownerDocument.createElement(Mtext.ELEMENT));
        }
        final JEuclidElement last = (JEuclidElement) this.ownerDocument
                .createElement(Mtext.ELEMENT);
        this.appendChild(last);
        return last;
    }

    /**
     * Sets a specific child to the newElement, creating other subelements as
     * necessary.
     * 
     * @param index
     *            the index to set (0=the first child)
     * @param newElement
     *            new element to be set as child.
     */
    protected void setMathElement(final int index,
            final MathMLElement newElement) {
        final org.w3c.dom.NodeList childList = this.getChildNodes();
        while (childList.getLength() < index) {
            this.appendChild(this.getOwnerDocument().createTextNode(""));
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
        final List<Node> childList = ElementListSupport
                .createListOfChildren(this);
        int count = 0;
        for (final Node n : childList) {
            if (n instanceof JEuclidElement) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the text content of this element.
     * 
     * @return Text content.
     */
    public String getText() {
        return TextContent.getText(this);
    }

    /** {@inheritDoc} */
    public void setFakeParent(final JEuclidElement parent) {
        this.fakeParent = parent;
    }

    private JEuclidNode getParentAsJEuclidNode() {
        final Node parentNode = this.getParentNode();
        final JEuclidNode theParent;
        if (parentNode instanceof JEuclidNode) {
            theParent = (JEuclidNode) parentNode;
        } else {
            theParent = null;
        }
        if (theParent == null) {
            return this.fakeParent;
        } else {
            return theParent;
        }

    }

    /** {@inheritDoc} */
    public JEuclidElement getParent() {
        final JEuclidNode parentNode = this.getParentAsJEuclidNode();
        if (parentNode instanceof JEuclidElement) {
            return (JEuclidElement) parentNode;
        } else {
            return null;
        }
    }

    /**
     * Sets value of mathvariant attribute (style of the element).
     * 
     * @param mathvariant
     *            Value of mathvariant.
     */
    public void setMathvariant(final String mathvariant) {
        this.setAttribute(AbstractJEuclidElement.ATTR_MATHVARIANT, mathvariant);
    }

    /**
     * Returns value of mathvariant attribute (style of the element).
     * 
     * @return Value of mathvariant.
     */
    public String getMathvariant() {
        return this.getMathAttribute(AbstractJEuclidElement.ATTR_MATHVARIANT);
    }

    /**
     * Gets the font metrics of the used font.
     * 
     * @return Font metrics.
     * @param context
     *            LayoutContext to use.
     * @param g
     *            Graphics2D context to use.
     */
    public FontMetrics getFontMetrics(final Graphics2D g,
            final LayoutContext context) {
        return g.getFontMetrics(this.getFont(context));
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

    /**
     * Sets default values for math attributes. Default values are returned
     * through getMathAttribute, but not stored in the actual DOM tree. This is
     * necessary to support proper serialization.
     * 
     * @param key
     *            the attribute to set.
     * @param value
     *            value of the attribute.
     */
    protected void setDefaultMathAttribute(final String key, final String value) {
        this.defaultMathAttributes.put(key, value);
    }

    /**
     * retrieve an attribute from the MathML or default name space, returning
     * the default value if the attribute is not set.
     * 
     * @param attrName
     *            the name of the attribute
     * @return attribute value or null if not set.
     * @see #getMathAttribute(String, boolean)
     */
    protected String getMathAttribute(final String attrName) {
        return this.getMathAttribute(attrName, true);
    }

    /**
     * retrieve an attribute from the MathML or default name space.
     * 
     * @param attrName
     *            the name of the attribute
     * @param useDefault
     *            is true, the default value is used if the attribute is not
     *            set.
     * @return attribute value or null if not set.
     * @see #getMathAttribute(String)
     */
    protected String getMathAttribute(final String attrName,
            final boolean useDefault) {
        final String attrValue;
        Attr attr = this.getAttributeNodeNS(AbstractJEuclidElement.URI,
                attrName);
        if (attr == null) {
            attr = this.getAttributeNode(attrName);
        }
        if (attr == null) {
            if (useDefault) {
                attrValue = this.getDefaultMathAttribute(attrName);
            } else {
                attrValue = null;
            }
        } else {
            attrValue = attr.getValue().trim();
        }
        return attrValue;
    }

    /**
     * Retrieves the previously stored default value for this attribute.
     * 
     * @param attrName
     *            name of the Attribute
     * @return value set by {@link #setDefaultMathAttribute(String, String)} or
     *         null if not set.
     */
    private String getDefaultMathAttribute(final String attrName) {
        return this.defaultMathAttributes.get(attrName);
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

    /**
     * Returns the distance of the baseline and the middleline.
     * 
     * @return Distance baseline - middleline.
     * @param context
     *            Layout Context to use
     * @param g
     *            Graphics2D context to use.
     */
    public float getMiddleShift(final Graphics2D g, final LayoutContext context) {
        return this.getFontMetrics(g, context).getAscent()
                * AbstractJEuclidElement.MIDDLE_SHIFT;
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
        this.setAttribute(AbstractJEuclidElement.ATTR_STYLE, mathElementStyle);
    }

    /** {@inheritDoc} */
    @Override
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
    public boolean hasChildPrescripts(final JEuclidElement child) {
        return false;
    }

    /** {@inheritDoc} */
    public boolean hasChildPostscripts(final JEuclidElement child,
            final LayoutContext context) {
        return false;
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
    public LayoutContext getChildLayoutContext(final int childNum,
            final LayoutContext context) {
        return this.applyLocalAttributesToContext(context);
    }

    /**
     * Retrieve the LayoutContext valid for the current node.
     * 
     * @param context
     *            external context.
     * @return the current layout context.
     */
    public LayoutContext applyLocalAttributesToContext(
            final LayoutContext context) {
        // TODO: Theoretically this only applies all to presentation token
        // elements except mspace and mglyph, and on no other elements except
        // mstyle 3.2.2
        return this.applyStyleAttributes(context);
    }

    /**
     * Apply Style attributed specified in 3.2.2 to a layout context.
     * 
     * @param applyTo
     *            the context to apply to
     * @return a context which has the style attributes changed accordingly. May
     *         be the original context if nothing has changed.
     */
    private LayoutContext applyStyleAttributes(final LayoutContext applyTo) {
        LayoutContext retVal = applyTo;

        // Variant is not inherited and therefore not part of the context.

        final String msize = this.getMathsize();

        final Color foreground;
        final String colorString = this.getMathcolor();
        if (colorString == null) {
            foreground = null;
        } else {
            foreground = AttributesHelper.stringToColor(colorString,
                    Color.BLACK);
        }

        // Background is handled differently and does not need to go into
        // context.

        if ((msize != null) || (foreground != null)) {
            retVal = new StyleAttributeLayoutContext(applyTo, msize, foreground);
        }

        return retVal;
    }

    /** {@inheritDoc} */
    public List<LayoutableNode> getChildrenToLayout() {
        final List<LayoutableNode> l = ElementListSupport
                .createListOfLayoutChildren(this);
        return l;
    }

    /** {@inheritDoc} */
    public List<LayoutableNode> getChildrenToDraw() {
        final List<LayoutableNode> l = ElementListSupport
                .createListOfLayoutChildren(this);
        return l;
    }

    /**
     * Layout for elements which are stage independent.
     * <p>
     * This function will layout an element which is layed out the same no
     * matter what stage it is in. This is the case for most elements.
     * <p>
     * Notable exceptions are mo and tables.
     * 
     * @param view
     *            View Object for this layout.
     * @param info
     *            An info object which will be filled during layout.
     * @param stage
     *            current layout stage.
     * @param context
     *            current LayoutContext.
     */
    protected void layoutStageInvariant(final LayoutView view,
            final LayoutInfo info, final LayoutStage stage,
            final LayoutContext context) {
        ElementListSupport.layoutSequential(view, info, this
                .getChildrenToLayout(), stage);
    }

    /** {@inheritDoc} */
    public void layoutStage1(final LayoutView view, final LayoutInfo info,
            final LayoutStage childMinStage, final LayoutContext context) {
        this.layoutStageInvariant(view, info, LayoutStage.STAGE1, context);

        // TODO: This should be done in a better way.
        if (this.getMathbackground() == null) {
            info.setLayoutStage(childMinStage);
        } else {
            info.setLayoutStage(LayoutStage.STAGE1);
        }
    }

    /** {@inheritDoc} */
    public void layoutStage2(final LayoutView view, final LayoutInfo info,
            final LayoutContext context) {
        this.layoutStageInvariant(view, info, LayoutStage.STAGE2, context);

        // TODO: put in own function, ensure this is also called from
        // subclasses.
        final String background = this.getMathbackground();
        final Color backgroundColor = AttributesHelper.stringToColor(
                background, null);
        ElementListSupport.addBackground(backgroundColor, info, false);
        info.setLayoutStage(LayoutStage.STAGE2);
    }

    static {
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

        AbstractJEuclidElement.DEPRECATED_ATTRIBUTES.add(Mo.ATTR_MOVEABLEWRONG);
    }

    /**
     * Override this function to get notified whenever the contents of this
     * element have changed.
     */
    protected void changeHook() {
        // Override me!
    }

    /** {@inheritDoc} */
    @Override
    public boolean dispatchEvent(final Event evt) {
        if (evt instanceof DOMMutationEvent) {
            this.changeHook();
        }
        return super.dispatchEvent(evt);
    }
}

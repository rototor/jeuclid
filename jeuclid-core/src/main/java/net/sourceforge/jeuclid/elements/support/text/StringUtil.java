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

package net.sourceforge.jeuclid.elements.support.text;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.elements.support.GraphicsSupport;
import net.sourceforge.jeuclid.elements.support.attributes.MathVariant;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utilities for String handling.
 * 
 * @version $Revision$
 */
// CHECKSTYLE:OFF
// Data Abstraction Coupling is too high. Hover, String handling is not
// simple.
public final class StringUtil {
    // CHECKSTYLE:ON

    /**
     * Set to true if we're running under Mac OS X.
     */
    public static final boolean OSX = System.getProperty("mrj.version") != null; //$NON-NLS-1$

    static final CharacterMapping CMAP = CharacterMapping.getInstance();

    private StringUtil() {
        // do nothing
    }

    /**
     * Converts a given String to an attributed string with the proper variants
     * set.
     * 
     * @param inputString
     *            the string to convert.
     * @param baseVariant
     *            variant to base on for regular characters
     * @param fontSize
     *            size of Font to use.
     * @param context
     *            Layout Context to use.
     * @return an attributed string that has Textattribute.FONT set for all
     *         characters.
     */
    public static AttributedString convertStringtoAttributedString(
            final String inputString, final MathVariant baseVariant,
            final float fontSize, final LayoutContext context) {
        if (inputString == null) {
            return new AttributedString("");
        }
        final StringBuilder builder = new StringBuilder();
        final List<Font> fonts = new ArrayList<Font>();
        final String plainString = CharConverter.convertLate(inputString);

        for (int i = 0; i < plainString.length(); i++) {
            if (!Character.isLowSurrogate(plainString.charAt(i))) {

                final CodePointAndVariant cpav1 = new CodePointAndVariant(
                        plainString.codePointAt(i), baseVariant);
                final Object[] codeAndFont = StringUtil.mapCpavToCpaf(cpav1,
                        fontSize, context);
                final int codePoint = (Integer) codeAndFont[0];
                final Font font = (Font) codeAndFont[1];

                builder.appendCodePoint(codePoint);
                fonts.add(font);
                if (Character.isSupplementaryCodePoint(codePoint)) {
                    fonts.add(font);
                }
            }
        }

        final AttributedString aString = new AttributedString(builder
                .toString());

        final int len = builder.length();

        for (int i = 0; i < len; i++) {
            final char currentChar = builder.charAt(i);
            if (!Character.isLowSurrogate(currentChar)) {
                final Font font = fonts.get(i);
                final int count;
                if (Character.isHighSurrogate(currentChar)) {
                    count = 2;
                } else {
                    count = 1;
                }
                aString.addAttribute(TextAttribute.FONT, font, i, i + count);
            }
        }
        return aString;
    }

    /**
     * Provide the text content of the current element as
     * AttributedCharacterIterator.
     * 
     * @param contextNow
     *            LayoutContext of the parent element.
     * @param contextElement
     *            Parent Element.
     * @param node
     *            Current node.
     * @param corrector
     *            Font-size corrector.
     * @return An {@link AttributedCharacterIterator} over the text contents.
     */
    public static AttributedCharacterIterator textContentAsAttributedCharacterIterator(
            final LayoutContext contextNow,
            final JEuclidElement contextElement, final Node node,
            final float corrector) {
        AttributedCharacterIterator retVal;
        if (node instanceof Element) {

            final MultiAttributedCharacterIterator maci = new MultiAttributedCharacterIterator();
            final NodeList children = node.getChildNodes();
            AttributedCharacterIterator aci = null;
            final int childCount = children.getLength();
            for (int i = 0; i < childCount; i++) {
                final LayoutContext subContext;
                final Node child = children.item(i);
                final JEuclidElement subContextElement;
                if (child instanceof AbstractJEuclidElement) {
                    subContext = ((AbstractJEuclidElement) child)
                            .applyLocalAttributesToContext(contextNow);
                    subContextElement = (JEuclidElement) child;
                } else {
                    subContext = contextNow;
                    subContextElement = contextElement;
                }
                aci = StringUtil.textContentAsAttributedCharacterIterator(
                        subContext, subContextElement, child, corrector);
                maci.appendAttributedCharacterIterator(aci);
            }

            if (childCount != 1) {
                aci = maci;
            }

            if (node instanceof TextContentModifier) {
                final TextContentModifier t = (TextContentModifier) node;
                retVal = t.modifyTextContent(aci, contextNow);
            } else {
                retVal = aci;
            }
        } else {
            final String theText = TextContent.getText(node);
            final float fontSizeInPoint = GraphicsSupport
                    .getFontsizeInPoint(contextNow)
                    * corrector;

            retVal = StringUtil.convertStringtoAttributedString(theText,
                    contextElement.getMathvariantAsVariant(), fontSizeInPoint,
                    contextNow).getIterator();
        }
        return retVal;
    }

    private static Object[] mapCpavToCpaf(final CodePointAndVariant cpav1,
            final float fontSize, final LayoutContext context) {
        final List<CodePointAndVariant> alternatives = StringUtil.CMAP
                .getAllAlternatives(cpav1);

        Font font = null;
        int codePoint = 0;
        final Iterator<CodePointAndVariant> it = alternatives.iterator();
        boolean cont = true;
        while (cont) {
            final CodePointAndVariant cpav = it.next();
            if (it.hasNext()) {
                codePoint = cpav.getCodePoint();
                font = cpav.getVariant().createFont(fontSize, codePoint,
                        context, false);
                if (font != null) {
                    cont = false;
                }
            } else {
                codePoint = cpav.getCodePoint();
                font = cpav.getVariant().createFont(fontSize, codePoint,
                        context, true);
                cont = false;
            }
        }
        return new Object[] { codePoint, font };
    }

    /**
     * Safely creates a Text Layout from an attributed string. Unlike the
     * TextLayout constructor, the String here may actually be empty.
     * 
     * @param g
     *            Graphics context.
     * @param aString
     *            an Attributed String
     * @param context
     *            Layout Context to use.
     * @return a TextLayout
     */
    public static TextLayout createTextLayoutFromAttributedString(
            final Graphics2D g, final AttributedString aString,
            final LayoutContext context) {
        final AttributedCharacterIterator charIter = aString.getIterator();
        final boolean empty = charIter.first() == CharacterIterator.DONE;
        final FontRenderContext suggestedFontRenderContext = g
                .getFontRenderContext();
        boolean antialiasing = (Boolean) context
                .getParameter(Parameter.ANTIALIAS);
        if (!empty) {
            final Font font = (Font) aString.getIterator().getAttribute(
                    TextAttribute.FONT);
            if (font != null) {
                final float fontsize = font.getSize2D();
                final float minantialias = (Float) context
                        .getParameter(Parameter.ANTIALIAS_MINSIZE);
                antialiasing &= fontsize >= minantialias;
            }
        }

        final FontRenderContext realFontRenderContext = new FontRenderContext(
                suggestedFontRenderContext.getTransform(), antialiasing, false);

        final TextLayout theLayout;
        if (empty) {
            theLayout = new TextLayout(" ", new Font("", 0, 0),
                    realFontRenderContext);
        } else {
            synchronized (TextLayout.class) {
                // Catches a rare NullPointerException in
                // sun.font.FileFontStrike.getCachedGlyphPtr(FileFontStrike.java:448)
                theLayout = new TextLayout(aString.getIterator(),
                        realFontRenderContext);
            }
        }
        return theLayout;
    }

    /**
     * Retrieves the real width from a given text layout.
     * 
     * @param layout
     *            the textlayout
     * @return width
     */
    public static float getWidthForTextLayout(final TextLayout layout) {
        final Rectangle2D r2d = layout.getBounds();
        float realWidth = (float) r2d.getWidth();
        final float xo = (float) r2d.getX();
        if (xo > 0) {
            realWidth += xo;
        }
        // Unfortunately this is necessary, although it does not look like it
        // makes a lot of sense.
        final float invisibleAdvance = layout.getAdvance()
                - layout.getVisibleAdvance();
        return realWidth + invisibleAdvance;
    }

    /**
     * Contains layout information retrieved from a TextLayout.
     */
    public static class TextLayoutInfo {
        private final float ascent;

        private final float descent;

        private final float offset;

        private final float width;

        /**
         * Default Constructor.
         * 
         * @param newAscent
         *            text ascent.
         * @param newDescent
         *            text descent.
         * @param newOffset
         *            text start offset.
         * @param newWidth
         *            text width.
         */
        protected TextLayoutInfo(final float newAscent, final float newDescent,
                final float newOffset, final float newWidth) {
            this.ascent = newAscent;
            this.descent = newDescent;
            this.offset = newOffset;
            this.width = newWidth;
        }

        /**
         * Getter method for ascent.
         * 
         * @return the ascent
         */
        public float getAscent() {
            return this.ascent;
        }

        /**
         * Getter method for descent.
         * 
         * @return the descent
         */
        public float getDescent() {
            return this.descent;
        }

        /**
         * Getter method for offset.
         * 
         * @return the offset
         */
        public float getOffset() {
            return this.offset;
        }

        /**
         * Getter method for width.
         * 
         * @return the width
         */
        public float getWidth() {
            return this.width;
        }

    };

    /**
     * Retrieve the actual layout information from a textLayout. This is
     * different than the values given when calling the functions directly.
     * 
     * @param textLayout
     *            TextLayout to look at.
     * @param trim
     *            Trim to actual content
     * @return a TextLayoutInfo.
     */
    public static TextLayoutInfo getTextLayoutInfo(final TextLayout textLayout,
            final boolean trim) {
        final Rectangle2D textBounds = textLayout.getBounds();
        final float ascent = (float) (-textBounds.getY());
        final float descent = (float) (textBounds.getY() + textBounds
                .getHeight());
        final float xo = (float) textBounds.getX();
        final float xOffset;
        if (xo < 0) {
            xOffset = -xo;
        } else {
            if (trim) {
                xOffset = -xo;
            } else {
                xOffset = 0.0f;
            }
        }
        final float width = StringUtil.getWidthForTextLayout(textLayout);
        return new TextLayoutInfo(ascent, descent, xOffset, width);
    }

}

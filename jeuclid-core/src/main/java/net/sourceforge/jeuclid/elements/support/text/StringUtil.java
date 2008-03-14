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
import java.util.List;
import java.util.Vector;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.LayoutContext.Parameter;
import net.sourceforge.jeuclid.elements.support.attributes.MathVariant;

/**
 * Utilities for String handling.
 * 
 * @version $Revision$
 */
public final class StringUtil {

    /**
     * Set to true if we're running under Mac OS X.
     */
    public static final boolean OSX = System.getProperty("mrj.version") != null; //$NON-NLS-1$

    static final CharacterMapping CMAP = CharacterMapping.getInstance();

    private StringUtil() {
        // do nothing
    }

    /**
     * Converts a given String to an attributed string with the proper
     * variants set.
     * 
     * @param inputString
     *            the string to convert.
     * @param baseVariant
     *            variant to base on for regular characters
     * @param fontSize
     *            size of Font to use.
     * @param context
     *            Layotu Context to use.
     * @return an attributed string that has Textattribute.FONT set for all
     *         characters.
     */
    public static AttributedString convertStringtoAttributedString(
            final String inputString, final MathVariant baseVariant,
            final float fontSize, final LayoutContext context) {
        final StringBuilder builder = new StringBuilder();
        final List<MathVariant> variants = new Vector<MathVariant>();
        final String plainString = CharConverter.convertLate(inputString);

        for (int i = 0; i < plainString.length(); i++) {
            if (!Character.isLowSurrogate(plainString.charAt(i))) {

                CodePointAndVariant cpav = new CodePointAndVariant(
                        plainString.codePointAt(i), baseVariant);

                cpav = StringUtil.CMAP.extractUnicodeAttr(cpav);

                // High Plane is broken on OS X!
                cpav = StringUtil.CMAP.composeUnicodeChar(cpav,
                        StringUtil.OSX);

                final int codePoint = cpav.getCodePoint();
                final MathVariant variant = cpav.getVariant();

                builder.appendCodePoint(codePoint);
                variants.add(variant);
                if (Character.isSupplementaryCodePoint(codePoint)) {
                    variants.add(variant);
                }
            }
        }

        final AttributedString aString = new AttributedString(builder
                .toString());

        final int len = builder.length();

        for (int i = 0; i < len; i++) {
            final char currentChar = builder.charAt(i);
            if (!Character.isLowSurrogate(currentChar)) {
                final MathVariant variant = variants.get(i);
                final int count;
                if (Character.isHighSurrogate(currentChar)) {
                    count = 2;
                } else {
                    count = 1;
                }
                aString.addAttribute(TextAttribute.FONT, variant.createFont(
                        fontSize, builder.codePointAt(i), context), i, i
                        + count);
            }
        }
        return aString;
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
                suggestedFontRenderContext.getTransform(), antialiasing,
                false);

        final TextLayout theLayout;
        if (!empty) {
            theLayout = new TextLayout(aString.getIterator(),
                    realFontRenderContext);
        } else {
            theLayout = new TextLayout(" ", new Font("", 0, 0),
                    realFontRenderContext);
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
        protected TextLayoutInfo(final float newAscent,
                final float newDescent, final float newOffset,
                final float newWidth) {
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
    public static TextLayoutInfo getTextLayoutInfo(
            final TextLayout textLayout, final boolean trim) {
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

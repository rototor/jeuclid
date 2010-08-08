/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.elements.presentation.general;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.List;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.support.GraphicsSupport;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;
import net.sourceforge.jeuclid.layout.GraphicsObject;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutView;
import net.sourceforge.jeuclid.layout.LayoutableNode;
import net.sourceforge.jeuclid.layout.LineObject;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.mathml.MathMLRadicalElement;

/**
 * common superclass for root like elements (root, sqrt).
 * 
 * @version $Revision$
 */
public abstract class AbstractRoot extends AbstractJEuclidElement implements
        MathMLRadicalElement {

    private static final String EXTRA_SPACE = "0.1ex";

    private static final String ROOT_WIDTH = "0.5em";

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public AbstractRoot(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);
    }

    /**
     * retrieve the content of this radical element.
     * 
     * @return A List&lt;MathElement&gt; with the contents for this element.
     */
    protected abstract List<LayoutableNode> getContent();

    /** {@inheritDoc} */
    // CHECKSTYLE:OFF
    // This function is too long, but it depends on too many parameters.
    @Override
    protected void layoutStageInvariant(final LayoutView view,
            final LayoutInfo info, final LayoutStage stage,
            final LayoutContext context) {
        // CHECKSTYLE:ON

        // Basic Calculations
        final Graphics2D g = view.getGraphics();
        final LayoutContext now = this.applyLocalAttributesToContext(context);
        final float middleShift = this.getMiddleShift(g, context);
        final float linethickness = GraphicsSupport.lineWidth(now);
        final float extraSpace = AttributesHelper.convertSizeToPt(
                AbstractRoot.EXTRA_SPACE, now, "");
        final float rootwidth = AttributesHelper.convertSizeToPt(
                AbstractRoot.ROOT_WIDTH, context, "");
        final Color color = (Color) now.getParameter(Parameter.MATHCOLOR);
        float xPos = linethickness;
        final LayoutableNode index = (LayoutableNode) this.getIndex();
        final List<GraphicsObject> graphicObjects = info.getGraphicObjects();
        graphicObjects.clear();

        // Draw Index
        float indexAscent;
        if (index == null) {
            indexAscent = 0.0f;
        } else {
            final LayoutInfo indexInfo = view.getInfo(index);
            final float indexPos = middleShift + linethickness / 2.0f
                    + extraSpace + indexInfo.getDescentHeight(stage);
            indexInfo.moveTo(xPos, -indexPos, stage);
            xPos += indexInfo.getWidth(stage);
            graphicObjects.add(new LineObject(linethickness, -middleShift,
                    xPos, -middleShift, linethickness, color));
            indexAscent = indexPos + indexInfo.getAscentHeight(stage);
        }

        // Skip Root Space
        xPos += rootwidth;

        // Draw Content below Root
        final float contentStartX = xPos;
        final FontMetrics metrics = this
                .getFontMetrics(view.getGraphics(), now);
        float maxAscent = metrics.getAscent();
        float maxDescent = metrics.getDescent();
        for (final LayoutableNode child : this.getContent()) {
            final LayoutInfo childInfo = view.getInfo(child);
            childInfo.moveTo(xPos, 0, stage);
            maxAscent = Math.max(maxAscent, childInfo.getAscentHeight(stage));
            maxDescent = Math
                    .max(maxDescent, childInfo.getDescentHeight(stage));
            xPos += childInfo.getWidth(stage);
        }
        xPos += 2 * extraSpace;
        final float topLinePos = maxAscent + 2 * extraSpace + linethickness
                / 2.0f;

        // Fill in Info
        info.setAscentHeight(Math.max(topLinePos + linethickness / 2.0f,
                indexAscent), stage);
        info.setDescentHeight(maxDescent + linethickness / 2.0f, stage);
        info.setHorizontalCenterOffset(xPos / 2.0f, stage);
        info.setWidth(xPos + linethickness, stage);
        info.setStretchAscent(maxAscent);
        info.setStretchDescent(maxDescent);

        // Add Root Glyph
        graphicObjects.add(new LineObject(contentStartX - rootwidth,
                -middleShift, contentStartX - rootwidth / 2.0f, maxDescent,
                linethickness, color));
        graphicObjects.add(new LineObject(contentStartX - rootwidth / 2.0f,
                maxDescent, contentStartX, -topLinePos, linethickness, color));
        graphicObjects.add(new LineObject(contentStartX, -topLinePos, xPos,
                -topLinePos, linethickness, color));
    }
}

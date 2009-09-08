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

package net.sourceforge.jeuclid.elements.support;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jeuclid.layout.FillRectObject;
import net.sourceforge.jeuclid.layout.GraphicsObject;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutView;
import net.sourceforge.jeuclid.layout.LayoutableNode;

import org.w3c.dom.Node;

/**
 * Class to support Lists of MathElements.
 * <p>
 * This class can be used by all elements that have some kind of a list of
 * children that they need to handle in a row-like manner.
 * 
 * @version $Revision$
 */
public final class ElementListSupport {

    private ElementListSupport() {
        // Utility class.
    }

    /**
     * Creates a list of children for the given Element.
     * 
     * @param parent
     *            the parent element.
     * @return list of Children.
     */
    public static List<Node> createListOfChildren(final Node parent) {
        final org.w3c.dom.NodeList childList = parent.getChildNodes();
        final int len = childList.getLength();
        final List<Node> children = new ArrayList<Node>(len);
        for (int i = 0; i < len; i++) {
            final Node child = childList.item(i);
            children.add(child);
        }
        return children;

    }

    /**
     * Creates a list of layoutable children for the given Element.
     * 
     * @param parent
     *            the parent element.
     * @return list of Children.
     */
    public static List<LayoutableNode> createListOfLayoutChildren(
            final Node parent) {
        final org.w3c.dom.NodeList childList = parent.getChildNodes();
        final int len = childList.getLength();
        final List<LayoutableNode> children = new ArrayList<LayoutableNode>(len);
        for (int i = 0; i < len; i++) {
            final Node child = childList.item(i);
            if (child instanceof LayoutableNode) {
                children.add((LayoutableNode) child);
            }
        }
        return children;

    }

    /**
     * @param view
     *            View Object
     * @param info
     *            Info to fill
     * @param parent
     *            Current Node
     * @param stage
     *            Stage to load Info From
     * @param borderLeftTop
     *            border around element.
     * @param borderRightBottom
     *            border around element.
     */
    public static void fillInfoFromChildren(final LayoutView view,
            final LayoutInfo info, final Node parent, final LayoutStage stage,
            final Dimension2D borderLeftTop, final Dimension2D borderRightBottom) {
        float ascentHeight = (float) borderLeftTop.getHeight();
        float descentHeight = (float) borderRightBottom.getHeight();
        final float startX = (float) borderLeftTop.getWidth();
        float width = startX;
        for (final LayoutableNode child : ElementListSupport
                .createListOfLayoutChildren(parent)) {
            final LayoutInfo childInfo = view.getInfo(child);
            ascentHeight = Math.max(ascentHeight, -childInfo.getPosY(stage)
                    + childInfo.getAscentHeight(stage));
            descentHeight = Math.max(descentHeight, childInfo.getPosY(stage)
                    + childInfo.getDescentHeight(stage));
            width = Math.max(width, childInfo.getPosX(stage)
                    + childInfo.getWidth(stage));
        }
        info.setAscentHeight(ascentHeight + (float) borderLeftTop.getHeight(),
                stage);
        info.setDescentHeight(descentHeight
                + (float) borderRightBottom.getHeight(), stage);
        info.setHorizontalCenterOffset((width + startX) / 2.0f, stage);
        info.setWidth(width + (float) borderRightBottom.getWidth(), stage);
    }

    /**
     * @param view
     *            View Object
     * @param info
     *            Info to fill
     * @param children
     *            Children to layout
     * @param stage
     *            Stage to load Info From
     */
    public static void layoutSequential(final LayoutView view,
            final LayoutInfo info, final List<LayoutableNode> children,
            final LayoutStage stage) {
        float ascentHeight = 0.0f;
        float descentHeight = 0.0f;
        float posX = 0.0f;
        float stretchAscent = 0.0f;
        float stretchDescent = 0.0f;

        for (final LayoutableNode child : children) {
            final LayoutInfo childInfo = view.getInfo(child);
            ascentHeight = Math.max(ascentHeight, childInfo
                    .getAscentHeight(stage));
            descentHeight = Math.max(descentHeight, childInfo
                    .getDescentHeight(stage));
            stretchAscent = Math.max(stretchAscent, childInfo
                    .getStretchAscent());
            stretchDescent = Math.max(stretchDescent, childInfo
                    .getStretchDescent());
            childInfo.moveTo(posX, 0.0f, stage);
            posX += childInfo.getWidth(stage);
        }
        info.setAscentHeight(ascentHeight, stage);
        info.setDescentHeight(descentHeight, stage);
        info.setStretchAscent(stretchAscent);
        info.setStretchDescent(stretchDescent);
        info.setHorizontalCenterOffset(posX / 2.0f, stage);
        info.setWidth(posX, stage);
    }

    /**
     * Add a background Rectangle for the given background color.
     * 
     * @param backgroundColor
     *            background color (may be null)
     * @param info
     *            LayoutInfo object to add to. Must already be completely
     *            rendered (stage 2)
     * @param useCeil
     *            if true, the {@link Math#ceil(double)} will be used to avoid
     *            anti-aliasing artifacts.
     */
    public static void addBackground(final Color backgroundColor,
            final LayoutInfo info, final boolean useCeil) {
        if (backgroundColor != null) {
            final GraphicsObject fillObject;
            if (useCeil) {
                fillObject = new FillRectObject(backgroundColor, (float) Math
                        .ceil(info.getAscentHeight(LayoutStage.STAGE2)),
                        (float) Math.ceil(info
                                .getDescentHeight(LayoutStage.STAGE2)),
                        (float) Math.ceil(info.getWidth(LayoutStage.STAGE2)));
            } else {
                fillObject = new FillRectObject(backgroundColor, info
                        .getAscentHeight(LayoutStage.STAGE2), info
                        .getDescentHeight(LayoutStage.STAGE2), info
                        .getWidth(LayoutStage.STAGE2));

            }

            info.getGraphicObjects().add(0, fillObject);
        }

    }
}

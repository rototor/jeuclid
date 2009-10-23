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

package net.sourceforge.jeuclid.layout;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.elements.presentation.token.Mo;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 * @version $Revision$
 */
public class JEuclidView implements AbstractView, LayoutView, EventListener {

    private final LayoutableDocument document;

    private final Map<Node, LayoutInfo> layoutMap;

    private final LayoutContext context;

    private final Graphics2D graphics;

    /**
     * Default Constructor.
     * 
     * @param node
     *            document to layout.
     * @param layoutGraphics
     *            Graphics context to use for layout calculations. This should
     *            be compatible to the context used for painting, but does not
     *            have to be the same.
     * @param layoutContext
     *            layoutContext to use.
     */
    public JEuclidView(final Node node, final LayoutContext layoutContext,
            final Graphics2D layoutGraphics) {
        if (node instanceof LayoutableDocument) {
            this.document = (LayoutableDocument) node;
        } else {
            this.document = DOMBuilder.getInstance().createJeuclidDom(node);
        }
        this.graphics = layoutGraphics;
        this.context = layoutContext;
        this.layoutMap = new HashMap<Node, LayoutInfo>();
    }

    /** {@inheritDoc} */
    public DocumentView getDocument() {
        return this.document;
    }

    /**
     * Draw this view onto a Graphics context.
     * 
     * @param x
     *            x-offset for left edge
     * @param y
     *            y-offset for baseline
     * @param g
     *            Graphics context for painting. Should be compatible to the
     *            context used during construction, but does not have to be the
     *            same.
     */
    public void draw(final Graphics2D g, final float x, final float y) {
        this.layout();
        final RenderingHints hints = g.getRenderingHints();
        if ((Boolean) (this.context.getParameter(Parameter.ANTIALIAS))) {
            hints.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON));
        }
        hints.add(new RenderingHints(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_NORMALIZE));
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY));
        g.setRenderingHints(hints);

        final boolean debug = (Boolean) (this.context
                .getParameter(Parameter.DEBUG));
        this.drawNode(this.document, g, x, y, debug);

    }

    private void drawNode(final LayoutableNode node, final Graphics2D g,
            final float x, final float y, final boolean debug) {

        final LayoutInfo myInfo = this.getInfo(node);
        if (debug) {
            final float x1 = x;
            final float x2 = x + myInfo.getWidth(LayoutStage.STAGE2);
            final float y1 = y - myInfo.getAscentHeight(LayoutStage.STAGE2);
            final float y2 = y + myInfo.getDescentHeight(LayoutStage.STAGE2);
            g.setColor(Color.BLUE);
            g.draw(new Line2D.Float(x1, y1, x2, y1));
            g.draw(new Line2D.Float(x1, y1, x1, y2));
            g.draw(new Line2D.Float(x2, y1, x2, y2));
            g.draw(new Line2D.Float(x1, y2, x2, y2));
            g.setColor(Color.RED);
            g.draw(new Line2D.Float(x1, y, x2, y));
        }
        for (final GraphicsObject go : myInfo.getGraphicObjects()) {
            go.paint(x, y, g);
        }

        for (final LayoutableNode child : node.getChildrenToDraw()) {
            final LayoutInfo childInfo = this.getInfo(child);
            this.drawNode(child, g, x + childInfo.getPosX(LayoutStage.STAGE2),
                    y + childInfo.getPosY(LayoutStage.STAGE2), debug);
        }
    }

    private LayoutInfo layout() {
        return this.layout(this.document, LayoutStage.STAGE2, this.context);
    }

    private LayoutInfo layout(final LayoutableNode node,
            final LayoutStage toStage, final LayoutContext parentContext) {
        final LayoutInfo info = this.getInfo(node);

        if (node instanceof EventTarget) {
            final EventTarget evtNode = (EventTarget) node;
            evtNode.addEventListener("DOMSubtreeModified", this, false);
            evtNode.addEventListener(Mo.MOEVENT, this, false);
        }

        if (LayoutStage.NONE.equals(info.getLayoutStage())) {
            LayoutStage childMinStage = LayoutStage.STAGE2;
            int count = 0;
            for (final LayoutableNode l : node.getChildrenToLayout()) {
                final LayoutInfo in = this.layout(l, LayoutStage.STAGE1, node
                        .getChildLayoutContext(count, parentContext));
                count++;
                if (LayoutStage.STAGE1.equals(in.getLayoutStage())) {
                    childMinStage = LayoutStage.STAGE1;
                }
            }
            node.layoutStage1(this, info, childMinStage, parentContext);
        }
        if (LayoutStage.STAGE1.equals(info.getLayoutStage())
                && LayoutStage.STAGE2.equals(toStage)) {
            int count = 0;
            for (final LayoutableNode l : node.getChildrenToLayout()) {
                this.layout(l, LayoutStage.STAGE2, node.getChildLayoutContext(
                        count, parentContext));
                count++;
            }
            node.layoutStage2(this, info, parentContext);
        }
        return info;
    }

    /** {@inheritDoc} */
    public LayoutInfo getInfo(final LayoutableNode node) {
        if (node == null) {
            return null;
        }
        LayoutInfo info = this.layoutMap.get(node);
        if (info == null) {
            info = new LayoutInfoImpl();
            this.layoutMap.put(node, info);
        }
        return info;
    }

    /**
     * @return width of this view.
     */
    public float getWidth() {
        final LayoutInfo info = this.layout();
        return info.getWidth(LayoutStage.STAGE2);
    }

    /**
     * @return ascent height.
     */
    public float getAscentHeight() {
        final LayoutInfo info = this.layout();
        return info.getAscentHeight(LayoutStage.STAGE2);
    }

    /**
     * @return descent height.
     */
    public float getDescentHeight() {
        final LayoutInfo info = this.layout();
        return info.getDescentHeight(LayoutStage.STAGE2);
    }

    /** {@inheritDoc} */
    public Graphics2D getGraphics() {
        return this.graphics;
    }

    /** {@inheritDoc} */
    public void handleEvent(final Event evt) {
        final EventTarget origin = evt.getCurrentTarget();
        if (origin instanceof LayoutableNode) {
            final LayoutableNode lorigin = (LayoutableNode) origin;
            final LayoutInfo info = this.getInfo(lorigin);
            info.setLayoutStage(LayoutStage.NONE);
        }
    }

    /**
     * Data structure for storing a {@link Node} along with its rendering
     * boundary ({@link Rectangle2D}).
     */
    public static final class NodeRect {
        private final Node node;

        private final Rectangle2D rect;

        private NodeRect(final Node n, final Rectangle2D r) {
            this.node = n;
            this.rect = r;
        }

        /**
         * @return The Node this rectangle refers to.
         */
        public Node getNode() {
            return this.node;
        }

        /**
         * @return The rendering boundary.
         */
        public Rectangle2D getRect() {
            return this.rect;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder b = new StringBuilder();
            b.append(this.node).append('/').append(this.rect);
            return b.toString();
        }

    }

    /**
     * Get the node and rendering information from a mouse position.
     * 
     * @param x
     *            x-coord
     * @param y
     *            y-coord
     * @param offsetX
     *            starting x position offset
     * @param offsetY
     *            starting y position offset
     * @return list of nodes with rendering information
     */
    public List<JEuclidView.NodeRect> getNodesAt(final float x, final float y,
            final float offsetX, final float offsetY) {
        this.layout();
        final List<JEuclidView.NodeRect> nodes = new LinkedList<JEuclidView.NodeRect>();
        this.getNodesAtRec(x, y, offsetX, offsetY, this.document, nodes);
        return nodes;
    }

    /**
     * Check whether the given mouse position (with given offset) is in the
     * rendering area of the given node - if so, append it to the nodes list
     * 
     * @param x
     *            x-coord
     * @param y
     *            y-coord
     * @param offsetX
     *            x position offset to node
     * @param offsetY
     *            y position offset to node
     * @param node
     *            node to check
     * @param nodesSoFar
     *            vector of nodes so far
     */
    private void getNodesAtRec(final float x, final float y,
            final float offsetX, final float offsetY, final Node node,
            final List<JEuclidView.NodeRect> nodesSoFar) {
        if (node instanceof LayoutableNode) {
            final LayoutInfo info = this.layoutMap.get(node);

            // this will be STAGE2
            final LayoutStage stage = info.getLayoutStage();

            // find top-left corner of rendering area for this node
            final float infoX = info.getPosX(stage) + offsetX;
            final float infoY = info.getPosY(stage) + offsetY
                    - info.getAscentHeight(stage);

            // create rectangle of rendered node area
            final Rectangle2D.Float rect = new Rectangle.Float(infoX, infoY,
                    info.getWidth(stage), info.getAscentHeight(stage)
                            + info.getDescentHeight(stage));

            // record node and rectangle if it contains the mouse position
            if (rect.contains(x, y)) {
                nodesSoFar.add(new NodeRect(node, rect));

                // recurse on child nodes
                final NodeList nodeList = node.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    this.getNodesAtRec(x, y, infoX, infoY
                            + info.getAscentHeight(stage), nodeList.item(i),
                            nodesSoFar);
                }
            }
        }
    }

    /**
     * Gets the absolute Bounds for a given node and offset. May return null if
     * the node could not be found.
     * 
     * @param offsetX
     *            x position offset to node
     * @param offsetY
     *            y position offset to node
     * 
     * @param node
     *            A layoutable node which was layouted in the current view.
     * @return the rectangle with the absolute bounds or null if the given node
     *         was not layouted in this view.
     * 
     */
    public Rectangle2D getRect(final float offsetX, final float offsetY,
            final LayoutableNode node) {
        this.layout();
        final LayoutInfo info = this.layoutMap.get(node);
        final Rectangle2D retVal;
        if (info == null) {
            retVal = null;
        } else {
            LayoutableNode recNode = node;
            float recInfoX = info.getPosX(LayoutStage.STAGE2) + offsetX;
            float recInfoY = info.getPosY(LayoutStage.STAGE2) + offsetY
                    - info.getAscentHeight(LayoutStage.STAGE2);
            while (recNode.getParentNode() != null
                    && recNode.getParentNode() instanceof LayoutableNode) {
                recNode = (LayoutableNode) recNode.getParentNode();
                final LayoutInfo recInfo = this.layoutMap.get(recNode);
                recInfoX = recInfoX + recInfo.getPosX(LayoutStage.STAGE2);
                recInfoY = recInfoY + recInfo.getPosY(LayoutStage.STAGE2);
            }
            retVal = new Rectangle.Float(recInfoX, recInfoY, info
                    .getWidth(LayoutStage.STAGE2), info
                    .getAscentHeight(LayoutStage.STAGE2)
                    + info.getDescentHeight(LayoutStage.STAGE2));
        }
        return retVal;
    }

}

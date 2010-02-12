/*
 * Copyright 2002 - 2008 JEuclid, http://jeuclid.sf.net
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

package cViewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;

import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.layout.JEuclidView;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutableNode;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import cTree.CElement;

public class MathComponentUI extends ComponentUI implements
        PropertyChangeListener {

    private JMathComponent mathComponent;

    private JEuclidView jEuclidView;

    private Node document;

    private ArrayList<MyLine> lines;

    private MyLine bestLine = null;

    private Dimension preferredSize;

    /**
     * Default constructor.
     */
    public MathComponentUI() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics g, final JComponent c) {

        this.preferredSize = null;
        final Dimension dim = this.mathComponent.getSize();
        final Point start = this
                .getStartPointWithBordersAndAdjustDimension(dim);
        if (this.jEuclidView != null) {
            final Point2D alignOffset = this.calculateAlignmentOffset(dim);
            this.jEuclidView.draw((Graphics2D) g, (float) alignOffset.getX()
                    + start.x, (float) alignOffset.getY() + start.y);
            g.setColor(Color.ORANGE);
            ((Graphics2D) g).setStroke(new BasicStroke(2f));
            if (!this.getLines().isEmpty()) {
                for (final MyLine line : this.getLines()) {
                    final Line2D dLine = line.getLine();
                    g.drawLine((int) dLine.getX1(), (int) dLine.getY1(),
                            (int) dLine.getX2(), (int) dLine.getY2());
                }
            }
            if (this.getBestLine() != null) {
                g.setColor(Color.YELLOW);
                ((Graphics2D) g).setStroke(new BasicStroke(3f));
                final Line2D bLine = this.getBestLine().getLine();
                g.drawLine((int) bLine.getX1(), (int) bLine.getY1(),
                        (int) bLine.getX2(), (int) bLine.getY2());
            }
        }
    }

    public ArrayList<MyLine> getLines() {
        if (this.lines == null) {
            this.lines = new ArrayList<MyLine>();
        }
        return this.lines;
    }

    public void setLines() {
        if (this.mathComponent.getCActive() != null) {
            CElement now = this.mathComponent.getCActive();
            int i = 0;
            while (now.hasNextC()) {
                now = now.getNextSibling();
                i = i + 1;
                this.getLines().add(new MyLine(i, this.getRightLine(now)));
            }
            now = this.mathComponent.getCActive();
            i = 0;
            while (now.hasPrevC()) {
                now = now.getPrevSibling();
                i = i - 1;
                this.getLines().add(new MyLine(i, this.getLeftLine(now)));
            }
        }
    }

    public Point2D calculateAlignmentOffset(final Dimension dim) {
        final float xo;
        if ((this.mathComponent.getHorizontalAlignment() == SwingConstants.LEADING)
                || (this.mathComponent.getHorizontalAlignment() == SwingConstants.LEFT)) {
            xo = 0.0f;
        } else if ((this.mathComponent.getHorizontalAlignment() == SwingConstants.TRAILING)
                || (this.mathComponent.getHorizontalAlignment() == SwingConstants.RIGHT)) {
            xo = dim.width - this.jEuclidView.getWidth();
        } else {
            xo = (dim.width - this.jEuclidView.getWidth()) / 2.0f;
        }
        final float yo;
        if (this.mathComponent.getVerticalAlignment() == SwingConstants.TOP) {
            yo = this.jEuclidView.getAscentHeight();
        } else if (this.mathComponent.getVerticalAlignment() == SwingConstants.BOTTOM) {
            yo = dim.height - this.jEuclidView.getDescentHeight();
        } else {
            yo = (dim.height + this.jEuclidView.getAscentHeight() - this.jEuclidView
                    .getDescentHeight()) / 2.0f;
        }
        return new Point2D.Float(xo, yo);
    }

    private Point getStartPointWithBordersAndAdjustDimension(
            final Dimension dim) {
        Point start = new Point(0, 0);
        final Border border = this.mathComponent.getBorder();
        if (border != null) {
            final Insets insets = border.getBorderInsets(this.mathComponent);
            if (insets != null) {
                dim.width -= insets.left + insets.right;
                dim.height -= insets.top + insets.bottom;
                start = new Point(insets.left, insets.top);
            }
        }
        return start;
    }

    /** {@inheritDoc} */
    @Override
    public void installUI(final JComponent c) {
        this.mathComponent = (JMathComponent) c;
        c.addPropertyChangeListener(this);
        this.installDefaults(this.mathComponent);
    }

    /**
     * Configures the default properties from L&F.
     * 
     * @param c
     *            the component
     */
    protected void installDefaults(final JMathComponent c) {
        LookAndFeel.installProperty(c, "opaque", Boolean.FALSE);
    }

    /** {@inheritDoc} */
    @Override
    public void uninstallUI(final JComponent c) {
        c.removePropertyChangeListener(this);
        this.mathComponent = null;
    }

    /** {@inheritDoc} */
    public void propertyChange(final PropertyChangeEvent evt) {
        final String name = evt.getPropertyName();
        if ("documentNew".equals(name) || "property".equals(name)) {
            final JMathComponent jc = (JMathComponent) evt.getSource();
            this.document = (Node) evt.getNewValue();
            this.redo(jc.getParameters(), (Graphics2D) jc.getGraphics());
        } else {
            try {
                final JMathComponent jc = (JMathComponent) evt.getSource();
                this.redo(jc.getParameters(), (Graphics2D) jc.getGraphics());
            } catch (final ClassCastException ia) {
                ia.printStackTrace();
            }
        }
    }

    private void redo(final MutableLayoutContext parameters,
            final Graphics2D g2d) {
        if ((this.document == null) || (g2d == null)) {
            this.jEuclidView = null;
        } else {
            this.jEuclidView = new JEuclidView(this.document, parameters, g2d);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getPreferredSize(final JComponent c) {
        return this.getMathComponentSize(c);
    }

    /**
     * Retrieve the preferred size of the math component. This function caches
     * its result for faster operation.
     * 
     * @param c
     *            the math component to measure
     * @return the preferred size.
     */
    private Dimension getMathComponentSize(final JComponent c) {
        if (this.preferredSize == null) {
            if (this.jEuclidView == null || c.getGraphics() == null) {
                return super.getPreferredSize(c);
            }
            this.calculatePreferredSize(c);
        }
        return this.preferredSize;
    }

    private void calculatePreferredSize(final JComponent c) {
        this.preferredSize = new Dimension((int) Math.ceil(this.jEuclidView
                .getWidth()), (int) Math.ceil(this.jEuclidView
                .getAscentHeight()
                + this.jEuclidView.getDescentHeight()));

        final Border border = c.getBorder();
        if (border != null) {
            final Insets insets = border.getBorderInsets(c);
            if (insets != null) {
                this.preferredSize.width += insets.left + insets.right;
                this.preferredSize.height += insets.top + insets.bottom;
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getMaximumSize(final JComponent c) {
        return this.getMathComponentSize(c);
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getMinimumSize(final JComponent c) {
        return this.getMathComponentSize(c);
    }

    /**
     * Get vector of {@link JEuclidView.NodeRect} at a particular mouse
     * position.
     * 
     * @param x
     *            x-coord
     * @param y
     *            y-coord
     * @return list of nodes with rendering information
     */
    public List<JEuclidView.NodeRect> getNodesAt(final float x, final float y) {
        final Point2D point = this
                .calculateAlignmentOffset(this.mathComponent.getSize());
        return this.jEuclidView.getNodesAt(x, y, (float) point.getX(),
                (float) point.getY());
    }

    public Rectangle2D getRect(final CElement cEl) {
        if (cEl != null) {
            final Point2D point = this
                    .calculateAlignmentOffset(this.mathComponent.getSize());
            Rectangle2D rel = this.jEuclidView.getRect((float) point.getX(),
                    (float) point.getY(), (LayoutableNode)cEl.getElement());
            if (cEl.getExtPraefix() != null) {
                final Rectangle2D rpf = this.jEuclidView.getRect((float) point
                        .getX(), (float) point.getY(), (LayoutableNode)cEl.getExtPraefix());
                final Rectangle2D rges = rpf.createUnion(rel);
                rel =  rges;
            }
            return rel;
        } else {
            return null;
        }
    }

    public Line2D.Float getLeftLine(final CElement cEl) {
        if (cEl != null) {
            final Point2D point = this
                    .calculateAlignmentOffset(this.mathComponent.getSize());
            Rectangle2D rel = this.jEuclidView.getRect((float) point.getX(),
                    (float) point.getY(), (LayoutableNode)cEl.getElement());
            if (cEl.getExtPraefix() != null) {
                final Rectangle2D rpf = this.jEuclidView.getRect((float) point
                        .getX(), (float) point.getY(), (LayoutableNode)cEl.getExtPraefix());
                final Rectangle2D rges = rpf.createUnion(rel);
                rel = rges;
            }
            return new Line2D.Float((int) rel.getMinX(), (int) rel.getMinY(),
                    (int) rel.getMinX(), (int) rel.getMaxY());
        } else {
            return null;
        }
    }

    public Line2D.Float getRightLine(final CElement cEl) {
        if (cEl != null) {
            final Point2D point = this
                    .calculateAlignmentOffset(this.mathComponent.getSize());
            Rectangle2D rel = this.jEuclidView.getRect((float) point.getX(),
                    (float) point.getY(), (LayoutableNode) cEl.getElement());
            if (cEl.getExtPraefix() != null) {
                final Rectangle2D rpf = this.jEuclidView.getRect((float) point
                        .getX(), (float) point.getY(), (LayoutableNode) cEl.getExtPraefix());
                final Rectangle2D rges = rpf.createUnion(rel);
                rel = rges;
            }
            return new Line2D.Float((int) rel.getMaxX(), (int) rel.getMinY(),
                    (int) rel.getMaxX(), (int) rel.getMaxY());
        } else {
            return null;
        }
    }

    /**
     * Gets the x-Coord of the left boundary of the ViewRectangle of the given
     * CElement, if it exists, otherwise it returns -1;
     * 
     * @param cEl
     * @return
     */
    public int getXMin(final CElement cEl) {
        if (this.getRect(cEl) != null) {
            return (int) this.getRect(cEl).getMinX();
        } else {
            return -1;
        }
    }

    /**
     * Gets the x-Coord of the right boundary of the ViewRectangle of the
     * given CElement, if it exists, otherwise it returns -1;
     * 
     * @param cEl
     * @return
     */
    public int getXMax(final CElement cEl) {
        if (this.getRect(cEl) != null) {
            return (int) this.getRect(cEl).getMaxX();
        } else {
            return 5000;
        }
    }

    public Node getNodeFromView(final int x, final int y) {
        final List<JEuclidView.NodeRect> nodeRects = this.getNodesAt(x, y);
        if (!nodeRects.isEmpty()) {
            return nodeRects.get(nodeRects.size() - 1).getNode();
        } else {
            return this.mathComponent.getDocument().getFirstChild();
        }
    }

    public static void getDocInfo(final Node d) {
        if (d != null) {
            if (d instanceof Element) {
                // final Element el = (Element) d;
                // System.out.println(el.getBaseURI()); null
                // Tagname : mi, mo, mrow, mfrac, msqrt ... ebenso localName
                // ebenso NodeName
                // + d.getClass() + d.getNodeValue()
                if (d.hasAttributes()) {
                    final String result = "Attributes: ";
                    final org.w3c.dom.NamedNodeMap attr = d.getAttributes();
                    for (int i = 0; i < attr.getLength(); i++) {
                        System.out.println(result + " " + i + " "
                                + attr.item(i).getNodeName() + " "
                                + attr.item(i).getNodeValue());
                    }
                    System.out.println(result);
                }
            } else {
                System.out.println("*keinElement" + d.getNodeType()
                        + d.getNodeValue());
            }
            if (d.hasChildNodes()) {
                System.out.println("*runter");
                MathComponentUI.getDocInfo(d.getFirstChild());
                System.out.println("*rauf");
            }
            MathComponentUI.getDocInfo(d.getNextSibling());
        }
    }

    public static void getDocInfo(final CElement d, final boolean withSiblings) {
        if (d != null) {
            if (d.getExtPraefix() != null) {
                System.out.println("C*Element "
                        + d.getClass().getSimpleName() + " " + d.getCType()
                        + " " + d.getElement().getNodeName() + " "
                        + d.getCRolle() + " "
                        + d.getExtPraefix().getTextContent());
            } else {
                System.out.println("C*Element "
                        + d.getClass().getSimpleName() + " " + d.getCType()
                        + " " + d.getElement().getNodeName() + " "
                        + d.getCRolle() + " extVZ null");
            }
            if (d.hasChildC()) {
                System.out.println("*runter");
                MathComponentUI.getDocInfo(d.getFirstChild(), true);
                System.out.println("*rauf");
            }
            if (d.hasNextC() && withSiblings) {
                MathComponentUI.getDocInfo(d.getNextSibling(), true);
            }
        }
    }

    public void setBestLine(final MyLine bestLine) {
        this.bestLine = bestLine;
    }

    public MyLine getBestLine() {
        return this.bestLine;
    }

    public class MyLine {
        private Line2D.Float line;

        private int dist;

        public MyLine(final int i, final Line2D.Float line) {
            this.dist = i;
            this.line = line;
        }

        /**
         * Getter method for line.
         * 
         * @return the line
         */
        public Line2D.Float getLine() {
            return this.line;
        }

        /**
         * Setter method for line.
         * 
         * @param line
         *            the line to set
         */
        public void setLine(final Line2D.Float line) {
            this.line = line;
        }

        /**
         * Getter method for dist.
         * 
         * @return the dist
         */
        public int getDist() {
            return this.dist;
        }

        /**
         * Setter method for dist.
         * 
         * @param dist
         *            the dist to set
         */
        public void setDist(final int dist) {
            this.dist = dist;
        }

    }
}

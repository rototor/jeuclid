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
/* $Id$ */

package cViewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;

import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.layout.JEuclidView;

import org.w3c.dom.Node;

public class MathViewerUI extends ComponentUI implements
        PropertyChangeListener {

    private JMathViewer mathViewer;

    private JEuclidView jEuclidView;

    private Node document;

    private Dimension preferredSize;

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics g, final JComponent c) {
        this.preferredSize = null;
        // using the size seems to cause flickering is some cases
        final Dimension dim = this.mathViewer.getSize();
        final Point start = this
                .getStartPointWithBordersAndAdjustDimension(dim);
        this.paintBackground(g, dim, start);
        if (this.jEuclidView != null) {
            final Point2D alignOffset = this.calculateAlignmentOffset(dim);
            this.jEuclidView.draw((Graphics2D) g, (float) alignOffset.getX()
                    + start.x, (float) alignOffset.getY() + start.y);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void update(final Graphics g, final JComponent c) {
        if (c.isOpaque()) {
            g.setColor(c.getBackground());
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
        }
        this.paint(g, c);
    }

    public Point2D calculateAlignmentOffset(final Dimension dim) {
        final float xo;
        if ((this.mathViewer.getHorizontalAlignment() == SwingConstants.LEADING)
                || (this.mathViewer.getHorizontalAlignment() == SwingConstants.LEFT)) {
            xo = 0.0f;
        } else if ((this.mathViewer.getHorizontalAlignment() == SwingConstants.TRAILING)
                || (this.mathViewer.getHorizontalAlignment() == SwingConstants.RIGHT)) {
            xo = dim.width - this.jEuclidView.getWidth();
        } else {
            xo = (dim.width - this.jEuclidView.getWidth()) / 2.0f;
        }
        final float yo;
        if (this.mathViewer.getVerticalAlignment() == SwingConstants.TOP) {
            yo = this.jEuclidView.getAscentHeight();
        } else if (this.mathViewer.getVerticalAlignment() == SwingConstants.BOTTOM) {
            yo = dim.height - this.jEuclidView.getDescentHeight();
        } else {
            yo = (dim.height + this.jEuclidView.getAscentHeight() - this.jEuclidView
                    .getDescentHeight()) / 2.0f;
        }
        return new Point2D.Float(xo, yo);
    }

    private void paintBackground(final Graphics g, final Dimension dim,
            final Point start) {
        final Color back = this.getRealBackgroundColor();
        if (back != null) {
            g.setColor(back);
            g.fillRect(start.x, start.y, dim.width, dim.height);
        }
    }

    private Point getStartPointWithBordersAndAdjustDimension(
            final Dimension dim) {
        Point start = new Point(0, 0);
        final Border border = this.mathViewer.getBorder();
        if (border != null) {
            final Insets insets = border.getBorderInsets(this.mathViewer);
            if (insets != null) {
                dim.width -= insets.left + insets.right;
                dim.height -= insets.top + insets.bottom;
                start = new Point(insets.left, insets.top);
            }
        }
        return start;
    }

    private Color getRealBackgroundColor() {
        Color back = this.mathViewer.getBackground();
        if (this.mathViewer.isOpaque()) {
            if (back == null) {
                back = Color.WHITE;
            }
            // Remove Alpha
            back = new Color(back.getRGB());
        }
        return back;
    }

    /** {@inheritDoc} */
    @Override
    public void installUI(final JComponent c) {
        this.mathViewer = (JMathViewer) c;
        c.addPropertyChangeListener(this);
        this.installDefaults(this.mathViewer);
    }

    /**
     * Configures the default properties from L&F.
     * 
     * @param c
     *            the component
     */
    protected void installDefaults(final JMathViewer c) {
        LookAndFeel.installProperty(c, "opaque", Boolean.FALSE);
    }

    /** {@inheritDoc} */
    @Override
    public void uninstallUI(final JComponent c) {
        c.removePropertyChangeListener(this);
        this.mathViewer = null;
    }

    /** {@inheritDoc} */
    public void propertyChange(final PropertyChangeEvent evt) {
        final String name = evt.getPropertyName();
        if ("documentNew".equals(name) || "property".equals(name)) {
            final JMathViewer jc = (JMathViewer) evt.getSource();
            this.document = (Node) evt.getNewValue();
            this.redo(jc.getParameters(), (Graphics2D) jc.getGraphics());
        } else {
            try {
                final JMathViewer jc = (JMathViewer) evt.getSource();
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
        return this.getMathViewerSize(c);
    }

    /**
     * Retrieve the preferred size of the math component. This function caches
     * its result for faster operation.
     * 
     * @param c
     *            the math component to measure
     * @return the preferred size.
     */
    private Dimension getMathViewerSize(final JComponent c) {
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
        return this.getMathViewerSize(c);
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getMinimumSize(final JComponent c) {
        return this.getMathViewerSize(c);
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
        final Point2D point = this.calculateAlignmentOffset(this.mathViewer
                .getSize());
        return this.jEuclidView.getNodesAt(x, y, (float) point.getX(),
                (float) point.getY());
    }

    public Node getNodeFromView(final int x, final int y) {
        final List<JEuclidView.NodeRect> nodeRects = this.getNodesAt(x, y);
        if (!nodeRects.isEmpty()) {
            System.out.println(nodeRects.get(nodeRects.size() - 1).getNode()
                    .getParentNode().getTextContent());

            // return this.jEuclidView.getNode(x, y);
            return nodeRects.get(nodeRects.size() - 1).getNode();
        } else {
            return null;
        }
    }
}

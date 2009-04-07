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

/* $Id: MathComponentUI.java 783 2008-06-07 14:12:27Z maxberger $ */
/* $Id: MathComponentUI.java 783 angepasst ekuenzel $ */

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

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import org.w3c.dom.Node;
import euclid.MutableLayoutContext;
import euclid.layout.JEuclidView;


public class MathComponentUI extends ComponentUI implements
        PropertyChangeListener {

    private JMathComponent mathComponent;
    private JEuclidView jEuclidView;
    private Node document;
    private Dimension preferredSize;

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics g, final JComponent c) {
        preferredSize = null;
        // using the size seems to cause flickering is some cases
        final Dimension dim = mathComponent.getSize();
        final Point start = this
                .getStartPointWithBordersAndAdjustDimension(dim);
        paintBackground(g, dim, start);
        if (jEuclidView != null) {
            final Point2D alignOffset = calculateAlignmentOffset(dim);
            jEuclidView.draw((Graphics2D) g, (float) alignOffset.getX()
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

    private void paintBackground(final Graphics g, final Dimension dim,
            final Point start) {
        final Color back = this.getRealBackgroundColor();
        if (back != null) {
            g.setColor(back);
            g.fillRect(start.x, start.y, dim.width, dim.height);
        }
    }

    private Point getStartPointWithBordersAndAdjustDimension(final Dimension dim) {
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

    private Color getRealBackgroundColor() {
        Color back = this.mathComponent.getBackground();
        if (mathComponent.isOpaque()) {
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
        mathComponent = (JMathComponent) c;
        c.addPropertyChangeListener(this);
        installDefaults(mathComponent);
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
        if ((document == null) || (g2d == null)) {
        	jEuclidView = null;
        } else {
            jEuclidView = new JEuclidView((org.w3c.dom.Document) mathComponent.getDocument(), document, parameters, g2d);
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
        preferredSize = new Dimension((int) Math.ceil(this.jEuclidView
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
    
    public Node getNodeFromView(int x, int y) {
    	return jEuclidView.getNode(x, y);
    }

}

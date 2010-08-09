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

package net.sourceforge.jeuclid.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.layout.JEuclidView;

import org.w3c.dom.Node;

/**
 * See http://today.java.net/pub/a/today/2007/02/22/how-to-write-custom-swing-
 * component.html for details.
 * 
 * @version $Revision$
 * 
 */
public class MathComponentUI extends ComponentUI implements
        PropertyChangeListener {

    // /**
    // * Logger for this class
    // */
    // Currently Unused
    // private static final Log LOGGER =
    // LogFactory.getLog(MathComponentUI.class);

    private final Map<JMathComponent, Reference<ViewContext>> contextCache = new HashMap<JMathComponent, Reference<ViewContext>>();

    /**
     * Default constructor.
     */
    public MathComponentUI() {
        super();
        // nothing to do.
    }

    private JEuclidView getJeuclidView(final Graphics g, final JComponent c) {
        JMathComponent jc = (JMathComponent) c;
        ViewContext cache = null;
        Reference<ViewContext> ref = contextCache.get(jc);
        if (ref != null) {
            cache = ref.get();
        }

        if (cache == null) {
            cache = new ViewContext(jc);
            contextCache.put(jc, new SoftReference<ViewContext>(cache));
        }

        return cache.getJeculidView((Graphics2D) g);
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics g, final JComponent c) {
        JEuclidView jEuclidView = this.getJeuclidView(g, c);
        final Dimension dim = this.calculatePreferredSize(c, jEuclidView);
        final Point start = this.getStartPointWithBordersAndAdjustDimension(c,
                dim);
        this.paintBackground(g, c, dim, start);

        final Point2D alignOffset = this.calculateAlignmentOffset(
                (JMathComponent) c, jEuclidView, dim);
        jEuclidView.draw((Graphics2D) g, (float) alignOffset.getX() + start.x,
                (float) alignOffset.getY() + start.y);

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

    private Point2D calculateAlignmentOffset(JMathComponent jc,
            JEuclidView jEuclidView, final Dimension dim) {
        final float xo;
        if ((jc.getHorizontalAlignment() == SwingConstants.LEADING)
                || (jc.getHorizontalAlignment() == SwingConstants.LEFT)) {
            xo = 0.0f;
        } else if ((jc.getHorizontalAlignment() == SwingConstants.TRAILING)
                || (jc.getHorizontalAlignment() == SwingConstants.RIGHT)) {
            xo = dim.width - jEuclidView.getWidth();
        } else {
            xo = (dim.width - jEuclidView.getWidth()) / 2.0f;
        }
        final float yo;
        if (jc.getVerticalAlignment() == SwingConstants.TOP) {
            yo = jEuclidView.getAscentHeight();
        } else if (jc.getVerticalAlignment() == SwingConstants.BOTTOM) {
            yo = dim.height - jEuclidView.getDescentHeight();
        } else {
            yo = (dim.height + jEuclidView.getAscentHeight() - jEuclidView
                    .getDescentHeight()) / 2.0f;
        }
        return new Point2D.Float(xo, yo);
    }

    private void paintBackground(final Graphics g, JComponent c,
            final Dimension dim, final Point start) {
        final Color back = this.getRealBackgroundColor(c);
        if (back != null) {
            g.setColor(back);
            g.fillRect(start.x, start.y, dim.width, dim.height);
        }
    }

    private Point getStartPointWithBordersAndAdjustDimension(JComponent c,
            final Dimension dim) {
        Point start = new Point(0, 0);
        final Border border = c.getBorder();
        if (border != null) {
            final Insets insets = border.getBorderInsets(c);
            if (insets != null) {
                dim.width -= insets.left + insets.right;
                dim.height -= insets.top + insets.bottom;
                start = new Point(insets.left, insets.top);
            }
        }
        return start;
    }

    private Color getRealBackgroundColor(JComponent c) {
        Color back = c.getBackground();
        if (c.isOpaque()) {
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
        if (c instanceof JMathComponent) {
            c.addPropertyChangeListener(this);
            this.installDefaults(c);
        } else {
            throw new IllegalArgumentException(
                    "This UI can only be installed on a JMathComponent");
        }
    }

    /**
     * Configures the default properties from L&F.
     * 
     * @param c
     *            the component
     */
    protected void installDefaults(final JComponent c) {
        // LookAndFeel.installColorsAndFont(c, "Label.background",
        // "Label.foreground", "Label.font");
        LookAndFeel.installProperty(c, "opaque", Boolean.FALSE);
    }

    /** {@inheritDoc} */
    @Override
    public void uninstallUI(final JComponent c) {
        c.removePropertyChangeListener(this);
        this.contextCache.remove(c);
    }

    /** {@inheritDoc} */
    public void propertyChange(final PropertyChangeEvent evt) {
        this.contextCache.remove(evt.getSource());
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getPreferredSize(final JComponent c) {
        return this.getMathComponentSize(c);
    }

    /**
     * Retrieve the preferred size of the math component.
     * 
     * @param c
     *            the math component to measure
     * @return the preferred size.
     */
    private Dimension getMathComponentSize(final JComponent c) {
        JEuclidView jEuclidView = this.getJeuclidView(c.getGraphics(), c);
        return this.calculatePreferredSize(c, jEuclidView);
    }

    private Dimension calculatePreferredSize(final JComponent c,
            JEuclidView jEuclidView) {
        Dimension retVal;
        retVal = new Dimension((int) Math.ceil(jEuclidView.getWidth()),
                (int) Math.ceil(jEuclidView.getAscentHeight()
                        + jEuclidView.getDescentHeight()));

        final Border border = c.getBorder();
        if (border != null) {
            final Insets insets = border.getBorderInsets(c);
            if (insets != null) {
                retVal.width += insets.left + insets.right;
                retVal.height += insets.top + insets.bottom;
            }
        }
        return retVal;
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
     * @param mathComponent
     *            MathComponent to look in.
     * @param x
     *            x-coord
     * @param y
     *            y-coord
     * @return list of nodes with rendering information
     */
    public List<JEuclidView.NodeRect> getNodesAt(JMathComponent mathComponent,
            final float x, final float y) {
        JEuclidView jEuclidView = getJeuclidView(mathComponent.getGraphics(),
                mathComponent);
        final Point2D point = this.calculateAlignmentOffset(mathComponent,
                jEuclidView, mathComponent.getSize());
        return jEuclidView.getNodesAt(x, y, (float) point.getX(),
                (float) point.getY());
    }

    private static class ViewContext {
        final Node document;
        final LayoutContext layoutContext;
        final Map<Graphics2D, JEuclidView> jeuclidViews = new HashMap<Graphics2D, JEuclidView>();

        public ViewContext(JMathComponent jMathComponent) {
            this.document = jMathComponent.getDocument();
            this.layoutContext = jMathComponent.getParameters();
        }

        public JEuclidView getJeculidView(Graphics2D g2d) {
            JEuclidView jeuclidView = jeuclidViews.get(g2d);
            if (jeuclidView == null) {
                jeuclidView = new JEuclidView(document, layoutContext, g2d);
                jeuclidViews.put(g2d, jeuclidView);
            }
            return jeuclidView;
        }
    }

}

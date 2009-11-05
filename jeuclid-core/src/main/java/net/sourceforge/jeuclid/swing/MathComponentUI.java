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
import java.util.List;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;

import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.layout.JEuclidView;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

/**
 * See http://today.java.net/pub/a/today/2007/02/22/how-to-write-custom-swing-
 * component.html for details.
 * 
 * @version $Revision$
 * 
 */
public class MathComponentUI extends ComponentUI implements
        PropertyChangeListener {

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(MathComponentUI.class);

    private JMathComponent mathComponent;

    /**
     * Reference to layout tree.
     */
    private JEuclidView jEuclidView;

    /** Reference to document. */
    private Node document;

    private Dimension preferredSize;

    /**
     * Default constructor.
     */
    public MathComponentUI() {
        super();
        // nothing to do.
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics g, final JComponent c) {
        this.preferredSize = null;
        // using the size seems to cause flickering is some cases
        final Dimension dim = this.mathComponent.getSize();
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

    private Point2D calculateAlignmentOffset(final Dimension dim) {
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
        if (this.mathComponent.isOpaque()) {
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
            this.mathComponent = (JMathComponent) c;
            c.addPropertyChangeListener(this);
            this.installDefaults(this.mathComponent);
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
    protected void installDefaults(final JMathComponent c) {
        // LookAndFeel.installColorsAndFont(c, "Label.background",
        // "Label.foreground", "Label.font");
        LookAndFeel.installProperty(c, "opaque", Boolean.FALSE);
    }

    /** {@inheritDoc} */
    @Override
    public void uninstallUI(final JComponent c) {
        c.removePropertyChangeListener(this);
        this.mathComponent = null;
    }

    /**
     * compare old and new document and get highest differnt nodes
     * @param  oldDoc old Document
     * @param  newDoc new Document
     * @return highest 2 different nodes (node[0] is from old Document, node[1] from new), null if any Document is null
     */
    private static Node[] compareDocuments(Document oldDoc, Document newDoc) {
        DocumentTraversal traversal1, traversal2;
        TreeWalker walker1, walker2;
        Node[] roots = null;

        // abort if any document is null
        if (oldDoc == null || newDoc == null || oldDoc.getDocumentElement() == null || newDoc.getDocumentElement() == null) {
            return roots;
        }

        traversal1 = (DocumentTraversal) oldDoc;
        traversal2 = (DocumentTraversal) newDoc;

        walker1 = traversal1.createTreeWalker(oldDoc.getDocumentElement(), NodeFilter.SHOW_ALL, null, true);
        walker2 = traversal2.createTreeWalker(newDoc.getDocumentElement(), NodeFilter.SHOW_ALL, null, true);

        roots = traverseLevel(walker1, walker2);

        return roots;
    }

    /**
     * helper methode for comparing one level in the trees
     * @param walkerOld Treewalker for old Document
     * @param walkerNew Treewalker for new Document
     * @return highest 2 different nodes (node[0] is from old Document, node[1] from new), null if any Document is null
     */
    private static final Node[] traverseLevel(TreeWalker walkerOld, TreeWalker walkerNew) {
        Node parentOld, parentNew, nodeOld, nodeNew;
        NodeList nlOld, nlNew;
        Node[] roots, tmp;

        parentOld = walkerOld.getCurrentNode();
        parentNew = walkerNew.getCurrentNode();

        nlOld = parentOld.getChildNodes();
        nlNew = parentNew.getChildNodes();
        roots = new Node[2];

        // compare children number
        if (nlOld.getLength() != nlNew.getLength()) {
            roots[0] = parentOld;
            roots[1] = parentNew;
            //      System.out.println("-- CHILDRENS of " + roots[0] + " - "+roots[1]);
        } else {

            for (nodeOld = walkerOld.firstChild(), nodeNew = walkerNew.firstChild(); nodeOld != null && nodeNew != null; nodeOld = walkerOld.nextSibling(), nodeNew = walkerNew.nextSibling()) {

                tmp = traverseLevel(walkerOld, walkerNew);

                if (tmp[0] != null) {  // childrens are different

                    // abort if more than 2 childs differ
                    if (roots[0] != null) {
                        roots[0] = parentOld;
                        roots[1] = parentNew;
                        //               System.out.println("-- 2 CHILDRENS of " + roots[0] + " - "+roots[1]);
                        break;
                    }

                    roots[0] = tmp[0];
                    roots[1] = tmp[1];
                }
            }

            if (!parentOld.getNodeName().equals(parentNew.getNodeName())) {
                roots[0] = parentOld;
                roots[1] = parentNew;
                //System.out.println("-- NODE " + roots[0] + " - "+roots[1]);
            }

            if (parentOld.getNodeType() == 3 && parentNew.getNodeType() == 3 && !parentOld.getTextContent().trim().equals(parentNew.getTextContent().trim())) {
                roots[0] = parentOld.getParentNode();
                roots[1] = parentNew.getParentNode();
                //    System.out.println("-- NODE TEXT " + roots[0] + " - "+roots[1]);
                //      System.out.println("["+parentOld.getTextContent() + "] - ["+parent2.getTextContent()+"]");
            }
        }

        walkerOld.setCurrentNode(parentOld);
        walkerNew.setCurrentNode(parentNew);
        return roots;
    }

    /** {@inheritDoc} */
    public void propertyChange(final PropertyChangeEvent evt) {
        final Node[] roots;
        final String name = evt.getPropertyName();

        if ("document".equals(name) || "property".equals(name)) {
            final JMathComponent jc = (JMathComponent) evt.getSource();
            this.document = (Node) evt.getNewValue();
            roots = compareDocuments((Document) evt.getOldValue(), (Document) this.document);

            this.redo(roots, jc.getParameters(), (Graphics2D) jc.getGraphics());
            // jc.repaint();
        } else {
            try {
                final JMathComponent jc = (JMathComponent) evt.getSource();
                this.redo(null, jc.getParameters(), (Graphics2D) jc.getGraphics());
            } catch (final ClassCastException ia) {
                MathComponentUI.LOGGER.debug(ia);
            }
        }
    }

    private void redo(Node[] roots, final MutableLayoutContext parameters,
            final Graphics2D g2d) {
        if ((this.document == null) || (g2d == null)) {
            this.jEuclidView = null;
        } else {
            this.jEuclidView = new JEuclidView(this.document, this.jEuclidView, roots, parameters, g2d);
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
        final Point2D point = this.calculateAlignmentOffset(this.mathComponent
                .getSize());
        return this.jEuclidView.getNodesAt(x, y, (float) point.getX(),
                (float) point.getY());
    }

}

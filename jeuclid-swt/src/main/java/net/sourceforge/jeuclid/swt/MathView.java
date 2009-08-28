/*
 * Copyright 2007 - 2009 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.swt;

import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.elements.generic.DocumentElement;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Node;

/**
 * Contains a display component for SWT.
 * 
 * @todo improve.
 * @version $Revision$
 */
public final class MathView extends Canvas {

    // /**
    // * Logger for this class
    // */
    // private static final Log LOGGER = LogFactory.getLog(MathView.class);

    private final MathRenderer mathRenderer = MathRenderer.getInstance();

    private Node document;

    private ImageData renderedFormula;

    private MutableLayoutContext layoutContext = new LayoutContextImpl(
            LayoutContextImpl.getDefaultLayoutContext());

    /**
     * Create a new MathView Widget.
     * 
     * @param parent
     *            Parent component
     * @param style
     *            SWT style attributes.
     */
    public MathView(final Composite parent, final int style) {
        super(parent, style);
        this.setDocument(new DocumentElement());
        this.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(final DisposeEvent e) {
                MathView.this.widgetDisposed(e);
            }
        });
        this.addPaintListener(new PaintListener() {
            public void paintControl(final PaintEvent e) {
                MathView.this.paintControl(e);
            }
        });
    }

    private void paintControl(final PaintEvent e) {
        final GC gc = e.gc;
        final Device device = gc.getDevice();
        final Color c = new Color(device, 255, 255, 255);
        gc.setBackground(c);
        gc.fillRectangle(e.x, e.y, e.width, e.height);
        c.dispose();
        if (this.renderedFormula != null) {
            final Image im = new Image(device, this.renderedFormula);
            gc.drawImage(im, 0, 0);
            im.dispose();
        }
    }

    private void widgetDisposed(final DisposeEvent e) {
        this.document = null;
        this.renderedFormula = null;
    }

    private void recreate() {
        this.renderedFormula = this.mathRenderer.render(this.document,
                this.layoutContext);
    }

    /**
     * @param doc
     *            the document to set
     */
    public void setDocument(final Node doc) {
        final Node oldValue = this.document;
        this.document = doc;
        if (doc != oldValue) {
            this.recreate();
            this.redraw();
        }
    }

    /**
     * @return the document
     */
    public Node getDocument() {
        return this.document;
    }

}

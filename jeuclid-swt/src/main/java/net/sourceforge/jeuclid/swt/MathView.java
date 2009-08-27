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

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * This class will contain a display component for SWT.
 * 
 * @todo actually implement SWT component
 * @version $Revision$
 */
public final class MathView extends Canvas {
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
    }

    private void widgetDisposed(final DisposeEvent e) {
        // Nothing to do yet
    }

}

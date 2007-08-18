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

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.LayoutContext.Parameter;

import org.w3c.dom.views.DocumentView;

/**
 * @author Max Berger
 * @version $Revision$
 */
public class RootLayoutNodeImpl extends CompoundLayout implements
        RootLayoutNode {

    private final DocumentView documentView;

    /**
     * Default Constructor.
     * 
     * @param context
     *            Layout Context
     * @param theDoc
     *            document this layout refers to.
     */
    public RootLayoutNodeImpl(final LayoutContext context,
            final DocumentView theDoc) {
        super(context);
        this.documentView = theDoc;
    }

    /** {@inheritDoc} */
    public DocumentView getDocument() {
        return this.documentView;
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final float parentX, final float parentY,
            final Graphics2D g) {
        final RenderingHints hints = g.getRenderingHints();
        if ((Boolean) (this.getLayoutContext()
                .getParameter(Parameter.ANTIALIAS))) {
            hints.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON));
        }
        hints.add(new RenderingHints(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_NORMALIZE));
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY));
        g.setRenderingHints(hints);
        super.paint(parentX, parentY, g);
    }
}

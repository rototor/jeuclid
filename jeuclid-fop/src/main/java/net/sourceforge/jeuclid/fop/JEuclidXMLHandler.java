/*
 * Copyright 2007 - 2008 JEuclid, http://jeuclid.sf.net
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

/* 
 * Parts of the contents are heavily inspired by work done for Barcode4J by
 * Jeremias Maerki, available at http://barcode4j.sf.net/
 */
package net.sourceforge.jeuclid.fop;

import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.xmlgraphics.Graphics2DImagePainterMathML;

import org.apache.fop.render.Graphics2DAdapter;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererContext;
import org.apache.fop.render.XMLHandler;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;
import org.w3c.dom.Document;

/**
 * XMLHandler which draws MathML through a fop G2DAdapter.
 * 
 * @version $Revision$
 */
public class JEuclidXMLHandler implements XMLHandler {

    /** Creates a new instance of JEuclidXMLHandler. */
    public JEuclidXMLHandler() {
    }

    /** {@inheritDoc} */
    public void handleXML(final RendererContext rendererContext,
            final Document document, final String ns) throws Exception {
        final Graphics2DAdapter g2dAdapter = rendererContext.getRenderer()
                .getGraphics2DAdapter();

        if (g2dAdapter != null) {
            final Graphics2DImagePainter painter = Graphics2DImagePainterMathML
                    .createGraphics2DImagePainter(document);
            g2dAdapter.paintImage(painter, rendererContext,
                    ((Integer) rendererContext.getProperty("xpos"))
                            .intValue(), ((Integer) rendererContext
                            .getProperty("ypos")).intValue(),
                    ((Integer) rendererContext.getProperty("width"))
                            .intValue(), ((Integer) rendererContext
                            .getProperty("height")).intValue());

        }
    }

    /** {@inheritDoc} */
    public boolean supportsRenderer(final Renderer renderer) {
        return renderer.getGraphics2DAdapter() != null;
    }

    /** {@inheritDoc} */
    public String getNamespace() {
        return AbstractJEuclidElement.URI;
    }

}

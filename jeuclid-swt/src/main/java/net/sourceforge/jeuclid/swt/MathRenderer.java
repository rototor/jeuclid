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

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.IOException;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.converter.Converter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.w3c.dom.Node;

/**
 * Renders MathML to SWT ImageData.
 * 
 * @version $Revision$
 */
public final class MathRenderer {

    private static final int COLOR_ENTRIES = 3;

    private static final int BITS_PER_PIXEL = MathRenderer.COLOR_ENTRIES * 8;

    private static final PaletteData PALETTE_BGR = new PaletteData(0xff,
            0xff00, 0xff0000);

    private static final class SingletonHolder {
        private static final MathRenderer INSTANCE = new MathRenderer();

        private SingletonHolder() {
        }
    }

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(MathRenderer.class);

    private final Converter converter = Converter.getInstance();

    /**
     * Default constructor.
     */
    private MathRenderer() {
        // Empty on purpose.
    }

    /**
     * Retrieve an instance of the converter singleton class.
     * 
     * @return a Converter object.
     */
    public static MathRenderer getInstance() {
        return MathRenderer.SingletonHolder.INSTANCE;
    }

    /**
     * Renders MathML into ImageData.
     * 
     * @param document
     *            The MathML Document to render
     * @param layoutContext
     *            LayoutContext to use
     * @return an ImageData instance or null if an error occurred.
     */
    public ImageData render(final Node document,
            final LayoutContext layoutContext) {
        ImageData renderedFormula;
        if (document == null) {
            renderedFormula = null;
        } else {
            try {
                final BufferedImage bi = this.converter.render(document,
                        layoutContext, BufferedImage.TYPE_3BYTE_BGR);
                final Raster r = bi.getRaster();
                final DataBuffer b = r.getDataBuffer();
                final DataBufferByte db = (DataBufferByte) b;
                final byte[] data = db.getData();
                final int w = bi.getWidth();
                renderedFormula = new ImageData(w, bi.getHeight(),
                        MathRenderer.BITS_PER_PIXEL, MathRenderer.PALETTE_BGR,
                        MathRenderer.COLOR_ENTRIES * w, data);
            } catch (IOException io) {
                MathRenderer.LOGGER.warn(io.getMessage(), io);
                renderedFormula = null;
            }
        }
        return renderedFormula;
    }

}

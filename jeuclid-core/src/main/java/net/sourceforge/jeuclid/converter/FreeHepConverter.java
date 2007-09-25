/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.converter;

import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.layout.JEuclidView;

import org.freehep.graphics2d.VectorGraphics;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Converter for output formats supported by FreeHEP.
 * 
 * @version $Revision$
 */
public class FreeHepConverter implements ConverterPlugin {

    private final Constructor<VectorGraphics> streamConst;

    FreeHepConverter(final Class<?> converterClass)
            throws NoSuchMethodException {

        this.streamConst = ((Class<VectorGraphics>) converterClass)
                .getConstructor(OutputStream.class, Dimension.class);
    }

    /** {@inheritDoc} */
    public Dimension convert(final Node doc, final LayoutContext context,
            final OutputStream outStream) throws IOException {
        final Properties p = new Properties();
        p.setProperty("PageSize", "A5");
        final VectorGraphics tempg = this.createGraphics(
                new ByteArrayOutputStream(), new Dimension(1, 1));
        final JEuclidView view = new JEuclidView(doc, context, tempg);
        final int ascent = (int) Math.ceil(view.getAscentHeight());
        final Dimension size = new Dimension((int) Math.ceil(view.getWidth()), 
                (int) Math.ceil(view.getDescentHeight()) + ascent);

        final VectorGraphics g = this.createGraphics(outStream, size);
        g.setCreator("JEuclid (from MathML)");
        //g.setProperties(p);
        g.startExport();
        view.draw(g, 0, ascent);
        g.endExport();

        return size;
    }

    /** {@inheritDoc} */
    public Document convert(final Node doc, final LayoutContext context) {
        return null;
    }

    private VectorGraphics createGraphics(final OutputStream os, final Dimension d) {
        try {
            return this.streamConst.newInstance(os, d);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected - failed to create a FreeHep VectorGraphics instance", e);
        }
    }
}

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

package net.sourceforge.jeuclid.testsuite;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.converter.Processor;

/**
 * Serve the W3C MathML Testsuite v3, rendering all formulas with JEuclid to SVG.
 * 
 * @version $Revision$
 */
public class Servlet3 extends HttpServlet {
    private static final int BLOCK_SIZE = 4096;

    private static final float DISPLAY_SIZE = 16.0f;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final MutableLayoutContext context;

    /**
     * Default Constructor.
     */
    public Servlet3() {
        this.context = new LayoutContextImpl(LayoutContextImpl
                .getDefaultLayoutContext());
        this.context.setParameter(Parameter.MATHSIZE, Servlet3.DISPLAY_SIZE);
    }

    /** {@inheritDoc} */
    @Override
    protected void doGet(final HttpServletRequest req,
            final HttpServletResponse resp) throws ServletException,
            IOException {
        final String file = req.getPathInfo();
        final InputStream stream = Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(
                        "mml3-testsuite/" + file);
        if (stream == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, file);
        } else {
            final OutputStream out = resp.getOutputStream();

            boolean processed = false;
            if (file.endsWith(".xhtml")) {
                final Source inputSource = new StreamSource(stream);
                final Result result = new StreamResult(out);
                try {
                    Processor.getInstance().process(inputSource, result);
                    processed = true;
                } catch (final TransformerException te) {
                    processed = false;
                }
            }
            if (!processed) {
                final byte[] buf = new byte[Servlet3.BLOCK_SIZE];
                int count = stream.read(buf);
                while (count > -1) {
                    out.write(buf, 0, count);
                    count = stream.read(buf);
                }
            }
        }
    }
}

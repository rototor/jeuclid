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

package net.sourceforge.jeuclid.parser;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.MutableLayoutContext;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Factory class to create MathBase from JAXP Sources.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public final class MathBaseFactory {

    private static MathBaseFactory mathBaseFactory;

    private final Parser parser;

    private MathBaseFactory() throws ParserConfigurationException {
        this.parser = Parser.getParser();
    }

    /**
     * Retrieve the (singleton) MathBaseFactory object.
     * 
     * @return a MathBaseFactory.
     * @throws ParserConfigurationException
     *             when the internal (DOM) parser could not be created.
     * 
     */
    public static synchronized MathBaseFactory getMathBaseFactory()
            throws ParserConfigurationException {
        if (MathBaseFactory.mathBaseFactory == null) {
            MathBaseFactory.mathBaseFactory = new MathBaseFactory();
        }
        return MathBaseFactory.mathBaseFactory;
    }

    /**
     * Parse an input source and return the MathBase object.
     * 
     * @param params
     *            set of parameters to use.
     * @param source
     *            the Source to use. Currently supported are
     *            {@link javax.xml.transform.dom.DOMSource},
     *            {@link javax.xml.transform.stream.StreamSource}
     * @return the MathBase object.
     * @throws IOException
     *             if an I/O error occurs.
     * @throws IllegalArgumentException
     *             if the Source is of an unsupported object type.
     */
    public MathBase createMathBase(final Source source,
            final MutableLayoutContext params) throws IOException {

        try {
            final Node node = this.parser.parse(source);

            final MathBase base = new MathBase();
            DOMBuilder.getDOMBuilder().createJeuclidDom(node, base);
            base.getRootElement().setLayoutContext(params);
            return base;
        } catch (final SAXException e) {
            throw new IOException("Parse Error: " + e.getMessage());
        }

    }
}

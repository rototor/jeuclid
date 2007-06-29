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

package net.sourceforge.jeuclid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import net.sourceforge.jeuclid.parser.MathBaseFactory;
import net.sourceforge.jeuclid.parser.Parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Utility class for the support parsing MathML and OpenDocument Formula (ODF)
 * files.
 * <p>
 * This class supports parsing of files that are either MathML or OpenDocument
 * Formula (ODF) files. It also supports parsing MathML from a given text
 * string.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public final class MathMLParserSupport {

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory
            .getLog(MathMLParserSupport.class);

    private MathMLParserSupport() {
    }

    /**
     * Parse an input file and return the MathBase object.
     * 
     * @param params
     *            set of parameters to use.
     * @param document
     *            the document to parse. See
     *            {@link DOMBuilder#DOMBuilder(Node, MathBase)} for the list
     *            of valid node types.
     * @return the MathBase object.
     * @throws SAXException
     *             if a parse error occurs.
     * @throws IOException
     *             if a read io error occurs.
     */
    public static MathBase createMathBaseFromDocument(final Node document,
            final Map<ParameterKey, String> params) throws SAXException,
            IOException {

        try {
            return MathBaseFactory.getMathBaseFactory().createMathBase(
                    new DOMSource(document), params);
        } catch (final ParserConfigurationException e) {
            // Should not happen. But who knows?
            MathMLParserSupport.LOGGER.warn(e);
            return null;
        }

    }

    /**
     * Parse an input file and return the MathBase object.
     * 
     * @param params
     *            Conversion parameters to use.
     * @param inFile
     *            the file to parse.
     * @return the MathBase object.
     * @throws SAXException
     *             if a parse error occurs.
     * @throws IOException
     *             if a read io error occurs.
     */
    public static MathBase createMathBaseFromFile(final File inFile,
            final Map<ParameterKey, String> params) throws SAXException,
            IOException {
        try {
            return MathBaseFactory.getMathBaseFactory().createMathBase(
                    new StreamSource(new FileInputStream(inFile)), params);
        } catch (final ParserConfigurationException e) {
            // Should not happen. But who knows?
            MathMLParserSupport.LOGGER.warn(e);
            return null;
        }
    }

    /**
     * Creates a DocumentBuilder that can be used to parse MathML documents
     * into a standard DOM model.
     * 
     * @return a DocumentBuilder instance that is configured for MathML
     * @throws ParserConfigurationException
     *             if the builder could not be configured properly.
     */
    public static DocumentBuilder createDocumentBuilder()
            throws ParserConfigurationException {
        final DocumentBuilder builder = Parser.getParser()
                .getDocumentBuilder();
        return builder;
    }

    /**
     * Parse an input stream in MathML XML format.
     * 
     * @param inStream
     *            the stream to parse.
     * @return the DOM Tree
     * @throws SAXException
     *             if a parse error occurs.
     * @throws IOException
     *             if a read I/O error occurs.
     */
    public static Document parseInputStreamXML(final InputStream inStream)
            throws SAXException, IOException {
        try {
            return Parser.getParser().parseStreamSourceAsXml(
                    new StreamSource(inStream));
        } catch (final ParserConfigurationException e) {
            // Should not happen. But who knows?
            MathMLParserSupport.LOGGER.warn(e);
            return null;
        }
    }

    /**
     * Parse an input stream in ODF format.
     * 
     * @param inStream
     *            the stream to parse.
     * @return the DOM Tree
     * @throws SAXException
     *             if a parse error occurs.
     * @throws IOException
     *             if a read I/O error occurs.
     */
    public static Document parseInputStreamODF(final InputStream inStream)
            throws SAXException, IOException {
        try {
            return Parser.getParser().parseStreamSourceAsOdf(
                    new StreamSource(inStream));
        } catch (final ParserConfigurationException e) {
            // Should not happen. But who knows?
            MathMLParserSupport.LOGGER.warn(e);
            return null;
        }
    }

    /**
     * Parse an input file and return the DOM tree.
     * <p>
     * This function will auto-detect if the given input is in MathML or ODF
     * format.
     * 
     * @param inFile
     *            the file to parse.
     * @return the DOM Tree
     * @throws SAXException
     *             if a parse error occurs.
     * @throws IOException
     *             if a read I/O error occurs.
     */
    public static Document parseFile(final File inFile) throws SAXException,
            IOException {
        try {
            return Parser.getParser().parseStreamSource(
                    new StreamSource(new FileInputStream(inFile)));
        } catch (final ParserConfigurationException e) {
            // Should not happen. But who knows?
            MathMLParserSupport.LOGGER.warn(e);
            return null;
        }
    }

    /**
     * Create a DOM Document from a given string containing MathML content.
     * This function uses a DOM Parser configured for MathML.
     * 
     * @param content
     *            A String containing MathML.
     * @return a DOM Document.
     * @throws SAXException
     *             a parsing error occurred.
     * @throws ParserConfigurationException
     *             a configuration error occurred.
     * @throws IOException
     *             for any other IO exception.
     */
    public static Document parseString(final String content)
            throws SAXException, ParserConfigurationException, IOException {
        return Parser.getParser().parseStreamSourceAsXml(
                new StreamSource(new StringReader(content)));
    }

}

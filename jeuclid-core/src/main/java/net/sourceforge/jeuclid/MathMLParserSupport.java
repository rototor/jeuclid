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
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Utility class for the support parsing MathML and OpenDocument Formula (ODF)
 * files.
 * <p>
 * This class supports parsing of files that are either MathML or Opendocument
 * Formula (ODF) files. It also supports parsing MathML from a given text
 * string.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public final class MathMLParserSupport {
    private static final String COULD_NOT_CREATE_PARSER = "Could not create Parser: ";

    private static final String CONTENT_XML = "content.xml";

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
        final MathBase base = new MathBase(params);

        if (document != null) {
            new DOMBuilder(document, base);
        }
        return base;
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
        final Document document = MathMLParserSupport.parseFile(inFile);
        return MathMLParserSupport.createMathBaseFromDocument(document,
                params);
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
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        try {
            documentBuilderFactory.setXIncludeAware(true);
        } catch (final UnsupportedOperationException uoe) {
            MathMLParserSupport.LOGGER.debug("Unsupported Operation: "
                    + uoe.getMessage());
        }
        final DocumentBuilder parser = documentBuilderFactory
                .newDocumentBuilder();
        parser.setEntityResolver(new ResourceEntityResolver());
        parser.setErrorHandler(new ErrorHandler() {
            public void error(final SAXParseException exception)
                    throws SAXException {
                MathMLParserSupport.LOGGER.warn(exception);
            }

            public void fatalError(final SAXParseException exception)
                    throws SAXException {
                throw exception;
            }

            public void warning(final SAXParseException exception)
                    throws SAXException {
                MathMLParserSupport.LOGGER.debug(exception);
            }
        });
        return parser;
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
        Document document = null;
        try {
            final DocumentBuilder parser = MathMLParserSupport
                    .createDocumentBuilder();
            document = parser.parse(inStream);
        } catch (final ParserConfigurationException pce) {
            MathMLParserSupport.LOGGER.fatal(
                    MathMLParserSupport.COULD_NOT_CREATE_PARSER
                            + pce.getMessage(), pce);
        }
        return document;
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
        final ZipInputStream zipStream = new ZipInputStream(inStream);
        Document document = null;
        try {
            final DocumentBuilder parser = MathMLParserSupport
                    .createDocumentBuilder();
            ZipEntry entry = zipStream.getNextEntry();
            while (entry != null) {
                if (MathMLParserSupport.CONTENT_XML.equals(entry.getName())) {
                    document = parser.parse(zipStream);
                    entry = null;
                } else {
                    entry = zipStream.getNextEntry();
                }
            }
        } catch (final ParserConfigurationException pce) {
            MathMLParserSupport.LOGGER.fatal(
                    MathMLParserSupport.COULD_NOT_CREATE_PARSER
                            + pce.getMessage(), pce);
        }
        return document;
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
        Document document = null;
        try {
            final DocumentBuilder parser = MathMLParserSupport
                    .createDocumentBuilder();
            try {
                document = parser.parse(inFile.toURI().toString());
            } catch (final SAXParseException se) {
                try {
                    // Also try as ODF:
                    final ZipFile zipFile = new ZipFile(inFile);
                    final ZipEntry contentEntry = zipFile
                            .getEntry(MathMLParserSupport.CONTENT_XML);
                    final InputStream contentStream = zipFile
                            .getInputStream(contentEntry);
                    document = parser.parse(new InputSource(contentStream));
                } catch (final SAXParseException e2) {
                    throw e2;
                } catch (final IOException io) {
                    throw se;
                }
            }
        } catch (final ParserConfigurationException pce) {
            MathMLParserSupport.LOGGER.fatal(
                    MathMLParserSupport.COULD_NOT_CREATE_PARSER
                            + pce.getMessage(), pce);
        }
        return document;
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
        final DocumentBuilder parser = MathMLParserSupport
                .createDocumentBuilder();
        return parser.parse(new InputSource(new StringReader(content)));
    }

}

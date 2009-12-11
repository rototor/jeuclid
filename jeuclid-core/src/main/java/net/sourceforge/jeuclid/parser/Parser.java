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

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import net.jcip.annotations.ThreadSafe;
import net.sourceforge.jeuclid.ResourceEntityResolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.image.loader.ImageSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A JAXP compatible approach to MathML Parsing.
 * 
 * @version $Revision$
 */
// CHECKSTYLE:OFF
// This class is too complex.
@ThreadSafe
public final class Parser {
    // CHECKSTYLE:ON

    private static final class LoggerErrorHandler implements ErrorHandler {
        public LoggerErrorHandler() {
            // Empty on purpose
        }

        public void error(final SAXParseException exception)
                throws SAXException {
            Parser.LOGGER.warn(exception);
        }

        public void fatalError(final SAXParseException exception)
                throws SAXException {
            throw exception;
        }

        public void warning(final SAXParseException exception)
                throws SAXException {
            Parser.LOGGER.debug(exception);
        }
    }

    private static final class UnclosableInputStream extends FilterInputStream {
        protected UnclosableInputStream(final InputStream in) {
            super(in);
        }

        @Override
        public void close() throws IOException {
            // Do Nothing.
        }
    }

    /**
     * Detection buffer size. Rationale: After the first 128 bytes a XML file
     * and a ZIP file should be distinguishable.
     */
    private static final int DETECTION_BUFFER_SIZE = 128;

    private static final String BAD_STREAM_SOURCE = "Bad StreamSource: ";

    private static final String CONTENT_XML = "content.xml";

    private static final String CANNOT_HANDLE_SOURCE = "Cannot handle Source: ";

    private static final class SingletonHolder {
        private static final Parser INSTANCE = new Parser();

        private SingletonHolder() {
        }
    }

    /**
     * Logger for this class.
     */
    private static final Log LOGGER = LogFactory.getLog(Parser.class);

    private final Map<Long, Reference<DocumentBuilder>> builders;

    /**
     * Default constructor.
     */
    protected Parser() {
        this.builders = new ConcurrentHashMap<Long, Reference<DocumentBuilder>>();
    }

    private DocumentBuilder createDocumentBuilder() {
        DocumentBuilder documentBuilder;
        try {
            try {
                documentBuilder = this.tryCreateDocumentBuilder(true);
            } catch (final UnsupportedOperationException uoe) {
                Parser.LOGGER.debug("Unsupported Operation: "
                        + uoe.getMessage());
                documentBuilder = this.tryCreateDocumentBuilder(false);
            } catch (final ParserConfigurationException pce) {
                Parser.LOGGER.debug("ParserConfigurationException: "
                        + pce.getMessage());
                documentBuilder = this.tryCreateDocumentBuilder(false);
            }
            documentBuilder.setEntityResolver(new ResourceEntityResolver());
            documentBuilder.setErrorHandler(new LoggerErrorHandler());
        } catch (final ParserConfigurationException pce2) {
            Parser.LOGGER.warn("Could not create Parser: " + pce2.getMessage());
            assert false : "Could not create Parser";
            documentBuilder = null;
        }
        return documentBuilder;
    }

    private DocumentBuilder tryCreateDocumentBuilder(final boolean xinclude)
            throws ParserConfigurationException {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        if (xinclude) {
            documentBuilderFactory.setXIncludeAware(true);
        }
        final DocumentBuilder documentBuilder = documentBuilderFactory
                .newDocumentBuilder();
        return documentBuilder;
    }

    /**
     * Retrieve the singleton Parser instance.
     * 
     * @return a Parser object.
     */
    public static Parser getInstance() {
        return Parser.SingletonHolder.INSTANCE;
    }

    /**
     * use {@link #getInstance()} instead.
     * 
     * @return see {@link #getInstance()}
     * @throws ParserConfigurationException
     *             see {@link #getInstance()}
     * @deprecated use {@link #getInstance()} instead.
     */
    @Deprecated
    public static Parser getParser() throws ParserConfigurationException {
        return Parser.getInstance();
    }

    /**
     * Parse a StreamSource and return its Document.
     * <p>
     * This method will auto-detect ODF or XML format and load an appropriate
     * parser.
     * 
     * @param streamSource
     *            A StreamSource.
     * @return A DOM Document representation for this source.
     * @throws SAXException
     *             if a parse error occurred.
     * @throws IOException
     *             if an I/O error occurred.
     */
    public Document parseStreamSource(final StreamSource streamSource)
            throws SAXException, IOException {
        Document retVal = null;
        InputStream inputStream = streamSource.getInputStream();
        if (inputStream != null) {

            // Alternative 1: Parse as XML, and fall back to ODF
            if (!inputStream.markSupported()) {
                inputStream = new BufferedInputStream(inputStream);
            }
            final InputStream filterInput = new UnclosableInputStream(
                    inputStream);
            filterInput.mark(Parser.DETECTION_BUFFER_SIZE);
            try {
                retVal = this.parseStreamSourceAsXml(new StreamSource(
                        filterInput));
                inputStream.close();
            } catch (final SAXParseException se) {
                filterInput.reset();
                try {
                    retVal = this.parseStreamSourceAsOdf(new StreamSource(
                            filterInput));
                } catch (final IOException io) {
                    throw se;
                }
                inputStream.close();
            }

            // Alternative 2: peek for ZIP magic and call matching parser.

            // final PushbackInputStream pi = new PushbackInputStream(
            // inputStream, 4);
            // final byte[] magic = new byte[4];
            // pi.read(magic);
            // pi.unread(magic);
            // if ((magic[0] == 'P') && (magic[1] == 'K') && (magic[2] == 3)
            // && (magic[3] == 4)) {
            // retVal = this.parseStreamSourceAsOdf(streamSource);
            // }
        }
        if (retVal == null) {
            retVal = this.parseStreamSourceAsXml(streamSource);
        }
        return retVal;
    }

    /**
     * Parse a given StreamSource which represents an ODF document.
     * 
     * @param streamSource
     *            StreamSource to parse.
     * @return the Document contained within.
     * @throws SAXException
     *             if a parse error occurred.
     * @throws IOException
     *             if an I/O error occurred.
     */
    public Document parseStreamSourceAsOdf(final StreamSource streamSource)
            throws IOException, SAXException {
        final InputStream is = streamSource.getInputStream();
        if (is == null) {
            throw new IllegalArgumentException(Parser.BAD_STREAM_SOURCE
                    + streamSource);
        }
        final ZipInputStream zipStream = new ZipInputStream(is);
        Document document = null;
        ZipEntry entry = zipStream.getNextEntry();
        while (entry != null) {
            if (Parser.CONTENT_XML.equals(entry.getName())) {
                document = this.getDocumentBuilder().parse(zipStream);
                entry = null;
            } else {
                entry = zipStream.getNextEntry();
            }
        }
        return document;
    }

    /**
     * Parse a given StreamSource which represents an XML document.
     * 
     * @param streamSource
     *            StreamSource to parse.
     * @return the Document contained within.
     * @throws SAXException
     *             if a parse error occurred.
     * @throws IOException
     *             if an I/O error occurred.
     */
    public Document parseStreamSourceAsXml(final StreamSource streamSource)
            throws SAXException, IOException {
        InputSource inp = null;
        final String systemId = streamSource.getSystemId();
        if (systemId != null) {
            inp = new InputSource(systemId);
        }
        final InputStream is = streamSource.getInputStream();
        if ((inp == null) && (is != null)) {
            inp = new InputSource(is);
        }
        final Reader ir = streamSource.getReader();
        if ((inp == null) && (ir != null)) {
            inp = new InputSource(ir);
        }

        if (inp == null) {
            throw new IllegalArgumentException(Parser.BAD_STREAM_SOURCE
                    + streamSource);
        }

        return this.getDocumentBuilder().parse(inp);
    }

    /**
     * Retrieve a DocumentBuilder suitable for MathML parsing.
     * <p>
     * Please note:
     * <ul>
     * <li>There is one instance of the builder per thread.</li>
     * <li>The builder instance is not thread safe, so it may not be passed
     * among threads.</li>
     * <li>Multiple Threads may call getDocumentBuilder concurrently</li>
     * </ul>
     * 
     * @return a DocumentBuilder
     */
    public DocumentBuilder getDocumentBuilder() {
        // Note: No synchronization needed, as id will be different for every
        // thread!
        final long id = Thread.currentThread().getId();
        final Reference<DocumentBuilder> builderRef = this.builders.get(id);
        if (builderRef != null) {
            final DocumentBuilder builder = builderRef.get();
            if (builder != null) {
                return builder;
            }
        }
        final DocumentBuilder builder = this.createDocumentBuilder();
        this.builders.put(id, new SoftReference<DocumentBuilder>(builder));
        return builder;
    }

    /**
     * Extract the top Node from a given Source.
     * 
     * @param source
     *            the Source to use. Currently supported are {@link DOMSource} ,
     *            {@link StreamSource}
     * @return the top NODE.
     * @throws SAXException
     *             if a parse error occurred.
     * @throws IOException
     *             if an I/O error occurred.
     */
    public Node parse(final Source source) throws SAXException, IOException {
        final Node retVal;
        if (source instanceof StreamSource) {
            final StreamSource streamSource = (StreamSource) source;
            retVal = this.parseStreamSource(streamSource);
        } else if (source instanceof ImageSource) {
            final ImageSource imageSource = (ImageSource) source;
            final StreamSource streamSource = new StreamSource(imageSource
                    .getInputStream());
            retVal = this.parseStreamSource(streamSource);
        } else if (source instanceof DOMSource) {
            final DOMSource domSource = (DOMSource) source;
            retVal = domSource.getNode();
        } else {
            try {
                final Transformer t = TransformerFactory.newInstance()
                        .newTransformer();
                final DOMResult r = new DOMResult();
                t.transform(source, r);
                retVal = r.getNode();
            } catch (final TransformerException e) {
                Parser.LOGGER.warn(e.getMessage());
                throw new IllegalArgumentException(Parser.CANNOT_HANDLE_SOURCE
                        + source, e);
            }
        }
        return retVal;
    }
}

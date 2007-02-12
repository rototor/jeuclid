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

/* $Id: ODFSupport.java,v 1.5.2.4 2007/01/31 22:50:24 maxberger Exp $ */

package net.sourceforge.jeuclid.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.jeuclid.DOMMathBuilder;
import net.sourceforge.jeuclid.MathBase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Utility class for the support of Opendocument Formula (ODF) files.
 * <p>
 * This class supports parsing of files that are either MathML or Opendocument
 * Formula (ODF) files.
 * 
 * @author Max Berger
 * @version $Revision: 1.5.2.4 $ $Date: 2007/01/31 22:50:24 $
 */
public class ODFSupport {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ODFSupport.class);

    private ODFSupport() {
    }

    /**
     * Parse an input file and return the MathBase object.
     * 
     * @param params
     * 
     * @param inFile
     *            the file to parse.
     * @param log
     *            logger instance to use.
     * @return the MathBase object.
     * @throws SAXException
     *             if a parse error occurs.
     * @throws IOException
     *             if a read io error occurs.
     */
    public static MathBase createMathBaseFromDocument(Document document,
            Map<ParameterKey, String> params) throws SAXException, IOException {
        MathBase base = new MathBase(params);

        if (document != null) {
            new DOMMathBuilder(document, base);
        }
        return base;
    }

    /**
     * Parse an input file and return the MathBase object.
     * 
     * @param inFile
     *            the file to parse.
     * @param log
     *            logger instance to use.
     * @return the MathBase object.
     * @throws SAXException
     *             if a parse error occurs.
     * @throws IOException
     *             if a read io error occurs.
     */
    public static MathBase createMathBaseFromFile(File inFile,
            Map<ParameterKey, String> params) throws SAXException, IOException {
        Document document = parseFile(inFile);
        return createMathBaseFromDocument(document, params);
    }

    /*
     * Parse an input file and return the DOM tree.
     * 
     * @param inFile the file to parse. @param log logger instance to use.
     * @return the DOM Tree @throws SAXException if a parse error occurs.
     * @throws IOException if a read io error occurs.
     */
    public static Document parseFile(File inFile) throws SAXException,
            IOException {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        Document document = null;
        DocumentBuilder parser = null;
        try {
            parser = documentBuilderFactory.newDocumentBuilder();
            parser.setEntityResolver(new ResourceEntityResolver());
            parser.setErrorHandler(new ErrorHandler() {
                public void error(SAXParseException exception)
                        throws SAXException {
                    throw exception;
                }

                public void fatalError(SAXParseException exception)
                        throws SAXException {
                    throw exception;
                }

                public void warning(SAXParseException exception)
                        throws SAXException {
                    throw exception;
                }
            });
            document = parser.parse(inFile.toURI().toString());
        } catch (SAXParseException se) {
            try {
                // Also try as odf:
                ZipFile zipFile = new ZipFile(inFile);
                ZipEntry contentEntry = zipFile.getEntry("content.xml");
                InputStream contentStream = zipFile
                        .getInputStream(contentEntry);
                document = parser.parse(new InputSource(contentStream));
            } catch (SAXParseException e2) {
                // Not ODF, throw regular error
                // and ignore all errors here
                logger.warn("Parsing failed: " + se.getMessage(), e2);
                throw se;
            } catch (IOException io) {
                // Not ODF, throw regular error
                // and ignore all errors here
                logger.warn("Parsing failed: " + se.getMessage(), io);
                throw se;
            }
        } catch (ParserConfigurationException pce) {
            logger.fatal("Parsing failed: " + pce.getMessage(), pce);
        }
        return document;
    }

}

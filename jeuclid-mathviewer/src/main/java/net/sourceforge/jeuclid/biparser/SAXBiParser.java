/*
 * Copyright 2009 - 2010 JEuclid, http://jeuclid.sf.net
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

/* $Id $ */

package net.sourceforge.jeuclid.biparser;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * this class is creates a SAXParser as singleton.
 * 
 * @version $Revision$
 */
public final class SAXBiParser {

    /** hide constructor. */
    private SAXBiParser() {
    }

    /** make a new SAXBiParser object as singleton. */
    private static final class SingletonHolder {

        /** make only one instance of SAXBiParser. */
        private static final SAXBiParser INSTANCE = new SAXBiParser();

        /** hide constructor. */
        private SingletonHolder() {
        }
    }

    /**
     * get the instance of the SAXParser.
     * 
     * @return the singleton instance of the SAXParser
     */
    public static SAXBiParser getInstance() {
        return SAXBiParser.SingletonHolder.INSTANCE;
    }

    /**
     * parse a text with SAXParser.
     * 
     * @param text
     *            inputtext to parse
     * @return result BiTree of parsed inputtext
     */
    public BiTree parse(final String text) {
        BiTree tree;
        DefaultHandler handler;
        SAXParserFactory factory;
        SAXParser saxParser;
        StringReader inStream;
        InputSource inSource;

        tree = new BiTree();
        handler = new JEuclidSAXHandler(text, tree);
        factory = SAXParserFactory.newInstance();
        inStream = new StringReader(text);
        inSource = new InputSource(inStream);

        try {
            factory.setNamespaceAware(true);
            factory.setValidating(false);

            saxParser = factory.newSAXParser();
            saxParser.parse(inSource, handler);
        } catch (final SAXParseException e) {
            tree = null;
        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
            tree = null;
        } catch (final SAXException e) {
            e.printStackTrace();
            tree = null;
        } catch (final IOException e) {
            e.printStackTrace();
            tree = null;
        }
        return tree;
    }
}

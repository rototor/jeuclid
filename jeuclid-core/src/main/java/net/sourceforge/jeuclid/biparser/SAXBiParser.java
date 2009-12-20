package net.sourceforge.jeuclid.biparser;

import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


public class SAXBiParser {
    private SAXBiParser() {
    }

    private static final class SingletonHolder {
        private static final SAXBiParser INSTANCE = new SAXBiParser();

        private SingletonHolder() {
        }
    }

    /**
     * @return the singleton instance of the DOMBuilder
     */
    public static SAXBiParser getInstance() {
        return SAXBiParser.SingletonHolder.INSTANCE;
    }

    public BiTree parse(String text) {
        BiTree tree;
        DefaultHandler handler;
        SAXParserFactory factory;

        tree = new BiTree();
        handler = new JEuclidSAXHandler(text, tree);
        factory = SAXParserFactory.newInstance();

        try {
            SAXParser saxParser = factory.newSAXParser();
            factory.setNamespaceAware(true);
            StringReader inStream = new StringReader(text);
            InputSource inSource = new InputSource(inStream);
            saxParser.parse(inSource, handler);
        } catch (SAXParseException e) {
            tree = null;
        } catch (Throwable t) {
            t.printStackTrace();
            tree = null;
        }

        return tree;
    }
}
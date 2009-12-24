package net.sourceforge.jeuclid.biparser;

import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * this class is creates a SAXParser as singleton
  *
 * @author dominik
 */
public class SAXBiParser {
    private SAXBiParser() {
    }

    private static final class SingletonHolder {
        private static final SAXBiParser INSTANCE = new SAXBiParser();

        private SingletonHolder() {
        }
    }

    /**
     * get the instance of the SAXParser
     * @return the singleton instance of the SAXParser
     */
    public static SAXBiParser getInstance() {
        return SAXBiParser.SingletonHolder.INSTANCE;
    }

    /**
     * parse a text with SAXParser
     * @param text inputtext to parse
     * @return result BiTree of parsed inputtext
     */
    public BiTree parse(String text) {
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
        } catch (SAXParseException e) {
            tree = null;
        } catch (Throwable t) {
            t.printStackTrace();
            tree = null;
        }

        return tree;
    }
}
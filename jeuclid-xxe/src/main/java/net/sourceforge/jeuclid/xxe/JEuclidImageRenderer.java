package net.sourceforge.jeuclid.xxe;

import java.awt.Image;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.converter.Converter;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.xmlmind.xmledit.imagetoolkit.ImageRenderer;
import com.xmlmind.xmledit.imagetoolkit.ImageRendererAdapter;
import com.xmlmind.xmledit.imagetoolkit.ImageToolkitUtil;

public class JEuclidImageRenderer extends ImageRendererAdapter {

    private static JEuclidImageRenderer renderer;

    private JEuclidImageRenderer() {
    }

    /** {@inheritDoc} */
    @Override
    public Image createImage(final URL url, final double width,
            final int widthType, final double height, final int heightType,
            final boolean preserveAspectRatio, final boolean smooth,
            final double screenResolution) throws Exception {

        Document doc = null;
        try {
            doc = MathMLParserSupport.parseInputStreamXML((InputStream) url
                    .getContent());
        } catch (SAXException se) {
            try {
                doc = MathMLParserSupport
                        .parseInputStreamODF((InputStream) url.getContent());
            } catch (SAXException s2) {
                throw se;
            }
        }
        final MathBase base = MathMLParserSupport.createMathBaseFromDocument(
                doc, MathBase.getDefaultParameters());
        final Image mml = Converter.getConverter().render(base);
        final Image scaledImage = ImageToolkitUtil.scaleImage(mml, width,
                widthType, height, heightType, preserveAspectRatio, smooth);
        return scaledImage;
    }

    public static ImageRenderer getRenderer() {
        if (JEuclidImageRenderer.renderer == null) {
            JEuclidImageRenderer.renderer = new JEuclidImageRenderer();
        }
        return JEuclidImageRenderer.renderer;
    }

}

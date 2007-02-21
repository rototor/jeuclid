package net.sourceforge.jeuclid.test;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.Map;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.util.Converter;
import net.sourceforge.jeuclid.util.MathMLParserSupport;
import net.sourceforge.jeuclid.util.ParameterKey;

import org.testng.annotations.Test;
import org.w3c.dom.Document;

public class ConverterTest {

    /**
     * Test string with xml header.
     */
    public static String TEST1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
            + "<mrow><munderover><mo>&#x0222B;</mo><mn>1</mn><mi>x</mi></munderover>"
            + "<mfrac><mi>dt</mi><mi>t</mi></mfrac></mrow></math>";

    @Test
    public void testConverterPNG() throws Exception {
        Document doc = MathMLParserSupport.parseString(TEST1);
        File outFile = new File(getOutDir(), "test1.png");
        Map<ParameterKey, String> params = MathBase.getDefaultParameters();
        params.put(ParameterKey.FontSize, "25");
        params.put(ParameterKey.OutFileType, "image/png");
        Converter.convert(doc, outFile, params);
        assertTrue(outFile.exists());
        assertTrue(outFile.length() > 0);

        // displayDocument(doc);
    }

    @Test
    public void testConverterSVG() throws Exception {
        Document doc = MathMLParserSupport.parseString(TEST1);
        File outFile = new File(getOutDir(), "test1.svg");
        Map<ParameterKey, String> params = MathBase.getDefaultParameters();
        params.put(ParameterKey.FontSize, "25");
        params.put(ParameterKey.OutFileType, Converter.TYPE_SVG);
        Converter.convert(doc, outFile, params);
        assertTrue(outFile.exists());
        assertTrue(outFile.length() > 0);

        // displayDocument(doc);
    }

    public File getOutDir() {
        File outDir = new File("temp");
        if (!outDir.isDirectory())
            outDir.mkdirs();
        return outDir;
    }

    /**
     * Tests the examples at resources/test/exampleX.mml.
     * 
     * @throws Exception
     *             if an error occurs.
     */
    @Test
    public void testConvertEmbeddedExamples() throws Exception {
        Map<ParameterKey, String> params = MathBase.getDefaultParameters();
        params.put(ParameterKey.FontSize, "16");
        params.put(ParameterKey.OutFileType, "image/png");

        for (int example = 1; example <= 8; example++) {
            String exName = "example" + example + ".mml";
            File outFile = new File(getOutDir(), "example" + example + ".png");
            Document document = MathBaseTest.loadDocument(exName);
            Converter.convert(document, outFile, params);
            assertTrue(outFile.exists());
            assertTrue(outFile.length() > 0);
        }
    }

    @Test
    public void testConverterMimeTypes() throws Exception {
        assertTrue(Converter.TYPE_SVG.equalsIgnoreCase(Converter
                .getMimeTypeForSuffix(Converter.EXTENSION_SVG)));
        assertTrue(Converter.EXTENSION_SVG.equalsIgnoreCase(Converter
                .getSuffixForMimeType(Converter.TYPE_SVG)));

        assertTrue("image/png".equalsIgnoreCase(Converter
                .getMimeTypeForSuffix("png")));
        assertTrue("png".equalsIgnoreCase(Converter
                .getSuffixForMimeType("image/png")));

    }

}

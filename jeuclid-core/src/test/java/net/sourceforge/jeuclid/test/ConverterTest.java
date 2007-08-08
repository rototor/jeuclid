package net.sourceforge.jeuclid.test;

import java.io.File;

import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.LayoutContext.Parameter;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.converter.Converter;
import net.sourceforge.jeuclid.converter.ConverterRegistry;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

public class ConverterTest {

    /**
     * Test string with xml header.
     */
    public static final String TEST1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
            + "<mrow><munderover><mo>&#x0222B;</mo><mn>1</mn><mi>x</mi></munderover>"
            + "<mfrac><mi>dt</mi><mi>t</mi></mfrac></mrow></math>";

    @Test
    public void testConverterPNG() throws Exception {
        final Document doc = MathMLParserSupport
                .parseString(ConverterTest.TEST1);
        final File outFile = new File(this.getOutDir(), "test1.png");
        final MutableLayoutContext params = LayoutContextImpl
                .getDefaultLayoutContext();
        params.setParameter(Parameter.MATHSIZE, 25f);

        Converter.getConverter().convert(doc, outFile, "image/png", params);
        Assert.assertTrue(outFile.exists());
        Assert.assertTrue(outFile.length() > 0);

        // displayDocument(doc);
    }

    @Test
    public void testConverterSVG() throws Exception {
        final Document doc = MathMLParserSupport
                .parseString(ConverterTest.TEST1);
        final File outFile = new File(this.getOutDir(), "test1.svg");
        final MutableLayoutContext params = LayoutContextImpl
                .getDefaultLayoutContext();
        params.setParameter(Parameter.MATHSIZE, 25f);
        Converter.getConverter().convert(doc, outFile,
                net.sourceforge.jeuclid.converter.Converter.TYPE_SVG, params);
        Assert.assertTrue(outFile.exists());
        Assert.assertTrue(outFile.length() > 0);

        // displayDocument(doc);
    }

    public File getOutDir() {
        final File outDir = new File("temp");
        if (!outDir.isDirectory()) {
            outDir.mkdirs();
        }
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
        final MutableLayoutContext params = LayoutContextImpl
                .getDefaultLayoutContext();
        params.setParameter(Parameter.MATHSIZE, 16f);

        for (int example = 1; example <= 9; example++) {
            final String exName = "example" + example + ".mml";
            final File outFile = new File(this.getOutDir(), "example"
                    + example + ".png");
            final Document document = MathBaseTest.loadDocument(exName);
            Converter.getConverter().convert(document, outFile, "image/png",
                    params);
            Assert.assertTrue(outFile.exists());
            Assert.assertTrue(outFile.length() > 0);
        }
    }

    @Test
    public void testConverterMimeTypes() throws Exception {
        Assert
                .assertTrue(net.sourceforge.jeuclid.converter.Converter.TYPE_SVG
                        .equalsIgnoreCase(ConverterRegistry
                                .getRegisty()
                                .getMimeTypeForSuffix(
                                        net.sourceforge.jeuclid.converter.Converter.EXTENSION_SVG)));
        Assert
                .assertTrue(net.sourceforge.jeuclid.converter.Converter.EXTENSION_SVG
                        .equalsIgnoreCase(ConverterRegistry
                                .getRegisty()
                                .getSuffixForMimeType(
                                        net.sourceforge.jeuclid.converter.Converter.TYPE_SVG)));

        final String pngType = ConverterRegistry.getRegisty()
                .getMimeTypeForSuffix("png");
        Assert.assertTrue("image/png".equalsIgnoreCase(pngType)
                || "image/x-png".equalsIgnoreCase(pngType));
        Assert.assertTrue("png".equalsIgnoreCase(ConverterRegistry
                .getRegisty().getSuffixForMimeType("image/png")));

    }

}

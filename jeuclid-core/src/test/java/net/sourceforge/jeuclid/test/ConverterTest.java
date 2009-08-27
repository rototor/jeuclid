/*
 * Copyright 2002 - 2008 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.test;

import java.awt.image.BufferedImage;
import java.io.File;

import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.converter.Converter;
import net.sourceforge.jeuclid.converter.ConverterRegistry;
import net.sourceforge.jeuclid.converter.ConverterPlugin.DocumentWithDimension;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

/**
 * @version $Revision$
 */
public class ConverterTest {

    /**
     * Test string with xml header.
     */
    public static final String TEST1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
            + "<mrow><munderover><mo>&#x0222B;</mo><mn>1</mn><mi>x</mi></munderover>"
            + "<mfrac><mi>dt</mi><mi>t</mi></mfrac></mrow></math>";

    /**
     * Tests if PNG converter is available.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testConverterPNG() throws Exception {
        final Document doc = MathMLParserSupport
                .parseString(ConverterTest.TEST1);
        final File outFile = new File(this.getOutDir(), "test1.png");
        final MutableLayoutContext params = new LayoutContextImpl(
                LayoutContextImpl.getDefaultLayoutContext());
        params.setParameter(Parameter.MATHSIZE, 25f);

        Converter.getInstance().convert(doc, outFile, "image/png", params);
        Assert.assertTrue(outFile.exists());
        Assert.assertTrue(outFile.length() > 0);
    }

    /**
     * Tests if SVG converter is available and creates an output file.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testConverterSVG() throws Exception {
        final Document doc = MathMLParserSupport
                .parseString(ConverterTest.TEST1);
        final File outFile = new File(this.getOutDir(), "test1.svg");
        final MutableLayoutContext params = new LayoutContextImpl(
                LayoutContextImpl.getDefaultLayoutContext());
        params.setParameter(Parameter.MATHSIZE, 25f);
        Converter.getInstance().convert(doc, outFile,
                net.sourceforge.jeuclid.converter.Converter.TYPE_SVG, params);
        Assert.assertTrue(outFile.exists(), "SVG file was not created");
        Assert.assertTrue(outFile.length() > 0, "SVG file is empty");
        Assert.assertTrue(outFile.length() > 2048, "SVG file is too small");
    }

    /**
     * Tests if PDF converter is available and creates an output file.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testConverterPDF() throws Exception {
        final Document doc = MathMLParserSupport
                .parseString(ConverterTest.TEST1);
        final File outFile = new File(this.getOutDir(), "test1.pdf");
        final MutableLayoutContext params = new LayoutContextImpl(
                LayoutContextImpl.getDefaultLayoutContext());
        params.setParameter(Parameter.MATHSIZE, 25f);
        Converter.getInstance()
                .convert(doc, outFile, "application/pdf", params);
        Assert.assertTrue(outFile.exists(), "PDF file was not created");
        Assert.assertTrue(outFile.length() > 0, "PDF file is empty");
        Assert.assertTrue(outFile.length() > 2048, "PDF file is too small");
    }

    /**
     * Tests if SVG converter is available and creates an output DOM.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testConverterSVGtoDOM() throws Exception {
        final Document doc = MathMLParserSupport
                .parseString(ConverterTest.TEST1);
        final MutableLayoutContext params = new LayoutContextImpl(
                LayoutContextImpl.getDefaultLayoutContext());
        params.setParameter(Parameter.MATHSIZE, 25f);
        final DocumentWithDimension svgdocdim = Converter.getInstance()
                .convert(doc,
                        net.sourceforge.jeuclid.converter.Converter.TYPE_SVG,
                        params);
        Assert.assertNotNull(svgdocdim);
        final Document svgdoc = svgdocdim.getDocument();
        Assert.assertNotNull(svgdoc);
        Assert.assertTrue(svgdoc instanceof SVGDocument,
                "Document is not a SVGDocument");
        final Element e = (Element) svgdoc.getFirstChild();
        Assert.assertEquals("svg", e.getLocalName());
        Assert.assertTrue(e.getChildNodes().getLength() > 0,
                "SVG Document is empty!");
        Assert.assertTrue(svgdocdim.getDimension().height > 1);
        Assert.assertTrue(svgdocdim.getDimension().width > 1);
    }

    /**
     * Create and return temp directory.
     * 
     * @return temp directory.
     */
    public File getOutDir() {
        final File outDir = new File("temp");
        if (!outDir.isDirectory()) {
            final boolean success = outDir.mkdirs();
            Assert
                    .assertTrue(
                            success,
                            "Failed to create temp directory. Please delete all files / directories named temp");
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
        final MutableLayoutContext params = new LayoutContextImpl(
                LayoutContextImpl.getDefaultLayoutContext());
        params.setParameter(Parameter.MATHSIZE, 16f);

        for (int example = 1; example <= 10; example++) {
            final String exName = "example" + example + ".mml";
            final File outFile = new File(this.getOutDir(), "example" + example
                    + ".png");
            final Document document = MathBaseTest.loadDocument(exName);
            Converter.getInstance().convert(document, outFile, "image/png",
                    params);
            Assert.assertTrue(outFile.exists());
            Assert.assertTrue(outFile.length() > 0);
        }
    }

    /**
     * Tests if some mime-types are registered properly.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testConverterMimeTypes() throws Exception {
        Assert
                .assertTrue(net.sourceforge.jeuclid.converter.Converter.TYPE_SVG
                        .equalsIgnoreCase(ConverterRegistry
                                .getInstance()
                                .getMimeTypeForSuffix(
                                        net.sourceforge.jeuclid.converter.Converter.EXTENSION_SVG)));
        Assert
                .assertTrue(net.sourceforge.jeuclid.converter.Converter.EXTENSION_SVG
                        .equalsIgnoreCase(ConverterRegistry
                                .getInstance()
                                .getSuffixForMimeType(
                                        net.sourceforge.jeuclid.converter.Converter.TYPE_SVG)));

        final String pngType = ConverterRegistry.getInstance()
                .getMimeTypeForSuffix("png");
        Assert.assertTrue("image/png".equalsIgnoreCase(pngType)
                || "image/x-png".equalsIgnoreCase(pngType));
        Assert.assertTrue("png".equalsIgnoreCase(ConverterRegistry
                .getInstance().getSuffixForMimeType("image/png")));

    }

    /**
     * Tests if SVG converter is available and creates an output file.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testConverterBufferedImage() throws Exception {
        final Document doc = MathMLParserSupport
                .parseString(ConverterTest.TEST1);
        final MutableLayoutContext params = new LayoutContextImpl(
                LayoutContextImpl.getDefaultLayoutContext());
        params.setParameter(Parameter.MATHSIZE, 25f);
        BufferedImage bi = Converter.getInstance().render(doc, params);
        Assert.assertTrue(bi.getWidth() > 10,
                "Image Created was not wide enough");
        Assert.assertTrue(bi.getHeight() > 10,
                "Image Created was not tall enough");
        int ltpixel = bi.getRGB(0, 0);
        Assert.assertEquals(ltpixel, 0, "Expected Transparent Pixel, got "
                + ltpixel);
    }

}

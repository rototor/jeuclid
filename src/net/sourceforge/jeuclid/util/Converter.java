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

/* $Id: Converter.java,v 1.6.2.4 2007/01/31 22:50:24 maxberger Exp $ */

package net.sourceforge.jeuclid.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import net.sourceforge.jeuclid.MathBase;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.shetline.io.GIFOutputStream;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Utility class for conversion from MathML to other formats.
 * <p>
 * This class supports easy conversion from a MathML or ODF file to any
 * supported output format.
 * <p>
 * Currently supported output formats:
 * <ul>
 * <li>image/svg+xml
 * <li>image/gif
 * </ul>
 * 
 * @author Max Berger
 * @version $Revision: 1.6.2.4 $ $Date: 2007/01/31 22:50:24 $
 */
final public class Converter {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Converter.class);

    /**
     * Mime type for SVG.
     */
    public static final String TYPE_SVG = "image/svg+xml";

    /**
     * Mime type for GIF.
     */
    public static final String TYPE_GIF = "image/gif";

    private Converter() {
    }

    /**
     * Converts an existing file from MathML or ODF to the given type.
     * 
     * @param inFile
     *            input file.
     * @param outFile
     *            output file.
     * @param outFileType
     *            mimetype for the output file.
     * @param log
     *            logger instance to use.
     * @return true if the conversion was sucessful.
     * @throws IOException
     *             if an io error occured during read or write.
     */
    public static boolean convert(File inFile, File outFile, String outFileType)
            throws IOException {
        Document doc;
        try {
            doc = ODFSupport.parseFile(inFile);
            return convert(doc, outFile, outFileType);
        } catch (SAXException e) {
            logger.error("Failed to parse file:" + inFile, e);
            return false;
        }
    }

    /**
     * Converts an existing file from MathML or ODF to the given type.
     * 
     * @param inStream
     *            input file.
     * @param outFile
     *            output file.
     * @param outFileType
     *            mimetype for the output file.
     * @param log
     *            logger instance to use.
     * @return true if the conversion was sucessful.
     * @throws IOException
     *             if an io error occured during read or write.
     */
    public static boolean convert(Document doc, File outFile, String outFileType)
            throws IOException {
        Map params = MathBase.getDefaultParameters();
        params.put(ParameterKey.OutFileType, outFileType);
        return convert(doc, outFile, params);
    }

    /**
     * Converts an existing file from MathML or ODF to the given type.
     * 
     * @param inStream
     *            input file.
     * @param outFile
     *            output file.
     * @param outFileType
     *            mimetype for the output file.
     * @param log
     *            logger instance to use.
     * @return true if the conversion was sucessful.
     * @throws IOException
     *             if an io error occured during read or write.
     */
    public static boolean convert(Document doc, File outFile,
            Map<ParameterKey, String> params) throws IOException {

        try {
            logger.info("Converting " + doc + " to " + outFile + " ...");

            MathBase base = ODFSupport.createMathBaseFromDocument(doc, params);

            if (TYPE_GIF.equalsIgnoreCase(params.get(ParameterKey.OutFileType))) {
                BufferedImage tempimage = new BufferedImage(1, 1,
                        BufferedImage.TYPE_INT_RGB);
                Graphics tempg = tempimage.getGraphics();
                int width = base.getWidth(tempg);
                int height = base.getHeight(tempg);

                BufferedImage image = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics g = image.createGraphics();

                Color transparency = new Color(78, 91, 234);

                g.setColor(transparency);
                g.fillRect(0, 0, width, height);
                g.setColor(Color.black);

                base.paint(g);

                BufferedOutputStream buffer = new BufferedOutputStream(
                        new FileOutputStream(outFile));
                GIFOutputStream.writeGIF(buffer, image,
                        GIFOutputStream.ORIGINAL_COLOR, transparency);
                buffer.flush();
                buffer.close();
            } else if (TYPE_SVG.equalsIgnoreCase(params
                    .get(ParameterKey.OutFileType))) {

                // Get a DOMImplementation
                DOMImplementation domImpl = GenericDOMImplementation
                        .getDOMImplementation();

                // Create an instance of org.w3c.dom.Document
                Document document = domImpl.createDocument(null, "svg", null);

                // Create an instance of the SVG Generator
                SVGGeneratorContext svgContext = SVGGeneratorContext
                        .createDefault(document);
                svgContext.setComment("Converted from MathML using JEuclid");
                SVGGraphics2D svgGenerator = new SVGGraphics2D(svgContext, true);
                // Ask the test to render into the SVG Graphics2D implementation

                svgGenerator.setSVGCanvasSize(new Dimension(base
                        .getWidth(svgGenerator), base.getHeight(svgGenerator)));
                base.paint(svgGenerator);

                svgGenerator.stream(outFile.getAbsolutePath());

            } else {

                Iterator it = ImageIO.getImageWritersByMIMEType(params
                        .get(ParameterKey.OutFileType));
                if (it.hasNext()) {
                    ImageWriter writer = (ImageWriter) it.next();
                    BufferedImage tempimage = new BufferedImage(1, 1,
                            BufferedImage.TYPE_INT_ARGB);
                    Graphics tempg = tempimage.getGraphics();
                    int width = base.getWidth(tempg);
                    int height = base.getHeight(tempg);

                    BufferedImage image = new BufferedImage(width, height,
                            BufferedImage.TYPE_INT_ARGB);
                    Graphics g = image.createGraphics();

                    Color transparency = new Color(255, 255, 255, 0);

                    g.setColor(transparency);
                    g.fillRect(0, 0, width, height);
                    g.setColor(Color.black);

                    base.paint(g);

                    ImageOutputStream ios = new FileImageOutputStream(outFile);
                    writer.setOutput(ios);
                    writer.write(image);
                    ios.close();

                } else {
                    logger.fatal("Unsupported output type: "
                            + params.get(ParameterKey.OutFileType));
                    return false;
                }
            }
        } catch (SAXException ex) {
            logger.fatal("Failed to process: " + ex.getMessage(), ex);
            if (outFile != null) {
                outFile.delete();
            }
            return false;
        }
        return true;
    }

    /**
     * Main function for use from scripts.
     * 
     * @param args
     *            command line arguments.
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            showUsage();
        } else {
            try {
                convert(new File(args[0]), new File(args[1]), args[2]);
            } catch (IOException e) {
                System.out.println(e.getClass().toString() + ": "
                        + e.getMessage());
                System.out.println();
                showUsage();
            }
        }
    }

    private static void showUsage() {
        System.out.println("JEuclid Converter");
        System.out.println("");
        System.out.println("Usage:");
        System.out.println("");
        System.out.println("Converter source target targettype");
        System.out.println("");
        System.out.println("where:");
        System.out
                .println(" source is the path to the source file (MathML or ODF format)");
        System.out.println(" target is the path to the target file");
        System.out.println(" targettype is one of the supported types:");
        System.out.print("    " + TYPE_GIF + " " + TYPE_SVG);
        String[] iiotypes = ImageIO.getWriterMIMETypes();
        for (int i = 0; i < iiotypes.length; i++) {
            System.out.print(" " + iiotypes[i]);
        }
        System.out.println();
        System.out.println();

    }
}

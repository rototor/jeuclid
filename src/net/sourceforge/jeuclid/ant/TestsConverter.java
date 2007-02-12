/*
 * Copyright 2002 - 2006 JEuclid, http://jeuclid.sf.net
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

/* $Id: TestsConverter.java,v 1.3 2006/08/07 01:17:40 maxberger Exp $ */

package net.sourceforge.jeuclid.ant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Options;
import org.apache.fop.apps.XSLTInputHandler;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.XMLReader;


public class TestsConverter extends MatchingTask {
    /**
     * XSL:FO stylesheet that render test xml into PDF.
     */
    private File m_style;

    /**
     * Input directory where testsXXX directories are resided.
     */
    private File m_inDir;

    /**
     * Output directory where to place result pdf's.
     */
    private File m_outDir;

    /**
     * Fonts config file for FOP
     */
    private File m_fontConfig;

    /**
     * Directory where needed dtd's are resided.
     */
    private File m_dtdDir;

    /**
     * HTTP share directory where to place result pdf's.
     */
    private String m_httpShare;

    /**
     * Constructor.
     */
    public TestsConverter() {
        super();
    }

    /**
     * Executes the task.
     *
     * @throws org.apache.tools.ant.BuildException
     *          If the task could not build the output pdf
     */
    public void execute() throws BuildException {
        Driver driver = new Driver();
        driver.setRenderer(Driver.RENDER_PDF);

        // setup FOP config
        if (m_fontConfig != null) {
            if (!m_fontConfig.exists()) {
                System.out.println("WARNING: FOP config file was not found:" + m_fontConfig);
            } else {
                try {
                    new Options(m_fontConfig);
                } catch (FOPException e) {
                    System.out.println("ERROR: FOP config cannot be applied:" + e.getMessage());
                }
            }
        }

        if (m_inDir == null) {
            throw new BuildException("ERROR: tests source directory, \"tests.inDir\" ,is not specified");
        }
        if (m_outDir == null) {
            throw new BuildException("ERROR: tests output directory, \"tests.outDir\" ,is not specified");
        }

        DirectoryScanner scanner = getDirectoryScanner(m_inDir);
        String[] dirs = scanner.getIncludedDirectories();

        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        Document doc;
        try {
            doc = documentBuilderFactory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e1) {
            doc = null;
        }
        Element root = doc.createElement("test_results"); // Create Root
                                                            // Element

        for (int i = 0; i < dirs.length; i++) {
            if (!dirs[i].trim().equals("")) {
                setIncludes("*.xml");
                File testDir = new File(m_inDir, dirs[i]);
                DirectoryScanner testScanner = getDirectoryScanner(testDir);
                String[] xmls = testScanner.getIncludedFiles();
                for (int j = 0; j < xmls.length; j++) {
                    String pdfName = xmls[j].substring(0, xmls[j].length() - 3) + "pdf";
                    File outPdf = new File(m_outDir, pdfName);
                    process(driver, new File(testDir, xmls[j]), outPdf);

                    Document xmlDoc;
                    try {
                        final DocumentBuilder parser = documentBuilderFactory
                                .newDocumentBuilder();
                        xmlDoc = parser
                                .parse(testDir.getPath() + "/" + xmls[j]);
                    } catch (Exception e) {
                        throw new BuildException("Can't found input source for FOP:" + e.getMessage(), e);
                    }
                    String desc = xmlDoc.getDocumentElement().getAttribute("description");
                    Element description = doc.createElement("description");
                    description.appendChild(doc.createTextNode(desc));

                    Element name = doc.createElement("pdf_name");
                    name.appendChild(doc.createTextNode(outPdf.getName()));

                    Element pdfFile = doc.createElement("pdf_file");
                    pdfFile.appendChild(doc.createTextNode(m_httpShare + "/" + outPdf.getName()));

                    Element result = doc.createElement("test_result");
                    result.appendChild(description);
                    result.appendChild(name);
                    result.appendChild(pdfFile);
                    root.appendChild(result);
                }
            }
        }
        doc.appendChild(root);
        try {
            final File f = new File(m_outDir.getAbsolutePath(),
                    "testsresults.xml");
            final StreamResult sr = new StreamResult(f);
            final Transformer t = TransformerFactory.newInstance()
                    .newTransformer();
            t.transform(new DOMSource(doc), sr);

        } catch (Exception e) {
            throw new BuildException("ERROR: Error during creating tests-result.xml:"
                    + e.getMessage(), e);
        }
    }

    /**
     * Process single mml file
     *
     * @param driver     FOP Driver instance
     * @param xmlFile    XML source file
     * @param resultFile result PDF file
     * @throws BuildException in any unsatisfied case of rendering.
     */
    private void process(final Driver driver, final File xmlFile, final File resultFile)
            throws BuildException {
        if (m_style == null) {
            throw new BuildException("Stylesheet has to be typed as \"tests.stylesheet\" attribute.");
        }

        try {
            driver.setOutputStream(new FileOutputStream(resultFile));
        } catch (FileNotFoundException e) {
            throw new BuildException("Can't set output source for FOP:" + e.getMessage(), e);
        }

        try {
            XSLTInputHandler inputHandler = new XSLTInputHandler(xmlFile,
                    m_style);
            inputHandler.setParameter("testPath", xmlFile.getParent() + "\\");
            inputHandler.setParameter("dtdPath", m_dtdDir + "\\");
            XMLReader parser = inputHandler.getParser();
            driver.render(parser, inputHandler.getInputSource());
        } catch (FOPException e) {
            throw new BuildException("ERROR: Fop error during rendering:" + e.getMessage(), e);
        }
    }

    /**
     * Setter.
     *
     * @param stylesheet stylesheet
     */
    public void setStyle(final File stylesheet) {
        m_style = stylesheet;
    }

    /**
     * Setter.
     *
     * @param inDir inDir
     */
    public void setInDir(final File inDir) {
        m_inDir = inDir;
    }

    /**
     * Setter.
     *
     * @param outDir outDir
     */
    public void setOutDir(final File outDir) {
        m_outDir = outDir;
    }

    /**
     * Setter.
     *
     * @param fontConfig fontConfig
     */
    public void setFontConfig(final File fontConfig) {
        m_fontConfig = fontConfig;
    }

    /**
     * Setter.
     *
     * @param dtdDir dtdDir
     */
    public void setDtdDir(final File dtdDir) {
        m_dtdDir = dtdDir;
    }

    /**
     * Setter.
     *
     * @param httpShare httpShare
     */
    public void setHttpShare(final String httpShare) {
        m_httpShare = httpShare;
    }
}

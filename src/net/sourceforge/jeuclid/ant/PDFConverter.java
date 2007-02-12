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

/* $Id$ */

package net.sourceforge.jeuclid.ant;

import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Options;
import org.apache.fop.apps.InputHandler;
import org.apache.fop.apps.XSLTInputHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Performs convertion single MathML file or fileset of MathML files
 * into PDF.
 * This task was incidentally implemented for FOP and JEuclid contribution
 * to make FOP able to understand MathML tags.
 * <p/>
 * Important note:<br/>
 * MathML can be inserted into PDF either as image or by drawing it with pure font's
 * symbols. The way of insertin may affect on MathML formula's appearence in the
 * resulted PDF file.
 *
 */

public class PDFConverter extends MatchingTask {

    /**
     * Fonts config file for FOP
     */
    private File m_fontConfig;

    /**
     * XSL:FO stylesheet that inserts mathml xml into PDF.
     */
    private File m_foFile;

    /**
     * XSL:FO stylesheet that inserts mathml xml into PDF.
     * SXLT transformation is needed to insert m_inFile
     */
    private File m_style;

    /**
     * Input directory where *.mml files are resided.
     */
    private File m_inDir;

    /**
     * Output directory where to place result pdf.
     */
    private File m_outDir;

    /**
     * MathML xml source that have to be inserted into PDF.
     */
    private File m_mml;

    /**
     * MathML xml source that have to be inserted into PDF.
     */
    private File m_outpdf;

    /**
     * Constructor.
     */
    public PDFConverter() {
        super();
    }

    /**
     * Executes the task.
     *
     * @throws org.apache.tools.ant.BuildException
     *          If the task could not build the output pdf
     */
    public void execute()
            throws BuildException {
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

        // set input source
        if (m_foFile != null) {
            // set output stream
            try {
                driver.setOutputStream(new FileOutputStream(m_outpdf));
            } catch (FileNotFoundException e) {
                throw new BuildException("Can't set output source for FOP:" + e.getMessage(), e);
            }

            try {
                driver.setInputSource(new InputSource(new FileInputStream(m_foFile)));
            } catch (FileNotFoundException e) {
                throw new BuildException("Can't set input source for FOP:" + e.getMessage(), e);
            }
            try {
                driver.run();
            } catch (IOException e) {
                throw new BuildException("Error while FOP rendering:" + e.getMessage(), e);
            } catch (FOPException e) {
                throw new BuildException("Error while FOP rendering:" + e.getMessage(), e);
            }
        } else if (m_mml != null) {
            process(driver, m_mml, m_outpdf);
        } else if (m_inDir != null) {
            if (m_outDir == null) {
                throw new BuildException("ERROR: destination directory, \"outDir\" ,is not specified");
            }

            final DirectoryScanner scanner = getDirectoryScanner(m_inDir);
            final String[] list = scanner.getIncludedFiles();
            final String[] dirs = scanner.getIncludedDirectories();
            for (int i = 0; i < list.length; ++i) {
                final File outPdf = new File(m_outDir, list[i] + ".pdf");
                if (!outPdf.getParentFile().exists()) {
                    outPdf.getParentFile().mkdirs();
                }
                process(driver, new File(m_inDir, list[i]), outPdf);
            }

            // Process all the directoried marked for styling
            for (int j = 0; j < dirs.length; ++j) {
                File intDir = new File(m_inDir, dirs[j]);
                if (!intDir.exists()) {
                    intDir.mkdirs();
                }
                String[] internalList = intDir.list();
                for (int i = 0; i < internalList.length; ++i) {
                    process(driver, new File(m_inDir, internalList[i]), new File(m_outDir, internalList[i] + ".pdf"));
                }
            }
        }
    }

    /**
     * Process single mml file
     *
     * @param driver     FOP Driver instance
     * @param mmlFile    MathML source file
     * @param resultFile result PDF file
     * @throws BuildException in any unsatisfied case of rendering.
     */
    private void process(final Driver driver, final File mmlFile, final File resultFile)
            throws BuildException {
        if (m_style == null) {
            throw new BuildException("Stylesheet has to be typed as \"style\" attribute.");
        }

        try {
            driver.setOutputStream(new FileOutputStream(resultFile));
        } catch (FileNotFoundException e) {
            throw new BuildException("Can't set output source for FOP:" + e.getMessage(), e);
        }

        try {
            InputHandler inputHandler = new XSLTInputHandler(mmlFile, m_style);
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
     * @param foFile foFile
     */
    public void setFoFile(final File foFile) {
        m_foFile = foFile;
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
     * @param inFile inFile
     */
    public void setMml(final File inFile) {
        m_mml = inFile;
    }

    /**
     * Setter.
     *
     * @param outFile outFile
     */
    public void setOutPdf(final File outFile) {
        m_outpdf = outFile;
    }

    /**
     * Setter.
     *
     * @param fontConfig fontConfig
     */
    public void setFontConfig(final File fontConfig) {
        m_fontConfig = fontConfig;
    }
}

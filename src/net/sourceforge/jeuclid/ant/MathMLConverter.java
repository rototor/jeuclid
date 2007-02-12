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

/* $Id: MathMLConverter.java,v 1.10.2.5 2007/01/31 22:50:25 maxberger Exp $ */

package net.sourceforge.jeuclid.ant;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sourceforge.jeuclid.DOMMathBuilder;
import net.sourceforge.jeuclid.MathBase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.shetline.io.GIFOutputStream;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

/**
 * This task converts MathML files to GIF images.
 * 
 */
public class MathMLConverter extends MatchingTask {

    /**
     * 
     */
    private File m_destDir = null;

    private File m_baseDir = null;

    private File m_inFile = null;

    private File m_outFile = null;

    private boolean m_force = false;

    /**
     * Creates a new MathMLConverter Task.
     */
    public MathMLConverter() {
    }

    /**
     * Executes the task.
     * 
     * @throws org.apache.tools.ant.BuildException
     *             If the task could build the images
     */
    public void execute() throws BuildException {
        DirectoryScanner scanner;
        String[] list;
        String[] dirs;

        if (m_baseDir == null) {
            m_baseDir = getProject().resolveFile(".");
        }

        // if we have an in file and out then process them
        if ((m_inFile != null) && (m_outFile != null)) {
            process(m_inFile, m_outFile);
            return;
        }

        /*
         * if we get here, in and out have not been specified, we are in batch
         * processing mode.
         */

        // -- make sure Source directory exists...
        if (m_destDir == null) {
            String msg = "m_destDir attributes must be set!";

            throw new BuildException(msg);
        }
        scanner = getDirectoryScanner(m_baseDir);
        log("Transforming into " + m_destDir, Project.MSG_INFO);

        // Process all the files marked for styling
        list = scanner.getIncludedFiles();
        for (int i = 0; i < list.length; ++i) {
            process(m_baseDir, list[i], m_destDir);
        }

        // Process all the directoried marked for styling
        dirs = scanner.getIncludedDirectories();
        for (int j = 0; j < dirs.length; ++j) {
            list = new File(m_baseDir, dirs[j]).list();
            for (int i = 0; i < list.length; ++i) {
                process(m_baseDir, list[i], m_destDir);
            }
        }
    }

    /**
     * Set whether to check dependencies, or always generate.
     * 
     * @param force
     *            True, if the task should always generate the images.
     */
    public void setForce(boolean force) {
        this.m_force = force;
    }

    /**
     * Set the base directory.
     * 
     * @param dir
     *            Base directory
     */
    public void setBasedir(File dir) {
        m_baseDir = dir;
    }

    /**
     * Set the destination directory into which the result files should be
     * copied to.
     * 
     * @param dir
     *            Destination directory
     */
    public void setDestdir(File dir) {
        m_destDir = dir;
    }

    /**
     * Sets an out file.
     * 
     * @param outFile
     *            Output file
     */
    public void setOut(File outFile) {
        this.m_outFile = outFile;
    }

    /**
     * Sets an input xml file to be converted.
     * 
     * @param inFile
     *            Input file
     */
    public void setIn(File inFile) {
        this.m_inFile = inFile;
    }

    /**
     * Ensures if the destination directory exists
     * 
     * @param targetFile
     *            Destination directory
     * @throws BuildException
     *             If, the task could create the directory
     */
    private void ensureDirectoryFor(File targetFile) throws BuildException {
        File directory = new File(targetFile.getParent());

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new BuildException("Unable to create directory: "
                        + directory.getAbsolutePath());
            }
        }
    }

    /**
     * Processes the given input XML file and stores the result in the given
     * resultFile.
     * 
     * @param baseDir
     *            Base directory
     * @param xmlFile
     *            Source file
     * @param destDir
     *            Destination directory
     * @throws BuildException
     *             If the task couldn't convert the file
     */
    private void process(File baseDir, String xmlFile, File destDir)
            throws BuildException {

        File outFile = null;
        File inFile = null;

        try {
            inFile = new File(baseDir, xmlFile);
            int dotPos = xmlFile.lastIndexOf('.');

            if (dotPos > 0) {
                outFile = new File(destDir, xmlFile.substring(0, xmlFile
                        .lastIndexOf('.'))
                        + ".gif");
            } else {
                outFile = new File(destDir, xmlFile + ".gif");
            }
            if (m_force || (inFile.lastModified() > outFile.lastModified())) {
                ensureDirectoryFor(outFile);

                MathBase base = new MathBase(MathBase.getDefaultParameters());

                final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                        .newInstance();
                final DocumentBuilder parser = documentBuilderFactory
                        .newDocumentBuilder();
                Document document = null;
                try {
                    document = parser.parse(inFile.toString());
                } catch (SAXParseException se) {
                    log("The grammar file is not valid: " + se.getMessage());
                    return;
                }

                new DOMMathBuilder(document, base);

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
            }
        } catch (Exception ex) {
            // If failed to process document, must delete target document,
            // or it will not attempt to process it the second time
            log("Failed to process " + inFile, Project.MSG_INFO);
            if (outFile != null) {
                outFile.delete();
            }

            throw new BuildException(ex);
        }

    }

    /**
     * Processes the given input XML file and stores the result in the given
     * resultFile.
     * 
     * @param inFile
     *            Source file
     * @param outFile
     *            Destination file
     * @throws BuildException
     *             If the task couldn't convert the file
     */
    private void process(File inFile, File outFile) throws BuildException {
        try {
            log("In file " + inFile + " time: " + inFile.lastModified(),
                    Project.MSG_DEBUG);
            log("Out file " + outFile + " time: " + outFile.lastModified(),
                    Project.MSG_DEBUG);
            if (m_force || (inFile.lastModified() > outFile.lastModified())) {
                ensureDirectoryFor(outFile);
                log("Processing " + inFile + " to " + outFile, Project.MSG_INFO);

                MathBase base = new MathBase(MathBase.getDefaultParameters());

                final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                        .newInstance();
                final DocumentBuilder parser = documentBuilderFactory
                        .newDocumentBuilder();
                Document document = null;
                try {
                    document = parser.parse(inFile.toString());
                } catch (SAXParseException se) {
                    log("The grammar file is not valid: " + se.getMessage());
                    return;
                }

                new DOMMathBuilder(document, base);

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
            }
        } catch (Exception ex) {
            log("Failed to process " + inFile, Project.MSG_INFO);
            if (outFile != null) {
                outFile.delete();
            }
            throw new BuildException(ex);
        }
    }
}
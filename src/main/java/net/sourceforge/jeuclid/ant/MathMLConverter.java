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

/* $Id$ */

package net.sourceforge.jeuclid.ant;

import java.io.File;
import java.io.IOException;

import net.sourceforge.jeuclid.util.Converter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;

/**
 * This task converts MathML files to images.
 * 
 * @author Unkown
 * @author Max Berger
 * @version $Revision$ $Date$
 */
public class MathMLConverter extends MatchingTask {

    /**
     * 
     */
    private File m_destDir;

    private File m_baseDir;

    private File m_inFile;

    private File m_outFile;

    private String m_outType = "image/png";

    private boolean m_force = false;

    /**
     * Creates a new MathMLConverter Task.
     */
    public MathMLConverter() {
    }

    /**
     * Executes the task.
     * 
     */
    @Override
    public void execute() {
        DirectoryScanner scanner;
        String[] list;
        String[] dirs;

        if (this.m_baseDir == null) {
            this.m_baseDir = this.getProject().resolveFile(".");
        }

        // if we have an in file and out then process them
        if ((this.m_inFile != null) && (this.m_outFile != null)) {
            try {
                Converter.convert(this.m_inFile, this.m_outFile,
                        this.m_outType);
            } catch (final IOException io) {
                throw new BuildException(io);
            }
            return;
        }

        /*
         * if we get here, in and out have not been specified, we are in batch
         * processing mode.
         */

        // -- make sure Source directory exists...
        if (this.m_destDir == null) {
            final String msg = "m_destDir attributes must be set!";

            throw new BuildException(msg);
        }
        scanner = this.getDirectoryScanner(this.m_baseDir);
        this.log("Transforming into " + this.m_destDir, Project.MSG_INFO);

        // Process all the files marked for styling
        list = scanner.getIncludedFiles();
        for (int i = 0; i < list.length; ++i) {
            this.process(this.m_baseDir, list[i], this.m_destDir);
        }

        // Process all the directoried marked for styling
        dirs = scanner.getIncludedDirectories();
        for (int j = 0; j < dirs.length; ++j) {
            list = new File(this.m_baseDir, dirs[j]).list();
            for (int i = 0; i < list.length; ++i) {
                this.process(this.m_baseDir, list[i], this.m_destDir);
            }
        }
    }

    /**
     * Set whether to check dependencies, or always generate.
     * 
     * @param force
     *            True, if the task should always generate the images.
     */
    public void setForce(final boolean force) {
        this.m_force = force;
    }

    /**
     * Set the base directory.
     * 
     * @param dir
     *            Base directory
     */
    public void setBasedir(final File dir) {
        this.m_baseDir = dir;
    }

    /**
     * Set the destination directory into which the result files should be
     * copied to.
     * 
     * @param dir
     *            Destination directory
     */
    public void setDestdir(final File dir) {
        this.m_destDir = dir;
    }

    /**
     * Sets an out file.
     * 
     * @param outFile
     *            Output file
     */
    public void setOut(final File outFile) {
        this.m_outFile = outFile;
    }

    /**
     * Sets an input xml file to be converted.
     * 
     * @param inFile
     *            Input file
     */
    public void setIn(final File inFile) {
        this.m_inFile = inFile;
    }

    /**
     * Sets outpuf file mimetype.
     * 
     * @param mimetype
     *            mimetype for output file.
     */
    public void setType(final String mimetype) {
        this.m_outType = mimetype;
    }

    /**
     * Ensures if the destination directory exists
     * 
     * @param targetFile
     *            Destination directory
     */
    private void ensureDirectoryFor(final File targetFile) {
        final File directory = new File(targetFile.getParent());

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
     */
    private void process(final File baseDir, final String xmlFile,
            final File destDir) {

        File outFile = null;
        File inFile = null;
        final String suffix = "."
                + Converter.getSuffixForMimeType(this.m_outType);

        try {
            inFile = new File(baseDir, xmlFile);
            final int dotPos = xmlFile.lastIndexOf('.');

            if (dotPos > 0) {
                outFile = new File(destDir, xmlFile.substring(0, xmlFile
                        .lastIndexOf('.'))
                        + suffix);
            } else {
                outFile = new File(destDir, xmlFile + suffix);
            }
            if (this.m_force
                    || (inFile.lastModified() > outFile.lastModified())) {
                this.ensureDirectoryFor(outFile);
                Converter.convert(inFile, outFile, this.m_outType);
            }
        } catch (final Exception ex) {
            // If failed to process document, must delete target document,
            // or it will not attempt to process it the second time
            this.log("Failed to process " + inFile, Project.MSG_INFO);
            if (outFile != null) {
                outFile.delete();
            }

            throw new BuildException(ex);
        }

    }

}

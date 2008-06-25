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

package net.sourceforge.jeuclid.ant;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.converter.Converter;
import net.sourceforge.jeuclid.converter.ConverterRegistry;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.util.FileUtils;

/**
 * This task converts MathML files to images.
 * 
 * @version $Revision$
 */
public class MathMLConverter extends MatchingTask {

    private static final String CURRENT_DIR = "./";

    private static final char EXTENSION_SEP = '.';

    /**
     * 
     */
    private File mdestDir;

    private File mbaseDir;

    private File minFile;

    private File moutFile;

    private String moutType = "image/png";

    private boolean mforce;

    private final MutableLayoutContext context;

    private final FileUtils fileUtils;

    /**
     * Creates a new MathMLConverter Task.
     */
    public MathMLConverter() {
        this.context = new LayoutContextImpl(LayoutContextImpl
                .getDefaultLayoutContext());
        this.fileUtils = FileUtils.getFileUtils();
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

        if (this.mbaseDir == null) {
            this.mbaseDir = this.getProject().resolveFile(
                    MathMLConverter.CURRENT_DIR);
            this.log("Base is not sets, sets to " + this.mbaseDir,
                    Project.MSG_WARN);
        }

        // if we have an in file and out then process them
        if ((this.minFile != null) && (this.moutFile != null)) {
            this.log("Transforming file: " + this.minFile + " --> "
                    + this.moutFile, Project.MSG_VERBOSE);
            try {
                Converter.getInstance().convert(this.minFile, this.moutFile,
                        this.moutType, this.context);
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
        if (this.mdestDir == null) {
            throw new BuildException("m_destDir attributes must be set!");
        }
        scanner = this.getDirectoryScanner(this.mbaseDir);
        this.log("Transforming into " + this.mdestDir, Project.MSG_INFO);

        // Process all the files marked for styling
        list = scanner.getIncludedFiles();
        this.log("Included files: " + Arrays.toString(list),
                Project.MSG_VERBOSE);
        this.process(this.mbaseDir, Arrays.asList(list), this.mdestDir);

        // Process all the directories marked for styling
        dirs = scanner.getIncludedDirectories();
        this.log("Included directories: " + Arrays.toString(dirs),
                Project.MSG_VERBOSE);
        for (final String dir : dirs) {
            list = this.fileUtils.resolveFile(this.mbaseDir, dir).list();
            this.process(this.mbaseDir, Arrays.asList(list), this.mdestDir);
        }
    }

    /**
     * Sets support for anti alias (default is <i>true</i>).
     * 
     * @param antiAlias
     *            Flag for support anti alias.
     */
    public void setAntiAlias(final boolean antiAlias) {
        this.setOption(Parameter.ANTIALIAS, antiAlias);
    }

    /**
     * Sets minimal size for turn on anti alias (default is <i>10.0</i>).
     * 
     * @param antiAliasMinSize
     *            Minimal size in float number.
     */
    public void setAntiAliasMinSize(final float antiAliasMinSize) {
        this.setOption(Parameter.ANTIALIAS_MINSIZE, antiAliasMinSize);
    }

    /**
     * Sets background color.
     * 
     * @param color
     *            String representation of color.
     */
    public void setBackgroundColor(final String color) {
        if (this.isNullOrEmpty(color)) {
            this.log("Attribute \"backgroundcolor\" is empty, not used",
                    Project.MSG_WARN);
        } else {
            this.setOption(Parameter.MATHBACKGROUND, color);
        }
    }

    /**
     * Sets support for debug (default is <i>false</i>).
     * 
     * @param debug
     *            Flag for support debug.
     */
    public void setDebug(final boolean debug) {
        this.setOption(Parameter.DEBUG, debug);
    }

    /**
     * Sets display style (default is <i>BLOCK</i>.
     * 
     * @param display
     *            String value of display style.
     * 
     * @see net.sourceforge.jeuclid.context.Display
     */
    public void setDisplay(final String display) {
        this.setOption(Parameter.DISPLAY, display);
    }

    /**
     * Sets list of supported font families for <i>Double-Struck</i>.
     * 
     * @param fonts
     *            List separated by comma.
     */
    public void setFontsDoublestruck(final String fonts) {
        this.setOption(Parameter.FONTS_DOUBLESTRUCK, fonts);
    }

    /**
     * Sets list of supported font families for <i>Fraktur</i>.
     * 
     * @param fonts
     *            List separated by comma.
     */
    public void setFontsFraktur(final String fonts) {
        this.setOption(Parameter.FONTS_FRAKTUR, fonts);
    }

    /**
     * Sets font size of text.
     * 
     * @param fontSize
     *            Font size as float value.
     */
    public void setFontSize(final float fontSize) {
        this.setOption(Parameter.MATHSIZE, fontSize);
    }

    /**
     * Sets list of supported font families for <i>Monospaced</i>.
     * 
     * @param fonts
     *            List separated by comma.
     */
    public void setFontsMonospaced(final String fonts) {
        this.setOption(Parameter.FONTS_MONOSPACED, fonts);
    }

    /**
     * Sets list of supported font families for <i>Sans-Serif</i>.
     * 
     * @param fonts
     *            List separated by comma.
     */
    public void setFontsSansSerif(final String fonts) {
        this.setOption(Parameter.FONTS_SANSSERIF, fonts);
    }

    /**
     * Sets list of supported font families for <i>Script</i>.
     * 
     * @param fonts
     *            List separated by comma.
     */
    public void setFontsScript(final String fonts) {
        this.setOption(Parameter.FONTS_SCRIPT, fonts);
    }

    /**
     * Sets list of supported font families for <i>Serif</i>.
     * 
     * @param fonts
     *            List separated by comma.
     */
    public void setFontsSerif(final String fonts) {
        this.setOption(Parameter.FONTS_SERIF, fonts);
    }

    /**
     * Sets foreground color.
     * 
     * @param color
     *            String representation of color.
     */
    public void setForegroundColor(final String color) {
        if (this.isNullOrEmpty(color)) {
            this
                    .log(
                            "Attribute \"foregroundcolor\" is empty, use default color",
                            Project.MSG_WARN);
        } else {
            this.setOption(Parameter.MATHCOLOR, color);
        }
    }

    /**
     * Sets &lt;mfrac&gt; keep scriptlevel.
     * 
     * @param keepScriptLevel
     *            if true, element will NEVER increase children's scriptlevel
     *            (in violation of the spec).
     */
    public void setMfracKeepScriptLevel(final boolean keepScriptLevel) {
        this.setOption(Parameter.MFRAC_KEEP_SCRIPTLEVEL, keepScriptLevel);
    }

    /**
     * Sets scripts level (default is <i>0</i>).
     * 
     * @param level
     *            Script level.
     */
    public void setScriptLevel(final int level) {
        this.setOption(Parameter.SCRIPTLEVEL, level);
    }

    /**
     * Sets minimal size of smallest font size (default is <i>8.0</i>).
     * 
     * @param minSize
     *            Size of font.
     */
    public void setScriptMinSize(final float minSize) {
        this.setOption(Parameter.SCRIPTMINSIZE, minSize);
    }

    /**
     * Sets size of multiplier (default is <i>0.71</i>).
     * 
     * @param multSize
     *            Size of multiplier.
     */
    public void setScriptSizeMult(final float multSize) {
        this.setOption(Parameter.SCRIPTSIZEMULTIPLIER, multSize);
    }

    /**
     * Set whether to check dependencies, or always generate.
     * 
     * @param force
     *            True, if the task should always generate the images.
     */
    public void setForce(final boolean force) {
        this.logProperty("force", force);
        this.mforce = force;
    }

    /**
     * Set the base directory.
     * 
     * @param dir
     *            Base directory
     */
    public void setBasedir(final File dir) {
        this.logProperty("basedir", dir);
        this.mbaseDir = dir;
    }

    /**
     * Set the destination directory into which the result files should be
     * copied to.
     * 
     * @param dir
     *            Destination directory
     */
    public void setDestdir(final File dir) {
        this.logProperty("destdir", dir);
        this.mdestDir = dir;
    }

    /**
     * Sets an out file.
     * 
     * @param outFile
     *            Output file
     */
    public void setOut(final File outFile) {
        this.logProperty("out", outFile);
        this.moutFile = outFile;
    }

    /**
     * Sets an input xml file to be converted.
     * 
     * @param inFile
     *            Input file
     */
    public void setIn(final File inFile) {
        this.logProperty("in", inFile);
        this.minFile = inFile;
    }

    /**
     * Sets output file mimetype.
     * 
     * @param mimetype
     *            mimetype for output file.
     */
    public void setType(final String mimetype) {
        this.logProperty("type", mimetype);
        this.moutType = mimetype;
    }

    /**
     * Processes the given input XML file and stores the result in the given
     * resultFile.
     * 
     * @param baseDir
     *            Base directory
     * @param xmlFiles
     *            Source file
     * @param destDir
     *            Destination directory
     */
    private void process(final File baseDir, final List<String> xmlFiles,
            final File destDir) {
        for (final String xmlFile : xmlFiles) {
            File outFile = null;
            File inFile = null;
            final String suffix = MathMLConverter.EXTENSION_SEP
                    + ConverterRegistry.getInstance().getSuffixForMimeType(
                            this.moutType);
            this.log("Found extension: " + suffix, Project.MSG_DEBUG);
            try {
                inFile = this.fileUtils.resolveFile(baseDir, xmlFile);
                final int dotPos = xmlFile
                        .lastIndexOf(MathMLConverter.EXTENSION_SEP);

                if (dotPos > 0) {
                    outFile = this.fileUtils.resolveFile(destDir, xmlFile
                            .substring(0, dotPos)
                            + suffix);
                } else {
                    outFile = this.fileUtils.resolveFile(destDir, xmlFile
                            + suffix);
                }
                this.log("Input file: " + inFile, Project.MSG_DEBUG);
                this.log("Output file: " + outFile, Project.MSG_DEBUG);
                if (this.mforce
                        || !this.fileUtils.isUpToDate(inFile, outFile)) {
                    this.fileUtils.createNewFile(outFile, true);
                    Converter.getInstance().convert(inFile, outFile,
                            this.moutType, this.context);
                }
            } catch (final IOException ex) {
                // If failed to process document, must delete target document,
                // or it will not attempt to process it the second time
                this.log("Failed to process " + inFile, Project.MSG_ERR);
                FileUtils.delete(outFile);

                throw new BuildException(ex);
            }
        }
    }

    /**
     * Convert string value of parameter and sets to current context.
     * 
     * @param param
     *            Type of parameter.
     * @param value
     *            String value of parameter.
     */
    private void setOption(final Parameter param, final String value) {
        this.setOption(param, param.fromString(value));
    }

    /**
     * Sets parameter for current context.
     * 
     * @param param
     *            Type of parameter.
     * @param value
     *            Object with value of parameter.
     */
    private void setOption(final Parameter param, final Object value) {
        this.logProperty(param.getOptionName(), value);
        this.context.setParameter(param, value);
    }

    /**
     * Tests if string is null or is empty.
     * 
     * @param s
     *            Tested string.
     * 
     * @return <code>true</code> if string is null or empty else
     *         <code>false</code>.
     */
    private boolean isNullOrEmpty(final String s) {
        // TODO: use .isEmpty() when JEuclid moves to 1.6
        return s == null || s.length() == 0;
    }

    /**
     * Logs property, which is sets.
     * 
     * @param param
     *            Parameter name.
     * @param value
     *            Parameter value.
     */
    private void logProperty(final String param, final Object value) {
        this.log("Sets property \"" + param + "\" with value: " + value,
                Project.MSG_DEBUG);
    }
}

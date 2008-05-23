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

package net.sourceforge.jeuclid.app.mathviewer;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.converter.Converter;
import net.sourceforge.jeuclid.converter.ConverterRegistry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * File I/O support functionality for MathViewer.
 * 
 * @version $Revision$
 */
public final class FileIO {

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(FileIO.class);

    private static final String EXPORT_ERROR = "MathViewer.exportError"; //$NON-NLS-1$,

    private File lastPath;

    private static final class SingletonHolder {
        private static FileIO instance = new FileIO();

        private SingletonHolder() {
        }
    }

    private FileIO() {
    }

    /**
     * Retrieve the FileIO object.
     * 
     * @return the FileIO object
     */
    public static FileIO getInstance() {
        return FileIO.SingletonHolder.instance;
    }

    private static String getExtension(final String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    private static class SaveExportFilter implements FilenameFilter {

        private final Set<String> extensions;

        protected SaveExportFilter() {
            this.extensions = ConverterRegistry.getInstance()
                    .getAvailableExtensions();
        };

        public boolean accept(final File dir, final String name) {
            return this.extensions.contains(FileIO.getExtension(name));
        }
    }

    /**
     * Select a file.
     * 
     * @param parent
     *            Frame of the parent
     * @return A File or null.
     */
    public File selectFileToOpen(final Frame parent) {
        final File selectedFile;

        if (MathViewer.OSX) {
            // Have to use AWT file chooser for Mac-friendlyness
            final FileDialog chooser = new FileDialog(parent,
                    "Please select a MathML file");
            if (this.lastPath != null) {
                chooser.setDirectory(this.lastPath.toString());
            }
            chooser.setVisible(true);
            final String fileName = chooser.getFile();
            if (fileName != null) {
                selectedFile = new File(chooser.getDirectory(), fileName);
            } else {
                selectedFile = null;
            }
        } else {
            final JFileChooser fc = new JFileChooser(this.lastPath);
            final int returnVal = fc.showOpenDialog(parent);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                selectedFile = fc.getSelectedFile();
            } else {
                selectedFile = null;
            }
        }
        if (selectedFile != null) {
            this.lastPath = selectedFile.getParentFile();
        }
        return selectedFile;
    }

    /**
     * Load the given file.
     * 
     * @param selectedFile
     *            File object to load.
     * @param parent
     *            Frame of parent window
     * @return a parsed Document or null
     */
    public Document loadFile(final Frame parent, final File selectedFile) {
        Document retVal;
        if (selectedFile == null) {
            retVal = null;
        } else {
            try {
                retVal = MathMLParserSupport.parseFile(selectedFile);
            } catch (final SAXException e) {
                retVal = null;
                FileIO.LOGGER.warn(e.getMessage(), e);
                JOptionPane
                        .showMessageDialog(
                                parent,
                                e.getMessage(),
                                Messages.getString("MathViewer.errorParsing"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
            } catch (final IOException e) {
                retVal = null;
                FileIO.LOGGER.warn(e.getMessage(), e);
                JOptionPane
                        .showMessageDialog(
                                parent,
                                e.getMessage(),
                                Messages
                                        .getString("MathViewer.errorAccessing"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
            }
        }
        return retVal;
    }

    /**
     * Save a document.
     * 
     * @param parent
     *            frame of parent.
     * @param document
     *            the MML document to save.
     * @param params
     *            rendering parameters.
     */
    public void saveDocument(final Frame parent, final Node document,
            final MutableLayoutContext params) {
        final File selectedFile;

        if (MathViewer.OSX) {
            // Have to use AWT file chooser for Mac-friendlyness
            final FileDialog chooser = new FileDialog(parent, "Export to...",
                    FileDialog.SAVE);
            if (this.lastPath != null) {
                chooser.setDirectory(this.lastPath.toString());
            }
            chooser.setFilenameFilter(new SaveExportFilter());

            chooser.setVisible(true);
            final String fileName = chooser.getFile();
            if (fileName != null) {
                selectedFile = new File(chooser.getDirectory(), fileName);
            } else {
                selectedFile = null;
            }
        } else {
            final JFileChooser fc = new JFileChooser(this.lastPath);
            final int returnVal = fc.showSaveDialog(parent);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                selectedFile = fc.getSelectedFile();
            } else {
                selectedFile = null;
            }

        }
        if (selectedFile != null) {
            this.lastPath = selectedFile.getParentFile();

            FileIO.LOGGER.info(selectedFile);

            int doIt = JOptionPane.YES_OPTION;

            if (selectedFile.exists()) {
                doIt = JOptionPane.showConfirmDialog(parent, "File "
                        + selectedFile.getName()
                        + " already exists. Overwrite?", "Confirm Overwrite",
                        JOptionPane.YES_NO_OPTION);
            }

            if (doIt == JOptionPane.YES_OPTION) {
                this.exportAs(parent, selectedFile, document, params);
            }
        }
    }

    private void exportAs(final Frame parent, final File selectedFile,
            final Node document, final MutableLayoutContext params) {
        final String fileName = selectedFile.getName();
        final String extension = FileIO.getExtension(fileName);
        final String mimetype = ConverterRegistry.getInstance()
                .getMimeTypeForSuffix(extension);
        if (mimetype != null) {
            try {
                if (Converter.getInstance().convert(document, selectedFile,
                        mimetype, params) == null) {
                    JOptionPane.showMessageDialog(parent,
                            "Failed to write to " + fileName, Messages
                                    .getString(FileIO.EXPORT_ERROR),
                            JOptionPane.ERROR_MESSAGE);

                }
            } catch (final IOException e) {
                FileIO.LOGGER.warn(e);
                JOptionPane.showMessageDialog(parent, e.getMessage(),
                        Messages.getString(FileIO.EXPORT_ERROR),
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(parent,
                    "Unsupported file extension " + extension, Messages
                            .getString(FileIO.EXPORT_ERROR),
                    JOptionPane.ERROR_MESSAGE);

        }
    }

}

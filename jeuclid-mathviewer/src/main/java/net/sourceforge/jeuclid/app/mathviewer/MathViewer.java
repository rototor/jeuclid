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

import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.sourceforge.jeuclid.Constants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.apple.eawt.Application;

/**
 * A simple application for viewing MathML documents.

 * @version $Revision$
 */
public final class MathViewer {

    /**
     * Set to true if we're running under Mac OS X.
     */
    public static final boolean OSX =
            System.getProperty("mrj.version") != null; //$NON-NLS-1$

    /**
     * Logger for this class.
     */
    private static final Log LOGGER = LogFactory.getLog(MathViewer.class);

    /**
     * Source file.
     */
    private static File source;

    /**
     * Default constructor.
     */
    private MathViewer() {
        // Empty on purpose
    }

    /**
     * Launches this application.
     * @param args
     *            command line arguments. Ignored.
     */
    public static void main(final String[] args) {
        MathViewer.source = null;
        if (args.length > 0) {
            MathViewer.source = new File(args[0]);
        }
        if (MathViewer.OSX) {
            System.setProperty("apple.laf.useScreenMenuBar",
                    Constants.TRUE); //$NON-NLS-1$ //$NON-NLS-2$
        }

        try {
            UIManager.setLookAndFeel(UIManager
                    .getSystemLookAndFeelClassName());
        } catch (final ClassNotFoundException e) {
            MathViewer.LOGGER.debug(e);
        } catch (final InstantiationException e) {
            MathViewer.LOGGER.debug(e);
        } catch (final IllegalAccessException e) {
            MathViewer.LOGGER.debug(e);
        } catch (final UnsupportedLookAndFeelException e) {
            MathViewer.LOGGER.debug(e);
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final MainFrame mainFrame = new MainFrame();
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                if (MathViewer.OSX) {
                    final Application a = Application.getApplication();
                    a.setEnabledAboutMenu(true);
                    a.setEnabledPreferencesMenu(true);
                    a.addApplicationListener(new MainFrameAppListener(
                            mainFrame));
                }
                if (MathViewer.source != null) {
                    mainFrame.loadFile(MathViewer.source);
                }
                mainFrame.setVisible(true);
            }
        });
    }
}

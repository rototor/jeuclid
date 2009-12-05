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

import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;

/**
 * @version $Revision$
 */
public class MainFrameAppListener extends ApplicationAdapter {

    //private final MainFrame frame;
    private final MainFrameEx frame;

    // /**
    // * Logger for this class
    // */
    // Unused.
    // private static final Log LOGGER = LogFactory
    // .getLog(MainFrameAppListener.class);

    /**
     * Default Constructor.
     * 
     * @param mainFrame
     *            MainFrame to use
     */
    public MainFrameAppListener(final MainFrameEx mainFrame) {
        this.frame = mainFrame;
    }

    /** {@inheritDoc} */
    @Override
    public void handleOpenFile(final ApplicationEvent arg0) {
        this.frame.loadFile(new File(arg0.getFilename()));
        arg0.setHandled(true);
    }

    /** {@inheritDoc} */
    @Override
    public void handleAbout(final ApplicationEvent arg0) {
        this.frame.displayAbout();
        arg0.setHandled(true);
    }

    /** {@inheritDoc} */
    @Override
    public void handlePreferences(final ApplicationEvent arg0) {
        this.frame.displaySettings();
        arg0.setHandled(true);
    }

    /** {@inheritDoc} */
    @Override
    public void handleQuit(final ApplicationEvent arg0) {
        System.exit(0);
        arg0.setHandled(true);
    }
}

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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to load i18n messages.
 * 
 * @version $Revision$
 */
public final class Messages {
    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(Messages.class);

    private static final String BUNDLE_NAME = "intl.mathviewer"; //$NON-NLS-1$

    private static ResourceBundle resourceBundle;

    private Messages() {
    }

    /**
     * retrieve a translation string.
     * 
     * @param key
     *            key to look for
     * @return the expanded string
     */
    public static String getString(final String key) {
        String retVal = '!' + key + '!';
        try {
            if (Messages.resourceBundle != null) {
                retVal = Messages.resourceBundle.getString(key);
            }
        } catch (final MissingResourceException e) {
            Messages.LOGGER.warn(e.getMessage());
        }
        return retVal;
    }

    static {
        try {
            Messages.resourceBundle = ResourceBundle
                    .getBundle(Messages.BUNDLE_NAME);
        } catch (final MissingResourceException e) {
            Messages.LOGGER.warn(e.getMessage());
        }
    }
}

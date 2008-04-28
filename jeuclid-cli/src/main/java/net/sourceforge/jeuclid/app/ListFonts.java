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

package net.sourceforge.jeuclid.app;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import net.sourceforge.jeuclid.font.FontFactory;

/**
 * Lists all fonts available to JEuclid.
 * 
 * @version $Revision$
 */
public final class ListFonts {
    /**
     * Default Constructor.
     */
    private ListFonts() {
        // Empty on purpose
    }

    /**
     * Retrieves font list and prints it to the console.
     * 
     * @param args
     *            not used.
     */
    public static void main(final String[] args) {
        final FontFactory f = FontFactory.getInstance();
        final List<String> allFonts = new Vector<String>(f.listFontNames());
        Collections.sort(allFonts);
        for (final String s : allFonts) {
            System.out.println(s);
        }
    }
}

/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.app.support;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.jeuclid.ParameterKey;

/**
 * This class contains a command line parser for JEuclid apps.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public final class CommandLineParser {

    private CommandLineParser() {
        // empty on purpose
    }

    /**
     * Results from command line parsing.
     */
    public static class ParseResults {
        private final File source;

        private final File target;

        private final String mimetype;

        private final Map<ParameterKey, String> params;

        /**
         * Construct a ParseResults Object.
         * 
         * @param s
         *            source file
         * @param t
         *            target file
         * @param p
         *            rendering parameters
         * @param type
         *            mime type
         */
        public ParseResults(final File s, final File t, final String type,
                final Map<ParameterKey, String> p) {
            this.source = s;
            this.target = t;
            this.params = p;
            this.mimetype = type;
        }

        /**
         * @return the params
         */
        public final Map<ParameterKey, String> getParams() {
            return this.params;
        }

        /**
         * @return the source
         */
        public final File getSource() {
            return this.source;
        }

        /**
         * @return the target
         */
        public final File getTarget() {
            return this.target;
        }

        /**
         * Getter method for mimetype.
         * 
         * @return the mimetype
         */
        public String getMimetype() {
            return this.mimetype;
        }

    }

    /**
     * parses the command line and returns its values.
     * 
     * @param args
     *            the command line
     * @return a ParseResults instance
     */
    public static ParseResults parseCommandLine(final String[] args) {
        File source = null;
        File target = null;
        String mimetype = null;
        int count = 0;
        final Map<ParameterKey, String> params = new HashMap<ParameterKey, String>();
        String option = null;
        for (final String curArg : args) {
            if (curArg.startsWith("-")) {
                option = curArg.substring(1);
            } else {
                if ("OutFileType".equalsIgnoreCase(option)) {
                    mimetype = curArg;
                } else if (option != null) {
                    final ParameterKey key = ParameterKey.valueOf(option);
                    params.put(key, curArg);
                    option = null;
                } else if (count == 0) {
                    source = new File(curArg);
                } else if (count == 1) {
                    target = new File(curArg);
                } else {
                    throw new IllegalArgumentException("To many files given");
                }
                count++;
            }
        }
        if (option != null) {
            throw new IllegalArgumentException("No value given for " + option);
        }
        return new ParseResults(source, target, mimetype, params);
    }
}

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

package net.sourceforge.jeuclid.app.foprep;

import java.io.PrintWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sourceforge.jeuclid.converter.Processor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Main class for fo-preprocess application.
 * 
 * @version $Revision$
 */
public final class Main {

    private static final String STD_INOUT = "-";

    private static final String OPTION_OUT = "out";

    private static final String OPTION_IN = "in";

    private Main() {
        // Empty on purpose.
    }

    /**
     * Application entry point.
     * 
     * @param args
     *            Command line arguments.
     */
    public static void main(final String[] args) {
        final Options options = new Options();
        final Option in = new Option(Main.OPTION_IN, true, "input (fo) file");
        in.setRequired(true);
        options.addOption(in);
        final Option out = new Option(Main.OPTION_OUT, true,
                "output (fo) file");
        out.setRequired(true);
        options.addOption(out);
        final CommandLineParser parser = new GnuParser();
        try {
            final CommandLine cmd = parser.parse(options, args);
            final String inputFile = cmd.getOptionValue(Main.OPTION_IN);
            final String outputFile = cmd.getOptionValue(Main.OPTION_OUT);
            final Result result;
            if (Main.STD_INOUT.equals(outputFile)) {
                result = new StreamResult(new PrintWriter(System.out));
            } else {
                result = new StreamResult(outputFile);
            }
            final Source source;
            if (Main.STD_INOUT.equals(inputFile)) {
                source = new StreamSource(System.in);
            } else {
                source = new StreamSource(inputFile);
            }
            Processor.getInstance().process(source, result);
        } catch (final ParseException e1) {
            System.out.println("Invalid command line:" + e1.getMessage());
            new HelpFormatter().printHelp(
                    "foprep -in input.fo -out output.fo", options);
        } catch (final TransformerException e) {
            System.out.println("An error occurred during processing: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }
}

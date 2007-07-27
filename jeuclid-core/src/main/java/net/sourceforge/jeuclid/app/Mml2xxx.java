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

package net.sourceforge.jeuclid.app;

import java.io.File;
import java.io.IOException;

import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.app.support.CommandLineParser;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.converter.Converter;
import net.sourceforge.jeuclid.converter.ConverterRegistry;

/**
 * Utility class to be used from the command line to call the converters.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public final class Mml2xxx {

    private static final String COLON = ": ";

    private static final String SPACE = " ";

    private Mml2xxx() {
        // Empty on purpose
    }

    /**
     * Main function for use from scripts.
     * 
     * @param args
     *            command line arguments.
     */
    public static void main(final String[] args) {

        try {
            final CommandLineParser.ParseResults parseResults = CommandLineParser
                    .parseCommandLine(args);
            final File source = parseResults.getSource();
            final File target = parseResults.getTarget();

            final MutableLayoutContext params = LayoutContextImpl
                    .getDefaultLayoutContext();
            final boolean mimeTypeIsSet = parseResults.getMimetype() != null;
            // TODO This is BROKEN at the moment!
            // params.putAll(parseResults.getParams());

            if (source == null) {
                throw new IllegalArgumentException("No source given");
            } else if (target == null) {
                throw new IllegalArgumentException("No target given");
            } else if (!source.isFile()) {
                throw new IllegalArgumentException("Source is not a file");
            }

            final String outFileType;
            if (!mimeTypeIsSet) {
                final String fileName = target.getName();
                final String extension = fileName.substring(fileName
                        .lastIndexOf('.') + 1);
                final String mimetype = ConverterRegistry.getRegisty()
                        .getMimeTypeForSuffix(extension);
                outFileType = mimetype;
            } else {
                outFileType = parseResults.getMimetype();
            }

            Converter.getConverter().convert(source, target, outFileType,
                    params);

        } catch (final ArrayIndexOutOfBoundsException aiobe) {
            Mml2xxx.showUsage();
            System.out.println(aiobe.getClass().toString() + Mml2xxx.COLON
                    + aiobe.getMessage());
        } catch (final IllegalArgumentException ia) {
            Mml2xxx.showUsage();
            System.out.println(ia.getClass().toString() + Mml2xxx.COLON
                    + ia.getMessage());
        } catch (final IOException e) {
            Mml2xxx.showUsage();
            System.out.println(e.getClass().toString() + Mml2xxx.COLON
                    + e.getMessage());
        }
    }

    private static void showUsage() {
        System.out.println("JEuclid BasicConverter");
        System.out.println("");
        System.out.println("Usage:");
        System.out.println("");
        System.out.println("mml2xxx source target (option value)*");
        System.out.println("");
        System.out.println("where:");
        System.out
                .println(" source is the path to the source file (MathML or ODF format)");
        System.out.println(" target is the path to the target file");
        System.out.println("Possible options (with default value):");
        // TODO: BROKEN!
        // final ParameterKey[] options = ParameterKey.values();
        // for (final ParameterKey param : options) {
        // final String name = param.name();
        // System.out.print(" -" + name);
        // System.out.print(Mml2xxx.SPACE);
        // System.out.println(MathBase.getDefaultParameters().get(param));
        // }
        System.out.println("The following output types are supported:");
        System.out.print("   ");
        for (final String type : ConverterRegistry.getRegisty()
                .getAvailableOutfileTypes()) {
            System.out.print(Mml2xxx.SPACE + type);
        }
        System.out.println();
        System.out.println("Example: ");
        System.out.println("  mml2xxx a.mml a.png -BackgroundColor white");
        System.out.println();
        System.out.println();

    }

}

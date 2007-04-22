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
import java.util.Map;

import net.sourceforge.jeuclid.Converter;
import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.ParameterKey;

/**
 * Utility class to be used from the command line to call the converters.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public final class Mml2xxx {

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

        int count = 0;
        File source = null;
        File target = null;
        boolean mimeTypeIsSet = false;
        final Map<ParameterKey, String> params = MathBase
                .getDefaultParameters();
        try {
            for (int i = 0; i < args.length; i++) {
                final String curArg = args[i];
                if (curArg.startsWith("-")) {
                    final String option = curArg.substring(1);
                    i++;
                    final String value = args[i];
                    final ParameterKey key = ParameterKey.valueOf(option);
                    if (key.equals(ParameterKey.OutFileType)) {
                        mimeTypeIsSet = true;
                    }
                    params.put(key, value);
                } else {
                    if (count == 0) {
                        source = new File(curArg);
                    } else if (count == 1) {
                        target = new File(curArg);
                    } else {
                        throw new IllegalArgumentException();
                    }
                    count++;
                }
            }

            if (count != 2) {
                throw new IllegalArgumentException();
            } else if (!source.isFile()) {
                throw new IllegalArgumentException();
            }

            if (!mimeTypeIsSet) {
                final String fileName = target.getName();
                final String extension = fileName.substring(fileName
                        .lastIndexOf('.') + 1);
                final String mimetype = Converter
                        .getMimeTypeForSuffix(extension);
                params.put(ParameterKey.OutFileType, mimetype);
            }

            Converter.convert(source, target, params);

        } catch (ArrayIndexOutOfBoundsException aiobe) {
            Mml2xxx.showUsage();
        } catch (IllegalArgumentException ia) {
            Mml2xxx.showUsage();
        } catch (final IOException e) {
            Mml2xxx.showUsage();
            System.out.println(e.getClass().toString() + ": "
                    + e.getMessage());
        }
    }

    private static void showUsage() {
        System.out.println("JEuclid Converter");
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
        final ParameterKey[] options = ParameterKey.values();
        for (int i = 0; i < options.length; i++) {
            final ParameterKey param = options[i];
            final String name = param.name();
            System.out.print(" -" + name);
            System.out.print(" ");
            System.out.println(MathBase.getDefaultParameters().get(param));
        }
        System.out.println("The following output types are supported:");
        System.out.print("   ");
        for (final String type : Converter.getAvailableOutfileTypes()) {
            System.out.print(" " + type);
        }
        System.out.println();
        System.out.println("Example: ");
        System.out.println("  mml2xxx a.mml a.png -BackgroundColor white");
        System.out.println();
        System.out.println();

    }

}

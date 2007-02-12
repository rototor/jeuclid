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

/* $Id: Mml2xxx.java,v 1.1.2.2 2007/02/08 18:57:39 maxberger Exp $ */

package net.sourceforge.jeuclid.app;

import java.io.File;
import java.io.IOException;

import net.sourceforge.jeuclid.util.Converter;

/**
 * Utility class to be used from the command line to call the converters.
 * 
 * @author Max Berger
 */
public class Mml2xxx {

    /**
     * Main function for use from scripts.
     * 
     * @param args
     *            command line arguments.
     */
    public static void main(final String[] args) {
        if (args.length != 3) {
            Mml2xxx.showUsage();
        } else {
            try {
                Converter.convert(new File(args[0]), new File(args[1]),
                        args[2]);
            } catch (final IOException e) {
                System.out.println(e.getClass().toString() + ": "
                        + e.getMessage());
                System.out.println();
                Mml2xxx.showUsage();
            }
        }
    }

    private static void showUsage() {
        System.out.println("JEuclid Converter");
        System.out.println("");
        System.out.println("Usage:");
        System.out.println("");
        System.out.println("mml2xxx source target targettype");
        System.out.println("");
        System.out.println("where:");
        System.out
                .println(" source is the path to the source file (MathML or ODF format)");
        System.out.println(" target is the path to the target file");
        System.out.println(" targettype is one of the supported types:");
        System.out.print("   ");
        for (final String type : Converter.getAvailableOutfileTypes()) {
            System.out.print(" " + type);
        }
        System.out.println();
        System.out.println();

    }

}

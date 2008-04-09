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
import java.util.Locale;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.LayoutContext.Parameter;
import net.sourceforge.jeuclid.LayoutContext.Parameter.EnumTypeWrapper;
import net.sourceforge.jeuclid.LayoutContext.Parameter.TypeWrapper;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.converter.Converter;
import net.sourceforge.jeuclid.converter.ConverterRegistry;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;

/**
 * Utility class to be used from the command line to call the converters.
 * 
 * @version $Revision$
 */
public final class Mml2xxx {

    private static final String OUT_FILE_TYPE = "outFileType";

    private Mml2xxx() {
        // Empty on purpose
    }

    private static Options createOptions() {
        final Options options = new Options();
        final Option oft = new Option(Mml2xxx.OUT_FILE_TYPE, true,
                "output file mime type [default: derived from the target file's extention]"
                        + "; available values are: "
                        + StringUtils.join(ConverterRegistry.getRegisty()
                                .getAvailableOutfileTypes().iterator(), ' '));
        options.addOption(oft);
        final LayoutContext defaultCtx = LayoutContextImpl
                .getDefaultLayoutContext();
        for (final Parameter param : Parameter.values()) {
            final TypeWrapper typeWrapper = param.getTypeWrapper();
            final StringBuilder desc = new StringBuilder(param
                    .getOptionDesc());
            final String defValue = param.toString(defaultCtx
                    .getParameter(param));
            if (defValue != null) {
                desc.append(" [default: ").append(defValue).append("]");
            }
            final Option o = new Option(param.getOptionName(), true, desc
                    .toString());
            String argName = param.getTypeWrapper().getValueType()
                    .getSimpleName().toLowerCase(Locale.ENGLISH);
            if (typeWrapper instanceof EnumTypeWrapper) {
                try {
                    argName = StringUtils.join(
                            ((EnumTypeWrapper) typeWrapper).values(), '|');
                } catch (final RuntimeException e) {
                    // do nothing, not a big issue
                }
            }
            o.setArgName(argName);
            options.addOption(o);
        }
        return options;
    }

    /**
     * Main function for use from scripts.
     * 
     * @param args
     *            command line arguments.
     */
    public static void main(final String[] args) {
        final Options options = Mml2xxx.createOptions();
        CommandLine cmdLine = null;
        try {
            cmdLine = new GnuParser().parse(options, args);
            final String[] files = cmdLine.getArgs();
            switch (files.length) {
            case 0:
                throw new ParseException("No source given");
            case 1:
                throw new ParseException("No target given");
            case 2:
                break;
            default:
                throw new ParseException("Too many non-option arguments");
            }
            final File source = new File(files[0]);
            if (!source.isFile() || !source.canRead()) {
                throw new ParseException(
                        "Source is not a file or not readable");
            }

            String outFileType = cmdLine
                    .getOptionValue(Mml2xxx.OUT_FILE_TYPE);
            final String isNotSupported = " is not supported";
            if (outFileType == null) {
                final String fileName = files[1];
                final int dot = fileName.lastIndexOf('.');
                if (dot == -1 || dot == fileName.length() - 1) {
                    throw new ParseException(
                            "no -"
                                    + Mml2xxx.OUT_FILE_TYPE
                                    + " option is given and target file has no extension");
                }
                final String extension = fileName.substring(dot + 1);
                outFileType = ConverterRegistry.getRegisty()
                        .getMimeTypeForSuffix(extension);
                if (outFileType == null) {
                    throw new IllegalArgumentException("File extension "
                            + extension + isNotSupported);
                }
            } else {
                if (!ConverterRegistry.getRegisty()
                        .getAvailableOutfileTypes().contains(outFileType)) {
                    throw new IllegalArgumentException("Output type "
                            + outFileType + isNotSupported);
                }
            }
            final LayoutContextImpl ctx = new LayoutContextImpl(
                    LayoutContextImpl.getDefaultLayoutContext());
            for (final Parameter param : Parameter.values()) {
                final String value = cmdLine.getOptionValue(param
                        .getOptionName());
                if (value != null) {
                    ctx.setParameter(param, param.fromString(value));
                }
            }

            Converter.getConverter().convert(source, new File(files[1]),
                    outFileType, ctx);

        } catch (final ParseException pe) {
            System.err.println(pe);
            Mml2xxx.showUsage(options);
            System.exit(1);
        } catch (final IOException ioe) {
            System.err.println("Error encountered during converion process");
            ioe.printStackTrace(System.err);
            System.exit(2);
        } catch (final IllegalArgumentException iae) {
            System.err.println(iae);
            Mml2xxx.showUsage(options);
            System.exit(1);
        }
    }

    private static void showUsage(final Options options) {
        new HelpFormatter().printHelp(
                "mml2xxx <source file> <target file> [options]",
                "source is the path to the source file (MathML or ODF format)"
                        + System.getProperty("line.separator")
                        + "target is the path to the target file", options,
                "Example: mml2xxx a.mml a.png -backgroundColor white");
    }

}

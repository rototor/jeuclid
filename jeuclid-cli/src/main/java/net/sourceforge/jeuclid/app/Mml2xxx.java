/*
 * Copyright 2007 - 2008 JEuclid, http://jeuclid.sf.net
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.context.typewrapper.EnumTypeWrapper;
import net.sourceforge.jeuclid.context.typewrapper.TypeWrapper;
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
// CHECKSTYLE:OFF
// Data abstraction coupling is too high. But it makes no sense to split up
// this class.
public final class Mml2xxx {
    // CHECKSTYLE:ON

    private static final String OUT_FILE_TYPE = "outFileType";

    private static final String DEFAULT_TYPE = "image/png";

    private Mml2xxx() {
        // Empty on purpose
    }

    private static Options createOptions() {
        final Options options = new Options();
        final Option oft = new Option(Mml2xxx.OUT_FILE_TYPE, true,
                "output file mime type [default: derived from the target file's extention]"
                        + "; available values are: "
                        + StringUtils.join(ConverterRegistry.getInstance()
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
                argName = StringUtils.join(((EnumTypeWrapper) typeWrapper)
                        .values(), '|');
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

            final List<String> files = Arrays.asList(cmdLine.getArgs());
            if (files.size() < 2) {
                throw new ParseException("Not enough arguments!");
            }
            final int sourceCount = files.size() - 1;
            final File lastFile = new File(files.get(sourceCount));
            final boolean multi = lastFile.isDirectory();
            final List<File> sources = Mml2xxx.createListOfSourceFiles(files,
                    sourceCount);

            final MutableLayoutContext ctx = Mml2xxx
                    .createLayoutContext(cmdLine);
            if (multi) {
                Mml2xxx.convertMultipleFiles(cmdLine, lastFile, sources, ctx);
            } else {
                if (sources.size() != 1) {
                    throw new ParseException(
                            "Too many file arguments. Did you want to add a target directory?");
                }
                final String outFileType = Mml2xxx.findOutfileType(cmdLine,
                        lastFile.getName());
                Converter.getInstance().convert(sources.get(0), lastFile,
                        outFileType, ctx);
            }
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

    private static void convertMultipleFiles(final CommandLine cmdLine,
            final File lastFile, final List<File> sources,
            final MutableLayoutContext layoutContext) throws ParseException,
            IOException {
        final String outFileType = Mml2xxx.findOutfileType(cmdLine, null);
        for (final File source : sources) {
            final String fileName = source.getName();
            final int dotpos = fileName.lastIndexOf('.');
            final String baseName;
            if (dotpos >= 0) {
                baseName = fileName.substring(0, dotpos);
            } else {
                baseName = fileName;
            }
            final File target = new File(lastFile, baseName
                    + '.'
                    + ConverterRegistry.getInstance().getSuffixForMimeType(
                            outFileType));
            Converter.getInstance().convert(source, target, outFileType,
                    layoutContext);
        }
    }

    private static List<File> createListOfSourceFiles(
            final List<String> files, final int count) throws ParseException {
        final List<File> sources = new ArrayList<File>(count);
        for (int i = 0; i < count; i++) {
            final String current = files.get(i);
            final File source = new File(current);
            if (!source.isFile() || !source.canRead()) {
                throw new ParseException(current
                        + " is not a file or not readable");
            }
            sources.add(source);
        }
        return sources;
    }

    private static String findOutfileType(final CommandLine cmdLine,
            final String fileName) throws ParseException {
        String outFileType = cmdLine.getOptionValue(Mml2xxx.OUT_FILE_TYPE);
        final String isNotSupported = " is not supported";
        if ((outFileType == null) && (fileName != null)) {
            final int dot = fileName.lastIndexOf('.');
            if (dot != -1 && dot != fileName.length() - 1) {
                final String extension = fileName.substring(dot + 1);
                outFileType = ConverterRegistry.getInstance()
                        .getMimeTypeForSuffix(extension);
            }
        }
        if (outFileType == null) {
            System.out.println("No ouput type could be detected, assuming "
                    + Mml2xxx.DEFAULT_TYPE);
            outFileType = Mml2xxx.DEFAULT_TYPE;
        } else {
            if (!ConverterRegistry.getInstance().getAvailableOutfileTypes()
                    .contains(outFileType)) {
                throw new IllegalArgumentException("Output type "
                        + outFileType + isNotSupported);
            }
        }
        return outFileType;
    }

    private static MutableLayoutContext createLayoutContext(
            final CommandLine cmdLine) {
        final MutableLayoutContext ctx = new LayoutContextImpl(
                LayoutContextImpl.getDefaultLayoutContext());
        for (final Parameter param : Parameter.values()) {
            final String value = cmdLine
                    .getOptionValue(param.getOptionName());
            if (value != null) {
                ctx.setParameter(param, param.fromString(value));
            }
        }
        return ctx;
    }

    private static void showUsage(final Options options) {
        final HelpFormatter hf = new HelpFormatter();
        final String lineSep = hf.getNewLine();
        hf
                .printHelp(
                        "mml2xxx <source file(s)> <target file/directory> [options]",
                        "source is the path to the source file (MathML or ODF format)"
                                + lineSep
                                + "target is the path to the target file / directory"
                                + lineSep
                                + "If multiple source files are given, target must be a directory",
                        options,
                        "Example: mml2xxx a.mml a.png -backgroundColor white");
    }

}

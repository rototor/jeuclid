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

package net.sourceforge.jeuclid.converter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.jeuclid.elements.support.ClassLoaderSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freehep.util.export.ExportFileType;

/**
 * actual detector for FreeHEP graphics formats. Depends on the presence of
 * FreeHEP, hence the "internal".
 * 
 * @version $Revision$
 */
public final class FreeHepInternalDetector {

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory
            .getLog(FreeHepInternalDetector.class);

    private static final Map<String, String> PLUGINS_CLASSES = new HashMap<String, String>();

    private FreeHepInternalDetector() {
        // Empty on purpose
    }

    /**
     * Actual detection and registration routine.
     * 
     * @param registry
     *            ConverterRegistry to use
     */
    public static void actuallyDetectConversionPlugins(
            final ConverterRegistry registry) {

        for (final Map.Entry<String, String> e : FreeHepInternalDetector.PLUGINS_CLASSES
                .entrySet()) {

            try {
                final Class<?> infoClass = ClassLoaderSupport.getInstance()
                        .loadClass(e.getKey());
                final ExportFileType fileType = (ExportFileType) infoClass
                        .getConstructor().newInstance();

                final Class<?> graphicsClass = ClassLoaderSupport.getInstance()
                        .loadClass(e.getValue());
                FreeHepInternalDetector.actuallyRegister(registry, fileType,
                        graphicsClass);
            } catch (final NoSuchMethodException ex) {
                FreeHepInternalDetector.LOGGER.debug(ex);
            } catch (final ClassNotFoundException ex) {
                FreeHepInternalDetector.LOGGER.debug(ex);
            } catch (final IllegalArgumentException ex) {
                FreeHepInternalDetector.LOGGER.debug(ex);
            } catch (final SecurityException ex) {
                FreeHepInternalDetector.LOGGER.debug(ex);
            } catch (final InstantiationException ex) {
                FreeHepInternalDetector.LOGGER.debug(ex);
            } catch (final IllegalAccessException ex) {
                FreeHepInternalDetector.LOGGER.debug(ex);
            } catch (final InvocationTargetException ex) {
                FreeHepInternalDetector.LOGGER.debug(ex);
            }
        }
    }

    private static void actuallyRegister(final ConverterRegistry registry,
            final ExportFileType fileType, final Class<?> graphicsClass)
            throws NoSuchMethodException {
        final ConverterPlugin freeHepConverter = new FreeHepConverter(
                graphicsClass);
        for (final String mimeType : fileType.getMIMETypes()) {
            for (final String suffix : fileType.getExtensions()) {
                registry.registerMimeTypeAndSuffix(mimeType, suffix, false);
            }
            registry.registerConverter(mimeType, freeHepConverter, false);
        }
    }

    static {
        FreeHepInternalDetector.PLUGINS_CLASSES.put(
                "org.freehep.graphicsio.emf.EMFExportFileType",
                "org.freehep.graphicsio.emf.EMFGraphics2D");
        FreeHepInternalDetector.PLUGINS_CLASSES.put(
                "org.freehep.graphicsio.gif.GIFExportFileType",
                "org.freehep.graphicsio.gif.GIFGraphics2D");
        FreeHepInternalDetector.PLUGINS_CLASSES.put(
                "org.freehep.graphicsio.pdf.PDFExportFileType",
                "org.freehep.graphicsio.pdf.PDFGraphics2D");
        FreeHepInternalDetector.PLUGINS_CLASSES.put(
                "org.freehep.graphicsio.ps.PSExportFileType",
                "org.freehep.graphicsio.ps.PSGraphics2D");
        FreeHepInternalDetector.PLUGINS_CLASSES.put(
                "org.freehep.graphicsio.svg.SVGExportFileType",
                "org.freehep.graphicsio.svg.SVGGraphics2D");
        FreeHepInternalDetector.PLUGINS_CLASSES.put(
                "org.freehep.graphicsio.swf.SWFExportFileType",
                "org.freehep.graphicsio.swf.SWFGraphics2D");
    }
}

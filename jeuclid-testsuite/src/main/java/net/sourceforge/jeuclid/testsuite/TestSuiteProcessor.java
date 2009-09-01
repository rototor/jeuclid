/*
 * Copyright 2009 - 2009 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.testsuite;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.converter.Processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;

/**
 * Process the actual TestSuite.
 * 
 * @version $Revision$
 */
public final class TestSuiteProcessor {

    private static final float DISPLAY_SIZE = 16.0f;

    private static final Processor MML2SVGPROCESSOR = Processor.getInstance();
    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(Processor.class);

    private final Transformer modificationTransformer;

    private final MutableLayoutContext context;

    private static final class SingletonHolder {
        private static final TestSuiteProcessor INSTANCE = new TestSuiteProcessor();

        private SingletonHolder() {
        }
    }

    private TestSuiteProcessor() {
        this.context = new LayoutContextImpl(LayoutContextImpl
                .getDefaultLayoutContext());
        this.context.setParameter(Parameter.MATHSIZE,
                TestSuiteProcessor.DISPLAY_SIZE);
        Transformer t;
        try {
            t = TransformerFactory.newInstance().newTemplates(
                    new StreamSource(DOMBuilder.class
                            .getResourceAsStream("/support/ModifySuite3.xsl")))
                    .newTransformer();
        } catch (final TransformerConfigurationException e) {
            t = null;
        }
        this.modificationTransformer = t;
    }

    /**
     * Retrieve the TestSuiteProcessor singleton object.
     * 
     * @return the TestSuiteProcessor.
     */
    public static TestSuiteProcessor getInstance() {
        return TestSuiteProcessor.SingletonHolder.INSTANCE;
    }

    /**
     * Process Source testsuite xhtml file to xhtml.
     * 
     * @param inputSource
     *            InputSource
     * @param result
     *            Result
     * @param apply3Mod
     *            if true apply modifications specific to testsuite3
     * @return true if something was written to result.
     */
    public boolean process(final Source inputSource, final Result result,
            final boolean apply3Mod) {
        boolean processed;
        final DOMResult intermediate = new javax.xml.transform.dom.DOMResult();
        try {
            if (apply3Mod && (this.modificationTransformer != null)) {
                TestSuiteProcessor.MML2SVGPROCESSOR.process(inputSource,
                        intermediate);
                final Node head = intermediate.getNode();
                this.modificationTransformer.transform(new DOMSource(head),
                        result);
            } else {
                TestSuiteProcessor.MML2SVGPROCESSOR
                        .process(inputSource, result);
            }
            processed = true;
        } catch (final NullPointerException npe) {
            TestSuiteProcessor.LOGGER.warn(npe.getMessage(), npe);
            processed = false;
        } catch (final TransformerException te) {
            TestSuiteProcessor.LOGGER.warn(te.getMessage(), te);
            processed = false;
        }
        return processed;
    }

}

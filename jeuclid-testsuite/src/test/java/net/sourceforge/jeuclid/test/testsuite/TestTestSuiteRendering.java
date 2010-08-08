/*
 * Copyright 2008 - 2009 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.test.testsuite;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.elements.generic.DocumentElement;
import net.sourceforge.jeuclid.layout.JEuclidView;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutableNode;
import net.sourceforge.jeuclid.parser.Parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

/**
 * Compares Rendering of the testsuite to previous renderings to catch
 * regression bugs.
 * 
 * @version $Revision$
 */
// CHECKSTYLE:OFF
// This is a test class.
public class TestTestSuiteRendering {
    private static final float LARGE_FONT_SIZE = 48.0f;

    // CHECKSTYLE:ON
    /**
     * Logger for this class.
     */
    private static final Log LOGGER = LogFactory
            .getLog(TestTestSuiteRendering.class);

    private static final String RENDER_NAME = "renderInfos.ser";

    private final LayoutContext layoutContext;

    private final Graphics2D g2d;

    private final Map<String, List<RenderInfo>> currentRendering = new HashMap<String, List<RenderInfo>>();

    private final Map<String, List<RenderInfo>> oldRendering;

    private final File tempDir;

    /**
     * Default Constructor.
     */
    public TestTestSuiteRendering() {
        final MutableLayoutContext mlc = new LayoutContextImpl(
                LayoutContextImpl.getDefaultLayoutContext());
        mlc.setParameter(Parameter.ANTIALIAS, true);
        mlc.setParameter(Parameter.MATHSIZE,
                TestTestSuiteRendering.LARGE_FONT_SIZE);
        mlc.setParameter(Parameter.FONTS_SANSSERIF, "DejaVu Sans");
        mlc.setParameter(Parameter.FONTS_SERIF, "DejaVu Serif");
        this.layoutContext = mlc;

        final Image tempimage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        this.g2d = (Graphics2D) tempimage.getGraphics();

        this.tempDir = new File("temp");
        if (!this.tempDir.isDirectory()) {
            final boolean success = this.tempDir.mkdirs();
            assert success;
        }
        this.oldRendering = this.loadRenderings();
    }

    private void createInfo(final JEuclidView view, final LayoutableNode node,
            final List<RenderInfo> renderInfos) {
        final LayoutInfo info = view.getInfo(node);
        final RenderInfo renderInfo = new RenderInfo(node.getNodeName(), info
                .getAscentHeight(LayoutStage.STAGE2), info
                .getDescentHeight(LayoutStage.STAGE2), info
                .getWidth(LayoutStage.STAGE2),
                info.getPosX(LayoutStage.STAGE2), info
                        .getPosY(LayoutStage.STAGE2));
        renderInfos.add(renderInfo);
        for (final LayoutableNode n : node.getChildrenToLayout()) {
            this.createInfo(view, n, renderInfos);
        }
    }

    /**
     * Renders complete testsuite and compares results.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testRenderMml2Testsuite() throws Exception {

        final InputStream i = ClassLoader
                .getSystemResourceAsStream("mml2-testsuite.list");
        final BufferedReader br = new BufferedReader(new InputStreamReader(i,
                "UTF-8"));
        String line;
        final List<String> failures = new ArrayList<String>();
        while ((line = br.readLine()) != null) {
            this.renderAndCompare(line, failures);
        }
        Assert.assertTrue(failures.isEmpty(), failures.toString());
        this.saveCurrentRenderings();
    }

    private void saveCurrentRenderings() throws Exception {
        final ObjectOutputStream oo = new ObjectOutputStream(
                new FileOutputStream(new File(this.tempDir,
                        TestTestSuiteRendering.RENDER_NAME)));
        oo.writeObject(this.currentRendering);
        oo.close();
    }

    @SuppressWarnings("unchecked")
    private Map<String, List<RenderInfo>> loadRenderings() {
        Map<String, List<RenderInfo>> retVal = null;
        try {
            final ObjectInputStream oi = new ObjectInputStream(
                    new FileInputStream(new File(this.tempDir,
                            TestTestSuiteRendering.RENDER_NAME)));
            retVal = (Map<String, List<RenderInfo>>) oi.readObject();
            oi.close();
        } catch (final Exception e) {
            retVal = null;
        }
        if (retVal == null) {
            TestTestSuiteRendering.LOGGER
                    .info("Could not load old rendering infos. Will create them for the first time.");
            retVal = new HashMap<String, List<RenderInfo>>();
        }
        return retVal;
    }

    private void renderAndCompare(final String name, final List<String> failures)
            throws Exception {
        final List<RenderInfo> currentList = this.render(name);
        this.currentRendering.put(name, currentList);
        final List<RenderInfo> oldList = this.oldRendering.get(name);
        this.compareRenderings(name, currentList, oldList, failures);
    }

    private void compareRenderings(final String name,
            final List<RenderInfo> currentList, final List<RenderInfo> oldList,
            final List<String> failures) {
        boolean have = false;
        if (oldList == null) {
            // TODO: Maybe log?
            return;
        }
        if (currentList.size() != oldList.size()) {
            failures.add(name + " has changed in number of elements! (old: "
                    + oldList.size() + " new: " + currentList.size() + ")");
            have = true;
        } else
        for (int i = 0; i < currentList.size(); i++) {
            final RenderInfo current = currentList.get(i);
            final RenderInfo old = oldList.get(i);
            final String similarities = current.checkSimilar(old);
            if (similarities.length() > 0) {
                failures.add(name + " differes for element "
                        + current.getElementName() + " in" + similarities);
                have = true;
            }
        }
        if (have) {
            failures.add("\n");
        }
    }

    private List<RenderInfo> render(final String line) throws Exception {
        final List<RenderInfo> currentList = new LinkedList<RenderInfo>();
        try {
            final InputStream i = ClassLoader.getSystemResourceAsStream(line);
            final Document d = Parser.getInstance().parseStreamSource(
                    new StreamSource(i));
            final DocumentElement docElement = DOMBuilder.getInstance()
                    .createJeuclidDom(d);
            final JEuclidView view = new JEuclidView(docElement,
                    this.layoutContext, this.g2d);
            // Forces Layout
            view.getAscentHeight();
            this.createInfo(view, docElement, currentList);
        } catch (final IOException io) {
            // ignore
        }
        return currentList;
    }
}

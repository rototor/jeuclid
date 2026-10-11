/*
 * Copyright 2026 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.xmlgraphics;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import net.sourceforge.jeuclid.Constants;

import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSource;
import org.apache.xmlgraphics.image.loader.impl.DefaultImageContext;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link PreloaderMathML}.
 */
public class PreloaderMathMLTest {

    private final ImageContext context = new DefaultImageContext();

    /**
     * A bitmap is not handed to the XML parser, so nothing keeps its stream
     * (and the file behind it) reachable once the caller is done with it.
     *
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testBitmapIsNotParsed() throws Exception {
        final ByteArrayOutputStream png = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB),
                "png", png);
        ImageInputStream in = new MemoryCacheImageInputStream(
                new ByteArrayInputStream(png.toByteArray()));

        final ImageInfo info = new PreloaderMathML().preloadImage("test.png",
                new ImageSource(in, "test.png", true), this.context);
        Assert.assertNull(info);
        Assert.assertEquals(0, in.getStreamPosition());

        final WeakReference<ImageInputStream> ref = new WeakReference<ImageInputStream>(
                in);
        in = null;
        for (int i = 0; i < 10 && ref.get() != null; i++) {
            System.gc();
            Thread.sleep(50);
        }
        Assert.assertNull("the stream is still reachable", ref.get());
    }

    /**
     * MathML is still recognised, here after a byte order mark, an XML
     * declaration and white space.
     *
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testMathMLIsPreloaded() throws Exception {
        final String mathml = "﻿<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "  <math xmlns=\"http://www.w3.org/1998/Math/MathML\"><mi>x</mi></math>";
        final ImageInputStream in = new MemoryCacheImageInputStream(
                new ByteArrayInputStream(mathml.getBytes("UTF-8")));

        final ImageInfo info = new PreloaderMathML().preloadImage("test.mml",
                new ImageSource(in, "test.mml", true), this.context);
        Assert.assertNotNull(info);
        Assert.assertEquals(Constants.MATHML_MIMETYPE, info.getMimeType());
    }
}

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

package net.sourceforge.jeuclid.test;

import java.awt.Font;

import net.sourceforge.jeuclid.font.FontFactory;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test for {@link net.sourceforge.jeuclid.font.FontFactory} class and its
 * concrete subclasses.
 * 
 * @version $Revision$
 */
public final class FontFactoryTest {

    private static final String DIALOG = "Dialog";

    private static final int TEST_FONT_SIZE = 14;

    /** */
    public FontFactoryTest() {
    }

    /**
     * Tests basic FontFactory functionality (returning standard fonts,
     * defaulting to standard fonts).
     * 
     * @throws Exception
     *             if anything goes wrong
     */
    @Test
    public void fontFactoryTest() throws Exception {
        final String builtInFamily = "Monospaced";
        final String defaultFamily = FontFactoryTest.DIALOG;
        final String customFamily = "Bitstream Vera Sans Mono";
        final String customAltname = "BitstreamVeraSansMono-Roman";
        final FontFactory fontFactory = FontFactory.getInstance();

        // get a non-default built-in font
        final Font builtInFont = fontFactory.getFont(builtInFamily,
                Font.PLAIN, FontFactoryTest.TEST_FONT_SIZE);
        Assert.assertEquals(builtInFamily, builtInFont.getFamily());

        // get a non-default, not cached font
        final Font foo = fontFactory.getFont("foo456789", Font.PLAIN,
                FontFactoryTest.TEST_FONT_SIZE);
        Assert.assertEquals(foo.getFamily(), defaultFamily);

        // register a custom font and then get it
        fontFactory.registerFont(Font.TRUETYPE_FONT, this.getClass()
                .getResourceAsStream("/VeraMono.ttf"));
        Font customFont = fontFactory.getFont(customFamily, Font.PLAIN,
                FontFactoryTest.TEST_FONT_SIZE);
        if (customFont.getFamily().equals(FontFactoryTest.DIALOG)) {
            customFont = fontFactory.getFont(customAltname, Font.PLAIN,
                    FontFactoryTest.TEST_FONT_SIZE);
            Assert.assertEquals(customFont.getFamily(), customAltname);
        } else {
            Assert.assertEquals(customFont.getFamily(), customFamily);
        }
        Assert.assertEquals(customFont.getSize(),
                FontFactoryTest.TEST_FONT_SIZE);
    }

}

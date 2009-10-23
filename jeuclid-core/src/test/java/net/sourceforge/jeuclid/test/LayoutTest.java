/*
 * Copyright 2002 - 2008 JEuclid, http://jeuclid.sf.net
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

import java.util.List;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.elements.presentation.general.Mfrac;
import net.sourceforge.jeuclid.elements.presentation.token.Mi;
import net.sourceforge.jeuclid.elements.presentation.token.Mn;
import net.sourceforge.jeuclid.layout.JEuclidView;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutableNode;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLDocument;
import org.w3c.dom.mathml.MathMLFractionElement;
import org.w3c.dom.mathml.MathMLMathElement;
import org.w3c.dom.mathml.MathMLPresentationContainer;
import org.w3c.dom.mathml.MathMLPresentationToken;
import org.w3c.dom.views.DocumentView;

/**
 * @version $Revision$
 */
public class LayoutTest {

    /**
     * Test string with xml header.
     */
    final static public String TEST1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
            + "<mrow><mo>(</mo><mn>555</mn></mrow></math>";

    /**
     * Test if there is something in the view.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testViewNotEmpty() throws Exception {
        final MathMLDocument docElement = DOMBuilder.getInstance()
                .createJeuclidDom(
                        MathMLParserSupport.parseString(LayoutTest.TEST1));
        final JEuclidView view = (JEuclidView) (((DocumentView) docElement)
                .getDefaultView());
        Assert.assertTrue(view.getAscentHeight() > 1.0f,
                "View has not enough ascent: " + view.getAscentHeight());
        Assert.assertTrue(view.getWidth() > 1.0f,
                "View has not enoguh width: " + view.getWidth());
        Assert.assertTrue(view.getDescentHeight() >= 0.0f,
                "Descent Height < 0: " + view.getDescentHeight());
    }

    /**
     * Tests if view modifies itself when DOM is modified.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testViewMutable() throws Exception {
        final MathMLDocument docElement = DOMBuilder.getInstance()
                .createJeuclidDom(
                        MathMLParserSupport.parseString(LayoutTest.TEST1));
        final JEuclidView view = (JEuclidView) (((DocumentView) docElement)
                .getDefaultView());

        final float oldAscent = view.getAscentHeight();
        final float oldWidth = view.getWidth();

        final MathMLMathElement mathElement = (MathMLMathElement) docElement
                .getFirstChild();
        final MathMLPresentationContainer mrow = (MathMLPresentationContainer) mathElement
                .getFirstChild();
        final LayoutableNode mop = (LayoutableNode) mrow.getFirstChild();
        final float oldmopascent = view.getInfo(mop).getAscentHeight(
                LayoutStage.STAGE2);

        final MathMLFractionElement mfrac = (MathMLFractionElement) docElement
                .createElement(Mfrac.ELEMENT);

        final MathMLPresentationToken denom = (MathMLPresentationToken) docElement
                .createElement(Mn.ELEMENT);

        denom.setTextContent("123");
        mfrac.setDenominator(denom);
        final MathMLPresentationToken nom = (MathMLPresentationToken) docElement
                .createElement(Mi.ELEMENT);
        nom.setTextContent("X");
        mfrac.setNumerator(nom);
        mrow.appendChild(mfrac);

        Assert.assertTrue(view.getWidth() > oldWidth,
                "Width of view should increase: " + view.getWidth() + " > "
                        + oldWidth);
        Assert.assertTrue(view.getAscentHeight() > oldAscent,
                "Heightof view should increase: " + view.getAscentHeight()
                        + " > " + oldAscent);

        final float newmopascent = view.getInfo(mop).getAscentHeight(
                LayoutStage.STAGE2);
        Assert.assertTrue(newmopascent > oldmopascent,
                "Operator should be larger: " + newmopascent + " > "
                        + oldmopascent);
    }

    /**
     * Test whitespace handling.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testWhitespace() throws Exception {
        final MathMLDocument docElement = DOMBuilder
                .getInstance()
                .createJeuclidDom(
                        MathMLParserSupport
                                .parseString("<math><mtext>x x</mtext><mtext> x x </mtext><mtext>x    x</mtext></math>"));
        final JEuclidView view = (JEuclidView) (((DocumentView) docElement)
                .getDefaultView());

        final MathMLMathElement mathElement = (MathMLMathElement) docElement
                .getFirstChild();
        final LayoutableNode m1 = (LayoutableNode) mathElement
                .getFirstChild();
        final LayoutableNode m2 = (LayoutableNode) m1.getNextSibling();
        final LayoutableNode m3 = (LayoutableNode) m2.getNextSibling();

        // To trigger layout
        view.getWidth();

        final float w1 = view.getInfo(m1).getWidth(LayoutStage.STAGE2);
        final float w2 = view.getInfo(m2).getWidth(LayoutStage.STAGE2);
        final float w3 = view.getInfo(m3).getWidth(LayoutStage.STAGE2);

        Assert.assertEquals(w2, w1,
                "Whitespace around text should be trimmed to none");
        Assert.assertEquals(w3, w1,
                "Whitespace inside text should be trimmed to 1");

    }

    /**
     * Test MO without math parent.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testMoWithoutParent() throws Exception {
        final MathMLDocument docElement = DOMBuilder
                .getInstance()
                .createJeuclidDom(
                        MathMLParserSupport
                                .parseString("<mrow><mo>&#x2211;</mo></mrow>"));
        final JEuclidView view = (JEuclidView) (((DocumentView) docElement)
                .getDefaultView());

        // To trigger layout
        view.getWidth();
    }

    /**
     * Test MO without any parent.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testMoWithoutParent2() throws Exception {
        final MathMLDocument docElement = DOMBuilder.getInstance()
                .createJeuclidDom(
                        MathMLParserSupport.parseString("<mo>&#x2211;</mo>"));
        final JEuclidView view = (JEuclidView) (((DocumentView) docElement)
                .getDefaultView());

        // To trigger layout
        view.getWidth();
    }

    /**
     * Test if getNodesAt() works.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testGetNodesAt() throws Exception {
        final MathMLDocument docElement = DOMBuilder.getInstance()
                .createJeuclidDom(
                        MathMLParserSupport.parseString(LayoutTest.TEST1));
        final JEuclidView view = (JEuclidView) (((DocumentView) docElement)
                .getDefaultView());
        final List<JEuclidView.NodeRect> rlist = view.getNodesAt(15, 0, 0, 0);
        Assert.assertSame(rlist.get(0).getNode(), docElement);
        final Node math = docElement.getFirstChild();
        Assert.assertSame(rlist.get(1).getNode(), math);
        final Node mrow = math.getFirstChild();
        Assert.assertSame(rlist.get(2).getNode(), mrow);
        final Node five = mrow.getFirstChild().getNextSibling();
        Assert.assertSame(rlist.get(3).getNode(), five);
    }
}

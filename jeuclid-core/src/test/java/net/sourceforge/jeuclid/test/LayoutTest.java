package net.sourceforge.jeuclid.test;

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
import org.w3c.dom.mathml.MathMLDocument;
import org.w3c.dom.mathml.MathMLFractionElement;
import org.w3c.dom.mathml.MathMLMathElement;
import org.w3c.dom.mathml.MathMLPresentationContainer;
import org.w3c.dom.mathml.MathMLPresentationToken;
import org.w3c.dom.views.DocumentView;

public class LayoutTest {

    /**
     * Test string with xml header.
     */
    final public static String TEST1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
            + "<mrow><mo>(</mo><mn>5</mn></mrow></math>";

    @Test
    public void testViewNotEmpty() throws Exception {
        final MathMLDocument docElement = DOMBuilder.getDOMBuilder()
                .createJeuclidDom(
                        MathMLParserSupport.parseString(LayoutTest.TEST1));
        final JEuclidView view = (JEuclidView) (((DocumentView) docElement)
                .getDefaultView());
        Assert.assertTrue(view.getAscentHeight() > 1.0f);
        Assert.assertTrue(view.getWidth() > 1.0f);
        Assert.assertTrue(view.getDescentHeight() >= 0.0f);
    }

    @Test
    public void testViewMutable() throws Exception {
        final MathMLDocument docElement = DOMBuilder.getDOMBuilder()
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

    @Test
    public void testWhitespace() throws Exception {
        final MathMLDocument docElement = DOMBuilder
                .getDOMBuilder()
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

}

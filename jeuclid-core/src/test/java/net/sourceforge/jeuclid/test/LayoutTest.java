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
import org.w3c.dom.views.DocumentView;

public class LayoutTest {

    /**
     * Test string with xml header.
     */
    final public static String TEST1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
            + "<mrow><mo>(</mo><mn>5</mn></mrow></math>";

    // final public static String TEST2 = "<?xml version=\"1.0\"
    // encoding=\"ISO-8859-1\"?><math mode=\"display\">"
    // + "<mrow><mo>(</mo><mn>5</mn></mrow></math>";

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

        final MathMLFractionElement mfrac = new Mfrac();
        final Mn denom = new Mn();
        denom.setTextContent("123");
        mfrac.setDenominator(denom);
        final Mi nom = new Mi();
        nom.setTextContent("X");
        mfrac.setNumerator(nom);
        mrow.appendChild(mfrac);

        Assert.assertTrue(view.getWidth() > oldWidth,
                "Width should increase: " + view.getWidth() + " > "
                        + oldWidth);
        Assert.assertTrue(view.getAscentHeight() > oldAscent,
                "Height should increase: " + view.getAscentHeight() + " > "
                        + oldAscent);

        final float newmopascent = view.getInfo(mop).getAscentHeight(
                LayoutStage.STAGE2);
        Assert.assertTrue(newmopascent > oldmopascent,
                "Operator should be larger: " + newmopascent + " > "
                        + oldmopascent);
    }
}

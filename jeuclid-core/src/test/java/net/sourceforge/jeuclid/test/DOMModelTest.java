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

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.MathMLSerializer;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.elements.JEuclidElementFactory;
import net.sourceforge.jeuclid.elements.presentation.general.Mfrac;
import net.sourceforge.jeuclid.elements.presentation.general.Mrow;
import net.sourceforge.jeuclid.elements.presentation.token.Mi;
import net.sourceforge.jeuclid.elements.presentation.token.Mo;
import net.sourceforge.jeuclid.elements.support.attributes.AbstractAttributeMap;
import net.sourceforge.jeuclid.elements.support.attributes.AttributeMap;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.mathml.MathMLActionElement;
import org.w3c.dom.mathml.MathMLAlignGroupElement;
import org.w3c.dom.mathml.MathMLAlignMarkElement;
import org.w3c.dom.mathml.MathMLAnnotationElement;
import org.w3c.dom.mathml.MathMLDocument;
import org.w3c.dom.mathml.MathMLEncloseElement;
import org.w3c.dom.mathml.MathMLFencedElement;
import org.w3c.dom.mathml.MathMLFractionElement;
import org.w3c.dom.mathml.MathMLGlyphElement;
import org.w3c.dom.mathml.MathMLLabeledRowElement;
import org.w3c.dom.mathml.MathMLMathElement;
import org.w3c.dom.mathml.MathMLMultiScriptsElement;
import org.w3c.dom.mathml.MathMLOperatorElement;
import org.w3c.dom.mathml.MathMLPaddedElement;
import org.w3c.dom.mathml.MathMLPresentationContainer;
import org.w3c.dom.mathml.MathMLPresentationToken;
import org.w3c.dom.mathml.MathMLRadicalElement;
import org.w3c.dom.mathml.MathMLScriptElement;
import org.w3c.dom.mathml.MathMLSemanticsElement;
import org.w3c.dom.mathml.MathMLSpaceElement;
import org.w3c.dom.mathml.MathMLStringLitElement;
import org.w3c.dom.mathml.MathMLStyleElement;
import org.w3c.dom.mathml.MathMLTableCellElement;
import org.w3c.dom.mathml.MathMLTableElement;
import org.w3c.dom.mathml.MathMLTableRowElement;
import org.w3c.dom.mathml.MathMLUnderOverElement;

/**
 * Various tests for the DOM model.
 * 
 * @author Max Berger
 */
public class DOMModelTest {

    final static AttributeMap aMap = new AbstractAttributeMap() {

        @Override
        protected String getAttribute(String attrName) {
            return null;
        }

        @Override
        protected String getAttributeNS(String namespace, String attrName) {
            return this.getAttribute(attrName);
        }

        public Map<String, String> getAsMap() {
            return new HashMap<String, String>();
        }
    };

    /**
     * Tests is the "id" attribute works.
     * 
     * @throws Exception
     *             if anything goes wrong.
     */
    @Test
    public void testID() throws Exception {
        final Document docWithID = MathMLParserSupport
                .parseString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
                        + "<mrow id='abc'><mn>1</mn></mrow></math>");

        final MathMLDocument docElement = DOMBuilder.getDOMBuilder()
                .createJeuclidDom(
                        docWithID,
                        new MathBase(LayoutContextImpl
                                .getDefaultLayoutContext()));

        final MathMLMathElement mathElement = (MathMLMathElement) docElement
                .getFirstChild();

        // TODO: enable this test
        // assertEquals(mathElement.getDisplay(), "block");
        final MathMLPresentationContainer row = (MathMLPresentationContainer) mathElement
                .getFirstChild();
        Assert.assertNotNull(row);
        Assert.assertEquals(row.getId(), "abc");
    }

    /**
     * Tests if serialization works.
     * 
     * @throws Exception
     *             if anything goes wrong.
     */
    @Test
    public void testSerialization() throws Exception {
        final Document origDoc = MathMLParserSupport
                .parseString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
                        + "<mrow id='abc'><mn>1</mn></mrow></math>");
        final MathMLDocument mathMLDoc = DOMBuilder.getDOMBuilder()
                .createJeuclidDom(
                        origDoc,
                        new MathBase(LayoutContextImpl
                                .getDefaultLayoutContext()));
        final String reserialStr = MathMLSerializer.serializeDocument(
                mathMLDoc, false, false);

        final Document reserial = MathMLParserSupport
                .parseString(reserialStr);
        Assert.assertTrue(reserial.isEqualNode(origDoc));
    }

    /**
     * Tests if serialization with malignmark works.
     * 
     * @throws Exception
     *             if anything goes wrong.
     */
    @Test
    public void testSerialization2() throws Exception {
        final Document origDoc = MathMLParserSupport
                .parseString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
                        + "<mtext>Alignment<malignmark/>Test</mtext></math>");
        final MathMLDocument mathMLDoc = DOMBuilder.getDOMBuilder()
                .createJeuclidDom(
                        origDoc,
                        new MathBase(LayoutContextImpl
                                .getDefaultLayoutContext()));
        final String reserialStr = MathMLSerializer.serializeDocument(
                mathMLDoc, false, false);

        final Document reserial = MathMLParserSupport
                .parseString(reserialStr);
        Assert.assertTrue(reserial.isEqualNode(origDoc));
    }

    /**
     * Tests is all attributes on mathOperator work.
     * 
     * @throws Exception
     *             if anything goes wrong.
     */
    @Test
    public void testMOAttrs() throws Exception {
        final Document doc = MathMLParserSupport
                .parseString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
                        + "<mo stretchy='true'>X</mo>"
                        + "<mo stretchy='false'>Y</mo>"
                        + "<mo>&#x0007d;</mo>"
                        + "<mo>&#x02254;</mo>"
                        + "<mo>&#x0201d;</mo>" + "</math>");
        final MathMLDocument docElement = DOMBuilder.getDOMBuilder()
                .createJeuclidDom(
                        doc,
                        new MathBase(LayoutContextImpl
                                .getDefaultLayoutContext()));

        final MathMLMathElement mathElement = (MathMLMathElement) docElement
                .getFirstChild();

        final MathMLOperatorElement mo = (MathMLOperatorElement) mathElement
                .getChildNodes().item(0);
        Assert.assertNotNull(mo);
        Assert.assertTrue(Boolean.parseBoolean(mo.getStretchy()));
        final Mo mo2 = (Mo) mathElement.getChildNodes().item(1);
        Assert.assertNotNull(mo2);
        Assert.assertFalse(Boolean.parseBoolean(mo2.getStretchy()));
        final Mo mo3 = (Mo) mathElement.getChildNodes().item(2);
        // Should be strechty, since it is fence
        Assert.assertTrue(Boolean.parseBoolean(mo3.getStretchy()));
        final Mo mo4 = (Mo) mathElement.getChildNodes().item(3);
        Assert.assertFalse(Boolean.parseBoolean(mo4.getStretchy()));
        final Mo mo5 = (Mo) mathElement.getChildNodes().item(2);
        Assert.assertTrue(Boolean.parseBoolean(mo5.getStretchy()));
    }

    /**
     * Tests of objects created from MathElementFactory implement the proper
     * interfaces from W3C Dom.
     * 
     * @throws Exception
     *             if anything goes wrong.
     */
    @Test
    public void testInterfaces() throws Exception {
        final MathBase base = new MathBase(LayoutContextImpl
                .getDefaultLayoutContext());

        // This mapping is taken straight from Table D.2.2, MathML 2.0 spec
        // TODO: Someday none of these should be commented out.

        Assert.assertTrue(JEuclidElementFactory.elementFromName("math",
                DOMModelTest.aMap, base) instanceof MathMLMathElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("mi",
                DOMModelTest.aMap, base) instanceof MathMLPresentationToken);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("mn",
                DOMModelTest.aMap, base) instanceof MathMLPresentationToken);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("mo",
                DOMModelTest.aMap, base) instanceof MathMLOperatorElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("mtext",
                DOMModelTest.aMap, base) instanceof MathMLPresentationToken);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("mspace",
                DOMModelTest.aMap, base) instanceof MathMLSpaceElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("ms",
                DOMModelTest.aMap, base) instanceof MathMLStringLitElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("mglyph",
                DOMModelTest.aMap, base) instanceof MathMLGlyphElement);
        Assert
                .assertTrue(JEuclidElementFactory.elementFromName("mrow",
                        DOMModelTest.aMap, base) instanceof MathMLPresentationContainer);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("mfrac",
                DOMModelTest.aMap, base) instanceof MathMLFractionElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("msqrt",
                DOMModelTest.aMap, base) instanceof MathMLRadicalElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("mroot",
                DOMModelTest.aMap, base) instanceof MathMLRadicalElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("mstyle",
                DOMModelTest.aMap, base) instanceof MathMLStyleElement);
        Assert
                .assertTrue(JEuclidElementFactory.elementFromName("merror",
                        DOMModelTest.aMap, base) instanceof MathMLPresentationContainer);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("mpadded",
                DOMModelTest.aMap, base) instanceof MathMLPaddedElement);
        Assert
                .assertTrue(JEuclidElementFactory.elementFromName("mphantom",
                        DOMModelTest.aMap, base) instanceof MathMLPresentationContainer);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("mfenced",
                DOMModelTest.aMap, base) instanceof MathMLFencedElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("menclose",
                DOMModelTest.aMap, base) instanceof MathMLEncloseElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("msub",
                DOMModelTest.aMap, base) instanceof MathMLScriptElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("msup",
                DOMModelTest.aMap, base) instanceof MathMLScriptElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("msubsup",
                DOMModelTest.aMap, base) instanceof MathMLScriptElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("munder",
                DOMModelTest.aMap, base) instanceof MathMLUnderOverElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("mover",
                DOMModelTest.aMap, base) instanceof MathMLUnderOverElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("munderover",
                DOMModelTest.aMap, base) instanceof MathMLUnderOverElement);
        Assert
                .assertTrue(JEuclidElementFactory.elementFromName(
                        "mmultiscripts", DOMModelTest.aMap, base) instanceof MathMLMultiScriptsElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("mtable",
                DOMModelTest.aMap, base) instanceof MathMLTableElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("mlabeledtr",
                DOMModelTest.aMap, base) instanceof MathMLLabeledRowElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("mtr",
                DOMModelTest.aMap, base) instanceof MathMLTableRowElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("mtd",
                DOMModelTest.aMap, base) instanceof MathMLTableCellElement);
        Assert
                .assertTrue(JEuclidElementFactory.elementFromName(
                        "maligngroup", DOMModelTest.aMap, base) instanceof MathMLAlignGroupElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("malignmark",
                DOMModelTest.aMap, base) instanceof MathMLAlignMarkElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("maction",
                DOMModelTest.aMap, base) instanceof MathMLActionElement);
        // Assert
        // .assertTrue(MathElementFactory.elementFromName("cn", aMap,
        // base) instanceof MathMLCnElement);
        // Assert
        // .assertTrue(MathElementFactory.elementFromName("ci", aMap,
        // base) instanceof MathMLCiElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("csymbol",
        // aMap,
        // base) instanceof MathMLCsymbolElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("apply", aMap,
        // base) instanceof MathMLApplyElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("reln", aMap,
        // base) instanceof MathMLContentContainer);
        // Assert
        // .assertTrue(MathElementFactory.elementFromName("fn", aMap,
        // base) instanceof MathMLFnElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("interval",
        // aMap, base) instanceof MathMLIntervalElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("inverse",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("condition",
        // aMap, base) instanceof MathMLConditionElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("declare",
        // aMap,
        // base) instanceof MathMLDeclareElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("lambda",
        // aMap,
        // base) instanceof MathMLLambdaElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("compose",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("ident", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("domain",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("codomain",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("image", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert
        // .assertTrue(MathElementFactory.elementFromName(
        // "domainofapplication", aMap, base) instanceof
        // MathMLContentContainer);
        // Assert.assertTrue(MathElementFactory.elementFromName("piecewise",
        // aMap, base) instanceof MathMLPiecewiseElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("piece", aMap,
        // base) instanceof MathMLCaseElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("otherwise",
        // aMap, base) instanceof MathMLContentContainer);
        // Assert.assertTrue(MathElementFactory.elementFromName("quotient",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("exp", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("factorial",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("divide",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("max", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("min", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("minus", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("plus", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("power", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("rem", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("times", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("root", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("gcd", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("and", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert
        // .assertTrue(MathElementFactory.elementFromName("or", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("xor", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("not", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("implies",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("forall",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("exists",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("abs", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("conjugate",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("arg", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("real", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("imaginary",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("lcm", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("floor", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("ceiling",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert
        // .assertTrue(MathElementFactory.elementFromName("eq", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("neq", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert
        // .assertTrue(MathElementFactory.elementFromName("gt", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert
        // .assertTrue(MathElementFactory.elementFromName("lt", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("geq", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("leq", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("equivalent",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("approx",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("factorof",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("int", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("diff", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("partialdiff",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("lowlimit",
        // aMap, base) instanceof MathMLContentContainer);
        // Assert.assertTrue(MathElementFactory.elementFromName("uplimit",
        // aMap,
        // base) instanceof MathMLContentContainer);
        // Assert.assertTrue(MathElementFactory.elementFromName("bvar", aMap,
        // base) instanceof MathMLBvarElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("degree",
        // aMap,
        // base) instanceof MathMLContentContainer);
        // Assert.assertTrue(MathElementFactory.elementFromName("divergence",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("grad", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("curl", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("laplacian",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("set", aMap,
        // base) instanceof MathMLSetElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("list", aMap,
        // base) instanceof MathMLListElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("union", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("intersect",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert
        // .assertTrue(MathElementFactory.elementFromName("in", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("notin", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("subset",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("prsubset",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("notsubset",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("notprsubset",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("setdiff",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("card", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert
        // .assertTrue(MathElementFactory.elementFromName(
        // "cartesianproduct", aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("sum", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("product",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("limit", aMap,
        // base) instanceof MathMLPredefinedSymbol);

        // This interface does not exist.
        // Assert.assertTrue(MathElementFactory.elementFromName("tendsto",
        // aMap,
        // base) instanceof MathMLTendsToElement);

        // Assert.assertTrue(MathElementFactory.elementFromName("exp", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert
        // .assertTrue(MathElementFactory.elementFromName("ln", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("log", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("sin", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("cos", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("tan", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("sec", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("csc", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("cot", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("sinh", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("cosh", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("tanh", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("sech", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("csch", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("coth", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("arcsin",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("arccos",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("arctan",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("arccosh",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("arccot",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("arccoth",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("arccsc",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("arccsch",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("arcsec",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("arcsech",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("arcsinh",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("arctanh",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("mean", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("sdev", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("variance",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("median",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("mode", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("moment",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("momentabout",
        // aMap, base) instanceof MathMLContentContainer);
        // Assert.assertTrue(MathElementFactory.elementFromName("vector",
        // aMap,
        // base) instanceof MathMLVectorElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("matrix",
        // aMap,
        // base) instanceof MathMLMatrixElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("matrixrow",
        // aMap, base) instanceof MathMLMatrixrowElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("determinant",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("transpose",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("selector",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("vectorproduct",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("scalarproduct",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("outerproduct",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("annotation",
                DOMModelTest.aMap, base) instanceof MathMLAnnotationElement);
        Assert.assertTrue(JEuclidElementFactory.elementFromName("semantics",
                DOMModelTest.aMap, base) instanceof MathMLSemanticsElement);
        // Assert
        // .assertTrue(MathElementFactory.elementFromName(
        // "annotation-xml", aMap, base) instanceof
        // MathMLXMLAnnotationElement);
        // Assert.assertTrue(MathElementFactory.elementFromName("integers",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("reals", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("rationals",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert
        // .assertTrue(MathElementFactory.elementFromName(
        // "naturalnumbers", aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("complexes",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("primes",
        // aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("exponentiale",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("imaginaryi",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("notanumber",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("true", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("false", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("emptyset",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert
        // .assertTrue(MathElementFactory.elementFromName("pi", aMap,
        // base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("eulergamma",
        // aMap, base) instanceof MathMLPredefinedSymbol);
        // Assert.assertTrue(MathElementFactory.elementFromName("infinity",
        // aMap, base) instanceof MathMLPredefinedSymbol);

    }

    @Test
    public void testFrac() throws Exception {
        final MathBase base = new MathBase(LayoutContextImpl
                .getDefaultLayoutContext());
        final MathMLFractionElement mfrac = new Mfrac(base);
        final Mi mi = new Mi(base);
        final Mrow mrow = new Mrow(base);
        final Mi mi2 = new Mi(base);
        mfrac.setDenominator(mi);
        mfrac.setNumerator(mrow);
        Assert.assertEquals(mi, mfrac.getDenominator());
        Assert.assertEquals(mrow, mfrac.getNumerator());
        Assert.assertEquals(mfrac.getChildNodes().getLength(), 2);
        mfrac.setNumerator(mi2);
        Assert.assertEquals(mi, mfrac.getDenominator());
        Assert.assertEquals(mi2, mfrac.getNumerator());
        Assert.assertEquals(mfrac.getChildNodes().getLength(), 2);
    }

    @Test
    public void testMMultiScripts() throws Exception {
        final Document doc = MathMLParserSupport
                .parseString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
                        + "<mmultiscripts>"
                        + "<mo>x</mo>"
                        + "<mi>a</mi>"
                        + "<mi>b</mi>"
                        + "<mprescripts/>"
                        + "<mi>c</mi>"
                        + "<mi>d</mi>" + "</mmultiscripts>" + "</math>");
        final MathBase base = new MathBase(LayoutContextImpl
                .getDefaultLayoutContext());
        final MathMLDocument docElement = DOMBuilder.getDOMBuilder()
                .createJeuclidDom(doc, base);
        final MathMLMathElement mathElement = (MathMLMathElement) docElement
                .getFirstChild();

        final MathMLMultiScriptsElement multi = (MathMLMultiScriptsElement) mathElement
                .getChildNodes().item(0);

        Assert.assertEquals(multi.getBase().getTextContent(), "x");
        Assert.assertEquals(multi.getSubScript(1).getTextContent(), "a");
        Assert.assertEquals(multi.getSuperScript(1).getTextContent(), "b");
        Assert.assertEquals(multi.getPreSubScript(1).getTextContent(), "c");
        Assert.assertEquals(multi.getPreSuperScript(1).getTextContent(), "d");
        Assert.assertEquals(multi.getNumprescriptcolumns(), 1);
        Assert.assertEquals(multi.getNumscriptcolumns(), 1);
        final Mi mi = new Mi(base);
        multi.insertPreSubScriptBefore(0, mi);
        Assert.assertEquals(multi.getNumprescriptcolumns(), 2);
        Assert.assertEquals(multi.getChildNodes().getLength(), 8);
    }

    @Test
    public void testMMultiScripts2() throws Exception {
        final Document doc = MathMLParserSupport
                .parseString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
                        + "<mmultiscripts>" + "</mmultiscripts>" + "</math>");
        final MathBase base = new MathBase(LayoutContextImpl
                .getDefaultLayoutContext());
        final MathMLDocument docElement = DOMBuilder.getDOMBuilder()
                .createJeuclidDom(doc, base);
        final MathMLMathElement mathElement = (MathMLMathElement) docElement
                .getFirstChild();
        final MathMLMultiScriptsElement multi = (MathMLMultiScriptsElement) mathElement
                .getChildNodes().item(0);
        multi.setSubScriptAt(1, new Mi(base));
        Assert.assertEquals(multi.getChildNodes().getLength(), 3);
        multi.setSuperScriptAt(1, new Mi(base));
        Assert.assertEquals(multi.getChildNodes().getLength(), 3);
        multi.insertPreSuperScriptBefore(0, new Mi(base));
        Assert.assertEquals(multi.getChildNodes().getLength(), 6);
        multi.insertPreSubScriptBefore(0, new Mi(base));
        Assert.assertEquals(multi.getChildNodes().getLength(), 8);
    }

}

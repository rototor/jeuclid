/*
 * Copyright 2009 Erhard Kuenzel
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
/*
 * <mo>·</mo><mo>×</mo>
 */

package cViewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Displays MathML content in a Swing Component.
 */
public final class JMathViewer extends JComponent implements SwingConstants,
        FocusListener, Accessible {

    private static final long serialVersionUID = 20081230L;

    private static String uiClassId = "MathViewerUI16";

    private int horizontalAlignment = SwingConstants.CENTER;

    // SwingConstants.CENTER;
    private int verticalAlignment = SwingConstants.CENTER;

    // SwingConstants.CENTER;
    protected Document document;

    // Der MathML_String als Speicher für den letzten Zustand
    private final MutableLayoutContext parameters;

    // Actions die mit Maus Tastatur oder Buttons ausgelöst werden können

    public JMathViewer() {
        this.parameters = new LayoutContextImpl(LayoutContextImpl
                .getDefaultLayoutContext());
        this.setParameter(Parameter.MATHSIZE, 24f);
        this.setUI(new MathViewerUI16());
        this
                .setContent("<math><mrow><mi>x</mi><mo>=</mo>"
                        + "<mfrac><mrow>"
                        + "<mrow><mo>-</mo><mi>b</mi></mrow>"
                        + "<mo>+</mo>"
                        + "<msqrt>"
                        + "<mrow>"
                        + "<msup><mi>b</mi><mn>2</mn></msup>"
                        + "<mo>-</mo>"
                        + "<mrow><mn>4</mn><mo><mchar name=\"InvisibleTimes\"/></mo><mi>a</mi><mo><mchar name=\"InvisibleTimes\"/></mo><mi>c</mi></mrow>"
                        + "</mrow>" + "</msqrt>" + "</mrow>" + "<mrow>"
                        + "<mn>2</mn>"
                        + "<mo><mchar name=\"InvisibleTimes\"/></mo>"
                        + "<mi>a</mi>" + "</mrow></mfrac>" + "</mrow></math>");
        // für die Bedienung über die Tastur wichtig
        this.setFocusable(false);
    }

    public void setDocument(final Document doc) {
        this.document = doc;
        this.firePropertyChange("documentNew", null, doc);
        // rearrangiere EuclidVisuals
        this.reval();
        // revalidate, repaint
    }

    // getter des org.w3c.dom.Node und weitere getter
    public Node getDocument() {
        return this.document;
    }

    public MutableLayoutContext getParameters() {
        return this.parameters;
    }

    @Override
    public Color getForeground() {
        return (Color) this.parameters.getParameter(Parameter.MATHCOLOR);
    }

    public float getFontSize() {
        return (Float) this.parameters.getParameter(Parameter.MATHSIZE);
    }

    public int getHorizontalAlignment() {
        return this.horizontalAlignment;
    }

    @Override
    public Dimension getPreferredSize() {
        final Dimension m = this.getMinimumSize();
        return new Dimension(m.width + 20, m.height + 20);
    }

    public MathViewerUI getUI() {
        return (MathViewerUI) this.ui;
    }

    @Override
    public String getUIClassID() {
        return JMathViewer.uiClassId;
    }

    public int getVerticalAlignment() {
        return this.verticalAlignment;
    }

    @Override
    public void setBackground(final Color c) {
        super.setBackground(c);
        this.reval();
    }

    // wird vom MathFrame aufgerufen
    public void setContent(final String contentString) {
        try {
            final String s = JMathElementHandler
                    .testMathMLString(contentString);
            // erzeugt das org.w3c.dom-Document und übergibt es
            final Document document = DOMBuilder.getInstance()
                    .createJeuclidDom(MathMLParserSupport.parseString(s));
            this.setDocument(document);
        } catch (final SAXException e) {
            throw new RuntimeException(e);
        } catch (final ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setParameter(final Parameter key, final Object newValue) {
        this.setParameters(Collections.singletonMap(key, newValue));
    }

    public void setParameters(final Map<Parameter, Object> newValues) {
        for (final Map.Entry<Parameter, Object> entry : newValues.entrySet()) {
            final Parameter key = entry.getKey();
            final Object oldValue = this.parameters.getParameter(key);
            this.parameters.setParameter(key, entry.getValue());
            this.firePropertyChange(key.name(), oldValue, this.parameters
                    .getParameter(key));
        }
        this.revalidate();
        this.repaint();
    }

    public void setFontSize(final float fontSize) {
        this.setParameter(Parameter.MATHSIZE, fontSize);
    }

    @Override
    public void setForeground(final Color fg) {
        super.setForeground(fg);
        this.setParameter(Parameter.MATHCOLOR, fg);
    }

    public void setHorizontalAlignment(final int hAlignment) {
        this.horizontalAlignment = hAlignment;
    }

    @Override
    public void setOpaque(final boolean opaque) {
        super.setOpaque(opaque);
        this.reval();
    }

    public void setVerticalAlignment(final int vAlignment) {
        this.verticalAlignment = vAlignment;
    }

    @Override
    public void setSize(final int width, final int height) {
        super.setSize(width, height);
    }

    // Private Utilities
    public void reval() {
        this.revalidate();
        this.repaint();
        this.requestFocusInWindow();
    }

    public void focusGained(final FocusEvent e) {
    }

    public void focusLost(final FocusEvent e) {
    }
}
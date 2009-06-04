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
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.accessibility.Accessible;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.MathMLSerializer;
import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.xml.sax.SAXException;

import cTree.CElement;
import cTree.CElementHelper;
import cTree.CPlusRow;
import cTree.CRolle;
import cTree.CTimesRow;
import cTree.adapter.DOMElementMap;
import cTree.adapter.EElementHelper;
import cTree.cAlter.AlterHandler;

/**
 * Displays MathML content in a Swing Component.
 */
public final class JMathComponent extends JComponent implements
        SwingConstants, FocusListener, Accessible, EventListener {

    private static final long serialVersionUID = 20081230L;

    private static String uiClassId = "MathComponentUI16";

    private static Class<?> mathComponentUIClass;

    private int horizontalAlignment = SwingConstants.CENTER;

    private int verticalAlignment = SwingConstants.CENTER;

    protected Document document;

    protected ArrayList<CElement> activeC;

    private Archiv archiv;

    private MutableLayoutContext parameters;

    private HashMap<Object, Action> actions;

    public JMathComponent() {
        // empty on purpose
    }

    public void initialize() {
        this.updateUI();
        this.activeC = new ArrayList<CElement>();
        this.archiv = new Archiv();
        final String initS = "<math><mrow><mi>x</mi><mo>=</mo>"
                + "<mfrac><mrow><mrow><mo>-</mo><mi>b</mi></mrow>"
                + "<mo>+</mo><msqrt><mrow><msup><mi>b</mi><mn>2</mn></msup>"
                + "<mo>-</mo><mrow><mn>4</mn><mo><mchar name=\"InvisibleTimes\"/></mo><mi>a</mi><mo><mchar name=\"InvisibleTimes\"/></mo><mi>c</mi></mrow>"
                + "</mrow></msqrt></mrow><mrow><mn>2</mn>"
                + "<mo><mchar name=\"InvisibleTimes\"/></mo>" + "<mi>a</mi>"
                + "</mrow></mfrac>" + "</mrow></math>";
        this.setContent(initS);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        this.requestFocusInWindow();
    }

    public MutableLayoutContext getParameters() {
        if (this.parameters == null) {
            this.parameters = new LayoutContextImpl(LayoutContextImpl
                    .getDefaultLayoutContext());
            this.setParameter(Parameter.MATHSIZE, 48f);
        }
        return this.parameters;
    }

    public CElement getCActive() {
        return this.activeC.isEmpty() ? null : this.activeC.get(0);
    }

    public void setCActive(final CElement cElement) {
        this.activeC.set(0, cElement);
    }

    public void clearCButFirst() {
        final CElement first = this.activeC.get(0);
        this.activeC.remove(first);
        for (final CElement el : this.activeC) {
            el.removeCLastProperty();
        }
        this.activeC.clear();
        this.activeC.add(first);
    }

    public void removeCActivity() {
        final CElement first = this.activeC.get(0);
        first.removeCActiveProperty();
        for (final CElement el : this.activeC) {
            el.removeCLastProperty();
        }
    }

    public class ZerlegeAction extends AbstractAction {
        private static final long serialVersionUID = 20081230L;

        public JTextField textField;

        public ZerlegeAction(final String string) {
            super(string);
        }

        public void actionPerformed(final ActionEvent ae) {
            final JMathComponent mathC = JMathComponent.this;
            final CElement cAct = mathC.getCActive();
            if (cAct != null) {
                mathC.addToUndo();
                mathC.removeCActivity();
                final String cleaned = ViewerFactory.getInst().getMathFrame()
                        .getTextField().getText().replace(" ", "");
                final CElement newAct = cAct.getParent().split(cAct, cleaned);
                newAct.setCActiveProperty();
                mathC.setCActive(newAct);
                mathC.requestFocusInWindow();
                mathC.modifyDocument();
                ViewerFactory.getInst().getMathFrame().getCounter()
                        .incCount();
            }

        }
    }

    private HashMap<Object, Action> getActions() {
        // Legt die Actions der Komponente in einem HashMap ab
        this.actions = new HashMap<Object, Action>();

        AbstractAction myAction = new ZerlegeAction("Splitten");
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("ZoomIn") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                final CElement cAct = JMathComponent.this.getCActive();
                if (cAct != null) {
                    JMathComponent.this.removeCActivity();
                    JMathComponent.this.setCActive(JMathComponent.this
                            .getCActive().tryToSelectFirstChild());
                    JMathComponent.this.getCActive().setCActiveProperty();
                    JMathComponent.this.modifyDocument();
                }

            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("ZoomOut") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                final CElement cAct = JMathComponent.this.getCActive();
                if (cAct != null) {
                    JMathComponent.this.removeCActivity();
                    JMathComponent.this.setCActive(JMathComponent.this
                            .getCActive().tryToSelectParent());
                    JMathComponent.this.getCActive().setCActiveProperty();
                    JMathComponent.this.modifyDocument();
                }

            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("GeheWeiter") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                final CElement cAct = JMathComponent.this.getCActive();
                if (cAct != null) {
                    JMathComponent.this.removeCActivity();
                    JMathComponent.this.setCActive(JMathComponent.this
                            .getCActive().tryToSelectRight());
                    JMathComponent.this.getCActive().setCActiveProperty();
                    JMathComponent.this.modifyDocument();
                }

            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("Selection+") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                final CElement cAct = JMathComponent.this.getCActive();
                if (cAct != null) {
                    final CElement lastC = JMathComponent.this.activeC
                            .get(JMathComponent.this.activeC.size() - 1);
                    if (lastC.hasNextC()
                            && lastC.hasParent()
                            && ((lastC.getParent() instanceof CTimesRow) || (lastC
                                    .getParent() instanceof CPlusRow))) {
                        final CElement nextC = lastC.getNextSibling();
                        nextC.setCLastProperty();
                        JMathComponent.this.activeC.add(nextC);
                        JMathComponent.this.modifyDocument();
                    }
                }

            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("Selection-") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                final CElement cAct = JMathComponent.this.getCActive();
                if (cAct != null) {
                    final CElement lastC = JMathComponent.this.activeC
                            .get(JMathComponent.this.activeC.size() - 1);
                    if (JMathComponent.this.activeC.size() > 1) {
                        lastC.removeCLastProperty();
                        JMathComponent.this.activeC.remove(lastC);
                        JMathComponent.this.modifyDocument();
                    }
                }

            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("GeheZurueck") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                final CElement cAct = JMathComponent.this.getCActive();
                if (cAct != null) {
                    JMathComponent.this.removeCActivity();
                    JMathComponent.this.setCActive(JMathComponent.this
                            .getCActive().tryToSelectLeft());
                    JMathComponent.this.getCActive().setCActiveProperty();
                    JMathComponent.this.modifyDocument();
                }

            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("BewegeRechts") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                final CElement cAct = JMathComponent.this.getCActive();
                final JMathComponent myComp = JMathComponent.this;
                if (cAct != null) {
                    myComp.addToUndo();
                    myComp.removeCActivity();
                    if (myComp.getUI().getBestLine() != null) {
                        final int i = myComp.getUI().getBestLine().getDist();
                        myComp
                                .setCActive(cAct.getParent().moveRight(i,
                                        cAct));
                    } else {
                        myComp.setCActive(cAct.getParent().moveRight(cAct));
                    }
                    myComp.getCActive().setCActiveProperty();
                    myComp.modifyDocument();
                }

            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("BewegeLinks") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                final CElement cAct = JMathComponent.this.getCActive();
                final JMathComponent myComp = JMathComponent.this;
                if (cAct != null) {
                    myComp.addToUndo();
                    myComp.removeCActivity();
                    if (myComp.getUI().getBestLine() != null) {
                        final int i = myComp.getUI().getBestLine().getDist();
                        System.out.println("Distance: " + i);
                        myComp.setCActive(cAct.getParent().moveLeft(i, cAct));
                    } else {
                        myComp.setCActive(cAct.getParent().moveLeft(cAct));
                    }
                    myComp.getCActive().setCActiveProperty();
                    myComp.modifyDocument();
                }
            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("Aendern") {
            private static final long serialVersionUID = 20090406L;

            public void actionPerformed(final ActionEvent ae) {
                final JMathComponent myComp = JMathComponent.this;
                if (myComp.getCActive() != null) {
                    myComp.addToUndo();
                    myComp.setCActive(AlterHandler.getInstance().change(
                            JMathComponent.this.activeC,
                            ae.getActionCommand()));
                    myComp.getCActive().setCActiveProperty();
                    myComp.modifyDocument();
                    ViewerFactory.getInst().getMathFrame().getCounter()
                            .incCount();
                }
            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("Verbinden") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                final CElement cAct = JMathComponent.this.getCActive();
                final JMathComponent myComp = JMathComponent.this;
                if (cAct != null && cAct.getParent() != null) {
                    myComp.addToUndo();
                    myComp.removeCActivity();
                    myComp.setCActive(cAct.getParent().combineRight(cAct));
                    myComp.getCActive().setCActiveProperty();
                    myComp.modifyDocument();
                    ViewerFactory.getInst().getMathFrame().getCounter()
                            .incCount();
                }
            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("Rausziehen") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                final CElement cAct = JMathComponent.this.getCActive();
                final JMathComponent myComp = JMathComponent.this;
                if (cAct != null && cAct.getParent() != null) {
                    myComp.addToUndo();
                    myComp.setCActive(myComp.getCActive().getParent()
                            .extract(myComp.activeC));
                    myComp.getCActive().setCActiveProperty();
                    myComp.modifyDocument();
                    ViewerFactory.getInst().getMathFrame().getCounter()
                            .incCount();
                }
            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("Klammere") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                final JMathComponent myComp = JMathComponent.this;
                if (myComp.getCActive() != null) {
                    myComp.addToUndo();
                    System.out.println("Fencing in JMathComponent");
                    myComp.setCActive(myComp.getCActive().getParent().fence(
                            myComp.activeC));
                    myComp.getCActive().setCActiveProperty();
                    myComp.modifyDocument();
                    ViewerFactory.getInst().getMathFrame().getCounter()
                            .incCount();
                }
            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("Entklammere") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                final CElement cAct = JMathComponent.this.getCActive();
                if (cAct != null) {
                    final JMathComponent myComp = JMathComponent.this;
                    myComp.addToUndo();
                    myComp.removeCActivity();
                    myComp.setCActive(cAct.getParent().defence(cAct));
                    myComp.getCActive().setCActiveProperty();
                    myComp.modifyDocument();
                    ViewerFactory.getInst().getMathFrame().getCounter()
                            .incCount();
                }
            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("Meins") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                JMathComponent.this.resetDoc();
                System.out.println(ViewerFactory.getInst().getMathFrame()
                        .getStateTransfer().getResult());
            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("Undo") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                final JMathComponent myComp = JMathComponent.this;
                if (myComp.archiv.canUndo()) {
                    String now = MathMLSerializer.serializeDocument(myComp
                            .getDocument(), false, false);
                    now = JMathComponentHelper.cleanString(now);
                    final String old = myComp.archiv.undo(now);
                    myComp.setContent(old);
                    myComp.modifyDocument();
                }
            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("Redo") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                final JMathComponent myComp = JMathComponent.this;
                if (myComp.archiv.canRedo()) {
                    String now = MathMLSerializer.serializeDocument(myComp
                            .getDocument(), false, false);
                    now = JMathComponentHelper.cleanString(now);
                    final String newer = myComp.archiv.redo(now);
                    myComp.setContent(newer);
                    myComp.modifyDocument();
                }
            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("Vergrößern") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                JMathComponent.this.setParameter(Parameter.MATHSIZE,
                        JMathComponent.this.getFontSize() * 1.25f);
            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);

        myAction = new AbstractAction("Verkleinern") {
            private static final long serialVersionUID = 20081230L;

            public void actionPerformed(final ActionEvent ae) {
                JMathComponent.this.setParameter(Parameter.MATHSIZE,
                        JMathComponent.this.getFontSize() * 0.8f);
            }
        };
        this.actions.put(myAction.getValue(Action.NAME), myAction);
        return this.actions;
    }

    public Action getActionByName(final String name) {
        return this.getActions().get(name);
    }

    private void addToUndo() {
        final String s = MathMLSerializer.serializeDocument(this
                .getDocument(), false, false);
        this.archiv.addEdit(JMathComponentHelper.cleanString(s));
    }

    private void resetDoc() {
        String redo = MathMLSerializer.serializeDocument(this.getDocument(),
                false, false);
        redo = JMathComponentHelper.cleanString(redo);
        this.setContent(redo);
        this.modifyDocument();
    }

    public void setDocument(final Document doc) {
        DOMElementMap.getInstance().getCElement.clear();
        // Zuordnung zwischen Element und CElement löschen
        this.document = doc;
        JMathElementHandler.parseDom(this.document.getFirstChild());
        // org.w3c.dom-Doc mit Anmerkungen über den RowTyp versehen
        EElementHelper.setDots(this.document.getFirstChild());
        // invisible und visible Dots richtig setzen in org.w3c.dom
        CElementHelper.buildCFromEPraefixAdjust(
                this.document.getFirstChild(), CRolle.DOCUMENTCHILD, false);
        // CTree aufbauen und activeC setzen
        this.activeC.clear();
        this.activeC.add(DOMElementMap.getInstance().getCElement
                .get(this.document.getFirstChild()));
        this.firePropertyChange("documentNew", null, doc);
        // rearrangiere EuclidVisuals
        this.reval();
        // revalidate, repaint
    }

    public void modifyDocument() {
        JMathElementHandler.parseDom(this.getDocument().getFirstChild());
        EElementHelper.setDots(this.getDocument().getFirstChild());
        this.firePropertyChange("documentChange", null, this.getDocument());
        ViewerFactory.getInst().getTreeViewDialog().update();
        this.reval();
    }

    // org.w3c.dom.Node -> String
    public String getContent() {
        return MathMLSerializer.serializeDocument(this.getDocument(), false,
                false);
    }

    // getter des org.w3c.dom.Node und weitere getter
    public Node getDocument() {
        return this.document;
    }

    @Override
    public Color getForeground() {
        return (Color) this.getParameters().getParameter(Parameter.MATHCOLOR);
    }

    public float getFontSize() {
        return (Float) this.getParameters().getParameter(Parameter.MATHSIZE);
    }

    public int getHorizontalAlignment() {
        return this.horizontalAlignment;
    }

    @Override
    public Dimension getPreferredSize() {
        final Dimension m = this.getMinimumSize();
        return new Dimension(m.width + 100, m.height + 100);
    }

    public MathComponentUI getUI() {
        return (MathComponentUI) this.ui;
    }

    @Override
    public String getUIClassID() {
        return JMathComponent.uiClassId;
    }

    public int getVerticalAlignment() {
        return this.verticalAlignment;
    }

    @Override
    public void setBackground(final Color c) {
        super.setBackground(c);
        this.reval();
    }

    public void setNewContent(final String contentString) {
        this.archiv.discardAllEdits();
        this.setContent(contentString);
    }

    // wird vom MathFrame aufgerufen
    private void setContent(final String contentString) {
        try {
            final String s = JMathElementHandler
                    .testMathMLString(contentString);
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
            final Object oldValue = this.getParameters().getParameter(key);
            this.getParameters().setParameter(key, entry.getValue());
            this.firePropertyChange(key.name(), oldValue, this
                    .getParameters().getParameter(key));
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

    public void handleEvent(final Event ev) {
        System.out.println("Event handled");
        // if (ev.getType().equals(MutationEventImpl.DOM_NODE_INSERTED)) {
        // String redo = MathMLSerializer.serializeDocument(
        // JMathComponent.this.getDocument(), false, false);
        // redo = JMathComponentHelper.cleanString(redo);
        // JMathComponent.this.setContent(redo);
        // JMathComponent.this.modifyDocument();
        // }
    }

    /** {@inheritDoc} */
    @Override
    public void updateUI() {
        if (UIManager.get(this.getUIClassID()) == null) {
            try {
                this
                        .setUI((MathComponentUI) JMathComponent.mathComponentUIClass
                                .newInstance());
            } catch (final InstantiationException e) {
                System.out.println(e.getMessage());
            } catch (final IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        } else {
            this.setUI(UIManager.getUI(this));
        }
    }

    static {
        Class<?> uiClass;
        String id;
        try {
            uiClass = Thread.currentThread().getContextClassLoader()
                    .loadClass("cViewer.MathComponentUI16");
            id = "MathComponentUI16";
        } catch (final ClassNotFoundException t) {
            uiClass = MathComponentUI.class;
            id = "MathComponentUI";
        }
        JMathComponent.uiClassId = id;
        JMathComponent.mathComponentUIClass = uiClass;
    }
}
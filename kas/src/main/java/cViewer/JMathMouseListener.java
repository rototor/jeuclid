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

package cViewer;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.MouseInputAdapter;

import org.w3c.dom.Node;

import cTree.CElement;
import cTree.CType;
import cTree.adapter.C_Changer;
import cTree.adapter.DOMElementMap;
import cTree.cAlter.AlterHandler;
import cViewer.MathComponentUI.MyLine;

public class JMathMouseListener extends MouseInputAdapter {
    private final JMathComponent mathComponent;

    private int x0;

    private int y0;

    private final JTextField jTextField;

    public JMathMouseListener(final JMathComponent jm, final JTextField s) {
        this.mathComponent = jm;
        this.jTextField = s;
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        this.x0 = e.getX();
        this.y0 = e.getY();
        this.getUI().setLines();
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        // nach rechts ziehen: Zusammenfassen
        if (e.getX() > this.x0 + 10) {
            if ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) {
                this.mathComponent.getActionByName("Verbinden")
                        .actionPerformed(null);
            } else {
                this.mathComponent.getActionByName("BewegeRechts")
                        .actionPerformed(null);
            }
            // x0=e.getX();
            // nach links ziehen: Aufspalten oder Extrahieren
        } else if (e.getX() < this.x0 - 10) {
            if ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) {
                if (this.jTextField.getText() == null
                        || "".equals(this.jTextField.getText())) {
                    this.mathComponent.getActionByName("Rausziehen")
                            .actionPerformed(null);
                } else {
                    this.mathComponent.getActionByName("Splitten")
                            .actionPerformed(null);
                }
            } else {
                this.mathComponent.getActionByName("BewegeLinks")
                        .actionPerformed(null);
            }
            // nach unten ziehen aendern
        } else if (e.getY() > this.y0 + 10) {
            this.mathComponent.getActionByName("Selection+").actionPerformed(
                    null);
        } else if (e.getY() < this.y0 - 10) {
            this.mathComponent.getActionByName("Selection-").actionPerformed(
                    null);
        }
        this.getUI().getLines().clear();
        this.getUI().setBestLine(null);
    }

    public void showMenu(final MouseEvent evt) {
        final HashMap<String, C_Changer> options = AlterHandler.getInstance()
                .getOptions(this.mathComponent.activeC);
        final JPopupMenu menu = new JPopupMenu();
        JMenuItem item = new JMenuItem(this.mathComponent
                .getActionByName("Aendern"));
        for (final String s : options.keySet()) {
            item = new JMenuItem(this.mathComponent
                    .getActionByName("Aendern"));
            item.setText(s);
            menu.add(item);
        }
        menu.show(this.mathComponent, evt.getX(), evt.getY());
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        this.mathComponent.requestFocusInWindow();
    }

    @Override
    public void mouseExited(final MouseEvent e) {
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        final ArrayList<MyLine> lines = this.getUI().getLines();
        if (!lines.isEmpty()) {
            MyLine bestLine = lines.get(0);
            double bestDist = Math.abs(bestLine.getLine().getX1() - e.getX());
            for (final MyLine line : lines) {
                final double newDist = Math.abs(line.getLine().getX1()
                        - e.getX());
                if (newDist < bestDist) {
                    bestLine = line;
                    bestDist = newDist;
                }
            }
            this.getUI().setBestLine(bestLine);
        }
        this.mathComponent.repaint();
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        final CElement cAct = this.mathComponent.getCActive();
        // Rechtsklick: Aendern
        if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
            if (this.mathComponent.getCActive() != null
                    && this.mathComponent.getCActive().getCType() != CType.MATH) {
                this.showMenu(e);
                this.mathComponent.modifyDocument();
            }
            // Herauszoomen
        } else if ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) {
            if (this.mathComponent.getCActive() != null
                    && this.mathComponent.getCActive().getCType() != CType.MATH) {
                this.mathComponent.getActionByName("ZoomOut")
                        .actionPerformed(null);
                // this.getUI().getLines().clear();
                this.mathComponent.repaint();
            }
            // Parent zeigen
            // } else if ((e.getModifiers() & InputEvent.CTRL_MASK) ==
            // InputEvent.CTRL_MASK) {
            // if (this.mathComponent.getCActive() != null
            // && this.mathComponent.getCActive().hasParent()) {
            // // this.getUI().getLines().clear();
            // // this.getUI().setRect(this.getUI().getRect(newE));
            // this.mathComponent.repaint();
            // }
            // Auswählen
        } else {
            final Node el = this.getUI().getNodeFromView(e.getX(), e.getY());
            final CElement newE = DOMElementMap.getInstance().getCElement
                    .get(el);

            // this.getUI().getLines().clear();
            this.mathComponent.repaint();
            this.mathComponent.setCActive(cTree.CNavHelper.chooseElement(
                    cAct, newE));
            this.mathComponent.clearCButFirst();
            this.mathComponent.modifyDocument();
        }
    }

    private MathComponentUI getUI() {
        return this.mathComponent.getUI();
    }

}

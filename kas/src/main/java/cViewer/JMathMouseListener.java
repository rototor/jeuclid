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
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import cTree.CElement;
import cTree.CType;
import cTree.cAlter.AlterHandler;

public class JMathMouseListener implements MouseListener {
    private final JMathComponent mathComponent;

    private int x0;

    private int y0;

    private final JTextField jTextField;

    public JMathMouseListener(final JMathComponent jm, final JTextField s) {
        this.mathComponent = jm;
        this.jTextField = s;
    }

    public void mousePressed(final MouseEvent e) {
        this.x0 = e.getX();
        this.y0 = e.getY();
    }

    public void mouseMoved(final MouseEvent e) {
    }

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
                    this.mathComponent.getActionByName("Zerlegen")
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
    }

    public void showMenu(final MouseEvent evt) {
        System.out.println("Showing Menu");
        final ArrayList<String> strings = AlterHandler.getInstance()
                .getOptions(this.mathComponent.activeC);
        final JPopupMenu menu = new JPopupMenu();
        JMenuItem item = new JMenuItem(this.mathComponent
                .getActionByName("Aendern"));
        item.setText("Umwandlungen:");
        item.setEnabled(false);
        menu.add(item);
        for (final String s : strings) {
            item = new JMenuItem(this.mathComponent
                    .getActionByName("Aendern"));
            item.setText(s);
            menu.add(item);
        }
        menu.show(this.mathComponent, evt.getX(), evt.getY());
    }

    public void mouseEntered(final MouseEvent e) {
        this.mathComponent.requestFocusInWindow();
    }

    public void mouseExited(final MouseEvent e) {
    }

    public void mouseDragged(final MouseEvent e) {
    }

    public void mouseClicked(final MouseEvent e) {
        final CElement cAct = this.mathComponent.getCActive();
        // Rechtsklick: Aendern
        if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
            if (this.mathComponent.getCActive() != null
                    && this.mathComponent.getCActive().getCType() != CType.MATH) {
                System.out.println("Was"
                        + this.mathComponent.getCActive().getCType());
                this.showMenu(e);
            }
            // Herauszoomen
        } else if ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) {
            if (this.mathComponent.getCActive() != null
                    && this.mathComponent.getCActive().getCType() != CType.MATH) {
                this.mathComponent.getActionByName("ZoomOut")
                        .actionPerformed(null);
            }
            // Auswählen
        } else {
            final CElement newE = cTree.adapter.DOMElementMap.getInstance().getCElement
                    .get(this.mathComponent.getUI().getNodeFromView(e.getX(),
                            e.getY()));
            this.mathComponent.setCActive(cTree.CNavHelper.chooseElement(
                    cAct, newE));
            this.mathComponent.clearCButFirst();
            this.mathComponent.modifyDocument();
        }
    }
}

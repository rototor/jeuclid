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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class JMathKeyListener implements KeyListener {
    private final JMathComponent mathComponent;

    private final HashMap<String, String> myInputMap;

    public JMathKeyListener(final JMathComponent jm) {
        this.mathComponent = jm;
        this.myInputMap = new HashMap<String, String>();
        this.myInputMap.put("z", "Zerlegen");
        this.myInputMap.put("y", "ZoomIn");
        this.myInputMap.put("e", "Entklammere");
        this.myInputMap.put("w", "ZoomOut");
        this.myInputMap.put("k", "Klammere");
        this.myInputMap.put("a", "GeheZurueck");
        this.myInputMap.put("A", "BewegeLinks");
        this.myInputMap.put("s", "GeheWeiter");
        this.myInputMap.put("S", "BewegeRechts");
        this.myInputMap.put("c", "Aendern");
        this.myInputMap.put("v", "Verbinden");
        this.myInputMap.put("V", "Rausziehen");
        this.myInputMap.put("t", "Zerlegen");
        this.myInputMap.put("+", "Vergrößern");
        this.myInputMap.put("-", "Verkleinern");
        this.myInputMap.put("n", "Selection-");
        this.myInputMap.put("m", "Selection+");
        this.myInputMap.put("z", "Undo");
        this.myInputMap.put("u", "Undo");
    }

    public void keyTyped(final KeyEvent e) {
        this.mathComponent.requestFocusInWindow();
        final String actionName = this.myInputMap.get(String.valueOf(e
                .getKeyChar()));
        if (actionName != null) {
            this.mathComponent.getActionByName(
                    this.myInputMap.get(String.valueOf(e.getKeyChar())))
                    .actionPerformed(null);
        }
    }

    public void keyPressed(final KeyEvent e) {
    }

    public void keyReleased(final KeyEvent e) {
    }

}

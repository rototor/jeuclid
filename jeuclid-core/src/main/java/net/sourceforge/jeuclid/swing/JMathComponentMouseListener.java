/*
 * Copyright 2002 - 2010 JEuclid, http://jeuclid.sf.net
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

/* $Id $ */

package net.sourceforge.jeuclid.swing;

import net.sourceforge.jeuclid.layout.JEuclidView.NodeRect;
import org.w3c.dom.Node;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 * A simple mouse listener for Graphics&lt;=&gt;Text association.
 *
 * @version $Revision: $
 */
public class JMathComponentMouseListener implements MouseListener {

    /**
     * math component instance.
     */
    private final JMathComponent mathComponent;

    /**
     * standard constructor.
     *
     * @param mathComponentInstance
     *            math component instance
     */
    public JMathComponentMouseListener(
            final JMathComponent mathComponentInstance) {
        this.mathComponent = mathComponentInstance;
        assert this.mathComponent.getCursorListener() != null : "Cursorlistener is required by JMathComponentMouseListener";
    }

    /**
     * mouse click event handler.
     *
     * @param e
     *            mouse event
     */
    public final void mouseClicked(final MouseEvent e) {
        final MathComponentUI ui = this.mathComponent.getUI();
        final List<NodeRect> rectList = ui.getNodesAt(this.mathComponent, e.getX(), e.getY());
        if (rectList != null && rectList.size() > 0) {
            final Node lastNode = rectList.get(rectList.size() - 1).getNode();
            final CursorListener cursorListener = this.mathComponent.getCursorListener();
            if (cursorListener != null) {
                cursorListener.updateCursorPosition(lastNode);
            }
        }
    }

    /**
     * mouse pressed event (unused).
     *
     * @param e
     *            mouse event
     */
    public final void mousePressed(final MouseEvent e) {
    }

    /**
     * mouse pressed event (unused).
     *
     * @param e
     *            mouse event
     */
    public final void mouseReleased(final MouseEvent e) {
    }

    /**
     * mouse pressed event (unused).
     *
     * @param e
     *            mouse event
     */
    public final void mouseEntered(final MouseEvent e) {
    }

    /**
     * mouse pressed event (unused).
     *
     * @param e
     *            mouse event
     */
    public final void mouseExited(final MouseEvent e) {
    }
}

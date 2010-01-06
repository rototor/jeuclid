/*
 *  Copyright 2009 Mario.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package net.sourceforge.jeuclid.swing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.List;
import javax.swing.JOptionPane;
import net.sourceforge.jeuclid.layout.JEuclidView.NodeRect;


public class JMathComponentMouseListener implements MouseListener {

    private JMathComponent mathComponent;
    public JMathComponentMouseListener(JMathComponent mathComponent) {
        this.mathComponent = mathComponent;
    }

    public void mouseClicked(MouseEvent e) {
        JOptionPane.showMessageDialog(this.mathComponent, "test");
        MathComponentUI ui = mathComponent.getUI();
        List<NodeRect> rectList = ui.getNodesAt(e.getX(), e.getY());
        
        if(rectList != null && rectList.size() > 0) {
            NodeRect lastRect = rectList.get(rectList.size()-1);
            Rectangle2D r = lastRect.getRect();
            drawCursor(r.getX()+r.getWidth(),r.getY(),r.getHeight());
        }
    }

    private void drawCursor(final double x, final double y, final double height)
    {
        MathComponentUI ui = this.mathComponent.getUI();
        MathComponentUI.Cursor cursor = ui.getCursor();
        if(cursor == null)
            cursor = new MathComponentUI.Cursor();
        cursor.setX(x);
        cursor.setY(y);
        cursor.setHeight(height);
        ui.setCursor(cursor);
    }

    public void mousePressed(MouseEvent e) {
        
    }

    public void mouseReleased(MouseEvent e) {
        
    }

    public void mouseEntered(MouseEvent e) {
        
    }

    public void mouseExited(MouseEvent e) {
    }
    

}

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

public class ViewerFactory {

    private volatile static ViewerFactory uniqueInstance;

    private JMathComponent math;

    private MathFrame frame;

    private MyTreeViewDialog treeViewDialog;

    private ViewerFactory() {
        // Empty on purpose
    }

    /**
     * Returns the SingletonObject of the Factory.
     * 
     * @return
     */
    public static ViewerFactory getInst() {
        if (ViewerFactory.uniqueInstance == null) {
            synchronized (ViewerFactory.class) {
                if (ViewerFactory.uniqueInstance == null) {
                    ViewerFactory.uniqueInstance = new ViewerFactory();
                }
            }
        }
        return ViewerFactory.uniqueInstance;
    }

    /**
     * Returns the Panel where you do the changes.
     * 
     * @return
     */
    public JMathComponent getMathComponent() {
        if (this.math == null) {
            this.math = new JMathComponent();
        }
        return this.math;
    }

    /**
     * Returns the Frame.
     * 
     * @return
     */

    public MathFrame getMathFrame() {
        if (this.frame == null) {
            this.frame = new MathFrame();
        }
        return this.frame;
    }

    public MyTreeViewDialog getTreeViewDialog() {
        if (this.treeViewDialog == null) {
            this.treeViewDialog = new MyTreeViewDialog();
        }
        return this.treeViewDialog;
    }

    /**
     * Single-Use Dialog which displays an Overview of Commands and their
     * access.
     * 
     */
    public MyHowToDialog getHowToDialog() {
        final MyHowToDialog dialog = new MyHowToDialog();
        dialog.pack();
        dialog.setLocationRelativeTo(this.getMathComponent());
        dialog.setVisible(true);
        return dialog;
    }

    /**
     * Single-Use Dialog which displays an CopyrightMessage.
     * 
     */
    public MyAboutDialog getAboutDialog() {
        final MyAboutDialog dialog = new MyAboutDialog();
        dialog.pack();
        dialog.setLocationRelativeTo(this.getMathComponent());
        dialog.setVisible(true);
        return dialog;
    }

    /**
     * Single-Use Dialog which displays a TextField. The String text is the
     * text, the user should typ into the TextField.
     * 
     * @param text
     *            String to be typed into the TextField
     * @return the Dialog
     */
    public MyInputDialog getInputDialog(final String text) {
        final MyInputDialog dialog = new MyInputDialog(text);
        dialog.pack();
        dialog.setLocationRelativeTo(this.getMathComponent());
        dialog.setVisible(true);
        return dialog;
    }

    /**
     * Single-Use Dialog which displays a ComboDialog. The Options are set by
     * the TransferObject, the chosenOption is the resultString of the
     * TransferObject.
     * 
     * @param o
     *            cViewer.TransferObject
     * @return the Dialog
     */
    public MyComboDialog getComboDialog(final TransferObject o) {
        final MyComboDialog dialog = new MyComboDialog(o);
        dialog.pack();
        dialog.setLocationRelativeTo(this.getMathComponent());
        dialog.setVisible(true);
        return dialog;
    }

}

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

package cTree.cExtract;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import cTree.CElement;
import cTree.CMinTerm;
import cTree.CTimesRow;
import cViewer.ViewerFactory;

public class CE_2StrichMinrow extends CE_1 {

    CE_1 extracter;

    @Override
    protected CElement createExtraction(final CElement parent,
            final ArrayList<CElement> selection, final CElement defElement) {
        return this.extracter.createExtraction(parent, selection, defElement);
    }

    private boolean canExtractOne(final CElement cEl) {
        System.out.println("CEl " + cEl.getText());
        return (cEl instanceof CTimesRow)
                || (cEl instanceof CMinTerm && ((CMinTerm) cEl).getValue() instanceof CTimesRow);
    }

    @Override
    protected boolean canExtract(final CElement parent,
            final ArrayList<CElement> selection) {
        for (final CElement cEl : selection) {
            if (!this.canExtractOne(cEl)) {
                return false;
            }
        }
        final Object[] possibilities = { "Vorzeichen", "erster Faktor",
                "letzter Faktor" };
        final String s = (String) JOptionPane.showInputDialog(ViewerFactory
                .getInst().getMathComponent(), "Wähle:", "Was herausziehen?",
                JOptionPane.QUESTION_MESSAGE, null, possibilities,
                "erster Faktor");
        if ("Vorzeichen".equals(s)) {
            this.extracter = new CE_2StrichPunktVZM();
        } else if ("erster Faktor".equals(s)) {
            this.extracter = new CE_2StrichPunkt1M();
        } else if ("letzter Faktor".equals(s)) {
            this.extracter = new CE_2StrichPunktLM();
        } else {
            return false;
        }
        return this.extracter.canExtract(parent, selection);
    }
}

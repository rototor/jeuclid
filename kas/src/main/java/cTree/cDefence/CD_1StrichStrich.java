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

package cTree.cDefence;

import java.util.ArrayList;

import org.w3c.dom.Element;

import cTree.CElement;
import cTree.CFences;
import cTree.CPlusRow;

public class CD_1StrichStrich extends CD_Base {

    @Override
    public CElement doIt(final CD_Event message) {
        System.out.println("Do the defence work strich strich");
        final CFences f = this.getFences();
        final CElement p = this.getParent();
        final CElement content = this.getInside();
        final boolean aussenMinus = (f.hasExtMinus());
        final Element op = (f.getExtPraefix() != null) ? (Element) f
                .getExtPraefix().cloneNode(true) : null;

        // Wir bilden drei Rows, bis zur Klammer, das Innere und nach der
        // Klammer
        final ArrayList<CElement> rows = new ArrayList<CElement>();
        rows.addAll(((CPlusRow) p).startTo(f)); // clone der ersten
        // Summanden
        rows.addAll(((CPlusRow) this.createInsertion(f, content, aussenMinus,
                op)).getMemberList());
        rows.addAll(((CPlusRow) p).endFrom(f)); // clone der letzten
        // Summanden
        System.out.println("--- Anzahl der Member " + rows.size());
        for (final CElement el : rows) {
            System.out.println("--- " + el.getCType());
        }
        // Die drei Rows werden zu einer verschmolzen
        final CPlusRow newParent = CPlusRow.createRow(rows);
        newParent.correctInternalPraefixesAndRolle();

        // Das Parent wird eingefügt
        p.getParent().replaceChild(newParent, p, true, true);
        return newParent.getFirstChild();
    }

    protected CElement createInsertion(final CElement fences,
            final CElement content, final boolean aussenMinus,
            final Element op) {
        System.out.println("Creating insertion strich strich");
        final CElement newChild = content.cloneCElement(true);
        if (aussenMinus) {
            System.out.println("min vor Row!!!");
            ((CPlusRow) newChild).toggleAllVZButFirst(false);
        }
        if (op != null) {
            newChild.getFirstChild().setExtPraefix(op);
        }
        return newChild;
    }

}

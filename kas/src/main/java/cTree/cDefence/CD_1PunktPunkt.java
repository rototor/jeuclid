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
import cTree.CTimesRow;

public class CD_1PunktPunkt extends CD_Base {

    @Override
    public CElement doIt(final CD_Event message) {
        // Situationen: a*(x*y)*b -> a*x*y*b
        System.out.println("Do the defence work strich strich");
        final CFences f = this.getFences(); // (x*y)
        final CElement p = this.getParent(); // a*(x*y)*b
        final CElement content = this.getInside(); // x*y
        if (this.canDo()) {
            final boolean aussenDiv = (f.hasExtDiv());
            final Element op = (f.getExtPraefix() != null) ? (Element) f
                    .getExtPraefix().cloneNode(true) : null;

            // Drei Rows bis zur Klammer, Klammerinneres, nach der Klammer
            final ArrayList<CElement> rows = new ArrayList<CElement>();
            rows.addAll(((CTimesRow) p).startTo(f));
            rows.addAll(((CTimesRow) this.createInsertion(f, content,
                    aussenDiv, op)).getMemberList());
            rows.addAll(((CTimesRow) p).endFrom(f));

            // Verschmelzen der Rows zu einer a*x*y*b
            final CTimesRow newParent = CTimesRow.createRow(rows);
            newParent.correctInternalPraefixesAndRolle();

            // Das Parent wird eingefügt
            final CElement gParent = p.getParent();
            if (gParent instanceof CFences) {
                final CElement ggParent = gParent.getParent();
                System.out.println(gParent.toString());
                System.out.println(ggParent.toString());

                final CElement newF = CFences.createFenced(newParent);
                return ggParent.replaceChild(newF, gParent, true, true);
            } else {
                return gParent.replaceChild(newParent, p, true, true);
            }
        } else {
            return f;
        }

    }

    protected CElement createInsertion(final CElement fences,
            final CElement content, final boolean aussenDiv, final Element op) {
        System.out.println("Defence punkt punkt");
        final CElement newChild = content.cloneCElement(true);
        if (aussenDiv) {
            System.out.println("div vor Row!!!");
            ((CTimesRow) newChild).toggleAllVZButFirst(false);
        }
        if (op != null) {
            newChild.getFirstChild().setExtPraefix(op);
        }
        return newChild;
    }

}

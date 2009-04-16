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
import cTree.CTimesRow;

public class CD_1PunktPunkt extends CD_1 {

    @Override
    public CElement defence(final CElement parent, final CElement fences,
            final CElement content) {
        System.out.println("Do the defence work punkt punkt");
        final boolean aussenDiv = (fences.hasExtDiv());
        final Element op = (fences.getExtPraefix() != null) ? (Element) fences
                .getExtPraefix().cloneNode(true)
                : null;

        // Drei Rows bis zur Klammer, Klammerinneres, nach der Klammer
        final ArrayList<CElement> rows = new ArrayList<CElement>();
        rows.addAll(((CTimesRow) parent).startTo(fences));
        rows.addAll(((CTimesRow) this.createInsertion(fences, content,
                aussenDiv, op)).getMemberList());
        rows.addAll(((CTimesRow) parent).endFrom(fences));

        // Verschmelzen der Rows zu einer
        final CTimesRow newParent = CTimesRow.createRow(rows);
        newParent.correctInternalPraefixesAndRolle();

        // Das Parent wird eingefügt
        parent.getParent().replaceChild(newParent, parent, true, true);
        return newParent.getFirstChild();
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

    @Override
    protected boolean replaceP(final CElement parent, final CElement fences) {
        return true;
    }
}

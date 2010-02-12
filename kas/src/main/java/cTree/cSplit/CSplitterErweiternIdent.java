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

package cTree.cSplit;

import cTree.CElement;
import cTree.CFences;
import cTree.CFrac;
import cTree.CIdent;
import cTree.CTimesRow;
import cTree.adapter.C_Changer;
import cTree.cDefence.CD_Event;
import cTree.cDefence.DefHandler;

public class CSplitterErweiternIdent extends CSplitterBase {

    private CFrac oldFrac;

    private CElement oldNum;

    private final CD_Event oldNumMes = new CD_Event(false);

    private CElement oldDen;

    private final CD_Event oldDenMes = new CD_Event(false);

    private CElement newEl;

    private enum SplitTyp {
        E, NO
    };

    private SplitTyp splitTyp;

    public CSplitterErweiternIdent() {
        this.splitTyp = SplitTyp.NO;
    }

    @Override
    protected void init(final CS_Event event) {
        System.out.println("Init the Erw Ident split");
        final CElement cE1 = event.getFirst();
        final String op = event.getOperator();
        if (cE1 instanceof CFrac && op.length() == 1 && (op.charAt(0) <= 'z')
                && (op.charAt(0) >= 'a')) {
            System.out.println("Check ok");
            this.newEl = CIdent.createIdent(cE1.getElement(), op);
            this.oldFrac = (CFrac) cE1;
            this.oldNum = CFences.condCreateFenced(this.oldFrac.getZaehler()
                    .cloneCElement(true), this.oldNumMes);
            // evtl falsch ! Vorzeichen!
            this.oldDen = CFences.condCreateFenced(this.oldFrac.getNenner()
                    .cloneCElement(true), this.oldDenMes);
            // evtl falsch ! Vorzeichen!
            this.splitTyp = SplitTyp.E;
        } else {
            this.splitTyp = SplitTyp.NO;
        }
    }

    protected CElement condCleanOne(final CElement el, final boolean doIt) {
        final CD_Event e = new CD_Event(el);
        final C_Changer c = DefHandler.getInst().getChanger(e);
        if (doIt && c.canDo()) {
            return c.doIt(null);
        } else {
            return el;
        }
    }

    @Override
    public boolean canDo() {
        System.out.println("Check the erweitern CEl-Split");
        return this.splitTyp != SplitTyp.NO;
    }

    @Override
    public CElement split() {
        System.out.println("Do the Erweitern split");
        final CTimesRow newNum = CTimesRow.createRow(CTimesRow.createList(
                this.oldNum, this.newEl));
        // extra this.condCleanOne(newNum, this.oldNumMes.isMessage());
        newNum.correctInternalPraefixesAndRolle();
        final CTimesRow newDen = CTimesRow.createRow(CTimesRow.createList(
                this.oldDen, this.newEl));
        newDen.correctInternalPraefixesAndRolle();
        // extra this.condCleanOne(newDen, this.oldDenMes.isMessage());
        final CFrac newFrac = CFrac.createFraction(newNum, newDen);
        return newFrac;
    }

}

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
import cTree.CMessage;
import cTree.CTimesRow;
import cTree.cDefence.DefenceHandler;

public class CSplitterErweiternIdent extends CSplitter1 {

    private CFrac oldFrac;

    private CElement oldNum;

    private final CMessage oldNumMes = new CMessage(false);

    private CElement oldDen;

    private final CMessage oldDenMes = new CMessage(false);

    private CElement newEl;

    private enum SplitTyp {
        E, NO
    };

    private SplitTyp splitTyp;

    public CSplitterErweiternIdent() {
        this.splitTyp = SplitTyp.NO;
    }

    private void init(final CElement cE1, final String operator) {
        System.out.println("Init the Erw Ident split");

        if (cE1 instanceof CFrac && operator.length() == 1
                && (operator.charAt(0) <= 'z') && (operator.charAt(0) >= 'a')) {
            System.out.println("Check ok");
            this.newEl = CIdent.createIdent(cE1.getElement(), operator);

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

    protected void condCleanOne(final CElement el, final boolean doIt) {
        if (doIt
                && DefenceHandler.getInstance().canDefence(el.getParent(),
                        el, el.getFirstChild())) {
            DefenceHandler.getInstance().defence(el.getParent(), el,
                    el.getFirstChild());
        }
    }

    @Override
    public boolean check(final CElement cE1, final String operator) {
        System.out.println("Check the erweitern CEl-Split");
        this.init(cE1, operator);
        return this.splitTyp != SplitTyp.NO;
    }

    @Override
    public CElement split(final CElement parent, final CElement cE1,
            final String operator) {

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

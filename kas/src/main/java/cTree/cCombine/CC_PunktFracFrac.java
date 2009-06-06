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

package cTree.cCombine;

import cTree.CElement;
import cTree.CFences;
import cTree.CFrac;
import cTree.CTimesRow;
import cTree.cDefence.CD_Event;

public class CC_PunktFracFrac extends CC_Base {

    private CElement z1;

    private CElement n1;

    private CElement z2;

    private CElement n2;

    private final CD_Event z1_TryDefence = new CD_Event(false);

    private final CD_Event z2_TryDefence = new CD_Event(false);

    private final CD_Event n1_TryDefence = new CD_Event(false);

    private final CD_Event n2_TryDefence = new CD_Event(false);

    private boolean gleicheDiv;

    private CFrac newChild;

    @Override
    public boolean canDo() {
        return true;
    }

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, CD_Event cDEvent) {
        System.out.println("Multipliziere Brüche");
        final boolean zuerstDiv = cE1.hasExtDiv();
        final boolean dannDiv = cE2.hasExtDiv();
        this.gleicheDiv = zuerstDiv && dannDiv || !zuerstDiv && !dannDiv;
        final CFrac bruch1 = (CFrac) cE1;
        final CFrac bruch2 = (CFrac) cE2;
        this.z1 = CFences.condCreateFenced(bruch1.getZaehler().cloneCElement(
                false), this.z1_TryDefence);
        this.z2 = CFences.condCreateFenced(bruch2.getZaehler().cloneCElement(
                false), this.z2_TryDefence);
        this.n1 = CFences.condCreateFenced(bruch1.getNenner().cloneCElement(
                false), this.n1_TryDefence);
        this.n2 = CFences.condCreateFenced(bruch2.getNenner().cloneCElement(
                false), this.n2_TryDefence);
        if (this.gleicheDiv) {
            final CTimesRow zaehlerNeu = CTimesRow.createRow(CTimesRow
                    .createList(this.z1, this.z2));
            final CTimesRow nennerNeu = CTimesRow.createRow(CTimesRow
                    .createList(this.n1, this.n2));
            zaehlerNeu.correctInternalPraefixesAndRolle();
            nennerNeu.correctInternalPraefixesAndRolle();
            this.newChild = CFrac.createFraction(zaehlerNeu, nennerNeu);
        } else {
            final CTimesRow zaehlerNeu = CTimesRow.createRow(CTimesRow
                    .createList(this.z1, this.n2));
            final CTimesRow nennerNeu = CTimesRow.createRow(CTimesRow
                    .createList(this.n1, this.z2));
            zaehlerNeu.correctInternalPraefixesAndRolle();
            nennerNeu.correctInternalPraefixesAndRolle();
            this.newChild = CFrac.createFraction(zaehlerNeu, nennerNeu);
        }
        this.z1 = this.newChild.getZaehler().getFirstChild();
        this.z2 = this.z1.getNextSibling();
        this.n1 = this.newChild.getNenner().getFirstChild();
        this.n2 = this.n1.getNextSibling();
        return this.newChild;
    }

    @Override
    protected void clean() {
        this.condCleanOne(this.z2, this.z2_TryDefence.isDoDef());
        this.z1 = this.newChild.getZaehler().getFirstChild();
        this.condCleanOne(this.z1, this.z1_TryDefence.isDoDef());
        this.condCleanOne(this.n2, this.n2_TryDefence.isDoDef());
        this.n1 = this.newChild.getNenner().getFirstChild();
        this.condCleanOne(this.n1, this.n1_TryDefence.isDoDef());
    }
}

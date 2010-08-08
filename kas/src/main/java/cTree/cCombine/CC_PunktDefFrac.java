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

public class CC_PunktDefFrac extends CC_Base {

    private CElement z1;

    private CElement z2;

    private final CD_Event z1_TryDefence = new CD_Event(false);

    private final CD_Event z2_TryDefence = new CD_Event(false);

    private boolean gleicheDiv;

    private CFrac newChild;

    @Override
    public boolean canDo() {
        return true;
    }

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, CD_Event cDEvent) {
        System.out.println("Multipliziere Num Frac");
        final boolean zuerstDiv = cE1.hasExtDiv();
        final boolean dannDiv = cE2.hasExtDiv();
        this.gleicheDiv = zuerstDiv && dannDiv || !zuerstDiv && !dannDiv;
        final CFrac bruch2 = (CFrac) cE2;
        final CElement zaehlerAlt = bruch2.getZaehler();
        final CElement nennerAlt = bruch2.getNenner();
        if (this.gleicheDiv) {
            this.z2 = CFences.condCreateFenced(zaehlerAlt
                    .cloneCElement(false), this.z2_TryDefence);
            this.z1 = CFences.condCreateFenced(cE1.cloneCElement(false),
                    this.z1_TryDefence);
            final CTimesRow zaehlerNeu = CTimesRow.createRow(CTimesRow
                    .createList(this.z1, this.z2));
            this.z1 = zaehlerNeu.getFirstChild();
            this.z2 = this.z1.getNextSibling();
            zaehlerNeu.correctInternalPraefixesAndRolle();
            this.newChild = CFrac.createFraction(zaehlerNeu, nennerAlt);
        } else {
            this.z2 = CFences.condCreateFenced(
                    nennerAlt.cloneCElement(false), this.z2_TryDefence);
            this.z1 = CFences.condCreateFenced(cE1.cloneCElement(false),
                    this.z1_TryDefence);
            final CTimesRow zaehlerNeu = CTimesRow.createRow(CTimesRow
                    .createList(this.z1, this.z2));
            this.z1 = zaehlerNeu.getFirstChild();
            this.z2 = this.z1.getNextSibling();
            zaehlerNeu.correctInternalPraefixesAndRolle();
            this.newChild = CFrac.createFraction(zaehlerNeu, zaehlerAlt);
        }
        return this.newChild;
    }

    @Override
    protected void clean() {
        this.condCleanOne(this.z1, this.z1_TryDefence.isDoDef());
        this.condCleanOne(this.z2, this.z2_TryDefence.isDoDef());
    }

}

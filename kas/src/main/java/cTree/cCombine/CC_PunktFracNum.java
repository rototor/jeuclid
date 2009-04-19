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
import cTree.CMessage;
import cTree.CTimesRow;

public class CC_PunktFracNum extends CC_ {

    private CElement z1;

    private CElement n1;

    private CElement z2;

    private CElement n2;

    private final CMessage z1_TryDefence = new CMessage(false);

    private final CMessage z2_TryDefence = new CMessage(false);

    private final CMessage n1_TryDefence = new CMessage(false);

    private final CMessage n2_TryDefence = new CMessage(false);

    private boolean gleicheDiv;

    private CFrac newChild;

    @Override
    protected boolean canCombine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        return true;
    }

    @Override
    protected CElement createCombination(final CElement parent,
            final CElement cE1, final CElement cE2) {
        System.out.println("Multipliziere Brüche");
        final boolean zuerstDiv = cE1.hasExtDiv();
        final boolean dannDiv = cE2.hasExtDiv();
        this.gleicheDiv = zuerstDiv && dannDiv || !zuerstDiv && !dannDiv;
        final CFrac bruch1 = (CFrac) cE1;
        final CElement zaehlerAlt = bruch1.getZaehler();
        final CElement nennerAlt = bruch1.getNenner();
        if (this.gleicheDiv) {
            this.z1 = CFences.condCreateFenced(zaehlerAlt
                    .cloneCElement(false), this.z1_TryDefence);
            this.z2 = CFences.condCreateFenced(cE2.cloneCElement(false),
                    this.z2_TryDefence);
            final CTimesRow zaehlerNeu = CTimesRow.createRow(CTimesRow
                    .createList(this.z1, this.z2));
            this.z1 = zaehlerNeu.getFirstChild();
            this.z2 = this.z1.getNextSibling();
            zaehlerNeu.correctInternalPraefixesAndRolle();
            this.newChild = CFrac.createFraction(zaehlerNeu, nennerAlt);
        } else {
            this.n1 = CFences.condCreateFenced(
                    nennerAlt.cloneCElement(false), this.n1_TryDefence);
            this.n2 = CFences.condCreateFenced(cE2.cloneCElement(false),
                    this.n2_TryDefence);
            final CTimesRow nennerNeu = CTimesRow.createRow(CTimesRow
                    .createList(this.n1, this.n2));
            this.n1 = nennerNeu.getFirstChild();
            this.n2 = this.n1.getNextSibling();
            nennerNeu.correctInternalPraefixesAndRolle();
            this.newChild = CFrac.createFraction(zaehlerAlt, nennerNeu);
        }
        return this.newChild;
    }

    @Override
    protected void clean() {
        if (this.gleicheDiv) {
            this.condCleanOne(this.z1, this.z1_TryDefence.isMessage());
            this.condCleanOne(this.z2, this.z2_TryDefence.isMessage());
        } else {
            this.condCleanOne(this.n1, this.n1_TryDefence.isMessage());
            this.condCleanOne(this.n2, this.n2_TryDefence.isMessage());
        }
    }

}

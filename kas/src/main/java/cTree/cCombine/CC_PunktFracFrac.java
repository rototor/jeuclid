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
import cTree.cDefence.DefenceHandler;

public class CC_PunktFracFrac extends CC_ {

    private CFences z1;

    private CFences n1;

    private CFences z2;

    private CFences n2;

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
        final CFrac bruch2 = (CFrac) cE2;
        this.z1 = CFences.createFenced(bruch1.getZaehler().cloneCElement(
                false));
        this.n1 = CFences.createFenced(bruch1.getNenner()
                .cloneCElement(false));
        this.z2 = CFences.createFenced(bruch2.getZaehler().cloneCElement(
                false));
        this.n2 = CFences.createFenced(bruch2.getNenner()
                .cloneCElement(false));
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
        this.z1 = (CFences) this.newChild.getZaehler().getFirstChild();
        this.z2 = (CFences) this.z1.getNextSibling();
        System.out.println("*" + this.z2.getText());
        this.n1 = (CFences) this.newChild.getNenner().getFirstChild();
        this.n2 = (CFences) this.n1.getNextSibling();
        return this.newChild;
    }

    @Override
    protected void clean() {
        if (DefenceHandler.getInstance().canDefence(this.z2.getParent(),
                this.z2, this.z2.getFirstChild())) {
            DefenceHandler.getInstance().defence(this.z2.getParent(),
                    this.z2, this.z2.getFirstChild());
        }
        this.z1 = (CFences) this.newChild.getZaehler().getFirstChild();
        if (DefenceHandler.getInstance().canDefence(this.z1.getParent(),
                this.z1, this.z1.getFirstChild())) {
            DefenceHandler.getInstance().defence(this.z1.getParent(),
                    this.z1, this.z1.getFirstChild());
        }
        if (DefenceHandler.getInstance().canDefence(this.n2.getParent(),
                this.n2, this.n2.getFirstChild())) {
            DefenceHandler.getInstance().defence(this.n2.getParent(),
                    this.n2, this.n2.getFirstChild());
        }
        this.n1 = (CFences) this.newChild.getNenner().getFirstChild();
        if (DefenceHandler.getInstance().canDefence(this.n1.getParent(),
                this.n1, this.n1.getFirstChild())) {
            DefenceHandler.getInstance().defence(this.n1.getParent(),
                    this.n1, this.n1.getFirstChild());
        }
    }
}

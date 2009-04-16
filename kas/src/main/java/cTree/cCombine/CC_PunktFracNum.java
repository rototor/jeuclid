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

public class CC_PunktFracNum extends CC_ {

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
        final CElement zaehlerAlt = bruch1.getZaehler();
        final CElement nennerAlt = bruch1.getNenner();
        if (this.gleicheDiv) {
            this.z1 = CFences.createFenced(zaehlerAlt.cloneCElement(false));
            this.z2 = CFences.createFenced(cE2.cloneCElement(false));
            final CTimesRow zaehlerNeu = CTimesRow.createRow(CTimesRow
                    .createList(this.z1, this.z2));
            this.z1 = (CFences) zaehlerNeu.getFirstChild();
            this.z2 = (CFences) this.z1.getNextSibling();
            zaehlerNeu.correctInternalPraefixesAndRolle();
            this.newChild = CFrac.createFraction(zaehlerNeu, nennerAlt);
        } else {
            final CFences n1 = CFences.createFenced(nennerAlt
                    .cloneCElement(false));
            final CFences n2 = CFences.createFenced(cE2.cloneCElement(false));
            final CTimesRow nennerNeu = CTimesRow.createRow(CTimesRow
                    .createList(n1, n2));
            this.n1 = (CFences) nennerNeu.getFirstChild();
            this.n2 = (CFences) this.n1.getNextSibling();
            nennerNeu.correctInternalPraefixesAndRolle();
            this.newChild = CFrac.createFraction(zaehlerAlt, nennerNeu);
        }
        return this.newChild;
    }

    @Override
    protected void clean() {
        if (this.gleicheDiv) {
            System.out.println("---"
                    + this.z1.equals(this.newChild.getFirstChild()
                            .getFirstChild()));
            if (DefenceHandler.getInstance().canDefence(this.z1.getParent(),
                    this.z1, this.z1.getFirstChild())) {
                DefenceHandler.getInstance().defence(this.z1.getParent(),
                        this.z1, this.z1.getFirstChild());
            }
            if (DefenceHandler.getInstance().canDefence(this.z2.getParent(),
                    this.z2, this.z2.getFirstChild())) {
                DefenceHandler.getInstance().defence(this.z2.getParent(),
                        this.z2, this.z2.getFirstChild());
            }
        } else {
            if (DefenceHandler.getInstance().canDefence(this.n1.getParent(),
                    this.n1, this.n1.getFirstChild())) {
                DefenceHandler.getInstance().defence(this.n1.getParent(),
                        this.n1, this.n1.getFirstChild());
            }
            if (DefenceHandler.getInstance().canDefence(this.n2.getParent(),
                    this.n2, this.n2.getFirstChild())) {
                DefenceHandler.getInstance().defence(this.n2.getParent(),
                        this.n2, this.n2.getFirstChild());
            }
        }
    }
}

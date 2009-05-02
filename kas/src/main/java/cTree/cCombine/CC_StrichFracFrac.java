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
import cTree.CMinTerm;
import cTree.CPlusRow;
import cTree.CRolle;

public class CC_StrichFracFrac extends CC_ {

    private CElement z1;

    private CElement n1;

    private CElement z2;

    private final CMessage z1_TryDefence = new CMessage(false);

    private final CMessage z2_TryDefence = new CMessage(false);

    private final CMessage n1_TryDefence = new CMessage(false);

    private boolean gleicheMin;

    private CFrac newChild;

    @Override
    protected boolean canCombine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        if (cE1 instanceof CFrac && cE2 instanceof CFrac) {
            final CFrac b1 = (CFrac) cE1;
            this.z1 = b1.getZaehler().cloneCElement(false);
            this.n1 = b1.getNenner().cloneCElement(false);
            final CFrac b2 = (CFrac) cE2;
            this.z2 = b2.getZaehler().cloneCElement(false);
            if (this.n1.getText().equals(b2.getNenner().getText())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected CElement createCombination(final CElement parent,
            final CElement cE1, final CElement cE2) {
        System.out.println("Addieren Brüche");
        final boolean zuerstMin = cE1.hasExtMinus();
        final boolean dannMin = cE2.hasExtMinus();
        this.gleicheMin = zuerstMin && dannMin || !zuerstMin && !dannMin;

        this.z1 = CFences.condCreateFenced(this.z1, this.z1_TryDefence);
        this.z2 = CFences.condCreateFenced(this.z2, this.z2_TryDefence);
        this.n1 = CFences.condCreateFenced(this.n1, this.n1_TryDefence);
        if (cE1.getCRolle() == CRolle.NACHVZMINUS) {
            final CMinTerm cM = CMinTerm.createMinTerm(this.z1);
            final CPlusRow zaehlerNeu = CPlusRow.createRow(CPlusRow
                    .createList(cM, this.z2));
            if (dannMin) {
                zaehlerNeu.getFirstChild().getNextSibling().togglePlusMinus(
                        false);
            }
            zaehlerNeu.correctInternalPraefixesAndRolle();
            this.newChild = CFrac.createFraction(zaehlerNeu, this.n1);
        } else {
            if (this.gleicheMin) {
                final CPlusRow zaehlerNeu = CPlusRow.createRow(CPlusRow
                        .createList(this.z1, this.z2));
                zaehlerNeu.correctInternalPraefixesAndRolle();
                this.newChild = CFrac.createFraction(zaehlerNeu, this.n1);
            } else {
                final CPlusRow zaehlerNeu = CPlusRow.createRow(CPlusRow
                        .createList(this.z1, this.z2));
                zaehlerNeu.correctInternalPraefixesAndRolle();
                zaehlerNeu.getFirstChild().getNextSibling().togglePlusMinus(
                        false);
                this.newChild = CFrac.createFraction(zaehlerNeu, this.n1);
            }
            this.z1 = this.newChild.getZaehler().getFirstChild();
            this.z2 = this.z1.getNextSibling();
            this.n1 = this.newChild.getNenner().getFirstChild();
        }

        return this.newChild;
    }

    @Override
    protected void clean() {
        this.condCleanOne(this.z2, this.z2_TryDefence.isMessage());
        this.z1 = this.newChild.getZaehler().getFirstChild();
        this.condCleanOne(this.z1, this.z1_TryDefence.isMessage());
        this.n1 = this.newChild.getNenner().getFirstChild();
        this.condCleanOne(this.n1, this.n1_TryDefence.isMessage());
    }
}

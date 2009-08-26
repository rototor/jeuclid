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
import cTree.CNum;
import cTree.CPot;
import cTree.CTimesRow;

public class CSplitterTimesPot extends CSplitterBase {

    private int origB;

    private int origE;

    private int targetE;

    private int result;

    private boolean canSplit;

    public CSplitterTimesPot() {
        this.origB = 1;
        this.origE = 1;
        this.targetE = 1;
        this.result = 0;
        this.canSplit = false;
    }

    @Override
    protected void init(final CS_Event event) {
        System.out.println("Init the M Num split");
        final CElement cE1 = event.getFirst();
        final String op = event.getOperator();
        if (cE1 instanceof CPot) {
            final CPot cPot = (CPot) cE1;
            if (cPot.getBasis() instanceof CNum
                    && cPot.getExponent() instanceof CNum) {
                this.origB = ((CNum) cPot.getBasis()).getValue();
                this.origE = ((CNum) cPot.getExponent()).getValue();
                final int pos = op.indexOf("^");
                if (pos > 0 && pos < op.length()) {
                    final String vorher = op.substring(0, pos);
                    final String nachher = op.substring(pos + 1);
                    System.out.println("Zerlege " + pos + " " + vorher + " "
                            + nachher);
                    try {
                        final int targetB = Integer.parseInt(vorher);
                        this.targetE = Integer.parseInt(nachher);
                        if (targetB == this.origB
                                && this.origE > this.targetE) {
                            this.result = this.origE - this.targetE;
                            this.canSplit = true;
                        }
                    } catch (final NumberFormatException e) {
                    }
                }
            }
        }
    }

    @Override
    public boolean canDo() {
        System.out.println("Check the Mult Pot split");
        return this.canSplit;
    }

    @Override
    public CElement split() {
        System.out.println("Do the Times Pot split");
        final CElement cE1 = this.getEvent().getFirst();
        final CPot first = (CPot) cE1.cloneCElement(false);
        ((CNum) first.getExponent()).setValue(this.result);
        final CPot second = CPot.createPot(first.getBasis().cloneCElement(
                false), this.targetE);
        final CTimesRow newTR = CTimesRow.createRow(CTimesRow.createList(
                first, second));
        newTR.correctInternalPraefixesAndRolle();
        return newTR;
    }

}

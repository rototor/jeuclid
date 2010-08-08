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
import cTree.CTimesRow;

// zu komplex sollte mit getChanger in drei weitere Klassen gesplittet werden

public class CSplitterTimesNum extends CSplitterBase {

    private int nr1;

    private int nr2;

    private int result;

    private enum SplitTyp {
        M15M3, D, E, NO
    };

    private SplitTyp splitTyp;

    private CElement first;

    private CElement second;

    public CSplitterTimesNum() {
        this.nr1 = 1;
        this.nr2 = 1;
        this.result = 0;
        this.splitTyp = SplitTyp.NO;
    }

    @Override
    protected void init(final CS_Event event) {
        System.out.println("Init the M Num split");
        final CElement cE1 = event.getFirst();
        final String op = event.getOperator();
        if (cE1 instanceof CNum) {
            try {
                this.nr2 = Integer.parseInt(op);
                if (this.nr2 == 0) {
                    this.splitTyp = SplitTyp.NO;
                } else {
                    this.nr1 = ((CNum) cE1).getValue();
                    if (cE1.hasExtDiv()) {
                        // geht: :5 -> :(5*3)*3 -> :15*3
                        this.result = this.nr1 * this.nr2;
                        this.splitTyp = SplitTyp.D;
                    } else {
                        if (this.nr1 % this.nr2 == 0) {
                            // geht: 15 -> (15:3)*3 -> 5*3
                            this.result = this.nr1 / this.nr2;
                            this.splitTyp = SplitTyp.M15M3;
                        } else {
                            // geht nie:
                            this.splitTyp = SplitTyp.NO;
                        }
                    }
                }
            } catch (final NumberFormatException e) {
                // geht nicht, operator muss Zahl sein
                this.splitTyp = SplitTyp.NO;
            }
        } else {
            try {
                this.nr2 = Integer.parseInt(op);
                if (this.nr2 != 1) {
                    this.nr1 = 1;
                    this.nr2 = 1;
                    this.result = 0;
                    this.splitTyp = SplitTyp.NO;
                } else {
                    // geht: 5 -> 5*1
                    this.splitTyp = SplitTyp.E;
                }
            } catch (final NumberFormatException e) {
                // geht nicht, operator muss Zahl sein
                this.splitTyp = SplitTyp.NO;
            }
        }
    }

    @Override
    public boolean canDo() {
        System.out.println("Check the Mult Num split");
        return this.splitTyp != SplitTyp.NO;
    }

    @Override
    public CElement split() {
        final CElement parent = this.getEvent().getParent();
        final CElement cE1 = this.getEvent().getFirst();
        if (this.splitTyp == SplitTyp.E) {
            System.out.println("Do the Mult 1 split");
            this.first = cE1.cloneCElement(false);
            this.second = CNum.createNum(parent.getElement(), "1");
        } else {
            System.out.println("Do the Mult Num split");
            this.first = CNum
                    .createNum(parent.getElement(), "" + this.result);
            this.second = CNum.createNum(cE1.getElement(), "" + this.nr2);
            if (this.splitTyp == SplitTyp.M15M3) {
                this.second.setPraefix("*");
            } else {
                this.second.setPraefix(":");
            }
        }
        final CTimesRow newRow = CTimesRow.createRow(CTimesRow.createList(
                this.first, this.second));
        newRow.correctInternalPraefixesAndRolle();
        return newRow;

    }

}

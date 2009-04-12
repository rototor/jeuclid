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

public class CSplitterDiv extends CSplitter1 {

    private int nr1;

    private int nr2;

    private int result;

    private enum SplitTyp {
        D15D3, D3D15, M, NO
    };

    private SplitTyp splitTyp;

    public CSplitterDiv() {
        this.nr1 = 1;
        this.nr2 = 1;
        this.result = 0;
        this.splitTyp = SplitTyp.NO;
    }

    @Override
    public void init(final CElement cE1, final String operator) {
        System.out.println("Init the Div Num split");
        if (cE1 instanceof CNum) {
            try {
                this.nr2 = Integer.parseInt(operator);
                if (this.nr2 == 0) {
                    this.splitTyp = SplitTyp.NO;
                } else {
                    this.nr1 = ((CNum) cE1).getValue();
                    if (cE1.hasExtDiv()) {
                        if (this.nr1 % this.nr2 == 0) {
                            // geht: :15 -> :(15:3):3 -> :5:3
                            this.result = this.nr1 / this.nr2;
                            this.splitTyp = SplitTyp.D15D3;
                        } else if (this.nr2 % this.nr1 == 0) {
                            // geht: :3 -> *(15:3):15 -> *5:15
                            this.result = this.nr2 / this.nr1;
                            this.splitTyp = SplitTyp.D3D15;
                        } else {
                            // geht nie: :5 -> :(5:3):3 denn 5:3 nicht in int
                            this.splitTyp = SplitTyp.NO;
                        }
                    } else {
                        // geht immer: *5 -> *(5*3):3
                        this.result = this.nr1 * this.nr2;
                        this.splitTyp = SplitTyp.M;
                    }
                }
            } catch (final NumberFormatException e) {
                // geht nicht, operator muss Zahl sein
                this.splitTyp = SplitTyp.NO;
            }
        } else {
            this.nr1 = 1;
            this.nr2 = 1;
            this.result = 0;
            // split bisher nur f�r Zahlen
            this.splitTyp = SplitTyp.NO;
        }
    }

    @Override
    public boolean check(final CElement cE1, final String operator) {
        System.out.println("Check the Div Num split");
        this.init(cE1, operator);
        return this.splitTyp != SplitTyp.NO;
    }

    @Override
    public CElement split(final CElement parent, final CElement cE1,
            final String operator) {

        System.out.println("Do the Div Num split");
        if (this.splitTyp == SplitTyp.D3D15) {
            cE1.toggleTimesDiv(false);
        }
        final CNum first = CNum.createNum(parent.getElement(), ""
                + CSplitterDiv.this.result);
        final CNum second = CNum.createNum(cE1.getElement(), ""
                + CSplitterDiv.this.nr2);
        if (this.splitTyp == SplitTyp.D15D3) {
            second.setPraefix("*");
        } else {
            second.setPraefix(":");
        }
        final CTimesRow newRow = CTimesRow.createRow(CTimesRow.createList(
                first, second));
        newRow.correctInternalPraefixesAndRolle();
        return newRow;
    }

}
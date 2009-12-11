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
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;

public class CSplitterPot extends CSplitterBase {

    private int nr1;

    private int nr2;

    private int result;

    private boolean canSplit;

    public CSplitterPot() {
        this.nr1 = 1;
        this.nr2 = 1;
        this.result = 0;
        this.canSplit = false;
    }

    @Override
    public C_Changer getChanger(final C_Event event) {
        if (event instanceof CS_Event) {
            this.setEvent(event);
            final String op = ((CS_Event) event).getOperator();
            this.init(event.getFirst(), op);
            if (this.canDo()) {
                return this;
            }
        }
        return new C_No(event);
    }

    protected void init(final CElement cE1, final String operator) {
        if (cE1 instanceof CNum) {
            try {
                this.nr2 = Integer.parseInt(operator);
                if (this.nr2 <= 1) {
                    this.canSplit = false;
                } else {
                    this.nr1 = ((CNum) cE1).getValue();
                    final double root = Math.pow(this.nr1,
                            1 / ((double) this.nr2));
                    this.result = (int) root;
                    System.out.println("Result " + this.result);
                    if (Math.abs(root - this.result) > 0.0001) { // naja
                        this.canSplit = false;
                    } else {
                        this.canSplit = true;
                    }
                }
            } catch (final NumberFormatException e) {
                // geht nicht, operator muss Zahl sein
                this.canSplit = false;
            }
        } else {
            this.nr1 = 1;
            this.nr2 = 1;
            this.result = 0;
            // split bisher nur für Zahlen
            this.canSplit = false;
        }
    }

    @Override
    public boolean canDo() {
        System.out.println("Can split: " + this.canSplit);
        return this.canSplit;
    }

    @Override
    public CElement split() {
        final CElement parent = this.getEvent().getParent();
        final CElement cE1 = this.getEvent().getFirst();
        System.out.println("Do the Pot Num split");
        final CNum first = CNum.createNum(parent.getElement(), ""
                + this.result);
        final CNum second = CNum.createNum(cE1.getElement(), "" + this.nr2);
        final CPot newPot = CPot.createPot(first, second);
        return newPot;
    }

}

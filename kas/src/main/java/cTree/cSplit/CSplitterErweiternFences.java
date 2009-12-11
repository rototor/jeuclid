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
import cTree.CFences;
import cTree.CFrac;
import cTree.CIdent;
import cTree.CNum;
import cTree.CPlusRow;
import cTree.CTimesRow;
import cTree.adapter.C_Changer;
import cTree.cDefence.CD_Event;
import cTree.cDefence.DefHandler;

/**
 * nur Splits der Form e(x-45) oder e(x+2) oder e(3x+2) sind möglich!
 * 
 * @version $Revision$
 */
public class CSplitterErweiternFences extends CSplitterBase {

    private int nr1;

    private int nr2;

    private CFrac oldFrac;

    private CElement oldNum;

    private final CD_Event oldNumMes = new CD_Event(false);

    private CElement oldDen;

    private final CD_Event oldDenMes = new CD_Event(false);

    private CElement newEl;

    private enum SplitTyp {
        E, NO
    };

    private SplitTyp splitTyp;

    public CSplitterErweiternFences() {
        this.nr1 = 1;
        this.nr2 = 1;
        this.splitTyp = SplitTyp.NO;
    }

    @Override
    protected void init(final CS_Event event) {
        System.out.println("Init the Erw Fences split");
        final CElement cE1 = event.getFirst();
        final String op = event.getOperator();
        if (cE1 instanceof CFrac && this.checkSimpleString(cE1, op)) {

            this.oldFrac = (CFrac) cE1;
            this.oldNum = CFences.condCreateFenced(this.oldFrac.getZaehler()
                    .cloneCElement(true), this.oldNumMes);
            // evtl falsch ! Vorzeichen!
            this.oldDen = CFences.condCreateFenced(this.oldFrac.getNenner()
                    .cloneCElement(true), this.oldDenMes);
            // evtl falsch ! Vorzeichen!
            this.splitTyp = SplitTyp.E;
        } else {
            this.splitTyp = SplitTyp.NO;
        }
    }

    private boolean checkSimpleString(final CElement cE1, final String s) {
        System.out.println("cSS 1 " + s);
        if (s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')') {
            final String innen = s.substring(1, s.length() - 1);
            System.out.println("cSS 2 " + innen);
            if (innen.length() > 2) {
                final int p = Math
                        .max(innen.indexOf('+'), innen.indexOf('-'));
                System.out.println(innen.indexOf('+') + " "
                        + innen.indexOf('-'));
                final String num1 = innen.substring(0, p - 1);
                final String var = innen.substring(p - 1, p);
                final String op = innen.substring(p, p + 1);
                final String num2 = innen.substring(p + 1);
                System.out.println("cSS 3" + num1);
                if (p >= 2) {
                    if (var.compareTo("a") >= 0 && var.compareTo("z") <= 0) {
                        System.out.println("cSS 4");
                        try {
                            System.out.println("cSS 5");
                            this.nr1 = Integer.parseInt(num1);
                            this.nr2 = Integer.parseInt(num2);
                            final CNum cN1 = CNum.createNum(cE1.getElement(),
                                    "" + this.nr1);
                            final CIdent cI = CIdent.createIdent(cE1
                                    .getElement(), var);
                            final CTimesRow cT = CTimesRow
                                    .createRow(CTimesRow.createList(cN1, cI));
                            cT.correctInternalPraefixesAndRolle();
                            final CNum cN2 = CNum.createNum(cE1.getElement(),
                                    "" + this.nr2);
                            if (op.equals("+")) {
                                cN2.setPraefix("+");
                            } else {
                                cN2.setPraefix("-");
                            }
                            final CPlusRow cP = CPlusRow.createRow(CPlusRow
                                    .createList(cT, cN2));
                            cP.correctInternalPraefixesAndRolle();
                            this.newEl = cP;
                            System.out.println("cSS 7");
                            return true;
                        } catch (final NumberFormatException e) {
                        }
                    }
                } else {
                    if (var.compareTo("a") >= 0 && var.compareTo("z") <= 0) {
                        System.out.println("cSS 4");
                        try {
                            System.out.println("cSS 5");
                            this.nr2 = Integer.parseInt(num2);
                            System.out.println("cSS 6");
                            final CIdent cI = CIdent.createIdent(cE1
                                    .getElement(), var);
                            final CNum cN = CNum.createNum(cE1.getElement(),
                                    "" + this.nr2);
                            if (op.equals("+")) {
                                cN.setPraefix("+");
                            } else {
                                cN.setPraefix("-");
                            }
                            final CPlusRow cP = CPlusRow.createRow(CPlusRow
                                    .createList(cI, cN));
                            cP.correctInternalPraefixesAndRolle();
                            this.newEl = cP;
                            System.out.println("cSS 7");
                            return true;
                        } catch (final NumberFormatException e) {
                        }
                    }
                }
            }
        }
        return false;
    }

    protected CElement condCleanOne(final CElement el, final boolean doIt) {
        final CD_Event e = new CD_Event(el);
        final C_Changer c = DefHandler.getInst().getChanger(e);
        if (doIt && c.canDo()) {
            return c.doIt(null);
        } else {
            return el;
        }
    }

    @Override
    public boolean canDo() {
        System.out.println("Check the erweitern CEl-Split");
        return this.splitTyp != SplitTyp.NO;
    }

    @Override
    public CElement split() {
        System.out.println("Do the Erweitern split");
        final CTimesRow newNum = CTimesRow.createRow(CTimesRow.createList(
                this.oldNum, this.newEl));
        // extra this.condCleanOne(newNum, this.oldNumMes.isMessage());
        newNum.correctInternalPraefixesAndRolle();
        final CTimesRow newDen = CTimesRow.createRow(CTimesRow.createList(
                this.oldDen, this.newEl));
        newDen.correctInternalPraefixesAndRolle();
        // extra this.condCleanOne(newDen, this.oldDenMes.isMessage());
        final CFrac newFrac = CFrac.createFraction(newNum, newDen);
        return newFrac;
    }

}

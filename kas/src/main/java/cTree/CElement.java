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

package cTree;

import java.util.ArrayList;

import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.ElementAdapter;
import cTree.adapter.PraefixAdapter;
import cTree.adapter.RolleAdapter;
import cTree.cCombine.CombHandler;
import cTree.cDefence.CD_Event;
import cTree.cDefence.DefHandler;
import cTree.cExtract.ExtractHandler;
import cTree.cSplit.CS_Event;
import cTree.cSplit.SplitHandler;

public abstract class CElement extends RolleAdapter implements
        Comparable<CElement> {

    public CType getCType() {
        return CType.UNKNOWN;
    }

    // --- boolesche Tester default Verhalten ----------------------------
    public boolean hatGleichenBetrag(final CElement cE2) {
        return false;
    }

    public boolean is0() {
        return false;
    }

    public boolean istGleichartigesMonom(final CElement e2) {
        return false;
    }

    // --- Support für das Verbinden, Extrahieren und Aufspalten in dem CTree
    public CElement combineRight(final CElement active) {
        // der Operand
        final CElement erstesElement = active;
        if (erstesElement.hasNextC()) {
            // nach den Infos in event wird ein Changer gesucht
            final C_Event event = new C_Event(active);
            C_Changer c = CombHandler.getInst().getChanger(event);
            // in der Message kann der Changer ein Defence anfordern
            final CD_Event message = new CD_Event(false);
            CElement newActive = c.doIt(message);
            if (message != null && message.isDoDef()) {
                c = DefHandler.getInst().getChanger(message);
                // weitere Anforderungen werden nicht akzeptiert
                newActive = c.doIt(new CD_Event(true));
            }
            return newActive;
        } else {
            return active;
        }
    }

    public CElement extract(final ArrayList<CElement> active) {
        // nach den Infos in event wird ein Changer gesucht
        final C_Event event = new C_Event(active);
        C_Changer c = ExtractHandler.getInst().getChanger(event);
        // in der Message kann der Changer ein Defence anfordern
        final CD_Event message = new CD_Event(false);
        CElement newActive = c.doIt(message);
        if (message != null && message.isDoDef()) {
            c = DefHandler.getInst().getChanger(message);
            newActive = c.doIt(new CD_Event(true));
        }
        return newActive;
    };

    public CElement split(final CElement zuZerlegen, final String s) {
        // nach den Infos in event wird ein Changer gesucht
        final C_Event event = new CS_Event(zuZerlegen, s);
        C_Changer c = SplitHandler.getInst().getChanger(event);
        // in der Message kann der Changer ein Defence anfordern
        final CD_Event message = new CD_Event(false);
        CElement newActive = c.doIt(message);
        if (message != null && message.isDoDef()) {
            c = DefHandler.getInst().getChanger(message);
            newActive = c.doIt(new CD_Event(true));
        }
        return newActive;
    }

    // --- Support für die Klammern in dem CTree
    // --- wird von der CPlusRow und der CTimesRow überschrieben
    public CElement fence(final ArrayList<CElement> active) {
        System.out.println("Fencing in CElement");
        if (active.size() == 1) {
            return this.standardFencing(active.get(0));
        } else {
            return active.get(0);
        }
    }

    public CElement standardFencing(final CElement active) {
        System.out.println("CElement fencing");
        final CElement fences = CFences.createFenced(active
                .cloneCElement(false));
        this.replaceChild(fences, active, true, true);
        return fences;
    }

    public final CElement defence(final CElement aFencePair) {
        if (aFencePair instanceof CFences && aFencePair.hasChildC()) {
            final CD_Event e = new CD_Event(aFencePair);
            final C_Changer c = DefHandler.getInst().getChanger(e);
            c.doIt(null);
        }
        return aFencePair;
    }

    // compare

    public boolean hasNumberValue() {
        return false;
    }

    public double getNumberValue() {
        return Double.NaN;
    }

    public int compareTo(final CElement o) {
        if (o.hasMinOrDiv()) {
            return this.hasMinOrDiv() ? this.compareWithoutPraefix(o) : -1;
        } else {
            return this.hasMinOrDiv() ? 1 : this.compareWithoutPraefix(o);
        }
    }

    private int compareWithoutPraefix(final CElement o) {
        if (this.getCType() != o.getCType()) {
            return this.getCType().compareTo(o.getCType());
        } else {
            return this.internalCompare(o);
        }
    }

    public abstract int internalCompare(final CElement o);

    // --- Ausgaben ---------------------------------------
    public void show() {
        ElementAdapter.showElement(this.element);
        if (this.praefix != null) {
            System.out.print("Vorzeichen: ");
            PraefixAdapter.showPraefix(this.praefix);
        } else {
            System.out.println("kein extVZ");
        }
        System.out.println("cType: " + this.getCType());
        System.out.println("cRolle: " + this.cRolle);
        if (this.hasChildC()) {
            this.getFirstChild().showWithSiblings();
        }
    }

    private void showWithSiblings() {
        ElementAdapter.showElement(this.element);
        if (this.praefix != null) {
            System.out.print("Vorzeichen: ");
            PraefixAdapter.showPraefix(this.praefix);
        } else {
            System.out.println("kein intVZ");
        }
        System.out.println("cType: " + this.getCType());
        System.out.println("cRolle: " + this.cRolle);
        if (this.hasChildC()) {
            this.getFirstChild().showWithSiblings();
        }
        if (this.hasNextC()) {
            this.getNextSibling().showWithSiblings();
        }
    }
}

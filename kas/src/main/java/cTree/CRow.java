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

import cTree.adapter.DOMElementMap;

public abstract class CRow extends CElement {

    // Getter
    public abstract CRolle getFirstElementRolle();

    public abstract CRolle getLastElementRolle();

    public int getCount() {
        int count = 1;
        CElement cEl = this.getFirstChild();
        while (cEl.hasNextC()) {
            cEl = cEl.getNextSibling();
            count++;
        }
        return count;
    }

    // boolesche Tester, warscheinlich nicht wichtig
    protected abstract boolean isNecessaryRow(CElement el);

    // Mutatoren
    protected abstract CElement tauscheMitVorzeichen(CElement el1,
            CElement el2, boolean nachRechts);

    // Support für die Verschiebung in dem CTree
    @Override
    public CElement moveRight(final CElement el1) {
        if (el1.hasNextC()) {
            final CElement el2 = el1.getNextSibling();
            return this.tauscheMitVorzeichen(el1, el2, true);
        }
        return el1;
    }

    @Override
    public CElement moveRight(final int i, CElement el1) {
        for (int j = 0; j < i; j++) {
            if (el1.hasNextC()) {
                final CElement el2 = el1.getNextSibling();
                el1 = this.tauscheMitVorzeichen(el1, el2, true);
            }
        }
        return el1;
    }

    @Override
    public CElement moveLeft(final CElement el2) {
        if (el2.hasPrevC()) {
            final CElement el1 = el2.getPrevSibling();
            return this.tauscheMitVorzeichen(el1, el2, false);
        }
        return el2;
    }

    @Override
    public CElement moveLeft(int i, CElement el2) {
        System.out.println("i " + i);
        i = -i;

        for (int j = 0; j < i; j++) {
            if (el2.hasPrevC()) {
                final CElement el1 = el2.getPrevSibling();
                el2 = this.tauscheMitVorzeichen(el1, el2, false);
            }
        }
        return el2;
    }

    // ---------Support für die Vorzeichenbehandlung
    // noch nicht getestet
    public abstract void toggleAllVZ();

    // der erste Faktor wird nicht angetastet
    public abstract void toggleAllVZButFirst(boolean resolveFirstMinrow);

    // ------------------- insert und remove
    @Override
    public CElement insertBefore(final CElement newChild,
            final CElement refChild) {
        final boolean hasPrev = refChild.hasPrevC();
        final CElement e = super.insertBefore(newChild, refChild);
        if (hasPrev) {
            e.setCRolle(this.getLastElementRolle());
        } else {
            e.setCRolle(this.getFirstElementRolle());
            refChild.setCRolle(this.getLastElementRolle());
        }
        return DOMElementMap.getInstance().getCElement.get(e);
    }

    @Override
    public CElement removeChild(final CElement e,
            final boolean correctNextRolle, final boolean unregister,
            final boolean withNormalParent) {
        // evtl Rolle des nächsten umsetzen
        if (correctNextRolle
                && (e.getCRolle() == this.getFirstElementRolle())
                && e.hasNextC()) {
            e.getNextSibling().setCRolle(this.getFirstElementRolle());
            if (this instanceof CTimesRow) {
                e.getNextSibling().setTimesToEmpty();
            } else {
                e.getNextSibling().setPlusToEmpty();
            }
        }
        return super.removeChild(e, correctNextRolle, unregister,
                withNormalParent);
    }

    // ----------- Bulkoperationen
    /*
     * enthält den Endwert nicht mehr
     */
    public ArrayList<CElement> startTo(final CElement stop) {
        final ArrayList<CElement> result = new ArrayList<CElement>();
        if (this.hasChildC()) {
            CElement cElement = this.getFirstChild();
            if (cElement.equals(stop)) {
                return result;
            }
            CElement newElement = cElement.cloneCElement(true);
            result.add(newElement);
            while (cElement.hasNextC()) {
                cElement = cElement.getNextSibling();
                if (cElement.equals(stop)) {
                    return result;
                }
                newElement = cElement.cloneCElement(true);
                result.add(newElement);
            }
        }
        return result;
    }

    /*
     * enthält den Startwert noch nicht
     */
    public ArrayList<CElement> endFrom(final CElement start) {
        final ArrayList<CElement> result = new ArrayList<CElement>();
        if (this.hasChildC()) {
            CElement cElement = this.getFirstChild();
            while (!cElement.equals(start) && cElement.hasNextC()) {
                cElement = cElement.getNextSibling();
            }
            if (cElement.equals(start)) {
                while (cElement.hasNextC()) {
                    cElement = cElement.getNextSibling();
                    result.add(cElement.cloneCElement(true));
                }
            }
        }
        return result;
    }

    public ArrayList<CElement> getMemberList() {
        final ArrayList<CElement> result = new ArrayList<CElement>();
        if (this.hasChildC()) {
            CElement cElement = this.getFirstChild();
            CElement newElement = cElement.cloneCElement(true);
            result.add(newElement);
            while (cElement.hasNextC()) {
                cElement = cElement.getNextSibling();
                newElement = cElement.cloneCElement(true);
                result.add(newElement);
            }
        }
        return result;
    }

    public ArrayList<CElement> getMemberListFirstWithoutPraefix() {
        final ArrayList<CElement> result = new ArrayList<CElement>();
        if (this.hasChildC()) {
            CElement cElement = this.getFirstChild();
            CElement newElement = cElement.cloneCElement(false);
            result.add(newElement);
            while (cElement.hasNextC()) {
                cElement = cElement.getNextSibling();
                newElement = cElement.cloneCElement(true);
                result.add(newElement);
            }
        }
        return result;
    }

    public static ArrayList<CElement> cloneList(
            final ArrayList<CElement> oldList) {
        final ArrayList<CElement> newList = new ArrayList<CElement>();
        for (final CElement el : oldList) {
            newList.add(el.cloneCElement(true));
        }
        return newList;
    }

    public static ArrayList<CElement> makeSingleElementList(final CElement el) {
        final ArrayList<CElement> result = new ArrayList<CElement>();
        result.add(el);
        return result;
    }

    public static ArrayList<CElement> merge(final ArrayList<CElement> first,
            final ArrayList<CElement> second) {
        for (final CElement el : second) {
            first.add(el);
        }
        return first;
    }

    public static CRow createRow(final ArrayList<CElement> list,
            final String typ) {
        if (list.isEmpty()) {
            return null;
        } else {
            final CRow newRow = (CRow) CElementHelper.createAll(list.get(0)
                    .getElement(), "mrow", typ, CRolle.UNKNOWN, null);
            boolean isFirst = true;
            for (final CElement el : list) {
                if ("*".equals(typ)) {
                    CTimesRow.insertMember(newRow, el, isFirst);
                } else {
                    CPlusRow.insertMember(newRow, el, isFirst);
                }
                isFirst = false;
            }
            return newRow;
        }
    }

    public static void showAll(final ArrayList<CElement> list) {
        for (final CElement el : list) {
            el.show();
            System.out.println("------------------");
        }
    }

    // ------------ normalize - Support - wohl nicht genutzt
    protected void loescheLeereChildRows() {
        if (this.hasChildC()) {
            CElement actChild = this.getFirstChild();
            CElement nextChild = actChild.getNextSibling();
            do {
                if (actChild instanceof CTimesRow
                        && (actChild.hasChildC() == false)) {
                    this.removeChild(actChild, true, true, false);
                }
                actChild = nextChild;
                if (actChild != null && actChild.hasNextC()) {
                    nextChild = actChild.getNextSibling();
                }
            } while ((actChild != null) && actChild.hasNextC());
        }
    }

    protected void loescheChildRowWithOneChild() {
        if (this.hasChildC()) {
            CElement actChild = this.getFirstChild();
            CElement nextChild = actChild.getNextSibling();
            do {
                if (!this.isNecessaryRow(actChild)) {
                    final CElement childsChild = actChild.getFirstChild();
                    if (actChild.hasExtMinus()) {
                        childsChild.togglePlusMinus(false);
                    } else if (actChild.hasExtDiv()) {
                        childsChild.toggleTimesDiv(false);
                    }
                    this.replaceChild(childsChild, actChild, false, false);
                    if (childsChild.getCRolle() == this.getLastElementRolle()) {
                        childsChild.setEmptyToPlus();
                    }
                }
                actChild = nextChild;
                if (actChild != null && actChild.hasNextC()) {
                    nextChild = actChild.getNextSibling();
                }
            } while ((actChild != null) && actChild.hasNextC());
        }
    }

    @Override
    public void normalize() {
        this.loescheLeereChildRows();
        this.loescheChildRowWithOneChild();
        // loeseDieseRowInGrosserGleichartigerRowAuf();
    };
}

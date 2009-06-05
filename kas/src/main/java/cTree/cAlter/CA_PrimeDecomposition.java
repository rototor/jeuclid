package cTree.cAlter;

import java.util.ArrayList;
import java.util.List;

import cTree.CElement;
import cTree.CFences;
import cTree.CNum;
import cTree.CPot;
import cTree.CTimesRow;
import cTree.adapter.C_Changer;
import cTree.cDefence.CD_Event;
import cTree.cDefence.DefHandler;

public class CA_PrimeDecomposition extends CA_Base {

    private class PairOfInt {
        public int base;

        public int exp;

        PairOfInt(final int b, final int e) {
            this.base = b;
            this.exp = e;
        }
    }

    @Override
    public CElement doIt(final CD_Event message) {
        CElement old = this.getEvent().getFirst();
        try {
            int n = ((CNum) old).getValue();
            final List<PairOfInt> list = new ArrayList<PairOfInt>();
            int i = 2; // iterates candidates for prime factors
            while (i <= n) {
                if (n % i == 0) { // prime factor found
                    int exp = 0; // counts the instances of the factor
                    while (n % i == 0) {
                        exp++;
                        n = n / i;
                    }
                    list.add(new PairOfInt(i, exp));
                }
                i++;
            }
            for (final PairOfInt p : list) {
                System.out.println(p.base + " ^ " + p.exp);
            }
            final ArrayList<CElement> pL = new ArrayList<CElement>();
            for (final PairOfInt p : list) {
                if (p.exp == 1) {
                    pL.add(CNum.createNum(old.getElement(), "" + p.base));
                } else {
                    final CNum cB = CNum.createNum(old.getElement(), ""
                            + p.base);
                    pL.add(CPot.createPot(cB, p.exp));
                }
            }
            CElement result;
            if (pL.size() > 1) {
                final CTimesRow cTR = CTimesRow.createRow(pL);
                cTR.correctInternalPraefixesAndRolle();
                result = cTR;
            } else {
                result = pL.get(0);
            }
            final CFences cF = CFences.createFenced(result);
            final CElement parent = old.getParent();
            if (parent instanceof CFences) {
                final CElement grandparent = parent.getParent();
                grandparent.replaceChild(cF, parent, true, true);
                old = cF;
            } else {
                parent.replaceChild(cF, old, true, true);
                final CD_Event e = new CD_Event(cF);
                final C_Changer c = DefHandler.getInst().getChanger(e);
                old = c.doIt(null);
            }

        } catch (final NumberFormatException e) {
            System.out.println("CA_Primfaktorzerlegung: ParseFehler");
        }
        return old;
    }

    @Override
    public String getText() {
        return "Primfaktorzerlegung";
    }

    @Override
    public boolean canDo() {
        if (this.getEvent() != null && this.getEvent().getFirst() != null) {
            final CElement first = this.getFirst();
            return (first instanceof CNum) && (((CNum) first).getValue() > 1);
        }
        return false;
    }

}

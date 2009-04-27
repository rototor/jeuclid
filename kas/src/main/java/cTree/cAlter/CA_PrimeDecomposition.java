package cTree.cAlter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cTree.CElement;
import cTree.CFences;
import cTree.CNum;
import cTree.CPot;
import cTree.CTimesRow;
import cTree.cDefence.DefenceHandler;

public class CA_PrimeDecomposition extends CAlter {

    private class PairOfInt {
        public int base;

        public int exp;

        PairOfInt(final int b, final int e) {
            this.base = b;
            this.exp = e;
        }
    }

    @Override
    public CElement change(final ArrayList<CElement> els) {
        CElement old = els.get(0);
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
            final CTimesRow cTR = CTimesRow.createRow(pL);
            cTR.correctInternalPraefixesAndRolle();
            final CFences cF = CFences.createFenced(cTR);
            final CElement parent = old.getParent();
            if (parent instanceof CFences) {
                final CElement grandparent = parent.getParent();
                grandparent.replaceChild(cF, parent, true, true);
                old = cF;
            } else {
                parent.replaceChild(cF, old, true, true);
                old = DefenceHandler.getInstance().defence(cF.getParent(),
                        cF, cF.getInnen());
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
    public boolean check(final ArrayList<CElement> els) {
        final CElement el = els.get(0);
        return (el instanceof CNum) && (((CNum) el).getValue() > 1);
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}

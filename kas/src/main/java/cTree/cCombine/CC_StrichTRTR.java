package cTree.cCombine;

import cTree.CElement;
import cTree.CMinTerm;
import cTree.CNum;
import cTree.CRolle;
import cTree.CTimesRow;

public class CC_StrichTRTR extends CC_ {

    private boolean gleicheMin(final CElement el1, final CElement el2) {
        final boolean result1 = (el1.hasExtMinus() && el2.hasExtMinus());
        final boolean result2 = (!el1.hasExtMinus() && !el2.hasExtMinus());
        return result1 || result2;
    }

    @Override
    protected boolean canCombine(final CElement parent, final CElement cTR1,
            final CElement cTR2) {
        System.out.println("Repell add tr tr");
        if (!((CTimesRow) cTR1).isMonom()) {
            return false;
        }
        if (!((CTimesRow) cTR2).isMonom()) {
            return false;
        }
        return true;
    }

    @Override
    protected CElement createCombination(final CElement parent,
            final CElement cTR1, final CElement cTR2) {
        System.out.println("Combine add tr tr");
        final CTimesRow my1 = (CTimesRow) cTR1;
        final CTimesRow my2 = (CTimesRow) cTR2;
        // nun haben wir gleichartige Monome wir holen die Koeffizienten
        final int exp1 = my1.getKoeffAsBetragFromMonom().getValue();
        final int exp2 = my2.getKoeffAsBetragFromMonom().getValue();
        // newChild ist in der Regel ein Clone von cTR1, allerdings sollten
        // wir sicherheitshalber
        // einen Koeffizienten einfügen, falls es ihn nicht gibt:
        // negative Koeffizienten sind nicht vorgesehen!!
        CElement newChild = null;
        if (my1.getFirstChild() instanceof CNum) {
            newChild = cTR1.cloneCElement(false);
        } else {
            final CElement new1 = CNum.createNum(parent.getElement(), "1");
            new1.setCRolle(CRolle.FAKTOR1);
            System.out.println("Try to insert 1");
            newChild = CTimesRow.createRow(CTimesRow.createList(new1, my1));
        }

        if (cTR1.getCRolle() == CRolle.SUMMAND1) {
            if (cTR2.hasExtMinus()) {
                if (exp1 == exp2) {
                    newChild = CNum.createNum(parent.getElement(), "0");
                    newChild.setCRolle(CRolle.SUMMAND1);
                } else if (exp1 > exp2) {
                    newChild.getFirstChild().setText("" + (exp1 - exp2));
                } else {
                    final CElement arg = cTR1.cloneCElement(false); // evtl
                                                                    // falsch
                                                                    // falls
                                                                    // es
                                                                    // keinen
                                                                    // echten
                                                                    // Koeffizienten
                                                                    // bi
                    arg.getFirstChild().setText("" + (exp2 - exp1));
                    newChild = CMinTerm.createMinTerm(arg, CRolle.SUMMAND1);
                }
            } else {
                newChild.getFirstChild().setText("" + (exp1 + exp2));
            }

        } else {
            if (this.gleicheMin(cTR2, cTR1)) { // erhöhe Koeff um 1
                ((CTimesRow) newChild).getKoeffAsBetragFromMonom().setText(
                        "" + (exp1 + exp2));
            } else { // erniedrige Koeffizient um 1
                if (exp1 == exp2) {
                    newChild = CNum.createNum(parent.getElement(), "0");
                    newChild.setCRolle(CRolle.SUMMANDN1);
                } else if (exp1 > exp2) {
                    newChild.getFirstChild().setText("" + (exp1 - exp2));
                } else {
                    cTR1.togglePlusMinus(false); // evtl. falsch, wenn es
                                                 // keinen echten
                                                 // Koeffizienten gibt
                    newChild.getFirstChild().setText("" + (exp2 - exp1));
                }
            }
        }
        return newChild;
    }
}

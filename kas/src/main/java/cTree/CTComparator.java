package cTree;

import java.util.Comparator;

public class CTComparator implements Comparator<CElement> {
    /**
     * Default Constructor.
     */
    public CTComparator() {
        // Empty on purpose
    }

    public int compare(final CElement o1, final CElement o2) {
        if (o2.hasMinOrDiv()) {
            return o1.hasMinOrDiv() ? this.compareWithoutPraefix(o1, o2) : -1;
        } else {
            return o1.hasMinOrDiv() ? 1 : this.compareWithoutPraefix(o1, o2);
        }
    }

    private int compareWithoutPraefix(final CElement o1, final CElement o2) {
        final CType t1 = o1.getCType();
        final CType t2 = o2.getCType();
        if (t1 != t2
                && !(t1 == CType.IDENT && t2 == CType.POT || t2 == CType.IDENT
                        && t1 == CType.POT)) {
            return o1.getCType().compareTo(o2.getCType());
        } else {
            return o1.internalCompare(o2);
        }
    }
}

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
        if (o1.getCType() != o2.getCType()) {
            return o1.getCType().compareTo(o2.getCType());
        } else {
            return o1.internalCompare(o2);
        }
    }
}

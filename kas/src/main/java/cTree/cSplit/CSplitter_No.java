package cTree.cSplit;

import cTree.CElement;

public class CSplitter_No extends CSplitterBase {
    /**
     * Default Constructor.
     */
    public CSplitter_No() {
        // Empty on purpose
    }

    @Override
    public CElement doIt() {
        return this.getEvent().getFirst();
    }

    @Override
    public CElement split() {
        return null;
    }
}

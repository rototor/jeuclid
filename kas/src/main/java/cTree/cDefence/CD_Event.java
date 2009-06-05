package cTree.cDefence;

import java.util.ArrayList;

import cTree.CElement;
import cTree.CFences;
import cTree.adapter.C_Event;

public class CD_Event extends C_Event {

    private boolean doDef;

    public CD_Event(final boolean doDefence) {
        super();
        this.doDef = doDefence;
    }

    public CD_Event(final ArrayList<CElement> selection) {
        super(selection);
        this.doDef = true;
    }

    public CD_Event(final CElement cElement) {
        super(cElement);
        this.doDef = true;
    }

    public CD_Event(final CElement cElement, final boolean doDefence) {
        super(cElement);
        this.doDef = doDefence;
    }

    public CElement getFences() {
        return this.getSelection().get(0);
    }

    public CElement getInside() {
        if (this.getFences() != null && (this.getFences() instanceof CFences)) {
            return ((CFences) this.getFences()).getInnen();
        } else {
            return null;
        }
    }

    public boolean isDoDef() {
        return this.doDef;
    }

    public void setDoDef(final boolean doIt) {
        this.doDef = doIt;
    }

}

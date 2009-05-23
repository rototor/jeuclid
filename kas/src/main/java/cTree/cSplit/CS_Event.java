package cTree.cSplit;

import cTree.CElement;
import cTree.adapter.C_Event;

public class CS_Event extends C_Event {

    private final String s;

    public CS_Event(final CElement cElement, final String s) {
        super(cElement);
        this.s = s;
    }

    public String getString() {
        return this.s;
    }

    public String getOperation() {
        return this.s.substring(0, 1);
    }

    public String getOperator() {
        return this.s.substring(1);
    }
}

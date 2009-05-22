package cTree.cExtract;

import java.util.ArrayList;

import cTree.CElement;
import cTree.adapter.C_Event;

public class CE_Event extends C_Event {

    public CE_Event(final ArrayList<CElement> selection) {
        super(selection);
    }

    public CE_Event(final CElement cElement) {
        super(cElement);
    }

}

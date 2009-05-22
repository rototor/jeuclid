package cTree.adapter;

import java.util.ArrayList;

import cTree.CElement;
import cTree.cExtract.CE_Event;

public abstract class C_Changer {

    private CE_Event event;

    public abstract CElement doIt();

    public abstract boolean canDo(C_Event e);

    public CE_Event getEvent() {
        return this.event;
    }

    public void setEvent(final CE_Event event) {
        this.event = event;
    }

    public boolean justAll(final ArrayList<CElement> selection) {
        return !(selection.get(0).hasPrevC() || selection.get(
                selection.size() - 1).hasNextC());
    }

    public void insertOrReplace(final CElement newC, final boolean replace) {
        final CElement parent = this.event.getParent();
        final ArrayList<CElement> selection = this.event.getSelection();
        if (replace) {
            parent.getParent().replaceChild(newC, parent, true, true);
        } else {
            parent.replaceChild(newC, this.event.getFirst(), true, true);
            for (int i = 1; i < selection.size(); i++) {
                parent.removeChild(selection.get(i), true, true, false);
            }
        }
    }
}

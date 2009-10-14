package cTree.adapter;

import java.util.ArrayList;

import cTree.CElement;

public class C_Event {
    private final ArrayList<CElement> selection;

    public C_Event() {
        this.selection = new ArrayList<CElement>();
    }

    public C_Event(final ArrayList<CElement> selection) {
        this.selection = selection;
    }

    public C_Event(final CElement cElement) {
        this.selection = new ArrayList<CElement>();
        this.selection.add(cElement);
    }

    public ArrayList<CElement> getSelection() {
        return this.selection;
    }

    public void setCElement(final CElement cElement) {
        this.selection.clear();
        this.selection.add(cElement);
    }

    public CElement getFirst() {
        return this.selection.get(0);
    }

    public CElement getFirstFirst() {
        return this.selection.get(0).getFirstChild();
    }

    public CElement getParent() {
        return this.selection.get(0).getParent();
    }

}

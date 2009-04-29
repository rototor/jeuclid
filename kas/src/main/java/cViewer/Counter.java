package cViewer;

import java.util.Observable;

public class Counter extends Observable {

    private int number;

    /**
     * Default Constructor.
     */
    public Counter() {
        this.number = 0;
        this.number = 0;
        this.setChanged();
        this.notifyObservers();
    }

    public void reset() {
        this.number = 0;
        this.setChanged();
        this.notifyObservers();
    }

    public int getCount() {
        return this.number;
    }

    public void incCount() {
        System.out.println("SetChanged");
        this.number++;
        this.setChanged();
        this.notifyObservers();
    }
}

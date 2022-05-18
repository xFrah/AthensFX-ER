package com.athens.athensfx;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

class EquivalentExchange<S extends Person> extends Thread {
    protected final LinkedBlockingQueue<S> newborns;
    protected final ArrayList<S> list;
    protected final LinkedBlockingQueue<Integer> dead;
    Population population;

    public EquivalentExchange(ArrayList<S> list, LinkedBlockingQueue<S> newbornList, LinkedBlockingQueue<Integer> deadListint, int s, Population p) {
        super("EquivalentExchange-" + s);
        this.newborns = newbornList;
        this.dead = deadListint;
        this.list = list;
        this.population = p;
    }

    public void run() {
        while (population.running) { // efficiency?
            try {
                list.set(dead.take(), newborns.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
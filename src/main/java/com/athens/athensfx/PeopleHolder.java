package com.athens.athensfx;

import javafx.scene.chart.XYChart;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

public class PeopleHolder <S extends Person> {
    private final Population.PopulationUpdaterLock updaterPool;
    final ConcurrentLinkedQueue<Integer> dead = new ConcurrentLinkedQueue<>();
    final XYChart.Series<Number,Number> series = new XYChart.Series<>();
    final ArrayList<S> alive = new ArrayList<>();
    private boolean started = false;
    public ThreadLocalRandom tlr;

    public PeopleHolder(Population.PopulationUpdaterLock pool) { this.updaterPool = pool; }

    void startThreads() {
        if (!started) {
            started = true;
            new PeopleUpdater().start();
        } else {
            System.out.println("Thread already started...");
        }
    }

    void newSoul (S soul) {
        Integer passedSoul = dead.poll();
        if (passedSoul == null) {
            alive.add(soul);
        } else {
            alive.set(passedSoul, soul);
        }
    }

    public boolean randomSex () {
        return tlr.nextBoolean();
    }

    class PeopleUpdater extends Thread {
        PeopleUpdater () {
            super("PeopleUpdater");
            tlr = ThreadLocalRandom.current();
        } // todo is this efficient or not?

        public void run() {
            try {
                while (updaterPool.running) {
                    for (int i = 0; i < alive.size(); i++) {
                        alive.get(i).update(i);
                    }
                    synchronized (updaterPool) {
                        updaterPool.finished++;
                        updaterPool.wait();
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

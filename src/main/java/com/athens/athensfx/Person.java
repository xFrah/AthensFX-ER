package com.athens.athensfx;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Person {

    private int age = 0;
    final private int deathAge;
    protected final Population p;
    private final AtomicInteger group;

    public Person(Population pop, AtomicInteger group) {
        this.p = pop;
        this.group = group;
        this.deathAge = GrimReaper.deathAge();
        increment();
    }

    abstract void update(int i) throws InterruptedException;
    abstract void die(int i) throws InterruptedException;
    abstract void setSingle();
    abstract void setOld();
    abstract void setDead();

    void deathChance(int i) throws InterruptedException {
        if (age++ >= deathAge) {
            setDead();
            decrement();
            die(i);
        }
    }

    void tooOld() { if (age > 45) {setOld();} }
    void tooYoung() { if (age == 20) {setSingle();} } // set to 30 and see what happens

    void increment() { group.incrementAndGet(); }
    void decrement() { group.decrementAndGet(); }

}
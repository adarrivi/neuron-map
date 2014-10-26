package com.adarrivi.neuron.model;

public class Dendrite {

    private int lifeSpan;
    private Neuron neuron;

    public Dendrite(Neuron neuron, int lifeSpan) {
        this.neuron = neuron;
        this.lifeSpan = lifeSpan;
    }

    void step() {
        lifeSpan--;
        if (lifeSpan < 0) {
            lifeSpan = 0;
        }
    }

    void receiveSpike() {
        lifeSpan += 2;
        neuron.activate();
    }

    public Neuron getNeuron() {
        return neuron;
    }

    public int getLifeSpan() {
        return lifeSpan;
    }

    boolean isAlive() {
        return lifeSpan > 0;
    }

    public void setLifeSpan(int lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

}

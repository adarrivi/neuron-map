package com.adarrivi.neuron.model;

public class Dendrite {

    private int lifeSpan = 5;
    private Neuron neuron;

    public Dendrite(Neuron neuron) {
        this.neuron = neuron;
    }

    void step() {
        lifeSpan--;
        if (lifeSpan < 0) {
            lifeSpan = 0;
        }
    }

    void receiveSpike() {
        lifeSpan += 3;
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

}

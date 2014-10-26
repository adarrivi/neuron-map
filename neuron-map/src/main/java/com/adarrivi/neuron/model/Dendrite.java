package com.adarrivi.neuron.model;

import java.util.Optional;

public class Dendrite {

    private Neuron neuron;
    private Optional<Spike> spike = Optional.empty();
    private boolean open = true;

    public Dendrite(Neuron neuron) {
        this.neuron = neuron;
    }

    Optional<Spike> consumeSpike() {
        Optional<Spike> spikeToBeSend = spike;
        spike = Optional.empty();
        if (open) {
            return spikeToBeSend;
        }
        return Optional.empty();
    }

    void receiveSpike(Spike spike) {
        this.spike = Optional.of(spike);

    }

    Neuron getNeuron() {
        return neuron;
    }

    void open() {
        open = true;
    }

    void close() {
        open = false;
    }

}

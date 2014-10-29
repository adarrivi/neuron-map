package com.adarrivi.neuron.model;

import java.util.Optional;

public class Dendrite {

    private Optional<Spike> spike = Optional.empty();

    void receiveSpike(Spike spike) {
        this.spike = Optional.of(spike);

    }

    public Optional<Spike> getSpike() {
        return spike;
    }

    public void dismiss() {
        spike = Optional.empty();
    }

}

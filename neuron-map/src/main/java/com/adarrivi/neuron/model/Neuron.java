package com.adarrivi.neuron.model;

import java.util.ArrayList;
import java.util.List;

public class Neuron {

    private BrainPosition position;
    private List<Dendrite> accesibleDendrites = new ArrayList<>();
    private List<Dendrite> dendrites = new ArrayList<>();
    private boolean activated;
    private boolean inputNeuron;

    public Neuron(BrainPosition position) {
        this.position = position;
    }

    public void addDentrite(Dendrite dendrite) {
        dendrites.add(dendrite);
    }

    public void addAccessibleDendrite(Dendrite dendrite) {
        accesibleDendrites.add(dendrite);
    }

    public BrainPosition getPosition() {
        return position;
    }

    public void activate() {
        if (!activated) {
            activated = true;
            accesibleDendrites.stream().filter(Dendrite::isAlive).forEach(Dendrite::receiveSpike);
        }
    }

    public void step() {
        if (inputNeuron) {
            activate();
        }
        activated = false;
        dendrites.forEach(Dendrite::step);
    }

    public List<Dendrite> getAccesibleDendrites() {
        return accesibleDendrites;
    }

    public boolean isInputNeuron() {
        return inputNeuron;
    }

    public void setInputNeuron(boolean inputNeuron) {
        this.inputNeuron = inputNeuron;
    }

    public boolean isIsolated() {
        return !dendrites.stream().filter(Dendrite::isAlive).findAny().isPresent();
    }

}

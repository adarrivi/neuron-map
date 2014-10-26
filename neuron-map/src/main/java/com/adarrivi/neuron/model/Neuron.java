package com.adarrivi.neuron.model;

import java.util.ArrayList;
import java.util.List;

public class Neuron {

    private BrainPosition position;
    private List<Neuron> accesibleNeurons = new ArrayList<>();

    public Neuron(BrainPosition position) {
        this.position = position;
    }

    public BrainPosition getPosition() {
        return position;
    }

    public void setAccesibleNeurons(List<Neuron> accesibleNeurons) {
        this.accesibleNeurons = accesibleNeurons;
    }

    public List<Neuron> getAccesibleNeurons() {
        return accesibleNeurons;
    }

}

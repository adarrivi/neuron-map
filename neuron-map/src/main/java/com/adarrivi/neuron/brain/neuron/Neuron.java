package com.adarrivi.neuron.brain.neuron;

import java.util.ArrayList;
import java.util.List;

import com.adarrivi.neuron.model.Axon;
import com.adarrivi.neuron.model.BrainPosition;
import com.adarrivi.neuron.model.Dendrite;

public class Neuron {

    private BrainPosition position;
    private List<Axon> axons = new ArrayList<>();
    private List<Dendrite> dendrites = new ArrayList<>();
    private boolean receivedOnce;
    private int currentPotencial;
    private NeuronType type;

    Neuron(int currentPotencial, BrainPosition position, NeuronType type) {
        this.currentPotencial = currentPotencial;
        this.position = position;
        this.type = type;
    }

    NeuronType getType() {
        return type;
    }

    void setType(NeuronType type) {
        this.type = type;
    }

    void setPosition(BrainPosition position) {
        this.position = position;
    }

    void addDentrite(Dendrite dendrite) {
        dendrites.add(dendrite);
    }

    public BrainPosition getPosition() {
        return position;
    }

    public boolean isSending() {
        return axons.stream().filter(axon -> !axon.isReady()).findAny().isPresent();
    }

    void addAxon(Axon axon) {
        axons.add(axon);
    }

    List<Axon> getAxons() {
        return axons;
    }

    int getCurrentPotencial() {
        return currentPotencial;
    }

    boolean isReceivedOnce() {
        return receivedOnce;
    }

    List<Dendrite> getDendrites() {
        return dendrites;
    }

    void setReceivedOnce(boolean receivedOnce) {
        this.receivedOnce = receivedOnce;
    }

    void setCurrentPotencial(int currentPotencial) {
        this.currentPotencial = currentPotencial;
    }

}

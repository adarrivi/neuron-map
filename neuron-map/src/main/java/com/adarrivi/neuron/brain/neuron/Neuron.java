package com.adarrivi.neuron.brain.neuron;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private int lifeSpan;

    Neuron(int currentPotencial, BrainPosition position, NeuronType type, int maxLifeSpan) {
        this.currentPotencial = currentPotencial;
        this.position = position;
        this.type = type;
        this.lifeSpan = maxLifeSpan;
    }

    void step() {
        lifeSpan--;
    }

    NeuronType getType() {
        return type;
    }

    int getLifeSpan() {
        return lifeSpan;
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

    void removeConnection(Neuron neuron) {
        List<Axon> toRemove = axons.stream().filter(axon -> axon.getDestination().equals(neuron)).collect(Collectors.toList());
        axons.removeAll(toRemove);
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

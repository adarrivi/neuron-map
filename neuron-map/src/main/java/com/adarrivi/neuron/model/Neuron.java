package com.adarrivi.neuron.model;

import java.util.ArrayList;
import java.util.List;

public class Neuron {

    private BrainPosition position;
    private List<Axon> axons = new ArrayList<>();
    private List<Dendrite> dendrites = new ArrayList<>();
    private boolean inputNeuron;
    private boolean outputNeuron;
    private boolean receivedOnce;
    private int currentPotencial;

    public Neuron(int currentPotencial, BrainPosition position) {
        this.currentPotencial = currentPotencial;
        this.position = position;
    }

    public void setPosition(BrainPosition position) {
        this.position = position;
    }

    public void addDentrite(Dendrite dendrite) {
        dendrites.add(dendrite);
    }

    public BrainPosition getPosition() {
        return position;
    }

    public boolean isSending() {
        return axons.stream().filter(axon -> !axon.isReady()).findAny().isPresent();
    }

    public boolean isInputNeuron() {
        return inputNeuron;
    }

    public void setInputNeuron() {
        this.inputNeuron = true;
    }

    public void setOutputNeuron() {
        this.outputNeuron = true;
    }

    public void addAxon(Axon axon) {
        axons.add(axon);
    }

    public List<Axon> getAxons() {
        return axons;
    }

    public int getCurrentPotencial() {
        return currentPotencial;
    }

    public boolean isOutputNeuron() {
        return outputNeuron;
    }

    public boolean isReceivedOnce() {
        return receivedOnce;
    }

    public List<Dendrite> getDendrites() {
        return dendrites;
    }

    public void setReceivedOnce(boolean receivedOnce) {
        this.receivedOnce = receivedOnce;
    }

    public void setCurrentPotencial(int currentPotencial) {
        this.currentPotencial = currentPotencial;
    }

}

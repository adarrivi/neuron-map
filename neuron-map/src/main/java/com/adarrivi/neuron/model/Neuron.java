package com.adarrivi.neuron.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Neuron {

    @Value("${brain.neuron.potencial.rest}")
    private int restPotencial;
    @Value("${brain.neuron.potencial.threshold}")
    private int thresholdPotencial;

    @Autowired
    private Randomizer randomizer;

    private BrainPosition position;
    private List<Axon> axons = new ArrayList<>();
    private List<Dendrite> dendrites = new ArrayList<>();
    private boolean inputNeuron;
    private int currentPotencial;

    @PostConstruct
    public void init() {
        position = randomizer.getRandomPosition();
        currentPotencial = restPotencial;
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

    public boolean isActivated() {
        return isInputNeuron() || currentPotencial >= thresholdPotencial;
    }

    private void fireAndReset() {
        axons.forEach(Axon::triggerSpike);
        dendrites.forEach(Dendrite::close);
        currentPotencial = restPotencial;
    }

    public void step() {
        consumeSpikesFromDendrites();
        if (isActivated()) {
            fireAndReset();
        }
        axons.forEach(Axon::step);
        openDendritesIfAxonsReady();
    }

    private void consumeSpikesFromDendrites() {
        dendrites.forEach(this::consumeSpikeFromDendrite);
    }

    private void consumeSpikeFromDendrite(Dendrite dendrite) {
        Optional<Spike> spike = dendrite.consumeSpike();
        if (spike.isPresent()) {
            currentPotencial += spike.get().getIntensity();
        }
    }

    private void openDendritesIfAxonsReady() {
        if (!isSending()) {
            dendrites.forEach(Dendrite::open);
        }
    }

    public boolean isSending() {
        return axons.stream().filter(axon -> !axon.isReady()).findAny().isPresent();
    }

    public boolean isRestPotencial() {
        return restPotencial == currentPotencial;
    }

    public boolean isInputNeuron() {
        return inputNeuron;
    }

    public void setInputNeuron(boolean inputNeuron) {
        this.inputNeuron = inputNeuron;
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

}

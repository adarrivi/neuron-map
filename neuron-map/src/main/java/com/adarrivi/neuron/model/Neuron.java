package com.adarrivi.neuron.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Neuron {

    @Autowired
    private Randomizer randomizer;

    private BrainPosition position;
    private List<Dendrite> accesibleDendrites = new ArrayList<>();
    private List<Dendrite> dendrites = new ArrayList<>();
    private boolean activated;
    private boolean inputNeuron;

    @PostConstruct
    public void init() {
        position = randomizer.getRandomPosition();
    }

    public void setPosition(BrainPosition position) {
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
        if (isIsolated()) {
            Optional<Dendrite> randomDendrite = randomizer.getRandomElement(dendrites);
            if (randomDendrite.isPresent()) {
                randomDendrite.get().setLifeSpan(1);
            }
        }
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

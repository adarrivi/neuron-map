package com.adarrivi.neuron.model;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Axon implements SteppableEntity {

    @Autowired
    private Randomizer randomizer;

    @Value("${brain.axon.speed.min}")
    private int minStepsRequiredToSendSpike;
    @Value("${brain.axon.speed.max}")
    private int maxStepsRequiredToSendSpike;
    @Value("${brain.spike.intensity.min}")
    private int minSpikeIntensity;
    @Value("${brain.spike.intensity.max}")
    private int maxSpikeIntensity;

    private Dendrite dendrite;
    private int spikeIntensity;
    private int stepsRequiredToSendSpike;
    private Optional<Spike> spike;
    private int currentSpikeStep;
    private BrainPosition destinationPosition;

    @PostConstruct
    public void init() {
        spikeIntensity = randomizer.getRandomNumber(minSpikeIntensity, maxSpikeIntensity);
        stepsRequiredToSendSpike = randomizer.getRandomNumber(minStepsRequiredToSendSpike, maxStepsRequiredToSendSpike);
        spike = Optional.empty();
    }

    public void triggerSpike() {
        spike = Optional.of(new Spike(spikeIntensity));
    }

    public void setDestinationNeuron(Neuron neuron) {
        destinationPosition = neuron.getPosition();
    }

    public BrainPosition getDestinatioPosition() {
        return destinationPosition;
    }

    public void setDendrite(Dendrite dendrite) {
        this.dendrite = dendrite;
    }

    @Override
    public void step() {
        if (spike.isPresent()) {
            currentSpikeStep++;
            if (currentSpikeStep >= stepsRequiredToSendSpike) {
                sendSpikeToDendriteAndReset();

            }
        }
    }

    private void sendSpikeToDendriteAndReset() {
        dendrite.receiveSpike(spike.get());
        spike = Optional.empty();
        currentSpikeStep = 0;
    }

    public boolean isReady() {
        return currentSpikeStep == 0;
    }

}

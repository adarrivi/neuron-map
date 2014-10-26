package com.adarrivi.neuron.model;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Randomizer {

    private static final Random RANDOM = new Random();
    private static final Logger LOGGER = LoggerFactory.getLogger(Randomizer.class);

    @Value("${brain.height}")
    private int brainHeight;
    @Value("${brain.width}")
    private int branWidth;

    public BrainPosition getRandomPosition() {
        BrainPosition brainPosition = new BrainPosition(RANDOM.nextInt(branWidth), RANDOM.nextInt(brainHeight), getRandomRotation());
        LOGGER.debug("Neuron {} {}", brainPosition.getX(), brainPosition.getY());
        return brainPosition;
    }

    public double getRandomRotation() {
        return getRandomDouble(2 * Math.PI);
    }

    private double getRandomDouble(double limit) {
        return RANDOM.nextDouble() * limit;
    }

}

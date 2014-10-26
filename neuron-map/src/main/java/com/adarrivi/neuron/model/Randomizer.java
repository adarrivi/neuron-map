package com.adarrivi.neuron.model;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Randomizer {

    private static final Random RANDOM = new Random();

    @Value("${brain.height}")
    private int brainHeight;
    @Value("${brain.width}")
    private int branWidth;
    @Value("${brain.dendrite.maxLiveSpan}")
    private int maxLifeSpan;

    public BrainPosition getRandomPosition() {
        return new BrainPosition(RANDOM.nextInt(branWidth), RANDOM.nextInt(brainHeight), getRandomRotation());
    }

    public double getRandomRotation() {
        return getRandomDouble(2 * Math.PI);
    }

    private double getRandomDouble(double limit) {
        return RANDOM.nextDouble() * limit;
    }

    public <T> Optional<T> getRandomElement(List<T> list) {
        if (list.isEmpty()) {
            return Optional.empty();
        }
        int index = RANDOM.nextInt(list.size());
        return Optional.of(list.get(index));
    }

    public int getRandomLifeSpan() {
        if (RANDOM.nextBoolean()) {
            return RANDOM.nextInt(maxLifeSpan + 1);
        }
        return 0;
    }

}

package com.adarrivi.neuron.model;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Randomizer {

    private static final Random RANDOM = new Random();

    @Value("${frame.height}")
    private int frameHeight;
    @Value("${frame.width}")
    private int frameWidth;

    public BrainPosition getRandomPosition() {
        return new BrainPosition(RANDOM.nextInt(frameWidth), RANDOM.nextInt(frameHeight));
    }

    public <T> Optional<T> getRandomElement(List<T> list) {
        if (list.isEmpty()) {
            return Optional.empty();
        }
        int index = RANDOM.nextInt(list.size());
        return Optional.of(list.get(index));
    }

    public int getRandomNumber(int minNumber, int maxNumber) {
        return RANDOM.nextInt(maxNumber - minNumber) + minNumber;
    }

}

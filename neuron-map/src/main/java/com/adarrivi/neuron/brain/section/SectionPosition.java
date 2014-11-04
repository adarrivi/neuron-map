package com.adarrivi.neuron.brain.section;

import java.util.Random;

import com.adarrivi.neuron.model.BrainPosition;

class SectionPosition {
    private static final Random RANDOM = new Random();

    private int sideLenght;
    private BrainPosition originPosition;

    SectionPosition(int sideLenght, BrainPosition originPosition) {
        this.sideLenght = sideLenght;
        this.originPosition = originPosition;
    }

    boolean containsPosition(BrainPosition inputPosition) {
        return isInsideAxis(inputPosition.getX(), originPosition.getX()) && isInsideAxis(inputPosition.getY(), originPosition.getY());
    }

    private boolean isInsideAxis(int inputAxisPosition, int axis) {
        return inputAxisPosition >= axis && inputAxisPosition < axis + sideLenght;
    }

    BrainPosition getRandomPosition() {
        int x = getRandomWithingSide();
        int y = getRandomWithingSide();
        return new BrainPosition(x + originPosition.getX(), y + originPosition.getY());
    }

    private int getRandomWithingSide() {
        return RANDOM.nextInt(sideLenght);
    }

    BrainPosition getOriginPosition() {
        return originPosition;
    }
}

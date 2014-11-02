package com.adarrivi.neuron.brain.section;

import java.util.Random;

import com.adarrivi.neuron.model.BrainPosition;

public class Section {

    private static final Random RANDOM = new Random();

    private int gridXIndex;
    private int gridYIndex;
    private int sideLenght;
    private BrainPosition position;

    Section(int gridXIndex, int gridYIndex, int sideLenght, BrainPosition position) {
        this.gridXIndex = gridXIndex;
        this.gridYIndex = gridYIndex;
        this.sideLenght = sideLenght;
        this.position = position;
    }

    boolean containsPosition(BrainPosition inputPosition) {
        return isInsideAxis(inputPosition.getX(), position.getX()) && isInsideAxis(inputPosition.getY(), position.getY());
    }

    private boolean isInsideAxis(int inputAxisPosition, int axis) {
        return inputAxisPosition >= axis && inputAxisPosition < axis + sideLenght;
    }

    int getGridXIndex() {
        return gridXIndex;
    }

    int getGridYIndex() {
        return gridYIndex;
    }

    BrainPosition getRandomPosition() {
        int x = getRandomWithingSide();
        int y = getRandomWithingSide();
        return new BrainPosition(x + position.getX(), y + position.getY());
    }

    private int getRandomWithingSide() {
        return RANDOM.nextInt(sideLenght);
    }

    BrainPosition getPosition() {
        return position;
    }

}

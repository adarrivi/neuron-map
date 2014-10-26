package com.adarrivi.neuron.view;

import com.adarrivi.neuron.model.BrainPosition;

public class DrawPosition {

    private int x;
    private int y;
    private double rotation;

    public DrawPosition(int offset, BrainPosition position) {
        this(offset, position.getX(), position.getY(), position.getRotation());
    }

    public DrawPosition(int offset, int x, int y, double rotation) {
        this.x = offset + x;
        this.y = offset + y;
        this.rotation = rotation;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getRotation() {
        return rotation;
    }

}

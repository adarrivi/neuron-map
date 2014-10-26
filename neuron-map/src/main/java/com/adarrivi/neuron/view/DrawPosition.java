package com.adarrivi.neuron.view;

import com.adarrivi.neuron.model.BrainPosition;

class DrawPosition {

    private int x;
    private int y;
    private double rotation;

    DrawPosition(int offset, BrainPosition position) {
        this(offset, position.getX(), position.getY(), position.getRotation());
    }

    DrawPosition(int offset, int x, int y, double rotation) {
        this.x = offset + x;
        this.y = offset + y;
        this.rotation = rotation;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    double getRotation() {
        return rotation;
    }

}

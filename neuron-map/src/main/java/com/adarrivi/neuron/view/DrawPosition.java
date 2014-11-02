package com.adarrivi.neuron.view;

import com.adarrivi.neuron.model.BrainPosition;

class DrawPosition {

    private int x;
    private int y;

    DrawPosition(int offset, BrainPosition position) {
        this(offset, position.getX(), position.getY());
    }

    DrawPosition(int offset, int x, int y) {
        this.x = offset + x;
        this.y = offset + y;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

}

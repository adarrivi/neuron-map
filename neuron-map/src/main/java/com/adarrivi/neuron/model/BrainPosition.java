package com.adarrivi.neuron.model;

public class BrainPosition {

    private int x;
    private int y;

    public BrainPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double distance(BrainPosition pt) {
        double px = pt.getX() - this.getX();
        double py = pt.getY() - this.getY();
        return Math.sqrt(px * px + py * py);
    }

    public BrainPosition addX(int value) {
        return new BrainPosition(x + value, y);
    }

    public BrainPosition addY(int value) {
        return new BrainPosition(x, y + value);
    }

    public BrainPosition add(int xValue, int yValue) {
        return new BrainPosition(x + xValue, y + yValue);
    }

}

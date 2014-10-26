package com.adarrivi.neuron.model;

class Spike {

    private int intensity;

    Spike(int intensity) {
        this.intensity = intensity;
    }

    int getIntensity() {
        return intensity;
    }

    void setIntensity(int intensity) {
        this.intensity = intensity;
    }

}

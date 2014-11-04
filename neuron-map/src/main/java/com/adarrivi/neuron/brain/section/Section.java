package com.adarrivi.neuron.brain.section;

import com.adarrivi.neuron.model.BrainPosition;

public class Section {

    private SectionPosition position;
    private PotencialChange fastPotencialChange;
    private PotencialChange longPotencialChange;

    Section(int fastPotencialMaxSteps, int longPotencialMaxSteps) {
        this.fastPotencialChange = new PotencialChange(fastPotencialMaxSteps);
        this.longPotencialChange = new PotencialChange(longPotencialMaxSteps);
    }

    void setPosition(SectionPosition position) {
        this.position = position;
    }

    PotencialChange getFastPotencialChange() {
        return fastPotencialChange;
    }

    PotencialChange getLongPotencialChange() {
        return longPotencialChange;
    }

    BrainPosition getRandomPosition() {
        return position.getRandomPosition();
    }

    boolean containsPosition(BrainPosition inputPosition) {
        return position.containsPosition(inputPosition);
    }

    BrainPosition getOriginPosition() {
        return position.getOriginPosition();
    }

}

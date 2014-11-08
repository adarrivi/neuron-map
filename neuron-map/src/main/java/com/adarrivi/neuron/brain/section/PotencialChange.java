package com.adarrivi.neuron.brain.section;

import java.util.LinkedList;
import java.util.Optional;

class PotencialChange {

    private int maxStepsToRecord;
    private LinkedList<Integer> potencialChanges;
    private int potencialSum;

    PotencialChange(int maxStepsToRecord) {
        this.maxStepsToRecord = maxStepsToRecord;
        potencialChanges = new LinkedList<>();
    }

    void addPotencialChanges(int changesInCurrentStep) {
        potencialChanges.add(changesInCurrentStep);
        if (potencialChanges.size() > maxStepsToRecord) {
            potencialChanges.remove();
        }
        Optional<Integer> sum = potencialChanges.stream().reduce((a, b) -> a + b);
        if (sum.isPresent()) {
            potencialSum = sum.get();
        }
    }

    void reset() {
        potencialChanges.clear();
    }

    void increaseSumPotencialBy(int value) {
        potencialSum += value;
    }

    int getSumPotencialChange() {
        return potencialSum;
    }

}

package com.adarrivi.neuron.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.adarrivi.neuron.model.NeuronContainer;

@Component
public class Processor {

    @Autowired
    private NeuronContainer neuronContainer;

    public void processNewStep() {
        neuronContainer.stepNeurons();
    }

}

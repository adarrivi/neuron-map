package com.adarrivi.neuron.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.adarrivi.neuron.brain.neuron.NeuronContainer;
import com.adarrivi.neuron.brain.section.SectionGrid;

@Component
public class Processor {

    @Autowired
    private NeuronContainer neuronContainer;
    @Autowired
    private SectionGrid sectionGrid;

    public void processNewStep() {
        neuronContainer.stepNeurons();
        sectionGrid.step();
    }

}

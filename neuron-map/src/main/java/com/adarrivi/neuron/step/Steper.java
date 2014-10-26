package com.adarrivi.neuron.step;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.adarrivi.neuron.logic.Processor;
import com.adarrivi.neuron.view.BrainJPanel;

@Component
public class Steper {

    @Autowired
    private Processor processor;
    @Autowired
    private BrainJPanel brainJPanel;

    @Scheduled(initialDelay = 1000, fixedDelay = 100)
    public void step() {
        processor.processNewStep();
        brainJPanel.repaint();
    }
}

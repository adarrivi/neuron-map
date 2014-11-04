package com.adarrivi.neuron.brain.neuron;

import java.awt.Color;
import java.awt.Graphics2D;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adarrivi.neuron.model.Axon;
import com.adarrivi.neuron.view.DrawerComponent;
import com.adarrivi.neuron.view.ElementDrawer;

@Component
class NeuronDrawer implements ElementDrawer {

    @Value("${brain.neuron.potencial.threshold}")
    private int minPotencial;
    @Value("${brain.neuron.potencial.rest}")
    private int maxPotencial;

    @Autowired
    private NeuronContainer neuronContainer;
    @Autowired
    private DrawerComponent drawerComponent;

    @Override
    public void drawElements(Graphics2D graphics2d) {
        neuronContainer.getNeurons().forEach(neuron -> drawNeuron(neuron, graphics2d));
    }

    private void drawNeuron(Neuron neuron, Graphics2D graphics2d) {
        neuron.getAxons().forEach(axon -> drawConnection(neuron, axon, graphics2d));
        setColorByNeuronType(neuron, graphics2d);
        drawerComponent.drawCenteredCircle(neuron.getPosition(), 5, graphics2d);
        drawerComponent.setColor(Color.BLACK, 150, graphics2d);
        drawerComponent.drawString(neuron.getPosition(), neuron.getCurrentPotencial() + "", graphics2d);
    }

    private void drawConnection(Neuron neuron, Axon axon, Graphics2D graphics2d) {
        drawerComponent.setColor(Color.WHITE, 50, graphics2d);
        drawerComponent.drawLine(neuron.getPosition(), axon.getDestinatioPosition(), graphics2d);
    }

    private void setColorByNeuronType(Neuron neuron, Graphics2D graphics2d) {
        if (NeuronType.INPUT.equals(neuron.getType())) {
            drawerComponent.setColor(Color.PINK, DrawerComponent.MIN_TRANSPARENCY, graphics2d);
        } else if (NeuronType.OUTPUT.equals(neuron.getType())) {
            drawerComponent.setColor(Color.RED, DrawerComponent.MIN_TRANSPARENCY, graphics2d);
        } else if (NeuronType.BINDING.equals(neuron.getType())) {
            drawerComponent.setColor(Color.ORANGE, DrawerComponent.MIN_TRANSPARENCY, graphics2d);
        } else if (NeuronType.FAST.equals(neuron.getType())) {
            drawerComponent.setColor(Color.BLUE, DrawerComponent.MIN_TRANSPARENCY, graphics2d);
        } else {
            drawerComponent.setColor(Color.YELLOW, DrawerComponent.MIN_TRANSPARENCY, graphics2d);
        }
    }

}

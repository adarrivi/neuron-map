package com.adarrivi.neuron.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class NeuronContainer {

    @Value("${brain.neuron.number}")
    private int numberOfNeurons;
    @Value("${brain.neuron.accessible.distance}")
    private int neuronAccessibleDistance;
    @Value("${brain.neuron.active}")
    private int activeNeuronNumber;

    @Autowired
    private Randomizer randomizer;
    @Autowired
    private ApplicationContext applicationContext;

    private List<Neuron> neurons;

    public void initialize() {
        neurons = new ArrayList<>();
        for (int i = 0; i < numberOfNeurons; i++) {
            addNewRandomNeuron();
        }
        setUpInitialConntections();
        setActiveNeurons();
    }

    private void addNewRandomNeuron() {
        neurons.add(applicationContext.getBean(Neuron.class));
    }

    private void setUpInitialConntections() {
        neurons.forEach(this::setAccesibleNeurons);
    }

    private void setAccesibleNeurons(Neuron neuron) {
        List<Neuron> allNeurons = new ArrayList<>(neurons);
        allNeurons.remove(neuron);
        Stream<Neuron> allAccesibleNeurons = allNeurons.stream().filter(
                accesible -> neuron.getPosition().distance(accesible.getPosition()) < neuronAccessibleDistance);
        allAccesibleNeurons.forEach(accesible -> addNewConnection(neuron, accesible));
    }

    private void addNewConnection(Neuron neuron, Neuron accessible) {
        Dendrite accessibleDendrite = new Dendrite(accessible, 0);
        accessible.addDentrite(accessibleDendrite);
        neuron.addAccessibleDendrite(accessibleDendrite);
    }

    private void setActiveNeurons() {
        for (int i = 0; i < activeNeuronNumber; i++) {
            randomizer.getRandomElement(neurons).get().setInputNeuron(true);
        }
    }

    public List<Neuron> getNeurons() {
        return new ArrayList<>(neurons);
    }

}

package com.adarrivi.neuron.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class NeuronContainer {

    @Value("${brain.numberOfNeurons}")
    private int numberOfNeurons;
    @Value("${brain.neuron.accessible.distance}")
    private int neuronAccessibleDistance;
    @Value("${brain.inputNeurons}")
    private int activeNeuronNumber;

    @Autowired
    private Randomizer randomizer;
    @Autowired
    private ApplicationContext applicationContext;

    private List<Neuron> neurons;

    public void initialize() {
        createRandomNeurons();
        neurons.forEach(this::setAxonConnectionToAccessibleNeurons);
        setInputNeurons();
    }

    private void createRandomNeurons() {
        neurons = new ArrayList<>();
        for (int i = 0; i < numberOfNeurons; i++) {
            neurons.add(applicationContext.getBean(Neuron.class));
        }
    }

    private void setAxonConnectionToAccessibleNeurons(Neuron neuron) {
        List<Neuron> accessibleNeurons = getAccessibleNeurons(neuron);
        accessibleNeurons.forEach(accessible -> connectWithAxon(neuron, accessible));
    }

    private List<Neuron> getAccessibleNeurons(Neuron neuron) {
        List<Neuron> allNeurons = new ArrayList<>(neurons);
        allNeurons.remove(neuron);
        return allNeurons.stream().filter(accessible -> neuron.getPosition().distance(accessible.getPosition()) < neuronAccessibleDistance)
                .collect(Collectors.toList());
    }

    private void connectWithAxon(Neuron fromNeuron, Neuron toNeuron) {
        Dendrite toDendrite = new Dendrite(toNeuron);
        toNeuron.addDentrite(toDendrite);
        Axon fromAxon = applicationContext.getBean(Axon.class);
        fromAxon.setDendrite(toDendrite);
        fromNeuron.addAxon(fromAxon);
    }

    private void setInputNeurons() {
        for (int i = 0; i < activeNeuronNumber; i++) {
            randomizer.getRandomElement(neurons).get().setInputNeuron(true);
        }
    }

    public List<Neuron> getNeurons() {
        return new ArrayList<>(neurons);
    }

}

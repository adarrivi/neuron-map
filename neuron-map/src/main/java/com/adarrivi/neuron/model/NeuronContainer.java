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

    @Value("${frame.height}")
    private int height;
    @Value("${frame.width}")
    private int width;
    @Value("${frame.border}")
    private int border;

    @Autowired
    private Randomizer randomizer;
    @Autowired
    private ApplicationContext applicationContext;

    private List<Neuron> neurons;

    public void initialize() {
        createRandomNeurons();
        neurons.forEach(this::setAxonConnectionToAccessibleNeurons);
        // setInputNeurons();
    }

    private void createRandomNeurons() {
        neurons = new ArrayList<>();
        for (int i = 0; i < numberOfNeurons; i++) {
            neurons.add(applicationContext.getBean(Neuron.class));
        }
        Neuron origin = applicationContext.getBean(Neuron.class);
        origin.setInputNeuron();
        origin.setPosition(new BrainPosition(border, border, 0));
        neurons.add(origin);

        Neuron output = applicationContext.getBean(Neuron.class);
        output.setOutputNeuron();
        output.setPosition(new BrainPosition(width - border, height - border, 0));
        neurons.add(output);
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
        Dendrite toDendrite = new Dendrite();
        toNeuron.addDentrite(toDendrite);
        Axon fromAxon = applicationContext.getBean(Axon.class);
        fromAxon.setDendrite(toDendrite);
        fromAxon.setDestinationNeuron(toNeuron);
        fromNeuron.addAxon(fromAxon);
    }

    private void setInputNeurons() {
        for (int i = 0; i < activeNeuronNumber; i++) {
            randomizer.getRandomElement(neurons).get().setInputNeuron();
        }
    }

    public List<Neuron> getNeurons() {
        return new ArrayList<>(neurons);
    }

}

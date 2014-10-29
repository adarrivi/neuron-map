package com.adarrivi.neuron.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.adarrivi.neuron.logic.NeuronLogic;

@Component
public class NeuronContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeuronContainer.class);

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
    @Autowired
    private NeuronLogic neuronLogic;

    private List<Neuron> neurons;

    public void initialize() {
        createRandomNeurons();
        neurons.forEach(this::setAxonConnectionToAccessibleNeurons);
        // setInputNeurons();
    }

    public void stepNeurons() {
        List<Future<String>> collect = neurons.stream().map(neuron -> neuronLogic.step(neuron)).collect(Collectors.toList());
        for (Future<String> future : collect) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.debug("Interrupted {}", e);
            }
        }
    }

    private void createRandomNeurons() {
        neurons = new ArrayList<>();
        for (int i = 0; i < numberOfNeurons; i++) {
            neurons.add(neuronLogic.createRandomNeuron());
        }
        Neuron origin = neuronLogic.createNeuron(new BrainPosition(border, border, 0));
        origin.setInputNeuron();
        neurons.add(origin);

        Neuron output = neuronLogic.createNeuron(new BrainPosition(width - border, height - border, 0));
        output.setOutputNeuron();
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

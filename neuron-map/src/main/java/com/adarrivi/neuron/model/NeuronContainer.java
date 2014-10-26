package com.adarrivi.neuron.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NeuronContainer {

    @Value("${brain.neuron.number}")
    private int numberOfNeurons;
    @Value("${brain.neuron.accessible.distance}")
    private int neuronAccessibleDistance;

    @Autowired
    private Randomizer randomizer;

    private List<Neuron> neurons = new ArrayList<>();

    @PostConstruct
    public void initialize() {
        for (int i = 0; i < numberOfNeurons; i++) {
            addNewRandomNeuron();
        }
        setUpInitialConntections();
    }

    private void addNewRandomNeuron() {
        neurons.add(new Neuron(randomizer.getRandomPosition()));
    }

    private void setUpInitialConntections() {
        neurons.forEach(this::setAccesibleNeurons);
    }

    private void setAccesibleNeurons(Neuron neuron) {
        List<Neuron> allNeurons = new ArrayList<>(neurons);
        allNeurons.remove(neuron);
        List<Neuron> allAccesible = allNeurons.stream()
                .filter(accesible -> neuron.getPosition().distance(accesible.getPosition()) < neuronAccessibleDistance)
                .collect(Collectors.toList());
        neuron.setAccesibleNeurons(allAccesible);
    }

    public List<Neuron> getNeurons() {
        return new ArrayList<>(neurons);
    }

}

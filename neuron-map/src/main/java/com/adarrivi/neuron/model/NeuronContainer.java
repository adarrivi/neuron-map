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

import com.adarrivi.neuron.brain.section.SectionGrid;
import com.adarrivi.neuron.logic.NeuronLogic;

@Component
public class NeuronContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeuronContainer.class);

    @Value("${brain.neuron.accessible.distance}")
    private int accessibleDistance;

    @Autowired
    private SectionGrid sectionGrid;
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
        neurons.addAll(createRouteOfNeurons(new BrainPosition(0, 0), new BrainPosition(0, 600)));
        neurons.addAll(createRouteOfNeurons(new BrainPosition(200, 0), new BrainPosition(200, 600)));
        neurons.addAll(createRouteOfNeurons(new BrainPosition(300, 0), new BrainPosition(600, 600)));
    }

    private List<Neuron> createRouteOfNeurons(BrainPosition from, BrainPosition to) {
        List<BrainPosition> route = sectionGrid.getRoute(from, to, 70);
        List<Neuron> neuronsInRoute = route.stream().map(position -> neuronLogic.createNeuron(position)).collect(Collectors.toList());
        for (int i = 0; i < neuronsInRoute.size() - 1; i++) {
            connectWithAxon(neuronsInRoute.get(i), neuronsInRoute.get(i + 1));
        }
        return neuronsInRoute;

    }

    private void setAxonConnectionToAccessibleNeurons(Neuron neuron) {
        List<Neuron> accessibleNeurons = getAccessibleNeurons(neuron);
        accessibleNeurons.forEach(accessible -> connectWithAxon(neuron, accessible));
    }

    private List<Neuron> getAccessibleNeurons(Neuron neuron) {
        List<Neuron> allNeurons = new ArrayList<>(neurons);
        allNeurons.remove(neuron);
        return allNeurons.stream().filter(accessible -> neuron.getPosition().distance(accessible.getPosition()) < accessibleDistance)
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

    public List<Neuron> getNeurons() {
        return new ArrayList<>(neurons);
    }

}

package com.adarrivi.neuron.brain.neuron;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.adarrivi.neuron.brain.section.SectionGrid;
import com.adarrivi.neuron.model.Axon;
import com.adarrivi.neuron.model.BrainPosition;
import com.adarrivi.neuron.model.Dendrite;

@Component
public class NeuronContainer {

    private static final Random RANDOM = new Random();

    @Value("${brain.neuron.accessible.distance}")
    private int accessibleDistance;
    @Value("${brain.grid.bindingNeuronsPerRoute}")
    private int bindingNeuronsPerRoute;

    @Autowired
    private SectionGrid sectionGrid;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private NeuronLogic neuronLogic;

    private List<Neuron> neurons;

    @PostConstruct
    public void initialize() {
        createRouteBindingNeurons();
    }

    public void stepNeurons() {
        removeDeadNeurons();
        neurons.forEach(neuronLogic::step);
    }

    private void removeDeadNeurons() {
        List<Neuron> deadNeurons = neurons.stream().filter(neuron -> !neuronLogic.isAlive(neuron)).collect(Collectors.toList());
        neurons.removeAll(deadNeurons);
        neurons.forEach(neuron -> deadNeurons.forEach(deadNeuron -> neuron.removeConnection(deadNeuron)));
    }

    private void createRouteBindingNeurons() {
        neurons = new ArrayList<>();
        neurons.addAll(createRouteOfNeurons(new BrainPosition(0, 0), new BrainPosition(0, 500)));
        // neurons.addAll(createRouteOfNeurons(new BrainPosition(100, 0), new
        // BrainPosition(100, 500)));
        // neurons.addAll(createRouteOfNeurons(new BrainPosition(200, 0), new
        // BrainPosition(200, 500)));
        neurons.addAll(createRouteOfNeurons(new BrainPosition(350, 0), new BrainPosition(350, 500)));
        neurons.addAll(createRouteOfNeurons(new BrainPosition(400, 0), new BrainPosition(400, 500)));
    }

    private List<Neuron> createRouteOfNeurons(BrainPosition from, BrainPosition to) {
        List<BrainPosition> route = sectionGrid.getRoute(from, to, bindingNeuronsPerRoute);
        List<Neuron> neuronsInRoute = route.stream().map(position -> neuronLogic.createNeuron(position, NeuronType.BINDING))
                .collect(Collectors.toList());
        neuronsInRoute.get(0).setType(NeuronType.INPUT);
        neuronsInRoute.get(neuronsInRoute.size() - 1).setType(NeuronType.OUTPUT);
        return connectRouteNeurons(neuronsInRoute);
    }

    private List<Neuron> connectRouteNeurons(List<Neuron> neuronsInRoute) {
        for (int i = 0; i < neuronsInRoute.size() - 1; i++) {
            connectWithAxon(neuronsInRoute.get(i), neuronsInRoute.get(i + 1));
        }
        return neuronsInRoute;
    }

    public void createNeuron(BrainPosition position, NeuronType type) {
        Neuron fastNeuron = neuronLogic.createNeuron(position, type);
        setAxonConnectionToAccessibleNeurons(fastNeuron);
        neurons.add(fastNeuron);
    }

    private void setAxonConnectionToAccessibleNeurons(Neuron neuron) {
        List<Neuron> accessibleNeurons = getAccessibleNeurons(neuron);
        accessibleNeurons.forEach(accessible -> maybeConnectWithAxon(neuron, accessible));
        accessibleNeurons.forEach(accessible -> maybeConnectWithAxon(accessible, neuron));
    }

    private List<Neuron> getAccessibleNeurons(Neuron neuron) {
        List<Neuron> allNeurons = new ArrayList<>(neurons);
        allNeurons.remove(neuron);
        return allNeurons.stream().filter(accessible -> neuron.getPosition().distance(accessible.getPosition()) < accessibleDistance)
                .collect(Collectors.toList());
    }

    private void maybeConnectWithAxon(Neuron fromNeuron, Neuron toNeuron) {
        if (RANDOM.nextBoolean()) {
            connectWithAxon(fromNeuron, toNeuron);
        }
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

    public void restart() {
        initialize();
    }

}

package com.adarrivi.neuron.brain.neuron;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adarrivi.neuron.model.Axon;
import com.adarrivi.neuron.model.BrainPosition;
import com.adarrivi.neuron.model.Dendrite;
import com.adarrivi.neuron.model.Spike;

@Component
class NeuronLogic {

    @Value("${brain.neuron.potencial.rest}")
    private int restPotencial;
    @Value("${brain.neuron.potencial.threshold}")
    private int thresholdPotencial;
    @Value("${brain.neuron.fast.lifeSpan}")
    private int fastLifeSpan;

    Neuron createNeuron(BrainPosition position, NeuronType type) {
        int lifeSpanSteps = 0;
        if (NeuronType.FAST.equals(type)) {
            lifeSpanSteps = fastLifeSpan;
        }
        return new Neuron(restPotencial, position, type, lifeSpanSteps);
    }

    boolean isAlive(Neuron neuron) {
        if (isInmortalType(neuron)) {
            return true;
        }
        return neuron.getLifeSpan() > 0;
    }

    private boolean isInmortalType(Neuron neuron) {
        return NeuronType.BINDING.equals(neuron.getType()) || NeuronType.INPUT.equals(neuron.getType())
                || NeuronType.OUTPUT.equals(neuron.getType());
    }

    boolean isActivated(Neuron neuron) {
        return NeuronType.INPUT.equals(neuron.getType()) || neuron.getCurrentPotencial() >= thresholdPotencial;
    }

    public void step(Neuron neuron) {
        consumeSpikesFromDendrites(neuron);
        fireAndReset(neuron);
        neuron.getAxons().forEach(Axon::step);
        openDendritesIfAxonsReady(neuron);
        neuron.step();
    }

    private boolean isSending(Neuron neuron) {
        return neuron.getAxons().stream().filter(axon -> !axon.isReady()).findAny().isPresent();
    }

    private void consumeSpikesFromDendrites(Neuron neuron) {
        if (!isSending(neuron)) {
            neuron.getDendrites().forEach(dendrite -> consumeSpikeFromDendrite(neuron, dendrite));
        }
    }

    private void consumeSpikeFromDendrite(Neuron neuron, Dendrite dendrite) {
        Optional<Spike> spike = dendrite.getSpike();
        if (spike.isPresent()) {
            neuron.setReceivedOnce(true);
            int currentPotencial = neuron.getCurrentPotencial();
            currentPotencial += spike.get().getIntensity();
            if (currentPotencial < restPotencial) {
                currentPotencial = restPotencial;
            }
            if (currentPotencial > thresholdPotencial) {
                currentPotencial = thresholdPotencial;
            }
            neuron.setCurrentPotencial(currentPotencial);
            dendrite.dismiss();
        }
    }

    private void fireAndReset(Neuron neuron) {
        if (isActivated(neuron)) {
            neuron.getAxons().forEach(Axon::triggerSpike);
            neuron.setCurrentPotencial(restPotencial);
        }
    }

    private void openDendritesIfAxonsReady(Neuron neuron) {
        if (!isSending(neuron)) {

        }
    }

}

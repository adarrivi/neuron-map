package com.adarrivi.neuron.brain.section;

import java.util.List;
import java.util.Optional;

import com.adarrivi.neuron.model.BrainPosition;

public interface SectionGrid {

    Optional<Section> getSectionByPosition(BrainPosition inputPosition);

    Optional<BrainPosition> getRandomPositionWithinSection(BrainPosition position);

    List<BrainPosition> getRoute(BrainPosition from, BrainPosition to, int steps);

    int getSectionLength();

    List<Section> getAllSections();

    void step();

}

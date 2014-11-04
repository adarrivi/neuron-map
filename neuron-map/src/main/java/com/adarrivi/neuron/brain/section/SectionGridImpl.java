package com.adarrivi.neuron.brain.section;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adarrivi.neuron.brain.neuron.Neuron;
import com.adarrivi.neuron.brain.neuron.NeuronContainer;
import com.adarrivi.neuron.brain.neuron.NeuronLogic;
import com.adarrivi.neuron.model.BrainPosition;

@Component
class SectionGridImpl implements SectionGrid {

    private static final Logger LOGGER = LoggerFactory.getLogger(SectionGridImpl.class);

    @Value("${brain.grid.section.length}")
    private int sectionLength;
    @Value("${brain.grid.xSize}")
    private int gridXSize;
    @Value("${brain.grid.ySize}")
    private int gridYSize;

    @Value("${brain.grid.section.fastPotencialMaxSteps}")
    private int fastPotencialMaxSteps;
    @Value("${brain.grid.section.longPotencialMaxSteps}")
    private int longPotencialMaxSteps;

    @Autowired
    private NeuronContainer neuronContainer;
    @Autowired
    private NeuronLogic neuronLogic;

    private List<List<Section>> grid;

    SectionGridImpl() {
    }

    @PostConstruct
    public void init() {
        grid = new ArrayList<>();
        for (int x = 0; x < gridXSize; x++) {
            grid.add(new ArrayList<>());
            for (int y = 0; y < gridYSize; y++) {
                BrainPosition brainPosition = new BrainPosition(x * sectionLength, y * sectionLength);
                SectionPosition sectionPosition = new SectionPosition(sectionLength, brainPosition);
                Section section = new Section(fastPotencialMaxSteps, longPotencialMaxSteps);
                section.setPosition(sectionPosition);
                grid.get(x).add(section);
            }
        }
    }

    @Override
    public Optional<Section> getSectionByPosition(BrainPosition inputPosition) {
        return getAllSectionsStream().filter(section -> section.containsPosition(inputPosition)).findAny();
    }

    private Stream<Section> getAllSectionsStream() {
        return grid.stream().flatMap(gridRow -> gridRow.stream());
    }

    @Override
    public Optional<BrainPosition> getRandomPositionWithinSection(BrainPosition inputPosition) {
        Optional<Section> section = getSectionByPosition(inputPosition);
        if (section.isPresent()) {
            return Optional.of(section.get().getRandomPosition());
        }
        return Optional.empty();
    }

    @Override
    public List<BrainPosition> getRoute(BrainPosition from, BrainPosition to, int steps) {
        int xIncrement = (to.getX() - from.getX()) / steps;
        int yIncrement = (to.getY() - from.getY()) / steps;
        List<BrainPosition> positions = new ArrayList<>();
        for (int x = from.getX(), y = from.getY(); x <= to.getX() && y <= to.getY(); x += xIncrement, y += yIncrement) {
            Optional<BrainPosition> positionWithinSection = getRandomPositionWithinSection(new BrainPosition(x, y));
            if (positionWithinSection.isPresent()) {
                positions.add(positionWithinSection.get());
            } else {
                LOGGER.debug("Not found {}-{}", x, y);
            }
        }
        return positions;
    }

    @Override
    public int getSectionLength() {
        return sectionLength;
    }

    @Override
    public List<Section> getAllSections() {
        return getAllSectionsStream().collect(Collectors.toList());
    }

    @Override
    public void step() {
        getAllSections().forEach(this::registerPotencialChanges);
    }

    private void registerPotencialChanges(Section section) {
        Stream<Neuron> neuronsInSection = neuronContainer.getNeurons().stream()
                .filter(neuron -> section.containsPosition(neuron.getPosition()));
        int activatedNeurons = Long.valueOf(neuronsInSection.filter(Neuron::isSending).count()).intValue();
        section.getFastPotencialChange().addPotencialChanges(activatedNeurons);
        section.getLongPotencialChange().addPotencialChanges(activatedNeurons);
    }
}

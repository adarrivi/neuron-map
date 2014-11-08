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
import com.adarrivi.neuron.brain.neuron.NeuronType;
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

    @Value("${brain.grid.section.fastNeuronSpanPotencialThreshold}")
    private int fastNeuronSpanPotencialThreshold;
    @Value("${brain.grid.section.longNeuronSpanPotencialThreshold}")
    private int longNeuronSpanPotencialThreshold;
    @Value("${brain.grid.section.maxNeurons}")
    private int maxNeuronsPerSection;
    @Autowired
    private NeuronContainer neuronContainer;

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
        influencedByOtherSections();
        spanNeurons();
    }

    private void influencedByOtherSections() {
        getAllSections().forEach(
                section -> section.getFastPotencialChange().increaseSumPotencialBy(getNeighboursFastAveragePotencial(section)));
    }

    private int getNeighboursFastAveragePotencial(Section aSection) {
        Stream<Section> neighbours = getAllSections().stream().filter(section -> areNeighbours(aSection, section));
        Double average = neighbours.mapToInt(section -> section.getFastPotencialChange().getSumPotencialChange()).average().getAsDouble();
        average /= 5;
        return average.intValue();
    }

    private boolean areNeighbours(Section a, Section b) {
        BrainPosition aPosition = a.getOriginPosition();
        return b.containsPosition(aPosition.addX(sectionLength)) || b.containsPosition(aPosition.addX(-sectionLength))
                || b.containsPosition(aPosition.addY(sectionLength)) || b.containsPosition(aPosition.addY(-sectionLength))
                || b.containsPosition(aPosition.add(sectionLength, sectionLength))
                || b.containsPosition(aPosition.add(-sectionLength, -sectionLength))
                || b.containsPosition(aPosition.add(sectionLength, -sectionLength))
                || b.containsPosition(aPosition.add(-sectionLength, sectionLength));
    }

    private void registerPotencialChanges(Section section) {
        int activatedNeurons = Long.valueOf(getNeuronsInSection(section).filter(Neuron::isSending).count()).intValue();
        section.getFastPotencialChange().addPotencialChanges(activatedNeurons);
        section.getLongPotencialChange().addPotencialChanges(activatedNeurons);
    }

    private Stream<Neuron> getNeuronsInSection(Section section) {
        return neuronContainer.getNeurons().stream().filter(neuron -> section.containsPosition(neuron.getPosition()));
    }

    private void spanNeurons() {
        getAllSectionsStream().forEach(this::spanFastNeurons);
    }

    private void spanFastNeurons(Section section) {
        if (canAddFastNeuronInSection(section)) {
            neuronContainer.createNeuron(section.getRandomPosition(), NeuronType.FAST);
            section.getFastPotencialChange().reset();
        }
    }

    private boolean canAddFastNeuronInSection(Section section) {
        return section.getFastPotencialChange().getSumPotencialChange() >= fastNeuronSpanPotencialThreshold
                && getNeuronsInSection(section).count() <= maxNeuronsPerSection;
    }

    @Override
    public void restart() {
        neuronContainer.restart();
        init();
    }

}

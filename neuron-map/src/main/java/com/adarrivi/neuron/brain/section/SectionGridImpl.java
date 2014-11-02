package com.adarrivi.neuron.brain.section;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    private List<List<Section>> grid;

    SectionGridImpl() {
    }

    @PostConstruct
    public void init() {
        grid = new ArrayList<>();
        for (int x = 0; x < gridXSize; x++) {
            grid.add(new ArrayList<>());
            for (int y = 0; y < gridYSize; y++) {
                int xPosition = x * sectionLength;
                int yPosition = y * sectionLength;
                grid.get(x).add(new Section(x, y, sectionLength, new BrainPosition(xPosition, yPosition)));
            }
        }
    }

    @Override
    public Optional<Section> getSectionByPosition(BrainPosition inputPosition) {
        return grid.stream().flatMap(gridRow -> gridRow.stream()).filter(section -> section.containsPosition(inputPosition)).findAny();
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
    public List<BrainPosition> getSectionOrigins() {
        return grid.stream().flatMap(row -> row.stream()).map(Section::getPosition).collect(Collectors.toList());
    }
}

package com.adarrivi.neuron.brain.section;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.adarrivi.neuron.model.BrainPosition;

public class SectionGridImplTest {

    private static final int SECTION_LENGTH = 20;
    private static final int GRID_SIZE = 3;

    private SectionGrid victim = new SectionGridImpl();

    private BrainPosition inputPosition;
    private Optional<Section> outputSection;
    private Optional<BrainPosition> outputPosition;

    @Test
    public void getSectionByPosition_Pos0x0_ReturnsSection0x0() {
        givenGrid();
        givenPosition(0, 0);
        whenGetSectionByPosition();
        thenSectionShouldBe(0, 0);
    }

    private void givenGrid() {
        victim = new SectionGridImpl();
        ReflectionTestUtils.setField(victim, "gridXSize", GRID_SIZE);
        ReflectionTestUtils.setField(victim, "gridYSize", GRID_SIZE);
        ReflectionTestUtils.setField(victim, "sectionLength", SECTION_LENGTH);
        ((SectionGridImpl) victim).init();
    }

    private void givenPosition(int x, int y) {
        inputPosition = new BrainPosition(x, y);
    }

    private void whenGetSectionByPosition() {
        outputSection = victim.getSectionByPosition(inputPosition);
    }

    private void thenSectionShouldBe(int expectedXGrid, int expectedYGrid) {
        Assert.assertTrue(outputSection.isPresent());
        Assert.assertTrue(outputSection.get().getGridXIndex() == expectedXGrid);
        Assert.assertTrue(outputSection.get().getGridYIndex() == expectedYGrid);
    }

    @Test
    public void getSectionByPosition_Pos0x20_ReturnsSection0x1() {
        givenGrid();
        givenPosition(0, 20);
        whenGetSectionByPosition();
        thenSectionShouldBe(0, 1);
    }

    @Test
    public void getSectionByPosition_Pos20x20_ReturnsSection1x1() {
        givenGrid();
        givenPosition(20, 20);
        whenGetSectionByPosition();
        thenSectionShouldBe(1, 1);
    }

    @Test
    public void getSectionByPosition_Negative_ReturnsEmpty() {
        givenGrid();
        givenPosition(-1, -1);
        whenGetSectionByPosition();
        thenSectionShouldBeEmpty();
    }

    private void thenSectionShouldBeEmpty() {
        Assert.assertFalse(outputSection.isPresent());
    }

    @Test
    public void getRandomPositionWithinSection_Pos0x0_ReturnsPositionInSection0x0() {
        givenGrid();
        givenPosition(0, 0);
        whenGetRandomPositionWithinSection();
        thenOutputPositionHasToBelongToSection(0, 0);
    }

    private void whenGetRandomPositionWithinSection() {
        outputPosition = victim.getRandomPositionWithinSection(inputPosition);
    }

    private void thenOutputPositionHasToBelongToSection(int gridIndexX, int gridIndexY) {
        Assert.assertTrue(outputPosition.isPresent());
        BrainPosition brainPosition = outputPosition.get();
        Optional<Section> sectionByPosition = victim.getSectionByPosition(brainPosition);
        Assert.assertTrue(sectionByPosition.isPresent());
        Section section = sectionByPosition.get();
        Assert.assertTrue(section.containsPosition(brainPosition));
    }

    @Test
    public void getRandomPositionWithinSection_Pos20x20_ReturnsPositionInSection1x1() {
        givenGrid();
        givenPosition(1, 1);
        whenGetRandomPositionWithinSection();
        thenOutputPositionHasToBelongToSection(1, 1);
    }

}

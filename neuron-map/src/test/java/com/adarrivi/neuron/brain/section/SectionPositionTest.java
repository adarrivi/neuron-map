package com.adarrivi.neuron.brain.section;

import org.junit.Assert;
import org.junit.Test;

import com.adarrivi.neuron.model.BrainPosition;

public class SectionPositionTest {

    private static final int SIDE_LENGTH = 20;

    private SectionPosition victim;
    private BrainPosition inputPosition;
    private boolean outputBoolean;

    @Test
    public void containPosition_InsideCenter_ReturnsTrue() {
        givenSection(new BrainPosition(10, 10));
        givenPosition(new BrainPosition(15, 15));
        whenContainsPosition();
        thenShouldReturn(true);
    }

    private void givenSection(BrainPosition origin) {
        victim = new SectionPosition(SIDE_LENGTH, origin);
    }

    private void givenPosition(BrainPosition position) {
        inputPosition = position;
    }

    private void whenContainsPosition() {
        outputBoolean = victim.containsPosition(inputPosition);
    }

    private void thenShouldReturn(boolean expectedValue) {
        Assert.assertEquals(expectedValue, outputBoolean);
    }

    @Test
    public void containPosition_OutsideX_ReturnsFalse() {
        givenSection(new BrainPosition(10, 10));
        givenPosition(new BrainPosition(9, 10));
        whenContainsPosition();
        thenShouldReturn(false);
    }

    @Test
    public void containPosition_OutsideXPlusLength_ReturnsFalse() {
        givenSection(new BrainPosition(10, 10));
        givenPosition(new BrainPosition(10 + SIDE_LENGTH, 10));
        whenContainsPosition();
        thenShouldReturn(false);
    }

    @Test
    public void containPosition_OutsideY_ReturnsFalse() {
        givenSection(new BrainPosition(10, 10));
        givenPosition(new BrainPosition(10, 9));
        whenContainsPosition();
        thenShouldReturn(false);
    }

    @Test
    public void containPosition_OutsideYPlusLength_ReturnsFalse() {
        givenSection(new BrainPosition(10, 10));
        givenPosition(new BrainPosition(10, 10 + SIDE_LENGTH));
        whenContainsPosition();
        thenShouldReturn(false);
    }

}

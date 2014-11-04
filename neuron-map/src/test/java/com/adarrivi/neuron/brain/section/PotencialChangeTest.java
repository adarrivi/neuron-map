package com.adarrivi.neuron.brain.section;

import org.junit.Assert;
import org.junit.Test;

public class PotencialChangeTest {

    private PotencialChange victim;

    private int outputSum;

    @Test
    public void getSum_NoSteps_ReturnsZero() {
        givenStepsToRecord(3);
        whenGetSumPotencialChange();
        thenSumShouldBe(0);
    }

    private void givenStepsToRecord(int steps) {
        victim = new PotencialChange(steps);
    }

    private void givenStep(int changes) {
        victim.addPotencialChanges(changes);
    }

    private void whenGetSumPotencialChange() {
        outputSum = victim.getSumPotencialChange();
    }

    private void thenSumShouldBe(int expectedSum) {
        Assert.assertEquals(expectedSum, outputSum);
    }

    @Test
    public void getSum_NoStepsToRecord_OneStep_ReturnsZero() {
        givenStepsToRecord(0);
        givenStep(10);
        whenGetSumPotencialChange();
        thenSumShouldBe(0);
    }

    @Test
    public void getSum_TwoSteps_UnderMax_ReturnsSum() {
        givenStepsToRecord(3);
        givenStep(10);
        givenStep(4);
        whenGetSumPotencialChange();
        thenSumShouldBe(14);
    }

    @Test
    public void getSum_FourSteps_OverMax_ReturnsSumLast() {
        givenStepsToRecord(3);
        givenStep(10);
        givenStep(4);
        givenStep(3);
        givenStep(2);
        whenGetSumPotencialChange();
        thenSumShouldBe(9);
    }
}

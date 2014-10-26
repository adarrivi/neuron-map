package com.adarrivi.neuron.step;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adarrivi.neuron.logic.Processor;
import com.adarrivi.neuron.view.BrainJPanel;

@Component
public class Stepper {

    @Autowired
    private Processor processor;
    @Autowired
    private BrainJPanel brainJPanel;

    @Value("${frame.minMillisPerFrame}")
    private int msPerFrame;
    @Value("${frame.skipFrames}")
    private int skipFrames;

    private ExecutorService executor = Executors.newFixedThreadPool(1);

    private boolean stop;
    private long elapsedTimeMs;
    private long steps;

    public void start() {
        executor.execute(() -> stepUntilCancel());
    }

    private void stepUntilCancel() {
        stop = false;
        while (!stop) {
            long currentTime = System.currentTimeMillis();
            sleepIfTooFast();
            stepProcess();
            elapsedTimeMs = currentTime;
            steps++;
        }
    }

    public void stop() {
        stop = true;
    }

    private void sleepIfTooFast() {
        if (elapsedTimeMs == 0) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        long toSleepMs = msPerFrame - (currentTime - elapsedTimeMs);
        if (toSleepMs > 0) {
            try {
                Thread.sleep(toSleepMs);
            } catch (InterruptedException e) {
                // Nothing to do
            }
        }
    }

    private void stepProcess() {
        processor.processNewStep();
        if (screenNeedsToBeUpdated()) {
            brainJPanel.repaint();
        }
    }

    private boolean screenNeedsToBeUpdated() {
        return skipFrames == 0 || steps % skipFrames == 0;
    }

}

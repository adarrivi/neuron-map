package com.adarrivi.neuron.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adarrivi.neuron.model.Axon;
import com.adarrivi.neuron.model.BrainPosition;
import com.adarrivi.neuron.model.Neuron;
import com.adarrivi.neuron.model.NeuronContainer;
import com.adarrivi.neuron.model.Randomizer;

@Component
public class Drawer {

    @Value("${frame.height}")
    private int height;
    @Value("${frame.width}")
    private int width;
    @Value("${frame.border}")
    private int border;

    @Value("${brain.neuron.potencial.threshold}")
    private int minPotencial;
    @Value("${brain.neuron.potencial.rest}")
    private int maxPotencial;

    @Autowired
    private NeuronContainer neuronContainer;
    @Autowired
    private Randomizer randomizer;

    public void propagateDrawingAction(Graphics2D graphics2d) {
        drawEntities(graphics2d);
    }

    private void drawEntities(Graphics2D graphics2d) {
        neuronContainer.getNeurons().forEach(neuron -> drawNeuron(neuron, graphics2d));
    }

    private void drawNeuron(Neuron neuron, Graphics2D graphics2d) {
        DrawPosition neuronDrawPosition = toDrawPosition(neuron.getPosition());
        Color color = new Color(Color.GREEN.getRed(), Color.GREEN.getGreen(), Color.GREEN.getBlue(), asAlpha(neuron.getCurrentPotencial()));
        graphics2d.setColor(color);
        drawCenteredCircle(graphics2d, neuronDrawPosition.getX(), neuronDrawPosition.getY(), 5);
    }

    private int asAlpha(int potencial) {
        int min = 50;
        int max = 254;

        if (potencial > maxPotencial) {
            return max;
        }
        if (potencial < minPotencial) {
            return min;
        }
        return (((potencial - (minPotencial)) * (max - min)) / (maxPotencial - (minPotencial))) + min;
    }

    public void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
        x = x - (r / 2);
        y = y - (r / 2);
        g.fillOval(x, y, r, r);
    }

    private DrawPosition toDrawPosition(BrainPosition position) {
        return new DrawPosition(border, position);
    }

    private void drawConnection(Neuron neuron, Axon axon, Graphics2D graphics2d) {
        DrawPosition position1 = toDrawPosition(neuron.getPosition());
        DrawPosition position2 = toDrawPosition(axon.getDestinatioPosition());
        graphics2d.setColor(getTransparentColors(Color.WHITE, 10).get(3));
        graphics2d.drawLine(position1.getX(), position1.getY(), position2.getX(), position2.getY());
    }

    private List<Color> getTransparentColors(Color color, int bands) {
        List<Color> bandList = new ArrayList<>();
        int offset = 220 / bands;
        for (int currentBand = 0; currentBand < 255; currentBand += offset) {
            bandList.add(new Color(color.getRed(), color.getGreen(), color.getBlue(), currentBand));
        }
        return bandList;
    }
}

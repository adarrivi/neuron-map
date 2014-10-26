package com.adarrivi.neuron.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
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
        drawElement(getNeuronImage(neuron), neuronDrawPosition, graphics2d);
        // graphics2d.setColor(Color.BLACK);
        // graphics2d.drawString(neuron.getCurrentPotencial() + "",
        // neuronDrawPosition.getX(), neuronDrawPosition.getY());
        neuron.getAxons().forEach(axon -> drawConnection(neuron, axon, graphics2d));
    }

    private Image getNeuronImage(Neuron neuron) {
        if (neuron.isInputNeuron()) {
            return ImageCache.INPUT_NEURON;
        }
        if (neuron.isOutputNeuron()) {
            return ImageCache.OUTPUT_NEURON;
        }
        if (neuron.isSending() || neuron.isActivated()) {
            return ImageCache.NEURON_ACTIVATED;
        }
        if (!neuron.isRestPotencial()) {
            return ImageCache.NEURON_HIGH;
        }
        return ImageCache.NEURON_LOW;
    }

    private DrawPosition toDrawPosition(BrainPosition position) {
        return new DrawPosition(border, position);
    }

    private void drawElement(Image image, DrawPosition position, Graphics2D graphics2d) {
        AffineTransform oldTransform = graphics2d.getTransform();
        AffineTransform rotator = AffineTransform.getRotateInstance(position.getRotation(), position.getX(), position.getY());
        graphics2d.setTransform(rotator);
        drawImage(image, position, graphics2d);
        graphics2d.setTransform(oldTransform);
    }

    private void drawImage(Image image, DrawPosition position, Graphics2D graphics2d) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        int centerX = position.getX() - (width / 2);
        int centerY = position.getY() - (height / 2);
        graphics2d.drawImage(image, centerX, centerY, null);
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

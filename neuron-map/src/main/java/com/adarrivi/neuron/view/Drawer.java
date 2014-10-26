package com.adarrivi.neuron.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adarrivi.neuron.model.Neuron;
import com.adarrivi.neuron.model.NeuronContainer;
import com.adarrivi.neuron.model.Randomizer;

@Component
public class Drawer {

    @Value("${brain.height}")
    private int height;
    @Value("${brain.width}")
    private int width;
    @Value("${brain.border}")
    private int border;

    @Autowired
    private NeuronContainer neuronContainer;
    @Autowired
    private Randomizer randomizer;

    public void propagateDrawingAction(Graphics2D graphics2d) {
        drawEntities(graphics2d);
        graphics2d.setColor(Color.BLUE);
    }

    private void drawEntities(Graphics2D graphics2d) {
        neuronContainer.getNeurons().forEach(neuron -> drawNeuron(neuron, graphics2d));
    }

    private void drawNeuron(Neuron neuron, Graphics2D graphics2d) {
        drawElement(ImageCache.NEURON, toDrawPosition(neuron), graphics2d);
        neuron.getAccesibleNeurons().forEach(accessible -> drawConnection(toDrawPosition(neuron), toDrawPosition(accessible), graphics2d));
    }

    private DrawPosition toDrawPosition(Neuron neuron) {
        return new DrawPosition(border, neuron.getPosition());
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

    private void drawConnection(DrawPosition position1, DrawPosition position2, Graphics2D graphics2d) {
        graphics2d.setColor(Color.RED);
        graphics2d.drawLine(position1.getX(), position1.getY(), position2.getX(), position2.getY());
    }

    private void drawRectangle(DrawPosition position1, DrawPosition position2, Graphics2D graphics2d) {
        graphics2d.drawLine(position1.getX(), position1.getY(), position2.getX(), position1.getY());
        graphics2d.drawLine(position1.getX(), position1.getY(), position1.getX(), position2.getY());
        graphics2d.drawLine(position1.getX(), position2.getY(), position2.getX(), position2.getY());
        graphics2d.drawLine(position2.getX(), position1.getY(), position2.getX(), position2.getY());
    }

}

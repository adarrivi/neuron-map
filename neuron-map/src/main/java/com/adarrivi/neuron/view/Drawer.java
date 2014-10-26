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

import com.adarrivi.neuron.model.Dendrite;
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
    @Value("${brain.dendrite.maxLiveSpan}")
    private int maxLifeSpan;

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
        Image image = ImageCache.NEURON;
        if (neuron.isInputNeuron()) {
            image = ImageCache.INPUT_NEURON;
        } else if (neuron.isIsolated()) {
            image = ImageCache.ISOLATED_NEURON;
        }
        drawElement(image, toDrawPosition(neuron), graphics2d);
        neuron.getAccesibleDendrites().forEach(dendrite -> drawConnection(neuron, dendrite, graphics2d));
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

    private void drawConnection(Neuron neuron, Dendrite dendrite, Graphics2D graphics2d) {
        DrawPosition position1 = toDrawPosition(neuron);
        DrawPosition position2 = toDrawPosition(dendrite.getNeuron());
        graphics2d.setColor(getConnectionColor(dendrite));
        graphics2d.drawLine(position1.getX(), position1.getY(), position2.getX(), position2.getY());
    }

    private Color getConnectionColor(Dendrite dendrite) {
        int lifeSpan = dendrite.getLifeSpan();
        if (lifeSpan > maxLifeSpan) {
            return Color.WHITE;
        }
        return getTransparentColors(Color.WHITE, maxLifeSpan + 1).get(lifeSpan);
    }

    private List<Color> getTransparentColors(Color color, int bands) {
        List<Color> bandList = new ArrayList<>();
        int offset = 220 / bands;
        for (int currentBand = 0; currentBand < 255; currentBand += offset) {
            bandList.add(new Color(color.getRed(), color.getGreen(), color.getBlue(), currentBand));
        }
        return bandList;
    }

    private void drawRectangle(DrawPosition position1, DrawPosition position2, Graphics2D graphics2d) {
        graphics2d.drawLine(position1.getX(), position1.getY(), position2.getX(), position1.getY());
        graphics2d.drawLine(position1.getX(), position1.getY(), position1.getX(), position2.getY());
        graphics2d.drawLine(position1.getX(), position2.getY(), position2.getX(), position2.getY());
        graphics2d.drawLine(position2.getX(), position1.getY(), position2.getX(), position2.getY());
    }

    public static Color darken(Color color, double fraction) {

        int red = (int) Math.round(Math.max(0, color.getRed() - 255 * fraction));
        int green = (int) Math.round(Math.max(0, color.getGreen() - 255 * fraction));
        int blue = (int) Math.round(Math.max(0, color.getBlue() - 255 * fraction));

        int alpha = color.getAlpha();

        return new Color(red, green, blue, alpha);

    }

}

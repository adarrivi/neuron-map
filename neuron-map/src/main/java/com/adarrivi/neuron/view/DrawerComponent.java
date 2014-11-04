package com.adarrivi.neuron.view;

import java.awt.Color;
import java.awt.Graphics2D;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adarrivi.neuron.model.BrainPosition;

@Component
public class DrawerComponent {

    public static final int MIN_TRANSPARENCY = 255;

    @Value("${frame.border}")
    private int border;

    public void drawSquare(BrainPosition origin, int sideLength, Graphics2D graphics2d) {
        DrawPosition position1 = toDrawPosition(origin);
        int x = position1.getX();
        int y = position1.getY();
        graphics2d.drawLine(x, y, x + sideLength, y);
        graphics2d.drawLine(x, y, x, y + sideLength);
        graphics2d.drawLine(x, y + sideLength, x + sideLength, y + sideLength);
        graphics2d.drawLine(x + sideLength, y, x + sideLength, y + sideLength);
    }

    public void drawSolidSquare(BrainPosition origin, int sideLength, Graphics2D graphics2d) {
        int halfSideLength = sideLength / 2;
        graphics2d.fillRect(origin.getX() + halfSideLength, origin.getY() + halfSideLength, sideLength, sideLength);
    }

    private DrawPosition toDrawPosition(BrainPosition position) {
        return new DrawPosition(border, position);
    }

    public int getColorTransparency(int minValue, int maxValue, int currentValue) {
        int minTransparencyValue = 50;
        int maxTransparencyValue = 254;

        if (currentValue > maxValue) {
            return maxTransparencyValue;
        }
        if (currentValue < minValue) {
            return minTransparencyValue;
        }
        return (((currentValue - (minValue)) * (maxTransparencyValue - minTransparencyValue)) / (maxValue - (minValue)))
                + minTransparencyValue;
    }

    public void drawCenteredCircle(BrainPosition circlePosition, int radius, Graphics2D graphics2d) {
        DrawPosition position = toDrawPosition(circlePosition);
        int x = position.getX();
        int y = position.getY();
        x = x - (radius / 2);
        y = y - (radius / 2);
        graphics2d.fillOval(x, y, radius, radius);
    }

    public void drawLine(BrainPosition from, BrainPosition to, Graphics2D graphics2d) {
        DrawPosition position1 = toDrawPosition(from);
        DrawPosition position2 = toDrawPosition(to);
        graphics2d.drawLine(position1.getX(), position1.getY(), position2.getX(), position2.getY());
    }

    public void setColor(Color color, int transparency, Graphics2D graphics2d) {
        Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), transparency);
        graphics2d.setColor(newColor);
    }

    public void drawString(BrainPosition strPosition, String string, Graphics2D graphics2d) {
        DrawPosition position = toDrawPosition(strPosition);
        graphics2d.drawString(string, position.getX(), position.getY());
    }
}

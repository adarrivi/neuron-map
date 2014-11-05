package com.adarrivi.neuron.brain.section;

import java.awt.Color;
import java.awt.Graphics2D;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adarrivi.neuron.view.DrawerComponent;
import com.adarrivi.neuron.view.ElementDrawer;

@Service
class SectionDrawer implements ElementDrawer {

    @Value("${draw.showData}")
    private boolean showData;

    @Autowired
    private DrawerComponent drawerComponent;
    @Autowired
    private SectionGrid sectionGrid;

    @Override
    public void drawElements(Graphics2D graphics2d) {
        sectionGrid.getAllSections().forEach(section -> drawSection(section, graphics2d));
    }

    private void drawSection(Section section, Graphics2D graphics2d) {
        if (showData) {
            drawPotentialBackgroundColor(section, graphics2d);
            drawOutline(section, graphics2d);
            drawPotencialValue(section, graphics2d);
        }
    }

    private void drawPotentialBackgroundColor(Section section, Graphics2D graphics2d) {
        int colorTransparency = drawerComponent.getLightColorTransparency(0, 20, section.getFastPotencialChange().getSumPotencialChange());
        drawerComponent.setColor(Color.BLUE, colorTransparency, graphics2d);
        drawerComponent.drawSolidSquare(section.getOriginPosition(), sectionGrid.getSectionLength(), graphics2d);
    }

    private void drawOutline(Section section, Graphics2D graphics2d) {
        int colorTransparency = drawerComponent.getLightColorTransparency(0, 20, section.getFastPotencialChange().getSumPotencialChange());
        drawerComponent.setColor(Color.GREEN, colorTransparency, graphics2d);
        drawerComponent.drawSquare(section.getOriginPosition(), sectionGrid.getSectionLength(), graphics2d);
    }

    private void drawPotencialValue(Section section, Graphics2D graphics2d) {
        drawerComponent.setColor(Color.RED, DrawerComponent.MIN_TRANSPARENCY, graphics2d);
        drawerComponent.drawString(section.getOriginPosition(), section.getFastPotencialChange().getSumPotencialChange() + "", graphics2d);
    }

}

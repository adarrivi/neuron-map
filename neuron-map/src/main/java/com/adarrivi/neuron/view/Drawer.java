package com.adarrivi.neuron.view;

import java.awt.Graphics2D;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Drawer {

    @Autowired
    private List<ElementDrawer> elementDrawers;

    public void propagateDrawingAction(Graphics2D graphics2d) {
        drawEntities(graphics2d);
    }

    private void drawEntities(Graphics2D graphics2d) {
        elementDrawers.forEach(drawer -> drawer.drawElements(graphics2d));
    }

}

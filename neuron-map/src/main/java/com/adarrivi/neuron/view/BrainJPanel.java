package com.adarrivi.neuron.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.annotation.PostConstruct;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BrainJPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    @Value("${brain.height}")
    private int height;
    @Value("${brain.width}")
    private int width;
    @Value("${brain.border}")
    private int border;

    @Autowired
    private Drawer drawer;

    @PostConstruct
    private void initialize() {
        setBackground(Color.GRAY);
        setDoubleBuffered(true);
        setSize(width + (2 * border), height + (2 * border));
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        drawer.propagateDrawingAction((Graphics2D) graphics);
        Toolkit.getDefaultToolkit().sync();
        graphics.dispose();
    }

}

package com.adarrivi.neuron.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adarrivi.neuron.model.NeuronContainer;

@Component
public class BrainJPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    @Value("${frame.height}")
    private int height;
    @Value("${frame.width}")
    private int width;
    @Value("${frame.border}")
    private int border;

    @Autowired
    private Drawer drawer;
    @Autowired
    private NeuronContainer neuronContainer;

    private JButton restartButton;

    @PostConstruct
    private void initialize() {
        createRestartButton();
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

    private void createRestartButton() {
        restartButton = new JButton("Restart");
        restartButton.setBounds(10, 11, 89, 23);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                neuronContainer.initialize();

            }
        });
        add(restartButton);
    }
}

package com.adarrivi.neuron.view;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageCache {

    public static final Image NEURON_ACTIVATED = new ImageIcon(ImageCache.class.getResource("/img/neuron-activated.png")).getImage();
    public static final Image NEURON_HIGH = new ImageIcon(ImageCache.class.getResource("/img/neuron-high.png")).getImage();
    public static final Image NEURON_MEDIUM = new ImageIcon(ImageCache.class.getResource("/img/neuron-medium.png")).getImage();
    public static final Image NEURON_LOW = new ImageIcon(ImageCache.class.getResource("/img/neuron-low.png")).getImage();
    public static final Image INPUT_NEURON = new ImageIcon(ImageCache.class.getResource("/img/input-neuron.png")).getImage();
    public static final Image ISOLATED_NEURON = new ImageIcon(ImageCache.class.getResource("/img/isolated-neuron.png")).getImage();

}

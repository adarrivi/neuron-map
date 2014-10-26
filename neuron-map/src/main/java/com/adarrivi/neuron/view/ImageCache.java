package com.adarrivi.neuron.view;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageCache {

    public static final Image NEURON = new ImageIcon(ImageCache.class.getResource("/img/neuron.png")).getImage();
    public static final Image INPUT_NEURON = new ImageIcon(ImageCache.class.getResource("/img/input-neuron.png")).getImage();
    public static final Image ISOLATED_NEURON = new ImageIcon(ImageCache.class.getResource("/img/isolated-neuron.png")).getImage();

}

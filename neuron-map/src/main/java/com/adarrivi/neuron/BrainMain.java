package com.adarrivi.neuron;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import com.adarrivi.neuron.config.NeuronContext;
import com.adarrivi.neuron.view.BrainJPanel;

@Component
public class BrainMain extends JFrame implements Runnable {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(BrainMain.class);

    @Value("${brain.height}")
    private int frameHeight;
    @Value("${brain.width}")
    private int frameWidth;
    @Value("${brain.border}")
    private int border;

    @Autowired
    private BrainJPanel brainJPanel;

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        AbstractApplicationContext applicationContext = new AnnotationConfigApplicationContext(NeuronContext.class);
        applicationContext.registerShutdownHook();
        LOG.debug("Application context loaded");
        BrainMain colony = applicationContext.getBean(BrainMain.class);
        SwingUtilities.invokeLater(colony);
    }

    protected BrainMain() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(BrainMain.class.getSimpleName());
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void run() {
        centerFrame();
        add(brainJPanel);
        LOG.debug("Container initialized");
    }

    private void centerFrame() {
        setSize(frameWidth + (3 * border), frameHeight + (3 * border));
        // set location must be called after setSize to center the frame
        setLocationRelativeTo(null);
    }
}

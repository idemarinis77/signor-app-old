package it.uniroma2.signor.internal.task.about;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;  

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author amministratore
 */
import it.uniroma2.signor.internal.managers.SignorManager;
//import it.uniroma2.signor.internal.ui.panels.about.AboutPanel;

import javax.swing.*;
import java.awt.*;

public class AboutTask extends AbstractTask {
    private SignorManager manager;

    public AboutTask(SignorManager manager) {
        this.manager = manager;
    }

    @Override
    public void run(TaskMonitor taskMonitor)  {
        
    }
//    public void run(TaskMonitor taskMonitor)  {
//        SwingUtilities.invokeLater(() -> {
//            JDialog d = new JDialog();
//            d.setTitle("About SIGNOR");
//            d.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
//            d.setContentPane(new AboutPanel(manager));
//            d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//            d.pack();
//            d.setVisible(true);
//        });
//    }
}
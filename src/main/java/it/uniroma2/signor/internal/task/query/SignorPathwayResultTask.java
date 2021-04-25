/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.task.query;
import it.uniroma2.signor.internal.task.query.*;
import it.uniroma2.signor.internal.ui.panels.legend.SignorLegendPanel;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import java.util.HashMap;
import org.cytoscape.work.TaskFactory;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.ui.panels.result.SignorResultPanel;
import it.uniroma2.signor.internal.task.query.factories.SignorGenericRetrieveResultFactory;
import it.uniroma2.signor.internal.task.query.factories.SignorPanelFactory;
import javax.swing.*;
import java.awt.*;
import java.util.Properties;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelComponent2;

import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;

import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.task.query.factories.SignorPanelFactory;
/**
 *
 * @author amministratore
 */
public class SignorPathwayResultTask extends AbstractTask {
        private final SignorManager manager;
        private final HashMap<String, String> parameters; 
        private Network network;           
    
    public SignorPathwayResultTask(Network network, HashMap<String, String> parameters ){
        this.manager=network.manager;
        this.network = network;
        this.parameters = parameters;        
    }
    
  
    @Override
    public void run(TaskMonitor monitor) {
        monitor.setTitle("Creating Pathway"); 
         
        
    }
}

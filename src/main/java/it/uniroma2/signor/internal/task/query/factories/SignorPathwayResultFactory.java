/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.task.query.factories;
import it.uniroma2.signor.internal.task.query.SignorPathwayResultTask;
import org.cytoscape.application.swing.search.AbstractNetworkSearchTaskFactory;
import org.cytoscape.work.TaskIterator;
import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.Icon;

import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.ConfigResources;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.utils.IconUtils;
import it.uniroma2.signor.internal.ui.components.SearchQueryComponent;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.task.query.SignorPathwayResultTask;
import it.uniroma2.signor.internal.ui.components.ChooseSearchoption;
import java.util.HashMap;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.AbstractTaskFactory;

/**
 *
 * @author amministratore
 */
public class SignorPathwayResultFactory extends AbstractTaskFactory {
        private final SignorManager manager;
        
        private final HashMap<String, String> parameters;  
    
        public SignorPathwayResultFactory(SignorManager manager, HashMap<String, String> parameters) {            
             this.manager = manager;
             this.parameters = parameters;
        }
   
        public TaskIterator createTaskIterator() {                           
                manager.utils.info("Performing SIGNOR search for "+parameters.toString()); 
                return new TaskIterator(new SignorPathwayResultTask(new Network(manager, parameters), parameters));              
        }
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.task.query.factories;
import org.cytoscape.application.swing.search.AbstractNetworkSearchTaskFactory;
import org.cytoscape.work.TaskIterator;
import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.Icon;


import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.task.query.SignorInteractomeTask;
import java.util.HashMap;
import org.cytoscape.work.AbstractTaskFactory;

/**
 *
 * @author amministratore
 */
public class SignorInteractomeFactory extends AbstractTaskFactory{

    
    SignorManager manager;   
    HashMap <String, Object> parameters = new HashMap();
    
    public SignorInteractomeFactory(SignorManager manager) {
         this.manager = manager;
         parameters.put("INTERACTOME", true);
         
         
    }
   

    public TaskIterator createTaskIterator() {            
        
            return new TaskIterator(new SignorInteractomeTask(new Network(manager, parameters))); 
        
    }

   

}

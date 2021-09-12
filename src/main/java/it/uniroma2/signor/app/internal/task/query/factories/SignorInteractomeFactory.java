/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.task.query.factories;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.*;
import org.cytoscape.work.TaskIterator;
import it.uniroma2.signor.app.internal.Config;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import it.uniroma2.signor.app.internal.task.query.SignorInteractomeTask;
import java.util.HashMap;
import org.cytoscape.work.AbstractTaskFactory;

public class SignorInteractomeFactory extends AbstractTaskFactory{

    
    SignorManager manager;   
    HashMap <String, Object> parameters = new HashMap();
    Boolean ptm_interactome;
    
    public SignorInteractomeFactory(SignorManager manager, Boolean ptm_interactome) {
         this.manager = manager;        
         this.ptm_interactome = ptm_interactome;
    }
   
    public TaskIterator createTaskIterator() {            
            parameters =NetworkSearch.buildSearch(Config.INTERACTOMENAME, "Homo Sapiens", "", false);
            return new TaskIterator(new SignorInteractomeTask(new Network(manager, parameters), ptm_interactome)); 
        
    }  

}

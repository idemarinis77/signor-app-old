/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.task.query.factories;

import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

import it.uniroma2.signor.app.internal.managers.SignorManager;
import it.uniroma2.signor.app.internal.task.query.AlgorithmTask;

public class AlgorithmFactory extends AbstractTaskFactory {
     CyNetworkView ntwView;
     SignorManager manager;
     public AlgorithmFactory(CyNetworkView ntwView, SignorManager manager) {
        this.ntwView = ntwView;
        this.manager = manager;
     }

    public TaskIterator createTaskIterator() {
        if (ntwView != null) {
            return new TaskIterator(new AlgorithmTask(ntwView, manager));
        }
        return null;
    }

    public boolean isReady() {
        return true;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.uniroma2.signor.internal.task.about.factories;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.task.about.AboutTask;


public class AboutTaskFactory extends AbstractTaskFactory {
    private SignorManager manager;

    public AboutTaskFactory(SignorManager manager) {
        this.manager = manager;
    }

    @Override
    public TaskIterator createTaskIterator() {
        return new TaskIterator(new AboutTask(manager));
    }
}

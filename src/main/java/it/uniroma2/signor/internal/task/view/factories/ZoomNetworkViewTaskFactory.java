package it.uniroma2.signor.internal.task.view.factories;

import org.cytoscape.task.AbstractNetworkViewTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.TaskIterator;
import it.uniroma2.signor.internal.task.view.ZoomNetworkViewTask;

import it.uniroma2.signor.internal.managers.SignorManager;

public class ZoomNetworkViewTaskFactory extends AbstractNetworkViewTaskFactory {

        public double scale;
        public SignorManager manager;
        
        public ZoomNetworkViewTaskFactory(double scale, SignorManager manager){
            this.scale = scale;
            this.manager = manager;
        }
    
	public TaskIterator createTaskIterator(CyNetworkView networkView) {
                manager.utils.info("ZoomFactory create task");
		return new TaskIterator(new ZoomNetworkViewTask(networkView, scale, manager));
	} 
}

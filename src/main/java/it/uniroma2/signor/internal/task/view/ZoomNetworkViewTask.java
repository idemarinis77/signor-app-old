package it.uniroma2.signor.internal.task.view;

import static org.cytoscape.view.presentation.property.BasicVisualLexicon.NETWORK_SCALE_FACTOR;
import org.cytoscape.task.AbstractNetworkViewTask;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import it.uniroma2.signor.internal.managers.SignorManager;

public class ZoomNetworkViewTask extends AbstractNetworkViewTask {

//	@Tunable(description="Scale")
	public double scale = 0.2; // Default value
        SignorManager manager;
        CyNetworkView view;
	public ZoomNetworkViewTask(CyNetworkView view, double scale, SignorManager manager) {
		super(view);
                this.scale = scale;
                this.manager = manager;
                this.view = view;
	}

	public void run(TaskMonitor tm) {
		manager.utils.info("ZommTask run() la vista "+view.toString());
		if(this.view == null){
			return;
		}
		
		double newScale = view.getVisualProperty(NETWORK_SCALE_FACTOR).doubleValue() * scale;
		view.setVisualProperty(NETWORK_SCALE_FACTOR, newScale);		
		view.updateView();
                manager.signorStyleManager.installView(view);
	}
}

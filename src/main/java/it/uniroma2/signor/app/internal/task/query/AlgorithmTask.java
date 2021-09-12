/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.task.query;

import it.uniroma2.signor.app.internal.managers.SignorManager;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.TunableSetter;


public class AlgorithmTask extends AbstractTask {
    SignorManager manager;
    CyNetworkView cyview;
    
    public AlgorithmTask(CyNetworkView ntwView, SignorManager manager){
        this.manager = manager;
        this.cyview = ntwView;
    }
    @Override
    public void run(TaskMonitor monitor) {        
        monitor.setTitle("Applying force-directed algorithm to SIGNOR Network");          
        CyLayoutAlgorithmManager layoutManager = manager.utils.getService(CyLayoutAlgorithmManager.class);
        CyLayoutAlgorithm alg = layoutManager.getLayout("force-directed-cl");
        if (alg == null) alg = layoutManager.getLayout("force-directed");
            Object context = alg.getDefaultLayoutContext();            
        try {
            TunableSetter setter = manager.utils.getService(TunableSetter.class);
            manager.utils.info(setter.toString());
            Map<String, Object> layoutArgs = new HashMap<>();
            layoutArgs.put("defaultNodeMass", 10.0);
            manager.utils.info(layoutArgs.toString());

            setter.applyTunables(context, layoutArgs);
            manager.utils.info(cyview.toString());
            Set<View<CyNode>> nodeViews = new HashSet<>(cyview.getNodeViews());
            
            TaskIterator taskIterator = alg.createTaskIterator(cyview, context, nodeViews, null);
            insertTasksAfterCurrentTask(taskIterator);  
        }
        catch (Exception e){
            manager.utils.error(e.toString());
        }
    }
    
}

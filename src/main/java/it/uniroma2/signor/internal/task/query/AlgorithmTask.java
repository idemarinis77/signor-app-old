/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.task.query;

import it.uniroma2.signor.internal.managers.SignorManager;
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

/**
 *
 * @author amministratore
 */

public class AlgorithmTask extends AbstractTask {
    SignorManager manager;
    CyNetworkView cyview;
    
    public AlgorithmTask(CyNetworkView ntwView, SignorManager manager){
        this.manager = manager;
        this.cyview = ntwView;
    }
    public void run(TaskMonitor monitor) {
        
            monitor.setTitle("Algorithm Signor Network");            
            
            CyLayoutAlgorithmManager layoutManager = manager.utils.getService(CyLayoutAlgorithmManager.class);
           if (layoutManager == null )manager.utils.info("Il layoutManager è nullo");
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
            
            if (nodeViews == null ) monitor.showMessage(TaskMonitor.Level.INFO, "nodeViews nullo");
            TaskIterator taskIterator = alg.createTaskIterator(cyview, context, nodeViews, null);
            insertTasksAfterCurrentTask(taskIterator);  
        }
        catch (Exception e){
            manager.utils.error(e.toString());
        }
    }
    
}

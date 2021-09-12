/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.managers;


import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.model.CyNetworkView;
import java.util.Set;
import org.cytoscape.task.read.LoadVizmapFileTaskFactory;
import it.uniroma2.signor.app.internal.ConfigResources;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.view.model.CyNetworkViewManager;

public class SignorStyleManager {
    final VisualMappingManager vmm;
    private final SignorManager manager;
    private final String filename;
  
    public SignorStyleManager(SignorManager manager, String filename){
        this.manager = manager;
        this.filename = filename;
        vmm = manager.utils.getService(VisualMappingManager.class);
    } 

    public void setupDefaultStyle(){
        Boolean newStyle = true;
        for (VisualStyle createdStyle : vmm.getAllVisualStyles()) {
            if (createdStyle.getTitle().equals(ConfigResources.SIGNOR_VER_STYLE)) {
                newStyle = false;                
                break;
            }
        }
        if(newStyle) {
            manager.utils.info("Loading SIGNOR file style "+filename);
            Set<VisualStyle> vsSet  = manager.utils.getService(LoadVizmapFileTaskFactory.class).loadStyles(getClass().getResourceAsStream(filename));
        }              
    }
    public void applyStyle(CyNetworkView view){
        for (VisualStyle createdStyle : vmm.getAllVisualStyles()) {
            if (createdStyle.getTitle().equals(ConfigResources.SIGNOR_VER_STYLE)) { 
                vmm.setVisualStyle(createdStyle, view);
                createdStyle.apply(view);
                view.updateView();           
                break;
            }
        }        
    }
    public void installView(CyNetworkView view){
        manager.utils.getService(CyNetworkViewManager.class).addNetworkView(view);
        manager.utils.getService(CyApplicationManager.class).setCurrentNetworkView(view);
    }
}

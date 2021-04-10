/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.managers;
import it.uniroma2.signor.internal.style.SignorPTMStyle;

import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.model.CyNetworkView;
import it.uniroma2.signor.internal.managers.SignorManager;
import java.io.File;
import java.util.Set;
import java.util.Iterator;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.task.read.LoadVizmapFileTaskFactory;
import it.uniroma2.signor.internal.Config;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.view.model.CyNetworkViewManager;

/**
 *
 * @author amministratore
 */
public class SignorStyleManager {
    final VisualMappingManager vmm;
    private final SignorManager manager;
    private String filename;
    private VisualStyle visualStyle;
    //private final SignorManager signormanager;
    //final CyServiceRegistrar registrar;
    public SignorStyleManager(SignorManager manager, String filename){
        //this.signormanager=sm;
        this.manager = manager;
        this.filename = filename;
        vmm = manager.utils.getService(VisualMappingManager.class);
    } 

    public void setupDefaultStyle(){
        //DefaultSignorStyle dfsm = new SignorPTMStyle(sm, filename);        
        Boolean newStyle = true;
        for (VisualStyle createdStyle : vmm.getAllVisualStyles()) {
            if (createdStyle.getTitle().equals(Config.SIGNOR_VER_STYLE)) {
                newStyle = false;                
                break;
            }
        }
        if(newStyle) {
            manager.utils.info("Loading file style "+filename);
            Set<VisualStyle> vsSet  = manager.utils.getService(LoadVizmapFileTaskFactory.class).loadStyles(getClass().getResourceAsStream(filename));
        }              
    }
    public void applyStyle(CyNetworkView view){
        for (VisualStyle createdStyle : vmm.getAllVisualStyles()) {
            if (createdStyle.getTitle().equals(Config.SIGNOR_VER_STYLE)) { 
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
        manager.lastCyNetworkView = view;
    }
}

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

import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.ConfigPathway;
import it.uniroma2.signor.internal.ConfigResources;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.utils.IconUtils;
import it.uniroma2.signor.internal.utils.HttpUtils;
import it.uniroma2.signor.internal.ui.components.SearchPTHQueryComponent;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.task.query.SignorGenericQueryTask;
import it.uniroma2.signor.internal.task.query.SignorPathwayResultTask;
import it.uniroma2.signor.internal.ui.components.ChoosePathwayoption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 *
 * @author amministratore
 */
public class SignorPathwayQueryFactory extends AbstractNetworkSearchTaskFactory {

    static URL SIGNOR_URL;
    private static final Icon SIGNOR_ICON=IconUtils.createImageIcon(ConfigResources.icon_path);
    
    static {
        try {
            SIGNOR_URL = new URL(Config.SIGNOR_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    SignorManager manager;
    private ChoosePathwayoption choosePathwayoption;
    private SearchPTHQueryComponent searchPTHQueryComponent; 
    public HashMap<String, Object> parameters_shift;
    public boolean param_shift = false;
    
    
    public SignorPathwayQueryFactory(SignorManager manager) {
         super(ConfigPathway.SIGNORPTH_ID, ConfigPathway.SIGNORPTH_NAME,ConfigPathway.SIGNORPTH_DESC, SIGNOR_ICON, SIGNOR_URL);
         this.manager = manager;
         choosePathwayoption = new ChoosePathwayoption(manager);
         searchPTHQueryComponent = new SearchPTHQueryComponent(manager); 
    }
    public boolean isReady() {

        return choosePathwayoption.isReady();        
    }

    public TaskIterator createTaskIterator() {            
        HashMap<String, Object> parameters = choosePathwayoption.getParameter();
        //I'm calling the task from option panel
        if (this.param_shift == true) parameters = parameters_shift;
        String pathway= (String) parameters.entrySet().iterator().next().getValue();
        String pathwayid = "";
        if(ConfigPathway.PathwayDiseaseList.containsValue(pathway))
                pathwayid = ConfigPathway.PathwayDiseaseList.entrySet().stream()
                            .filter(entry -> entry.getValue().equals(pathway))
                            .map(entry-> entry.getKey())
                            .collect(Collectors.joining());
            
            else if (ConfigPathway.PathwayList.containsValue(pathway))
                pathwayid = ConfigPathway.PathwayList.entrySet().stream()
                            .filter(entry -> entry.getValue().equals(pathway))
                            .map(entry-> entry.getKey())
                            .collect(Collectors.joining());
            
            else if (ConfigPathway.PathwayTumorList.containsValue(pathway))
                pathwayid = ConfigPathway.PathwayTumorList.entrySet().stream()
                            .filter(entry -> entry.getValue().equals(pathway))
                            .map(entry-> entry.getKey())
                            .collect(Collectors.joining());
        parameters.clear();
        parameters.put(ConfigPathway.PATHWAYID, pathwayid);
        try { 
            /*parameters = chooseSearchoption.getParameter();
            parameters.put("QUERY", pathway);*/
            manager.utils.info("Performing SIGNOR PTH search for "+parameters.toString()); 
            return new TaskIterator(new SignorPathwayResultTask(new Network(manager, parameters), pathwayid)); 
        }
        catch (Exception e){
            manager.utils.error("Problems in performing SIGNOR PTH search "+e.toString());
        }
        return null;
    }

    public JComponent getQueryComponent() {
        return searchPTHQueryComponent;
    }    
    @Override
    public JComponent getOptionsComponent() {        
        return choosePathwayoption;
    }

}

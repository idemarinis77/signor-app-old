/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.task.query.factories;
import org.cytoscape.application.swing.search.AbstractNetworkSearchTaskFactory;
import org.cytoscape.work.TaskIterator;
import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.Icon;
import it.uniroma2.signor.app.internal.ConfigResources;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import it.uniroma2.signor.app.internal.utils.IconUtils;
import it.uniroma2.signor.app.internal.ui.components.SearchPTHQueryComponent;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.NetworkField;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.NetworkSearch;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Pathway.PathwayField;
import it.uniroma2.signor.app.internal.task.query.SignorPathwayResultTask;
import it.uniroma2.signor.app.internal.ui.components.ChoosePathwayoption;
import java.util.HashMap;

public class SignorPathwayQueryFactory extends AbstractNetworkSearchTaskFactory {

    static URL SIGNOR_URL;
    private static final Icon SIGNOR_ICON=IconUtils.createImageIcon(ConfigResources.icon_path_pathway);
    public static final String SIGNORPTH_NAME = "SIGNOR pathway query";
    public static final String SIGNORPTH_DESC = "Query SIGNOR Database to visualize pathway";
    public static final String SIGNORPTH_ID = "SIGNOR_PTH_ID_SEARCH_FACTORY"; 
    static {
        try {
            SIGNOR_URL = new URL(ConfigResources.SIGNOR_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    SignorManager manager;
    private final ChoosePathwayoption choosePathwayoption;
    private final SearchPTHQueryComponent searchPTHQueryComponent; 
    public HashMap<String, Object> parameters_shift;
    public boolean param_shift = false;
    
    
    public SignorPathwayQueryFactory(SignorManager manager) {
         super(SIGNORPTH_ID, SIGNORPTH_NAME,SIGNORPTH_DESC, SIGNOR_ICON, SIGNOR_URL);
         this.manager = manager;
         choosePathwayoption = new ChoosePathwayoption(manager);
         searchPTHQueryComponent = new SearchPTHQueryComponent(manager); 
    }
    
    @Override
    public boolean isReady() {

        return choosePathwayoption.isReady();        
    }
    @Override
    public TaskIterator createTaskIterator() {            
        HashMap<String, Object> parameters = choosePathwayoption.getParameter();
        //I'm calling the task from option panel
        if (this.param_shift == true) parameters = parameters_shift;
        String pathwayid = (String) parameters.get(PathwayField.PATHWAYID);
        parameters.put(PathwayField.PATHWAYID, pathwayid);
        parameters.put(NetworkField.SPECIES, "Homo Sapiens");
        manager.utils.info(parameters.toString());
        HashMap <String, Object> buildParams = NetworkSearch.buildSearch(pathwayid, (String) parameters.get(NetworkField.SPECIES), 
                                                        NetworkField.PATHWAYSEARCH, false);
        try { 
            manager.utils.info("Performing SIGNOR PATHWAY search for "+parameters.toString()); 
            return new TaskIterator(new SignorPathwayResultTask(new Network(manager, buildParams), pathwayid)); 
        }
        catch (Exception e){
            manager.utils.error("Problems in performing SIGNOR PATHWAY search "+e.toString());
        }
        return null;
    }

    @Override
    public JComponent getQueryComponent() {
        return searchPTHQueryComponent;
    }    
    @Override
    public JComponent getOptionsComponent() {        
        return choosePathwayoption;
    }

}

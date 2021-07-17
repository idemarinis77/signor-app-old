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
import it.uniroma2.signor.internal.ConfigResources;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.*;
import it.uniroma2.signor.internal.task.query.SignorInteractomeTask;
import it.uniroma2.signor.internal.ui.components.SearchIntearctomeQueryComponent;
import static it.uniroma2.signor.internal.task.query.factories.SignorPathwayQueryFactory.SIGNORPTH_DESC;
//import static it.uniroma2.signor.internal.task.query.factories.SignorPathwayQueryFactory.SIGNORPTH_ID;
//import static it.uniroma2.signor.internal.task.query.factories.SignorPathwayQueryFactory.SIGNORPTH_NAME;
//import static it.uniroma2.signor.internal.task.query.factories.SignorPathwayQueryFactory.SIGNOR_URL;
import it.uniroma2.signor.internal.utils.IconUtils;
import java.util.HashMap;
import org.cytoscape.work.AbstractTaskFactory;

/**
 *
 * @author amministratore
 */
public class SignorInteractomeQueryFactory extends AbstractNetworkSearchTaskFactory{

    static URL SIGNOR_URL;
    private static final Icon SIGNOR_ICON=IconUtils.createImageIcon(ConfigResources.icon_path);
    public static final String SIGNORINT_NAME = "Signor interactome query";
    public static final String SIGNORINT_DESC = "Query SIGNOR Database to retrieve complete interactome";
    public static final String SIGNORINT_ID = "SIGNOR_INT_ID_SEARCH_FACTORY"; 
    static {
        try {
            SIGNOR_URL = new URL(ConfigResources.SIGNOR_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    SignorManager manager;
    Boolean ptm_interactome;
    HashMap <String, Object> parameters = new HashMap();
    private SearchIntearctomeQueryComponent searchIntearctomeQueryComponent;
    
    public SignorInteractomeQueryFactory(SignorManager manager, Boolean ptm_interactome) {
         super(SIGNORINT_ID, SIGNORINT_NAME, SIGNORINT_DESC, SIGNOR_ICON, SIGNOR_URL);
         this.manager = manager;        
         this.ptm_interactome = ptm_interactome;
         this.searchIntearctomeQueryComponent = new SearchIntearctomeQueryComponent(manager);
    }
    
    public boolean isReady() {

        return true;        
    }
    public TaskIterator createTaskIterator() {            
            parameters =NetworkSearch.buildSearch(Config.INTERACTOMENAME, "Homo Sapiens", "", false);
            return new TaskIterator(new SignorInteractomeTask(new Network(manager, parameters), ptm_interactome)); 
        
    }

    public JComponent getQueryComponent() {
        return searchIntearctomeQueryComponent;
    }    
    @Override
    public JComponent getOptionsComponent() {        
        return null;
    }

}

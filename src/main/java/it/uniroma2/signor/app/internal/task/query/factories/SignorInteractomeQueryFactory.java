/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.task.query.factories;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.*;
import org.cytoscape.application.swing.search.AbstractNetworkSearchTaskFactory;
import org.cytoscape.work.TaskIterator;
import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.Icon;
import it.uniroma2.signor.app.internal.Config;
import it.uniroma2.signor.app.internal.ConfigResources;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import it.uniroma2.signor.app.internal.task.query.SignorInteractomeTask;
import it.uniroma2.signor.app.internal.ui.components.SearchIntearctomeQueryComponent;
import it.uniroma2.signor.app.internal.utils.IconUtils;
import java.util.HashMap;

public class SignorInteractomeQueryFactory extends AbstractNetworkSearchTaskFactory{

    static URL SIGNOR_URL;
    private static final Icon SIGNOR_ICON=IconUtils.createImageIcon(ConfigResources.icon_path_interactome);
    public static final String SIGNORINT_NAME = "SIGNOR download interactome";
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
    private final SearchIntearctomeQueryComponent searchIntearctomeQueryComponent;
    
    public SignorInteractomeQueryFactory(SignorManager manager, Boolean ptm_interactome) {
         super(SIGNORINT_ID, SIGNORINT_NAME, SIGNORINT_DESC, SIGNOR_ICON, SIGNOR_URL);
         this.manager = manager;        
         this.ptm_interactome = ptm_interactome;
         this.searchIntearctomeQueryComponent = new SearchIntearctomeQueryComponent(manager);
    }
    
    @Override
    public boolean isReady() {

        return true;        
    }
    
    @Override
    public TaskIterator createTaskIterator() {            
            parameters =NetworkSearch.buildSearch(Config.INTERACTOMENAME, "Homo Sapiens", "", false);
            return new TaskIterator(new SignorInteractomeTask(new Network(manager, parameters), ptm_interactome)); 
        
    }
    @Override
    public JComponent getQueryComponent() {
        return searchIntearctomeQueryComponent;
    }    
    @Override
    public JComponent getOptionsComponent() {        
        return null;
    }

}

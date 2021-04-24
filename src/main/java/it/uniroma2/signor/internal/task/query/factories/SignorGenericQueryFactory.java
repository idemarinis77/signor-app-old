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
import it.uniroma2.signor.internal.utils.IconUtils;
import it.uniroma2.signor.internal.ui.components.SearchQueryComponent;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.task.query.SignorGenericQueryTask;
import it.uniroma2.signor.internal.ui.components.ChooseSearchoption;
import java.util.HashMap;

/**
 *
 * @author amministratore
 */
public class SignorGenericQueryFactory extends AbstractNetworkSearchTaskFactory {

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
    private SearchQueryComponent queryComponent = null;
    private ChooseSearchoption chooseSearchoption = new ChooseSearchoption(manager); 
    
    
    public SignorGenericQueryFactory(SignorManager manager) {
         super(Config.SIGNOR_ID, Config.SIGNOR_NAME,Config.SIGNOR_DESC, SIGNOR_ICON, SIGNOR_URL);
         this.manager = manager;
    }
    public boolean isReady() {
        return queryComponent.getQueryText() != null && queryComponent.getQueryText().length() > 0;
    }

    public TaskIterator createTaskIterator() {
        String terms = queryComponent.getQueryText();
        HashMap<String, Object> parameters;
  
        try { 
            parameters = chooseSearchoption.getParameter();
            parameters.put("QUERY", terms);
            manager.utils.info("Performing SIGNOR search for "+parameters.toString()); 
            return new TaskIterator(new SignorGenericQueryTask(new Network(manager, parameters), Config.SIGNOR_NAME, parameters, terms));
        }
        catch (Exception e){
            manager.utils.error("Problems in performing SIGNOR search "+e.toString());
        }
        return null;
    }

    public JComponent getQueryComponent() {
        if (queryComponent == null)
            queryComponent = new SearchQueryComponent();
        return queryComponent;
    }

    @Override
    public JComponent getOptionsComponent() {        
        return chooseSearchoption;
    }

}

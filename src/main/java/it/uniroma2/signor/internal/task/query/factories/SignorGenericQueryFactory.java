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
    static String SIGNOR_ID="it.uniroma2.signor";
    static String SIGNOR_NAME = "Signor single query";
    static String SIGNOR_DESC = "Query SIGNOR Database to create your causal network";
    static URL SIGNOR_URL;
    private static final Icon SIGNOR_ICON=IconUtils.createImageIcon("/images/signor_logo.png");
    
    static {
        try {
            SIGNOR_URL = new URL("https://signor.uniroma2.it");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    SignorManager manager;
    private SearchQueryComponent queryComponent = null;
    private ChooseSearchoption chooseSearchoption = new ChooseSearchoption(manager); 
    
    
    public SignorGenericQueryFactory(SignorManager manager) {
         super(SIGNOR_ID, SIGNOR_NAME,SIGNOR_DESC, SIGNOR_ICON, SIGNOR_URL);
         this.manager = manager;
    }
    public boolean isReady() {
        return queryComponent.getQueryText() != null && queryComponent.getQueryText().length() > 0;
    }

    public TaskIterator createTaskIterator() {
        String terms = queryComponent.getQueryText();
        String searchoptions = "";
        Boolean firstneighbor=false;
        HashMap<String, ?> parameters;
  
        try { 
            parameters = chooseSearchoption.getParameter();
            manager.utils.info("Performing SIGNOR search for "+parameters.toString()); 
            return new TaskIterator(new SignorGenericQueryTask(new Network(manager, parameters), SIGNOR_NAME, parameters, terms));
        }
        catch (Exception e){
            manager.utils.error("non riesco a "+e.getMessage()+" "+e.toString());
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

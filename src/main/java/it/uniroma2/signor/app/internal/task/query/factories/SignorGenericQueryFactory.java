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
import it.uniroma2.signor.app.internal.ui.components.SearchQueryComponent;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.NetworkSearch;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.NetworkField;
import it.uniroma2.signor.app.internal.task.query.SignorGenericQueryTask;
import it.uniroma2.signor.app.internal.ui.components.ChooseSearchoption;
import java.util.HashMap;

public class SignorGenericQueryFactory extends AbstractNetworkSearchTaskFactory {

    static URL SIGNOR_URL;
    private static final Icon SIGNOR_ICON=IconUtils.createImageIcon(ConfigResources.icon_path_search);
    public static final String SIGNOR_ID = "SIGNOR_ID_SEARCH_FACTORY";
    public static final String SIGNOR_NAME = "SIGNOR entity query and connect";
    public static final String SIGNOR_DESC = "Query SIGNOR Database to create your casual network";
    static {
        try {
            SIGNOR_URL = new URL(ConfigResources.SIGNOR_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    SignorManager manager;
    private SearchQueryComponent queryComponent = null;
    private final ChooseSearchoption chooseSearchoption = new ChooseSearchoption(manager); 
    
    
    public SignorGenericQueryFactory(SignorManager manager) {
         super(SIGNOR_ID, SIGNOR_NAME,SIGNOR_DESC, SIGNOR_ICON, SIGNOR_URL);
         this.manager = manager;
    }
    
    @Override
    public boolean isReady() {
        return queryComponent.getQueryText() != null && queryComponent.getQueryText().length() > 0;
    }

    public TaskIterator createTaskIterator() {
        String terms = queryComponent.getQueryText();
        String terms_no_space=terms.replace("\n", " ").trim();
        String terms_for_all = terms_no_space.toUpperCase();
        
        HashMap<String, Object> parameters;
        HashMap <String, Object> buildParams = new HashMap ();
        try { 
            parameters = chooseSearchoption.getParameter();
//            parameters.put(NetworkField.QUERY, terms_for_all);
            if(parameters.get(NetworkField.ALLSEARCH).equals(true)){
                buildParams = NetworkSearch.buildSearch(terms_for_all, (String) parameters.get(NetworkField.SPECIES), 
                                                        NetworkField.ALLSEARCH, false);
            }
            else if(parameters.get(NetworkField.CONNECTSEARCH).equals(true)){
                buildParams = NetworkSearch.buildSearch(terms_for_all, (String) parameters.get(NetworkField.SPECIES), 
                                                        NetworkField.CONNECTSEARCH, (Boolean) parameters.get(NetworkField.INCFIRSTNEISEARCH));
            }
            else if(parameters.get(NetworkField.INCFIRSTNEISEARCH).equals(true)){
                buildParams = NetworkSearch.buildSearch(terms_for_all, (String) parameters.get(NetworkField.SPECIES), 
                                                        NetworkField.INCFIRSTNEISEARCH, true);
            }
            else if(parameters.get(NetworkField.SHORTESTPATHSEARCH).equals(true)){
                buildParams = NetworkSearch.buildSearch(terms_for_all, (String) parameters.get(NetworkField.SPECIES), 
                                                        NetworkField.SHORTESTPATHSEARCH, false);
            }
            else if(parameters.get(NetworkField.SINGLESEARCH).equals(true)){
                buildParams = NetworkSearch.buildSearch(terms_for_all, (String) parameters.get(NetworkField.SPECIES), NetworkField.SINGLESEARCH, 
                        (Boolean) parameters.get(NetworkField.INCFIRSTNEISEARCH));
            }
            manager.utils.info("Performing SIGNOR search for buildParams "+buildParams.toString());
            return new TaskIterator(new SignorGenericQueryTask(new Network(manager, buildParams), SIGNOR_NAME, buildParams, terms_for_all));
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

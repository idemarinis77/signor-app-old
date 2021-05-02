/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.task.query.factories;
import it.uniroma2.signor.internal.task.query.CreateNetworkTask;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import it.uniroma2.signor.internal.ConfigResources;
import it.uniroma2.signor.internal.Config;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 *
 * @author amministratore
 */
public class SignorGenericRetrieveResultFactory extends AbstractTaskFactory{
    private String search;
    private Boolean includefirstneighbor;
    private String species;
    private String terms;
    private Network network;
    private HashMap<String, ?> parameters;
    
    public SignorGenericRetrieveResultFactory(String search, Boolean includefirstneighbor, String species, 
            String terms, Network network){
        this.search = search;
        this.includefirstneighbor = includefirstneighbor; 
        this.species = species;
        this.terms = terms;
        this.network=network;
    }
    
    public TaskIterator createTaskIterator() {
        String netName = Config.NTWPREFIX+terms;
        String URL = ConfigResources.WSSearchoptionMAP.get(search).queryFunction.apply(Config.SPECIESLIST.get(species), terms); 
        if(includefirstneighbor)
            URL = ConfigResources.WSSearchoptionMAP.get(search).queryFunction.apply(terms, "3");
        else if(search == Config.CONNECTSEARCH)
            URL = ConfigResources.WSSearchoptionMAP.get(search).queryFunction.apply(terms, "1");       
        else if(search == Config.ALLSEARCH)
            URL = ConfigResources.WSSearchoptionMAP.get(search).queryFunction.apply(terms, "2");           
        this.network.manager.utils.info("SignorGenericRetrieveResultFactory createTaskIterator(), retrieving info from"+URL+netName);
        return new TaskIterator(new CreateNetworkTask(network, terms, URL, netName, parameters)); 
   }  

    //public boolean isReady() { return true; }
}

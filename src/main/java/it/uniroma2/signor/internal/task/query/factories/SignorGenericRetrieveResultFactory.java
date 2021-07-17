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
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.NetworkField;
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
        network.manager.utils.info("I parametri sono questi "+network.parameters.toString());
        String netName = Config.NTWPREFIX+terms;
        String URL = ConfigResources.WSSearchoptionMAP.get(search).queryFunction.apply(Config.SPECIESLIST.get(species), terms);                 
        if(includefirstneighbor)
            URL = ConfigResources.WSSearchoptionMAP.get(search).queryFunction.apply(terms, "2");
        else if(search == NetworkField.CONNECTSEARCH)
            URL = ConfigResources.WSSearchoptionMAP.get(search).queryFunction.apply(terms, "1"); 
        else if(search == NetworkField.ALLSEARCH)
            URL = ConfigResources.WSSearchoptionMAP.get(search).queryFunction.apply(terms, "3");           
        else if(search == NetworkField.SHORTESTPATHSEARCH){
            String terms_no_space = terms.replace("%2C", " ").trim();
            String start = terms_no_space.split(" ")[0];
            String end = terms_no_space.split(" ")[1];
            URL = ConfigResources.WSSearchoptionMAP.get(search).queryFunction.apply(start, end);
            URL = URL + Config.SPECIESSHORTESTPATH.get(network.parameters.get(NetworkField.SPECIES));
            this.network.manager.utils.info("SPECIESSHORTESTPATH "+network.parameters.get(NetworkField.SPECIES)+
                    Config.SPECIESSHORTESTPATH.get(network.parameters.get(NetworkField.SPECIES)));
        }
        this.network.manager.utils.info("SignorGenericRetrieveResultFactory createTaskIterator(), retrieving info from"+URL+netName);
        //Bridged proteins searched, shortest path and all provides result only with uppercase terms
        //Single protein search provides result with any case
        return new TaskIterator(new CreateNetworkTask(network, terms.toUpperCase(), URL, netName, parameters)); 
   }  

    //public boolean isReady() { return true; }
}

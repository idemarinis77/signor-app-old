/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.task.query.factories;
import it.uniroma2.signor.app.internal.task.query.CreateNetworkTask;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.Network;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.NetworkField;
import it.uniroma2.signor.app.internal.ConfigResources;
import it.uniroma2.signor.app.internal.Config;
import java.util.HashMap;


public class SignorGenericRetrieveResultFactory extends AbstractTaskFactory{
    private final String search;
    private final Boolean includefirstneighbor;
    private final String species;
    private final String terms;
    private final Network network;
    private HashMap<String, ?> parameters;
    
    public SignorGenericRetrieveResultFactory(String search, Boolean includefirstneighbor, String species, 
            String terms, Network network){
        this.search = search;
        this.includefirstneighbor = includefirstneighbor; 
        this.species = species;
        this.terms = terms;
        this.network=network;
    }
    
    @Override
    public TaskIterator createTaskIterator() {
        network.manager.utils.info("Your search parameters are "+network.parameters.toString()+" you can find them in Network table");
        String terms_upper_case =terms.toUpperCase(); 
        String terms_for_netname=terms_upper_case.replace("%2C", " ").trim();
        String netName = Config.NTWPREFIX+terms_for_netname;
        String code_species ="";
        String URL = ConfigResources.WSSearchoptionMAP.get(search).queryFunction.apply(Config.SPECIESLIST.get(species), terms_upper_case);                 
        if(!Config.SPECIESLIST.get(species).equals("9606")){
            code_species = "&organism="+Config.SPECIESLIST.get(species);
        }
        if(includefirstneighbor)
            URL = ConfigResources.WSSearchoptionMAP.get(search).queryFunction.apply(terms_upper_case, "2")+code_species;
        else if(search.equals(NetworkField.CONNECTSEARCH))
            URL = ConfigResources.WSSearchoptionMAP.get(search).queryFunction.apply(terms_upper_case, "1")+code_species; 
        else if(search.equals(NetworkField.ALLSEARCH))
            URL = ConfigResources.WSSearchoptionMAP.get(search).queryFunction.apply(terms_upper_case, "3")+code_species;           
        else if(search.equals(NetworkField.SHORTESTPATHSEARCH)){
            String terms_no_space = terms_upper_case.replace("%2C", " ").trim();
            String start = terms_no_space.split(" ")[0];
            String end = terms_no_space.split(" ")[1];
            URL = ConfigResources.WSSearchoptionMAP.get(search).queryFunction.apply(start, end);
            URL = URL + Config.SPECIESSHORTESTPATH.get(network.parameters.get(NetworkField.SPECIES));
            this.network.manager.utils.info("SPECIESSHORTESTPATH "+network.parameters.get(NetworkField.SPECIES)+
                    Config.SPECIESSHORTESTPATH.get(network.parameters.get(NetworkField.SPECIES)));
        }
        //Bridged proteins searched, shortest path and all provides result only with uppercase terms
        //Single protein search provides result with any case
        return new TaskIterator(new CreateNetworkTask(network, terms_upper_case, URL, netName, parameters)); 
   }  
}

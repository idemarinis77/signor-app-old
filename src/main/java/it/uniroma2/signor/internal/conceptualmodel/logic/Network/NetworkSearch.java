/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.conceptualmodel.logic.Network;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.NetworkField;
import it.uniroma2.signor.internal.Config;
import java.util.*;

/**
 *
 * @author amministratore
 */
public class NetworkSearch {
//    private String type;
//    
//    public NetworkSearch(String type){
//        this.type = type;        
//    }
//    
    public static HashMap<String, Object> buildSearch(String query, String organism, String type, Boolean include_f_n){
        //SINGLE
        //ALL
        //CONNECT
          //INCLUDE FIRST NEIGHBOR
        //SHORTESTPATH
        //PATHWAY
        //INTERACTOME
        //ISPATHWAY
        //ISDISEASE
        //PTMLOADED
        //
        //This method return an HashMap built (filled) coherently to the search type
        /*QUERY=Q9GZM8, CONNECTSEARCH=false, SPECIES=Homo Sapiens, 
          ALLSEARCH=false, SHORTESTPATHSEARCH=false, INCFIRSTNEISEARCH=false, SINGLESEARCH=true*/ 
        HashMap<String, Object> buildSearchParams = new HashMap();
        if(type == NetworkField.SINGLESEARCH){
            buildSearchParams.put(NetworkField.SINGLESEARCH, true);
            buildSearchParams.put(NetworkField.ALLSEARCH, false);
            buildSearchParams.put(NetworkField.CONNECTSEARCH, false);
            buildSearchParams.put(NetworkField.SHORTESTPATHSEARCH, false);
            buildSearchParams.put(NetworkField.PATHWAYSEARCH, false);
            buildSearchParams.put(NetworkField.DISEASESEARCH, false);
            buildSearchParams.put(NetworkField.QUERY, query);
            buildSearchParams.put(NetworkField.INCFIRSTNEISEARCH, false);
            buildSearchParams.put(NetworkField.SPECIES, organism);
            buildSearchParams.put(NetworkField.PATHWAYID, "");
            buildSearchParams.put(NetworkField.PTMLOADED, false);
            buildSearchParams.put(NetworkField.VIEW, false);
            return buildSearchParams;
        }
        else if(type == NetworkField.ALLSEARCH){
            buildSearchParams.put(NetworkField.SINGLESEARCH, false);
            buildSearchParams.put(NetworkField.ALLSEARCH, true);
            buildSearchParams.put(NetworkField.CONNECTSEARCH, false);
            buildSearchParams.put(NetworkField.SHORTESTPATHSEARCH, false);
            buildSearchParams.put(NetworkField.PATHWAYSEARCH, false);
            buildSearchParams.put(NetworkField.DISEASESEARCH, false);
            buildSearchParams.put(NetworkField.QUERY, query);
            buildSearchParams.put(NetworkField.INCFIRSTNEISEARCH, false);
            buildSearchParams.put(NetworkField.SPECIES, organism);
            buildSearchParams.put(NetworkField.PATHWAYID, "");
            buildSearchParams.put(NetworkField.PTMLOADED, false);
            buildSearchParams.put(NetworkField.VIEW, false);
            return buildSearchParams;
        }
        else if(type == NetworkField.CONNECTSEARCH){
            buildSearchParams.put(NetworkField.SINGLESEARCH, false);
            buildSearchParams.put(NetworkField.ALLSEARCH, false);
            buildSearchParams.put(NetworkField.CONNECTSEARCH, true);
            buildSearchParams.put(NetworkField.SHORTESTPATHSEARCH, false);
            buildSearchParams.put(NetworkField.PATHWAYSEARCH, false);
            buildSearchParams.put(NetworkField.DISEASESEARCH, false);
            buildSearchParams.put(NetworkField.QUERY, query);
            buildSearchParams.put(NetworkField.INCFIRSTNEISEARCH, include_f_n);
            buildSearchParams.put(NetworkField.SPECIES, organism);
            buildSearchParams.put(NetworkField.PATHWAYID, "");
            buildSearchParams.put(NetworkField.PTMLOADED, false);
            buildSearchParams.put(NetworkField.VIEW, false);
            return buildSearchParams;
        }
         else if(type == NetworkField.SHORTESTPATHSEARCH){
            buildSearchParams.put(NetworkField.SINGLESEARCH, false);
            buildSearchParams.put(NetworkField.ALLSEARCH, false);
            buildSearchParams.put(NetworkField.CONNECTSEARCH, false);
            buildSearchParams.put(NetworkField.SHORTESTPATHSEARCH, true);
            buildSearchParams.put(NetworkField.PATHWAYSEARCH, false);
            buildSearchParams.put(NetworkField.DISEASESEARCH, false);
            buildSearchParams.put(NetworkField.QUERY, query);
            buildSearchParams.put(NetworkField.INCFIRSTNEISEARCH, false);
            buildSearchParams.put(NetworkField.SPECIES, organism);
            buildSearchParams.put(NetworkField.PATHWAYID, "");
            buildSearchParams.put(NetworkField.PTMLOADED, false);
            buildSearchParams.put(NetworkField.VIEW, false);
            return buildSearchParams;
        }
        else if(query == Config.INTERACTOMENAME){
            buildSearchParams.put(NetworkField.SINGLESEARCH, false);
            buildSearchParams.put(NetworkField.ALLSEARCH, false);
            buildSearchParams.put(NetworkField.CONNECTSEARCH, false);
            buildSearchParams.put(NetworkField.SHORTESTPATHSEARCH, true);
            buildSearchParams.put(NetworkField.PATHWAYSEARCH, false);
            buildSearchParams.put(NetworkField.DISEASESEARCH, false);
            buildSearchParams.put(NetworkField.QUERY, query);
            buildSearchParams.put(NetworkField.INCFIRSTNEISEARCH, false);
            buildSearchParams.put(NetworkField.SPECIES, organism);
            buildSearchParams.put(NetworkField.PATHWAYID, "");
            buildSearchParams.put(NetworkField.PTMLOADED, false);
            buildSearchParams.put(NetworkField.VIEW, false);
            return buildSearchParams;
        }
        else if(type == NetworkField.PATHWAYSEARCH) {
            buildSearchParams.put(NetworkField.SINGLESEARCH, false);
            buildSearchParams.put(NetworkField.ALLSEARCH, false);
            buildSearchParams.put(NetworkField.CONNECTSEARCH, false);
            buildSearchParams.put(NetworkField.SHORTESTPATHSEARCH, false);
            buildSearchParams.put(NetworkField.PATHWAYSEARCH, true);
            buildSearchParams.put(NetworkField.DISEASESEARCH, false);
            buildSearchParams.put(NetworkField.QUERY, query);
            buildSearchParams.put(NetworkField.INCFIRSTNEISEARCH, false);
            buildSearchParams.put(NetworkField.SPECIES, organism);
            buildSearchParams.put(NetworkField.PATHWAYID, query);
            buildSearchParams.put(NetworkField.PTMLOADED, false);
            buildSearchParams.put(NetworkField.VIEW, false);
            return buildSearchParams;
        }
        return null;
    }
    
    
    
}

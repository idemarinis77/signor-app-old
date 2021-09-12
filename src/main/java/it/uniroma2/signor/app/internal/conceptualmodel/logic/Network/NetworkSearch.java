/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.conceptualmodel.logic.Network;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.NetworkField;
import it.uniroma2.signor.app.internal.Config;
import java.util.*;
import org.cytoscape.model.CyRow;
import it.uniroma2.signor.app.internal.view.NetworkView;
import it.uniroma2.signor.app.internal.managers.SignorManager;

public class NetworkSearch {
 
    public static HashMap<String, Object> buildSearch(String querylc, String organism, String type, Boolean include_f_n){
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
        /*e.g. QUERY=Q9GZM8, CONNECTSEARCH=false, SPECIES=Homo Sapiens, 
          ALLSEARCH=false, SHORTESTPATHSEARCH=false, INCFIRSTNEISEARCH=false, SINGLESEARCH=true*/ 
        String queryuc = querylc.toUpperCase();
        HashMap<String, Object> buildSearchParams = new HashMap();
        if(type.equals(NetworkField.SINGLESEARCH)){
            buildSearchParams.put(NetworkField.SINGLESEARCH, true);
            buildSearchParams.put(NetworkField.ALLSEARCH, false);
            buildSearchParams.put(NetworkField.CONNECTSEARCH, false);
            buildSearchParams.put(NetworkField.SHORTESTPATHSEARCH, false);
            buildSearchParams.put(NetworkField.PATHWAYSEARCH, false);
            buildSearchParams.put(NetworkField.PATHWAYINFO, "");
            buildSearchParams.put(NetworkField.DISEASESEARCH, false);
            buildSearchParams.put(NetworkField.QUERY, queryuc);
            buildSearchParams.put(NetworkField.INCFIRSTNEISEARCH, false);
            buildSearchParams.put(NetworkField.ENTITYNOTFOUND, "");
            buildSearchParams.put(NetworkField.SPECIES, organism);
            buildSearchParams.put(NetworkField.PATHWAYID, "");
            buildSearchParams.put(NetworkField.PTMLOADED, false);
            buildSearchParams.put(NetworkField.VIEW, NetworkView.Type.DEFAULT.name());
            buildSearchParams.put(NetworkField.ROOTNETWORKPTM, false);            
            return buildSearchParams;
        }
        else if(type.equals(NetworkField.ALLSEARCH)){
            buildSearchParams.put(NetworkField.SINGLESEARCH, false);
            buildSearchParams.put(NetworkField.ALLSEARCH, true);
            buildSearchParams.put(NetworkField.CONNECTSEARCH, false);
            buildSearchParams.put(NetworkField.SHORTESTPATHSEARCH, false);
            buildSearchParams.put(NetworkField.PATHWAYSEARCH, false);
            buildSearchParams.put(NetworkField.PATHWAYINFO, "");
            buildSearchParams.put(NetworkField.DISEASESEARCH, false);
            buildSearchParams.put(NetworkField.QUERY, queryuc);
            buildSearchParams.put(NetworkField.INCFIRSTNEISEARCH, false);
            buildSearchParams.put(NetworkField.ENTITYNOTFOUND, "");
            buildSearchParams.put(NetworkField.SPECIES, organism);
            buildSearchParams.put(NetworkField.PATHWAYID, "");
            buildSearchParams.put(NetworkField.PTMLOADED, false);
            buildSearchParams.put(NetworkField.VIEW, NetworkView.Type.DEFAULT.name());
            buildSearchParams.put(NetworkField.ROOTNETWORKPTM, false); 
            return buildSearchParams;
        }
        else if(type.equals(NetworkField.CONNECTSEARCH)){
            buildSearchParams.put(NetworkField.SINGLESEARCH, false);
            buildSearchParams.put(NetworkField.ALLSEARCH, false);
            buildSearchParams.put(NetworkField.CONNECTSEARCH, true);
            buildSearchParams.put(NetworkField.SHORTESTPATHSEARCH, false);
            buildSearchParams.put(NetworkField.PATHWAYSEARCH, false);
            buildSearchParams.put(NetworkField.PATHWAYINFO, "");
            buildSearchParams.put(NetworkField.DISEASESEARCH, false);
            buildSearchParams.put(NetworkField.QUERY, queryuc);
            buildSearchParams.put(NetworkField.INCFIRSTNEISEARCH, include_f_n);
            buildSearchParams.put(NetworkField.ENTITYNOTFOUND, "");
            buildSearchParams.put(NetworkField.SPECIES, organism);
            buildSearchParams.put(NetworkField.PATHWAYID, "");
            buildSearchParams.put(NetworkField.PTMLOADED, false);
            buildSearchParams.put(NetworkField.VIEW, NetworkView.Type.DEFAULT.name());
            buildSearchParams.put(NetworkField.ROOTNETWORKPTM, false); 
            return buildSearchParams;
        }
         else if(type.equals(NetworkField.SHORTESTPATHSEARCH)){
            buildSearchParams.put(NetworkField.SINGLESEARCH, false);
            buildSearchParams.put(NetworkField.ALLSEARCH, false);
            buildSearchParams.put(NetworkField.CONNECTSEARCH, false);
            buildSearchParams.put(NetworkField.SHORTESTPATHSEARCH, true);
            buildSearchParams.put(NetworkField.PATHWAYSEARCH, false);
            buildSearchParams.put(NetworkField.PATHWAYINFO, "");
            buildSearchParams.put(NetworkField.DISEASESEARCH, false);
            buildSearchParams.put(NetworkField.QUERY, queryuc);
            buildSearchParams.put(NetworkField.INCFIRSTNEISEARCH, false);
            buildSearchParams.put(NetworkField.ENTITYNOTFOUND, "");
            buildSearchParams.put(NetworkField.SPECIES, organism);
            buildSearchParams.put(NetworkField.PATHWAYID, "");
            buildSearchParams.put(NetworkField.PTMLOADED, false);
            buildSearchParams.put(NetworkField.VIEW, NetworkView.Type.DEFAULT.name());
            buildSearchParams.put(NetworkField.ROOTNETWORKPTM, false); 
            return buildSearchParams;
        }
        else if(querylc.equals(Config.INTERACTOMENAME)){
            buildSearchParams.put(NetworkField.SINGLESEARCH, false);
            buildSearchParams.put(NetworkField.ALLSEARCH, false);
            buildSearchParams.put(NetworkField.CONNECTSEARCH, false);
            buildSearchParams.put(NetworkField.SHORTESTPATHSEARCH, false);
            buildSearchParams.put(NetworkField.PATHWAYSEARCH, false);
            buildSearchParams.put(NetworkField.PATHWAYINFO, "");
            buildSearchParams.put(NetworkField.DISEASESEARCH, false);
            buildSearchParams.put(NetworkField.QUERY, queryuc);
            buildSearchParams.put(NetworkField.INCFIRSTNEISEARCH, false);
            buildSearchParams.put(NetworkField.ENTITYNOTFOUND, "");
            buildSearchParams.put(NetworkField.SPECIES, organism);
            buildSearchParams.put(NetworkField.PATHWAYID, "");
            buildSearchParams.put(NetworkField.PTMLOADED, false);
            buildSearchParams.put(NetworkField.VIEW, NetworkView.Type.DEFAULT.name());
            buildSearchParams.put(NetworkField.ROOTNETWORKPTM, false); 
            return buildSearchParams;
        }
        else if(type.equals(NetworkField.PATHWAYSEARCH)) {
            buildSearchParams.put(NetworkField.SINGLESEARCH, false);
            buildSearchParams.put(NetworkField.ALLSEARCH, false);
            buildSearchParams.put(NetworkField.CONNECTSEARCH, false);
            buildSearchParams.put(NetworkField.SHORTESTPATHSEARCH, false);
            buildSearchParams.put(NetworkField.PATHWAYSEARCH, true);
            buildSearchParams.put(NetworkField.PATHWAYINFO, "");
            buildSearchParams.put(NetworkField.DISEASESEARCH, false);
            buildSearchParams.put(NetworkField.QUERY, queryuc);
            buildSearchParams.put(NetworkField.INCFIRSTNEISEARCH, false);
            buildSearchParams.put(NetworkField.ENTITYNOTFOUND, "");
            buildSearchParams.put(NetworkField.SPECIES, organism);
            buildSearchParams.put(NetworkField.PATHWAYID, querylc);
            buildSearchParams.put(NetworkField.PTMLOADED, false);
            buildSearchParams.put(NetworkField.VIEW, NetworkView.Type.DEFAULT.name());
            buildSearchParams.put(NetworkField.ROOTNETWORKPTM, false); 
            return buildSearchParams;
        }
        return null;
    }   
    
    public static HashMap<String, Object> buildParamsFromNetworkRecord(CyRow record, SignorManager manager){

        HashMap <String, Object> new_param =  new HashMap();
        HashMap <String, Class<?>> model_network = NetworkField.networkTableField();
        Iterator key_set = model_network.keySet().iterator();
        Iterator value_set = model_network.values().iterator();  
        for (Map.Entry<String, Class<?>> entry : model_network.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value.equals(String.class)){  
               Object record_value = (Object) record.get(Config.NAMESPACE, (String) key, String.class);                               
               new_param.put((String) key, record_value);
            }     
            if(value.equals(Boolean.class)){  
               Object record_value = (Object) record.get(Config.NAMESPACE, (String) key, Boolean.class);                               
               new_param.put((String) key, record_value);
            } 
            if(value.equals(Double.class)){  
               Object record_value = (Object) record.get(Config.NAMESPACE, (String) key, Double.class);                               
               new_param.put((String) key, record_value);
            } 
            if(value.equals(Integer.class)){  
               Object record_value = (Object) record.get(Config.NAMESPACE, (String) key, Integer.class);                               
               new_param.put((String) key, record_value);
            } 
        }
        return new_param;       
      
    }

}

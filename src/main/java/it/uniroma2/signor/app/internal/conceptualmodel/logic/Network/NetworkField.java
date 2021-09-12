/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.conceptualmodel.logic.Network;

import java.util.HashMap;

public class NetworkField {
    public static String QUERY = "QUERY";
    public static String SINGLESEARCH = "SINGLESEARCH";
    public static String ALLSEARCH = "ALLSEARCH";
    public static String CONNECTSEARCH = "CONNECTSEARCH";
    public static String SHORTESTPATHSEARCH = "SHORTESTPATHSEARCH";
    public static String INCFIRSTNEISEARCH = "INCFIRSTNEISEARCH";
    public static String ENTITYNOTFOUND = "ENTITYNOTFOUND";
    public static String SPECIES = "SPECIES";
    public static String PTMLOADED  = "PTMLOADED";
    public static String PATHWAYSEARCH = "PATHWAYSEARCH";
    public static String PATHWAYINFO = "PATHWAYINFO";
    public static String DISEASESEARCH = "DISEASESEARCH";
    public static String PATHWAYID = "PATHWAYID";
    public static String VIEW = "VIEW";
    public static String ROOTNETWORKPTM = "ROOTNETWORKPTM";
    
    public static HashMap<String, Class<?>> networkTableField(){
        HashMap<String, Class<?>> params = new HashMap ();
        params.put(NetworkField.SINGLESEARCH, Boolean.class);
        params.put(NetworkField.ALLSEARCH, Boolean.class);
        params.put(NetworkField.CONNECTSEARCH, Boolean.class);
        params.put(NetworkField.SHORTESTPATHSEARCH, Boolean.class);
        params.put(NetworkField.PATHWAYSEARCH, Boolean.class);
        params.put(NetworkField.PATHWAYINFO, String.class);
        params.put(NetworkField.DISEASESEARCH, Boolean.class);
        params.put(NetworkField.QUERY, String.class);
        params.put(NetworkField.INCFIRSTNEISEARCH, Boolean.class);
        params.put(NetworkField.ENTITYNOTFOUND, String.class);
        params.put(NetworkField.SPECIES, String.class);
        params.put(NetworkField.PATHWAYID, String.class);        
        params.put(NetworkField.PTMLOADED, Boolean.class); 
        params.put(NetworkField.VIEW, String.class); 
        params.put(NetworkField.ROOTNETWORKPTM, Boolean.class);
        return params;
    }
}

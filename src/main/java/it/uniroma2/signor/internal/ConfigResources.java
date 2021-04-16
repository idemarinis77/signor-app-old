/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
 
/**
 *
 * @author amministratore
 */
public class ConfigResources {
    
    public enum DBLINK{
        SIGNOR("signor", "Signor", s -> "https://signor.uniroma2.it/relation_result.php?" + s),    
        UNIPROT("uniprot", "UniProt", s -> "https://www.uniprot.org/uniprot/" + s),        
        PUBMED("pubmed", "PUBMED", s -> "http://www.ncbi.nlm.nih.gov/pubmed/" + s),
        CHEBI("chebi", "CHEBI", s -> "https://www.ebi.ac.uk/chebi/searchId.do?chebiId=" + s),
        PUBCHEM("pubchem", "PUBCHEM", s -> "https://pubchem.ncbi.nlm.nih.gov/compound/CHEBI:" + s),
        MENTHA("mentha", "Mentha", s -> "http://mentha.uniroma2.it/result.php?q=ID" + s),
        NDEX("ndex", "Ndex", s -> "https://ndexbio.org/"+ s),
        DRUGBANK("drugbank", "DRUGBANK", s -> "https://drugbank.ca/" + s),
        GENECARDS("genecards", "GENECARDS", s -> "http://www.genecards.org/cgi-bin/carddisp.pl?id=" + s),
        MiRBase("mirBase", "MiRBase", s -> "http://www.mirbase.org/ " + s);
        
        public String name;
        public String fancyName;
        public Function<String, String> queryFunction;

        DBLINK(String name, String fancyName, Function<String, String> queryFunction) {
            this.name = name;
            this.fancyName = fancyName;
            this.queryFunction = queryFunction;
        }
    }
    
    public static final Map<String, DBLINK> DBLINKSMAP = new HashMap();
    
    static {
        for (DBLINK db: DBLINK.values()) {
            DBLINKSMAP.put(db.name, db);
        }
    }
    
    
    public enum WSSearchoption{
       SINGLEQUERY("SINGLESEARCH", (o, s) -> "https://signor.uniroma2.it/getData.php?organism="+o+"&id=" +s),
       CONNECTQUERY("CONNECTSEARCH",(o, s) ->  "https://signor.uniroma2.it/getData.php?organism="+o+"&id=" +s),
       SHORTESTQUERY("SHORTESTPATHSEARCH", (o, s) -> "https://signor.uniroma2.it/getData.php?organism="+o+"&id=" +s),
       DESEASPTHQUERY("DESEASPTHSEARCH", (o, s) -> "https://signor.uniroma2.it/getData.php?organism="+o+"&id=" +s);

        
        public String name;
        public BiFunction<String, String, String> queryFunction;

        WSSearchoption(String name, BiFunction<String, String, String> queryFunction) {
            this.name = name;
            this.queryFunction = queryFunction;
        }
    }
    public static String images_path = "/images/";
    public static final HashMap<String,String> DBLOGOS= new HashMap(){
        {           
           put("signor", images_path+"signor_logo_small.png"); 
           put("uniprot", images_path+"uniprot_logo_small.png");
           put("pubmed", images_path+"PubMed_logo_small.png");
           put("chebi", images_path+"ChEBI_logo_small.png");
           put("pubchem", images_path+"PubChem_logo_small.png");
           put("mentha", images_path+"mentha_logo_small.png");
           put("ndex", images_path+"ndex_logo_small.png");
           put("drugbank", images_path+"drugbank_logo_small.png");
           put("mirBase", images_path+"mirbase_logo_small.png" );
           put("mint", images_path+"mint_logo_small.jpg" );
        };
    };
    
        
    public static String FILESTYLE = "/style/styles-signor_in_cytoscape_namespace.xml";
    public static String SIGNOR_VER_STYLE="SIGNOR_NS_0.1";
   
    public static String PATHALLRELATIONSQUERY = "https://signor.uniroma2.it/getPathwayData.php?relations";
}

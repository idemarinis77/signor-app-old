/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/*Master file configuration for URL, DB, FILES, IMAGES*/
public class ConfigResources {
    //API for Databases
    public enum DBLINK{
        SIGNOR("signor", "Signor", s -> "https://signor.uniroma2.it/relation_result.php?id=" + s),    
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
    
    //URL and SIGNOR WS
    public static String PATHLIST = "https://signor.uniroma2.it/getPathwayType.php";
    public static String PATHALLRELATIONSQUERY = "https://signor.uniroma2.it/getPathwayData.php?relations";
    public static String PATHALLDESCRIPTIONSQUERY = "https://signor.uniroma2.it/getPathwayData.php?description";
    public static String PATHSINGLEDESCRIPTIONSQUERY= "https://signor.uniroma2.it/getPathwayData.php?pathway=";
    public static String INTERACTOMEDWLD="https://signor.uniroma2.it/getData.php";
    //public static String ENTITYINFO = "https://signor.uniroma2.it/getData.php?entityInfo=";
    public static final String SIGNOR_URL="https://signor.uniroma2.it";
    
    public static enum WSSearchoption{
       SINGLEQUERY("SINGLESEARCH", (o, s) -> "https://signor.uniroma2.it/getData.php?organism="+o+"&id=" +s),
       ALLQUERY("ALLSEARCH", (p, l) -> "https://signor.uniroma2.it/getData.php?type=connect&proteins="+p+"&level="+l),
       CONNECTQUERY("CONNECTSEARCH", (p, l) -> "https://signor.uniroma2.it/getData.php?type=connect&proteins="+p+"&level="+l),
       SHORTESTQUERY("SHORTESTPATHSEARCH", (s, t) -> "https://signor.uniroma2.it/ShortestPathSIGNOR/getData.php?start_node="+s+"&end_node="+t+"&longer=FALSE&organism="),
       DESEASPTHQUERY("DESEASPTHSEARCH", (o, s) -> "https://signor.uniroma2.it/getData.php?organism="+o+"&id=" +s),
       PATHWAYQUERY("PATHWAYSEARCH", (p, s) -> "https://signor.uniroma2.it/getPathwayData.php?pathway="+p+"&relations="+s),
       ENTITYINFOSEARCH("ENTITYINFOSEARCH", (s, o) -> "https://signor.uniroma2.it/getData.php?entityInfo="+s+"&organism="+o);
       
       public String name;
       public BiFunction<String, String, String> queryFunction;

       WSSearchoption(String name, BiFunction<String, String, String> queryFunction) {
           this.name = name;
           this.queryFunction = queryFunction;
       }
    }
    public static final Map<String, WSSearchoption> WSSearchoptionMAP = new HashMap();
    
    static {
        for (WSSearchoption ws: WSSearchoption.values()) {
            WSSearchoptionMAP.put(ws.name, ws);
        }
    }   
    //Images and logos
    public static String icon_path = "/images/signor_logo.png";
    public static String icon_path_search = "/images/Signor_Logo_orange.png";
    public static String icon_path_interactome = "/images/Signor_Logo_blue.png";
    public static String icon_path_interactome_ptm = "/images/Signor_Logo_fucsia.png";
    public static String icon_path_pathway = "/images/Signor_Logo_green.png";
    public static String iconpth_path = "/images/button_path.png";
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
    
    //Style
    public static String FILESTYLE = "/style/styles-signor_in_cytoscape_namespace.xml";
    public static String SIGNOR_VER_STYLE="SIGNOR_NS_0.2";   
    
}

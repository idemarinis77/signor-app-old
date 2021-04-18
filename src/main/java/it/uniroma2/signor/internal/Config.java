/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.net.URL;
import java.util.Map;
import org.w3c.dom.*;
import javax.xml.parsers.*;
/**
 *
 * @author amministratore
 */
public class Config {
    
    public enum DBLINK{
        SIGNOR("signor", "Signor", s -> "https://signor.uniroma2.it/relation_result.php" + s),    
        UNIPROT("uniprot", "UniProt", s -> "https://www.uniprot.org/uniprot/" + s),        
        PUBMED("pubmed", "PUBMED", s -> "http://www.ncbi.nlm.nih.gov/pubmed/" + s),
        CHEBI("chebi", "CHEBI", s -> "https://www.ebi.ac.uk/chebi/searchId.do?chebiId=" + s),
        PUBCHEM("pubchem", "PUBCHEM", s -> "https://pubchem.ncbi.nlm.nih.gov/compound/CHEBI:" + s),
        MENTHA("mentha", "Mentha", s -> "http://mentha.uniroma2.it/result.php?q=ID" + s),
        NDEX("ndex", "Ndex", s -> "https://ndexbio.org/"+ s),
        DRUGBANK("drugbank", "DRUGBANK", s -> "https://drugbank.ca/" + s),
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
       DESEASPTHQUERY("DESEASPTHSEARCH", (o, s) -> "https://signor.uniroma2.it/getData.php?organism="+o+"&id=" +s),
       PATHQUERY("PATHSEARCH", (o, s) -> "https://signor.uniroma2.it/getData.php?organism="+o+"&id=" +s);
        
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

    public static final String SIGNOR_ID="it.uniroma2.signor";
    public static final String SIGNOR_NAME = "Signor single/bridge query";
    public static final String SIGNOR_DESC = "Query SIGNOR Database to create your casual network";
    public static final String SIGNOR_URL="https://signor.uniroma2.it";
    
    public static final String NAMESPACE = "SIGNOR";
    public static final String NAMESPACEPTM = "SIGNOR - PTM";
    public static final String NTWPREFIX ="SIGNOR NTW - ";
    
    public static final String SINGLESCOPE = "single";
    public static final String ALLSCOPE = "all";
    public static final String CONNECTSCOPE = "connect";
    public static final String SHORTESTPATHSCOPE = "shortest path";
    public static final String INCFIRSTNEISCOPE = "include first neighbor";
    public static final String[] HEADERSINGLESEARCH = {"ENTITYA", "TYPEA", "IDA", "DATABASEA", "ENTITYB", "TYPEB", "IDB",
                                                    "DATABASEB", "EFFECT", "MECHANISM", "RESIDUE", "SEQUENCE", "TAX_ID", 
                                                    "CELL_DATA", "TISSUE_DATA", "MODULATOR_COMPLEX", "TARGET_COMPLEX", 
                                                    "MODIFICATIONA","MODASEQ", "MODIFICATIONB", "MODBSEQ", "PMID", 
                                                    "DIRECT", "NOTES", "ANNOTATOR", "SENTENCE", "SIGNOR_ID", "SCORE"};
    public static final String[] HEADERPTH = {"PATHWAY_ID", "PATHWAY_NAME", "ENTITYA", "REGULATOR_LOCATION", "TYPEA", "IDA", "DATABASEA",
                                                    "ENTITYB", "TARGET_LOCATION", "TYPEB", "IDB", "DATABASEB", "EFFECT", 
                                                    "MECHANISM", "RESIDUE", "SEQUENCE", "TAX_ID", "CELL_DATA",
                                                    "TISSUE_DATA", "MODULATOR_COMPLEX", "TARGET_COMPLEX", "MODIFICATIONA", "MODASEQ", 
                                                    "MODIFICATIONB", "MODBSEQ", "PMID", "DIRECT", "NOTES", "ANNOTATOR", 
                                                    "SENTENCE", "SIGNOR_ID",};

    public static final HashMap<String,Class<?>> PTMNODEFIELD= new HashMap(){
        {
           put("RESIDUE", String.class);
           put("TYPE", String.class);
           put("SEQUENCE", String.class);           
        }
    };
    public static final HashMap<String,Class<?>> PTMEDGEFIELD= new HashMap(){
        {
           put("EdgeParent", Long.class);           
           put("NodeSourceSUID", Long.class);           
           put("NodeTargetSUID", Long.class);           
           put("INTERACTION", String.class);
        }
    };
        //Fields from TSV for Nodes,
    public static final HashMap<String,Class<?>> NETWORKFIELD= new HashMap(){
        {
           put("QUERYTYPE", String.class);
           put("SINGLESEARCH", Boolean.class);
           put("ORGANISM", String.class);
           put("PTM LOADED", Boolean.class);
           put("DATA", String.class);
        }
    };
    //Fields from TSV for Nodes,
    public static final HashMap<String,Class<?>> NODEFIELD= new HashMap(){
        {
           put("ENTITY", String.class);
           put("TYPE", String.class);
           put("ID", String.class);
           put("DATABASE", String.class);
        }
    };
    public static final HashMap<String,String> NODEFIELDMAP= new HashMap(){
        {
           put("ENTITYA", "ENTITY");
           put("TYPEA", "TYPE");
           put("IDA", "ID");
           put("DATABASEA", "DATABASE");
           put("ENTITYB", "ENTITY");
           put("TYPEB", "TYPE");
           put("IDB", "ID");
           put("DATABASEB", "DATABASE");
        }
    };
    public static final HashMap<String,Class<?>> EDGEFIELD = new HashMap(){
        {
           put("Interaction", String.class);
           put("MECHANISM", String.class);
           put("RESIDUE", String.class);
           put("SEQUENCE", String.class);
           put("TAX_ID", Integer.class);
           put("CELL_DATA", String.class);
           put("TISSUE_DATA", String.class);
           put("MODULATOR_COMPLEX", String.class);
           put("TARGET_COMPLEX", String.class);
           put("MODIFICATIONA", String.class);
           put("MODASEQ", String.class);
           put("MODIFICATIONB", String.class);
           put("MODBSEQ", String.class);
           put("PMID", String.class);
           put("DIRECT", String.class);
           put("NOTES", String.class);
           put("ANNOTATOR", String.class);
           put("SENTENCE", String.class);
           put("SCORE", Double.class);
           put("SIGNOR_ID", String.class);           
        }
    };
    //Fields from TSV for Edges
    public static final HashMap<String,String> EDGEFIELDMAP = new HashMap(){
        {
           put("EFFECT", "Interaction");
           put("MECHANISM", "MECHANISM");
           put("RESIDUE", "RESIDUE");
           put("SEQUENCE", "SEQUENCE");
           put("TAX_ID", "TAX_ID");
           put("CELL_DATA", "CELL_DATA");
           put("TISSUE_DATA", "TISSUE_DATA");
           put("MODULATOR_COMPLEX", "MODULATOR_COMPLEX");
           put("TARGET_COMPLEX", "TARGET_COMPLEX");
           put("MODIFICATIONA", "MODIFICATIONA");
           put("MODASEQ", "MODASEQ");
           put("MODIFICATIONB", "MODIFICATIONB");
           put("MODBSEQ", "MODBSEQ");
           put("PMID", "PMID");
           put("DIRECT", "DIRECT");
           put("NOTES", "NOTES");
           put("ANNOTATOR", "ANNOTATOR");
           put("SENTENCE", "SENTENCE");
           put("SCORE", "SCORE");
           put("SIGNOR_ID", "SIGNOR_ID");           
        }
    };
    public static final HashMap<String,String> FIELDPTHWORDER= new HashMap(){
        {
           put("ENTITYA", "ENTITY");
           put("TYPEA", "TYPE");
           put("IDA", "IDA");
           put("DATABASEA", "DATABASE");
           put("ENTITYB", "ENTITY");
           put("TYPEB", "TYPE");
           put("IDB", "IDB");
           put("PATHWAY_ID", "PATHWAY_ID");
           put("PATHWAY_NAME", "PATHWAY_NAME");
           put("EFFECT", "EFFECT");
           put("TARGET_LOCATION", "TARGET_LOCATION");
           put("MECHANISM", "MECHANISM");
           put("RESIDUE", "RESIDUE");
           put("SEQUENCE", "SEQUENCE");
           put("TAX_ID", "TAX_ID");
           put("CELL_DATA", "CELL_DATA");
           put("PMID", "PMID");
           put("DIRECT", "DIRECT");
           put("ANNOTATOR", "ANNOTATOR");
           put("SENTENCE", "SENTENCE");
           put("SIGNOR_ID", "SIGNOR_ID");
           put("REGULATOR_LOCATION", "REGULATOR_LOCATION");
           put("NOTES", "NOTES");
           put("TISSUE_DATA", "TISSUE_DATA");
           put("MODIFICATIONA", "MODIFICATIONA");
           put("MODBSEQ", "MODBSEQ");
           put("MODULATOR_COMPLEX", "MODULATOR_COMPLEX"); 
           put("TARGET_COMPLEX", "TARGET_COMPLEX");
           put("MODIFICATIONB", "MODIFICATIONB");
           put("MODASEQ", "MODASEQ");
        }
    };
     
    public static String FILESTYLE = "/style/styles-signor_in_cytoscape_namespace.xml";
    public static String SIGNOR_VER_STYLE="SIGNOR_NS_0.1";
    
    //Label in Option search
    public static final HashMap<String,String> SEARCHOPTION = new HashMap<>(){
        {put("SINGLESEARCH", "single");
         put("ALLSEARCH", "all");
         put("CONNECTSEARCH", "connect");
         put("SHORTESTPATHSEARCH", "shortest path");
         put("INCFIRSTNEISEARCH", "include first neighbor");
      }
    };

    public static final String INCFIRSTNEITOOLTIP = "Choose this option to";
    public static final HashMap<String,String> SPECIES = new HashMap<>(){
        {put ("Homo Sapiens", "9606");
         put ("Mus musculus", "10090");
         put ("Rattus norvegicus", "10116");
        }
    };
    
    public static final String identifier_panel = "it.uniroma2.signor.app.details";
    
}

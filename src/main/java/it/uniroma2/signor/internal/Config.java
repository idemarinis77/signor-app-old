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

    
    public static final String SIGNOR_ID = "SIGNOR_ID_SEARCH_FACTORY";
    public static final String SIGNOR_NAME = "Signor single/bridge query";
    public static final String SIGNOR_DESC = "Query SIGNOR Database to create your casual network";
    public static final String SIGNOR_URL="https://signor.uniroma2.it";
    
    public static final String NAMESPACE = "SIGNOR";
    public static final String NAMESPACEPTM = "SIGNOR - PTM";
    public static final String NTWPREFIX ="SIGNOR NTW - ";
    
//    public static final String SINGLESCOPE = "single";
//    public static final String ALLSCOPE = "all";
//    public static final String CONNECTSCOPE = "connect";
//    public static final String SHORTESTPATHSCOPE = "shortest path";
//    public static final String INCFIRSTNEISCOPE = "include first neighbor";
    public static final String[] HEADERSINGLESEARCH = {"ENTITYA", "TYPEA", "IDA", "DATABASEA", "ENTITYB", "TYPEB", "IDB",
                                                    "DATABASEB", "EFFECT", "MECHANISM", "RESIDUE", "SEQUENCE", "TAX_ID", 
                                                    "CELL_DATA", "TISSUE_DATA", "MODULATOR_COMPLEX", "TARGET_COMPLEX", 
                                                    "MODIFICATIONA","MODASEQ", "MODIFICATIONB", "MODBSEQ", "PMID", 
                                                    "DIRECT", "NOTES", "ANNOTATOR", "SENTENCE", "SIGNOR_ID", "SCORE"};
    

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
    //Fields for Networks,
    public static final HashMap<String,Class<?>> NETWORKFIELD= new HashMap(){
        {
           put("QUERY", String.class);
           put("SINGLESEARCH", Boolean.class);
           put("ALLSEARCH", Boolean.class);
           put("CONNECTSEARCH", Boolean.class);           
           put("SHORTESTPATHSEARCH", Boolean.class);
           put("INCFIRSTNEISEARCH", Boolean.class);
           put("SPECIES", String.class);
           put("PTMLOADED", Boolean.class);
           put("ISPATHWAY", Boolean.class);
           put("ISDISEASE", Boolean.class);
           put("PATHWAYID", String.class);
        }
    };
    
    public static final String NODEID = "ID";
    public static final String NODETYPE = "TYPE";
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
    
    public static final String[] HEADER_ROOT_NODE_ADDINFO_COMPLEX = {"NAME", "SIG_ID", "DESCRIPTION", "FORMEDBY", "COMPLEXPORTAL_ID"};
    public static final String[] HEADER_ROOT_NODE_ADDINFO_CHEMICAL = {"NAME", "ENTITY_ALIAS", "ENTITY_DB_ID", "ALT_ID", "FORMULA", "IUPAC"};
    public static final String[] HEADER_ROOT_NODE_ADDINFO_FUSIONPROTEIN = {"NAME", "SIG_ID", "FP_DESCRIPTION", "FP_SEQUENCE", "IDS"};
    public static final String[] HEADER_ROOT_NODE_ADDINFO_PROTEINFAMILY = {"NAME", "SIG_ID", "PF_DESCRIPTION", "FORMEDBY"};
    public static final String[] HEADER_ROOT_NODE_ADDINFO_STIMULUS = {"NAME", "SIG_ID", "STIM_DESCRIPTION"};
    public static final String[] HEADER_ROOT_NODE_ADDINFO_PHENOTYPE = {"NAME", "SIG_ID", "PHE_DESCRIPTION"};
    public static final String[] HEADER_ROOT_NODE_ADDINFO_MIRNA = {"NAME", "MIRNA_DB_ID", "RNA_CENTRAL"};
    public static final String[] HEADER_ROOT_NODE_ADDINFO_PROTEIN = {"NAME", "GENE_NAME", "ENTITY_DB_ID", "FUNCTION", "ENTITY_ALIAS"};
    
    public static final String[] ROOT_NODE_COMMONS_ADDINFO = {"NAME", "FORMEDBY", "DESCRIPTION"};
    public static final String PATHWAYLISTADDINFO = "PATHWAYLIST";
    
    public static final HashMap<String,Class<?>> NODEFIELDADDITIONAL= new HashMap(){
        {
           put(ROOT_NODE_COMMONS_ADDINFO[0], String.class);
           put(ROOT_NODE_COMMONS_ADDINFO[1], String.class);
           put(ROOT_NODE_COMMONS_ADDINFO[2], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_COMPLEX[1], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_COMPLEX[4], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_CHEMICAL[1], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_CHEMICAL[2], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_CHEMICAL[3], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_CHEMICAL[4], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_CHEMICAL[5], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_FUSIONPROTEIN[2], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_FUSIONPROTEIN[3], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_FUSIONPROTEIN[4], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_PROTEINFAMILY[2], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_STIMULUS[2], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_PHENOTYPE[2], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_MIRNA[1], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_MIRNA[2], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_PROTEIN[1], String.class);
           put(HEADER_ROOT_NODE_ADDINFO_PROTEIN[3], String.class);
           put(PATHWAYLISTADDINFO, String.class);
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
    
    public static String FILESTYLE = "/style/styles-signor_in_cytoscape_namespace.xml";
    public static String SIGNOR_VER_STYLE="SIGNOR_NS_0.1";
    
    public static final String SINGLESEARCH = "SINGLESEARCH";
    public static final String ALLSEARCH = "ALLSEARCH";
    public static final String CONNECTSEARCH = "CONNECTSEARCH";
    public static final String SHORTESTPATHSEARCH = "SHORTESTPATHSEARCH";
    public static final String INCFIRSTNEISEARCH = "INCFIRSTNEISEARCH";
    public static final String SPECIES = "SPECIES";
    //Label in Option search
//    public static final HashMap<String,String> SEARCHOPTION = new HashMap<>(){
//        {put(SINGLESEARCH, "single");
//         put(ALLSEARCH, "all");
//         put(CONNECTSEARCH, "connect");
//         put(SHORTESTPATHSEARCH, "shortest path");
//         put(INCFIRSTNEISEARCH, "include first neighbor");
//      }
//    };

    //public static final String INCFIRSTNEITOOLTIP = "Choose this option to";
    public static final HashMap<String,String> SPECIESLIST = new HashMap<>(){
        {put ("Homo Sapiens", "9606");
         put ("Mus musculus", "10090");
         put ("Rattus norvegicus", "10116");
        }
    };
    
    public static final String identifier_panel = "it.uniroma2.signor.app.details";
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal;
import java.util.HashMap;

public class Config {
 
    public static final String NAMESPACE = "SIGNOR";
    public static final String NAMESPACEPTM = "SIGNOR - PTM";
    public static final String NTWPREFIX ="SIGNOR Network - ";
    public static final String INTERACTOMENAME = "INTERACTOME";
    
    public static final HashMap<String,String> SPECIESLIST = new HashMap<>(){
        {put ("Homo Sapiens", "9606");
         put ("Mus musculus", "10090");
         put ("Rattus norvegicus", "10116");
        }
    };
    
    public static final HashMap<String,String> SPECIESSHORTESTPATH = new HashMap<>(){
        {put ("Homo Sapiens", "human");
         put ("Mus musculus", "mouse");
         put ("Rattus norvegicus", "rat");
        }
    };
    public static final HashMap<String,String> DIRECTMAP = new HashMap<>(){
        {put ("t", "YES");
         put ("f", "NO");}
    };
    public static final String identifier_panel = "it.uniroma2.signor.app.details";
    public static final String[] HEADERSINGLESEARCH = {"ENTITYA", "TYPEA", "IDA", "DATABASEA", "ENTITYB", "TYPEB", "IDB",
                                                    "DATABASEB", "EFFECT", "MECHANISM", "RESIDUE", "SEQUENCE", "TAX_ID", 
                                                    "CELL_DATA", "TISSUE_DATA", "MODULATOR_COMPLEX", "TARGET_COMPLEX", 
                                                    "MODIFICATIONA","MODASEQ", "MODIFICATIONB", "MODBSEQ", "PMID", 
                                                    "DIRECT", "NOTES", "ANNOTATOR", "SENTENCE", "SIGNOR_ID", "SCORE"};
    
    public static int source_entity_position= 0;
    public static int target_entity_position= 4;
    public static int interaction_position = 8;
    public static int node_source_positions[] = {0,1,2,3};
    public static int node_target_positions[]= {4,5,6,7};
    public static int edge_positions[]= {8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27};
    
    
    public static final HashMap<String,String> PTMprefix = new HashMap<>(){
        {put ("acetylation", "ac");
         put ("carboxylation", "car");
         put ("cleavage", "cle");
         put ("glycosylation", "gli");
         put ("hydroxylation", "had");
         put ("methylation", "met");
         put ("neddylation", "ned");
         put ("palmitoylation", "pal");
         put ("phosphorylation", "ph");
         put ("sumoylation", "sum");
         put ("ubiquitination", "ub");
        }
    };
}

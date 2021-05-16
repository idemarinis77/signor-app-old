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
    public static int edge_positions[]= {8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26};
    
    
}

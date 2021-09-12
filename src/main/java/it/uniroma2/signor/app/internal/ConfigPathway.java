/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal;

import java.util.HashMap;

/*Master file configuration for PATHWAY MODEL*/
public class ConfigPathway {
        
    
        public static final String[] HEADERPTH = {"PATHWAY_ID", "PATHWAY_NAME", "ENTITYA", "REGULATOR_LOCATION", "TYPEA", "IDA", "DATABASEA",
                                                    "ENTITYB", "TARGET_LOCATION", "TYPEB", "IDB", "DATABASEB", "EFFECT", 
                                                    "MECHANISM", "RESIDUE", "SEQUENCE", "TAX_ID", "CELL_DATA",
                                                    "TISSUE_DATA", "MODULATOR_COMPLEX", "TARGET_COMPLEX", "MODIFICATIONA", "MODASEQ", 
                                                    "MODIFICATIONB", "MODBSEQ", "PMID", "DIRECT", "NOTES", "ANNOTATOR", 
                                                    "SENTENCE", "SIGNOR_ID","SCORE"};
    
        public static int node_source_positions[] = {2,4,5,6};
        public static int node_target_positions[]= {7,9,10,11};
        public static int edge_positions[] = {0,1,3,8,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
        public static int source_entity_position= 2;
        public static int target_entity_position= 7;
        public static int interaction_position= 12;
    
    

}

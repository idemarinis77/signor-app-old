/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal;

import java.util.HashMap;

/**
 *
 * @author amministratore
 */
/*Master file configuration for PATHWAY MODEL*/
public class ConfigPathway {
    

        public static final String SIGNORPTH_NAME = "Signor pathway query";
        public static final String SIGNORPTH_DESC = "Query SIGNOR Database to visualize pathway";
        //Oorde of fields, tabbed by \t, for URL parsing
        public static final String[] HEADERPTH = {"PATHWAY_ID", "PATHWAY_NAME", "ENTITYA", "REGULATOR_LOCATION", "TYPEA", "IDA", "DATABASEA",
                                                    "ENTITYB", "TARGET_LOCATION", "TYPEB", "IDB", "DATABASEB", "EFFECT", 
                                                    "MECHANISM", "RESIDUE", "SEQUENCE", "TAX_ID", "CELL_DATA",
                                                    "TISSUE_DATA", "MODULATOR_COMPLEX", "TARGET_COMPLEX", "MODIFICATIONA", "MODASEQ", 
                                                    "MODIFICATIONB", "MODBSEQ", "PMID", "DIRECT", "NOTES", "ANNOTATOR", 
                                                    "SENTENCE", "SIGNOR_ID",};
        
        //These attributes are used to search for list of pathway to show in summary sheet
        public static final String PTHID = "IDA";
        public static final String PTHNAME = "PATHWAY_NAME";
        
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
     
}

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
        public static final String SIGNORPTH_ID = "SIGNOR_PTH_ID_SEARCH_FACTORY";
        
        //Field to parse PATHWAY SIG and Name
        public static final String[] SIGNORPTH_DESCRIPTION = {"sig_id", "path_name", "path_description", "path_curator"};
        public static final String SIGNORPTH_SIG_ID = "sig_id";
        public static final String SIGNORPTH_PATH_NAME = "path_name";
        
        //Order of fields, tabbed by \t, for URL parsing. There is no header in URL
        public static final String[] HEADERPTH = {"PATHWAY_ID", "PATHWAY_NAME", "ENTITYA", "REGULATOR_LOCATION", "TYPEA", "IDA", "DATABASEA",
                                                    "ENTITYB", "TARGET_LOCATION", "TYPEB", "IDB", "DATABASEB", "EFFECT", 
                                                    "MECHANISM", "RESIDUE", "SEQUENCE", "TAX_ID", "CELL_DATA",
                                                    "TISSUE_DATA", "MODULATOR_COMPLEX", "TARGET_COMPLEX", "MODIFICATIONA", "MODASEQ", 
                                                    "MODIFICATIONB", "MODBSEQ", "PMID", "DIRECT", "NOTES", "ANNOTATOR", 
                                                    "SENTENCE", "SIGNOR_ID","SCORE"};
        
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
               put("MODIFICATIONB", "MODIFICATIONB");
               put("MODBSEQ", "MODBSEQ");
               put("MODULATOR_COMPLEX", "MODULATOR_COMPLEX"); 
               put("TARGET_COMPLEX", "TARGET_COMPLEX");
               put("MODIFICATIONA", "MODIFICATIONA");
               put("MODASEQ", "MODASEQ");
               put("SCORE", "SCORE");
            }
        };
        public static final HashMap<String,Class<?>> EDGEFIELDPTH= new HashMap(){
            {
               put("PATHWAY_ID", String.class);
               put("PATHWAY_NAME", String.class);
               put("EFFECT", String.class);
               put("TARGET_LOCATION", String.class);
               put("MECHANISM", String.class);
               put("RESIDUE", String.class);
               put("SEQUENCE", String.class);
               put("TAX_ID", String.class);
               put("CELL_DATA", String.class);
               put("PMID", String.class);
               put("DIRECT", String.class);
               put("ANNOTATOR", String.class);
               put("SENTENCE", String.class);
               put("SIGNOR_ID", String.class);
               put("REGULATOR_LOCATION", String.class);
               put("NOTES", String.class);
               put("TISSUE_DATA", String.class);
               put("MODIFICATIONB", String.class);
               put("MODBSEQ", String.class);
               put("MODULATOR_COMPLEX", String.class); 
               put("TARGET_COMPLEX", String.class);
               put("MODIFICATIONA", String.class);
               put("MODASEQ", String.class);
               put("SCORE", Double.class);
            }
        };
        public static final HashMap<String,String> EDGEFIELDPTHMAP= new HashMap(){
            {
               put("PATHWAY_ID", "PATHWAY_ID");
               put("PATHWAY_NAME","PATHWAY_NAME");
               put("EFFECT", "Interaction");
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
               put("MODIFICATIONB", "MODIFICATIONB");
               put("MODBSEQ", "MODBSEQ");
               put("MODULATOR_COMPLEX", "MODULATOR_COMPLEX"); 
               put("TARGET_COMPLEX", "TARGET_COMPLEX");
               put("MODIFICATIONA", "MODIFICATIONA");
               put("MODASEQ", "MODASEQ");
               put("SCORE", "SCORE");
            }
        };
     
}

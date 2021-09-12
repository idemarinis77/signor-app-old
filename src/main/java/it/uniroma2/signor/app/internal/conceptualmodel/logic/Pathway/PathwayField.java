/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.conceptualmodel.logic.Pathway;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class PathwayField {
        public static final String SIGNORPTH_SIG_ID = "sig_id";
        public static final String SIGNORPTH_PATH_NAME = "path_name";
        public static final String SIGNORPTH_DESCRIPTION = "coalesce";
        public static final String SIGNORPTH_CURATOR = "path_curator";
        public static final String[] SIGNORPTH_HEADER_DESC = {SIGNORPTH_SIG_ID, SIGNORPTH_PATH_NAME, SIGNORPTH_DESCRIPTION, SIGNORPTH_CURATOR};

        public static final HashMap<String,String> SIGNORPTHFIELDMAP = new HashMap() {
                  {
                     put(SIGNORPTH_SIG_ID, "Pathway identifier");
                     put(SIGNORPTH_PATH_NAME, "Name");
                     put(SIGNORPTH_DESCRIPTION, "Description");
                     put(SIGNORPTH_CURATOR, "Curator");
            }
        };
        //These attributes are used to search for list of pathway to show in summary sheet
        public static final String PTHIDA = "IDA";
        public static final String EFFECT = "EFFECT";
        
        public static final String PATHWAYID = "PATHWAYID";
        public static final String PATHWAY_ID = "PATHWAY_ID";
        public static final String PATHWAYNAME = "PATHWAY_NAME";
        public static final String REGULATOR_LOCATION = "REGULATOR_LOCATION";
        public static final String TARGET_LOCATION = "TARGET_LOCATION";
        public static final String Interaction = "Interaction";
        public static final String MECHANISM = "MECHANISM";
        public static final String RESIDUE = "RESIDUE";
        public static final String SEQUENCE = "SEQUENCE";
        public static final String TAX_ID = "TAX_ID";
        public static final String CELL_DATA = "CELL_DATA";
        public static final String TISSUE_DATA = "TISSUE_DATA";
        public static final String MODULATOR_COMPLEX = "MODULATOR_COMPLEX";
        public static final String TARGET_COMPLEX = "TARGET_COMPLEX";
        public static final String MODIFICATIONA = "MODIFICATIONA";
        public static final String MODASEQ = "MODASEQ";
        public static final String MODIFICATIONB = "MODIFICATIONB";
        public static final String MODBSEQ = "MODBSEQ";
        public static final String PMID = "PMID";
        public static final String DIRECT = "DIRECT";
        public static final String NOTES = "NOTES";
        public static final String ANNOTATOR = "ANNOTATOR";
        public static final String SENTENCE = "SENTENCE";
        public static final String SIGNOR_ID = "SIGNOR_ID";
        public static final String SCORE = "SCORE";
        //Order of fields, tabbed by \t, for URL parsing. There is no header in URL
        
        

        public static final LinkedHashMap<String,Class<?>> ORDEREDEDGEFIELDPTH= new LinkedHashMap(){
            {
               put(PATHWAY_ID, String.class);
               put(PATHWAYNAME, String.class);
               put(REGULATOR_LOCATION, String.class);
               put(TARGET_LOCATION, String.class);
               put(Interaction, String.class); 
               put(MECHANISM, String.class);
               put(RESIDUE, String.class);
               put(SEQUENCE, String.class);
               put(TAX_ID, Integer.class);
               put(CELL_DATA, String.class);
               put(TISSUE_DATA, String.class);
               put(MODULATOR_COMPLEX, String.class); 
               put(TARGET_COMPLEX, String.class);
               put(MODIFICATIONA, String.class);
               put(MODASEQ, String.class);
               put(MODIFICATIONB, String.class);
               put(MODBSEQ, String.class);
               put(PMID, String.class);
               put(DIRECT, String.class);
               put(NOTES, String.class);
               put(ANNOTATOR, String.class);
               put(SENTENCE, String.class);
               put(SIGNOR_ID, String.class);
               put(SCORE, Double.class);                        
            }
        };
        
        public static final HashMap<String,Class<?>> EDGEFIELDPTH= new HashMap(){
            {
               put(PATHWAY_ID, String.class);
               put(PATHWAYNAME, String.class);
               put(Interaction, String.class);
               put(TARGET_LOCATION, String.class);
               put(MECHANISM, String.class);
               put(RESIDUE, String.class);
               put(SEQUENCE, String.class);
               put(TAX_ID, Integer.class);
               put(CELL_DATA, String.class);
               put(PMID, String.class);
               put(DIRECT, String.class);
               put(ANNOTATOR, String.class);
               put(SENTENCE, String.class);
               put(SIGNOR_ID, String.class);
               put(REGULATOR_LOCATION, String.class);
               put(NOTES, String.class);
               put(TISSUE_DATA, String.class);
               put(MODIFICATIONB, String.class);
               put(MODBSEQ, String.class);
               put(MODULATOR_COMPLEX, String.class); 
               put(TARGET_COMPLEX, String.class);
               put(MODIFICATIONA, String.class);
               put(MODASEQ, String.class);
               put(SCORE, Double.class);
            }
        };
        public static final HashMap<String,String> EDGEFIELDPTHMAP= new HashMap(){
            {
               put(PATHWAY_ID, PATHWAY_ID);
               put(PATHWAYNAME,PATHWAYNAME);
               put(EFFECT, Interaction);
               put(TARGET_LOCATION, TARGET_LOCATION);
               put(MECHANISM, MECHANISM);
               put(RESIDUE, RESIDUE);
               put(SEQUENCE, SEQUENCE);
               put(TAX_ID, TAX_ID);
               put(CELL_DATA, CELL_DATA);
               put(PMID, PMID);
               put(DIRECT, DIRECT);
               put(ANNOTATOR, ANNOTATOR);
               put(SENTENCE, SENTENCE);
               put(SIGNOR_ID, SIGNOR_ID);
               put(REGULATOR_LOCATION, REGULATOR_LOCATION);
               put(NOTES, NOTES);
               put(TISSUE_DATA, TISSUE_DATA);
               put(MODIFICATIONB, MODIFICATIONB);
               put(MODBSEQ, MODBSEQ);
               put(MODULATOR_COMPLEX, MODULATOR_COMPLEX); 
               put(TARGET_COMPLEX, TARGET_COMPLEX);
               put(MODIFICATIONA, MODIFICATIONA);
               put(MODASEQ, MODASEQ);
               put(SCORE, SCORE);
            }
        };
     
}

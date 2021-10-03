/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.conceptualmodel.logic.Edges;

import java.util.HashMap;
import java.util.LinkedHashMap;


public class EdgeField {
    public static final String Interaction = "Interaction";
    public static final String EFFECT = "EFFECT";
    public static final String MECHANISM = "MECHANISM";
    public static final String RESIDUE ="RESIDUE";
    public static final String SEQUENCE ="SEQUENCE";
    public static final String TAX_ID ="TAX_ID";
    public static final String CELL_DATA ="CELL_DATA";
    public static final String TISSUE_DATA ="TISSUE_DATA";
    public static final String MODULATOR_COMPLEX="MODULATOR_COMPLEX";
    public static final String TARGET_COMPLEX = "TARGET_COMPLEX";
    public static final String MODIFICATIONA="MODIFICATIONA";
    public static final String MODASEQ="MODASEQ";
    public static final String MODIFICATIONB="MODIFICATIONB";
    public static final String MODBSEQ="MODBSEQ";
    public static final String PMID="PMID";
    public static final String DIRECT="DIRECT";
    public static final String NOTES="NOTES";
    public static final String ANNOTATOR="ANNOTATOR";
    public static final String SENTENCE="SENTENCE";
    public static final String SCORE="SCORE";
    public static final String SIGNOR_ID="SIGNOR_ID";
    public static final String MODIFIEDRESIDUE = "MODIFIED RESIDUE";
        
    public static final HashMap<String,Class<?>> EDGEFIELD = new HashMap(){
        {
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
           put(SCORE, Double.class);
           put(SIGNOR_ID, String.class);           
        }
    };

        public static final LinkedHashMap<String,Class<?>> ORDEREDEDGEFIELD = new LinkedHashMap(){
        {
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
    public static final HashMap<String,Class<?>> EDGESUMMARY = new HashMap(){
        {           
           put(MECHANISM, String.class);           
           put(MODIFICATIONA, String.class);           
           put(PMID, String.class);
           put(SENTENCE, String.class);
        }
    };
    
    //Fields from TSV for Edges
    public static final HashMap<String,String> EDGEFIELDMAP = new HashMap(){
        {
           put(EFFECT, Interaction);
           put(MECHANISM, MECHANISM);
           put(RESIDUE, RESIDUE);
           put(SEQUENCE, SEQUENCE);
           put(TAX_ID, TAX_ID);
           put(CELL_DATA, CELL_DATA);
           put(TISSUE_DATA, TISSUE_DATA);
           put(MODULATOR_COMPLEX, MODULATOR_COMPLEX);
           put(TARGET_COMPLEX, TARGET_COMPLEX);
           put(MODIFICATIONA, MODIFICATIONA);
           put(MODASEQ, MODASEQ);
           put(MODIFICATIONB, MODIFICATIONB);
           put(MODBSEQ, MODBSEQ);
           put(PMID, PMID);
           put(DIRECT, DIRECT);
           put(NOTES, NOTES);
           put(ANNOTATOR, ANNOTATOR);
           put(SENTENCE, SENTENCE);
           put(SCORE, SCORE);
           put(SIGNOR_ID, SIGNOR_ID);           
        }
    };
}

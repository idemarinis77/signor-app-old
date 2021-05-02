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
        public static final String PTHIDA = "IDA";
        public static final String PATHWAYNAME = "PATHWAY_NAME";
        public static final String PATHWAYID = "PATHWAYID";
        
        public static final HashMap<String,String> PathwayDiseaseList = new HashMap() {
      
            {
                     put("", "Select Disease below");
                     put("SIGNOR-AD", "Alzheimer");
                     put("SIGNOR-FSGS", "Focal segmental glomerulosclerosis");
                     put("SIGNOR-MSP", "Multiple sclerosis");
                     put("SIGNOR-NS", "Noonan sundrome");
                     put("SIGNOR-PD", "Parkinson");
            }
        };
        
        public static final HashMap<String,String> PathwayTumorList = new HashMap() {
                   {
                     put("", "Select Tumor below");
                     put("SIGNOR-AML-MiniPathway", "Acute Myeloid Leukemia");
                     put("SIGNOR-CRC", "Colorectal Carcinoma");
                     put("SIGNOR-GBM", "Glioblastoma Multiforme");
                     put("SIGNOR-HT", "Hepatocellular Tumor");
                     put("SIGNOR-LBC", "Luminal Breast Cancer");
                     put("SIGNOR-MM", "Malignant");
                     put("SIGNOR-PDAP", "Pancreatic");
                     put("SIGNOR-PC", "Prostate");
                     put("SIGNOR-RMS", "Rhabdomyosarcoma");
                     put("SIGNOR-TC", "Thyroid cancer");
                   }
        };
        
        public static final HashMap<String,String> PathwayList = new HashMap() {
                    {
                     put("", "Select Pathway Below");
                     put("SIGNOR-AC", "Adipogenesis");
                     put("SIGNOR-AMPK", "AMPK");
                     put("SIGNOR-Autophagy", "Autophagy");
                     put("SIGNOR-CM", "Catecholamine");
                     put("SIGNOR-G1-S_trans", "Cell cycle: G1/S phase transition");
                     put("SIGNOR-G2-M_trans", "Cell cycle: G2/M phase transitio");
                     put("SIGNOR-CS", "Complementing signal");
                     put("SIGNOR-DR", "Death receptor signaling");
                     put("SIGNOR-EGF", "EGFR signaling");
                     put("SIGNOR-Fibrosis", "Fibrosis");
                     put("SIGNOR-GCR", "Glucocorticoid receptor Signaling");
                     put("SIGNOR-HPP", "Hippo Signaling");
                     put("SIGNOR-IL1R", "IL1 Signaling");
                     put("SIGNOR-IL6", "IL6 Signaling");
                     put("SIGNOR-Inflammosome", "Inflammosome Activation");
                     put("SIGNOR-IOA", "Inhibition of Apoptosis");
                     put("SIGNOR-Inflammosome", "Inflammosome Activation");
                     put("SIGNOR-IIS", "Innate Immune Response");
                     put("SIGNOR-INSR", "Insulin Signaling");
                     put("SIGNOR-IS", "Integrin Signaling");
                     put("SIGNOR-LR", "Leptin Signaling");
                     put("SIGNOR-M1M2", "Macrophage polarization");
                     put("SIGNOR-MCAPO", "Mitochondrial control of Apoptosis");
                     put("SIGNOR-MS", "MTOR Signaling");
                     put("SIGNOR-NFKBC", "NF-KB Canonical");
                     put("SIGNOR-NFKBNC", "NF-KB non canonical");
                     put("SIGNOR-NOTCH", "NOTCH Signaling");
                     put("SIGNOR-P38", "P38 Signaling");
                     put("SIGNOR-PI3K-AKT", "PI3K/AKT Signaling");
                     put("SIGNOR-SAPK-JNK", "SAPK/JNK Signaling");
                     put("SIGNOR-Hedgehog", "Sonic  HedgeHog");
                     put("SIGNOR-TCA", "T Cell Activation");
                     put("SIGNOR-TGFb", "TGF-beta Signaling");
                     put("SIGNOR-TA", "TNF-alpha");
                     put("SIGNOR-TLR", "Toll like receptor");
                     put("SIGNOR-VEGF", "VEGF Signaling");
                     put("SIGNOR-WNT", "WNT Signaling");
                    }
        };
                     
        public static final HashMap<String,String> MapPathwayDescID = new HashMap() {
      
            {                     
                     put("Alzheimer", "SIGNOR-AD");
                     put("Focal segmental glomerulosclerosis", "SIGNOR-FSGS");
                     put("Multiple sclerosis", "SIGNOR-MSP");
                     put("Noonan sundrome", "SIGNOR-NS");
                     put("Parkinson", "SIGNOR-PD");            
                     put("Acute Myeloid Leukemia", "SIGNOR-AML-MiniPathway");
                     put("Colorectal Carcinoma", "SIGNOR-CRC");
                     put("Glioblastoma Multiforme", "SIGNOR-GBM");
                     put("Hepatocellular Tumor", "SIGNOR-HT");
                     put("Luminal Breast Cancer", "SIGNOR-LBC");
                     put("Malignant", "SIGNOR-MM");
                     put("Pancreatic", "SIGNOR-PDAP");
                     put("Prostate", "SIGNOR-PC");
                     put("Rhabdomyosarcoma", "SIGNOR-RMS");
                     put("Thyroid cancer", "SIGNOR-TC");                   
                     put("Adipogenesis", "SIGNOR-AC");
                     put("AMPK", "SIGNOR-AMPK");
                     put("Autophagy", "SIGNOR-Autophagy");
                     put("Catecholamine", "SIGNOR-CM");
                     put("Cell cycle: G1/S phase transition", "SIGNOR-G1-S_trans");
                     put("Cell cycle: G2/M phase transitio", "SIGNOR-G2-M_trans");
                     put("Complementing signal", "SIGNOR-CS");
                     put("Death receptor signaling", "SIGNOR-DR");
                     put("EGFR signaling", "SIGNOR-EGF");
                     put("Fibrosis", "SIGNOR-Fibrosis");
                     put("Glucocorticoid receptor Signaling", "SIGNOR-GCR");
                     put("Hippo Signaling", "SIGNOR-HPP");
                     put("IL1 Signaling", "SIGNOR-IL1R");
                     put("IL6 Signaling", "SIGNOR-IL6");
                     put("Inflammosome Activation", "SIGNOR-Inflammosome");
                     put("Inhibition of Apoptosis", "SIGNOR-IOA");
                     put("Inflammosome Activation", "SIGNOR-Inflammosome");
                     put("Innate Immune Response", "SIGNOR-IIS");
                     put("Insulin Signaling", "SIGNOR-INSR");
                     put("Integrin Signaling", "SIGNOR-IS");
                     put("Leptin Signaling", "SIGNOR-LR");
                     put("Macrophage polarization", "SIGNOR-M1M2");
                     put("Mitochondrial control of Apoptosis", "SIGNOR-MCAPO");
                     put("MTOR Signaling", "SIGNOR-MS");
                     put("NF-KB Canonical", "SIGNOR-NFKBC");
                     put("NF-KB non canonical", "SIGNOR-NFKBNC");
                     put("NOTCH Signaling", "SIGNOR-NOTCH");
                     put("P38 Signaling", "SIGNOR-P38");
                     put("PI3K/AKT Signaling", "SIGNOR-PI3K-AKT");
                     put("SAPK/JNK Signaling", "SIGNOR-SAPK-JNK");
                     put("Sonic  HedgeHog", "SIGNOR-Hedgehog");
                     put("T Cell Activation", "SIGNOR-TCA");
                     put("TGF-beta Signaling", "SIGNOR-TGFb");
                     put("TNF-alpha", "SIGNOR-TA");
                     put("Toll like receptor", "SIGNOR-TLR");
                     put("VEGF Signaling", "SIGNOR-VEGF");
                     put("WNT Signaling", "SIGNOR-WNT");
                    }
        };
                     
    
/*SIGNOR-CCN COVID-19
SIGNOR-Myogenesis IGF
SIGNOR-NOTCH_Myogenesis NOTCH
SIGNOR-NSCLCN Non-small-cell
SIGNOR-P38_Myogenesis P38
SIGNOR-SARS-COV-APOPTOSIS SARS-COV
SIGNOR-SARS-CoV-MAPK-Pathway SARS-CoV
SIGNOR-SCAAE SARS-CoV
SIGNOR-SCCS SARS-CoV
SIGNOR-SCFI SARS-CoV
SIGNOR-SCIR SARS-CoV
SIGNOR-SCISOVG SARS-CoV
SIGNOR-SCSG SARS-CoV
SIGNOR-SCUP SARS-CoV
SIGNOR-WNT_Myogenesis WNT*/

        
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
               put("Interaction", String.class);
               put("TARGET_LOCATION", String.class);
               put("MECHANISM", String.class);
               put("RESIDUE", String.class);
               put("SEQUENCE", String.class);
               put("TAX_ID", Integer.class);
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.conceptualmodel.logic.Nodes;
import java.util.HashMap;

public class NodeField {
    public static final String ENTITY = "ENTITY";
    public static final String TYPE ="TYPE";
    public static final String ID = "ID";
    public static final String DATABASE= "DATABASE";
    public static final String SOURCE= "SOURCE";
    public static final String TARGET= "TARGET";
    
    public static final HashMap<String,Class<?>> NODEFIELD= new HashMap(){
        {
           put(ENTITY, String.class);
           put(TYPE, String.class);
           put(ID, String.class);
           put(DATABASE, String.class);
        }
    };
    public static final HashMap<String,String> NODEFIELDMAP= new HashMap(){
        {
           put("ENTITYA", ENTITY);
           put("TYPEA", TYPE);
           put("IDA", ID);
           put("DATABASEA", DATABASE);
           put("ENTITYB", ENTITY);
           put("TYPEB", TYPE);
           put("IDB", ID);
           put("DATABASEB", DATABASE);
        }
    };

    
    public static final String NAME = "NAME";
    public static final String FORMEDBY = "FORMEDBY";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String SIG_ID = "SIG_ID";
    public static final String COMPLEXPORTAL_ID = "COMPLEXPORTAL_ID";
    public static final String ENTITY_ALIAS = "ENTITY_ALIAS";
    public static final String ENTITY_DB_ID = "ENTITY_DB_ID";
    public static final String ALT_ID = "ALT_ID";
    public static final String FORMULA = "FORMULA";
    public static final String IUPAC = "IUPAC";
    public static final String FP_DESCRIPTION = "FP_DESCRIPTION";
    public static final String FP_SEQUENCE = "FP_SEQUENCE";
    public static final String IDS = "IDS";
    public static final String PF_DESCRIPTION = "PF_DESCRIPTION";
    public static final String STIM_DESCRIPTION = "STIM_DESCRIPTION";
    public static final String PHE_DESCRIPTION = "PHE_DESCRIPTION";
    public static final String MIRNA_DB_ID = "MIRNA_DB_ID";
    public static final String RNA_CENTRAL = "RNA_CENTRAL";
    public static final String GENE_NAME = "GENE_NAME";
    public static final String FUNCTION = "FUNCTION";
    public static final String SYNONYMS = "SYNONYMS";
    public static final String PATHWAYLISTADDINFO = "PATHWAYLIST";

    public static final HashMap<String,String> NODESUMMARYMAP= new HashMap(){
        {
           put(NAME,"NAME" );
           put(DATABASE,"DATABASE" );
           put(ID, "ID");
           put(TYPE, "TYPE");
           put(FORMEDBY, "FORMED BY");
           put(DESCRIPTION, "DESCRIPTION");
           put(SIG_ID, "SIGNOR ID");
           put(COMPLEXPORTAL_ID, "COMPLEXPORTAL ID");
           put(ENTITY, "ENTITY");
           put(ENTITY_ALIAS, "ALIAS");
           put(ENTITY_DB_ID, "ENTITY DB");
           put(ALT_ID, "ALT_ID");
           put(FORMULA, "FORMULA");
           put(IUPAC, "IUPAC");
           put(FP_DESCRIPTION, "FP DESCRIPTION");
           put(FP_SEQUENCE, "FP SEQUENCE");
           put(IDS, "IDS");
           put(PF_DESCRIPTION, "PF DESCRIPTION");
           put(STIM_DESCRIPTION, "STIMULUS DESCRIPTION");
           put(PHE_DESCRIPTION, "PHE. DESCRIPTION");
           put(MIRNA_DB_ID, "MIRNA DB ID");
           put(RNA_CENTRAL, "RNA CENTRAL");
           put(GENE_NAME, "GENE NAME");
           put(FUNCTION, "FUNCTION");
           put(PATHWAYLISTADDINFO, "PATHWAYLISTADDINFO");
        }
    };
    public static final HashMap<String,Class<?>> NODEFIELDADDITIONAL= new HashMap(){
        {
           put(NAME, String.class);
           put(FORMEDBY, String.class);
           put(DESCRIPTION, String.class);
           put(SIG_ID, String.class);
           put(COMPLEXPORTAL_ID, String.class);
           put(ENTITY_ALIAS, String.class);
           put(ENTITY_DB_ID, String.class);
           put(ALT_ID, String.class);
           put(FORMULA, String.class);
           put(IUPAC, String.class);
           put(FP_DESCRIPTION, String.class);
           put(FP_SEQUENCE, String.class);
           put(IDS, String.class);
           put(PF_DESCRIPTION, String.class);
           put(STIM_DESCRIPTION, String.class);
           put(PHE_DESCRIPTION, String.class);
           put(MIRNA_DB_ID, String.class);
           put(RNA_CENTRAL, String.class);
           put(GENE_NAME, String.class);
           put(FUNCTION, String.class);   
           put(SYNONYMS, String.class);   
           put(PATHWAYLISTADDINFO, String.class);
        }
    };

}

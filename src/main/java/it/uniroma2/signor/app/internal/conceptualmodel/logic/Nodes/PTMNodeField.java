/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.conceptualmodel.logic.Nodes;
import java.util.HashMap;

public class PTMNodeField {
    public static final String RESIDUE = "RESIDUE";
    public static final String TYPE = "TYPE";
    public static final String SEQUENCE ="SEQUENCE";
    public static final String SOURCE ="SOURCE";    
    
    public static final HashMap<String,Class<?>> PTMNODEFIELD= new HashMap(){
        {
           put(RESIDUE, String.class);
           put(TYPE, String.class);
           put(SEQUENCE, String.class);
           put(SOURCE, Long.class);           
        }
    };

}

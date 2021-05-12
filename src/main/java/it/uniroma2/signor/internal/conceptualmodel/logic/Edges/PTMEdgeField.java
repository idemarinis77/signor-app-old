/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.conceptualmodel.logic.Edges;
import java.util.HashMap;

/**
 *
 * @author amministratore
 */
public class PTMEdgeField {
    public static final String EdgeParent = "EdgeParent";
    public static final String NodeSourceSUID = "NodeSourceSUID";
    public static final String  NodeTargetSUID = "NodeTargetSUID";
    public static final String  INTERACTION = "INTERACTION";
    
    public static final HashMap<String,Class<?>> PTMEDGEFIELD= new HashMap(){
        {
           put(EdgeParent, Long.class);           
           put(NodeSourceSUID, Long.class);           
           put(NodeTargetSUID, Long.class);           
           put(INTERACTION, String.class);
        }
    };
}
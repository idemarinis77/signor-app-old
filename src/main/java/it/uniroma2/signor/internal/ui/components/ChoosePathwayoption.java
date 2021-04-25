/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.ui.components;
import javax.swing.*;
import java.awt.*;

import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.utils.EasyGBC;

import it.uniroma2.signor.internal.Config;
import javax.swing.event.ChangeEvent;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.event.ActionEvent;


        
/**
 *
 * @author amministratore
 */
public class ChoosePathwayoption extends JPanel{
    private SignorManager manager;
    private Config CONFIG= new Config();
    private EasyGBC egb=new EasyGBC();

    //private JLabel specie = new JLabel("Choose specie");
    private JComboBox pathway = new JComboBox(CONFIG.SPECIES.keySet().toArray());
    private JComboBox disease = new JComboBox(CONFIG.SPECIES.keySet().toArray());
    private JComboBox tumor = new JComboBox(CONFIG.SPECIES.keySet().toArray());
    private JButton enableAll= new JButton("EnableAll");
    
    public ChoosePathwayoption(SignorManager manager){
        super(new GridBagLayout());
        EasyGBC layoutHelper = new EasyGBC().insets(10, 5, 5, 5);
        setBackground(Color.LIGHT_GRAY);        
        
        //connbutton.addChangeListener(this);
       
        
        Box pathway_box = Box.createVerticalBox();
        pathway_box.add(pathway);
        pathway_box.setBorder(BorderFactory.createTitledBorder("Pathway"));
        add(pathway_box, layoutHelper.anchor("north"));
        
        
        Box disease_box = Box.createVerticalBox();
        disease_box.add(disease);
        disease_box.setBorder(BorderFactory.createTitledBorder("Disease pathway"));
        add(disease_box, layoutHelper.down());
        
        Box tumor_box = Box.createVerticalBox();
        tumor_box.add(tumor);
        tumor_box.setBorder(BorderFactory.createTitledBorder("Tumor pathway"));
        add(tumor_box, layoutHelper.down());
        
        add(enableAll, layoutHelper.down());
        enableAll.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                    {  disease.setEnabled(true);
                       pathway.setEnabled(true);
                       tumor.setEnabled(true);                     
                }}); 
        
        
        this.manager=manager;    
        
        pathway.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                    {  disease.setEnabled(false);
                       tumor.setEnabled(false);                     
                }}); 
        tumor.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                    {  disease.setEnabled(false);
                       pathway.setEnabled(false);                     
                }});  
        disease.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                    {  tumor.setEnabled(false);
                       pathway.setEnabled(false);                     
                }});  
    }
    
//    @Override
//    public void actionPerformed(ActionEvent e){
//        
//    }

            
    public HashMap<String, String> getParameter(){
        if(tumor.isEnabled()){
            HashMap<String, String> formvalues = new HashMap<>() {
                {put ("TUMORPTH", (String) tumor.getSelectedItem());}
                 
            };
            return formvalues;
        }
        if(pathway.isEnabled()){
            HashMap<String, String> formvalues = new HashMap<>() {
                {put ("PATHWAYPTH", (String) pathway.getSelectedItem());}
                 
            };
            return formvalues;
        }
        if(disease.isEnabled()){
            HashMap<String, String> formvalues = new HashMap<>() {
                {put ("DISEASEPTH", (String) disease.getSelectedItem());}
                 
            };
            return formvalues;
        }
        else return null;
    }
    
    public boolean isReady(){
        if(tumor.getSelectedItem()!= null) return true;
        if(pathway.getSelectedItem()!= null) return true;
        if(disease.getSelectedItem()!= null) return true;
        return false;
    }

}

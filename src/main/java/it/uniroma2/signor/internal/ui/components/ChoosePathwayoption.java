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
import it.uniroma2.signor.internal.ConfigPathway;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.task.query.factories.SignorPathwayQueryFactory;
import it.uniroma2.signor.internal.task.query.SignorPathwayResultTask;
import it.uniroma2.signor.internal.task.query.factories.SignorGenericRetrieveResultFactory;
import javax.swing.event.ChangeEvent;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.util.stream.Collectors;
import javax.swing.event.ChangeListener;
import org.cytoscape.work.TaskFactory;
import it.uniroma2.signor.internal.event.SignorNetworkPathCreatedListener;
import it.uniroma2.signor.internal.event.SignorNetworkPathToCreateEvent;

        
/**
 *
 * @author amministratore
 */
public class ChoosePathwayoption extends JPanel {
    private SignorManager manager;
    
    private EasyGBC egb=new EasyGBC();

    //private JLabel specie = new JLabel("Choose specie");
    private JComboBox pathway = new JComboBox(ConfigPathway.PathwayList.values().toArray());
    private JComboBox disease = new JComboBox(ConfigPathway.PathwayDiseaseList.values().toArray());
    private JComboBox tumor = new JComboBox(ConfigPathway.PathwayTumorList.values().toArray());
    private JButton clear= new JButton("Clear");
    private JButton select= new JButton("Select");
    
    public ChoosePathwayoption(SignorManager manager){
        super(new GridBagLayout());
        EasyGBC layoutHelper = new EasyGBC().insets(10, 5, 5, 5);
        setBackground(Color.LIGHT_GRAY);        
               
        Box pathway_box = Box.createVerticalBox();
        pathway_box.add(pathway);
        pathway_box.setBorder(BorderFactory.createTitledBorder("Pathway"));
        add(pathway_box, layoutHelper.anchor("northwest"));        
        
        Box disease_box = Box.createVerticalBox();
        disease_box.add(disease);
        disease_box.setBorder(BorderFactory.createTitledBorder("Disease pathway"));
        add(disease_box, layoutHelper.down().anchor("west"));
        
        Box tumor_box = Box.createVerticalBox();
        tumor_box.add(tumor);
        tumor_box.setBorder(BorderFactory.createTitledBorder("Tumor pathway"));
        add(tumor_box, layoutHelper.down().anchor("west"));
        
        add(clear, layoutHelper.down());
        clear.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                    {  disease.setEnabled(true);
                       pathway.setEnabled(true);
                       tumor.setEnabled(true);                     
                }}); 
        add(select,layoutHelper.right());       
        select.addActionListener(e -> buildNetworkPathFromSelection());
        
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
        //initSelect();
    }
    
//    @Override
//    public void actionPerformed(ActionEvent e){
//        
//    }
//    public void addListener(Runnable listener) {
//        select.addChangeListener((changeEvent) -> listener.run());
//    }
//    public void stateChanged (ChangeEvent e) {
//        if(isReady()){
            
            
    public HashMap<String, String> getParameter(){
        if(tumor.isEnabled()){
            HashMap<String, String> formvalues = new HashMap<>() {
                {put ("PATHWAYID", (String) tumor.getSelectedItem());}                 
            };
            return formvalues;
        }
        if(pathway.isEnabled()){
            HashMap<String, String> formvalues = new HashMap<>() {
                {put ("PATHWAYID", (String) pathway.getSelectedItem());}                 
            };
            return formvalues;
        }
        if(disease.isEnabled()){
            HashMap<String, String> formvalues = new HashMap<>() {
                {put ("PATHWAYID", (String) disease.getSelectedItem());}                 
            };
            return formvalues;
        }
        else return null;
    }
   
    public boolean isReady(){
        if(tumor.getSelectedIndex() == 0 && tumor.isEnabled()) return false;
        if(pathway.getSelectedIndex() == 0 && pathway.isEnabled()) return false;
        if(disease.getSelectedIndex() == 0 && disease.isEnabled()) return false;
        if(tumor.getSelectedItem()!= null && !pathway.isEnabled() && !disease.isEnabled()) return true;
        if(pathway.getSelectedItem()!= null && !tumor.isEnabled() && !disease.isEnabled()) return true;
        if(disease.getSelectedItem()!= null && !tumor.isEnabled() && !pathway.isEnabled()) return true;

//        if(pathway.getSelectedItem()!= null) return true;
//        if(disease.getSelectedItem()!= null) return true;
        return false;
    }

    private void buildNetworkPathFromSelection(){
          if(this.isReady()) {
            manager.utils.info("Index selezionato "+tumor.getSelectedIndex());
            SignorPathwayQueryFactory spq = new SignorPathwayQueryFactory(this.manager);
            spq.parameters_shift = this.getParameter();
            spq.param_shift = true;
            manager.utils.execute(spq.createTaskIterator());
          }//       
    }    
   
}

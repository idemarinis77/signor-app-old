/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.ui.components;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import it.uniroma2.signor.app.internal.utils.EasyGBC;
import it.uniroma2.signor.app.internal.Config;
import it.uniroma2.signor.app.internal.ConfigResources;
import it.uniroma2.signor.app.internal.utils.HttpUtils;
import it.uniroma2.signor.app.internal.task.query.factories.SignorPathwayQueryFactory;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Pathway.PathwayField;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;

public class ChoosePathwayoption extends JPanel {
    private final SignorManager manager;    
    private JComboBox pathway;
    private JComboBox disease;
    private JComboBox tumor;
    private JComboBox covid;
    private final JButton clear= new JButton("Clear");
    private final JButton select= new JButton("Select");
    EasyGBC layoutHelper = new EasyGBC().insets(10, 5, 5, 5);
    private final HashMap<String,String> pathid_desc = new HashMap();
    
    public ChoosePathwayoption(SignorManager manager){
        super(new GridBagLayout());        
        setBackground(Color.LIGHT_GRAY);  
        this.manager=manager;
        init();
    }
    
    public void init(){
        BufferedReader brp = HttpUtils.getHTTPSignor(ConfigResources.PATHLIST, manager);
        ArrayList<String> results = HttpUtils.parseWS(brp, Config.HEADERSINGLESEARCH, false, manager);
        HashMap<String,String> covid_list = new HashMap();
        HashMap<String,String> disease_list = new HashMap();
        HashMap<String,String> pathway_list = new HashMap();
        HashMap<String,String> tumor_list = new HashMap();
        
        for (int i = 0; i < results.size(); i++) {  
            String[] attributes = results.get(i).split("\t");
            pathid_desc.put(attributes[1], attributes[0]);
            if(attributes[2].equals("COVID")){
                covid_list.put(attributes[0], attributes[1]);
            }
            else if (attributes[2].equals("disease")){
                disease_list.put(attributes[0], attributes[1]);
            }
            else if (attributes[2].equals("pathway")){
                pathway_list.put(attributes[0], attributes[1]);
            }
            else if (attributes[2].equals("tumor")){
                tumor_list.put(attributes[0], attributes[1]);
            }
        }
        Object[] pathlist = pathway_list.values().toArray();
        Arrays.sort(pathlist);
        Object[] pathlist2 = Arrays.copyOf(pathlist, pathlist.length + 1);
        pathlist2[0]= (Object) "Select Pathway";
        System.arraycopy(pathlist, 0, pathlist2, 1, pathlist.length);
        pathway = new JComboBox(pathlist2);
        
        Object[] pathdeslist = disease_list.values().toArray();
        Arrays.sort(pathdeslist);
        Object[] pathdeslist2 = Arrays.copyOf(pathdeslist, pathdeslist.length + 1);
        pathdeslist2[0]= (Object) "Select Disease pathway";
        System.arraycopy(pathdeslist, 0, pathdeslist2, 1, pathdeslist.length);
        disease = new JComboBox(pathdeslist2);
        
        Object[] pathtumlist = tumor_list.values().toArray();
        Arrays.sort(pathtumlist);
        Object[] pathtumlist2 = Arrays.copyOf(pathtumlist, pathtumlist.length + 1);
        pathtumlist2[0]= (Object) "Select Tumor pathway";
        System.arraycopy(pathtumlist, 0, pathtumlist2, 1, pathtumlist.length);
        tumor = new JComboBox(pathtumlist2);       
        
        Object[] covidlist = covid_list.values().toArray();
        Arrays.sort(covidlist);
        Object[] covidlist2 = Arrays.copyOf(covidlist, covidlist.length + 1);
        covidlist2[0]= (Object) "Select COVID pathway";
        System.arraycopy(covidlist, 0, covidlist2, 1, covidlist.length);
        covid = new JComboBox(covidlist2);           
        
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
        
        Box covid_box = Box.createVerticalBox();
        covid_box.add(covid);
        covid_box.setBorder(BorderFactory.createTitledBorder("COVID"));
        add(covid_box, layoutHelper.down().anchor("west"));  
        
        add(clear, layoutHelper.down());
        clear.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                    {  covid.setEnabled(true);
                       disease.setEnabled(true);
                       pathway.setEnabled(true);
                       tumor.setEnabled(true);                     
                }}); 
        add(select,layoutHelper.right());       
        select.addActionListener(e -> buildNetworkPathFromSelection());
         

        covid.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                    {  disease.setEnabled(false);
                       tumor.setEnabled(false);      
                       pathway.setEnabled(false);
                }});
        pathway.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                    {  disease.setEnabled(false);
                       tumor.setEnabled(false);      
                       covid.setEnabled(false);
                }}); 
        tumor.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                    {  disease.setEnabled(false);
                       pathway.setEnabled(false);    
                       covid.setEnabled(false);
                }});  
        disease.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                    {  tumor.setEnabled(false);
                       pathway.setEnabled(false);   
                       covid.setEnabled(false);
                }});  
     }
          
    public HashMap<String, Object> getParameter(){
        if(covid.isEnabled()){
            HashMap<String, Object> formvalues = new HashMap<>() {
                {put (PathwayField.PATHWAYID, (String) pathid_desc.get(covid.getSelectedItem()));}                 
            };
            return formvalues;
        }
        if(tumor.isEnabled()){
            HashMap<String, Object> formvalues = new HashMap<>() {
                {put (PathwayField.PATHWAYID, (String) pathid_desc.get(tumor.getSelectedItem()));}                 
            };
            return formvalues;
        }
        if(pathway.isEnabled()){
            HashMap<String, Object> formvalues = new HashMap<>() {
                {put (PathwayField.PATHWAYID, (String) pathid_desc.get(pathway.getSelectedItem()));}                 
            };
            return formvalues;
        }
        if(disease.isEnabled()){
            HashMap<String, Object> formvalues = new HashMap<>() {
                {put (PathwayField.PATHWAYID, (String) pathid_desc.get(disease.getSelectedItem()));}              
            };
            return formvalues;
        }
        else return null;
    }
   
    public boolean isReady(){
        if(covid.getSelectedIndex() == 0 && covid.isEnabled()) return false;
        if(tumor.getSelectedIndex() == 0 && tumor.isEnabled()) return false;
        if(pathway.getSelectedIndex() == 0 && pathway.isEnabled()) return false;
        if(disease.getSelectedIndex() == 0 && disease.isEnabled()) return false;
        if(covid.getSelectedItem()!= null && !pathway.isEnabled() && !disease.isEnabled() && !tumor.isEnabled()) return true;
        if(tumor.getSelectedItem()!= null && !pathway.isEnabled() && !disease.isEnabled() && !covid.isEnabled()) return true;
        if(pathway.getSelectedItem()!= null && !tumor.isEnabled() && !disease.isEnabled() && !covid.isEnabled()) return true;
        if(disease.getSelectedItem()!= null && !tumor.isEnabled() && !pathway.isEnabled() && !covid.isEnabled()) return true;
        return false;
    }

    private void buildNetworkPathFromSelection(){
          if(this.isReady()) {
            SignorPathwayQueryFactory spq = new SignorPathwayQueryFactory(this.manager);
            spq.parameters_shift = this.getParameter();
            spq.param_shift = true;
            manager.utils.execute(spq.createTaskIterator());
            //Re-enable all pathway lists
            covid.setEnabled(true);
            disease.setEnabled(true);
            pathway.setEnabled(true);
            tumor.setEnabled(true);  
          }      
    }    
   
}

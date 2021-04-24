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
import javax.swing.event.ChangeListener;

import java.util.HashMap;
import java.util.ArrayList;

/**
 *
 * @author amministratore
 */
public class ChooseSearchoption extends JPanel implements ChangeListener{
    private SignorManager manager;
    private Config CONFIG= new Config();
    private EasyGBC egb=new EasyGBC();
    private ButtonGroup btn = new ButtonGroup();
    //private JLabel specie = new JLabel("Choose specie");
    private JComboBox organism = new JComboBox(CONFIG.SPECIES.keySet().toArray());
    private JRadioButton singlebutton = new JRadioButton(CONFIG.SEARCHOPTION.get("SINGLESEARCH"), true);
    private JRadioButton allbutton = new JRadioButton(CONFIG.SEARCHOPTION.get("ALLSEARCH"), true);
    private JRadioButton connbutton= new JRadioButton(CONFIG.SEARCHOPTION.get("CONNECTSEARCH"), false);
    private JRadioButton shortpath= new JRadioButton(CONFIG.SEARCHOPTION.get("SHORTESTPATHSEARCH"), false);
    private JRadioButton inclfirstnei = new JRadioButton(CONFIG.SEARCHOPTION.get("INCFIRSTNEISEARCH"), false);
    
    public ChooseSearchoption(SignorManager manager){
        super(new GridBagLayout());
        EasyGBC layoutHelper = new EasyGBC().insets(10, 5, 5, 5);
        setBackground(Color.LIGHT_GRAY);        
        inclfirstnei.setEnabled(false);
        connbutton.addChangeListener(this);
        
        Box searchoptions = Box.createVerticalBox();
        btn.add(singlebutton);
        btn.add(allbutton);
        btn.add(connbutton);
        btn.add(shortpath);
        searchoptions.add(singlebutton);
        searchoptions.add(allbutton);
        searchoptions.add(connbutton);
        searchoptions.add(inclfirstnei);
        searchoptions.add(shortpath);
        searchoptions.setBorder(BorderFactory.createTitledBorder("Search type"));
        add(searchoptions, layoutHelper.anchor("north"));
        
        Box species = Box.createVerticalBox();
        species.add(organism);
        species.setBorder(BorderFactory.createTitledBorder("Species"));
        add(species, layoutHelper.down());
        
        this.manager=manager;    
    }
    
    public void addListener(Runnable listener) {
        connbutton.addChangeListener((changeEvent) -> listener.run());
    }
    public void stateChanged (ChangeEvent e) {
        if(connbutton.isSelected())
                inclfirstnei.setEnabled(true);
        else inclfirstnei.setEnabled(false);
    }
            
    public HashMap<String, Object> getParameter(){
        HashMap<String, Object> formvalues = new HashMap<>() {
            {put ("SINGLESEARCH", singlebutton.isSelected());
             put ("ALLSEARCH", allbutton.isSelected());
             put ("CONNECTSEARCH", connbutton.isSelected());
             put ("SHORTESTPATHSEARCH", shortpath.isSelected());
             put ("INCFIRSTNEISEARCH", inclfirstnei.isSelected() && inclfirstnei.isEnabled());
             put ("SPECIES", organism.getSelectedItem());
            }
        };
        return formvalues;
    }

}

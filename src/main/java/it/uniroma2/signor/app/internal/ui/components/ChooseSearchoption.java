/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.ui.components;
import javax.swing.*;
import java.awt.*;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import it.uniroma2.signor.app.internal.utils.EasyGBC;
import it.uniroma2.signor.app.internal.Config;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.HashMap;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.NetworkField;

public class ChooseSearchoption extends JPanel implements ChangeListener{
    private final ButtonGroup btn = new ButtonGroup();
    //private JLabel specie = new JLabel("Choose specie");
    private final JComboBox organism = new JComboBox(Config.SPECIESLIST.keySet().toArray());
    private final JRadioButton singlebutton = new JRadioButton("entity query", false);
    private final JRadioButton allbutton = new JRadioButton("all interactions", true);
    private final JRadioButton connbutton= new JRadioButton("connect entity in the list", false);
    private final JRadioButton shortpath= new JRadioButton("shortest path", false);
    private final JRadioButton inclfirstnei = new JRadioButton("connect by including bridge entities", false);
    
    public ChooseSearchoption(SignorManager manager){
        super(new GridBagLayout());
        EasyGBC layoutHelper = new EasyGBC().insets(10, 5, 5, 5);
        setBackground(Color.LIGHT_GRAY);        
        inclfirstnei.setEnabled(false);
        connbutton.addChangeListener(this);
        
        Box searchoptions = Box.createVerticalBox();
        btn.add(singlebutton);        
        btn.add(connbutton);
        btn.add(shortpath);
        btn.add(allbutton);
        searchoptions.add(singlebutton);        
        searchoptions.add(connbutton);
        searchoptions.add(inclfirstnei);
        searchoptions.add(shortpath);
        searchoptions.add(allbutton);
        searchoptions.setBorder(BorderFactory.createTitledBorder("Search type"));
        add(searchoptions, layoutHelper.anchor("north"));
        
        Box species = Box.createVerticalBox();
        species.add(organism);
        species.setBorder(BorderFactory.createTitledBorder("Species"));
        add(species, layoutHelper.down());        
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
            {put (NetworkField.SINGLESEARCH, singlebutton.isSelected());
             put (NetworkField.ALLSEARCH, allbutton.isSelected());
             put (NetworkField.CONNECTSEARCH, connbutton.isSelected());
             put (NetworkField.SHORTESTPATHSEARCH, shortpath.isSelected());
             put (NetworkField.INCFIRSTNEISEARCH, inclfirstnei.isSelected() && inclfirstnei.isEnabled());
             put (NetworkField.SPECIES, organism.getSelectedItem());
            }
        };

        return formvalues;
    }

}

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
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.NetworkField;

public class ChooseSearchoption extends JPanel implements ChangeListener{
    private final ButtonGroup btn = new ButtonGroup();
    //private JLabel specie = new JLabel("Choose specie");
    private final JComboBox organism = new JComboBox(Config.SPECIESLIST.keySet().toArray());
    private final JRadioButton singlebutton = new JRadioButton("single", false);
    private final JRadioButton allbutton = new JRadioButton("all", true);
    private final JRadioButton connbutton= new JRadioButton("connect", false);
    private final JRadioButton shortpath= new JRadioButton("shortest path", false);
    private final JRadioButton inclfirstnei = new JRadioButton("include bridge proteins query", false);
    
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

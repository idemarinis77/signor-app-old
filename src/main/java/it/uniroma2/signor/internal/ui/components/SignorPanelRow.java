/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.ui.components;
import org.cytoscape.model.CyRow;
import it.uniroma2.signor.internal.utils.EasyGBC;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.ConfigResources;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.io.File;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.utils.IconUtils;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.cytoscape.util.swing.OpenBrowser;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amministratore
 */

public class SignorPanelRow extends JPanel{
    
    private SignorManager manager;
    
    public SignorPanelRow (Integer row, Integer columns, SignorManager manager){
        super(new GridLayout(row, columns));
        this.setBackground(Color.white);
        this.setBorder(BorderFactory.createBevelBorder(0, Color.white, Color.white, new Color(82,166,119), Color.white));
        this.manager = manager;
    }   
    
    public void signorPanelRowDetailNode(JPanel jp, EasyGBC gbc, CyRow rownode){
        SignorLabelStyledBold id = new SignorLabelStyledBold("ID");
        this.add(id, gbc.position(0,0));
        JLabel id_value = new JLabel(rownode.get(Config.NAMESPACE, "ID", String.class));
        this.add(id_value, gbc.right().spanHoriz(2));
        SignorLabelStyledBold name = new SignorLabelStyledBold("Name");
        this.add(name, gbc.position(0,1));
        JLabel name_value = new JLabel(rownode.get(Config.NAMESPACE, "ENTITY", String.class));
        this.add(name_value, gbc.right().spanHoriz(2));
        SignorLabelStyledBold db = new SignorLabelStyledBold("DB");
        this.add(db, gbc.position(0,2));
        JLabel db_value = new JLabel(rownode.get(Config.NAMESPACE, "DATABASE", String.class));
        this.add(db_value, gbc.right()); 
        OpenBrowser openBrowser = manager.utils.getService(OpenBrowser.class);
        SignorLabelStyledBold dbLabel;        
        try{
            String db_value_norm = rownode.get(Config.NAMESPACE, "DATABASE", String.class).toLowerCase();
            String link_to_db = ConfigResources.DBLINKSMAP.get(db_value_norm).queryFunction.apply(rownode.get(Config.NAMESPACE, "ID", String.class));
            BufferedImage dblogo = ImageIO.read(getClass().getResource(ConfigResources.DBLOGOS.get(db_value_norm)));
            dbLabel = new SignorLabelStyledBold(new ImageIcon(dblogo), link_to_db, openBrowser, false);
           
            jp.add(this, gbc.down());
            jp.add(dbLabel, gbc.down().anchor("west"));
        }
        catch (Exception e){
            manager.utils.warn("Warning with renderning image database "+e.toString());
            manager.utils.info("valore del db "+rownode.get(Config.NAMESPACE, "DATABASE", String.class));
            jp.add(this, gbc.down());
        }                  
        
    }
    
    public void signorPanelRowDetailEdge(JPanel jp, EasyGBC gbc, CyRow edgenode){

        SignorLabelStyledBold id = new SignorLabelStyledBold("ID");
        this.add(id, gbc.position(0,0));
        JLabel id_value = new JLabel(edgenode.get(Config.NAMESPACE, "SIGNOR_ID", String.class));
        this.add(id_value, gbc.right());
        
        SignorLabelStyledBold mechanism = new SignorLabelStyledBold("MECHANISM");
        this.add(mechanism, gbc.position(0,1));
        JLabel mechanism_value = new JLabel(edgenode.get(Config.NAMESPACE, "MECHANISM", String.class));
        this.add(mechanism_value, gbc.right());
        
        SignorLabelStyledBold organism = new SignorLabelStyledBold("ORGANISM");
        this.add(organism, gbc.position(0,2));
        JLabel organism_value = new JLabel(edgenode.get(Config.NAMESPACE, "TAX_ID", Integer.class).toString());
        this.add(organism_value, gbc.right());
        
        SignorLabelStyledBold modification = new SignorLabelStyledBold("MODIFICATION");
        this.add(modification, gbc.position(0,3));
        JLabel modification_value = new JLabel(edgenode.get(Config.NAMESPACE, "MODIFICATIONA", String.class));
        this.add(modification_value, gbc.right());
        
        SignorLabelStyledBold pmid = new SignorLabelStyledBold("PubMed ID");
        this.add(pmid, gbc.position(0,4));
        JLabel pmid_value = new JLabel(edgenode.get(Config.NAMESPACE, "PMID", String.class));
        this.add(pmid_value, gbc.right());
        
        SignorLabelStyledBold annotation = new SignorLabelStyledBold("Annotation");
        this.add(annotation, gbc.position(0,5));     
        String annotation_values = edgenode.get(Config.NAMESPACE, "SENTENCE", String.class);
        //Split my very long annotation
        /*String wrapped_annotation_values = "";
        int index = 0;
        while (index < substring_annotation_values.length()) {
            wrapped_annotation_values += substring_annotation_values.substring(index, Math.min(index + 30,substring_annotation_values.length()))+"<\n>";
            index += 15;
        }
        info.setToolTipText(wrapped_annotation_values);*/
        this.add(new HelpButton(manager, annotation_values), gbc.right());
        
        /*JLabel annotation_value = new JLabel("<html>"+wrapped_annotation_values+"</html>");          
        */
        jp.add(this, gbc.down());
    }

}

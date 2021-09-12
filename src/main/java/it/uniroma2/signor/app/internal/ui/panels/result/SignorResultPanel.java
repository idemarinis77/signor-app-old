/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.ui.panels.result;
import java.awt.GridBagLayout;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Color;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.app.internal.ui.components.VerticalPanel;
import it.uniroma2.signor.app.internal.ui.components.CenteredLabel;
import it.uniroma2.signor.app.internal.utils.EasyGBC;
import it.uniroma2.signor.app.internal.ui.components.SignorButton;
import it.uniroma2.signor.app.internal.ui.components.SignorLabelStyledBold;
import it.uniroma2.signor.app.internal.ui.components.SignorPanelRow;
import it.uniroma2.signor.app.internal.task.query.factories.SignorGenericRetrieveResultFactory;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import org.cytoscape.work.TaskFactory;
import it.uniroma2.signor.app.internal.Config;

public class SignorResultPanel extends JPanel {
    private JPanel descriptionPanel;
    final EasyGBC layoutHelper = new EasyGBC();
    private final String species;
    private final String search;
    private final String terms;
    private final Boolean includefirstneighbor;
    private final Network network;
    private final SignorManager manager;
    private final ArrayList<String> results;
    
    public SignorResultPanel(Integer numberresutls, ArrayList<String> results, String species, String search, Boolean includefirstneighbor, String terms, Network network){ 
        super(new GridBagLayout());
        this.layoutHelper.insets(10,10,10,10);
        
        descriptionPanel = new JPanel(new GridLayout(1,4));        
        this.network = network;
        this.species = species;
        this.search = search;
        this.includefirstneighbor = includefirstneighbor;
        this.terms= terms;
        this.manager = network.manager;
        this.results = results;
        init(numberresutls, species);     
    }
   
    private void init(Integer numberresutls, String species) {             
        
        descriptionPanel = new VerticalPanel();
        descriptionPanel.add(new CenteredLabel(numberresutls+" results found for organism "+species, 16, Color.BLACK), layoutHelper.down().expandHoriz());
        descriptionPanel.add(new CenteredLabel("Please select the element to query as seeds to build network.", 14, Color.BLACK), layoutHelper.down().expandHoriz());
        
        add(descriptionPanel, layoutHelper.expandHoriz());    
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> close());        
        
        JPanel header = new JPanel(new GridLayout(1, 4));
        SignorLabelStyledBold entity = new SignorLabelStyledBold("Entity");
        SignorLabelStyledBold primaryid = new SignorLabelStyledBold("Primary ID");
        SignorLabelStyledBold type = new SignorLabelStyledBold("Type");
        SignorLabelStyledBold goTo = new SignorLabelStyledBold("Link");
        add(header, layoutHelper.down());
        header.add(entity);
        header.add(primaryid);
        header.add(type);
        header.add(goTo);
        //Iterator lines = results.listIterator();
        JPanel table_of_result_to_scroll = new JPanel(new GridBagLayout());
        for ( Iterator lines = results.listIterator(); lines.hasNext();){
            
            int i = 0;
            String line = (String) lines.next();
            String[] fields = line.split("\t");
            if(Config.SPECIESLIST.get(species)=="10090" || Config.SPECIESLIST.get(species) == "10116")
                i++;
            SignorPanelRow listresults = new SignorPanelRow(1, 4);
            JLabel primaentity = new JLabel(fields[i]);
            JLabel primaid = new JLabel(fields[i+1]);
            listresults.add(primaentity, layoutHelper.insets(2,2,2,2));
            listresults.add(primaid);
            if(Config.SPECIESLIST.get(species)=="10090" || Config.SPECIESLIST.get(species) == "10116"){
                JLabel primatype = new JLabel("proteins");
                listresults.add(primatype);
            }
            else{ 
                JLabel primatype = new JLabel(fields[i+2]);
                listresults.add(primatype);
            }
            SignorButton link= new SignorButton("Get relations");
            //****************************************
            //*******************************************
            //ATTENTION TO VERIFY THE RESULTS WITH THE NEW HEADER
            final int j = i+1;
            link.addActionListener(e -> buildNetworkFromSelection(fields[j]));

            listresults.add(link);  
            table_of_result_to_scroll.add(listresults, layoutHelper.down());
        }
        table_of_result_to_scroll.add(cancelButton, layoutHelper.down().anchor("east"));
        JScrollPane scrollPane = new JScrollPane(table_of_result_to_scroll, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.getVerticalScrollBar().setBlockIncrement(10);
        add(scrollPane, layoutHelper.down().anchor("east").expandBoth());
     }
    
     private void buildNetworkFromSelection(String primaryID) {      
        new Thread(() -> {
            TaskFactory factory = new SignorGenericRetrieveResultFactory(search, includefirstneighbor, species, primaryID ,network);
            manager.utils.execute(factory.createTaskIterator());
            close();
        }).start();
    }
    
    private void close() {
        ((Window) getRootPane().getParent()).dispose();
    }
}

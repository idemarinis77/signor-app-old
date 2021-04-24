/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.ui.panels.legend;
import it.uniroma2.signor.internal.conceptualmodel.logic.Nodes.Node;
import it.uniroma2.signor.internal.utils.EasyGBC;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.ui.components.SignorLabelStyledBold;
import it.uniroma2.signor.internal.ui.components.SignorPanelRow;
/**
 *
 * @author amministratore
 */
import java.util.Properties;
import java.util.Iterator;
import java.util.ArrayList;
import java.awt.*;
import java.time.Instant;
import java.util.Collection;
import javax.swing.*;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.util.HashMap;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.events.SelectedNodesAndEdgesEvent;
import org.cytoscape.model.events.SelectedNodesAndEdgesListener;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTableUtil;

public class SignorSummaryPanel extends JPanel {
    private SignorManager manager;
    private JPanel summPanel;

    private EasyGBC gbc=new EasyGBC();
    public Boolean selectionRunning= false;
    public CyNetwork current_cynetwork_to_serch_into;
    private CyNode rootNode;
    /*private static final int SIDES = 2;
    private static final int SIDE_LENGTH = 60;
    private static final int GAP = 1;
    private static final Color BG = Color.WHITE;
    private static final Color CELL_COLOR = Color.WHITE;*/
    private HashMap<String,String> summary;
    Node networkRootNode;
    //Dimension prefSize = new Dimension(SIDE_LENGTH, SIDE_LENGTH);     
    
    public SignorSummaryPanel(SignorManager manager){
        setLayout(new GridBagLayout());
        this.manager = manager; 

        current_cynetwork_to_serch_into = manager.lastCyNetwork;
       /* setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        setLayout(new GridLayout(0, 2, GAP, GAP));*/
        
        
        JPanel SummaryInfo = new JPanel();
        SummaryInfo.setLayout(new GridBagLayout());
        //SummaryInfo.setLayout(new BorderLayout());
        SummaryInfo.setBackground(Color.WHITE);
        {
            EasyGBC gbc1=new EasyGBC();
            summPanel = new JPanel();
            summPanel.setBackground(Color.WHITE);
            SummaryInfo.add(summPanel, gbc1.down().anchor("north").expandHoriz());
            SummaryInfo.add(Box.createVerticalGlue(), gbc1.down().down().expandVert());
            //summPanel.setLayout(new GridBagLayout());
            //SummaryInfo.add(summPanel, BorderLayout.NORTH);
            //NodeInfo.add(Box.createVerticalGlue(), gbc1.down().expandVert());
        }
        JScrollPane scrollPane = new JScrollPane(SummaryInfo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.getVerticalScrollBar().setBlockIncrement(10);
        add(scrollPane, gbc.down().anchor("east").expandBoth());
        revalidate();
        repaint();   
        //createContent();
    }
    
    public void createContent(){
        try {           
       
            if(manager.presentationManager.signorNetMap.containsKey(current_cynetwork_to_serch_into)){
               rootNode = manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into).rootNode;
               networkRootNode = new Node(manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into), rootNode);
            }
            summary = networkRootNode.Summary();
            //SignorPanelRow listresults = new SignorPanelRow(summary.size()+1, 2, manager);
            Integer relations = manager.lastNetwork.numberOfEdes();
            Iterator iter = summary.keySet().iterator();
            Iterator iterv = summary.values().iterator();
            Integer it =0;
            summPanel.setLayout(new GridLayout(summary.size()+1, 2));
            while(iter.hasNext()){
                summPanel.add(new SignorLabelStyledBold(iter.next().toString()), gbc.position(0, it));
                summPanel.add(new JLabel(iterv.next().toString()), gbc.right());
                it ++;
            } 
            summPanel.add(new SignorLabelStyledBold("Relations "), gbc.down());
            summPanel.add(new JLabel(manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into).numberOfEdes().toString()), gbc.right());
            /*listresults.add(new SignorLabelStyledBold("Relations "), gbc.down());
            listresults.add(new JLabel(manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into).numberOfEdes().toString()));
            summPanel.add(listresults);*/
        }
        catch (Exception e){
            manager.utils.error(e.toString());
        }       
    }
    
    public void recreateContent(){
        if(manager.presentationManager.signorNetMap.containsKey(current_cynetwork_to_serch_into)){
            summPanel.removeAll();
            createContent();
        }
    }
}

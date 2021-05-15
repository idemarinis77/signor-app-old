/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.ui.panels.legend;
import it.uniroma2.signor.internal.conceptualmodel.logic.Edges.Edge;
import it.uniroma2.signor.internal.utils.EasyGBC;

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


import org.cytoscape.model.CyEdge;
import org.cytoscape.model.events.SelectedNodesAndEdgesEvent;
import org.cytoscape.model.events.SelectedNodesAndEdgesListener;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.ConfigResources;
import it.uniroma2.signor.internal.ui.components.SignorButton;
import it.uniroma2.signor.internal.ui.components.SignorLabelStyledBold;
import it.uniroma2.signor.internal.ui.components.SignorPanelRow;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.image.BufferedImage;
import it.uniroma2.signor.internal.ui.components.HelpButton;
import java.util.HashMap;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.util.swing.OpenBrowser;

public class SignorEdgePanel extends JPanel {
    private SignorManager manager;
    private JPanel edgesPanel;
    private EasyGBC gbc=new EasyGBC();
    public Boolean selectionRunning= false;
    public CyNetwork current_cynetwork_to_serch_into;

    
    public SignorEdgePanel(SignorManager manager){
        this.manager = manager;
        setLayout(new GridBagLayout());
        current_cynetwork_to_serch_into = manager.lastCyNetwork;        
        JPanel edgeInfo = new JPanel();
        edgeInfo.setLayout(new BorderLayout());
        edgeInfo.setBackground(Color.WHITE);
        {
            EasyGBC gbc1=new EasyGBC();
            edgesPanel = new JPanel();
            edgesPanel.setBackground(Color.WHITE);
            edgesPanel.setLayout(new GridBagLayout());
            edgeInfo.add(edgesPanel, BorderLayout.NORTH);
        }
        JScrollPane scrollPane = new JScrollPane(edgeInfo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.getVerticalScrollBar().setBlockIncrement(10);
        add(scrollPane, gbc.down().anchor("east").expandBoth());
        revalidate();
        repaint();        
    }   
    
    public void selectedEdges() {
        
        this.selectionRunning=true;
        Network network = manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into);
        Collection<CyEdge> selectedEdges = CyTableUtil.getEdgesInState(current_cynetwork_to_serch_into, CyNetwork.SELECTED, true);   
        if(selectedEdges.size()>0){
            edgesPanel.removeAll();

            JPanel separator  = new JPanel();
            separator.setLayout(new GridBagLayout());
            separator.add(new SignorLabelStyledBold(">> Edge info "), gbc.down().anchor("west"));
            separator.setBackground(new Color(82, 166, 119));
            edgesPanel.add(separator, gbc.down().anchor("west").insets(2,0,2,0));
        }
        Iterator iter_sel_edges = selectedEdges.iterator();
        while(iter_sel_edges.hasNext()){
            
            CyEdge edge_current = (CyEdge) iter_sel_edges.next();
            Edge edge = network.getEdges().get(edge_current);            
            HashMap <String,String> summary = edge.getSummary();         
           
            Iterator iter = summary.keySet().iterator();
            Iterator iterv = summary.values().iterator();
            JPanel edgeinfo = new JPanel();
            edgeinfo.setLayout(new GridBagLayout());
            edgeinfo.add(new SignorLabelStyledBold("SOURCE - TARGET"), gbc.down());
            edgeinfo.add(new JLabel(edge.source.toString()+"-"+edge.target.toString()), gbc.right());
            while(iter.hasNext()){
                String key = iter.next().toString();
                String value = iterv.next().toString();
                SignorLabelStyledBold id = new SignorLabelStyledBold(key);
                edgeinfo.add(id, gbc.down());
                if(value.length() > 20)
                    edgeinfo.add(new HelpButton(manager, value), gbc.right());
                else edgeinfo.add(new JLabel(value), gbc.right());
            }           
            edgesPanel.add(edgeinfo, gbc.down());
        }              
    }
}

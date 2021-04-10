/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.ui.panels.legend;
import it.uniroma2.signor.internal.conceptualmodel.logic.Nodes.Node;
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
import it.uniroma2.signor.internal.ui.components.SignorPanelRow;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.util.HashMap;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTableUtil;

public class SignorEdgePanel extends JPanel {
    private SignorManager manager;
    private JPanel edgesPanel;
    private Config CONFIG = new Config();
    private EasyGBC gbc=new EasyGBC();
    public Boolean selectionRunning= false;
    
    public SignorEdgePanel(SignorManager manager){
        this.manager = manager;
        setLayout(new GridBagLayout());
        
        
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
        edgesPanel.removeAll();
        this.selectionRunning=true;
        //EasyGBC gbc=new EasyGBC();        
        Collection<CyEdge> selectedEdges = CyTableUtil.getEdgesInState(manager.lastNetwork.getCyNetwork(), CyNetwork.SELECTED, true);   
        
        SignorPanelRow edge_current_info = new SignorPanelRow(6,2, this.manager);
        CyEdge edge_current = selectedEdges.iterator().next();
        
        CyRow rowedge = this.manager.lastCyNetwork.getDefaultEdgeTable().getRow(edge_current.getSUID());
        edge_current_info.signorPanelRowDetailEdge(edgesPanel,gbc, rowedge);       
    }
}

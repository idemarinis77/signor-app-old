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


import org.cytoscape.model.CyNode;
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

public class SignorNodePanel extends JPanel {
    private SignorManager manager;
    private JPanel nodesPanel;
    private Config CONFIG = new Config();
    private EasyGBC gbc=new EasyGBC();
    public Boolean selectionRunning= false;
    
    public SignorNodePanel(SignorManager manager){
        this.manager = manager;
        setLayout(new GridBagLayout());        
        
        JPanel NodeInfo = new JPanel();
        NodeInfo.setLayout(new BorderLayout());
        NodeInfo.setBackground(Color.WHITE);
        {
            EasyGBC gbc1=new EasyGBC();
            nodesPanel = new JPanel();
            nodesPanel.setBackground(Color.WHITE);
            nodesPanel.setLayout(new GridBagLayout());
            NodeInfo.add(nodesPanel, BorderLayout.NORTH);
            //NodeInfo.add(Box.createVerticalGlue(), gbc1.down().expandVert());
        }
        JScrollPane scrollPane = new JScrollPane(NodeInfo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.getVerticalScrollBar().setBlockIncrement(10);
        add(scrollPane, gbc.down().anchor("east").expandBoth());
        revalidate();
        repaint();        
    }   
    
    public void selectedNodes() {
        nodesPanel.removeAll();
        this.selectionRunning=true;
        //EasyGBC gbc=new EasyGBC();        
        Collection<CyNode> selectedNodes = CyTableUtil.getNodesInState(manager.lastNetwork.getCyNetwork(), CyNetwork.SELECTED, true);        
        SignorPanelRow node_current_info = new SignorPanelRow(3,3, this.manager);
        CyNode node_current = selectedNodes.iterator().next();
        CyRow rownode = this.manager.lastCyNetwork.getDefaultNodeTable().getRow(node_current.getSUID());
        node_current_info.signorPanelRowDetailNode(nodesPanel,gbc, rownode);       
    }
}

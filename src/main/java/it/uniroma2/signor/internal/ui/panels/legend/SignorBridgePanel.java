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
import java.util.Map;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTableUtil;

public class SignorBridgePanel extends JPanel {
    private SignorManager manager;
    private JPanel connectPanel;

    private EasyGBC gbc=new EasyGBC();
    public CyNetwork current_cynetwork_to_serch_into;
    
    public SignorBridgePanel(SignorManager manager){
        this.manager = manager;
        setLayout(new GridBagLayout());        
        current_cynetwork_to_serch_into = manager.lastCyNetwork;
        JPanel ConnectInfo = new JPanel();
        ConnectInfo.setLayout(new BorderLayout());
        ConnectInfo.setBackground(Color.WHITE);
        {
            EasyGBC gbc1=new EasyGBC();
            connectPanel = new JPanel();
            connectPanel.setBackground(Color.WHITE);
            connectPanel.setLayout(new GridBagLayout());
            ConnectInfo.add(connectPanel, BorderLayout.NORTH);
            //NodeInfo.add(Box.createVerticalGlue(), gbc1.down().expandVert());
        }
        JScrollPane scrollPane = new JScrollPane(ConnectInfo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.getVerticalScrollBar().setBlockIncrement(10);
        add(scrollPane, gbc.down().anchor("east").expandBoth());
        revalidate();
        repaint();        
    }   
    
    public void createContent() {
        try {               
            Network networkCurrent = manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into);
            /*connectPanel.setLayout(new GridLayout(0, 2));
            Dimension parentSize = connectPanel.getParent().getSize();
            connectPanel.setPreferredSize(new Dimension(parentSize.width-250, parentSize.height));*/

            Map<CyNode, Node> signorNodes = networkCurrent.getNodes();
            manager.utils.info("SignorBridgePanel createcontent() "+signorNodes.toString());
            for (Map.Entry<CyNode, Node> entry : signorNodes.entrySet()) {              
                HashMap<String,String> summary = entry.getValue().getSummary();
                Iterator iter = summary.keySet().iterator();
                Iterator iterv = summary.values().iterator();
                manager.utils.info("SignorBridgePanel createcontent() "+summary.toString());
                manager.utils.info("SignorBridgePanel createcontent() "+summary.keySet());
                manager.utils.info("SignorBridgePanel createcontent() "+summary.values());
                
                while(iter.hasNext()){
                    String key = iter.next().toString();
                    String value = iterv.next().toString();
                    connectPanel.add(new JLabel(key), gbc.down());
                    connectPanel.add(new JLabel(value), gbc.right());

                }
//                connectPanel.add(new JLabel(entry.getValue().summary.get("")cyrow_node.get(Config.NAMESPACE, "ENTITY", String.class)), gbc.position(0, it));
//                connectPanel.add(new JLabel(cyrow_node_t.get(Config.NAMESPACE, "ENTITY", String.class)), gbc.right());
            }                          
        }
        catch (Exception e){
            manager.utils.error("SignorBridgePanel createContent() "+e.toString());
        }       
        
    }
    
    public void recreateContent(){
        if(manager.presentationManager.signorNetMap.containsKey(current_cynetwork_to_serch_into)){
            connectPanel.removeAll();
            createContent();
        }
    }
}

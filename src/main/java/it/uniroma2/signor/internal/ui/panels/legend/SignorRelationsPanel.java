/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.ui.panels.legend;
import it.uniroma2.signor.internal.conceptualmodel.logic.Nodes.*;
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
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.events.SelectedNodesAndEdgesEvent;
import org.cytoscape.model.events.SelectedNodesAndEdgesListener;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTableUtil;

public class SignorRelationsPanel extends JPanel {
    private SignorManager manager;
    private JPanel relPanel;

    private EasyGBC gbc=new EasyGBC();
    public Boolean selectionRunning= false;
    public CyNetwork current_cynetwork_to_serch_into;
    JScrollPane scrollPane;
     
    public SignorRelationsPanel(SignorManager manager){
        setLayout(new GridBagLayout());
        this.manager = manager; 
        current_cynetwork_to_serch_into = manager.lastCyNetwork;
        JPanel RelationInfo = new JPanel();
        RelationInfo.setLayout(new GridBagLayout());
        //RelationInfo.setLayout(new BorderLayout());
        RelationInfo.setBackground(Color.WHITE);
        {
            EasyGBC gbc1=new EasyGBC();
            relPanel = new JPanel();
            relPanel.setBackground(Color.WHITE);
            //relPanel.setLayout(new GridBagLayout());
            RelationInfo.add(relPanel, gbc1.down().anchor("north").expandHoriz());
            RelationInfo.add(Box.createVerticalGlue(), gbc1.down().down().expandVert());
            //RelationInfo.add(relPanel, BorderLayout.NORTH);
//            mainPanel.add(filters, d.down().anchor("north").expandHoriz());
//            mainPanel.add(createNodesPanel(), d.down().anchor("north").expandHoriz());
//            mainPanel.add(Box.createVerticalGlue(), d.down().expandVert());
        }
        scrollPane = new JScrollPane(RelationInfo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
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
            relPanel.setLayout(new GridLayout(0, 2));
//                Dimension parentSize = relPanel.getParent().getSize();
//                relPanel.setPreferredSize(new Dimension(parentSize.width-250, parentSize.height));
            Integer it = 0;
            for (CyEdge signorEdge : current_cynetwork_to_serch_into.getEdgeList()) {
                CyRow cyrow_node = current_cynetwork_to_serch_into.getDefaultNodeTable().getRow(signorEdge.getSource().getSUID());
                CyRow cyrow_node_t = current_cynetwork_to_serch_into.getDefaultNodeTable().getRow(signorEdge.getTarget().getSUID());
                CyRow edge_row= current_cynetwork_to_serch_into.getDefaultEdgeTable().getRow(signorEdge.getSUID());
                    
                relPanel.add(new JLabel(cyrow_node.get(Config.NAMESPACE, NodeField.ENTITY, String.class)), gbc.position(0, it));
                relPanel.add(new JLabel(cyrow_node_t.get(Config.NAMESPACE, NodeField.ENTITY, String.class)), gbc.right());                    
                it ++;                    
            }                           
        }
        catch (Exception e){
            manager.utils.error("SignorRelationsPanel createContent() "+e.toString());
        }       
    }
    
    public void recreateContent(){
        if(manager.presentationManager.signorNetMap.containsKey(current_cynetwork_to_serch_into)){
            relPanel.removeAll();
            createContent();
        }
    }
}

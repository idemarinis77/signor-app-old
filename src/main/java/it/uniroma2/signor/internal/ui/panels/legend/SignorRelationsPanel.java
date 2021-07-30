/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.ui.panels.legend;
import it.uniroma2.signor.internal.utils.EasyGBC;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.conceptualmodel.logic.Edges.Edge;
import java.awt.*;
import javax.swing.*;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.util.Map;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;

public class SignorRelationsPanel extends JPanel {
    private final SignorManager manager;
    private final JPanel relPanel;

    private final EasyGBC gbc=new EasyGBC();
    public Boolean selectionRunning= false;
    public CyNetwork current_cynetwork_to_serch_into;
    JScrollPane scrollPane;
     
    public SignorRelationsPanel(SignorManager manager){
        setLayout(new GridBagLayout());
        this.manager = manager; 
        current_cynetwork_to_serch_into = manager.lastCyNetwork;
        JPanel RelationInfo = new JPanel();
        RelationInfo.setLayout(new GridBagLayout());
        RelationInfo.setBackground(Color.WHITE);
        {
            EasyGBC gbc1=new EasyGBC();
            relPanel = new JPanel();
            relPanel.setBackground(Color.WHITE);
            RelationInfo.add(relPanel, gbc1.down().anchor("north").expandHoriz());
            RelationInfo.add(Box.createVerticalGlue(), gbc1.down().down().expandVert());
        }
        scrollPane = new JScrollPane(RelationInfo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.getVerticalScrollBar().setBlockIncrement(10);
        add(scrollPane, gbc.down().anchor("east").expandBoth());
        revalidate();
        repaint();   
    }
    
    public void createContent(){
        try {                             
            relPanel.setLayout(new GridLayout(0, 2));
            Network network = manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into);
            Map<CyEdge, Edge> cy_net_ed = network.getEdges();
            for (CyEdge cyEdge : cy_net_ed.keySet()) {            
                Edge edge = cy_net_ed.get(cyEdge);
                relPanel.add(new JLabel(edge.source.toString()), gbc.down());
                relPanel.add(new JLabel(edge.target.toString()), gbc.right());              
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

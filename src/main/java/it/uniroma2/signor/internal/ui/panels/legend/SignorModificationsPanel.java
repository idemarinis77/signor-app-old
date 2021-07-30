/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.ui.panels.legend;
import it.uniroma2.signor.internal.conceptualmodel.logic.Nodes.*;
import it.uniroma2.signor.internal.conceptualmodel.logic.Edges.*;
import it.uniroma2.signor.internal.utils.EasyGBC;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.Config;
import java.awt.*;
import javax.swing.*;
import static java.awt.Component.LEFT_ALIGNMENT;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;

public class SignorModificationsPanel extends JPanel {
    private final SignorManager manager;
    private final JPanel modPanel;

    private final EasyGBC gbc=new EasyGBC();
    public Boolean selectionRunning= false;
    public CyNetwork current_cynetwork_to_serch_into;

     
    public SignorModificationsPanel(SignorManager manager){
        setLayout(new GridBagLayout());
        this.manager = manager; 
        current_cynetwork_to_serch_into = manager.lastCyNetwork;
        JPanel ModificationInfo = new JPanel();
        ModificationInfo.setLayout(new GridBagLayout());
        ModificationInfo.setBackground(Color.WHITE);
        {
            EasyGBC gbc1=new EasyGBC();
            modPanel = new JPanel();
            modPanel.setBackground(Color.WHITE);
            ModificationInfo.add(modPanel, gbc1.down().anchor("north").expandHoriz());
            ModificationInfo.add(Box.createVerticalGlue(), gbc1.down().down().expandVert());
        }
        JScrollPane scrollPane = new JScrollPane(ModificationInfo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.getVerticalScrollBar().setBlockIncrement(10);
        add(scrollPane, gbc.down().anchor("east").expandBoth());
        revalidate();
        repaint();   
    }
    
    public void createContent(){
        try {                   
            modPanel.setLayout(new GridLayout(0, 5));            
            Integer it =0;
            for (CyEdge signorEdge : current_cynetwork_to_serch_into.getEdgeList()) {                
                CyRow cyrow_node = current_cynetwork_to_serch_into.getDefaultNodeTable().getRow(signorEdge.getTarget().getSUID());
                CyRow cyrow_edge = current_cynetwork_to_serch_into.getDefaultEdgeTable().getRow(signorEdge.getSUID());
                if (!cyrow_edge.get(Config.NAMESPACE, EdgeField.RESIDUE, String.class).isBlank()){
                    modPanel.add(new JLabel(cyrow_edge.get(Config.NAMESPACE, EdgeField.RESIDUE, String.class)), gbc.down());
                    modPanel.add(new JLabel(cyrow_edge.get(Config.NAMESPACE, EdgeField.SEQUENCE, String.class)), gbc.right());
                    modPanel.add(new JLabel(cyrow_node.get(Config.NAMESPACE, NodeField.ENTITY, String.class)), gbc.right());
                    modPanel.add(new JLabel(cyrow_edge.get(Config.NAMESPACE, EdgeField.Interaction, String.class).split(" ")[0]), gbc.right());
                    modPanel.add(new JLabel(cyrow_edge.get(Config.NAMESPACE, EdgeField.MECHANISM, String.class)), gbc.right());
                }
                it++;                
            }                  
        }
        catch (Exception e){
            manager.utils.error("SignorModificationsPanel createContent() "+e.toString());
        }       
    }
    
    public void recreateContent(){
        if(manager.presentationManager.signorNetMap.containsKey(current_cynetwork_to_serch_into)){
            modPanel.removeAll();
            createContent();
        }
    }
}

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
    private JPanel nodesPanel;

    private EasyGBC gbc=new EasyGBC();
    public Boolean selectionRunning= false;
    public CyNetwork current_cynetwork_to_serch_into;
    private Node rootNode;
    private static final int SIDES = 2;
    private static final int SIDE_LENGTH = 60;
    private static final int GAP = 1;
    private static final Color BG = Color.LIGHT_GRAY;
    private static final Color CELL_COLOR = Color.WHITE;
    
    public SignorSummaryPanel(SignorManager manager){
        this.manager = manager;          
        current_cynetwork_to_serch_into = manager.lastCyNetwork;
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        setLayout(new GridLayout(SIDES, 8, GAP, GAP));
        Dimension prefSize = new Dimension(SIDE_LENGTH, SIDE_LENGTH);
        for (int i = 0; i < SIDES; i++) {
            for (int j = 0; j < SIDES; j++) {
                JPanel cell = new JPanel();
                cell.setBackground(CELL_COLOR);
                cell.setPreferredSize(prefSize);
                add(cell);
            }
        }
        
       
        /*setLayout(new GridBagLayout());      
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
        repaint();        */
    }   
}

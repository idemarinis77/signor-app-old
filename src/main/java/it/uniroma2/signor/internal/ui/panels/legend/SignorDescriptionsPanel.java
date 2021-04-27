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
import it.uniroma2.signor.internal.ConfigResources;
import it.uniroma2.signor.internal.ui.components.SignorLabelStyledBold;
import it.uniroma2.signor.internal.ui.components.SignorPanelRow;
import it.uniroma2.signor.internal.utils.HttpUtils;
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

public class SignorDescriptionsPanel extends JPanel {
    private SignorManager manager;
    private JPanel descPanel;

    private EasyGBC gbc=new EasyGBC();
    public Boolean selectionRunning= false;
    public CyNetwork current_cynetwork_to_serch_into;

     
    public SignorDescriptionsPanel(SignorManager manager){
        setLayout(new GridBagLayout());
        this.manager = manager; 
        current_cynetwork_to_serch_into = manager.lastCyNetwork;
        JPanel DescriptionInfo = new JPanel();
        DescriptionInfo.setLayout(new GridBagLayout());
        //ModificationInfo.setLayout(new BorderLayout());
        DescriptionInfo.setBackground(Color.WHITE);
        {
            EasyGBC gbc1=new EasyGBC();
            descPanel = new JPanel();
            descPanel.setBackground(Color.WHITE);
            DescriptionInfo.add(descPanel, gbc1.down().anchor("north").expandHoriz());
            DescriptionInfo.add(Box.createVerticalGlue(), gbc1.down().down().expandVert());
            //modPanel.setLayout(new GridBagLayout());
            //ModificationInfo.add(modPanel, BorderLayout.NORTH);
            //NodeInfo.add(Box.createVerticalGlue(), gbc1.down().expandVert());
        }
        JScrollPane scrollPane = new JScrollPane(DescriptionInfo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
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
            //SignorPanelRow listresults = new SignorPanelRow(current_cynetwork_to_serch_into.getEdgeList().size(), 2, manager);
            //sig_id	path_name	coalesce	path_curator
            descPanel.setLayout(new GridLayout(4, 2));
            ArrayList<String> pathInfo = HttpUtils.parseWSNoheader(
                    HttpUtils.getHTTPSignor(ConfigResources.PATHSINGLEDESCRIPTIONSQUERY+
                            manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into).parameters.get("PATHWAYID"), manager));
                 
            for (int i = 0; i < pathInfo.size(); i++) {   
                if(pathInfo.get(i).startsWith("sig_id")) continue;
                String[] path_info_packed=pathInfo.get(i).split("\t");
                descPanel.add(new JLabel("PATH ID"), gbc.position(0, 0));
                descPanel.add(new JLabel(path_info_packed[0]), gbc.right());
                descPanel.add(new JLabel("PATH NAME"), gbc.position(0, 1));
                descPanel.add(new JLabel(path_info_packed[1]), gbc.right());
                descPanel.add(new JLabel("COALESCE"), gbc.position(0, 2));
                descPanel.add(new JLabel(path_info_packed[2]), gbc.right());
                descPanel.add(new JLabel("CURATOR"), gbc.position(0, 3));
                descPanel.add(new JLabel(path_info_packed[3]), gbc.right());
            }      
            
            //modPanel.add(listresults);
        }
        catch (Exception e){
            manager.utils.error(e.toString());
        }       
    }
    
    public void recreateContent(){
        if(manager.presentationManager.signorNetMap.containsKey(current_cynetwork_to_serch_into)){
            manager.utils.info(current_cynetwork_to_serch_into.toString()+" sono in description panel");
            descPanel.removeAll();
            createContent();
        }
    }
}

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

import it.uniroma2.signor.internal.ui.components.SignorLabelStyledBold;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.events.SelectedNodesAndEdgesEvent;
import org.cytoscape.model.events.SelectedNodesAndEdgesListener;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.NetworkField;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.NetworkSearch;
import it.uniroma2.signor.internal.conceptualmodel.logic.Nodes.NodeField;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.ConfigResources;
import it.uniroma2.signor.internal.ui.components.SignorButton;
import it.uniroma2.signor.internal.ui.components.SignorPanelRow;
import it.uniroma2.signor.internal.task.query.factories.SignorGenericRetrieveResultFactory;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.util.swing.OpenBrowser;

public class SignorNodePanel extends JPanel {
    private SignorManager manager;
    private JPanel nodesPanel;

    private EasyGBC gbc=new EasyGBC();
    public Boolean selectionRunning= false;
    public CyNetwork current_cynetwork_to_serch_into;
    
    public SignorNodePanel(SignorManager manager){
        this.manager = manager;
        setLayout(new GridBagLayout());        
        current_cynetwork_to_serch_into = manager.lastCyNetwork;
        JPanel NodeInfo = new JPanel();
        NodeInfo.setLayout(new BorderLayout());
        NodeInfo.setBackground(Color.WHITE);
        {
//            EasyGBC gbc1=new EasyGBC();
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
        Network network = manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into);
        Collection<CyNode> selectedNodes = CyTableUtil.getNodesInState(current_cynetwork_to_serch_into, CyNetwork.SELECTED, true);        
//        SignorPanelRow node_current_info = new SignorPanelRow(0,3, this.manager);
        
//        CyRow rownode = current_cynetwork_to_serch_into.getDefaultNodeTable().getRow(node_current.getSUID());
//        node_current_info.signorPanelRowDetailNode(nodesPanel,gbc, rownode); 

        Iterator iter_sel_nodes = selectedNodes.iterator();
        while(iter_sel_nodes.hasNext()){
            JPanel separator  = new JPanel();
            separator.setLayout(new GridBagLayout());
            separator.add(new SignorLabelStyledBold(">> Node info "), gbc.down().anchor("west"));
            separator.setBackground(new Color(82, 166, 119));
            nodesPanel.add(separator, gbc.down().anchor("west").insets(2,0,2,0));
            CyNode node_current = (CyNode) iter_sel_nodes.next();
            Node node = network.getNodes().get(node_current);
            
            HashMap <String,String> summary = node.getSummary();
            String entity_id = summary.get("ID");
            String entity_name = summary.get("ENTITY");
            Iterator iter = summary.keySet().iterator();
            Iterator iterv = summary.values().iterator();
            JPanel nodeinfo = new JPanel();
            nodeinfo.setLayout(new GridBagLayout());
            
            while(iter.hasNext()){
                String key = iter.next().toString();
                String value = iterv.next().toString();
                SignorLabelStyledBold id = new SignorLabelStyledBold(key);
                nodeinfo.add(id, gbc.down());
//                if(key!= Config.NODEFIELDMAP.get("DATABASEA")){ 
                if(key!= NodeField.DATABASE){
                    nodeinfo.add(new JLabel(value), gbc.right());
                }
                else {
                    //nodeinfo.add(new JLabel(value), gbc.right());
                    String db_value_norm = value.toLowerCase();
                    String link_to_db = ConfigResources.DBLINKSMAP.get(db_value_norm).queryFunction.apply(entity_id);
                    OpenBrowser openBrowser = manager.utils.getService(OpenBrowser.class);
                    SignorLabelStyledBold dbLabel;
                    try{
                        BufferedImage dblogo = ImageIO.read(getClass().getResource(ConfigResources.DBLOGOS.get(db_value_norm)));
                        dbLabel = new SignorLabelStyledBold(new ImageIcon(dblogo), link_to_db, openBrowser, false);
                        nodeinfo.add(dbLabel, gbc.right().anchor("east"));
                    }
                    catch (Exception e){
                        manager.utils.warn("SignorNodePanel selectedNodes(): warning with renderning image database "+e.toString());
                        manager.utils.info("SignorNodePanel selectedNodes(): valore del db "+value);
                        nodesPanel.add(nodeinfo, gbc.down());
                    }    
                }     
            }
            SignorButton searchID =  new SignorButton("Search "+entity_name);
            searchID.addActionListener(e-> buildSingleSearch(entity_id, network));
            nodesPanel.add(nodeinfo, gbc.down());
            nodesPanel.add(searchID, gbc.down());
        }       
    }
    
    private void buildSingleSearch(String id, Network network){
//        HashMap<String, Object> new_parameters = new HashMap<>() {
//                {put (NetworkField.ALLSEARCH, false);}  
//                {put (NetworkField.CONNECTSEARCH, false);} 
//                {put (NetworkField.INCFIRSTNEISEARCH, false);} 
//                {put (NetworkField.SHORTESTPATHSEARCH, false);}
//                {put (NetworkField.SINGLESEARCH, true);}
//                {put (NetworkField.SPECIES, network.parameters.get(NetworkField.SPECIES));}
//                {put ("QUERY", id);}
//        };
        String species = (String) network.parameters.get(NetworkField.SPECIES);
        if(species == null){
            species = "Homo Sapiens";
        }
        HashMap<String, Object> new_parameters = NetworkSearch.buildSearch(id, species, NetworkField.SINGLESEARCH, false);
        Network new_network = new Network(manager, new_parameters);
        
        
//        String species = (String) network.parameters.get(NetworkField.SPECIES);
//        if(species == null){
//            species = "Homo Sapiens";
//            new_parameters.replace(NetworkField.SPECIES, species);
//        }
        //(String search, Boolean includefirstneighbor, String species, 
//            String terms, Network network)
        SignorGenericRetrieveResultFactory sgrf = new SignorGenericRetrieveResultFactory(NetworkField.SINGLESEARCH,
                                                    false, species, id, new_network);
        manager.utils.execute(sgrf.createTaskIterator());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.ui.panels.legend;
import it.uniroma2.signor.internal.conceptualmodel.logic.Nodes.Node;
import it.uniroma2.signor.internal.utils.EasyGBC;
import java.util.Iterator;
import java.awt.*;
import java.util.Collection;
import javax.swing.*;
import it.uniroma2.signor.internal.ui.components.SignorLabelStyledBold;
import org.cytoscape.model.CyNode;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.NetworkField;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.NetworkSearch;
import it.uniroma2.signor.internal.conceptualmodel.logic.Nodes.NodeField;
import it.uniroma2.signor.internal.conceptualmodel.logic.Edges.*;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.ConfigResources;
import it.uniroma2.signor.internal.ui.components.SignorButton;
import it.uniroma2.signor.internal.task.query.factories.SignorGenericRetrieveResultFactory;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.util.HashMap;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.util.swing.OpenBrowser;

public class SignorNodePanel extends JPanel {
    private final SignorManager manager;
    private final JPanel nodesPanel;
    private static Font iconFont;
    private final EasyGBC gbc=new EasyGBC();
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
            nodesPanel = new JPanel();
            nodesPanel.setBackground(Color.WHITE);
            nodesPanel.setLayout(new GridBagLayout());
            NodeInfo.add(nodesPanel, BorderLayout.NORTH);
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
        this.selectionRunning=true;
        Network network = manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into);
        Collection<CyNode> selectedNodes = CyTableUtil.getNodesInState(current_cynetwork_to_serch_into, CyNetwork.SELECTED, true); 
        if(selectedNodes.size()>0){
            nodesPanel.removeAll();
        }        
        Iterator iter_sel_nodes = selectedNodes.iterator();
        while(iter_sel_nodes.hasNext()){                  
            JPanel nodeinfo = new JPanel();
            nodeinfo.setLayout(new GridBagLayout());
 
            CyNode node_current = (CyNode) iter_sel_nodes.next();
            if(!current_cynetwork_to_serch_into.getDefaultNodeTable().getRow(node_current.getSUID()).
                    get(Config.NAMESPACE, NodeField.TYPE, String.class).equals("residue")){
                Node node = network.getNodes().get(node_current);            
                HashMap <String,String> summary = node.getSummary();                
                String entity_name = summary.get(NodeField.ENTITY);
                String entity_id = summary.get(NodeField.ID);              
                Iterator iter = summary.keySet().iterator();
                Iterator iterv = summary.values().iterator();    
                while(iter.hasNext()){
                    String key = iter.next().toString();
                    String value = iterv.next().toString();
                    SignorLabelStyledBold id = new SignorLabelStyledBold(key);
                    nodeinfo.add(id, gbc.down());
                    if(key!= NodeField.DATABASE){
                        nodeinfo.add(new JLabel(value), gbc.right());
                    }
                    else {
                        String db_value_norm = value.toLowerCase();
                        String link_to_db = ConfigResources.DBLINKSMAP.get(db_value_norm).queryFunction.apply(entity_id);
                        OpenBrowser openBrowser = manager.utils.getService(OpenBrowser.class);
                        SignorLabelStyledBold dbLabel = new SignorLabelStyledBold(db_value_norm, link_to_db, openBrowser, false);
                        nodeinfo.add(dbLabel, gbc.right());
                    }
                }         
                SignorButton searchID =  new SignorButton("causal networks");
                searchID.addActionListener(e-> buildSingleSearch(entity_id, network));
                nodeinfo.add(new SignorLabelStyledBold("Search in SIGNOR"), gbc.down());
                nodeinfo.add(searchID, gbc.right());          
                CollapsablePanel collapsableINFO = new CollapsablePanel(iconFont, "Node INFO", nodeinfo, false );
                nodesPanel.add(collapsableINFO, gbc.down().anchor("north").insets(1,1,1,1));   
            }
            else {
                nodeinfo.add(new SignorLabelStyledBold("INTERACTION"), gbc.down());
                nodeinfo.add(new JLabel(current_cynetwork_to_serch_into.getDefaultNodeTable().getRow(node_current.getSUID()).
                    get("name", String.class)), gbc.right());
                nodeinfo.add(new SignorLabelStyledBold(EdgeField.SEQUENCE), gbc.down());
                nodeinfo.add(new JLabel(current_cynetwork_to_serch_into.getDefaultNodeTable().getRow(node_current.getSUID()).
                    get(Config.NAMESPACE,NodeField.ID, String.class)), gbc.right());
                CollapsablePanel collapsableINFO = new CollapsablePanel(iconFont, "PTM Node INFO", nodeinfo, false );
                nodesPanel.add(collapsableINFO, gbc.down().anchor("north").insets(1,1,1,1));   
            }
        }   
    }
    
    private void buildSingleSearch(String id, Network network){

        String species = (String) network.parameters.get(NetworkField.SPECIES);
        if(species == null){
            species = "Homo Sapiens";
        }
        HashMap<String, Object> new_parameters = NetworkSearch.buildSearch(id, species, NetworkField.SINGLESEARCH, false);
        Network new_network = new Network(manager, new_parameters);    
        SignorGenericRetrieveResultFactory sgrf = new SignorGenericRetrieveResultFactory(NetworkField.SINGLESEARCH,
                                                    false, species, id, new_network);
        manager.utils.execute(sgrf.createTaskIterator());
    }
}

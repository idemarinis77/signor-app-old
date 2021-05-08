/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.ui.panels.legend;
import it.uniroma2.signor.internal.conceptualmodel.logic.Nodes.Node;
import it.uniroma2.signor.internal.utils.EasyGBC;
import it.uniroma2.signor.internal.ui.components.SignorButton;
import it.uniroma2.signor.internal.task.query.factories.SignorGenericRetrieveResultFactory;

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
import it.uniroma2.signor.internal.ui.components.SignorLabelStyledBold;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.util.HashMap;
import java.util.Map;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTableUtil;

public class SignorBridgePanel extends JPanel {
    private SignorManager manager;
    private JPanel bridgePanel;

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
            bridgePanel = new JPanel();
            bridgePanel.setBackground(Color.WHITE);
            bridgePanel.setLayout(new GridBagLayout());
            ConnectInfo.add(bridgePanel, BorderLayout.NORTH);
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
            bridgePanel.setLayout(new GridBagLayout());
            if(networkCurrent.parameters.get(Config.INCFIRSTNEISEARCH).equals(false)){
               SignorButton ifn = new SignorButton("Include first neighbor");
               ifn.addActionListener(e-> buildIfn(networkCurrent));
               JPanel buttonPanel = new JPanel();
               buttonPanel.setLayout(new GridBagLayout());
               buttonPanel.add(ifn, gbc.anchor("west").expandHoriz());
               bridgePanel.add(buttonPanel, gbc.anchor("north").expandHoriz().insets(2, 0,2,0));
//               bridgePanel.add(ifn, gbc.anchor("north").expandHoriz());
            }      
            
            JPanel add_entity=new JPanel();
            add_entity.setLayout(new GridBagLayout());
            JTextArea new_entity = new JTextArea("Add entry");
            SignorButton new_entity_btn = new SignorButton("search");
            new_entity_btn.addActionListener(e-> addEntity(networkCurrent, new_entity.getText()));
            add_entity.add(new_entity, gbc.down());
            add_entity.add(new_entity_btn, gbc.right().expandHoriz());
            bridgePanel.add(add_entity, gbc.down().expandHoriz().insets(2, 0,2,0));
            
            JPanel entity_info=new JPanel();
            entity_info.setLayout(new GridBagLayout());
            Map<CyNode, Node> signorNodes = networkCurrent.getNodes();
            for (Map.Entry<CyNode, Node> entry : signorNodes.entrySet()) {              
                HashMap<String,String> summary = entry.getValue().getSummary();
                Iterator iter = summary.keySet().iterator();
                Iterator iterv = summary.values().iterator();              
                while(iter.hasNext()){
                    String key = iter.next().toString();
                    String value = iterv.next().toString();
                    entity_info.add(new SignorLabelStyledBold(key), gbc.down());
                    entity_info.add(new JLabel(value), gbc.right());

                }
//                connectPanel.add(new JLabel(entry.getValue().summary.get("")cyrow_node.get(Config.NAMESPACE, "ENTITY", String.class)), gbc.position(0, it));
//                connectPanel.add(new JLabel(cyrow_node_t.get(Config.NAMESPACE, "ENTITY", String.class)), gbc.right());
            }  
            bridgePanel.add(entity_info, gbc.anchor("north").down());
        }
        catch (Exception e){
            manager.utils.error("SignorBridgePanel createContent() "+e.toString());
        }       
        
    }
    
    public void recreateContent(){
        if(manager.presentationManager.signorNetMap.containsKey(current_cynetwork_to_serch_into)){
            bridgePanel.removeAll();
            createContent();
        }
    }
    
    private void buildIfn(Network network){
        //SignorGenericRetrieveResultFactory(String search, Boolean includefirstneighbor, String species, 
            //String terms, Network network){
        HashMap<String, Object> new_parameters = new HashMap();
        
        Iterator iter = network.parameters.keySet().iterator();
        Iterator iterv = network.parameters.values().iterator();              
        while(iter.hasNext()){
            String key = iter.next().toString();
            Object value = iterv.next();            
            if(key.equals("QUERY")){
                String packed_query = value.toString().replace(" ", "%2C");
                new_parameters.put(key, packed_query);
            }
            else if(key == Config.INCFIRSTNEISEARCH)
                 new_parameters.put(Config.INCFIRSTNEISEARCH, true);                 
            else new_parameters.put(key, value);                
        }
        Network newnetwork = new Network(manager, new_parameters);
        manager.utils.info("BridgePanel buildIfn first parameters "+network.parameters.toString());
        manager.utils.info("BridgePanel buildIfn after parameters "+new_parameters.toString());
        
        SignorGenericRetrieveResultFactory sgrf = new SignorGenericRetrieveResultFactory(Config.CONNECTSEARCH, true, Config.SPECIES,
                                                  (String) new_parameters.get("QUERY"), newnetwork);
        manager.utils.execute(sgrf.createTaskIterator());
    }
    private void addEntity(Network network, String new_entity){
        //SignorGenericRetrieveResultFactory(String search, Boolean includefirstneighbor, String species, 
            //String terms, Network network){
        String default_search = Config.CONNECTSEARCH;
        if(network.parameters.get(Config.ALLSEARCH).equals(true)) default_search = Config.ALLSEARCH;
        if(network.parameters.get(Config.SHORTESTPATHSEARCH).equals(true)) default_search = Config.SHORTESTPATHSEARCH;
        
        HashMap<String, Object> new_parameters = new HashMap();
        
        Iterator iter = network.parameters.keySet().iterator();
        Iterator iterv = network.parameters.values().iterator();              
        while(iter.hasNext()){
            String key = iter.next().toString();
            Object value = iterv.next();            
            if(key.equals("QUERY")){
                String packed_query = value.toString().replace(" ", "%2C");
                packed_query = packed_query+"%2C"+new_entity;
                new_parameters.put(key, packed_query);
            }    
            else new_parameters.put(key, value);               

        }
        Network newnetwork = new Network(manager, new_parameters);
        manager.utils.info("BridgePanel buildIfn first parameters "+network.parameters.toString());
        manager.utils.info("BridgePanel buildIfn after parameters "+new_parameters.toString()+"Default search "+default_search);
        
        SignorGenericRetrieveResultFactory sgrf = new SignorGenericRetrieveResultFactory(default_search, 
                (boolean) new_parameters.get(Config.INCFIRSTNEISEARCH), Config.SPECIES,
                                                  (String) new_parameters.get("QUERY"), newnetwork);
        manager.utils.execute(sgrf.createTaskIterator());
    }
}

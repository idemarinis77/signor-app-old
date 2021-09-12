/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.ui.panels.legend;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Nodes.*;
import it.uniroma2.signor.app.internal.utils.EasyGBC;
import it.uniroma2.signor.app.internal.ui.components.SignorButton;
import it.uniroma2.signor.app.internal.task.query.factories.SignorGenericRetrieveResultFactory;
import java.util.Iterator;
import java.awt.*;
import javax.swing.*;
import org.cytoscape.model.CyNode;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.NetworkField;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.NetworkSearch;
import it.uniroma2.signor.app.internal.ui.components.SignorLabelStyledBold;
import it.uniroma2.signor.app.internal.ui.components.SignorLabelWarning;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.util.HashMap;
import java.util.Map;
import org.cytoscape.model.CyNetwork;


public class SignorBridgePanel extends JPanel {
    private final SignorManager manager;
    private final JPanel bridgePanel;

    private final EasyGBC gbc=new EasyGBC();
    public CyNetwork current_cynetwork_to_serch_into;
    
    public SignorBridgePanel(SignorManager manager){
        this.manager = manager;
        setLayout(new GridBagLayout());        
        current_cynetwork_to_serch_into = manager.lastCyNetwork;
        JPanel ConnectInfo = new JPanel();
        ConnectInfo.setLayout(new BorderLayout());
        ConnectInfo.setBackground(Color.WHITE);
        {
            bridgePanel = new JPanel();
            bridgePanel.setBackground(Color.WHITE);
            bridgePanel.setLayout(new GridBagLayout());
            ConnectInfo.add(bridgePanel, BorderLayout.NORTH);
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
            bridgePanel.setLayout(new GridBagLayout());
            if(networkCurrent.parameters.get(NetworkField.INCFIRSTNEISEARCH).equals(false)
                    && networkCurrent.parameters.get(NetworkField.CONNECTSEARCH).equals(true)){
               SignorButton ifn = new SignorButton("Include first neighbor");
               ifn.addActionListener(e-> buildIfn(networkCurrent));
               JPanel buttonPanel = new JPanel();
               buttonPanel.setLayout(new GridBagLayout());
               buttonPanel.add(ifn, gbc.anchor("west").expandHoriz());
               bridgePanel.add(buttonPanel, gbc.anchor("north").expandHoriz().insets(2, 0,2,0));
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
            Map<CyNode, Node> signorNodes = networkCurrent.getSearchedNodes();
            
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
                entity_info.add(new SignorLabelStyledBold("-----"), gbc.down().expandHoriz());
            }  
            if(!networkCurrent.getEntityNotFound().isBlank()){
                String not_found = networkCurrent.getEntityNotFound();
                entity_info.add(new SignorLabelWarning(NodeField.ENTITY), gbc.down());
                entity_info.add(new SignorLabelWarning(" "+not_found+" entity not found in SIGNOR"), gbc.right());
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

        HashMap<String, Object> new_parameters = new HashMap();
        HashMap<String, Object> parameters = network.parameters;
        String packed_query = parameters.get(NetworkField.QUERY).toString().replace(" ", "%2C");
        String species = (String) parameters.get(NetworkField.SPECIES);
        new_parameters = NetworkSearch.buildSearch((String) parameters.get(NetworkField.QUERY), species, NetworkField.CONNECTSEARCH, true);
        Network newnetwork = new Network(manager, new_parameters);
        manager.utils.info("BridgePanel buildIfn first parameters "+network.parameters.toString());
        manager.utils.info("BridgePanel buildIfn after parameters "+new_parameters.toString());        
        SignorGenericRetrieveResultFactory sgrf = new SignorGenericRetrieveResultFactory(NetworkField.CONNECTSEARCH, true, species,
                                                  packed_query, newnetwork);
        manager.utils.execute(sgrf.createTaskIterator());
    }
    private void addEntity(Network network, String new_entity){
        String default_search = NetworkField.CONNECTSEARCH;
        if(network.parameters.get(NetworkField.ALLSEARCH).equals(true)) default_search = NetworkField.ALLSEARCH;
        if(network.parameters.get(NetworkField.SHORTESTPATHSEARCH).equals(true)) default_search = NetworkField.SHORTESTPATHSEARCH;
        HashMap<String, Object> new_parameters = new HashMap();
        HashMap<String, Object> parameters = network.parameters;
        String packed_query = parameters.get(NetworkField.QUERY).toString().replace(" ", "%2C");
        String species = (String) parameters.get(NetworkField.SPECIES);
        new_parameters = NetworkSearch.buildSearch((String) parameters.get(NetworkField.QUERY), species, default_search, true);
        Network newnetwork = new Network(manager, new_parameters);
        manager.utils.info("BridgePanel buildIfn first parameters "+network.parameters.toString());
        manager.utils.info("BridgePanel buildIfn after parameters "+new_parameters.toString()+"Default search "+default_search);        
        SignorGenericRetrieveResultFactory sgrf = new SignorGenericRetrieveResultFactory(default_search, 
                (boolean) new_parameters.get(NetworkField.INCFIRSTNEISEARCH), species,
                                                  packed_query, newnetwork);
        manager.utils.execute(sgrf.createTaskIterator());
    }
}

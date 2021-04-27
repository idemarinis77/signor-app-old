/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.managers;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.session.events.SessionLoadedListener;

import java.util.HashMap;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.ConfigPathway;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.subnetwork.CySubNetwork;
import it.uniroma2.signor.internal.conceptualmodel.logic.Nodes.Node;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import java.util.Properties;
import org.cytoscape.application.swing.CytoPanelComponent2;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.model.CyNetworkView;

/**
 *
 * @author amministratore
 */
public class SignorManager {
    public final SignorStyleManager signorStyleManager;
    public final PresentationManager presentationManager;
    public final SessionLoaderManager sessionLoaderManager;
    public final CytoUtils utils;
    public CyNetwork lastCyNetwork;
    public Network lastNetwork;
    public CyNetworkView lastCyNetworkView;
    //SignorLegendPanel signorPanel;
    //public Boolean PTMtableTocreate = false;
  
    public SignorManager(CyServiceRegistrar registrar) {        
        utils = new CytoUtils(registrar);        
        sessionLoaderManager = new SessionLoaderManager(this);
        presentationManager = new PresentationManager(this);
        utils.registerService(sessionLoaderManager, SessionLoadedListener.class, new Properties());
        signorStyleManager = new SignorStyleManager(this, Config.FILESTYLE);
        signorStyleManager.setupDefaultStyle();
    }
    
    public CyNetwork createNetwork(String name) {
        CyNetwork cyNetwork = this.utils.getService(CyNetworkFactory.class).createNetwork();
        CyNetworkManager netMgr = this.utils.getService(CyNetworkManager.class);
        Set<CyNetwork> nets = netMgr.getNetworkSet();
        Set<CyNetwork> allNets = new HashSet<>(nets);
        for (CyNetwork net : nets) {
            allNets.add(((CySubNetwork) net).getRootNetwork());
        }
        // See if this name is already taken by a network or a network collection (root network)
        int index = -1;
        boolean match = false;
        for (CyNetwork net : allNets) {
            String netName = net.getRow(net).get(CyNetwork.NAME, String.class);
            if (netName.equals(name)) {
                match = true;
            } else if (netName.startsWith(name)) {
                String subname = netName.substring(name.length());
                if (subname.startsWith(" - ")) {
                    try {
                        int v = Integer.parseInt(subname.substring(3));
                        if (v >= index)
                            index = v + 1;
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
        if (match && index < 0) {
            name = name + " - 1";
        } else if (index > 0) {
            name = name + " - " + index;
        }
        cyNetwork.getRow(cyNetwork).set(CyNetwork.NAME, name);
        this.lastCyNetwork = cyNetwork;
        return cyNetwork;
    }
    
    public void setCurrentNetwork(Network network){
        this.lastNetwork = network;
    }
   
    public CyNetwork createNetworkFromLine(ArrayList<String> results){
        CyNetwork signornet = this.lastCyNetwork;
        HashMap<String, CyNode> entity_read = new HashMap <String, CyNode>();
        for (int i = 0; i < results.size(); i++) {            
            String[] attributes = results.get(i).split("\t");
            CyNode nodeSource;
            CyNode nodeTarget;            
            if (!entity_read.containsKey(attributes[0])){
                nodeSource = signornet.addNode();
                entity_read.put(attributes[0], nodeSource);
            }
            else {
                nodeSource = entity_read.get(attributes[0]);
            }
            for (int a = 0; a < 4; a++){
                  String attribute = Config.HEADERSINGLESEARCH[a];
                  String map_attribute = Config.NODEFIELDMAP.get(attribute);
                  try {
                        signornet.getDefaultNodeTable().getRow(nodeSource.getSUID()).set(Config.NAMESPACE, map_attribute, attributes[a]);
                  }
                  catch (Exception e){
                      this.utils.error(Config.NAMESPACE+" "+map_attribute+" "+attribute+" "+attributes[a]+" "+e.toString());
                  }
            } 
            if (!entity_read.containsKey(attributes[4])){
                nodeTarget = signornet.addNode();
                entity_read.put(attributes[4], nodeTarget);
            }
            else {
                nodeTarget = entity_read.get(attributes[4]);
            }
            for (int a = 4; a < 8; a++){
                  String attribute = Config.HEADERSINGLESEARCH[a];
                  String map_attribute = Config.NODEFIELDMAP.get(attribute);
                  try {
                        signornet.getDefaultNodeTable().getRow(nodeTarget.getSUID()).set(Config.NAMESPACE, map_attribute, attributes[a]);
                  }
                  catch (Exception e){
                      this.utils.error(Config.NAMESPACE+" "+map_attribute+" "+attribute+" "+attributes[a]+" "+e.toString());
                  }
            } 
            CyEdge edge = signornet.addEdge(nodeSource, nodeTarget, false);
            for (int a = 8; a < attributes.length; a++){                
                String attribute = Config.HEADERSINGLESEARCH[a];
                String map_attribute = Config.EDGEFIELDMAP.get(attribute);
                try {
                    if((a == attributes.length -1) && attributes[a].startsWith("0.")){
                        //I'm reading the last field (Score) 
                        //Some lines could have no score, so I double check also the "0." at the beginning of the field
                        signornet.getDefaultEdgeTable().getRow(edge.getSUID()).set(Config.NAMESPACE, map_attribute, Double.parseDouble(attributes[a]));
                    }
                    else if (a == 12){
                        //I'm reading TAX_ID blank 
                        if(attributes[a].isBlank())
                           signornet.getDefaultEdgeTable().getRow(edge.getSUID()).set(Config.NAMESPACE, map_attribute, 0);
                        //I'm reading TAX_ID and it's of Integer type
                        else 
                           signornet.getDefaultEdgeTable().getRow(edge.getSUID()).set(Config.NAMESPACE, map_attribute, Integer.parseInt(attributes[a]));
                    }
                    else {
                        signornet.getDefaultEdgeTable().getRow(edge.getSUID()).set(Config.NAMESPACE, map_attribute, attributes[a]);
                    }
                }
                catch (Exception e){
                    this.utils.error(Config.NAMESPACE+" "+map_attribute+" "+attribute+" "+attributes[a]+" "+e.toString());
                }
            }  
        }
        utils.flushEvents();
        return signornet;
    }
    
    public CyNetwork createPathwayFromLine(ArrayList<String> results, CyNetwork signornet){        
        HashMap<String, CyNode> entity_read = new HashMap <String, CyNode>();
        
        int node_source_positions[] = {2,4,5,6};
        int node_target_positions[]= {7,9,10,11};
        int edge_positions[] = {0,1,3,8,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
        for (int i = 0; i < results.size(); i++) {   
            if(results.get(i).startsWith("pathway_id")) continue;
            String[] attributes = results.get(i).split("\t");
            
            CyNode nodeSource;
            CyNode nodeTarget;       
            //entitya position 3
            if (!entity_read.containsKey(attributes[2])){
                nodeSource = signornet.addNode();
                entity_read.put(attributes[2], nodeSource);
            }
            else {
                nodeSource = entity_read.get(attributes[2]);
            }
            for (int a = 0; a < node_source_positions.length; a++){
                  String attribute = ConfigPathway.HEADERPTH[node_source_positions[a]];
                  String map_attribute = Config.NODEFIELDMAP.get(attribute);
                  try {
                        signornet.getDefaultNodeTable().getRow(nodeSource.getSUID()).set(Config.NAMESPACE, map_attribute, attributes[node_source_positions[a]]);
                  }
                  catch (Exception e){
                      this.utils.error(Config.NAMESPACE+" "+map_attribute+" "+attribute+" "+attributes[node_source_positions[a]]+" "+e.toString());
                  }
            } 
            //entityb position 8
            if (!entity_read.containsKey(attributes[7])){
                nodeTarget = signornet.addNode();
                entity_read.put(attributes[7], nodeTarget);
            }
            else {
                nodeTarget = entity_read.get(attributes[7]);
            }
            for (int a = 0; a < node_target_positions.length; a++){
                  String attribute = ConfigPathway.HEADERPTH[node_target_positions[a]];
                  String map_attribute = Config.NODEFIELDMAP.get(attribute);
                  try {
                        signornet.getDefaultNodeTable().getRow(nodeTarget.getSUID()).set(Config.NAMESPACE, map_attribute, attributes[node_target_positions[a]]);
                  }
                  catch (Exception e){
                      this.utils.error(Config.NAMESPACE+" "+map_attribute+" "+attribute+" "+attributes[node_target_positions[a]]+" "+e.toString());
                  }
            } 
            CyEdge edge = signornet.addEdge(nodeSource, nodeTarget, false);
            int limit = edge_positions.length;
            //Score may miss so attributes are 31 and not 32
            if( attributes.length == 31 ) limit = edge_positions.length - 1;
            for (int a = 0; a < limit; a++){                
                String attribute = ConfigPathway.HEADERPTH[edge_positions[a]];
                String map_attribute = ConfigPathway.EDGEFIELDPTHMAP.get(attribute);
                try {
                    if(attributes[edge_positions[a]].startsWith("0.")){
                    //if((a == limit -1) && attributes[edge_positions[a]].startsWith("0.")){
                        //I'm reading the last field (Score) 
                        //Some lines could have no score, so I double check also the "0." at the beginning of the field
                        signornet.getDefaultEdgeTable().getRow(edge.getSUID()).set(Config.NAMESPACE, map_attribute, Double.parseDouble(attributes[edge_positions[a]]));
                        continue;
                    }
                    if ((a == 16)){
                        //I'm reading MODBSEQ
                        if(attributes[edge_positions[a]].isBlank())
                           signornet.getDefaultEdgeTable().getRow(edge.getSUID()).set(Config.NAMESPACE, map_attribute, "");
                        //I'm reading MODBSEQ and it's of String type
                        else 
                           signornet.getDefaultEdgeTable().getRow(edge.getSUID()).set(Config.NAMESPACE, map_attribute, (String) (attributes[edge_positions[a]]));
                        continue;
                    }
                    if ((a == 8)){
                        //I'm reading TAX_ID blank or  
                        if(attributes[edge_positions[a]].isBlank())
                           signornet.getDefaultEdgeTable().getRow(edge.getSUID()).set(Config.NAMESPACE, map_attribute, 0);
                        //I'm reading TAX_ID and it's of Integer type
                        else 
                           signornet.getDefaultEdgeTable().getRow(edge.getSUID()).set(Config.NAMESPACE, map_attribute, Integer.parseInt(attributes[edge_positions[a]]));
                        continue;
                    }
                    signornet.getDefaultEdgeTable().getRow(edge.getSUID()).set(Config.NAMESPACE, map_attribute, attributes[edge_positions[a]]);
                    
                }
                catch (Exception e){
                    this.utils.error(Config.NAMESPACE+" "+map_attribute+" "+attribute+" "+attributes[edge_positions[a]]+" "+e.toString()+" POSITION "+a);
                }
            }  
        }
        utils.flushEvents();
        return signornet;
    }
 }

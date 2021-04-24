/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.utils;

import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.conceptualmodel.logic.Nodes.Node;
import it.uniroma2.signor.internal.task.query.factories.AlgorithmFactory;
import it.uniroma2.signor.internal.task.query.AlgorithmTask;
import java.util.List;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetworkTableManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.task.hide.HideTaskFactory;
import org.cytoscape.task.hide.UnHideTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import org.cytoscape.model.CyNetwork;



/**
 *
 * @author amministratore
 */
public class DataUtils {
    
    public static Boolean isSignorNetwork(CyNetwork cyNetwork){
        return cyNetwork.getRow(cyNetwork).get(CyNetwork.NAME, String.class).startsWith(Config.NTWPREFIX);
    }
    
    public static void writeNetworkPTMInfo(SignorManager manager, Network network){
        CyApplicationManager cyApplicationManager = manager.utils.getService(CyApplicationManager.class);
        CyNetwork currentnet = cyApplicationManager.getCurrentNetwork();
        currentnet.getDefaultNetworkTable().getRow(currentnet.getSUID()).set(Config.NAMESPACE, "PTM LOADED", true);
        network.ptm_already_loaded = true;       
    }
    
    public static void PopulatePTMTables(SignorManager manager){
        CyTableManager tableManager = manager.utils.getService(CyTableManager.class);        
        CyApplicationManager cyApplicationManager = manager.utils.getService(CyApplicationManager.class);
        CyNetworkView networkView = cyApplicationManager.getCurrentNetworkView();
        CyNetwork currentnet = cyApplicationManager.getCurrentNetwork();
        try {

            Network networksignor = manager.presentationManager.signorNetMap.get(currentnet);
            //Boolean ptm_already_loaded = currentnet.getDefaultNetworkTable().getColumn("PTM LOADED").getValues(Boolean.class).get(0);
            Boolean ptm_already_loaded = networksignor.ptm_already_loaded;
            //CyNetworkTableManager cyNetworktableManager = manager.utils.getService(CyNetworkTableManager.class);
            if( tableManager.getAllTables(true).contains(networksignor.PTMnodeTable) &&
                tableManager.getAllTables(true).contains(networksignor.PTMedgeTable) && !ptm_already_loaded){
                //Starting populating PTM Table form Node and Edge tables default
                List<CyRow> listrow = currentnet.getDefaultEdgeTable().getAllRows();
                for(int i =0;  i< listrow.size(); i ++){                   
                    CyRow cyrow = listrow.get(i);
                    if (!cyrow.get(Config.NAMESPACE, "RESIDUE", String.class).isEmpty()) {
                       Long parent_edge_uid = cyrow.get("SUID", Long.class);
                       CyEdge cyEdgeParent = currentnet.getEdge(parent_edge_uid);
                  
                       String interaction = cyrow.get(Config.NAMESPACE, "INTERACTION", String.class);
                       String sequence = cyrow.get(Config.NAMESPACE, "SEQUENCE", String.class);
                       CyNode cyNode = currentnet.addNode();         
                                   
                       manager.utils.flushEvents();
                       networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, Color.MAGENTA);
                       networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_WIDTH, 20.0);    
                       networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_HEIGHT, 20.0);  
                       networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_BORDER_WIDTH, 0.0);               
                       
                       
                       networksignor.PTMnodeTable.getRow(cyNode.getSUID()).set(Config.NAMESPACEPTM, "RESIDUE", cyrow.get(Config.NAMESPACE, "RESIDUE", String.class));
                       networksignor.PTMnodeTable.getRow(cyNode.getSUID()).set(Config.NAMESPACEPTM, "TYPE", "residue");
                       networksignor.PTMnodeTable.getRow(cyNode.getSUID()).set(Config.NAMESPACEPTM, "SEQUENCE", cyrow.get(Config.NAMESPACE, "SEQUENCE", String.class));                   
                                      
                       CyNode cyNodeSourceParent = cyEdgeParent.getSource();
                       CyNode cyNodeTargetParent = cyEdgeParent.getTarget();
                       CyEdge cyEdge = currentnet.addEdge(cyNodeSourceParent, cyNode, false);
               
                       networksignor.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, "EdgeParent", parent_edge_uid);
                       networksignor.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, "NodeSourceSUID", cyNodeSourceParent.getSUID());
                       networksignor.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, "NodeTargetSUID", cyNodeTargetParent.getSUID());
                       networksignor.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, "INTERACTION", interaction);        
                       
                       CyEdge cyEdge2 = currentnet.addEdge(cyNodeTargetParent, cyNode, false);
                       networksignor.PTMedgeTable.getRow(cyEdge2.getSUID()).set(Config.NAMESPACEPTM, "EdgeParent", parent_edge_uid);
                       networksignor.PTMedgeTable.getRow(cyEdge2.getSUID()).set(Config.NAMESPACEPTM, "NodeSourceSUID", cyNodeSourceParent.getSUID());
                       networksignor.PTMedgeTable.getRow(cyEdge2.getSUID()).set(Config.NAMESPACEPTM, "NodeTargetSUID", cyNodeTargetParent.getSUID());
                       networksignor.PTMedgeTable.getRow(cyEdge2.getSUID()).set(Config.NAMESPACEPTM, "INTERACTION", interaction); 
                       //manager.utils.flushEvents();
                   }                   
               }
               //manager.PTMtableTocreate = false;               
               /*CyNetworkTableManager cyNetworktableManager = manager.utils.getService(CyNetworkTableManager.class);
               cyNetworktableManager.setTable(manager.cyNetwork, CyNode.class , CONFIG.NAMESPACEPTM, manager.currentNetwork.PTMnodeTable);*/
               //
            }          

            HideEdgeParentPTM(manager);
            manager.utils.info(networkView.getNodeViews().toString()+" la dimensione e "+networkView.getNodeViews().size());
            AlgorithmFactory algfactory = new AlgorithmFactory(networkView, manager);            
            manager.utils.execute(algfactory.createTaskIterator());
            writeNetworkPTMInfo(manager, networksignor);
        }
        catch (Exception e) {
            //manager.utils.info(manager.lastCyNetwork.getSUID().toString());
            manager.utils.info(e.toString()+" in Populate PTM Tables");   
        }
            
    }

    
    
    public static void HideEdgeParentPTM(SignorManager manager){
        CyTableManager tableManager = manager.utils.getService(CyTableManager.class);
        CyApplicationManager cyApplicationManager = manager.utils.getService(CyApplicationManager.class);
        CyNetwork currentnet = cyApplicationManager.getCurrentNetwork();
        CyNetworkView networkView = cyApplicationManager.getCurrentNetworkView();
        Network networksignor = manager.presentationManager.signorNetMap.get(currentnet);
        if(tableManager.getAllTables(true).contains(networksignor.PTMnodeTable) &&
               tableManager.getAllTables(true).contains(networksignor.PTMedgeTable)){
            
            List uid_edge_to_hide = networksignor.PTMedgeTable.getColumn(Config.NAMESPACEPTM, "EdgeParent").getValues(Long.class);
            Map<CyEdge, Long> edges_to_hide = new HashMap<>();
            for (Object uid_edge: uid_edge_to_hide){
                CyEdge edge_to_hide = currentnet.getEdge((Long) uid_edge);
                edges_to_hide.put(edge_to_hide, (Long) uid_edge);
            }
            HideTaskFactory htfactory = manager.utils.getService(HideTaskFactory.class);
            manager.utils.execute(htfactory.createTaskIterator(networkView, null, edges_to_hide.keySet()));
        }           
    }
    
    public static void ShowDefaultView(SignorManager manager){
        try {
            CyTableManager tableManager = manager.utils.getService(CyTableManager.class);
            CyApplicationManager cyApplicationManager = manager.utils.getService(CyApplicationManager.class);
            CyNetwork currentnet = cyApplicationManager.getCurrentNetwork();
            CyNetworkView networkView = cyApplicationManager.getCurrentNetworkView();
            Network networksignor = manager.presentationManager.signorNetMap.get(currentnet);

            if( tableManager.getAllTables(true).contains(networksignor.PTMnodeTable) &&
                   tableManager.getAllTables(true).contains(networksignor.PTMedgeTable)){
                
                List<CyNode> cynl = currentnet.getNodeList();
                List<CyEdge> cyel = currentnet.getEdgeList();
                
                //Grouping node and edges of PTM Tables and edges to view in Default                
                Map<CyNode, Long> ptm_nodes_to_hide = new HashMap<>();
                Map<CyEdge, Long> ptm_edges_to_hide = new HashMap<>();
                Map<CyEdge, Long> def_edges_to_unhide = new HashMap<>();
                

                for (CyNode cynode: cynl){
                    try {
                        if (networksignor.PTMnodeTable.getRow(cynode.getSUID()).get(Config.NAMESPACEPTM, "TYPE", String.class) == "residue"){
                            ptm_nodes_to_hide.put(cynode, cynode.getSUID());
                        }
                    }
                    catch (Exception e){
                        manager.utils.info(e.toString());
                    }
                }               
                
                
                for (CyEdge cyedge: cyel){
                    try {
                        //if (networksignor.PTMedgeTable.getRow(cyedge.getSUID()).get(Config.NAMESPACEPTM, "EdgeParent", String.class) != ""){
                        Long suid = cyedge.getSUID();
                        if(networksignor.PTMedgeTable.getColumn("SUID").getValues(Long.class).contains(suid)){
                            ptm_edges_to_hide.put(cyedge, cyedge.getSUID());
                        }
                        else if (networksignor.PTMedgeTable.getColumn(Config.NAMESPACEPTM, "EdgeParent").getValues(Long.class).contains(suid)){
                            def_edges_to_unhide.put(cyedge, cyedge.getSUID());
                        }
                    }
                    catch (Exception e){
                        manager.utils.error(e.toString());
                    }
                }
                
                HideTaskFactory htfactory = manager.utils.getService(HideTaskFactory.class);
                manager.utils.execute(htfactory.createTaskIterator(networkView, ptm_nodes_to_hide.keySet(), ptm_edges_to_hide.keySet()));               
                                
                UnHideTaskFactory unfactory = manager.utils.getService(UnHideTaskFactory.class);
                manager.utils.execute(unfactory.createTaskIterator(networkView, null, def_edges_to_unhide.keySet()));
            }
        }
        catch (Exception e) {
            //manager.utils.info(manager.lastCyNetwork.getSUID().toString());
            manager.utils.info(e.toString()+" in ShowDefaultView");            
        }
        
    }
    
    /*public static void PopulatePTMTables(SignorManager manager){

        CyTableManager tableManager = manager.utils.getService(CyTableManager.class);
        
        CyApplicationManager cyApplicationManager = manager.utils.getService(CyApplicationManager.class);
        CyNetworkView networkView = cyApplicationManager.getCurrentNetworkView();
        CyNetworkViewManager networkViewManager = manager.utils.getService(CyNetworkViewManager.class);

        //CyNetworkTableManager cyNetworktableManager = manager.utils.getService(CyNetworkTableManager.class);
        try {
           if( tableManager.getAllTables(true).contains(manager.lastNetwork.PTMnodeTable) &&
               tableManager.getAllTables(true).contains(manager.lastNetwork.PTMedgeTable)){
               //Starting populating PTM Table form Node and Edge tables default
               List<CyRow> listrow = manager.lastCyNetwork.getDefaultEdgeTable().getAllRows();
               for(int i =0;  i< listrow.size(); i ++){                   
                   CyRow cyrow = listrow.get(i);
                   if (!cyrow.get(Config.NAMESPACE, "RESIDUE", String.class).isEmpty()) {
                       Long parent_edge_uid = cyrow.get("SUID", Long.class);
                       CyEdge cyEdgeParent = manager.lastCyNetwork.getEdge(parent_edge_uid);
                  
                       String interaction = cyrow.get(Config.NAMESPACE, "INTERACTION", String.class);
                       String sequence = cyrow.get(Config.NAMESPACE, "SEQUENCE", String.class);
                       CyNode cyNode = manager.lastCyNetwork.addNode();            
                       
               manager.utils.flushEvents(); 
               networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, Color.MAGENTA);
               networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_WIDTH, 20.0);    
               networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_HEIGHT, 20.0);  
               networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_BORDER_WIDTH, 0.0);               
                                              
                       manager.lastNetwork.PTMnodeTable.getRow(cyNode.getSUID()).set(Config.NAMESPACEPTM, "RESIDUE", cyrow.get(Config.NAMESPACE, "RESIDUE", String.class));
                       manager.lastNetwork.PTMnodeTable.getRow(cyNode.getSUID()).set(Config.NAMESPACEPTM, "TYPE", "residue");
                       manager.lastNetwork.PTMnodeTable.getRow(cyNode.getSUID()).set(Config.NAMESPACEPTM, "SEQUENCE", cyrow.get(Config.NAMESPACE, "SEQUENCE", String.class));
                       
                                      
                       CyNode cyNodeSourceParent = cyEdgeParent.getSource();
                       CyNode cyNodeTargetParent = cyEdgeParent.getTarget();
                       CyEdge cyEdge = manager.lastCyNetwork.addEdge(cyNodeSourceParent, cyNode, false);
               
                       manager.lastNetwork.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, "EdgeParent", parent_edge_uid);
                       manager.lastNetwork.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, "NodeSourceSUID", cyNodeSourceParent.getSUID());
                       manager.lastNetwork.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, "NodeTargetSUID", cyNodeTargetParent.getSUID());
                       manager.lastNetwork.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, "INTERACTION", interaction);           
                   }                   
               }
               //manager.PTMtableTocreate = false;               
               /*CyNetworkTableManager cyNetworktableManager = manager.utils.getService(CyNetworkTableManager.class);
               cyNetworktableManager.setTable(manager.cyNetwork, CyNode.class , CONFIG.NAMESPACEPTM, manager.currentNetwork.PTMnodeTable);*/
               //
    /*        }
        }
        catch (Exception e) {
            //manager.utils.info(manager.lastCyNetwork.getSUID().toString());
            manager.utils.info(e.toString()+"*****");            
        }
            
    }*/

}

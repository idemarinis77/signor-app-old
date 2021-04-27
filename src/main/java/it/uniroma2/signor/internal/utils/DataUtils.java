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
        currentnet.getDefaultNetworkTable().getRow(currentnet.getSUID()).set(Config.NAMESPACE, "PTMLOADED", true);
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
                tableManager.getAllTables(true).contains(networksignor.PTMedgeTable)){
                //Starting populating PTM Table form Node and Edge tables default
                List<CyRow> listrow = currentnet.getDefaultEdgeTable().getAllRows();
                for(int i =0;  i< listrow.size(); i ++){                   
                    CyRow cyrow = listrow.get(i);
                    if (!cyrow.get(Config.NAMESPACE, "RESIDUE", String.class).isEmpty()) {
                       Long parent_edge_uid = cyrow.get("SUID", Long.class);
                       CyEdge cyEdgeParent = currentnet.getEdge(parent_edge_uid);
                       networksignor.ParentEdges.put(cyEdgeParent, cyEdgeParent.getSUID());
                       String interaction = cyrow.get(Config.NAMESPACE, "INTERACTION", String.class);
                       String sequence = cyrow.get(Config.NAMESPACE, "SEQUENCE", String.class);
                       CyNode cyNode = currentnet.addNode();    
                       networksignor.PTMnodes.put(cyNode, cyNode.getSUID());
                       
                       
                       String label = cyrow.get(Config.NAMESPACE, "RESIDUE", String.class); 
                       manager.utils.flushEvents();
                       networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, Color.MAGENTA);
                       networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_WIDTH, 20.0);    
                       networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_HEIGHT, 20.0);  
                       networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_BORDER_WIDTH, 0.0);               
                       networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_LABEL, label);
                       
                       networksignor.PTMnodeTable.getRow(cyNode.getSUID()).set(Config.NAMESPACEPTM, "RESIDUE", cyrow.get(Config.NAMESPACE, "RESIDUE", String.class));
                       networksignor.PTMnodeTable.getRow(cyNode.getSUID()).set(Config.NAMESPACEPTM, "TYPE", "residue");
                       networksignor.PTMnodeTable.getRow(cyNode.getSUID()).set(Config.NAMESPACEPTM, "SEQUENCE", cyrow.get(Config.NAMESPACE, "SEQUENCE", String.class));                   
                                      
                       CyNode cyNodeSourceParent = cyEdgeParent.getSource();
                       CyNode cyNodeTargetParent = cyEdgeParent.getTarget();
                       CyEdge cyEdge = currentnet.addEdge(cyNodeSourceParent, cyNode, false);
                       networksignor.PTMedges.put(cyEdge, cyEdge.getSUID());
                       networksignor.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, "EdgeParent", parent_edge_uid);
                       networksignor.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, "NodeSourceSUID", cyNodeSourceParent.getSUID());
                       networksignor.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, "NodeTargetSUID", cyNodeTargetParent.getSUID());
                       networksignor.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, "INTERACTION", interaction);        
                       
                       CyEdge cyEdge2 = currentnet.addEdge(cyNodeTargetParent, cyNode, false);
                       networksignor.PTMedgeTable.getRow(cyEdge2.getSUID()).set(Config.NAMESPACEPTM, "EdgeParent", parent_edge_uid);
                       networksignor.PTMedgeTable.getRow(cyEdge2.getSUID()).set(Config.NAMESPACEPTM, "NodeSourceSUID", cyNodeSourceParent.getSUID());
                       networksignor.PTMedgeTable.getRow(cyEdge2.getSUID()).set(Config.NAMESPACEPTM, "NodeTargetSUID", cyNodeTargetParent.getSUID());
                       networksignor.PTMedgeTable.getRow(cyEdge2.getSUID()).set(Config.NAMESPACEPTM, "INTERACTION", interaction); 
                       networksignor.PTMedges.put(cyEdge2, cyEdge2.getSUID());
                       //manager.utils.flushEvents();
                   }                   
               }
               
               //manager.PTMtableTocreate = false;               
               /*CyNetworkTableManager cyNetworktableManager = manager.utils.getService(CyNetworkTableManager.class);
               cyNetworktableManager.setTable(manager.cyNetwork, CyNode.class , CONFIG.NAMESPACEPTM, manager.currentNetwork.PTMnodeTable);*/
               //
               HideEdgeParentPTM(manager);
               AlgorithmFactory algfactory = new AlgorithmFactory(networkView, manager);            
               manager.utils.execute(algfactory.createTaskIterator());
               writeNetworkPTMInfo(manager, networksignor);
            }          
            /*else if (tableManager.getAllTables(true).contains(networksignor.PTMnodeTable) &&
                tableManager.getAllTables(true).contains(networksignor.PTMedgeTable) && ptm_already_loaded){
                UnHidePTM(manager);
            }*/
            
        }
        catch (Exception e) {
            //manager.utils.info(manager.lastCyNetwork.getSUID().toString());
            manager.utils.info(e.toString()+" in Populate PTM Tables");   
        }
            
    }

    /*public static void UnHidePTM(SignorManager manager){
        CyTableManager tableManager = manager.utils.getService(CyTableManager.class);
        CyApplicationManager cyApplicationManager = manager.utils.getService(CyApplicationManager.class);
        CyNetwork currentnet = cyApplicationManager.getCurrentNetwork();
        CyNetworkView networkView = cyApplicationManager.getCurrentNetworkView();
        Network networksignor = manager.presentationManager.signorNetMap.get(currentnet);
        
        if(tableManager.getAllTables(true).contains(networksignor.PTMnodeTable) &&
               tableManager.getAllTables(true).contains(networksignor.PTMedgeTable)){
               List uid_edge_to_unhide = networksignor.PTMedgeTable.getColumn("SUID").getValues(Long.class);
               Map<CyEdge, Long> edges_to_un_hide = new HashMap<>();
                for (Object uid_edge: uid_edge_to_unhide){
                    
                    CyEdge edge_to_hide = currentnet.getEdge((Long) uid_edge);
                    edges_to_un_hide.put(edge_to_hide, (Long) uid_edge);
                    
                }
                UnHideTaskFactory unfactory = manager.utils.getService(UnHideTaskFactory.class);
                manager.utils.execute(unfactory.createTaskIterator(networkView, null, edges_to_un_hide.keySet()));
                
               List uid_node_to_unhide = networksignor.PTMnodeTable.getColumn("SUID").getValues(Long.class);
               Map<CyNode, Long> nodes_to_un_hide = new HashMap<>();
               for (Object uid_node: uid_node_to_unhide){
                    CyNode node_to_hide = currentnet.getNode((Long) uid_node);
                    nodes_to_un_hide.put(node_to_hide, (Long) uid_node);
                }
                UnHideTaskFactory unfactory2 = manager.utils.getService(UnHideTaskFactory.class);
                manager.utils.execute(unfactory2.createTaskIterator(networkView, nodes_to_un_hide.keySet(), null));
                //HideEdgeParentPTM(manager);
        }
    }   */
    
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
            
            
            currentnet.removeNodes(networksignor.PTMnodes.keySet());
            currentnet.removeEdges(networksignor.PTMedges.keySet());
            UnHideTaskFactory unfactory = manager.utils.getService(UnHideTaskFactory.class);
            manager.utils.execute(unfactory.createTaskIterator(networkView, null, networksignor.ParentEdges.keySet()));
            
            
            
     /*       if( tableManager.getAllTables(true).contains(networksignor.PTMnodeTable) &&
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
            }*/
        }
        catch (Exception e) {
            //manager.utils.info(manager.lastCyNetwork.getSUID().toString());
            manager.utils.info(e.toString()+" in ShowDefaultView");            
        }
        
    }
    

}

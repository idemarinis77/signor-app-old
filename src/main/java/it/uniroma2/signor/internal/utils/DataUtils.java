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
import it.uniroma2.signor.internal.view.NetworkView;
import it.uniroma2.signor.internal.conceptualmodel.structures.Table;
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
import org.cytoscape.model.CyTableFactory;



/**
 *
 * @author amministratore
 */
public class DataUtils {
    
    public static Boolean isSignorNetwork(CyNetwork cyNetwork){
        if (cyNetwork!=null)
            return cyNetwork.getRow(cyNetwork).get(CyNetwork.NAME, String.class).startsWith(Config.NTWPREFIX);
        return false;
    }   
    public static Network prepareSubnetwork(Network parentnet, CyNetwork cyNetwork){
        Network subnet = new Network(parentnet.manager, parentnet.parameters);
        subnet.setNetwork(cyNetwork);
        subnet.isDeasesNetwork = parentnet.isDeasesNetwork;
        subnet.isPathwayNetwork = parentnet.isPathwayNetwork;
        subnet.ptm_already_loaded = parentnet.ptm_already_loaded;
        subnet.SetPathwayInfo(parentnet.getPathwayInfo());
        if(parentnet.isSingleSearch()) {
            String entity = (String) subnet.parameters.get("QUERY");
            subnet.setCyNodeRoot(entity);
        }
        return subnet;
    }
//    public static void writeNetworkPTMInfo(SignorManager manager, Network network, Boolean setted){
//        CyApplicationManager cyApplicationManager = manager.utils.getService(CyApplicationManager.class);
//        CyNetwork currentnet = cyApplicationManager.getCurrentNetwork();
//        currentnet.getDefaultNetworkTable().getRow(currentnet.getSUID()).set(Config.NAMESPACE, "PTMLOADED", setted);
//        network.ptm_already_loaded = setted;       
//    }
    
    public static void PopulatePTMTables(SignorManager manager, Boolean interactome){
        CyTableManager tableManager = manager.utils.getService(CyTableManager.class);        
        CyApplicationManager cyApplicationManager = manager.utils.getService(CyApplicationManager.class);
        CyNetworkView networkView = cyApplicationManager.getCurrentNetworkView();
        CyNetwork currentnet = cyApplicationManager.getCurrentNetwork();        
        try {
            Network networksignor = manager.presentationManager.signorNetMap.get(currentnet);
            Boolean ptm_already_loaded = networksignor.ptm_already_loaded;

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
//                       currentnet.getDefaultNodeTable().getRow(cyNode.getSUID()).set("name", label);
                       if(!interactome){
                           manager.utils.flushEvents();
                           networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, Color.MAGENTA);
                           networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_WIDTH, 20.0);    
                           networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_HEIGHT, 20.0);  
                           networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_BORDER_WIDTH, 0.0);               
                           networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_LABEL, label);
                       }
                       if(!ptm_already_loaded){
                           networksignor.PTMnodeTable.getRow(cyNode.getSUID()).set(Config.NAMESPACEPTM, "RESIDUE", cyrow.get(Config.NAMESPACE, "RESIDUE", String.class));
                           networksignor.PTMnodeTable.getRow(cyNode.getSUID()).set(Config.NAMESPACEPTM, "TYPE", "residue");
                           networksignor.PTMnodeTable.getRow(cyNode.getSUID()).set(Config.NAMESPACEPTM, "SEQUENCE", cyrow.get(Config.NAMESPACE, "SEQUENCE", String.class));                   
                       }

                       CyNode cyNodeSourceParent = cyEdgeParent.getSource();
                       CyNode cyNodeTargetParent = cyEdgeParent.getTarget();
                       CyEdge cyEdge = currentnet.addEdge(cyNodeSourceParent, cyNode, false);
    
                       currentnet.getDefaultEdgeTable().getRow(cyEdge.getSUID()).set("name", parent_edge_uid.toString());
                       currentnet.getDefaultEdgeTable().getRow(cyEdge.getSUID()).set("interaction", "up-regulates activity");
                       currentnet.getDefaultEdgeTable().getRow(cyEdge.getSUID()).set(Config.NAMESPACE, "Interaction", "up-regulates activity");
                       networksignor.PTMedges.put(cyEdge, cyEdge.getSUID());                       
                       if(!ptm_already_loaded){

                           networksignor.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, "EdgeParent", parent_edge_uid);
                           networksignor.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, "NodeSourceSUID", cyNodeSourceParent.getSUID());
                           networksignor.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, "NodeTargetSUID", cyNodeTargetParent.getSUID());
                           networksignor.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, "INTERACTION", interaction);   

                        }
                       CyEdge cyEdge2 = currentnet.addEdge(cyNodeTargetParent, cyNode, false);     
                       currentnet.getDefaultEdgeTable().getRow(cyEdge2.getSUID()).set("name", parent_edge_uid.toString());
                       currentnet.getDefaultEdgeTable().getRow(cyEdge.getSUID()).set("interaction", "down-regulates activity");
                       currentnet.getDefaultEdgeTable().getRow(cyEdge2.getSUID()).set(Config.NAMESPACE, "Interaction", "down-regulates activity");
                       if(!ptm_already_loaded){
                           networksignor.PTMedgeTable.getRow(cyEdge2.getSUID()).set(Config.NAMESPACEPTM, "EdgeParent", parent_edge_uid);
                           networksignor.PTMedgeTable.getRow(cyEdge2.getSUID()).set(Config.NAMESPACEPTM, "NodeSourceSUID", cyNodeSourceParent.getSUID());
                           networksignor.PTMedgeTable.getRow(cyEdge2.getSUID()).set(Config.NAMESPACEPTM, "NodeTargetSUID", cyNodeTargetParent.getSUID());
                           networksignor.PTMedgeTable.getRow(cyEdge2.getSUID()).set(Config.NAMESPACEPTM, "INTERACTION", interaction); 
                       }
                       networksignor.PTMedges.put(cyEdge2, cyEdge2.getSUID());
                       //manager.utils.flushEvents();
                   }                   
               }       
               if(!interactome){
                   HideEdgeParentPTM(manager);
                   AlgorithmFactory algfactory = new AlgorithmFactory(networkView, manager);            
                   manager.utils.execute(algfactory.createTaskIterator());
               }
//               writeNetworkPTMInfo(manager, networksignor, true);
               networksignor.ptm_already_loaded = false;
            }   
            manager.presentationManager.signorViewMap.replace(networksignor, NetworkView.Type.PTM);
        }
        catch (Exception e) {
            manager.utils.error("DataUtils PopulatePTMTables() "+e.toString());   
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
    
    public static void ShowDefaultView(SignorManager manager, Boolean interactome){
        try {
            CyApplicationManager cyApplicationManager = manager.utils.getService(CyApplicationManager.class);
            CyNetwork currentnet = cyApplicationManager.getCurrentNetwork();
            CyNetworkView networkView = cyApplicationManager.getCurrentNetworkView();
            Network networksignor = manager.presentationManager.signorNetMap.get(currentnet);            
            
            currentnet.removeNodes(networksignor.PTMnodes.keySet());
            currentnet.removeEdges(networksignor.PTMedges.keySet());
            if(!interactome){
                UnHideTaskFactory unfactory = manager.utils.getService(UnHideTaskFactory.class);
                manager.utils.execute(unfactory.createTaskIterator(networkView, null, networksignor.ParentEdges.keySet()));
            }
            manager.presentationManager.signorViewMap.replace(networksignor, NetworkView.Type.DEFAULT);   
            networksignor.PTMnodes.clear();
            networksignor.PTMedges.clear();
//            writeNetworkPTMInfo(manager, networksignor, false);
        }
        catch (Exception e) {
            manager.utils.info("DataUtils ShowDefaultView() "+e.toString());            
        }
        
    }
    

}

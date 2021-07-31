/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.utils;

import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.*;
import it.uniroma2.signor.internal.conceptualmodel.logic.Nodes.*;
import it.uniroma2.signor.internal.conceptualmodel.logic.Edges.*;
import it.uniroma2.signor.internal.task.query.factories.AlgorithmFactory;
import it.uniroma2.signor.internal.view.NetworkView;
import it.uniroma2.signor.internal.conceptualmodel.logic.Edges.PTMEdgeField;
import java.util.List;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.task.hide.HideTaskFactory;
import org.cytoscape.task.hide.UnHideTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.application.CyApplicationManager;
import java.util.HashMap;
import java.util.Map;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.subnetwork.CySubNetwork;


public class DataUtils {
    
    public static Boolean isSignorNetwork(CyNetwork cyNetwork){
        try {
            if (cyNetwork!=null){
                Boolean name_starts_with = cyNetwork.getRow(cyNetwork).get(CyNetwork.NAME, String.class).startsWith(Config.NTWPREFIX);
                Boolean column_connectsearch = TableUtil.ifColumnIfExist(cyNetwork.getDefaultNetworkTable(), Config.NAMESPACE, NetworkField.CONNECTSEARCH);
                if(name_starts_with && column_connectsearch)
                    return true;
            }
        }
        catch (Exception e){
            //maybe subnetwork
            CySubNetwork subnetwork = (CySubNetwork) cyNetwork;
            return isSignorNetwork(subnetwork.getRootNetwork());
        }
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
            String entity = (String) subnet.parameters.get(NetworkField.QUERY);
            subnet.setCyNodeRoot(entity);
        }        
        return subnet;
    }
    
    public static void PopulatePTMTables(SignorManager manager, Boolean interactome){
        CyTableManager tableManager = manager.utils.getService(CyTableManager.class);        
        CyApplicationManager cyApplicationManager = manager.utils.getService(CyApplicationManager.class);
        CyNetworkView networkView = cyApplicationManager.getCurrentNetworkView();
        CyNetwork currentnet = cyApplicationManager.getCurrentNetwork();        

        try {
            Network networksignor = manager.presentationManager.signorNetMap.get(currentnet);
            Boolean ptm_already_loaded = networksignor.ptm_already_loaded;
            Map<CyNode, Node> nodes = networksignor.getNodes();
            if( tableManager.getAllTables(true).contains(networksignor.PTMnodeTable) &&
                tableManager.getAllTables(true).contains(networksignor.PTMedgeTable)){
                //Starting populating PTM Table form Node and Edge tables default
                List<CyRow> listrow = currentnet.getDefaultEdgeTable().getAllRows();
                for(int i =0;  i< listrow.size(); i ++){                   
                    CyRow cyrow = listrow.get(i);
                    if (!cyrow.get(Config.NAMESPACE, EdgeField.RESIDUE, String.class).isEmpty()) {
                       Long parent_edge_uid = cyrow.get("SUID", Long.class);
                       CyEdge cyEdgeParent = currentnet.getEdge(parent_edge_uid);
                       networksignor.ParentEdges.put(cyEdgeParent, cyEdgeParent.getSUID());
                       String interaction = cyrow.get(Config.NAMESPACE, EdgeField.Interaction, String.class);
                       String sequence = cyrow.get(Config.NAMESPACE, EdgeField.SEQUENCE, String.class);
                       CyNode cyNode = currentnet.addNode();    
                       networksignor.PTMnodes.put(cyNode, cyNode.getSUID());                      

                       CyNode cyNodeSourceParent = cyEdgeParent.getSource();
                       String cyEdgeParent_name = cyrow.get("name", String.class);
                       String cyEdgeParent_shared_name = cyrow.get("shared name", String.class);
                       CyNode cyNodeTargetParent = cyEdgeParent.getTarget();
                       
                       String residue = cyrow.get(Config.NAMESPACE, EdgeField.RESIDUE, String.class);
                       String mechanism = cyrow.get(Config.NAMESPACE, EdgeField.MECHANISM, String.class);
                       String mechanism_orig = mechanism;
                       String effect = cyrow.get(Config.NAMESPACE, EdgeField.Interaction, String.class);
                       if(mechanism.startsWith("de")) mechanism = mechanism.substring(2);
                       String ptmprefix = Config.PTMprefix.get(mechanism);
                       String label = networksignor.getNodes().get(cyNodeTargetParent).toString()+"_"+ptmprefix+residue+"_"+mechanism;

                       currentnet.getDefaultNodeTable().getRow(cyNode.getSUID()).set("shared name", label);
                       currentnet.getDefaultNodeTable().getRow(cyNode.getSUID()).set("name", label);
                       currentnet.getDefaultNodeTable().getRow(cyNode.getSUID()).set(Config.NAMESPACE, NodeField.TYPE, EdgeField.RESIDUE.toLowerCase());
                       currentnet.getDefaultNodeTable().getRow(cyNode.getSUID()).set(Config.NAMESPACE, NodeField.ID, sequence);

                       manager.utils.flushEvents();
                                    
                       CyEdge cyEdge = currentnet.addEdge(cyNodeSourceParent, cyNode, true);
                       if(!ptm_already_loaded){
                           networksignor.PTMnodeTable.getRow(cyNode.getSUID()).set(Config.NAMESPACEPTM, PTMNodeField.RESIDUE, cyrow.get(Config.NAMESPACE, "RESIDUE", String.class));
                           networksignor.PTMnodeTable.getRow(cyNode.getSUID()).set(Config.NAMESPACEPTM, PTMNodeField.TYPE, "residue");
                           networksignor.PTMnodeTable.getRow(cyNode.getSUID()).set(Config.NAMESPACEPTM, PTMNodeField.SEQUENCE, sequence); 
                           networksignor.PTMnodeTable.getRow(cyNode.getSUID()).set(Config.NAMESPACEPTM, PTMNodeField.SOURCE, cyNodeSourceParent.getSUID()); 
                       }                           
                       String first_interaction = MappingFirstDirectionInteraction(mechanism_orig, NodeField.SOURCE);
                       String edge_first_interaction_sh_name = networksignor.getNodes().get(cyNodeSourceParent).toString()+"("+first_interaction+")"+residue;
                       currentnet.getDefaultEdgeTable().getRow(cyEdge.getSUID()).set(Config.NAMESPACE, EdgeField.Interaction, first_interaction);
                       currentnet.getDefaultEdgeTable().getRow(cyEdge.getSUID()).set("shared name", edge_first_interaction_sh_name);
                       currentnet.getDefaultEdgeTable().getRow(cyEdge.getSUID()).set("shared interaction", first_interaction);                       
                       currentnet.getDefaultEdgeTable().getRow(cyEdge.getSUID()).set("name", cyEdgeParent_name);
                       currentnet.getDefaultEdgeTable().getRow(cyEdge.getSUID()).set("interaction", first_interaction);

                       networksignor.PTMedges.put(cyEdge, cyEdge.getSUID());                       
                       if(!ptm_already_loaded){
                           networksignor.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, PTMEdgeField.EdgeParent, parent_edge_uid);
                           networksignor.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, PTMEdgeField.NodeSourceSUID, cyNodeSourceParent.getSUID());
                           networksignor.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, PTMEdgeField.NodeTargetSUID, cyNodeTargetParent.getSUID());
                           networksignor.PTMedgeTable.getRow(cyEdge.getSUID()).set(Config.NAMESPACEPTM, PTMEdgeField.Interaction, interaction);   
                       }
                       String second_interaction = MappingSecondDirectionInteraction(first_interaction, effect);
                       CyEdge cyEdge2 = currentnet.addEdge(cyNode, cyNodeTargetParent, true);
                       String edge_second_interaction_sh_name = residue+"("+second_interaction+")"+networksignor.getNodes().get(cyNodeTargetParent).toString();
                       currentnet.getDefaultEdgeTable().getRow(cyEdge2.getSUID()).set(Config.NAMESPACE, EdgeField.Interaction, second_interaction);
                       currentnet.getDefaultEdgeTable().getRow(cyEdge2.getSUID()).set("shared name", edge_second_interaction_sh_name);
                       currentnet.getDefaultEdgeTable().getRow(cyEdge2.getSUID()).set("shared interaction", second_interaction);
                       currentnet.getDefaultEdgeTable().getRow(cyEdge2.getSUID()).set("name", cyEdgeParent_name);
                       currentnet.getDefaultEdgeTable().getRow(cyEdge2.getSUID()).set("interaction", second_interaction);
                       networksignor.PTMedges.put(cyEdge2, cyEdge2.getSUID());
                       if(!ptm_already_loaded){
                           networksignor.PTMedgeTable.getRow(cyEdge2.getSUID()).set(Config.NAMESPACEPTM, PTMEdgeField.EdgeParent, parent_edge_uid);
                           networksignor.PTMedgeTable.getRow(cyEdge2.getSUID()).set(Config.NAMESPACEPTM, PTMEdgeField.NodeSourceSUID, cyNodeSourceParent.getSUID());
                           networksignor.PTMedgeTable.getRow(cyEdge2.getSUID()).set(Config.NAMESPACEPTM, PTMEdgeField.NodeTargetSUID, cyNodeTargetParent.getSUID());
                           networksignor.PTMedgeTable.getRow(cyEdge2.getSUID()).set(Config.NAMESPACEPTM, PTMEdgeField.Interaction, interaction); 
                       }                       
                       //manager.utils.flushEvents();
                   }                   
               }       
               if(!interactome){
                   HideEdgeParentPTM(manager);
                   AlgorithmFactory algfactory = new AlgorithmFactory(networkView, manager);            
                   manager.utils.execute(algfactory.createTaskIterator());
               }
               networksignor.ptm_already_loaded = false;
               if(!interactome)
                  networksignor.parameters.replace(NetworkField.PTMLOADED, true); 
               networksignor.parameters.replace(NetworkField.VIEW, NetworkView.Type.PTM.toString());
               manager.presentationManager.signorViewMap.replace(networksignor, NetworkView.Type.PTM);
               currentnet.getDefaultNetworkTable().getRow(currentnet.getSUID()).set(Config.NAMESPACE, NetworkField.VIEW, NetworkView.Type.PTM.toString());
            }              
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
            UnHideTaskFactory unfactory = manager.utils.getService(UnHideTaskFactory.class);
            manager.utils.execute(unfactory.createTaskIterator(networkView, null, networksignor.ParentEdges.keySet()));
            networksignor.parameters.replace(NetworkField.VIEW, NetworkView.Type.DEFAULT.toString());
            manager.presentationManager.signorViewMap.replace(networksignor, NetworkView.Type.DEFAULT);   
            currentnet.getDefaultNetworkTable().getRow(currentnet.getSUID()).set(Config.NAMESPACE, NetworkField.VIEW, NetworkView.Type.DEFAULT.toString());
            networksignor.PTMnodes.clear();
            networksignor.PTMedges.clear();
        }
        catch (Exception e) {
            manager.utils.info("DataUtils ShowDefaultView() "+e.toString());            
        }
        
    }
    
    public static String MappingFirstDirectionInteraction(String mechanism, String source_or_target){
        //EFFECT
        //up-regulates*
        //down-regulates*
        
        if(mechanism.startsWith("de") && source_or_target == NodeField.SOURCE)
            return "down-regulates";
        else return "up-regulates";              
    }
    public static String MappingSecondDirectionInteraction(String first_interaction, String effect){
        //EFFECT
        //up-regulates*
        //down-regulates*
        
        if(effect.startsWith("up") && first_interaction.startsWith("up"))
            return "up-regulates";
        else if (effect.startsWith("down") && first_interaction.startsWith("up"))
            return "down-regulates";
        else if (effect.startsWith("down") && first_interaction.startsWith("down"))
            return "up-regulates";
        else if (effect.startsWith("up") && first_interaction.startsWith("down"))
            return "down-regulates";
        else return "unknown";
    }
            
    public static void loadPTMfromTable (Network networksignor, CyNetwork cynet){
        List uid_parent_edge_to_load = networksignor.PTMedgeTable.getColumn(Config.NAMESPACEPTM, PTMEdgeField.EdgeParent).getValues(Long.class);
        for (Object uid_parent_edge: uid_parent_edge_to_load){
                CyEdge edge_to_load = cynet.getEdge((Long) uid_parent_edge);
                networksignor.ParentEdges.put(edge_to_load, (Long) uid_parent_edge);
        }
        List ptm_edge_to_load = networksignor.PTMedgeTable.getColumn(Config.NAMESPACEPTM, "SUID").getValues(Long.class);
        for (Object uid_ptm_edge: ptm_edge_to_load){
            CyEdge edge_to_load = cynet.getEdge((Long) uid_ptm_edge);
            networksignor.PTMedges.put(edge_to_load, (Long) uid_ptm_edge);
        }
        List ptm_node_to_load = networksignor.PTMnodeTable.getColumn(Config.NAMESPACEPTM, "SUID").getValues(Long.class);
        for (Object uid_ptm_node: ptm_node_to_load){
            CyNode node_to_load = cynet.getNode((Long) uid_ptm_node);
            networksignor.PTMnodes.put(node_to_load, (Long) uid_ptm_node);
        }
    }

}

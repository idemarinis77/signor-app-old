/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.utils;

import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.Config;
import java.util.List;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetworkTableManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTableManager;
/**
 *
 * @author amministratore
 */
public class DataManager {
      
    public static void PopulatePTMTables(SignorManager manager){
        Config CONFIG = new Config();
        CyTableManager tableManager = manager.utils.getService(CyTableManager.class);
        CyNetworkTableManager cyNetworktableManager = manager.utils.getService(CyNetworkTableManager.class);
        try {
           if( tableManager.getAllTables(true).contains(manager.currentNetwork.PTMnodeTable) &&
               tableManager.getAllTables(true).contains(manager.currentNetwork.PTMedgeTable)){
               //Starting populating PTM Table form Node and Edge tables default
               List<CyRow> listrow = manager.cyNetwork.getDefaultEdgeTable().getAllRows();
               for(int i =0;  i< listrow.size(); i ++){                   
                   CyRow cyrow = listrow.get(i);
                   if (!cyrow.get(CONFIG.NAMESPACE, "RESIDUE", String.class).isEmpty()) {
                       Long parent_edge_uid = cyrow.get("SUID", Long.class);
                       CyEdge cyEdgeParent = manager.cyNetwork.getEdge(parent_edge_uid);
                  
                       String interaction = cyrow.get(CONFIG.NAMESPACE, "INTERACTION", String.class);
                       String sequence = cyrow.get(CONFIG.NAMESPACE, "SEQUENCE", String.class);
                       CyNode cyNode = manager.cyNetwork.addNode();
                       manager.currentNetwork.PTMnodeTable.getRow(cyNode.getSUID()).set(CONFIG.NAMESPACEPTM, "RESIDUE", cyrow.get(CONFIG.NAMESPACE, "RESIDUE", String.class));
                       manager.currentNetwork.PTMnodeTable.getRow(cyNode.getSUID()).set(CONFIG.NAMESPACEPTM, "TYPE", "residue");
                       manager.currentNetwork.PTMnodeTable.getRow(cyNode.getSUID()).set(CONFIG.NAMESPACEPTM, "SEQUENCE", cyrow.get(CONFIG.NAMESPACE, "SEQUENCE", String.class));
                       
                       CyNode cyNodeSourceParent = cyEdgeParent.getSource();
                       CyNode cyNodeTargetParent = cyEdgeParent.getTarget();
                       CyEdge cyEdge = manager.cyNetwork.addEdge(cyNodeSourceParent, cyNode, false);
                       manager.currentNetwork.PTMedgeTable.getRow(cyEdge.getSUID()).set(CONFIG.NAMESPACEPTM, "EdgeParent", parent_edge_uid);
                       manager.currentNetwork.PTMedgeTable.getRow(cyEdge.getSUID()).set(CONFIG.NAMESPACEPTM, "NodeSourceSUID", cyNodeSourceParent.getSUID());
                       manager.currentNetwork.PTMedgeTable.getRow(cyEdge.getSUID()).set(CONFIG.NAMESPACEPTM, "NodeTargetSUID", cyNodeTargetParent.getSUID());
                       manager.currentNetwork.PTMedgeTable.getRow(cyEdge.getSUID()).set(CONFIG.NAMESPACEPTM, "INTERACTION", interaction);           
                   }                   
               }
               manager.PTMtableTocreate =false;
               //cyNetworktableManager.setTable(manager.cyNetwork, CyNode.class , CONFIG.NAMESPACEPTM, manager.currentNetwork.PTMnodeTable);
               //manager.utils.flushEvents();
            }
        }
        catch (Exception e) {
            manager.utils.info(e.toString());
        }
            
    }
}

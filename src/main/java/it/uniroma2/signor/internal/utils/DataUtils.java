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

import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.View;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TunableSetter;

/**
 *
 * @author amministratore
 */
public class DataUtils {
    
    public static Boolean isSignorNetwork(CyNetwork cyNetwork){
        return cyNetwork.getRow(cyNetwork).get(CyNetwork.NAME, String.class).startsWith(Config.NTWPREFIX);
    }
    
    
    public static void PopulatePTMTables(SignorManager manager){
        Config CONFIG = new Config();
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
                   if (!cyrow.get(CONFIG.NAMESPACE, "RESIDUE", String.class).isEmpty()) {
                       Long parent_edge_uid = cyrow.get("SUID", Long.class);
                       CyEdge cyEdgeParent = manager.lastCyNetwork.getEdge(parent_edge_uid);
                  
                       String interaction = cyrow.get(CONFIG.NAMESPACE, "INTERACTION", String.class);
                       String sequence = cyrow.get(CONFIG.NAMESPACE, "SEQUENCE", String.class);
                       CyNode cyNode = manager.lastCyNetwork.addNode();                      
                       

               manager.utils.flushEvents(); 
               networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, Color.MAGENTA);
               networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_WIDTH, 20.0);    
               networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_HEIGHT, 20.0);  
               networkView.getNodeView(cyNode).setLockedValue(BasicVisualLexicon.NODE_BORDER_WIDTH, 0.0);               
                       
                       
                       manager.lastNetwork.PTMnodeTable.getRow(cyNode.getSUID()).set(CONFIG.NAMESPACEPTM, "RESIDUE", cyrow.get(CONFIG.NAMESPACE, "RESIDUE", String.class));
                       manager.lastNetwork.PTMnodeTable.getRow(cyNode.getSUID()).set(CONFIG.NAMESPACEPTM, "TYPE", "residue");
                       manager.lastNetwork.PTMnodeTable.getRow(cyNode.getSUID()).set(CONFIG.NAMESPACEPTM, "SEQUENCE", cyrow.get(CONFIG.NAMESPACE, "SEQUENCE", String.class));
                       
                                      
                       CyNode cyNodeSourceParent = cyEdgeParent.getSource();
                       CyNode cyNodeTargetParent = cyEdgeParent.getTarget();
                       CyEdge cyEdge = manager.lastCyNetwork.addEdge(cyNodeSourceParent, cyNode, false);
               
                       manager.lastNetwork.PTMedgeTable.getRow(cyEdge.getSUID()).set(CONFIG.NAMESPACEPTM, "EdgeParent", parent_edge_uid);
                       manager.lastNetwork.PTMedgeTable.getRow(cyEdge.getSUID()).set(CONFIG.NAMESPACEPTM, "NodeSourceSUID", cyNodeSourceParent.getSUID());
                       manager.lastNetwork.PTMedgeTable.getRow(cyEdge.getSUID()).set(CONFIG.NAMESPACEPTM, "NodeTargetSUID", cyNodeTargetParent.getSUID());
                       manager.lastNetwork.PTMedgeTable.getRow(cyEdge.getSUID()).set(CONFIG.NAMESPACEPTM, "INTERACTION", interaction);           
                   }                   
               }
               //manager.PTMtableTocreate = false;               
               /*CyNetworkTableManager cyNetworktableManager = manager.utils.getService(CyNetworkTableManager.class);
               cyNetworktableManager.setTable(manager.cyNetwork, CyNode.class , CONFIG.NAMESPACEPTM, manager.currentNetwork.PTMnodeTable);*/
               //
            }
        }
        catch (Exception e) {
            manager.utils.info(manager.lastCyNetwork.getSUID().toString());
            manager.utils.info(e.toString()+"*****");            
        }
            
    }

}

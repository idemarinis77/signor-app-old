package it.uniroma2.signor.app.internal.conceptualmodel.structures;

import org.cytoscape.model.*;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.NetworkField;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Pathway.PathwayField;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Edges.*;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Nodes.*;
import java.util.*;
import it.uniroma2.signor.app.internal.Config;
import it.uniroma2.signor.app.internal.utils.TableUtil;
import org.cytoscape.model.CyNetwork;

public class Table {
    public final String primaryKey;
    public final boolean isPublic;
    public final boolean isMutable;
    public final CyTableFactory.InitialTableSize initialSize;

    public Table(String primaryKey, boolean isPublic, boolean isMutable, CyTableFactory.InitialTableSize initialSize) {        
        this.primaryKey = primaryKey;
        this.isPublic = isPublic;
        this.isMutable = isMutable;
        this.initialSize = initialSize;      
    }

    public void buildDefaultTable(SignorManager manager, String title, CyNetwork cynetwork) {
        switch(title){
            case "Node":
            NodeField.NODEFIELD.forEach((k, v) ->
                     TableUtil.createColumnIfNeeded(cynetwork.getDefaultNodeTable(), v, k, Config.NAMESPACE));                 
                break;            
            case "Edge":
            for (Map.Entry<String,Class<?>> entry : EdgeField.ORDEREDEDGEFIELD.entrySet()) {
                TableUtil.createColumnIfNeeded(cynetwork.getDefaultEdgeTable(), entry.getValue(), entry.getKey(), Config.NAMESPACE);
            }
                break;
            case "Network":
            NetworkField.networkTableField().forEach((k, v) ->
                      TableUtil.createColumnIfNeeded(cynetwork.getDefaultNetworkTable(), v, k, Config.NAMESPACE));
            break;
        }       
    }
    
    public static void buildAdditionalInfoForSummary(SignorManager manager, CyNetwork cynetwork) {
                NodeField.NODEFIELDADDITIONAL.forEach((k, v) ->
                  TableUtil.createColumnIfNeeded(cynetwork.getDefaultNodeTable(), v, k, Config.NAMESPACE));  
    }
    
    public void buildPTHTable(SignorManager manager, CyNetwork cynetwork) {
        NodeField.NODEFIELD.forEach((k, v) ->
                  TableUtil.createColumnIfNeeded(cynetwork.getDefaultNodeTable(), v, k, Config.NAMESPACE));
        for (Map.Entry<String,Class<?>> entry : PathwayField.ORDEREDEDGEFIELDPTH.entrySet()) {
                TableUtil.createColumnIfNeeded(cynetwork.getDefaultEdgeTable(), entry.getValue(), entry.getKey(), Config.NAMESPACE);
        }     
    }
    
//    public void buildPTMTable(SignorManager manager, String title, CyNetwork cynetwork) {
//        var PTMtableFactory = manager.utils.getService(CyTableFactory.class);                  
//        CyTableManager tableManager = manager.utils.getService(CyTableManager.class);
//        Set<CyTable> alltable = tableManager.getAllTables(true);
//        String prefix_col = cynetwork.getRow(cynetwork).get(CyNetwork.NAME, String.class)+" - ";
//        switch(title){
//            case "PTMNode":
//                //If Table is not created let's do it
//                if(alltable.stream().filter(t -> t.getTitle().matches(prefix_col+title)).count() == 0){
//                    CyTable PTMNODEtable = PTMtableFactory.createTable(prefix_col+title, primaryKey, Long.class, isPublic, isMutable, initialSize);
//                    PTMNodeField.PTMNODEFIELD.forEach((k, v) ->
//                        TableUtil.createColumnIfNeeded(PTMNODEtable, v, k, Config.NAMESPACEPTM));
//                    tableManager.addTable(PTMNODEtable);
//                    manager.presentationManager.signorNetMap.get(cynetwork).SetPTMNodeTable(PTMNODEtable);
//                }
//            break;
//            case "PTMEdge":
//                //If Table is not created let's do it
//                if(alltable.stream().filter(t -> t.getTitle().matches(prefix_col+title)).count() == 0){
//                    CyTable PTMEDGEtable = PTMtableFactory.createTable(prefix_col+title, primaryKey, Long.class, isPublic, isMutable, initialSize);
//                    PTMEdgeField.PTMEDGEFIELD.forEach((k, v) ->
//                        TableUtil.createColumnIfNeeded(PTMEDGEtable, v, k, Config.NAMESPACEPTM));
//                    tableManager.addTable(PTMEDGEtable);
//                    manager.presentationManager.signorNetMap.get(cynetwork).SetPTMEdgeTable(PTMEDGEtable);
//                }
//            break;
//        }  
//    } 


}



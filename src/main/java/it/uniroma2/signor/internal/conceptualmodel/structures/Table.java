package it.uniroma2.signor.internal.conceptualmodel.structures;

import org.cytoscape.model.*;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.NetworkField;
import it.uniroma2.signor.internal.conceptualmodel.logic.Nodes.*;
import it.uniroma2.signor.internal.conceptualmodel.logic.Edges.*;
import it.uniroma2.signor.internal.conceptualmodel.logic.Pathway.PathwayField;
import java.util.*;
import java.util.stream.Collectors;
import static org.cytoscape.model.CyTableFactory.InitialTableSize.MEDIUM;
import static org.cytoscape.model.CyTableFactory.InitialTableSize.SMALL;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.ConfigPathway;
import it.uniroma2.signor.internal.utils.TableUtil;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyEdge;

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
            EdgeField.EDGEFIELD.forEach((k, v) -> 
                    TableUtil.createColumnIfNeeded(cynetwork.getDefaultEdgeTable(), v, k, Config.NAMESPACE));
//                   cynetwork.getDefaultEdgeTable().createColumn(Config.NAMESPACE, k, v, false));
//                   manager.lastCyNetwork.getDefaultEdgeTable().createColumn(Config.NAMESPACE, k, v, false));
                break;
            case "Network":
                //            Config.NETWORKFIELD.forEach((k, v) ->
                NetworkField.networkTableField().forEach((k, v) ->
                      TableUtil.createColumnIfNeeded(cynetwork.getDefaultNetworkTable(), v, k, Config.NAMESPACE));
//                    cynetwork.getDefaultNetworkTable().createColumn(Config.NAMESPACE, k, v, false));
//                    manager.lastCyNetwork.getDefaultNetworkTable().createColumn(Config.NAMESPACE, k, v, false));
            break;
        }       
    }
    
    public static void buildAdditionalInfoForSummary(SignorManager manager, CyNetwork cynetwork) {
        /*CyTableManager tableManager = manager.utils.getService(CyTableManager.class);
        if (!tableManager.getTable(manager.lastCyNetwork.getDefaultNodeTable().getSUID()).
                          getColumns(Config.NAMESPACE).containsAll(Config.NODEFIELDADDITIONAL.keySet())) {*/
                NodeField.NODEFIELDADDITIONAL.forEach((k, v) ->
                  TableUtil.createColumnIfNeeded(cynetwork.getDefaultNodeTable(), v, k, Config.NAMESPACE));
                    /*manager.lastCyNetwork.getDefaultNodeTable().createColumn(Config.NAMESPACE, k, v, false));
        }*/
        
    }
    
    public void buildPTHTable(SignorManager manager, CyNetwork cynetwork) {
        NodeField.NODEFIELD.forEach((k, v) ->
                  TableUtil.createColumnIfNeeded(cynetwork.getDefaultNodeTable(), v, k, Config.NAMESPACE));
        PathwayField.EDGEFIELDPTH.forEach((k, v) ->
                    TableUtil.createColumnIfNeeded(cynetwork.getDefaultEdgeTable(), v, k, Config.NAMESPACE));      
    }
    
    public void buildPTMTable(SignorManager manager, String title, CyNetwork cynetwork) {
        var PTMtableFactory = manager.utils.getService(CyTableFactory.class);                  
        CyTableManager tableManager = manager.utils.getService(CyTableManager.class);
        Set<CyTable> alltable = tableManager.getAllTables(true);
        String prefix_col = cynetwork.getRow(cynetwork).get(CyNetwork.NAME, String.class)+" - ";
        switch(title){
            case "PTMNode":
                //If Table is not created let's do it
                if(alltable.stream().filter(t -> t.getTitle().matches(prefix_col+title)).count() == 0){
                    CyTable PTMNODEtable = PTMtableFactory.createTable(prefix_col+title, primaryKey, Long.class, isPublic, isMutable, initialSize);
                    PTMNodeField.PTMNODEFIELD.forEach((k, v) ->
                        TableUtil.createColumnIfNeeded(PTMNODEtable, v, k, Config.NAMESPACEPTM));
//                        PTMNODEtable.createColumn(Config.NAMESPACEPTM, k, v, isMutable));             
                    tableManager.addTable(PTMNODEtable);
//                    manager.lastNetwork.SetPTMNodeTable(PTMNODEtable);
                    manager.presentationManager.signorNetMap.get(cynetwork).SetPTMNodeTable(PTMNODEtable);
                }
            break;
            case "PTMEdge":
                //If Table is not created let's do it
                if(alltable.stream().filter(t -> t.getTitle().matches(prefix_col+title)).count() == 0){
                    CyTable PTMEDGEtable = PTMtableFactory.createTable(prefix_col+title, primaryKey, Long.class, isPublic, isMutable, initialSize);
                    PTMEdgeField.PTMEDGEFIELD.forEach((k, v) ->
                        TableUtil.createColumnIfNeeded(PTMEDGEtable, v, k, Config.NAMESPACEPTM));
//                        PTMEDGEtable.createColumn(Config.NAMESPACEPTM, k, v, isMutable));
                    tableManager.addTable(PTMEDGEtable);
//                    manager.lastNetwork.SetPTMEdgeTable(PTMEDGEtable);
                    manager.presentationManager.signorNetMap.get(cynetwork).SetPTMEdgeTable(PTMEDGEtable);
                }
            break;
        }  
    } 


}



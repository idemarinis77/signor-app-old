package it.uniroma2.signor.internal.conceptualmodel.structures;

import org.cytoscape.model.*;
import it.uniroma2.signor.internal.managers.SignorManager;

import java.util.*;
import java.util.stream.Collectors;
import static org.cytoscape.model.CyTableFactory.InitialTableSize.MEDIUM;
import static org.cytoscape.model.CyTableFactory.InitialTableSize.SMALL;
import it.uniroma2.signor.internal.Config;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyEdge;

public class Table {
    public final String primaryKey;
    public final boolean isPublic;
    public final boolean isMutable;
    public final CyTableFactory.InitialTableSize initialSize;
    private Config CONFIG = new Config();

    public Table(String primaryKey, boolean isPublic, boolean isMutable, CyTableFactory.InitialTableSize initialSize) {        
        this.primaryKey = primaryKey;
        this.isPublic = isPublic;
        this.isMutable = isMutable;
        this.initialSize = initialSize;      
    }

    public void buildDefaultTable(SignorManager manager, String title) {
        var tableFactory = manager.utils.getService(CyTableFactory.class);        
        CyTable table; 
        switch(title){
            case "Node":
            CONFIG.NODEFIELD.forEach((k, v) ->
                    manager.cyNetwork.getDefaultNodeTable().createColumn(CONFIG.NAMESPACE, k, v, false));
                break;            
            case "Edge":
            CONFIG.EDGEFIELD.forEach((k, v) -> 
                   manager.cyNetwork.getDefaultEdgeTable().createColumn(CONFIG.NAMESPACE, k, v, false));
                break;
        }       
    }
    
    public void buildPTMTable(SignorManager manager, String title) {
        var PTMtableFactory = manager.utils.getService(CyTableFactory.class);                  
        CyTableManager tableManager = manager.utils.getService(CyTableManager.class);
        Set<CyTable> alltable = tableManager.getAllTables(true);
        String prefix_col = manager.cyNetwork.getRow(manager.cyNetwork).get(CyNetwork.NAME, String.class)+" - ";
        
        switch(title){
            case "PTMNode":
                //If Table is not created let's do it
                if(alltable.stream().filter(t -> t.getTitle().matches(prefix_col+title)).count() == 0){
                    CyTable PTMNODEtable = PTMtableFactory.createTable(prefix_col+title, primaryKey, Long.class, isPublic, isMutable, initialSize);
                    CONFIG.PTMNODEFIELD.forEach((k, v) ->
                        PTMNODEtable.createColumn(CONFIG.NAMESPACEPTM, k, v, isMutable));             
                    tableManager.addTable(PTMNODEtable);
                    manager.currentNetwork.SetPTMNodeTable(PTMNODEtable);
                }
            break;
            case "PTMEdge":
                //If Table is not created let's do it
                if(alltable.stream().filter(t -> t.getTitle().matches(prefix_col+title)).count() == 0){
                    CyTable PTMEDGEtable = PTMtableFactory.createTable(prefix_col+title, primaryKey, Long.class, isPublic, isMutable, initialSize);
                    CONFIG.PTMEDGEFIELD.forEach((k, v) ->
                        PTMEDGEtable.createColumn(CONFIG.NAMESPACEPTM, k, v, isMutable));
                    tableManager.addTable(PTMEDGEtable);
                    manager.currentNetwork.SetPTMEdgeTable(PTMEDGEtable);
                }
            break;
        }  
    }

}



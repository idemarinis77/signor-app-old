package it.uniroma2.signor.internal.conceptualmodel.logic.Network;

import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.*;
import org.cytoscape.model.events.*;
import org.cytoscape.task.hide.HideTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import it.uniroma2.signor.internal.conceptualmodel.logic.Edges.NodeCouple;
import it.uniroma2.signor.internal.conceptualmodel.logic.Edges.Edge;

import it.uniroma2.signor.internal.conceptualmodel.logic.Nodes.Node;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.utils.HttpUtils;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.ConfigResources;
//import static uk.ac.ebi.intact.app.internal.utils.ModelUtils.Position;

public class Network {
    //implements AddedEdgesListener, AboutToRemoveEdgesListener, RemovedEdgesListener {
    //ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    public final SignorManager manager;
    CyNetwork cyNetwork;
    CyTable edgeTable;
    CyTable nodeTable;
    public CyTable PTMnodeTable;
    public CyTable PTMedgeTable;  

    private final Map<CyNode, Node> nodes = new HashMap<>();
    private final Map<CyEdge, Edge> edges = new HashMap<>();

    private final Map<NodeCouple, List<CyEdge>> coupleToDefaultEdges = new HashMap<>();
    
    public  Map<CyNode, Long> PTMnodes = new HashMap<>();
    public  Map<CyEdge, Long> PTMedges = new HashMap<>();
    public  Map<CyEdge, Long> ParentEdges = new HashMap<>();
   
    public Boolean isPathwayNetwork = false;
    public Boolean isDeasesNetwork = false;
    public Boolean ptm_already_loaded = false;
    public CyNode rootNode;
    private Node networkRootNode;

    public final HashMap<String, ?> parameters;
    
    public Network(SignorManager manager, HashMap<String, ?> parameters) {
        this.manager = manager;
        this.parameters = parameters;          
    }

    public Network(CyNetwork cynet, String netname, SignorManager manager){
        //Creating netowrk from session
        this.manager = manager;
        if(netname.startsWith("SIGNOR NTWP")){
            isPathwayNetwork = true;
            parameters = null;
        }
        else if(netname.startsWith("SIGNOR NTWD")){
            isDeasesNetwork = true;
            parameters = null;
        }
        else {
            parameters = new HashMap() { {put("SINGLESEARCH", true); }};
        }
        this.cyNetwork = cynet;
        setNetwork(cynet);
    }
    public CyNetwork getCyNetwork() {
        return cyNetwork;
    }
    public Integer numberOfEdes(){
        return edges.size();
    }
    public void SetPTMNodeTable (CyTable cytable){
        this.PTMnodeTable = cytable;
    }
    public void SetPTMEdgeTable (CyTable cytable){
        this.PTMedgeTable = cytable;
    }
    
    public void writeSearchNetwork(){
        for (String key: parameters.keySet()){
            this.cyNetwork.getDefaultNetworkTable().getRow(this.cyNetwork.getSUID()).set(Config.NAMESPACE, key, parameters.get(key));
            //this.cyNetwork.getDefaultNetworkTable().getRowCount()
         }
    }
   
    public boolean isSingleSearch(){
        return this.cyNetwork.getDefaultNetworkTable().getRow(this.cyNetwork.getSUID()).get(Config.NAMESPACE, "SINGLESEARCH", Boolean.class);
    }
    
    public void setCyNodeRoot(String entity){
        Collection<CyRow> listrow = this.cyNetwork.getDefaultNodeTable().getMatchingRows​(Config.NAMESPACE, "ID", entity);
        CyNode rootNode_to_find;
        for (listrow.iterator(); listrow.iterator().hasNext();){
            CyRow row = listrow.iterator().next();
            if (row.get(Config.NAMESPACE, "ID", String.class).equals(entity)){
                rootNode_to_find = cyNetwork.getNode(row.get("SUID", Long.class));
                this.rootNode = rootNode_to_find;
                break;
            }
        }   
        this.networkRootNode = new Node(this, rootNode);
        networkRootNode.Summary();
    }
    
    public Node getNetworkRootNode(){
        return networkRootNode;
    }
    
    public Map<CyNode, Node> getNodes(){
        return nodes;
    }

    public void setNetwork(CyNetwork cyNetwork) {
        this.cyNetwork = cyNetwork;
        
        cyNetwork.getNodeList().forEach(node -> nodes.put(node, new Node(this, node)));

        edgeTable = cyNetwork.getDefaultEdgeTable();
        nodeTable = cyNetwork.getDefaultNodeTable();
            
        //TableUtil.NullAndNonNullEdges identifiedOrNotEdges = TableUtils.splitNullAndNonNullEdges(cyNetwork, EdgeFields.SIGNOR_ID);
        
        for (CyEdge signorEdge : cyNetwork.getEdgeList()) {
            
            edges.put(signorEdge, (Edge) Edge.createEdge(this, signorEdge));
        }
        try {
        NodeCouple.putEdgesToCouples(edges.keySet(), coupleToDefaultEdges);
        }
        catch (Exception e){
            manager.utils.error(e.toString());
        }
    }    
}

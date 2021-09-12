package it.uniroma2.signor.app.internal.conceptualmodel.logic.Network;

import org.cytoscape.model.*;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Edges.*;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Nodes.*;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import java.util.List;
import java.util.*;
import it.uniroma2.signor.app.internal.Config;

public class Network {    
    public final SignorManager manager;
    CyNetwork cyNetwork;
    CyTable edgeTable;
    CyTable nodeTable;
//    public CyTable PTMnodeTable;
//    public CyTable PTMedgeTable;  

    private final Map<CyNode, Node> nodes = new HashMap<>();
    private final Map<CyNode, Node> searched_nodes = new HashMap<>();
    private final Map<CyEdge, Edge> edges = new HashMap<>();

//    private String entityNotFound="";
    
    public  Map<CyNode, Long> PTMnodes = new HashMap<>();
    public  Map<CyEdge, Long> PTMedges = new HashMap<>();
    public  Map<CyEdge, Long> ParentEdges = new HashMap<>();
   
//    public Boolean isPathwayNetwork = false;
//    public Boolean isDeasesNetwork = false;
//    public Boolean ptm_already_loaded = false;
    public CyNode rootNode;
    private Node networkRootNode;
    private ArrayList<String> pathway_info = new ArrayList<String>();
    public final HashMap<String, Object> parameters;
    
    public Network(SignorManager manager, HashMap<String, Object> parameters) {
        this.manager = manager;
        this.parameters = parameters;          
    }
    
    public CyNetwork getCyNetwork() {
        return cyNetwork;
    }
    public Integer numberOfEdges(){
        return edges.size();
    }
//    public void SetPTMNodeTable (CyTable cytable){
//        this.PTMnodeTable = cytable;
//    }
//    public void SetPTMEdgeTable (CyTable cytable){
//        this.PTMedgeTable = cytable;
//    }
    public void SetPathwayInfo (ArrayList<String> pathway_info){
        this.pathway_info = pathway_info;
    }
    public void writeSearchNetwork(){      
        for (String key: parameters.keySet()){          
            
            this.cyNetwork.getDefaultNetworkTable().getRow(this.cyNetwork.getSUID()).set(Config.NAMESPACE, key, parameters.get(key));
         }
    }
//    public boolean isPTMNetwork(){
//        Iterator it = nodes.entrySet().iterator();
//        while (it.hasNext()){
//            Map.Entry pair = (Map.Entry)it.next();
//            Node node = (Node) pair.getValue();            
//            if (node.getSummary().get(NodeField.TYPE)=="residue"){
//                return true;
//            }
//        }   
//        return false;
//    }    
    public boolean isSingleSearch(){
          return (Boolean) this.parameters.get(NetworkField.SINGLESEARCH);
    }
    
    public void setCyNodeRoot(String entity){
        Collection<CyRow> listrow = this.cyNetwork.getDefaultNodeTable().getMatchingRows​(Config.NAMESPACE, NodeField.ID, entity);
        CyNode rootNode_to_find;
        for (listrow.iterator(); listrow.iterator().hasNext();){
            CyRow row = listrow.iterator().next();
            if (row.get(Config.NAMESPACE, NodeField.ID, String.class).equals(entity)){
                rootNode_to_find = cyNetwork.getNode(row.get("SUID", Long.class));
                this.rootNode = rootNode_to_find;
                break;
            }
        }   
        this.networkRootNode = new Node(this, rootNode);
        networkRootNode.Summary(entity);
    }
    
    public Node getNetworkRootNode(){
        return networkRootNode;
    }
    
    public Map<CyNode, Node> getNodes(){
        return nodes;
    }
    
    public Map<CyNode, Node> getSearchedNodes(){
        return searched_nodes;
    }
    
    public Map<CyEdge, Edge> getEdges(){
        return edges;
    }
    
//    public ArrayList<String> getPathwayInfo(){
//        return this.pathway_info;
//    }
    public void setCyNetwork(CyNetwork cyNetwork) {
        this.cyNetwork = cyNetwork;
    }
    public void setNetwork(CyNetwork cyNetwork) {
//        this.cyNetwork = cyNetwork;
        setCyNetwork(cyNetwork);
        String searched_query = (String) this.parameters.get(NetworkField.QUERY);
        List<String> searched_entities = Arrays.asList(searched_query.split(" "));
        
        for (CyNode node: cyNetwork.getNodeList()){
            nodes.put(node, new Node(this, node));
            if(searched_entities.contains(this.cyNetwork.getDefaultNodeTable().getRow(node.getSUID()).get(Config.NAMESPACE, NodeField.ID, String.class))){
                searched_nodes.put(node, new Node(this, node));
            }    
            else if (searched_entities.contains(this.cyNetwork.getDefaultNodeTable().getRow(node.getSUID()).get(Config.NAMESPACE, NodeField.ENTITY, String.class))){
                searched_nodes.put(node, new Node(this, node));
            } 
        }
        edgeTable = cyNetwork.getDefaultEdgeTable();
        nodeTable = cyNetwork.getDefaultNodeTable();         
       
        for (CyEdge signorEdge : cyNetwork.getEdgeList()) {
            
            edges.put(signorEdge, (Edge) Edge.createEdge(this, signorEdge));
        }
    }    
    
    public void setEntityNotFound(String text){
//        this.entityNotFound=text;
        this.parameters.put(NetworkField.ENTITYNOTFOUND, text);
    }
    
    public String getEntityNotFound(){
        return (String) this.parameters.get(NetworkField.ENTITYNOTFOUND);
    }
}

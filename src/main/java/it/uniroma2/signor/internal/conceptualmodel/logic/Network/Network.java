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
        Collection<CyRow> listrow = cyNetwork.getDefaultNodeTable().getMatchingRows​(Config.NAMESPACE, "ID", entity);
        CyNode rootNode_to_find;
        for (listrow.iterator(); listrow.iterator().hasNext();){
            CyRow row = listrow.iterator().next();
            if (row.get(Config.NAMESPACE, "ID", String.class).equals(entity)){
                rootNode_to_find = cyNetwork.getNode(row.get("SUID", Long.class));
                this.rootNode = rootNode_to_find;
                break;
            }
        }   
    }
    
    //provare il complex con SIGNOR-C144
    //chemical CHEBI:3441
    //fusion protein SIGNOR-FP3
    //proteinfamily	SIGNOR-PF16
    //smallmolecule CHEBI:17650
    //stimulus	SIGNOR-ST13
    //phenotype	SIGNOR-PH92
    // mirna	MI0000300
    
   /* public void completeRootNodeInfo(){
        if(this.isSingleSearch()){            
            String typeOfNode = nodes.get(rootNode).summary.get("TYPE");
            String id = nodes.get(rootNode).summary.get("ID");
            Node rootNodeNet = nodes.get(rootNode);            
            
            ArrayList<String> packed_results = HttpUtils.parseWSNoheader(HttpUtils.getHTTPSignor(ConfigResources.ENTITYINFO+id, manager));
            String[] results = packed_results.get(0).split("\t");

            switch(typeOfNode){
                case "complex":
                //Formed By
                //E.g. SIGNOR-C144: CDK5/CDK5R1	SIGNOR-C144		Q00535,Q15078	CPX-2201 
                    for (Integer i =0; i< Config.HEADER_ROOT_NODE_ADDINFO_COMPLEX.length; i++){
                        if(Config.NODEFIELDADDITIONAL.containsKey(Config.HEADER_ROOT_NODE_ADDINFO_COMPLEX[i])){
                            rootNodeNet.summary.put(Config.HEADER_ROOT_NODE_ADDINFO_COMPLEX[i], results[i]);
                        }
                    }
                break;        
                case "chemical":
                //Name, Synonyms, IUPAC, Formula
                //E.g. CHEBI:3441: carvedilol	(+-)-1-(Carbazol-4-yloxy)-3-((2-(o-methoxyphenoxy)ethyl)amino)-2-propanol....	CHEBI:3441		C24H26N2O4	1-(9H-carbazol-4-yloxy)-3-{[2-(2-methoxyphenoxy)ethyl]amino}propan-2-ol
                    for (Integer i =0; i< Config.HEADER_ROOT_NODE_ADDINFO_CHEMICAL.length; i++){
                        if(Config.NODEFIELDADDITIONAL.containsKey(Config.HEADER_ROOT_NODE_ADDINFO_CHEMICAL[i])){
                            rootNodeNet.summary.put(Config.HEADER_ROOT_NODE_ADDINFO_CHEMICAL[i], results[i]);
                        }
                    }
                break; 
                case "smallmolecule":
                //Name, Synonyms, IUPAC, Formula
                //E.g. CHEBI:17650: cortisol	(11beta)-11,17,21-trihydroxypregn-4-ene-3,20-dione...	CHEBI:17650		C21H30O5	11beta,17,21-trihydroxypregn-4-ene-3,20-dione
                    for (Integer i =0; i< Config.HEADER_ROOT_NODE_ADDINFO_CHEMICAL.length; i++){
                        if(Config.NODEFIELDADDITIONAL.containsKey(Config.HEADER_ROOT_NODE_ADDINFO_CHEMICAL[i])){
                            rootNodeNet.summary.put(Config.HEADER_ROOT_NODE_ADDINFO_CHEMICAL[i], results[i]);
                        }
                    }
                break; 
                case "fusion protein":
                //Name, Formed by, Description, Sequence
                //E. g. SIGNOR-FP3: CBFbeta-MYH11	SIGNOR-FP3	Inversion of chromosome 16,.....	MPRVVPDQRSKFENEEFFR.... P35749,Q13951
                    for (Integer i =0; i< Config.HEADER_ROOT_NODE_ADDINFO_FUSIONPROTEIN.length; i++){
                        if(Config.NODEFIELDADDITIONAL.containsKey(Config.HEADER_ROOT_NODE_ADDINFO_FUSIONPROTEIN[i])){
                            rootNodeNet.summary.put(Config.HEADER_ROOT_NODE_ADDINFO_FUSIONPROTEIN[i], results[i]);
                        }
                    }
                break;   
                case "proteinfamily":
                //Name, Formed by
                //E.g. SIGNOR-PF16: p38	SIGNOR-PF16		P53778,O15264,Q16539,Q15759
                    for (Integer i =0; i< Config.HEADER_ROOT_NODE_ADDINFO_PROTEINFAMILY.length; i++){
                        if(Config.NODEFIELDADDITIONAL.containsKey(Config.HEADER_ROOT_NODE_ADDINFO_PROTEINFAMILY[i])){
                            rootNodeNet.summary.put(Config.HEADER_ROOT_NODE_ADDINFO_PROTEINFAMILY[i], results[i]);
                        }
                    }
                case "stimulus":
                //Description
                //E.g SIGNOR-ST13: Cell-Cell_contact	SIGNOR-ST13	Cell-Cell contact
                    for (Integer i =0; i< Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS.length; i++){
                        if(Config.NODEFIELDADDITIONAL.containsKey(Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS[i])){
                            rootNodeNet.summary.put(Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS[i], results[i]);
                        }
                    }
                break;
                case "phenotype":
                //Name, ID, Description
                //E.g. SIGNOR-PH92: Degranulation	SIGNOR-PH92	The regulated exocytosis of secretory granules
                    for (Integer i =0; i< Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS.length; i++){
                        if(Config.NODEFIELDADDITIONAL.containsKey(Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS[i])){
                            rootNodeNet.summary.put(Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS[i], results[i]);
                        }
                    }
                break;
                case "mirna":
                //Name, ID, Description
                //E.g. MI0000300: hsa-mir-223	MI0000300	
                    for (Integer i =0; i< Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS.length; i++){
                        if(Config.NODEFIELDADDITIONAL.containsKey(Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS[i])){
                            rootNodeNet.summary.put(Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS[i], results[i]);
                        }
                    }
                break;
            }
            rootNodeNet.writeFeatureSummary();
        }
    }*/
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

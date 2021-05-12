package it.uniroma2.signor.internal.conceptualmodel.logic.Nodes;


import org.cytoscape.model.*;
import it.uniroma2.signor.internal.conceptualmodel.logic.Element;
import it.uniroma2.signor.internal.conceptualmodel.logic.Edges.Edge;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.NetworkField;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.stream.Collectors;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.ConfigResources;
import it.uniroma2.signor.internal.ConfigPathway;
import it.uniroma2.signor.internal.conceptualmodel.structures.Table;
import java.io.BufferedReader;

import it.uniroma2.signor.internal.utils.HttpUtils;
import static java.util.stream.Collectors.toList;

public class Node implements Element {

    private final Network network;
    public final CyNode cyNode;
    public final CyRow nodeRow;
    private HashMap<String,String> summary= new HashMap<String,String>();

    public Node(final Network network, final CyNode cyNode) {
        this(network, cyNode, network.getCyNetwork().getRow(cyNode));
    }

    private Node(final Network network, final CyNode cyNode, CyRow nodeRow) {
        this.network = network;
        this.cyNode = cyNode;
        this.nodeRow = nodeRow;

        NodeField.NODEFIELD.forEach((k, v) ->
                    this.summary.put(k, nodeRow.get(Config.NAMESPACE, k, String.class)));  
    }   
    //complex SIGNOR-C144
    //chemical CHEBI:3441
    //fusion protein SIGNOR-FP3
    //proteinfamily	SIGNOR-PF16
    //smallmolecule CHEBI:17650
    //stimulus	SIGNOR-ST13
    //phenotype	SIGNOR-PH92
    // mirna	MI0000300
    //public HashMap<String,String> Summary(){  
    public void Summary(){  
        /*HashMap<String,String> basic_summary = new HashMap<String,String>();        
        Config.NODEFIELD.forEach((basic_node_key, basic_node_value) ->{
            basic_summary.put(basic_node_key , this.nodeRow.get(Config.NAMESPACE, basic_node_key, String.class)); });  */        
        //Now I must retrieve information from pathway
        //I need species to retrieve information from ws entityInfo
        String species = network.parameters.get(NetworkField.SPECIES).toString();
        String NodeID = this.summary.get(NodeField.ID);
        network.manager.utils.info("Searching pthw for "+NodeID);
        Integer position_of_id_in_line = Arrays.asList(ConfigPathway.HEADERPTH).indexOf(ConfigPathway.PTHIDA);
        Integer position_of_pthw_desc_in_line = Arrays.asList(ConfigPathway.HEADERPTH).indexOf(ConfigPathway.PATHWAYNAME);
        try {
            Table.buildAdditionalInfoForSummary(network.manager, network.getCyNetwork());
            BufferedReader br =  HttpUtils.getHTTPSignor(ConfigResources.PATHALLRELATIONSQUERY, network.manager);
            ArrayList<String> relation_pathways = HttpUtils.parseWSNoheader(br);
            String pathway_found_for_node = "";
            for(Iterator it = relation_pathways.iterator(); it.hasNext();){                
                String[] field = it.next().toString().split("\t");                      
                if (field[position_of_id_in_line].equals(NodeID)){
                    if (!pathway_found_for_node.contains(field[position_of_pthw_desc_in_line])){
                        pathway_found_for_node += " , "+field[position_of_pthw_desc_in_line];                        
                        network.manager.utils.info("FOUND PTHW "+field[1]);
                    }
                }
            }
            this.summary.put(NodeField.PATHWAYLISTADDINFO, pathway_found_for_node);     
            String typeOfNode = this.summary.get(NodeField.TYPE);
            String id = this.summary.get(NodeField.ID);
            //Node rootNodeNet = nodes.get(rootNode);            
//            ConfigResources.WSSearchoption.ENTITYINFOSEARCH
            br =  HttpUtils.getHTTPSignor(ConfigResources.WSSearchoption.ENTITYINFOSEARCH.
                                              queryFunction.apply(id, Config.SPECIESLIST.get(species)), network.manager);
            //ArrayList<String> packed_results = HttpUtils.parseWSNoheader(HttpUtils.getHTTPSignor(ConfigResources.ENTITYINFO+id, network.manager));
            ArrayList<String> packed_results = HttpUtils.parseWSNoheader(br);
            //I have the header maybe something like this: "name	gene_name	entity_db_id	function	entity_alias"
            String[] header = packed_results.get(0).split("\t");
            String[] results = packed_results.get(1).split("\t");
            for (Integer i =0; i< results.length; i++){
                summary.put(header[i], results[i]);
                nodeRow.set(Config.NAMESPACE, header[i].toUpperCase(), results[i]);
            }        
        }
        catch (Exception e){
            network.manager.utils.error("Found exception while loading Node Summary "+e.toString());
        }
        //return this.summary;
    }
    
    public HashMap<String,String> getSummary(){
        return this.summary;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node that = (Node) o;
        return cyNode.equals(that.cyNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cyNode);
    }

    @Override
    public String toString() {
        return summary.get(NodeField.ENTITY);
    }
    @Override
    public boolean isSelected() {
        return nodeRow.get(CyNetwork.SELECTED, Boolean.class);
    } 
    public Network getNetwork() {
        return this.network;
    }
}

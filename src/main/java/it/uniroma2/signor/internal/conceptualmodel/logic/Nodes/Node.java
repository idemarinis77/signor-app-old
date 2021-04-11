package it.uniroma2.signor.internal.conceptualmodel.logic.Nodes;


import org.cytoscape.model.*;
import it.uniroma2.signor.internal.conceptualmodel.logic.Element;
import it.uniroma2.signor.internal.conceptualmodel.logic.Edges.Edge;
/*import uk.ac.ebi.intact.app.internal.model.core.features.Feature;
import uk.ac.ebi.intact.app.internal.model.core.features.FeatureClassifier;
import uk.ac.ebi.intact.app.internal.model.core.identifiers.Identifier;
import uk.ac.ebi.intact.app.internal.model.core.identifiers.ontology.CVTerm;
import uk.ac.ebi.intact.app.internal.model.core.identifiers.ontology.OntologyIdentifier;
import uk.ac.ebi.intact.app.internal.model.core.identifiers.ontology.SourceOntology;*/
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.stream.Collectors;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.ConfigResources;
import java.io.BufferedReader;

import it.uniroma2.signor.internal.utils.HttpUtils;
import static java.util.stream.Collectors.toList;

//public class Node extends Interactor implements Comparable<Interactor>, Element {
public class Node implements Element {

    //private final WeakReference<Network> network;
    private final Network network;
    public final CyNode cyNode;
    public final CyRow nodeRow;


    public Node(final Network network, final CyNode cyNode) {
        this(network, cyNode, network.getCyNetwork().getRow(cyNode));
    }

    private Node(final Network network, final CyNode cyNode, CyRow nodeRow) {

        //this.network = new WeakReference<>(network);

        this.network = network;
        this.cyNode = cyNode;
        this.nodeRow = nodeRow;
        //Lo lascio per esempio this.mutated = MUTATED.getValue(nodeRow);
        
       /*List<String> nodeFeatures = FEATURES.getValue(nodeRow);
        if (nodeFeatures != null) {
            featureAcs.addAll(nodeFeatures.stream().filter(s -> !s.isBlank()).collect(toList()));
        }

        List<String> nodeIdentifiers = IDENTIFIERS.getValue(nodeRow);
        if (nodeIdentifiers != null) {
            identifierAcs.addAll(nodeIdentifiers.stream().filter(s -> !s.isBlank()).collect(toList()));
        }*/
    }
    
    public HashMap<String,String> Summary(){
        HashMap<String,String> basic_summary = new HashMap<String,String>();
        String NodeID = this.nodeRow.get(Config.NAMESPACE, "ID", String.class);
        Config.NODEFIELD.forEach((basic_node_key, basic_node_value) ->{
            basic_summary.put(basic_node_key , this.nodeRow.get(Config.NAMESPACE, basic_node_key, String.class)); });  
        //Now I must withdraw information from pathway
        network.manager.utils.info("Searching pthw for "+NodeID);
        Integer position_of_id_in_line = Arrays.asList(Config.HEADERPTH).indexOf("IDA");
        Integer position_of_pthw_desc_in_line = Arrays.asList(Config.HEADERPTH).indexOf("PATHWAY_NAME");
        try {
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
            basic_summary.put("PATHWAYLIST", pathway_found_for_node);            
        }
        catch (Exception e){
            network.manager.utils.error("Found exception while loading Node Summary "+e.toString());
        }
        return basic_summary;
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
        return cyNode.toString();
    }

    /*@Override
    public int compareTo(Interactor o) {
        if (name.isEmpty()) return Integer.MAX_VALUE;
        else if (o.name.isEmpty()) return Integer.MIN_VALUE;
        else return name.compareTo(o.name);
    }*/


    @Override
    public boolean isSelected() {
        return nodeRow.get(CyNetwork.SELECTED, Boolean.class);
    }

    /*public void updateMutationStatus() {
        mutated = getFeatures().stream().anyMatch(feature -> FeatureClassifier.mutation.contains(feature.type.id));
        MUTATED.setValue(nodeRow, mutated);
    }

    public Network getNetwork() {
        return Objects.requireNonNull(network.get());
    }*/
    public Network getNetwork() {
        return this.network;
    }
}

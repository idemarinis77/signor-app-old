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
import it.uniroma2.signor.internal.ConfigPathway;
import it.uniroma2.signor.internal.conceptualmodel.structures.Table;
import java.io.BufferedReader;

import it.uniroma2.signor.internal.utils.HttpUtils;
import static java.util.stream.Collectors.toList;

public class Node implements Element {

    private final Network network;
    public final CyNode cyNode;
    public final CyRow nodeRow;
    public HashMap<String,String> summary= new HashMap<String,String>();

    public Node(final Network network, final CyNode cyNode) {
        this(network, cyNode, network.getCyNetwork().getRow(cyNode));
    }

    private Node(final Network network, final CyNode cyNode, CyRow nodeRow) {
        this.network = network;
        this.cyNode = cyNode;
        this.nodeRow = nodeRow;

        Config.NODEFIELD.forEach((k, v) ->
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
    public HashMap<String,String> Summary(){        
        /*HashMap<String,String> basic_summary = new HashMap<String,String>();        
        Config.NODEFIELD.forEach((basic_node_key, basic_node_value) ->{
            basic_summary.put(basic_node_key , this.nodeRow.get(Config.NAMESPACE, basic_node_key, String.class)); });  */        
        //Now I must retrieve information from pathway
        //I need species to retrieve information from ws entityInfo
        String species = network.parameters.get("SPECIES").toString();
        String NodeID = this.summary.get(Config.NODEID);
        network.manager.utils.info("Searching pthw for "+NodeID);
        Integer position_of_id_in_line = Arrays.asList(ConfigPathway.HEADERPTH).indexOf(ConfigPathway.PTHIDA);
        Integer position_of_pthw_desc_in_line = Arrays.asList(ConfigPathway.HEADERPTH).indexOf(ConfigPathway.PTHNAME);
        try {
            Table.buildAdditionalInfoForSummary(network.manager);
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
            this.summary.put(Config.PATHWAYLISTADDINFO, pathway_found_for_node);     
            String typeOfNode = this.summary.get(Config.NODETYPE);
            String id = this.summary.get(Config.NODEID);
            //Node rootNodeNet = nodes.get(rootNode);            
            br =  HttpUtils.getHTTPSignor(ConfigResources.WSSearchoptionMAP.
                                              get("ENTITYINFOSEARCH").queryFunction.apply(id, Config.SPECIESLIST.get(species)), network.manager);
            //ArrayList<String> packed_results = HttpUtils.parseWSNoheader(HttpUtils.getHTTPSignor(ConfigResources.ENTITYINFO+id, network.manager));
            ArrayList<String> packed_results = HttpUtils.parseWSNoheader(br);
            //I have the header maybe something like this: "name	gene_name	entity_db_id	function	entity_alias"
            String[] results = packed_results.get(1).split("\t");

            switch(typeOfNode){
                case "complex":
                //Formed By
                //E.g. SIGNOR-C144: CDK5/CDK5R1	SIGNOR-C144		Q00535,Q15078	CPX-2201 
                    for (Integer i =0; i< Config.HEADER_ROOT_NODE_ADDINFO_COMPLEX.length; i++){
                        if(Config.NODEFIELDADDITIONAL.containsKey(Config.HEADER_ROOT_NODE_ADDINFO_COMPLEX[i])){
                            summary.put(Config.HEADER_ROOT_NODE_ADDINFO_COMPLEX[i], results[i]);
                            nodeRow.set(Config.NAMESPACE, Config.HEADER_ROOT_NODE_ADDINFO_COMPLEX[i], results[i]);
                        }
                    }
                break;        
                case "chemical":
                //Name, Synonyms, IUPAC, Formula
                //E.g. CHEBI:3441: carvedilol	(+-)-1-(Carbazol-4-yloxy)-3-((2-(o-methoxyphenoxy)ethyl)amino)-2-propanol....	CHEBI:3441		C24H26N2O4	1-(9H-carbazol-4-yloxy)-3-{[2-(2-methoxyphenoxy)ethyl]amino}propan-2-ol
                    for (Integer i =0; i< Config.HEADER_ROOT_NODE_ADDINFO_CHEMICAL.length; i++){
                        if(Config.NODEFIELDADDITIONAL.containsKey(Config.HEADER_ROOT_NODE_ADDINFO_CHEMICAL[i])){
                            summary.put(Config.HEADER_ROOT_NODE_ADDINFO_CHEMICAL[i], results[i]);
                            nodeRow.set(Config.NAMESPACE, Config.HEADER_ROOT_NODE_ADDINFO_CHEMICAL[i], results[i]);
                        }
                    }
                break; 
                case "smallmolecule":
                //Name, Synonyms, IUPAC, Formula
                //E.g. CHEBI:17650: cortisol	(11beta)-11,17,21-trihydroxypregn-4-ene-3,20-dione...	CHEBI:17650		C21H30O5	11beta,17,21-trihydroxypregn-4-ene-3,20-dione
                    for (Integer i =0; i< Config.HEADER_ROOT_NODE_ADDINFO_CHEMICAL.length; i++){
                        if(Config.NODEFIELDADDITIONAL.containsKey(Config.HEADER_ROOT_NODE_ADDINFO_CHEMICAL[i])){
                            summary.put(Config.HEADER_ROOT_NODE_ADDINFO_CHEMICAL[i], results[i]);
                            nodeRow.set(Config.NAMESPACE, Config.HEADER_ROOT_NODE_ADDINFO_CHEMICAL[i], results[i]);
                        }
                    }
                break; 
                case "fusion protein":
                //Name, Formed by, Description, Sequence
                //E. g. SIGNOR-FP3: CBFbeta-MYH11	SIGNOR-FP3	Inversion of chromosome 16,.....	MPRVVPDQRSKFENEEFFR.... P35749,Q13951
                    for (Integer i =0; i< Config.HEADER_ROOT_NODE_ADDINFO_FUSIONPROTEIN.length; i++){
                        if(Config.NODEFIELDADDITIONAL.containsKey(Config.HEADER_ROOT_NODE_ADDINFO_FUSIONPROTEIN[i])){
                            summary.put(Config.HEADER_ROOT_NODE_ADDINFO_FUSIONPROTEIN[i], results[i]);
                            nodeRow.set(Config.NAMESPACE, Config.HEADER_ROOT_NODE_ADDINFO_FUSIONPROTEIN[i], results[i]);
                        }
                    }
                break;   
                case "proteinfamily":
                //Name, Formed by
                //E.g. SIGNOR-PF16: p38	SIGNOR-PF16		P53778,O15264,Q16539,Q15759
                    for (Integer i =0; i< Config.HEADER_ROOT_NODE_ADDINFO_PROTEINFAMILY.length; i++){
                        if(Config.NODEFIELDADDITIONAL.containsKey(Config.HEADER_ROOT_NODE_ADDINFO_PROTEINFAMILY[i])){
                            summary.put(Config.HEADER_ROOT_NODE_ADDINFO_PROTEINFAMILY[i], results[i]);
                            nodeRow.set(Config.NAMESPACE, Config.HEADER_ROOT_NODE_ADDINFO_PROTEINFAMILY[i], results[i]);
                        }
                    }
                case "stimulus":
                //Description
                //E.g SIGNOR-ST13: Cell-Cell_contact	SIGNOR-ST13	Cell-Cell contact
                    for (Integer i =0; i< Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS.length; i++){
                        if(Config.NODEFIELDADDITIONAL.containsKey(Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS[i])){
                            summary.put(Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS[i], results[i]);
                            nodeRow.set(Config.NAMESPACE, Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS[i], results[i]);
                        }
                    }
                break;
                case "phenotype":
                //Name, ID, Description
                //E.g. SIGNOR-PH92: Degranulation	SIGNOR-PH92	The regulated exocytosis of secretory granules
                    for (Integer i =0; i< Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS.length; i++){
                        if(Config.NODEFIELDADDITIONAL.containsKey(Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS[i])){
                            summary.put(Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS[i], results[i]);
                            nodeRow.set(Config.NAMESPACE, Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS[i], results[i]);
                        }
                    }
                break;
                case "mirna":
                //Name, ID, Description
                //E.g. MI0000300: hsa-mir-223	MI0000300	
                    for (Integer i =0; i< Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS.length; i++){
                        if(Config.NODEFIELDADDITIONAL.containsKey(Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS[i])){
                            summary.put(Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS[i], results[i]);
                            nodeRow.set(Config.NAMESPACE, Config.HEADER_ROOT_NODE_ADDINFO_STIMULUS[i], results[i]);
                        }
                    }
                break;
            }          
        }
        catch (Exception e){
            network.manager.utils.error("Found exception while loading Node Summary "+e.toString());
        }
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
        return cyNode.toString();
    }
    @Override
    public boolean isSelected() {
        return nodeRow.get(CyNetwork.SELECTED, Boolean.class);
    } 
    public Network getNetwork() {
        return this.network;
    }
}

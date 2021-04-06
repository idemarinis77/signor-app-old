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

import static java.util.stream.Collectors.toList;

//public class Node extends Interactor implements Comparable<Interactor>, Element {
public class Node implements Element {

    //private final WeakReference<Network> network;
    private final Network network;
    public final CyNode cyNode;
    public final CyRow nodeRow;
    private Config CONFIG = new Config();


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
    
    public HashMap<String,String> makeStyleInfoNode(Long suid){
        CyRow rownode = this.network.getCyNetwork().getDefaultNodeTable().getRow(suid);
        HashMap<String, String> parameters = new HashMap(){
                {put("ID", suid.toString());
                 put("Name", rownode.get("ENTITY", String.class));
                 put("Database", rownode.get("DATABASE", String.class));
                }
        };      
        return parameters;
    }
    /*public List<Edge> getAdjacentEdges() {
        Network network = getNetwork();
        return network.getCyNetwork().getAdjacentEdgeList(cyNode, CyEdge.Type.ANY).stream().map(edge -> Edge.createEdge(network, edge)).collect(toList());
    }*/

    /*public List<Identifier> getIdentifiers() {
        CyTable identifiersTable = getNetwork().getIdentifiersTable();
        if (identifiersTable == null) return new ArrayList<>();
        return identifierAcs.stream().map(identifierAc -> new Identifier(identifiersTable.getRow(identifierAc))).collect(Collectors.toList());
        //Va inserita porzione di codice che elenca gli Identificativi
        
    }*/


    /*public List<Feature> getFeatures() {
        Network network = getNetwork();
        CyTable featuresTable = network.getFeaturesTable();
        if (featuresTable == null) return new ArrayList<>();
        Set<Long> adjacentEdgesSUID = network.getCyNetwork().getAdjacentEdgeList(cyNode, CyEdge.Type.ANY).stream().map(CyIdentifiable::getSUID).collect(Collectors.toSet());
        return featureAcs.stream()
                .map(featureAC -> new Feature(network, featuresTable.getRow(featureAC)))
                .filter(feature -> feature.isPresentIn(adjacentEdgesSUID))
                .collect(Collectors.toList());
        Al momento non serve
    }*/

    /*private JsonNode getDetailsJSON() {
        if (detailsJSON != null && !detailsJSON.isNull()) return detailsJSON;
        detailsJSON = HttpUtils.getJSON(INTACT_GRAPH_WS + "network/node/details/" + ac, new HashMap<>(), getNetwork().manager);
        return detailsJSON;
    }

    public List<Identifier> getCrossReferences() {
        List<Identifier> crossReferences = new ArrayList<>();
        JsonNode xrefs = getDetailsJSON().get("xrefs");
        if (xrefs == null) return crossReferences;
        for (JsonNode xref : xrefs) {
            JsonNode database = xref.get("database");
            String databaseName = database.get("shortName").textValue();
            OntologyIdentifier databaseIdentifier = new OntologyIdentifier(database.get("identifier").textValue());

            String identifier = xref.get("identifier").textValue();
            String qualifier = xref.get("qualifier").textValue();

            crossReferences.add(new Identifier(databaseName, databaseIdentifier, identifier, qualifier));
        }
        return crossReferences;
    }

    public JsonNode getAliasesJson() {
        return getDetailsJSON().get("aliases");
    }*/

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

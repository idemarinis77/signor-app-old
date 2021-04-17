package it.uniroma2.signor.internal.conceptualmodel.logic.Edges;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import it.uniroma2.signor.internal.conceptualmodel.logic.Element;
import it.uniroma2.signor.internal.conceptualmodel.logic.Nodes.Node;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
//import it.uniroma2.signor.internal.conceptualmodel.structures.EdgeFields;

import java.lang.ref.WeakReference;
import java.util.*;



public class Edge implements Element {
    private final WeakReference<Network> network;
    public final CyEdge cyEdge;
    public final String name;
    public final CyRow edgeRow;
    //public final Node source;
    //public final Node target;
    //public final double score;


    public static Edge createEdge(Network network, CyEdge edge) {
        if (network == null || edge == null) return null;
        CyRow edgeRow = network.getCyNetwork().getRow(edge);
        if (edgeRow == null){
            System.out.println("ARCO NULLO");
            return null;
        }
        /*Boolean isSummary = EdgeFields.IS_SUMMARY.getValue(edgeRow);
        if (isSummary) {
            return new SummaryEdge(network, edge);
        } else {
            return new EvidenceEdge(network, edge);
        }*/
        return null;
    }


    public Edge(Network network, CyEdge cyEdge) {
        this.network = new WeakReference<>(network);
        this.cyEdge = cyEdge;
        edgeRow = network.getCyNetwork().getRow(cyEdge);

        name = edgeRow.get(CyNetwork.NAME, String.class);
        /*score = EdgeFields.SCORE.getValue(edgeRow);
        source = network.getNode(cyEdge.getSource());
        target = cyEdge.getTarget() != null ? network.getNode(cyEdge.getTarget()) : null;

        sourceFeatureAcs = FEATURES.SOURCE.getValue(edgeRow);
        if (sourceFeatureAcs != null) {
            sourceFeatureAcs.removeIf(String::isBlank);
        }
        targetFeatureAcs = FEATURES.TARGET.getValue(edgeRow);
        if (targetFeatureAcs != null) {
            targetFeatureAcs.removeIf(String::isBlank);
        }*/
    }

    //public abstract Map<Node, List<Feature>> getFeatures() ;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge that = (Edge) o;
        return cyEdge.equals(that.cyEdge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cyEdge);
    }

    @Override
    public String toString() {
        return cyEdge.toString();
    }

    @Override
    public boolean isSelected() {
        return edgeRow.get(CyNetwork.SELECTED, Boolean.class);
    }

    public Network getNetwork() {
        return Objects.requireNonNull(network.get());
    }
}

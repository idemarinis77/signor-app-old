package it.uniroma2.signor.app.internal.conceptualmodel.logic.Edges;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Element;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Nodes.Node;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.app.internal.Config;
import java.lang.ref.WeakReference;
import java.util.*;



public class Edge implements Element {
    private final WeakReference<Network> network;
    public final CyEdge cyEdge;
    public final String name;
    public final CyRow edgeRow;
    public final Node source;
    public final Node target;
    public final double score;
    private HashMap<String,String> summary= new HashMap<String,String>();

    public static Edge createEdge(Network network, CyEdge edge) {
        if (network == null || edge == null) return null;
        CyRow edgeRow = network.getCyNetwork().getRow(edge);
        if (edgeRow == null){
            return null;
        }        
        Edge new_edge = new Edge (network, edge);
        return new_edge;       
    }


    public Edge(Network network, CyEdge cyEdge) {
        this.network = new WeakReference<>(network);
        this.cyEdge = cyEdge;
        edgeRow = network.getCyNetwork().getRow(cyEdge);

        name = edgeRow.get(CyNetwork.NAME, String.class);
        source = network.getNodes().get(cyEdge.getSource());
        target = network.getNodes().get(cyEdge.getTarget());
        
        score = ((edgeRow.get(Config.NAMESPACE, "SCORE", Double.class) == null) ? 0.0 : edgeRow.get(Config.NAMESPACE, "SCORE", Double.class)); 
        
        EdgeField.EDGESUMMARY.forEach((k, v) ->
                    this.summary.put(k, edgeRow.get(Config.NAMESPACE, k, String.class)));  
       
    }

    public HashMap<String,String> getSummary(){
        return this.summary;
    }
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.managers;
import java.util.HashMap;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.*;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.conceptualmodel.structures.Table;
import it.uniroma2.signor.internal.view.NetworkView;
import it.uniroma2.signor.internal.utils.DataUtils;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.model.events.NetworkAddedEvent;
import org.cytoscape.model.events.NetworkAddedListener;
import org.cytoscape.view.model.events.NetworkViewAddedEvent;
import org.cytoscape.view.model.events.NetworkViewAddedListener;
import java.util.*;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedEvent;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedListener;
import org.cytoscape.model.subnetwork.CySubNetwork;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.view.model.events.NetworkViewAboutToBeDestroyedEvent;
import org.cytoscape.view.model.events.NetworkViewAboutToBeDestroyedListener;

public class PresentationManager implements         
        NetworkAddedListener, NetworkViewAddedListener, 
        NetworkViewAboutToBeDestroyedListener, NetworkAboutToBeDestroyedListener{
    
    public HashMap<CyNetwork, Network> signorNetMap = new HashMap();
    public HashMap<Network, NetworkView.Type> signorViewMap = new HashMap();
    public HashMap<CyNetworkView, CyNetwork> signorCyNetworkViewMap = new HashMap();
    SignorManager manager;
    public HashMap<String, ?> parameters;
    public String searched_query;
    final CyRootNetworkManager rootNetworkManager;
    public PresentationManager(SignorManager manager) {        
        this.manager = manager;
        rootNetworkManager = manager.utils.getService(CyRootNetworkManager.class);
        manager.utils.registerAllServices(this, new Properties());
    }
    
    public void updateSignorNetworkCreated(CyNetwork cynet, Network netw){
        if(signorNetMap != null){
           signorNetMap.put(cynet, netw);
        }
        else {
           signorNetMap = new HashMap(){
               { put(cynet, netw); }
           };  
        }
        manager.lastCyNetwork = cynet;
        manager.lastNetwork = netw;
    }
    
    public void updateSignorViewCreated(Network netw, NetworkView.Type view_type){
        if(signorViewMap != null){
           signorViewMap.put(netw, view_type);
        }
        else {
           signorViewMap = new HashMap(){
               { put(netw, view_type); }
           };  
        }
    }    
    
    public void removeNetwork(Network network) {
        if (network == null) return;
        CyNetwork cyNetwork = network.getCyNetwork();
        if (cyNetwork == null) return;
        signorNetMap.remove(cyNetwork);        
        signorViewMap.remove(network);
        CyTableManager tableManager = manager.utils.getService(CyTableManager.class);
        CyTable ptmNodeTable = network.PTMnodeTable;
        if (ptmNodeTable != null) tableManager.deleteTable(ptmNodeTable.getSUID());
        CyTable ptmEdgeTable = network.PTMedgeTable;
        if (ptmEdgeTable != null) tableManager.deleteTable(ptmEdgeTable.getSUID());
    }

    
    @Override
    public void handleEvent(NetworkViewAboutToBeDestroyedEvent e) {
        signorViewMap.remove(e.getNetworkView());
    }
    
    @Override
    public void handleEvent(NetworkAboutToBeDestroyedEvent e) {
        CyNetwork cyNetwork = e.getNetwork();

        CyTableManager tableManager = manager.utils.getService(CyTableManager.class);
        Network network = signorNetMap.get(cyNetwork);
        if (network != null && DataUtils.isSignorNetwork(cyNetwork)) {
            CyTable ptmNode = network.PTMnodeTable;
            if (ptmNode != null) tableManager.deleteTable(ptmNode.getSUID());
            CyTable ptmEdge = network.PTMedgeTable;
            if (ptmEdge != null) tableManager.deleteTable(ptmEdge.getSUID());
            signorNetMap.remove(cyNetwork);
        }
    }       
    
    @Override
    public void handleEvent (NetworkViewAddedEvent e){
        if(manager.sessionLoaderManager.loadingsession.equals(false)){
            CyNetworkView cyNetworkView = e.getNetworkView();
            CyNetwork cyNetwork = cyNetworkView.getModel();
            try {
                if (signorNetMap.containsKey(cyNetwork)) {
                    if(signorCyNetworkViewMap != null){
                       signorCyNetworkViewMap.put(cyNetworkView, cyNetwork);
                    }                
                    else {
                       signorCyNetworkViewMap = new HashMap(){
                           { put(cyNetworkView, cyNetwork); }
                       };  
                    }    
                    String query = (String) signorNetMap.get(cyNetwork).parameters.get(NetworkField.QUERY);
                    if(query == Config.INTERACTOMENAME || cyNetwork.getRow(cyNetwork).get(CyNetwork.NAME, String.class).contains(Config.INTERACTOMENAME)){
                        //apply SIGNOR STYLE
                        List<CyNetwork> nets = new ArrayList<CyNetwork>();  
                        nets.add(cyNetwork);
                        manager.signorStyleManager.applyStyle(cyNetworkView);                        
                    }
                }
            }
            catch (Exception ex){
                manager.utils.error("Cannot create view for "+cyNetwork.toString()+" "+ex.toString());
            }
        }
    }
     
    @Override
    public void handleEvent(NetworkAddedEvent e) {
        try {
            CyNetwork cyNetwork = e.getNetwork();
            CySubNetwork newNetwork = (CySubNetwork) cyNetwork;
            // I don't want to make any action if this is the root Network
            if(newNetwork.getRootNetwork().getBaseNetwork() != cyNetwork && DataUtils.isSignorNetwork(cyNetwork)){
                CyNetwork parentCyNetwork = newNetwork.getRootNetwork().getBaseNetwork();
                Network parentNetwork = signorNetMap.get(parentCyNetwork);
                NetworkView.Type nettype = signorViewMap.get(parentNetwork);
                Network subnetwork = DataUtils.prepareSubnetwork(parentNetwork, cyNetwork);
                if (nettype.name().equals(NetworkView.Type.PTM.name()))
                    subnetwork.parameters.replace(NetworkField.ROOTNETWORKPTM, true);
                signorNetMap.put(cyNetwork, subnetwork);
                signorViewMap.put(subnetwork, nettype);
                Table PTMTableNode = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
                PTMTableNode.buildPTMTable(parentNetwork.manager, "PTMNode", cyNetwork);

                Table PTMTableEdge = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
                PTMTableEdge.buildPTMTable(parentNetwork.manager, "PTMEdge", cyNetwork); 
                subnetwork.writeSearchNetwork();
            }
        }
        catch (Exception ex){
            manager.utils.error("Network Added Event "+ex.toString());
        }
    }    
    
    public CyNetwork getCurrentCyNetwork() {
        return manager.utils.getService(CyApplicationManager.class).getCurrentNetwork();
    }
    
    public Network getCurrentNetwork() {
        return signorNetMap.get(getCurrentCyNetwork());
    }
}

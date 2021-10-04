/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.managers;
import it.uniroma2.signor.app.internal.Config;
import it.uniroma2.signor.app.internal.conceptualmodel.structures.Table;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.*;
import it.uniroma2.signor.app.internal.view.NetworkView;
import it.uniroma2.signor.app.internal.utils.DataUtils;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.model.events.NetworkAddedEvent;
import org.cytoscape.model.events.NetworkAddedListener;
import org.cytoscape.view.model.events.NetworkViewAddedEvent;
import org.cytoscape.view.model.events.NetworkViewAddedListener;
import java.util.*;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedEvent;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedListener;
import org.cytoscape.model.subnetwork.CySubNetwork;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
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
//        CyTableManager tableManager = manager.utils.getService(CyTableManager.class);
//        CyTable ptmNodeTable = network.PTMnodeTable;
//        if (ptmNodeTable != null) tableManager.deleteTable(ptmNodeTable.getSUID());
//        CyTable ptmEdgeTable = network.PTMedgeTable;
//        if (ptmEdgeTable != null) tableManager.deleteTable(ptmEdgeTable.getSUID());
    }

    
    @Override
    public void handleEvent(NetworkViewAboutToBeDestroyedEvent e) {
        signorCyNetworkViewMap.remove(e.getNetworkView());
    }
    
    @Override
    public void handleEvent(NetworkAboutToBeDestroyedEvent e) {
        CyNetwork cyNetwork = e.getNetwork();

        CyTableManager tableManager = manager.utils.getService(CyTableManager.class);
        Network network = signorNetMap.get(cyNetwork);
        if (network != null && DataUtils.isSignorNetwork(cyNetwork)) {
//            CyTable ptmNode = network.PTMnodeTable;
//            if (ptmNode != null) tableManager.deleteTable(ptmNode.getSUID());
//            CyTable ptmEdge = network.PTMedgeTable;
//            if (ptmEdge != null) tableManager.deleteTable(ptmEdge.getSUID());
            signorNetMap.remove(cyNetwork);
        }
    }       
    
    @Override
    public void handleEvent (NetworkViewAddedEvent e){
        if(manager.sessionLoaderManager.loadingsession.equals(false)){
            CyNetworkView cyNetworkView = e.getNetworkView();
            CyNetwork cyNetwork = cyNetworkView.getModel();
            
            try {
                Boolean view_exist = false;

                if (signorNetMap.containsKey(cyNetwork)) {
                    
                    if(signorCyNetworkViewMap != null){
                       if(signorCyNetworkViewMap.containsKey(cyNetworkView)) view_exist = true;
                       signorCyNetworkViewMap.put(cyNetworkView, cyNetwork);
                    }                
                    else {
                       signorCyNetworkViewMap = new HashMap(){
                           { put(cyNetworkView, cyNetwork); }
                       };  
                    }    
                    String query = (String) signorNetMap.get(cyNetwork).parameters.get(NetworkField.QUERY);
                    if(query.equals(Config.INTERACTOMENAME) || cyNetwork.getRow(cyNetwork).get(CyNetwork.NAME, String.class).contains(Config.INTERACTOMENAME)){
                        //apply SIGNOR STYLE
   
                        manager.signorStyleManager.applyStyle(cyNetworkView);                        
                    }
                    else if (DataUtils.isSignorNetwork(cyNetwork)){
                        //If you delete a view, this step is necessary to re-apply the style
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
                               
                //                Table PTMTableNode = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
//                PTMTableNode.buildPTMTable(parentNetwork.manager, "PTMNode", cyNetwork);

//                Table PTMTableEdge = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
//                PTMTableEdge.buildPTMTable(parentNetwork.manager, "PTMEdge", cyNetwork); 
                subnetwork.writeSearchNetwork();
            }
            //Maybe i'm cloning
            else if (DataUtils.isSignorNetwork(cyNetwork) && !signorNetMap.containsKey(cyNetwork) 
                    && manager.sessionLoaderManager.loadingsession.equals(false)){
                //I have to find the network cloned from

                CyRow netrow = cyNetwork.getDefaultNetworkTable().getRow(cyNetwork.getSUID());
                HashMap <String, Object> params = NetworkSearch.buildParamsFromNetworkRecord(netrow, manager);
                Network signornet = new Network(manager, params);
                manager.presentationManager.updateSignorNetworkCreated(cyNetwork, signornet);
                String view_type = (String) params.get(NetworkField.VIEW);     
                if(view_type.equals(NetworkView.Type.DEFAULT.toString())){
                        manager.presentationManager.updateSignorViewCreated(signornet, NetworkView.Type.DEFAULT);
                }     
                else manager.presentationManager.updateSignorViewCreated(signornet, NetworkView.Type.PTM);
                String searched_query = (String) params.get(NetworkField.QUERY);
                if(!searched_query.equals(Config.INTERACTOMENAME)){
                    signornet.setNetwork(cyNetwork);    
                    signornet.setEntityNotFound((String) params.get(NetworkField.ENTITYNOTFOUND));
                }
                if((Boolean)params.get(NetworkField.SINGLESEARCH).equals(true))
                   signornet.setCyNodeRoot(searched_query);
                if((Boolean)params.get(NetworkField.PTMLOADED).equals(true) && !searched_query.equals(Config.INTERACTOMENAME))
                    DataUtils.loadPTMInfoFromSession(signornet, cyNetwork);
                if(!"".equals((String) params.get(NetworkField.PATHWAYINFO))){
//                       signornet.isPathwayNetwork = true;
                   String pathway_info_from_db = (String) params.get(NetworkField.PATHWAYINFO);
                   String[] pathway_field = pathway_info_from_db.split(",");
                   ArrayList<String> pathway_info = new ArrayList<String>();
                   for (int i = 0; i<pathway_field.length; i++) {
                       pathway_info.add(pathway_field[i]);
                   }
                   signornet.SetPathwayInfo(pathway_info);
                }                
            }
        }
        catch (Exception ex){
            manager.utils.error("Cannot add Network "+ex.toString());
        }
    }    
    
    public CyNetwork getCurrentCyNetwork() {
        return manager.utils.getService(CyApplicationManager.class).getCurrentNetwork();
    }
    
    public Network getCurrentNetwork() {
        return signorNetMap.get(getCurrentCyNetwork());
    }
}

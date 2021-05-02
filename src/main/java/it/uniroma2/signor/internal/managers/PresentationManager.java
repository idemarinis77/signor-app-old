/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.managers;
import java.util.HashMap;

import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.event.*;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.model.events.NetworkAddedEvent;
import org.cytoscape.model.events.NetworkAddedListener;
import org.cytoscape.session.events.SessionLoadedEvent;
import org.cytoscape.view.model.events.NetworkViewAddedEvent;
import org.cytoscape.view.model.events.NetworkViewAddedListener;
import org.cytoscape.session.events.SessionLoadedEvent;
import org.cytoscape.session.events.SessionLoadedListener;
import java.util.Properties;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.view.model.CyNetworkViewManager;
/**
 *
 * @author amministratore
 */
public class PresentationManager implements         
        SessionLoadedListener,
        SignorNetworkCreatedListener {
    
    public HashMap<CyNetwork, Network> signorNetMap;
    public HashMap<CyNetworkView, Network> signorViewMap;
    SignorManager manager;
    public HashMap<String, ?> parameters;
    public String searched_query;
    
    public PresentationManager(SignorManager manager) {        
        this.manager = manager;
        manager.utils.registerAllServices(this, new Properties());
    }
    
    public void updateSignorNetworkCreated(CyNetwork cynet, Network netw){
        if(signorNetMap != null){
           signorNetMap.put(cynet, netw);
           manager.utils.info("Ho aggiunto la rete alla netMap "+signorNetMap.toString());
        }
        else {
           signorNetMap = new HashMap(){
               { put(cynet, netw); }
           };  
           manager.utils.info("Ho aggiunto la rete alla netMap "+signorNetMap.toString());
        }
        manager.lastCyNetwork = cynet;
        manager.lastNetwork = netw;
    }
    
    public void updateSignorViewCreated(CyNetworkView cyview, Network netw){
        if(signorNetMap != null){
           signorViewMap.put(cyview, netw);
           manager.utils.info("Ho aggiunto la rete alla netViewMap "+signorViewMap.toString());
        }
        else {
           signorViewMap = new HashMap(){
               { put(cyview, netw); }
           };  
           manager.utils.info("Ho aggiunto la rete alla netViewMap "+signorViewMap.toString());
        }
//        manager.lastCyNetwork = cynet;
//        manager.lastNetwork = netw;
    }
    
    
    
    public void removeNetwork(Network network) {
        if (network == null) return;
        CyNetwork cyNetwork = network.getCyNetwork();
        if (cyNetwork == null) return;
        signorNetMap.remove(cyNetwork);        
    }
    public void handleEvent (SignorNetworkCreatedEvent e){
        manager.utils.info(e.getNewNetwork().toString());
        Network signornet = e.getNewNetwork();
        if (signornet.parameters.containsKey("SINGLESEARCH")){
            if (signornet.parameters.get("SINGLESEARCH").equals(true))
                signornet.setCyNodeRoot(searched_query);
        }
    }
    /*@Override
    public void handleEvent (NetworkAddedEvent e){
        manager.utils.info(e.getNetwork().toString()+"Network Aggiunta");
    }
    
    @Override
    public void handleEvent (NetworkViewAddedEvent e){
        manager.utils.info(e.getNetworkView().toString()+"Network View Aggiunta");
    }*/
    @Override
    public void handleEvent(SessionLoadedEvent e) {
        manager.utils.info(e.getLoadedSession().toString()+"Sessione View Aggiunta");              
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.managers;
import java.util.HashMap;

import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
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
/**
 *
 * @author amministratore
 */
public class PresentationManager implements 
        NetworkAddedListener, 
        NetworkViewAddedListener,
        SessionLoadedListener {
    HashMap<CyNetworkView, Network> signorNetMap;
    SignorManager manager;
    public PresentationManager(SignorManager manager) {        
        this.manager = manager;
        manager.utils.registerAllServices(this, new Properties());
    }
    @Override
    public void handleEvent (NetworkAddedEvent e){
        manager.utils.info(e.getNetwork().toString()+"Network Aggiunta");
    }
    
    @Override
    public void handleEvent (NetworkViewAddedEvent e){
        manager.utils.info(e.getNetworkView().toString()+"Network View Aggiunta");
    }
    @Override
    public void handleEvent(SessionLoadedEvent e) {
        manager.utils.info(e.getLoadedSession().toString()+"Sessione View Aggiunta");
        CyNetworkView cnv = manager.utils.getService(CyApplicationManager.class).getCurrentNetworkView();
        
    }
}

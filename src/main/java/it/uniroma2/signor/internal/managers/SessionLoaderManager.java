package it.uniroma2.signor.internal.managers;

import org.cytoscape.model.*;
import org.cytoscape.session.CySession;
import org.cytoscape.session.CySessionManager;
import org.cytoscape.session.events.SessionLoadedEvent;
import org.cytoscape.session.events.SessionLoadedListener;
import org.cytoscape.session.events.SessionAboutToBeSavedEvent;
import org.cytoscape.session.events.SessionAboutToBeSavedListener;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.vizmap.VisualStyle;
//import org.cytoscape.work.Tunable;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import java.util.HashMap;
import it.uniroma2.signor.internal.utils.DataUtils;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.*;
import it.uniroma2.signor.internal.view.NetworkView;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.event.SignorNetworkCreatedEvent;
import it.uniroma2.signor.internal.view.NetworkView;
        
public class SessionLoaderManager implements SessionLoadedListener, SessionAboutToBeSavedListener  {
    final SignorManager manager;
    public Boolean loadingsession = false;
    
    public SessionLoaderManager(SignorManager manager) {
        this.manager = manager;
    }
    
    @Override
    public void handleEvent(SessionAboutToBeSavedEvent event) {
        //I want to be sure to write information in the network record
//        @Tunable (description="choose file to save session")
        CySessionManager cysessionManager = manager.utils.getService(CySessionManager.class);
//        CySession cysession = cysessionManager.getCurrentSession();
        manager.utils.info("SessionAboutToBeSavedEvent "+cysessionManager.getCurrentSession().getNetworks().toString());
//        for (CyNetwork cynetwork : cysessionManager.getCurrentSession().getNetworks()) {
//        
//            manager.utils.info("SessionAboutToBeSavedEvent "+cynetwork.toString());
//            Network network = manager.presentationManager.signorNetMap.get(cynetwork);
//            
//            if(network != null && DataUtils.isSignorNetwork(cynetwork)){
//                network.writeSearchNetwork();
//                manager.utils.info("SessionAboutToBeSavedEvent "+network.toString());
//            }
//        }        
    }
    @Override
    public void handleEvent(SessionLoadedEvent event) {

        this.loadingsession = true;
        try {
            manager.presentationManager.signorNetMap.clear();
            manager.presentationManager.signorViewMap.clear();
        }
        catch (Exception e){
            manager.utils.error("SessionLoadedEvent problem clearing presentation maps "+e.toString());
        }
        
        CySession loadedSession = event.getLoadedSession();       

        Set<CyNetwork> cyNetworks = loadedSession.getNetworks();
        try {
            for (CyNetwork cyNetwork : cyNetworks) {
                if (DataUtils.isSignorNetwork(cyNetwork)) {
                    CyRow netrow = cyNetwork.getDefaultNetworkTable().getRow(cyNetwork.getSUID());
                    HashMap <String, Object> params = NetworkSearch.buildParamsFromNetworkRecord(netrow, manager);
                    Network signornet = new Network(manager, params);
                    manager.presentationManager.updateSignorNetworkCreated(cyNetwork, signornet);
                    String view_type = (String) params.get(NetworkField.VIEW);
                    if(view_type == NetworkView.Type.DEFAULT.name()){
                        manager.presentationManager.updateSignorViewCreated(signornet, NetworkView.Type.DEFAULT);
                    }
                    else manager.presentationManager.updateSignorViewCreated(signornet, NetworkView.Type.PTM);
                    String searched_query = (String) params.get(NetworkField.QUERY);

                    Collection<CyTableMetadata> tables = loadedSession.getTables();
                    linkPTMTableToNewtork(tables, signornet);
                    manager.utils.info("reloading network "+cyNetwork.toString()+params.toString());
                    signornet.setNetwork(cyNetwork);
                    if((Boolean)params.get(NetworkField.SINGLESEARCH).equals(true) )
                       signornet.setCyNodeRoot(searched_query);
                    if((Boolean)params.get(NetworkField.PTMLOADED).equals(true) )
                        DataUtils.loadPTMfromTable(signornet, cyNetwork);
                    manager.utils.showResultsPanel(); 
                    manager.utils.fireEvent(new SignorNetworkCreatedEvent(manager, signornet)); 
                }
            }
            this.loadingsession = false;
        }
        catch (Exception e){
            this.loadingsession = false;
            manager.utils.error("LoadingSession SessionManager "+e.toString()+cyNetworks.toString());
        }
    }
    
    public void linkPTMTableToNewtork(Collection<CyTableMetadata> tables, Network signornet){
        
        try {
            Set<CyNetwork> setCynet = manager.presentationManager.signorNetMap.keySet();
            for (CyTableMetadata tableM : tables) {
                CyTable table = tableM.getTable();
                String title = table.getTitle();            
                String title2 = title.replaceFirst(" - PTM....", "");
                for (CyNetwork cynet : setCynet) {  
                    if(cynet.getDefaultNetworkTable().getRow(cynet.getSUID()).get("name", String.class).equals(title2)){
    //                   Network ntw = manager.presentationManager.signorNetMap.get(cynet);
                       if(title.endsWith("PTMEdge")){
                           signornet.PTMedgeTable=table;
                           manager.utils.info(signornet.PTMedgeTable.toString()+"** e rete **"+signornet.toString());
                       }
                       if(title.endsWith("PTMNode")){
                           signornet.PTMnodeTable=table;
                           manager.utils.info(signornet.PTMnodeTable.toString()+"** e rete **"+signornet.toString());
                       }                   
                    }                
                }
            }    
        }
        catch(Exception e){
            manager.utils.error("linkPTMTableToNework "+e.toString());
        }
    }    
}

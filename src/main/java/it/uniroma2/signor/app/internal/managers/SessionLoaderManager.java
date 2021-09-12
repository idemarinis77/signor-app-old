package it.uniroma2.signor.app.internal.managers;

import org.cytoscape.model.*;
import org.cytoscape.session.CySession;
import org.cytoscape.session.events.SessionLoadedEvent;
import org.cytoscape.session.events.SessionLoadedListener;
import java.util.Collection;
import java.util.Set;
import java.util.HashMap;
import it.uniroma2.signor.app.internal.utils.DataUtils;
import it.uniroma2.signor.app.internal.Config;
import it.uniroma2.signor.app.internal.event.SignorNetworkCreatedEvent;
import it.uniroma2.signor.app.internal.view.NetworkView;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.*;
import java.util.ArrayList;
        
public class SessionLoaderManager implements SessionLoadedListener  {
    final SignorManager manager;
    public Boolean loadingsession = false;
    
    public SessionLoaderManager(SignorManager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(SessionLoadedEvent event) {

        this.loadingsession = true;
        manager.presentationManager.signorNetMap.clear();
        manager.presentationManager.signorViewMap.clear();
        manager.presentationManager.signorCyNetworkViewMap.clear();
        
        CySession loadedSession = event.getLoadedSession();       
        manager.utils.info("LOADING SIGNOR SESSION");
        Set<CyNetwork> cyNetworks = loadedSession.getNetworks();
        try {
            for (CyNetwork cyNetwork : cyNetworks) {
                if (DataUtils.isSignorNetwork(cyNetwork)) {
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
                    Collection<CyTableMetadata> tables = loadedSession.getTables();
//                    linkPTMTableToNewtork(tables, signornet);
                    manager.utils.info("Reloading SIGNOR network "+cyNetwork.toString()+params.toString());
                    signornet.setCyNetwork(cyNetwork);
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
    
//    public void linkPTMTableToNewtork(Collection<CyTableMetadata> tables, Network signornet){
//        
//        try {
//            Set<CyNetwork> setCynet = manager.presentationManager.signorNetMap.keySet();
//            for (CyTableMetadata tableM : tables) {
//                CyTable table = tableM.getTable();
//                String title = table.getTitle();            
//                String title2 = title.replaceFirst(" - PTM....", "");
//                for (CyNetwork cynet : setCynet) {  
//                    if(cynet.getDefaultNetworkTable().getRow(cynet.getSUID()).get("name", String.class).equals(title2)){
//                       if(title.endsWith("PTMEdge")){
//                           signornet.PTMedgeTable=table;
//                           manager.utils.info("Linking tables "+signornet.PTMedgeTable.toString()+" to "+signornet.toString());
//                       }
//                       if(title.endsWith("PTMNode")){
//                           signornet.PTMnodeTable=table;
//                           manager.utils.info("Linking tables "+signornet.PTMnodeTable.toString()+" to "+signornet.toString());
//                       }                   
//                    }                
//                }
//            }    
//        }
//        catch(Exception e){
//            manager.utils.error("linkPTMTableToNework "+e.toString());
//        }
//    }    
}

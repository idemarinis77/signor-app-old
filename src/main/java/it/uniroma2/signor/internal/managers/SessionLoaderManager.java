package it.uniroma2.signor.internal.managers;

import org.cytoscape.model.*;
import org.cytoscape.session.CySession;
import org.cytoscape.session.events.SessionLoadedEvent;
import org.cytoscape.session.events.SessionLoadedListener;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.vizmap.VisualStyle;


import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

import it.uniroma2.signor.internal.utils.DataUtils;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.event.SignorNetworkCreatedEvent;
        
public class SessionLoaderManager implements SessionLoadedListener {
    final SignorManager manager;

    public SessionLoaderManager(SignorManager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(SessionLoadedEvent event) {
        /* Create signor networks for any networks loaded by signor-app
        manager.data.setLoadingSession(true);
        manager.data.networkMap.clear();
        manager.data.networkViewMap.clear();*/
        try {
            manager.presentationManager.signorNetMap.clear();
        }
        catch (Exception e){
            manager.utils.error("Problem clearing signorNetMap");
        }
        
        CySession loadedSession = event.getLoadedSession();       

        Set<CyNetwork> cyNetworks = loadedSession.getNetworks();
        try {
            for (CyNetwork cyNetwork : cyNetworks) {
                if (DataUtils.isSignorNetwork(cyNetwork)) {
                    //Distinguish type of network
                    String netname = cyNetwork.getDefaultNetworkTable().getRow(cyNetwork.getSUID()).get("name", String.class);
                    Network signornet = new Network(cyNetwork, netname, manager);
                    manager.presentationManager.updateSignorNetworkCreated(cyNetwork, signornet);
                    String searched_query = netname.substring(netname.indexOf(" - ")+3);
                    if (searched_query.indexOf(" - ") >0 ) 
                        searched_query = searched_query.substring(0, searched_query.indexOf(" - "));
                    manager.presentationManager.searched_query = searched_query;
                    Collection<CyTableMetadata> tables = loadedSession.getTables();
                    linkPTMTableToNewtork(tables);

                    //rebuildSignorTable();
                    /*manager.utils.info(cyNetwork.getDefaultEdgeTable().toString()+"TABELLE CARICATE");
                    manager.utils.info(cyNetwork.getEdgeList().toString()+"ARCHI CARICATI");*/
                    //manager.utils.showResultsPanel();
                    //manager.utils.fireEvent(new SignorNetworkCreatedEvent(manager, signornet));        


                    /*if (ModelUtils.ifHaveIntactNS(cyNetwork)) {
                        updateSUIDList(cyNetwork.getDefaultEdgeTable(), EdgeFields.SUMMARIZED_EDGES_SUID, CyEdge.class, loadedSession);
                        Network network = new Network(manager);
                        manager.data.addNetwork(network, cyNetwork);
                        network.completeMissingNodeColorsFromTables(true, () -> manager.data.networkViewMap.values().forEach(NetworkView::accordStyleToType));
                    }*/
                }
            }
        }
        catch (Exception e){
            manager.utils.error(e.toString());
        }
    }
    
    public void linkPTMTableToNewtork(Collection<CyTableMetadata> tables){
        Set<CyNetwork> setCynet = manager.presentationManager.signorNetMap.keySet();
        for (CyTableMetadata tableM : tables) {
            CyTable table = tableM.getTable();
            String title = table.getTitle();            
            String title2 = title.replaceFirst(" - PTM....", "");
            for (CyNetwork cynet : setCynet) {  
                if(cynet.getDefaultNetworkTable().getRow(cynet.getSUID()).get("name", String.class).equals(title2)){
                   Network ntw = manager.presentationManager.signorNetMap.get(cynet);
                   if(title.endsWith("PTMEdge")){
                       ntw.PTMedgeTable=table;
                       manager.utils.info(ntw.PTMedgeTable.toString()+"** e rete **"+ntw.toString());
                   }
                   if(title.endsWith("PTMNode")){
                       ntw.PTMnodeTable=table;
                   }
                   
                }                
            }
        }        
    }
    /*    linkIntactTablesToNetwork(loadedSession.getTables(), loadedSession);
        for (CyNetworkView view : loadedSession.getNetworkViews()) {
            if (manager.data.getNetwork(view.getModel()) != null) {
                manager.data.addNetworkView(view, true);
            }
        }


        NetworkView currentView = manager.data.getCurrentNetworkView();
        if (currentView != null) {
            manager.utils.fireEvent(new ViewUpdatedEvent(manager, currentView));
            manager.utils.showResultsPanel();
        } else {
            manager.utils.hideResultsPanel();
        }

        manager.data.setLoadingSession(false);
    }

    void linkIntactTablesToNetwork(Collection<CyTableMetadata> tables, CySession loadingSession) {
        for (CyTableMetadata tableM : tables) {
            CyTable table = tableM.getTable();

            List<String> uuids = NetworkFields.UUID.getAllValues(table);
            if (uuids.isEmpty()) continue;

            if (Table.FEATURE.containsAllFields(table)) {
                updateSUIDList(table, FeatureFields.EDGES_SUID, CyEdge.class, loadingSession);
                for (Network network : manager.data.networkMap.values()) {
                    CyNetwork cyNetwork = network.getCyNetwork();
                    CyRow netRow = cyNetwork.getRow(cyNetwork);
                    if (NetworkFields.UUID.getValue(netRow).equals(uuids.get(0))) { // If the UUID referenced in defaultValue belong to this network
                        network.setFeaturesTable(table);
                    }
                }
            }

            if (Table.IDENTIFIER.containsAllFields(table)) {
                for (Network network : manager.data.networkMap.values()) {
                    CyNetwork cyNetwork = network.getCyNetwork();
                    if (NetworkFields.UUID.getValue(cyNetwork.getRow(cyNetwork)).equals(uuids.get(0))) { // If the UUID referenced in defaultValue belong to this network
                        network.setIdentifiersTable(table);
                    }
                }
            }
        }
    }

    private void updateSUIDList(CyTable sourceTable, ListField<Long> linkField, Class<? extends CyIdentifiable> targetType, CySession loadingSession) {
        for (CyRow row : sourceTable.getAllRows()) {
            List<Long> suids = linkField.getValue(row);
            if (!suids.isEmpty() && loadingSession.getObject(suids.get(0), targetType) == null) return;
            linkField.map(row, oldSUID -> {
                CyIdentifiable object = loadingSession.getObject(oldSUID, targetType);
                if (object != null) return object.getSUID();
                return null;
            });
        }
    }*/
}

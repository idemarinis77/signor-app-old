/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.task.query;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import it.uniroma2.signor.app.internal.conceptualmodel.structures.Table;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.*;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import it.uniroma2.signor.app.internal.task.query.factories.AlgorithmFactory;
import org.cytoscape.model.*;
import org.cytoscape.model.CyNetwork;
import it.uniroma2.signor.app.internal.Config;
import it.uniroma2.signor.app.internal.ConfigResources;
import it.uniroma2.signor.app.internal.event.SignorNetworkCreatedEvent;
import it.uniroma2.signor.app.internal.utils.HttpUtils;
import it.uniroma2.signor.app.internal.view.NetworkView;
import java.util.ArrayList;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.work.FinishStatus;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskObserver;

public class SignorPathwayResultTask extends AbstractTask implements TaskObserver{
        private final SignorManager manager;
        private final String pathwayid; 
        private final Network network;           
    
    public SignorPathwayResultTask(Network network, String pathwayid ){
        this.manager=network.manager;
        this.network = network;
        this.pathwayid = pathwayid;        
    }
    
  
    @Override
    public void run(TaskMonitor monitor) {
        monitor.setTitle("Creating Pathway Network"); 
        String URL = ConfigResources.WSSearchoptionMAP.get("PATHWAYSEARCH").queryFunction.apply(pathwayid, "only");
        try {                     
             
            monitor.setTitle("Querying SIGNOR Database");            
            monitor.showMessage(TaskMonitor.Level.INFO, "Fetching data from SIGNOR");   
            ArrayList<String> pathway_info = HttpUtils.parseWSNoheader(
                    HttpUtils.getHTTPSignor(ConfigResources.PATHSINGLEDESCRIPTIONSQUERY+network.parameters.get("PATHWAYID"), manager));
            String pathway_description = pathway_info.get(1).split("\t")[1];
            CyNetwork cynet = manager.createNetwork(Config.NTWPREFIX+pathway_description);
            manager.presentationManager.updateSignorNetworkCreated(cynet, network);
//            network.isPathwayNetwork = true;
            manager.presentationManager.updateSignorViewCreated(network, NetworkView.Type.DEFAULT);
            
            ArrayList<String> results = HttpUtils.parseWSNoheader(HttpUtils.getHTTPSignor(URL, manager));
            if (cancelled) return;
            
//            network.SetPathwayInfo(pathway_info);
            network.parameters.replace(NetworkField.PATHWAYINFO, pathway_info.toString());
            Table PthTable = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
            PthTable.buildPTHTable(manager, cynet);
            
            //Populate tables and create MyNetwork
            CyNetworkManager netMan = manager.utils.getService(CyNetworkManager.class);
            cynet = manager.createPathwayFromLine(results, cynet);

            network.setNetwork(cynet);             
            
            netMan.addNetwork(cynet);   
            CyNetworkViewFactory cnvf = manager.utils.getService(CyNetworkViewFactory.class);            
            CyNetworkView ntwView = cnvf.createNetworkView(cynet);  
            if (cancelled) return;
            //Apply style
            manager.signorStyleManager.applyStyle(ntwView);
            manager.signorStyleManager.installView(ntwView);            
           
//            Table PTMTableNode = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
//            PTMTableNode.buildPTMTable(manager, "PTMNode", cynet);                
//            Table PTMTableEdge = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
//            PTMTableEdge.buildPTMTable(manager, "PTMEdge", cynet);
            
            Table NetworkTable = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
            NetworkTable.buildDefaultTable(manager, "Network", cynet);           

            network.writeSearchNetwork();
            if (cancelled) {
                    manager.utils.getService(CyNetworkManager.class).destroyNetwork(cynet);
                }

                if (cancelled) {
                    destroyNetwork(manager, network);
                    return;
                }
            AlgorithmFactory algfactory = new AlgorithmFactory(ntwView, manager);            
            manager.utils.execute(algfactory.createTaskIterator());          
            
            manager.utils.showResultsPanel();                
            manager.utils.fireEvent(new SignorNetworkCreatedEvent(manager, network));  
            
        }
        catch(Exception e){
            manager.utils.error(e.toString()+"Problem fectching data from SIGNOR "+URL);
        }
        
    }
        @ProvidesTitle
    public String getTitle() {
        return "Loading pathway interactions";
    }

    /**
     * Called by an <code>ObservableTask</code> when it is finished executing.
     *
     * @param task The task being observed
     */
    @Override
    public void taskFinished(ObservableTask task) {

    }
    private void destroyNetwork(SignorManager manager, Network network) {
        CyNetwork cyNetwork = network.getCyNetwork();

        CyNetworkManager networkManager = manager.utils.getService(CyNetworkManager.class);
        CyTableManager tableManager = manager.utils.getService(CyTableManager.class);

        if (cyNetwork != null && networkManager.networkExists(cyNetwork.getSUID()))
            networkManager.destroyNetwork(cyNetwork);

//        tableManager.deleteTable(network.PTMedgeTable.getSUID());
//        tableManager.deleteTable(network.PTMnodeTable.getSUID());

        
        manager.presentationManager.removeNetwork(network);
    }
    /**
     * Called by a <code>TaskManager</code> to tell us that the task iterator has completed.
     *
     * @param finishStatus Indicates how the task iterator completed.
     */
    @Override
    public void allFinished(FinishStatus finishStatus) {
        System.out.println("All finished : ");
    }

    public boolean isReady() { return true; }
}

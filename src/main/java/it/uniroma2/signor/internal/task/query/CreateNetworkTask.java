/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.task.query;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.task.query.SignorPanelTask;
import it.uniroma2.signor.internal.task.query.factories.SignorPanelFactory;
import it.uniroma2.signor.internal.utils.HttpUtils;
import it.uniroma2.signor.internal.event.*;
import org.cytoscape.model.*;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.conceptualmodel.structures.Table;
import static org.cytoscape.model.CyTableFactory.InitialTableSize.MEDIUM;
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.model.View;
import org.cytoscape.work.FinishStatus;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskObserver;
import org.cytoscape.work.TunableSetter;

/**
 *
 * @author amministratore
 */
public class CreateNetworkTask extends AbstractTask implements TaskObserver{
    Network network;
    String terms;
    String URL;
    String netname;
    
    public CreateNetworkTask(Network network, String terms, String URL, String netname, HashMap<String, ?> parameters){
        super();
        this.network=network;
        this.terms=terms;
        this.URL=URL;
        this.netname=netname;
    }
    public void run(TaskMonitor monitor) {
        SignorManager manager = network.manager;
        Config CONFIG = new Config();     
        try {
            monitor.setTitle("Querying Signor Network");            
            monitor.showMessage(TaskMonitor.Level.INFO, "Fetching data from "+URL);
            CyNetwork cynet = manager.createNetwork(netname);
            BufferedReader br = HttpUtils.getHTTPSignor(URL, manager);
            ArrayList<String> results = HttpUtils.parseWS(br, CONFIG.HEADERSINGLESEARCH);
            
            //Create tables
            Table NodeTable = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
            NodeTable.buildDefaultTable(manager, "Node");
            Table EdgeTable = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
            EdgeTable.buildDefaultTable(manager, "Edge");
                    
            //Populate tables and create MyNetwork
            CyNetworkManager netMan = manager.utils.getService(CyNetworkManager.class);
            cynet = manager.createElementsFromLine(results);
            
            netMan.addNetwork(cynet);            
            CyNetworkViewFactory cnvf = manager.utils.getService(CyNetworkViewFactory.class);            
            CyNetworkView ntwView = cnvf.createNetworkView(cynet);            
            //Apply style
            manager.signorStyleManager.applyStyle(ntwView);
            manager.installView(ntwView);            
            
            network.setNetwork(cynet);
            manager.setCurrentNetwork(network);
            
           // if(manager.PTMtableTocreate.equals(true)){
                Table PTMTableNode = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
                PTMTableNode.buildPTMTable(manager, "PTMNode");
                
                Table PTMTableEdge = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
                PTMTableEdge.buildPTMTable(manager, "PTMEdge");
            //}
            
            CyLayoutAlgorithmManager layoutManager = manager.utils.getService(CyLayoutAlgorithmManager.class);
            CyLayoutAlgorithm alg = layoutManager.getLayout("force-directed-cl");
            if (alg == null) alg = layoutManager.getLayout("force-directed");
            Object context = alg.getDefaultLayoutContext();
            TunableSetter setter = manager.utils.getService(TunableSetter.class);
            Map<String, Object> layoutArgs = new HashMap<>();
            layoutArgs.put("defaultNodeMass", 10.0);
            setter.applyTunables(context, layoutArgs);
            Set<View<CyNode>> nodeViews = new HashSet<>(ntwView.getNodeViews());
            TaskIterator taskIterator = alg.createTaskIterator(ntwView, context, nodeViews, null);
            insertTasksAfterCurrentTask(taskIterator);
            manager.utils.showResultsPanel();
            manager.utils.fireEvent(new SignorNetworkCreatedEvent(manager, network));      
        }
        catch (Exception e){
            manager.utils.error(e.toString()+"Problem fectching data from "+URL);
        }
    }
     @ProvidesTitle
    public String getTitle() {
        return "Loading interactions";
    }

    /**
     * Called by an <code>ObservableTask</code> when it is finished executing.
     *
     * @param task The task being observed
     */
    @Override
    public void taskFinished(ObservableTask task) {

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

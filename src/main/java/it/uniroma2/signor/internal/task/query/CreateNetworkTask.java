/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.task.query;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.view.NetworkView;
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
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import it.uniroma2.signor.internal.task.view.factories.ZoomNetworkViewTaskFactory;
import it.uniroma2.signor.internal.task.view.ZoomNetworkViewTask;
import static org.cytoscape.view.presentation.property.BasicVisualLexicon.NETWORK_SCALE_FACTOR;
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
import org.cytoscape.view.model.*;

/**
 *
 * @author amministratore
 */
public class CreateNetworkTask extends AbstractTask implements TaskObserver{
    Network network;
    String terms;
    String URL;
    String netname;
    SignorManager manager;
    
    public CreateNetworkTask(Network network, String terms, String URL, String netname, HashMap<String, ?> parameters){
        super();
        this.network=network;
        this.terms=terms;
        this.URL=URL;
        this.netname=netname;
    }
    public void run(TaskMonitor monitor) {
        //SignorManager manager = network.manager;
        manager = network.manager;
        
        try {
            monitor.setTitle("Querying Signor Network");            
            monitor.showMessage(TaskMonitor.Level.INFO, "Fetching data from "+URL);
            if (cancelled) return;
            BufferedReader br = HttpUtils.getHTTPSignor(URL, manager);
            ArrayList<String> results = HttpUtils.parseWS(br, Config.HEADERSINGLESEARCH);
            String newterms = terms.replace("%2C", " ");
            if(results.isEmpty()){
                
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "No results for "+newterms,
                    "No results", JOptionPane.ERROR_MESSAGE));
                return;
            }
            if (results.get(0).equals("No result found.")) {
                //https://signor.uniroma2.it/getData.php?organism=9606&id=Q96Q05
                //after having choosen with multiple results of RAF
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "No results for "+newterms,
                    "No results", JOptionPane.ERROR_MESSAGE));
            }
            else {
                
                if (cancelled) return;
                CyNetwork cynet = manager.createNetwork(netname);
                manager.presentationManager.updateSignorNetworkCreated(cynet, network);
                manager.presentationManager.updateSignorViewCreated(network, NetworkView.Type.DEFAULT);

                //Create tables
                Table NodeTable = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
                NodeTable.buildDefaultTable(manager, "Node");
                Table EdgeTable = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
                EdgeTable.buildDefaultTable(manager, "Edge");

                //Populate tables and create MyNetwork
                CyNetworkManager netMan = manager.utils.getService(CyNetworkManager.class);
                cynet = manager.createNetworkFromLine(results);
                
                //Populate my logic netowrk
                network.setNetwork(cynet);
                if(network.parameters.get(Config.SINGLESEARCH).equals(true))
                    network.setCyNodeRoot(terms);
                
                netMan.addNetwork(cynet);            
                CyNetworkViewFactory cnvf = manager.utils.getService(CyNetworkViewFactory.class);            
                CyNetworkView ntwView = cnvf.createNetworkView(cynet);            
//                manager.presentationManager.updateSignorViewCreated(ntwView, network);
                //Apply style
                manager.signorStyleManager.applyStyle(ntwView);
                manager.signorStyleManager.installView(ntwView);            

//                network.setNetwork(cynet);
                manager.setCurrentNetwork(network);
                //manager.presentationManager.updateSignorNetworkCreated(cynet, network);

                Table PTMTableNode = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
                PTMTableNode.buildPTMTable(manager, "PTMNode");

                Table PTMTableEdge = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
                PTMTableEdge.buildPTMTable(manager, "PTMEdge");
                Table NetworkTable = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
                NetworkTable.buildDefaultTable(manager, "Network");           


                network.writeSearchNetwork();     
                if (cancelled) {
                    manager.utils.getService(CyNetworkManager.class).destroyNetwork(cynet);
                }

                if (cancelled) {
                    destroyNetwork(manager, network);
                    return;
                }
//                if(network.parameters.get(Config.SINGLESEARCH).equals(true))
//                    network.setCyNodeRoot(terms);                
                
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

//                double scale = 0.5;
//                double newScale = ntwView.getVisualProperty(NETWORK_SCALE_FACTOR).doubleValue() * scale;
//                ntwView.setVisualProperty(NETWORK_SCALE_FACTOR, newScale);
//                manager.signorStyleManager.installView(ntwView);
//                ZoomNetworkViewTaskFactory zvtf = new ZoomNetworkViewTaskFactory(0.5, manager);
//                manager.utils.execute(zvtf.createTaskIterator(ntwView));
//                
                
                
                manager.utils.showResultsPanel();            
                manager.utils.fireEvent(new SignorNetworkCreatedEvent(manager, network));        
            }
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
//        manager.utils.showResultsPanel();            
//        manager.utils.fireEvent(new SignorNetworkCreatedEvent(manager, network));        

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
    
    private void destroyNetwork(SignorManager manager, Network network) {
        CyNetwork cyNetwork = network.getCyNetwork();

        CyNetworkManager networkManager = manager.utils.getService(CyNetworkManager.class);
        CyTableManager tableManager = manager.utils.getService(CyTableManager.class);

        if (cyNetwork != null && networkManager.networkExists(cyNetwork.getSUID()))
            networkManager.destroyNetwork(cyNetwork);

        tableManager.deleteTable(network.PTMedgeTable.getSUID());
        tableManager.deleteTable(network.PTMnodeTable.getSUID());

        
        manager.presentationManager.removeNetwork(network);
    }
}

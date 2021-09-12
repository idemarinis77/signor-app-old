/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.task.query;
import it.uniroma2.signor.app.internal.Config;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.app.internal.ConfigResources;
import it.uniroma2.signor.app.internal.conceptualmodel.structures.Table;
import it.uniroma2.signor.app.internal.utils.HttpUtils;
import it.uniroma2.signor.app.internal.view.NetworkView;
import java.io.BufferedReader;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;


public class SignorInteractomeTask extends AbstractTask {
    SignorManager manager;
    Network network;
    String URL = ConfigResources.INTERACTOMEDWLD;
    Boolean ptm_interactome;
    public SignorInteractomeTask(Network network, Boolean ptm_interactome){
        this.manager = network.manager;
        this.network = network;
        this.ptm_interactome = ptm_interactome;
    }
    
    @Override
    public void run(TaskMonitor monitor) {        
        monitor.setTitle("Loading all SIGNOR Interactome ....  please wait");    
        try {

            monitor.showMessage(TaskMonitor.Level.INFO, "Fetching data from "+URL);
            if (cancelled) return;
            BufferedReader br = HttpUtils.getHTTPSignor(URL, manager);
            ArrayList<String> results = HttpUtils.parseWSNoheader(br);
            if(results.isEmpty()){
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "No results for Signor Interactome",
                    "No results", JOptionPane.ERROR_MESSAGE));
                return;
            }
            else {
                if (cancelled) return;

                String suffix = "";
                monitor.showMessage(TaskMonitor.Level.INFO, "Creating Interactome of "+results.size()+" interactors");
                
                if(ptm_interactome.equals(true)) suffix = " - PTM";
                CyNetwork cynet = manager.createNetwork(Config.NTWPREFIX+Config.INTERACTOMENAME+suffix);                
                manager.presentationManager.updateSignorNetworkCreated(cynet, network);
                manager.presentationManager.updateSignorViewCreated(network, NetworkView.Type.DEFAULT);
                monitor.showMessage(TaskMonitor.Level.INFO, "Created network Interactome "+network.toString());
                //Create tables
                Table NodeTable = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
                NodeTable.buildDefaultTable(manager, "Node", cynet);
                Table EdgeTable = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
                EdgeTable.buildDefaultTable(manager, "Edge",cynet);

                //Populate tables and create MyNetwork
                CyNetworkManager netMan = manager.utils.getService(CyNetworkManager.class);
                cynet = manager.createNetworkFromLine(results, ptm_interactome);
                network.setNetwork(cynet);                
                netMan.addNetwork(cynet);         
//                Table PTMTableNode = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
//                PTMTableNode.buildPTMTable(manager, "PTMNode", cynet);
//                Table PTMTableEdge = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
//                PTMTableEdge.buildPTMTable(manager, "PTMEdge", cynet);
                Table NetworkTable = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
                NetworkTable.buildDefaultTable(manager, "Network", cynet);   
                network.writeSearchNetwork();     
            }
        }
            catch (Exception e){
                manager.utils.error(e.toString()+"Problem fectching INTERACTOME data from "+URL);
            }
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
    
}

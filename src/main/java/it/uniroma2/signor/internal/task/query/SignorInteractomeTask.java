/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.task.query;

import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.ConfigResources;
import it.uniroma2.signor.internal.conceptualmodel.structures.Table;
import it.uniroma2.signor.internal.event.SignorNetworkCreatedEvent;
import it.uniroma2.signor.internal.utils.HttpUtils;
import it.uniroma2.signor.internal.view.NetworkView;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.URI;
import java.net.URL;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;

import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.View;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.TunableSetter;

/**
 *
 * @author amministratore
 */

public class SignorInteractomeTask extends AbstractTask {
    SignorManager manager;
    Network network;
    String URL = ConfigResources.INTERACTOMEDWLD;
    
    public SignorInteractomeTask(Network network){
        this.manager = network.manager;
        this.network = network;
    }
    public void run(TaskMonitor monitor) {        
        monitor.setTitle("Loading all Signor Interactome ....  please wait");    
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

//                InputStream inputStream = new URL(ConfigResources.INTERACTOMEDWLD).openStream();
//                Scanner sc = new Scanner(inputStream, "UTF-8");
//                int j =0;
//                while(sc.hasNext()) {
//                       String line = sc.nextLine();
//                       if(j<10)
//                            manager.utils.info(line);
//                       j++;
//                    }
//                inputStream.close();

                monitor.showMessage(TaskMonitor.Level.INFO, "Creating Interactome of "+results.size()+" interactors");
                CyNetwork cynet = manager.createNetwork(Config.NTWPREFIX+Config.INTERACTOMENAME);
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
                cynet = manager.createNetworkFromLine(results);
                network.setNetwork(cynet);
                
                netMan.addNetwork(cynet);            
//                CyNetworkViewFactory cnvf = manager.utils.getService(CyNetworkViewFactory.class);            
//                CyNetworkView ntwView = cnvf.createNetworkView(cynet);            
//
//                //Apply style
//                manager.signorStyleManager.applyStyle(ntwView);
//                manager.signorStyleManager.installView(ntwView);         
                
                
                Table PTMTableNode = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
                PTMTableNode.buildPTMTable(manager, "PTMNode", cynet);
                Table PTMTableEdge = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
                PTMTableEdge.buildPTMTable(manager, "PTMEdge", cynet);
                Table NetworkTable = new Table("SUID", true, true, CyTableFactory.InitialTableSize.MEDIUM);
                NetworkTable.buildDefaultTable(manager, "Network", cynet);        
//                //Populate my logic netowrk
//                network.setNetwork(cynet);
//                DAVERIFICARE COME FARE
                network.writeSearchNetwork();     
//                if (cancelled) {
//                    manager.utils.getService(CyNetworkManager.class).destroyNetwork(cynet);
//                }
//
//                if (cancelled) {
//                    destroyNetwork(manager, network);
//                    return;
//                }

                manager.utils.showResultsPanel();            
                manager.utils.fireEvent(new SignorNetworkCreatedEvent(manager, network));
            }
        }
            catch (Exception e){
                manager.utils.error(e.toString()+"Problem fectching data from "+URL);
            }
    }
    
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

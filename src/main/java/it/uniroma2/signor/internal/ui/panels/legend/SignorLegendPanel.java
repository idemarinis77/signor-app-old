package it.uniroma2.signor.internal.ui.panels.legend;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.*;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelComponent2;
import org.cytoscape.application.swing.CytoPanelName;

import java.awt.Color;

import java.awt.GridBagLayout;
import java.awt.GridLayout;

import it.uniroma2.signor.internal.utils.EasyGBC;
import it.uniroma2.signor.internal.utils.IconUtils;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.event.*;
import it.uniroma2.signor.internal.utils.TimeUtils;
import it.uniroma2.signor.internal.utils.DataUtils;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.NetworkField;

import it.uniroma2.signor.internal.view.NetworkView;
import it.uniroma2.signor.internal.task.query.SignorPanelTask;
import it.uniroma2.signor.internal.ConfigResources;
import it.uniroma2.signor.internal.task.query.factories.SignorPanelFactory;

import java.awt.BorderLayout;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.events.SelectedNodesAndEdgesEvent;
import org.cytoscape.model.events.SelectedNodesAndEdgesListener;
import org.cytoscape.model.CyNetwork;
import java.util.stream.Collectors;
import org.cytoscape.application.CyApplicationManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.cytoscape.application.events.SetCurrentNetworkEvent;
import org.cytoscape.application.events.SetCurrentNetworkListener;
import org.cytoscape.application.events.SetCurrentNetworkViewListener;
import org.cytoscape.application.events.SetCurrentNetworkViewEvent;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.model.events.NetworkAddedEvent;
import org.cytoscape.model.events.NetworkAddedListener;
import org.cytoscape.model.subnetwork.CySubNetwork;
import org.cytoscape.view.model.CyNetworkView;

public class SignorLegendPanel extends JPanel implements 
        CytoPanelComponent, 
        CytoPanelComponent2,
        SelectedNodesAndEdgesListener,
        SignorNetworkCreatedListener,         
        SetCurrentNetworkListener{       

    private final JTabbedPane tabs = new JTabbedPane(JTabbedPane.BOTTOM);
    private static final Icon icon = IconUtils.createImageIcon(ConfigResources.icon_path);
    private SignorNodePanel snp;
    private SignorEdgePanel sep;
    private SignorSummaryPanel ssp;
    private SignorRelationsPanel srp;
    private SignorModificationsPanel smp;
    private SignorDescriptionsPanel sdp;
    private SignorBridgePanel sbp;
    private SignorManager manager;
    private CyNetwork current_cynetwork_to_serch_into;
//    private boolean tabSingleSearchAdded = false;
//    private boolean tabPathwayAdded = false;
    JRadioButton ptmviewON= new JRadioButton("PTM View");
    JRadioButton defviewON = new JRadioButton("Default View");
    Boolean registered;

    public SignorLegendPanel(SignorManager manager) {

        this.manager = manager;      
        this.registered = true;

        snp = new SignorNodePanel(manager);
        sep = new SignorEdgePanel(manager);
        ssp = new SignorSummaryPanel(manager);
	srp = new SignorRelationsPanel(manager);
      	smp = new SignorModificationsPanel(manager);
        sdp = new SignorDescriptionsPanel(manager);
        sbp = new SignorBridgePanel(manager);
        ActionListener listenerPTM = new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { 
                defviewON.setEnabled(true);
                ptmviewON.setEnabled(false);
                if (manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into).parameters.get(NetworkField.QUERY) == Config.INTERACTOMENAME)
                    DataUtils.PopulatePTMTables(manager, true); 
                else if (manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into).parameters.get(NetworkField.QUERY) != Config.INTERACTOMENAME)
                    DataUtils.PopulatePTMTables(manager, false); 
            }
        };
        ActionListener listenerDEF = new ActionListener() {            
            @Override public void actionPerformed(ActionEvent e) { 
                ptmviewON.setEnabled(true);
                defviewON.setEnabled(false);
                if (manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into).parameters.get(NetworkField.QUERY) == Config.INTERACTOMENAME)
                    DataUtils.ShowDefaultView(manager, true);
                else if (manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into).parameters.get(NetworkField.QUERY) != Config.INTERACTOMENAME)
                    DataUtils.ShowDefaultView(manager, false);                                  
            }
                //da implementare il ritorno al default
        };       

        EasyGBC gbc=new EasyGBC();
        this.setLayout(new BorderLayout());

        JPanel ModeView = new JPanel(new GridLayout(1,2)); 
        ModeView.setBorder(BorderFactory.createTitledBorder("Mode View"));
        ButtonGroup group = new ButtonGroup();

        
        ptmviewON.addActionListener(listenerPTM);
        defviewON.addActionListener(listenerDEF);       
        
        group.add(ptmviewON);
        group.add(defviewON);
        ModeView.add(ptmviewON);
        ModeView.add(defviewON); 
        add(ModeView, BorderLayout.NORTH);               

//        tabs.add("Nodes", snp); 
//        tabs.add("Edges", sep);

        add(tabs, BorderLayout.CENTER);    
        manager.utils.registerAllServices(this, new Properties());
        manager.utils.setDetailPanel(this);
    }
    public Component getComponent() {		
        return this;
    }
    public CytoPanelName getCytoPanelName() {		
        return CytoPanelName.EAST;
    }
    public String getTitle() {		
        return "SIGNOR";	
    }
    public String getIdentifier() {
        return Config.identifier_panel;
    }
    public Icon getIcon() {		
        return icon;	
    }    
    
//    private Instant lastSelection = Instant.now();

    public void handleEvent(SelectedNodesAndEdgesEvent event) {        
//        if (Instant.now().minusMillis(200).isAfter(lastSelection)) {
//            if (snp.selectionRunning || sep.selectionRunning) {
//                snp.selectionRunning = false;
//                sep.selectionRunning = false;
//                TimeUtils.sleep(200);
//            }
//            lastSelection = Instant.now();
            Collection<CyNode> selectedNodes = event.getSelectedNodes();
            Collection<CyEdge> selectedEdges = event.getSelectedEdges();
            boolean nodesSelected = !selectedNodes.isEmpty();
            boolean edgesSelected = !selectedEdges.isEmpty();
            if (nodesSelected && edgesSelected) {
                tabs.setSelectedComponent(snp);
            } else if (nodesSelected && selectedEdges.isEmpty()) {
                tabs.setSelectedComponent(snp);
            } else if (selectedNodes.isEmpty() && edgesSelected) {
                tabs.setSelectedComponent(sep);
            }
            new Thread(() -> snp.selectedNodes()).start();
            new Thread(() -> sep.selectedEdges()).start();
    }      
  
    public void showCytoPanel() {
        CySwingApplication swingApplication = manager.utils.getService(CySwingApplication.class);
        CytoPanel cytoPanel = swingApplication.getCytoPanel(CytoPanelName.EAST);
        if (!registered) {
            manager.utils.registerService(this, CytoPanelComponent.class, new Properties());
        }
        if (cytoPanel.getState() == CytoPanelState.HIDE){
            cytoPanel.setState(CytoPanelState.DOCK);
        }
    }
    public void hideCytoPanel() {
        manager.utils.unregisterService(this, CytoPanelComponent.class);        
        registered = false;
    }
    
    @Override
    public void handleEvent(SignorNetworkCreatedEvent event){
        try {
            
            CyApplicationManager cyApplicationManager = manager.utils.getService(CyApplicationManager.class);
            CyNetwork newcynet = cyApplicationManager.getCurrentNetwork();
            for (CyNetwork k :  manager.presentationManager.signorNetMap.keySet()){
                if (manager.presentationManager.signorNetMap.get(k).equals(event.getNewNetwork())){
                    newcynet = k;
                    break;
                }
            }      
    
            if (event.getNewNetwork().isPathwayNetwork.equals(true)){
                this.current_cynetwork_to_serch_into = newcynet;
                snp.current_cynetwork_to_serch_into = newcynet;
                sep.current_cynetwork_to_serch_into = newcynet;
                sdp.current_cynetwork_to_serch_into = newcynet;    
                tabs.removeAll();
                tabs.add("NODES", snp); 
                tabs.add("EDGES", sep);
                tabs.add("DESCRIPTIONS", sdp);           

                sdp.recreateContent();
                tabs.setSelectedComponent(sdp); 
                manager.utils.info("New SIGNOR network PATHWAY "+newcynet); 

            }
            else if(event.getNewNetwork().parameters.get(NetworkField.SINGLESEARCH).equals(true)){  
                this.current_cynetwork_to_serch_into = newcynet;
                ssp.current_cynetwork_to_serch_into = newcynet;
                srp.current_cynetwork_to_serch_into = newcynet;
                smp.current_cynetwork_to_serch_into = newcynet;
                snp.current_cynetwork_to_serch_into = newcynet;
                sep.current_cynetwork_to_serch_into = newcynet;
                tabs.removeAll();
                tabs.add("NODES", snp); 
                tabs.add("EDGES", sep);                    
                tabs.add("SUMMARY", ssp);
                tabs.add("RELATIONS", srp);
                tabs.add("MODIFICATIONS", smp);
                ssp.recreateContent();
                srp.recreateContent();
                smp.recreateContent(); 
                tabs.setSelectedComponent(ssp);       
                manager.utils.info("New SIGNOR network SINGLE SEARCH"+newcynet);                    
            }
            else if(event.getNewNetwork().parameters.get(NetworkField.ALLSEARCH).equals(true) || 
               event.getNewNetwork().parameters.get(NetworkField.CONNECTSEARCH).equals(true)){                       
                tabs.removeAll();
                this.current_cynetwork_to_serch_into = newcynet;
                sbp.current_cynetwork_to_serch_into = newcynet; 
                sep.current_cynetwork_to_serch_into = newcynet;
                snp.current_cynetwork_to_serch_into = newcynet;   
                tabs.add("NODES", snp); 
                tabs.add("EDGES", sep); 
                tabs.add("SEARCHED ENTITIES", sbp);
                sbp.recreateContent();
                tabs.setSelectedComponent(sbp);
                manager.utils.info("New SIGNOR the rest of search"+newcynet); 
               
            }
            else if (event.getNewNetwork().parameters.get(NetworkField.QUERY) == Config.INTERACTOMENAME){
                this.current_cynetwork_to_serch_into = newcynet;
                tabs.removeAll();
            }
            if(manager.sessionLoaderManager.loadingsession.equals(true) && 
                    event.getNewNetwork().parameters.get(NetworkField.ROOTNETWORKPTM).equals(true)){
                ptmviewON.setSelected(true);
                ptmviewON.setEnabled(false);
                defviewON.setSelected(false);
                defviewON.setEnabled(false);
            }
            else {
                ptmviewON.setSelected(false);
                ptmviewON.setEnabled(true);
                defviewON.setSelected(true);
                defviewON.setEnabled(false);
            }
        }
        catch (Exception e){
            manager.utils.warn("Not SingleSearch query or not Signor Network Created, can't add tabs");
        }        
    }
    
    @Override
    public void handleEvent(SetCurrentNetworkEvent e) {       
        try {
            CyNetwork newcynet = e.getNetwork();
            if (newcynet != null && DataUtils.isSignorNetwork(e.getNetwork())){
//                hideCytoPanel();
//            }                    
//            else{
                showCytoPanel();
                if (newcynet != null && DataUtils.isSignorNetwork(newcynet)){
                    if (manager.presentationManager.signorNetMap.containsKey(newcynet)){                           
                          
//                          CyNetwork cyNetwork = e.getNetwork();
                           CySubNetwork subCynetowrk = (CySubNetwork) newcynet;
                           // If this is a subnetwork with ptm setted I disable the option to switch from Default and PTM
                           
                            {
                               Network network = manager.presentationManager.signorNetMap.get(newcynet);
                               NetworkView.Type netviewtype = manager.presentationManager.signorViewMap.get(network);
                               if (netviewtype == NetworkView.Type.DEFAULT) {
                                    ptmviewON.setSelected(false);
                                    ptmviewON.setEnabled(true);
                                    defviewON.setSelected(true);
                                    defviewON.setEnabled(false);
                               }
                               else if (netviewtype == NetworkView.Type.PTM){
                                    ptmviewON.setSelected(true);
                                    ptmviewON.setEnabled(false);
                                    defviewON.setSelected(false);
                                    defviewON.setEnabled(true);
                               }
                          }
                            if(subCynetowrk.getRootNetwork().getBaseNetwork() != newcynet){
//                                CyNetwork parentCyNetwork = newNetwork.getRootNetwork().getBaseNetwork();
//                                Network parentNetwork = manager.presentationManager.signorNetMap.get(parentCyNetwork);
                                manager.utils.info("Sono in set network "+newcynet.toString());
                                Network subNetwork = manager.presentationManager.signorNetMap.get(newcynet);
//                                if(subNetwork.isPTMNetwork()){
//                                    defviewON.setEnabled(false);
//                                    defviewON.setSelected(false);
//                                    ptmviewON.setSelected(true);
//                                    ptmviewON.setEnabled(false);                                    
//                                }
                                if(manager.presentationManager.signorViewMap.get(subNetwork)==NetworkView.Type.PTM){
                                    defviewON.setEnabled(false);
                                    defviewON.setSelected(false);
                                    ptmviewON.setSelected(true);
                                    ptmviewON.setEnabled(false);     
                                }
                                else {
                                    defviewON.setEnabled(false);
                                    defviewON.setSelected(true);
                                    ptmviewON.setSelected(false);
                                    ptmviewON.setEnabled(true); 
                                }
                            }
                          if (manager.presentationManager.signorNetMap.get(newcynet).parameters.get(NetworkField.QUERY) == Config.INTERACTOMENAME){
                              this.current_cynetwork_to_serch_into = newcynet;
                              tabs.removeAll();
                          }
                          else if (manager.presentationManager.signorNetMap.get(newcynet).isPathwayNetwork.equals(true)){
                                this.current_cynetwork_to_serch_into = newcynet;
                                snp.current_cynetwork_to_serch_into = newcynet;
                                sep.current_cynetwork_to_serch_into = newcynet; 
                                sdp.current_cynetwork_to_serch_into = newcynet;
                                tabs.removeAll();
                                tabs.add("NODES", snp); 
                                tabs.add("EDGES", sep);
                                tabs.add("DESCRIPTIONS", sdp);
                                sdp.recreateContent();
                                tabs.setSelectedComponent(sdp);
                                return;
                            }
                            else if (manager.presentationManager.signorNetMap.get(newcynet).parameters.get(NetworkField.SINGLESEARCH).equals(true)) {                                   
                                   this.current_cynetwork_to_serch_into = newcynet;
                                   snp.current_cynetwork_to_serch_into = newcynet;
                                   sep.current_cynetwork_to_serch_into = newcynet; 
                                   ssp.current_cynetwork_to_serch_into = newcynet;  
                                   srp.current_cynetwork_to_serch_into = newcynet;
                                   smp.current_cynetwork_to_serch_into = newcynet;  
                                   tabs.removeAll();                                   
                                   tabs.add("NODES", snp); 
                                   tabs.add("EDGES", sep);     
                                   tabs.add("SUMMARY", ssp);
                                   tabs.add("RELATIONS", srp);
                                   tabs.add("MODIFICATIONS", smp);
                                   ssp.recreateContent();
                                   srp.recreateContent();
                                   smp.recreateContent();
                                   tabs.setSelectedComponent(ssp);
                            }   
                            else if(manager.presentationManager.signorNetMap.get(newcynet).parameters.get(NetworkField.ALLSEARCH).equals(true) ||
                                manager.presentationManager.signorNetMap.get(newcynet).parameters.get(NetworkField.CONNECTSEARCH).equals(true)){
                                   tabs.removeAll();                               
                                   this.current_cynetwork_to_serch_into = newcynet;
                                   sbp.current_cynetwork_to_serch_into = newcynet;   
                                   sep.current_cynetwork_to_serch_into = newcynet;
                                   snp.current_cynetwork_to_serch_into = newcynet;   
                                   tabs.add("NODES", snp); 
                                   tabs.add("EDGES", sep); 
                                   tabs.add("SEARCHED ENTITIES", sbp);
                                   sbp.recreateContent();
                                   tabs.setSelectedComponent(sbp);
                            }
                            else if (manager.presentationManager.signorNetMap.get(newcynet).parameters.get(NetworkField.QUERY) == Config.INTERACTOMENAME){
                               this.current_cynetwork_to_serch_into = newcynet;
                            }
                        }                            
                    }
            }
            else {
               hideCytoPanel(); 
            }
        }
        catch(Exception err){
            manager.utils.error("SignorLegendPanel handleEvent(SetCurrentNetworkEvent) "+e.getNetwork().toString());
        }  
    }   
}
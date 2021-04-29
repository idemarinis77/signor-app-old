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
import it.uniroma2.signor.internal.task.query.SignorPanelTask;
import it.uniroma2.signor.internal.ConfigResources;

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
import org.cytoscape.view.model.CyNetworkView;

public class SignorLegendPanel extends JPanel implements 
        CytoPanelComponent, 
        CytoPanelComponent2,
        SelectedNodesAndEdgesListener,
        SignorNetworkCreatedListener,         
        SetCurrentNetworkListener {       

    private final JTabbedPane tabs = new JTabbedPane(JTabbedPane.BOTTOM);
    private static final Icon icon = IconUtils.createImageIcon(ConfigResources.icon_path);
    private SignorNodePanel snp;
    private SignorEdgePanel sep;
    private SignorSummaryPanel ssp;
    private SignorRelationsPanel srp;
    private SignorModificationsPanel smp;
    private SignorDescriptionsPanel sdp;
    private SignorManager manager;
//    private boolean tabSingleSearchAdded = false;
//    private boolean tabPathwayAdded = false;
    JRadioButton ptmviewON= new JRadioButton("PTM View");
    JRadioButton defviewON = new JRadioButton("Default View");

    public SignorLegendPanel(SignorManager manager) {

        this.manager = manager;        
        snp = new SignorNodePanel(manager);
        sep = new SignorEdgePanel(manager);
        ssp = new SignorSummaryPanel(manager);
	srp = new SignorRelationsPanel(manager);
      	smp = new SignorModificationsPanel(manager);
        sdp = new SignorDescriptionsPanel(manager);
        ActionListener listenerPTM = new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { 
                defviewON.setEnabled(true);
                ptmviewON.setEnabled(false);
                DataUtils.PopulatePTMTables(manager); }
        };
        ActionListener listenerDEF = new ActionListener() {            
            @Override public void actionPerformed(ActionEvent e) { 
                ptmviewON.setEnabled(true);
                defviewON.setEnabled(false);
                DataUtils.ShowDefaultView(manager);                 
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

        tabs.add("Nodes", snp); 
        tabs.add("Edges", sep);

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
    
    private Instant lastSelection = Instant.now();

    public void handleEvent(SelectedNodesAndEdgesEvent event) {        
        if (Instant.now().minusMillis(200).isAfter(lastSelection)) {

            if (snp.selectionRunning || sep.selectionRunning) {
                snp.selectionRunning = false;
                sep.selectionRunning = false;
                TimeUtils.sleep(200);
            }
            lastSelection = Instant.now();
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
    }      
  
    public void showCytoPanel() {
        CySwingApplication swingApplication = manager.utils.getService(CySwingApplication.class);
        CytoPanel cytoPanel = swingApplication.getCytoPanel(CytoPanelName.EAST);
        if (cytoPanel.indexOfComponent(Config.identifier_panel)<=0) {
            manager.utils.registerService(this, CytoPanelComponent.class, new Properties());
        }
        if (cytoPanel.getState() == CytoPanelState.HIDE)
            cytoPanel.setState(CytoPanelState.DOCK);

//        // Tell tabs
//        Network currentNetwork = manager.data.getCurrentNetwork();
//        if (currentNetwork != null) {
//            nodePanel.networkChanged(currentNetwork);
//            edgePanel.networkChanged(currentNetwork);
//            legendPanel.networkChanged(currentNetwork);
//        }

    }
    public void hideCytoPanel() {
        manager.utils.unregisterService(this, CytoPanelComponent.class);
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
            //if (DataUtils.isSignorNetwork(manager.lastCyNetwork) && this.manager.lastNetwork.parameters.get("SINGLESEARCH").equals(true)
            if (event.getNewNetwork().parameters.containsKey(Config.SINGLESEARCH)){
                if(event.getNewNetwork().parameters.get(Config.SINGLESEARCH).equals(true)){  
                    ssp.current_cynetwork_to_serch_into = newcynet;
                    srp.current_cynetwork_to_serch_into = newcynet;
                    smp.current_cynetwork_to_serch_into = newcynet;
                    snp.current_cynetwork_to_serch_into = newcynet;
                    sep.current_cynetwork_to_serch_into = newcynet;
                    tabs.removeAll();
                    tabs.add("Nodes", snp); 
                    tabs.add("Edges", sep);                    
                    tabs.add("SUMMARY", ssp);
                    tabs.add("RELATIONS", srp);
                    tabs.add("MODIFICATIONS", smp);
                    ssp.recreateContent();
                    srp.recreateContent();
                    smp.recreateContent(); 
                    tabs.setSelectedComponent(ssp);                           
                }
            }
            
            if (event.getNewNetwork().isPathwayNetwork){
                //sdp.current_cynetwork_to_serch_into=manager.presentationManager.event.getNewNetwork();
                snp.current_cynetwork_to_serch_into = newcynet;
                sep.current_cynetwork_to_serch_into = newcynet;
                sdp.current_cynetwork_to_serch_into = newcynet;    
                tabs.removeAll();
                tabs.add("Nodes", snp); 
                tabs.add("Edges", sep);
                tabs.add("DESCRIPTIONS", sdp);
//                tabs.remove(smp);
//                tabs.remove(ssp);
//                tabs.remove(srp);                

                sdp.recreateContent();
                tabs.setSelectedComponent(sdp); 
            }
            ptmviewON.setSelected(false);
            ptmviewON.setEnabled(true);
            defviewON.setSelected(true);
            defviewON.setEnabled(false);
            manager.utils.info("New SIGNOR network "+manager.lastCyNetwork);
        }
        catch (Exception e){
            manager.utils.warn("Not SingleSearch query or not Signor Network Created, can't add tabs");
        }        
    }
    
    @Override
    public void handleEvent(SetCurrentNetworkEvent e) {
        CyNetwork newcynet = e.getNetwork();

        try {
            if (newcynet != null && !DataUtils.isSignorNetwork(e.getNetwork())){
                hideCytoPanel();
            }                    
            else{
                showCytoPanel();
                manager.utils.info("SignorLegendPanel handleEvent(SetCurrentNetworkEvent) "+e.getNetwork().toString());                  
                if (newcynet != null && DataUtils.isSignorNetwork(newcynet)){                               
                   if (manager.presentationManager.signorNetMap.containsKey(newcynet)){
                       if (manager.presentationManager.signorNetMap.get(newcynet).isPathwayNetwork){
                           //if(!tabPathwayAdded){
                               
//                               tabPathwayAdded = true;
//                           }
                           snp.current_cynetwork_to_serch_into = newcynet;
                           sep.current_cynetwork_to_serch_into = newcynet; 
                           sdp.current_cynetwork_to_serch_into = newcynet;
                           tabs.removeAll();
                           tabs.add("Nodes", snp); 
                           tabs.add("Edges", sep);
                           tabs.add("DESCRIPTIONS", sdp);
                           sdp.recreateContent();
                           /*if(tabSingleSearchAdded){
                               tabs.remove(smp);
                               tabs.remove(ssp);
                               tabs.remove(srp);
                               tabSingleSearchAdded = false;
                           }*/                           
                      }
                   
                       else {
                           //if (manager.presentationManager.signorNetMap.get(newcynet).isSingleSearch()){
                           //if (!tabSingleSearchAdded) {                           
                           
                           snp.current_cynetwork_to_serch_into = newcynet;
                           sep.current_cynetwork_to_serch_into = newcynet; 
                           ssp.current_cynetwork_to_serch_into = newcynet;  
                           srp.current_cynetwork_to_serch_into = newcynet;
                           smp.current_cynetwork_to_serch_into = newcynet;
                           tabs.removeAll();
                           tabs.add("Nodes", snp); 
                           tabs.add("Edges", sep);
                           tabs.add("SUMMARY", ssp);
                           tabs.add("RELATIONS", srp);
                           tabs.add("MODIFICATIONS", smp);
                           tabs.setSelectedComponent(ssp);
                           ssp.recreateContent();
                           srp.recreateContent();
                           smp.recreateContent();
                       }
                   }
                }
            }
        }
        catch(Exception err){
            manager.utils.error("SignorLegendPanel handleEvent(SetCurrentNetworkEvent) "+e.getNetwork().toString()+" "+err.toString());
        }  
    }   
}
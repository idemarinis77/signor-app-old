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
import org.cytoscape.service.util.CyServiceRegistrar;
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
import it.uniroma2.signor.internal.task.query.SignorPanelTask;

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
        SetCurrentNetworkViewListener,
        SetCurrentNetworkListener {       

    private final JTabbedPane tabs = new JTabbedPane(JTabbedPane.BOTTOM);
    private static final Icon icon = IconUtils.createImageIcon("/images/signor_logo.png");
    private SignorNodePanel snp;
    private SignorEdgePanel sep;
    private SignorManager manager;
    private boolean tabSingleSearchAdded = false;
    JRadioButton ptmviewON= new JRadioButton("PTM View");
    JRadioButton defviewON = new JRadioButton("Default View");

    public SignorLegendPanel(SignorManager manager) {

        this.manager = manager;        
        snp = new SignorNodePanel(manager);
        sep = new SignorEdgePanel(manager);
	
        ActionListener listenerPTM = new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { 
                defviewON.setEnabled(true);
                ptmviewON.setEnabled(false);
                DataUtils.PopulatePTMTables(manager); }
        };
        ActionListener listenerDEF = new ActionListener() {            
            @Override public void actionPerformed(ActionEvent e) { 
                ptmviewON.setEnabled(true);
                defviewON.setEnabled(false); }
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
            if (DataUtils.isSignorNetwork(manager) && this.manager.lastNetwork.parameters.get("SINGLESEARCH").equals(true)
                && !tabSingleSearchAdded){            
                tabs.add("SUMMARY", new JPanel());
                tabs.add("RELATIONS", new JPanel());
                tabs.add("MODIFICATIONS", new JPanel());
                tabSingleSearchAdded = true;                
            }
            //ptmviewON.setEnabled(true);
            ptmviewON.setSelected(false);
            ptmviewON.setEnabled(true);
            defviewON.setSelected(true);
            defviewON.setEnabled(false);
        }
        catch (Exception e){
            manager.utils.warn("Not SingleSearch query or not Signor Network Created, can't add tabs");
        }        
    }
    
    @Override
    public void handleEvent(SetCurrentNetworkEvent e) {
        manager.utils.info("Network event "+e.getNetwork().toString());
        /*Network network = manager.data.getNetwork(event.getNetwork());
        if (network != null && ModelUtils.ifHaveIntactNS(network.getCyNetwork())) {
            if (!registered) {
                showCytoPanel();
            }
            // Tell tabs
            nodePanel.networkChanged(network);
            edgePanel.networkChanged(network);
            legendPanel.networkChanged(network);
        } else {
            hideCytoPanel();
        }*/
    }

    @Override
    public void handleEvent(SetCurrentNetworkViewEvent e) {
        manager.utils.info("Network view event "+e.getNetworkView().toString());
    /*    CyNetworkView cyView = e.getNetworkView();
        if (cyView != null) {
            updateRadioButtons(cyView);
            legendPanel.networkViewChanged(cyView);
            edgePanel.networkViewChanged(cyView);
            NetworkView view = manager.data.getNetworkView(cyView);
            if (view != null) {
                setupFilters(view);
            }
        }*/
    }

   
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.ui.panels.legend;
import it.uniroma2.signor.internal.conceptualmodel.logic.Edges.*;
import it.uniroma2.signor.internal.utils.EasyGBC;
import java.util.Iterator;
import java.awt.*;
import java.util.Collection;
import javax.swing.*;
import org.cytoscape.model.CyEdge;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.ConfigResources;
import static java.awt.Component.LEFT_ALIGNMENT;
import it.uniroma2.signor.internal.ui.components.SignorLabelMore;
import java.util.HashMap;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.util.swing.OpenBrowser;
import it.uniroma2.signor.internal.ui.components.SignorLabelStyledBold;

public class SignorEdgePanel extends JPanel {
    private final SignorManager manager;
    private final JPanel edgesPanel;
    private final EasyGBC gbc=new EasyGBC();
    private static Font iconFont;
    public Boolean selectionRunning= false;
    public CyNetwork current_cynetwork_to_serch_into;

    
    public SignorEdgePanel(SignorManager manager){
        this.manager = manager;
        setLayout(new GridBagLayout());
        current_cynetwork_to_serch_into = manager.lastCyNetwork;        
        JPanel edgeInfo = new JPanel();
        edgeInfo.setLayout(new BorderLayout());
        edgeInfo.setBackground(Color.WHITE);
        {
            edgesPanel = new JPanel();
            edgesPanel.setBackground(Color.WHITE);
            edgesPanel.setLayout(new GridBagLayout());
            edgeInfo.add(edgesPanel, BorderLayout.NORTH);
        }
        JScrollPane scrollPane = new JScrollPane(edgeInfo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.getVerticalScrollBar().setBlockIncrement(10);
        add(scrollPane, gbc.down().anchor("east").expandBoth());
        revalidate();
        repaint();        
    }   
    
    public void selectedEdges() {
        
        this.selectionRunning=true;
        Network network = manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into);
        Collection<CyEdge> selectedEdges = CyTableUtil.getEdgesInState(current_cynetwork_to_serch_into, CyNetwork.SELECTED, true);   
        if(selectedEdges.size()>0){
            edgesPanel.removeAll();
        }
        Iterator iter_sel_edges = selectedEdges.iterator();
        while(iter_sel_edges.hasNext()){
            JPanel edgeinfo = new JPanel();
            CyEdge edge_current = (CyEdge) iter_sel_edges.next();
            Edge edge = network.getEdges().get(edge_current);    
            if(edge!=null){
                HashMap <String,String> summary = edge.getSummary();         

                Iterator iter = summary.keySet().iterator();
                Iterator iterv = summary.values().iterator();

                edgeinfo.setLayout(new GridBagLayout());
                edgeinfo.add(new SignorLabelStyledBold("SOURCE - TARGET  "), gbc.down());
                edgeinfo.add(new JLabel(edge.source.toString()+"-"+edge.target.toString()), gbc.right());
                while(iter.hasNext()){
                    String key = iter.next().toString();
                    String value = iterv.next().toString();
                    if(key.equals(EdgeField.PMID)){
                        OpenBrowser openBrowser = manager.utils.getService(OpenBrowser.class);
                        String label = value;
                        String link_to_db = ConfigResources.DBLINKSMAP.get("pubmed").queryFunction.apply(value);
                        SignorLabelStyledBold link_to_pmid = new SignorLabelStyledBold(label, link_to_db, openBrowser, false);
                        SignorLabelStyledBold id = new SignorLabelStyledBold(key);
                        edgeinfo.add(id, gbc.down());
                        edgeinfo.add(link_to_pmid, gbc.right());
                    }
                    else{
                        SignorLabelStyledBold id = new SignorLabelStyledBold(key.replaceFirst("MODIFICATIONA", "MODIFICATION"));
                        edgeinfo.add(id, gbc.down());
                        if(value.length() > 20 && !key.equals(EdgeField.MECHANISM))
                              edgeinfo.add(new SignorLabelMore(manager, "[read]", value), gbc.right());
                        else edgeinfo.add(new JLabel(value), gbc.right());
                    }
                }    
                CollapsablePanel collapsableINFO = new CollapsablePanel(iconFont, "Edge INFO", edgeinfo, false );
                edgesPanel.add(collapsableINFO, gbc.down().anchor("north"));
            }
            //Is a PTM Edge
            else {
                edgeinfo.add(new SignorLabelStyledBold("INTERACTION"), gbc.down());
                edgeinfo.add(new JLabel(current_cynetwork_to_serch_into.getDefaultEdgeTable().getRow(edge_current.getSUID()).
                    get("name", String.class)), gbc.right());
                CollapsablePanel collapsableINFO = new CollapsablePanel(iconFont, "PTM Edge INFO", edgeinfo, false );
                edgesPanel.add(collapsableINFO, gbc.down().anchor("north").insets(1,1,1,1));
            }
        }              
    }
}

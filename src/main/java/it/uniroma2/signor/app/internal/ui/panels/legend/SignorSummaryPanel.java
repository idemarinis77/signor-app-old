/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.ui.panels.legend;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Nodes.Node;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Nodes.NodeField;
import it.uniroma2.signor.app.internal.utils.EasyGBC;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import it.uniroma2.signor.app.internal.Config;
import it.uniroma2.signor.app.internal.ConfigResources;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Pathway.PathwayField;
import it.uniroma2.signor.app.internal.ui.components.SignorButton;
import it.uniroma2.signor.app.internal.ui.components.SignorLabelStyledBold;
import it.uniroma2.signor.app.internal.task.query.factories.SignorPathwayQueryFactory;
import it.uniroma2.signor.app.internal.ui.components.SignorLabelMore;
import it.uniroma2.signor.app.internal.utils.HttpUtils;
import java.util.Iterator;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.io.BufferedReader;
import java.util.HashMap;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyNetwork;


public class SignorSummaryPanel extends JPanel {
    private final SignorManager manager;
    private final JPanel summPanel;
    private static Font iconFont;

    private final EasyGBC gbc=new EasyGBC();
    public Boolean selectionRunning= false;
    public CyNetwork current_cynetwork_to_serch_into;  
    private HashMap<String,String> summary;
    Node networkRootNode;
    
    public SignorSummaryPanel(SignorManager manager){
        setLayout(new GridBagLayout());
        this.manager = manager;
        current_cynetwork_to_serch_into = manager.lastCyNetwork; 
        
        JPanel SummaryInfo = new JPanel();
        SummaryInfo.setLayout(new GridBagLayout());

        SummaryInfo.setBackground(Color.WHITE);
        {
            EasyGBC gbc1=new EasyGBC();
            summPanel = new JPanel();
            summPanel.setBackground(Color.WHITE);
            SummaryInfo.add(summPanel, gbc1.down().anchor("north").expandHoriz());
            SummaryInfo.add(Box.createVerticalGlue(), gbc1.down().expandVert());           
        }
        JScrollPane scrollPane = new JScrollPane(SummaryInfo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.getVerticalScrollBar().setBlockIncrement(10);
        add(scrollPane, gbc.down().anchor("east").expandBoth());
        revalidate();
        repaint();   
    }
    
    public void createContent(){
        try {
            if(manager.presentationManager.signorNetMap.containsKey(current_cynetwork_to_serch_into)){
               networkRootNode = manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into).getNetworkRootNode();
            }
            summary = networkRootNode.getSummary();
            Iterator iter = summary.keySet().iterator();
            Iterator iterv = summary.values().iterator();
            Integer it =0;
            summPanel.setLayout(new GridBagLayout());
            JPanel listpath = new JPanel();
            JPanel summary_info_but_path = new JPanel();            
            summary_info_but_path.setLayout(new GridLayout(0, 2));
            while(iter.hasNext()){
                String key = iter.next().toString();
                String value = iterv.next().toString();
                if(!key.equals("gene_name") && !key.equals("sig_id")&& !key.equals("complexportal_id")
                            && !key.equals("entity_db_id") && !key.equals("mirna_db_id")){          
                    if(key.equals(NodeField.PATHWAYLISTADDINFO)){
                        listpath.setLayout(new GridBagLayout());
                        String[] pathlist = value.split(" , ");                    
                        for (Integer i=0; i<pathlist.length; i++){
                            if(!pathlist[i].isBlank()){
                                SignorButton pathwayID =  new SignorButton(pathlist[i]);                         
                                listpath.add(pathwayID, gbc.down());
                                final String path_code = pathlist[i];
                                pathwayID.addActionListener(e-> buildPathWay(path_code));
                            }
                        }
                    }
                    else {
                        summary_info_but_path.add(new SignorLabelStyledBold(NodeField.NODESUMMARYMAP.get(key.toUpperCase())), gbc.position(0, it));     
                        if ( value.length() > 20 ) 
                             summary_info_but_path.add(new SignorLabelMore(manager, "[...]", value), gbc.right());
                        else summary_info_but_path.add(new JLabel(value), gbc.right());
                        it++;
                    }
                }
            }            
            summary_info_but_path.add(new SignorLabelStyledBold("Relations "), gbc.down());
            summary_info_but_path.add(new JLabel(manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into).numberOfEdges().toString()), gbc.right());
            CollapsablePanel collapsableINFO = new CollapsablePanel(iconFont, "Summary INFO", summary_info_but_path, false );
            CollapsablePanel collapsablePTH = new CollapsablePanel(iconFont, "Show pathway", listpath, false );
            summPanel.add(collapsablePTH, gbc.down().anchor("north"));
            summPanel.add(collapsableINFO, gbc.down().anchor("west"));    
        }
        catch (Exception e){
            manager.utils.error("SignorSummaryPanel createContent() "+e.toString());
        }       
    }
    private void buildPathWay(String pathid){
        BufferedReader brp = HttpUtils.getHTTPSignor(ConfigResources.PATHLIST, manager);
        ArrayList<String> results = HttpUtils.parseWS(brp, Config.HEADERSINGLESEARCH, false, manager);
        HashMap<String,String> pathid_desc = new HashMap();
        for (int i = 0; i < results.size(); i++) {  
            String[] attributes = results.get(i).split("\t");
            pathid_desc.put(attributes[1], attributes[0]);
        }  
        
        SignorPathwayQueryFactory spq = new SignorPathwayQueryFactory(this.manager);
        HashMap<String, Object> formvalues = new HashMap<>() {
            {put (PathwayField.PATHWAYID, (String) pathid_desc.get(pathid));}                 
        };
        spq.parameters_shift = formvalues;
        spq.param_shift = true;
        manager.utils.execute(spq.createTaskIterator());
    }   
    public void recreateContent(){
        if(manager.presentationManager.signorNetMap.containsKey(current_cynetwork_to_serch_into)){
            summPanel.removeAll();
            createContent();
        }
    }
}

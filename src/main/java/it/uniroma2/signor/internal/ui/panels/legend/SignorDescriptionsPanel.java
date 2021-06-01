/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.ui.panels.legend;
import it.uniroma2.signor.internal.conceptualmodel.logic.Nodes.Node;
import it.uniroma2.signor.internal.conceptualmodel.logic.Pathway.PathwayField;
import it.uniroma2.signor.internal.utils.EasyGBC;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.ConfigResources;
import it.uniroma2.signor.internal.ConfigPathway;
import it.uniroma2.signor.internal.task.query.factories.SignorPathwayQueryFactory;
import it.uniroma2.signor.internal.ui.components.SignorLabelStyledBold;
import it.uniroma2.signor.internal.ui.components.SignorPanelRow;
import it.uniroma2.signor.internal.utils.HttpUtils;
import it.uniroma2.signor.internal.ui.components.HelpButton;
/**
 *
 * @author amministratore
 */
import java.util.Properties;
import java.util.Iterator;
import java.util.ArrayList;
import java.awt.*;
import java.time.Instant;
import java.util.Collection;
import javax.swing.*;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.util.HashMap;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.events.SelectedNodesAndEdgesEvent;
import org.cytoscape.model.events.SelectedNodesAndEdgesListener;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTableUtil;

public class SignorDescriptionsPanel extends JPanel {
    private SignorManager manager;
    private JPanel descPanel;
    private static Font iconFont;
    private EasyGBC gbc=new EasyGBC();
    //public Boolean selectionRunning= false;
    public CyNetwork current_cynetwork_to_serch_into;

     
    public SignorDescriptionsPanel(SignorManager manager){
        setLayout(new GridBagLayout());
        this.manager = manager; 
        current_cynetwork_to_serch_into = manager.lastCyNetwork;
      
        JPanel DescriptionInfo = new JPanel();
        DescriptionInfo.setLayout(new BorderLayout());
        DescriptionInfo.setBackground(Color.WHITE);
        {
            EasyGBC gbc1=new EasyGBC();
            descPanel = new JPanel();
            descPanel.setLayout(new GridBagLayout());
//            descPanel.setBackground(Color.WHITE);
            DescriptionInfo.add(descPanel, BorderLayout.NORTH);
//            DescriptionInfo.add(Box.createVerticalGlue(), gbc1.down().down().expandVert());
            //modPanel.setLayout(new GridBagLayout());
            //ModificationInfo.add(modPanel, BorderLayout.NORTH);
            //NodeInfo.add(Box.createVerticalGlue(), gbc1.down().expandVert());
        }
        JScrollPane scrollPane = new JScrollPane(DescriptionInfo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.getVerticalScrollBar().setBlockIncrement(10);
        add(scrollPane, gbc.down().anchor("east").expandBoth());
        revalidate();
        repaint();   
        //createContent();
    }
    
    public void createContent(){
        try {                   
            //SignorPanelRow listresults = new SignorPanelRow(current_cynetwork_to_serch_into.getEdgeList().size(), 2, manager);
            //sig_id	path_name	coalesce	path_curator
//            descPanel.setLayout(new GridLayout(4, 2));
//            ArrayList<String> pathInfo = HttpUtils.parseWSNoheader(
//                    HttpUtils.getHTTPSignor(ConfigResources.PATHSINGLEDESCRIPTIONSQUERY+
//                            manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into).parameters.get("PATHWAYID"), manager));
            
            ArrayList<String> pathInfo = manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into).getPathwayInfo();
            String[] header_pth = pathInfo.get(0).split("\t");
            String[] path_info_packed=pathInfo.get(1).split("\t");
//            JPanel separator  = new JPanel();
//            separator.setLayout(new GridBagLayout());
//            separator.add(new SignorLabelStyledBold(">> Pathway info "), gbc.down().anchor("west"));
//            separator.setBackground(new Color(82, 166, 119));
//            descPanel.add(separator, gbc.down().anchor("west").insets(2,0,2,0));
            JPanel pathinfo = new JPanel();
            pathinfo.setLayout(new GridBagLayout());
            
            for (int i = 0; i < header_pth.length; i++) {                     
                pathinfo.add(new SignorLabelStyledBold(PathwayField.SIGNORPTHFIELDMAP.get(header_pth[i])), gbc.down());   
                //Path_curator may not be present and path_info_packed is shorter than header_pth
                if(i <path_info_packed.length){
//                    if ( path_info_packed[i].length() > 20 && PathwayField.SIGNORPTHFIELDMAP.get(header_pth[i])== "Description") 
                    if ( path_info_packed[i].length() > 20 && header_pth[i].startsWith(PathwayField.SIGNORPTH_DESCRIPTION))
                        pathinfo.add(new HelpButton(manager, path_info_packed[i]), gbc.right());
                    else 
                        pathinfo.add(new JLabel(path_info_packed[i]), gbc.right());      
                }
                else pathinfo.add(new JLabel(""), gbc.right());

            }      
            CollapsablePanel collapsableINFO = new CollapsablePanel(iconFont, "Pathway INFO", pathinfo, false );
            descPanel.add(collapsableINFO, gbc.down().anchor("north"));
//            descPanel.add(pathinfo, gbc.down());
        }        
        catch (Exception e){
            manager.utils.error("SignorDescriptionPanel CreateContent()"+e.toString());
        }       
    }
    
    public void recreateContent(){
        if(manager.presentationManager.signorNetMap.containsKey(current_cynetwork_to_serch_into)){
            descPanel.removeAll();
            createContent();
        }
    }

}

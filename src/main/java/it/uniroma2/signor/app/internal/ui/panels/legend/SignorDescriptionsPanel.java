/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.ui.panels.legend;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.NetworkField;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Pathway.PathwayField;
import it.uniroma2.signor.app.internal.utils.EasyGBC;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import it.uniroma2.signor.app.internal.ui.components.SignorLabelStyledBold;
import it.uniroma2.signor.app.internal.ui.components.SignorLabelMore;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import static java.awt.Component.LEFT_ALIGNMENT;
import org.cytoscape.model.CyNetwork;

public class SignorDescriptionsPanel extends JPanel {
    private final SignorManager manager;
    private final JPanel descPanel;
    private static Font iconFont;
    private final EasyGBC gbc=new EasyGBC();
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
            descPanel = new JPanel();
            descPanel.setLayout(new GridBagLayout());
            DescriptionInfo.add(descPanel, BorderLayout.NORTH);
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

//            ArrayList<String> pathInfo = manager.presentationManager.signorNetMap.get(current_cynetwork_to_serch_into).getPathwayInfo();
            
            String pathInfoString = (String) manager.presentationManager.signorNetMap.
                    get(current_cynetwork_to_serch_into).parameters.get(NetworkField.PATHWAYINFO);
 

            String[] header_pth = pathInfoString.substring(0,39).split("\t");
            String[] path_info_packed=pathInfoString.substring(40).split("\t");
            manager.utils.info(header_pth.toString());
            manager.utils.info(path_info_packed.toString());
            
            JPanel pathinfo = new JPanel();
            pathinfo.setLayout(new GridBagLayout());
            
            for (int i = 0; i < header_pth.length; i++) {   
                String header_cleaned = header_pth[i].replace("[", "");
                String header_cleaned_final = header_cleaned.replace(",","");
                String path_info_packed_cleaned = path_info_packed[i].replace("]", "");
                pathinfo.add(new SignorLabelStyledBold(PathwayField.SIGNORPTHFIELDMAP.get(header_cleaned_final)), gbc.down());   
                //Path_curator may not be present and path_info_packed is shorter than header_pth
                if(i <path_info_packed.length){
                    if ( path_info_packed_cleaned.length() > 20 && header_pth[i].startsWith(PathwayField.SIGNORPTH_DESCRIPTION))
                        pathinfo.add(new SignorLabelMore(manager, path_info_packed_cleaned.substring(0, 10)+" ... [more]", path_info_packed_cleaned), gbc.right());
//                        pathinfo.add(new HelpButton(manager, path_info_packed[i]), gbc.right());
                    else 
                        pathinfo.add(new JLabel(path_info_packed_cleaned), gbc.right());      
                }
                else pathinfo.add(new JLabel(""), gbc.right());

            }      
            CollapsablePanel collapsableINFO = new CollapsablePanel(iconFont, "Pathway INFO", pathinfo, false );
            descPanel.add(collapsableINFO, gbc.down().anchor("north").insets(2,0,0,0));
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

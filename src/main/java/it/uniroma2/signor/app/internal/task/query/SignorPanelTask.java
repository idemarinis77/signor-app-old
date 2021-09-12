/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.task.query;
import it.uniroma2.signor.app.internal.ui.panels.legend.SignorLegendPanel;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import it.uniroma2.signor.app.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import it.uniroma2.signor.app.internal.task.query.factories.SignorPanelFactory;
import java.awt.*;
import java.util.Properties;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelComponent2;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;
import it.uniroma2.signor.app.internal.Config;
import it.uniroma2.signor.app.internal.event.SignorNetworkCreatedEvent;

public class SignorPanelTask extends AbstractTask {
        private final SignorManager manager;
        private final SignorPanelFactory factory;
        private final Boolean show;           
    
    public SignorPanelTask(SignorManager manager,SignorPanelFactory factory, Boolean show ){
        this.manager=manager;
        this.factory = factory;
        this.show = show;        
    }
    
    public static boolean isPanelRegistered(SignorManager manager) {
        CySwingApplication swingApplication = manager.utils.getService(CySwingApplication.class);
        CytoPanel cytoPanel = swingApplication.getCytoPanel(CytoPanelName.EAST);
        return cytoPanel.indexOfComponent(Config.identifier_panel) >= 0;
    }
    
    @Override
    public void run(TaskMonitor monitor) {
         if (show)
            monitor.setTitle("Show results panel");
        else
            monitor.setTitle("Hide results panel");          
        CySwingApplication swingApplication = manager.utils.getService(CySwingApplication.class);
        CytoPanel cytoPanel = swingApplication.getCytoPanel(CytoPanelName.EAST);
        if (show && cytoPanel.indexOfComponent(Config.identifier_panel) < 0) {
  
            CytoPanelComponent2 signorpanel = new SignorLegendPanel(manager);
           // Register it
            manager.utils.registerService(signorpanel, CytoPanelComponent.class, new Properties());
            if (cytoPanel.getState() == CytoPanelState.HIDE){                
                cytoPanel.setState(CytoPanelState.DOCK);
                
            }
            Network currentNetwork = manager.presentationManager.getCurrentNetwork();
            if (currentNetwork != null) {
                manager.utils.fireEvent(new SignorNetworkCreatedEvent(manager, currentNetwork));                
            }
       } else if (!show && cytoPanel.indexOfComponent(Config.identifier_panel) >= 0) {
        
            int compIndex = cytoPanel.indexOfComponent(Config.identifier_panel);
            Component panel = cytoPanel.getComponentAt(compIndex);
            if (panel instanceof CytoPanelComponent2) {
                //Unregister it
                manager.utils.unregisterService(panel, CytoPanelComponent.class);
                manager.utils.setDetailPanel(null);
            }
        }
        factory.reregister();
    }
}

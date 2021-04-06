package it.uniroma2.signor.internal;


import org.cytoscape.application.swing.search.NetworkSearchTaskFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.session.CyNetworkNaming;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.BundleContext;

import org.cytoscape.task.read.LoadVizmapFileTaskFactory;
import org.cytoscape.task.visualize.ApplyVisualStyleTaskFactory;
import java.util.Properties;

import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.task.query.factories.SignorGenericQueryFactory;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CyAction;
import it.uniroma2.signor.internal.ui.panels.legend.SignorLegendPanel;
import it.uniroma2.signor.internal.ui.panels.legend.SignorLegendAction;

public class CyActivator extends AbstractCyActivator {
	public CyActivator() {
		super();
	}
	public void start(BundleContext bc) {
            boolean haveGUI = true;
            ServiceReference ref = bc.getServiceReference(CySwingApplication.class.getName());
            if (ref == null) {
                haveGUI = false;
                // Issue error and return
            }
            // Get a handle on the CyServiceRegistrar
            CyServiceRegistrar registrar = getService(bc, CyServiceRegistrar.class);
            SignorManager manager = new SignorManager(registrar);            
            CyApplicationManager cyApplicationManagerServiceRef = getService(bc,CyApplicationManager.class);

            
            {
                SignorGenericQueryFactory signorQuery = new SignorGenericQueryFactory(manager);
                Properties propsSearch = new Properties();
                registerService(bc, signorQuery, NetworkSearchTaskFactory.class, propsSearch);
            }

            if (haveGUI) {
                CyNetworkManager cyNetworkManagerServiceRef = getService(bc,CyNetworkManager.class);
                CySwingApplication cytoscapeDesktopService = getService(bc,CySwingApplication.class);
                
                //SignorLegendController signorLegendController = new SignorLegendController(registrar, cyNetworkManagerServiceRef);
                //SignorLegendPanel signorLegendGeneral = new SignorLegendPanel(registrar, signorLegendController, manager);
                SignorLegendPanel signorLegendGeneral = new SignorLegendPanel(registrar, manager);
                SignorLegendAction signorLegendAction = new SignorLegendAction(cytoscapeDesktopService,signorLegendGeneral);
                
                registerService(bc,signorLegendGeneral,CytoPanelComponent.class);
                registerService(bc,signorLegendAction,CyAction.class);     
            }
         /*    if (haveGUI) {
            ShowDetailPanelTaskFactory showResults = new ShowDetailPanelTaskFactory(manager);
            showResults.reregister();
            manager.utils.setShowDetailPanelTaskFactory(showResults);

            CyNetwork current = manager.data.getCurrentCyNetwork();
            if (ModelUtils.isIntactNetwork(current)) {
                manager.utils.execute(showResults.createTaskIterator(), true);
            }
        }*/
            
//          Properties propsSettings = new Properties();
//          propsSettings.setProperty(PREFERRED_MENU, "Apps.SIGNOR");
//          propsSettings.setProperty(TITLE, "About");
//          propsSettings.setProperty(MENU_GRAVITY, "21.0");
//          propsSettings.setProperty(IN_MENU_BAR, "true");
//          registerService(bc, new AboutTaskFactory(manager), TaskFactory.class, propsSettings);	
        }
        
}


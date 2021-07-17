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
import static org.cytoscape.work.ServiceProperties.*;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.task.query.factories.SignorGenericQueryFactory;
import it.uniroma2.signor.internal.task.query.factories.SignorPanelFactory;
import it.uniroma2.signor.internal.task.query.factories.SignorPathwayQueryFactory;
import it.uniroma2.signor.internal.task.query.factories.SignorInteractomeFactory;

import it.uniroma2.signor.internal.task.query.factories.SignorInteractomeQueryFactory;
import it.uniroma2.signor.internal.task.query.factories.SignorInteractomePTMQueryFactory;
import it.uniroma2.signor.internal.Config;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CyAction;
import it.uniroma2.signor.internal.ui.panels.legend.SignorLegendPanel;
import it.uniroma2.signor.internal.ui.panels.legend.SignorLegendAction;
import it.uniroma2.signor.internal.utils.DataUtils;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;

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
                Properties propsSettings = new Properties();
                propsSettings.setProperty(PREFERRED_MENU, "Apps.Signor");
                propsSettings.setProperty(TITLE, "Download Signor INTERACTOME (11Mb)");
                propsSettings.setProperty(MENU_GRAVITY, "6.0");
                propsSettings.setProperty(IN_MENU_BAR, "true");                

                propsSettings.setProperty(COMMAND_NAMESPACE, "signor");
                propsSettings.setProperty(COMMAND, "download interactome");
                propsSettings.setProperty(COMMAND_DESCRIPTION, "This query download the complete database of Signor");
                
                registerService(bc, new SignorInteractomeFactory(manager, false), TaskFactory.class, propsSettings);
            }
            {
                Properties propsSettings = new Properties();
                propsSettings.setProperty(PREFERRED_MENU, "Apps.Signor");
                propsSettings.setProperty(TITLE, "Download Signor INTERACTOME PTM (11Mb)");
                propsSettings.setProperty(MENU_GRAVITY, "7.0");
                propsSettings.setProperty(IN_MENU_BAR, "true");
                propsSettings.setProperty(INSERT_SEPARATOR_AFTER, "true");
                
               
                registerService(bc, new SignorInteractomeFactory(manager, true), TaskFactory.class, propsSettings);
            }
            {
                SignorGenericQueryFactory signorQuery = new SignorGenericQueryFactory(manager);
                Properties propsSearch = new Properties();
                registerService(bc, signorQuery, NetworkSearchTaskFactory.class, propsSearch);
            }
            {
                SignorPathwayQueryFactory signorPathwayQuery = new SignorPathwayQueryFactory(manager);
                Properties propsSearch = new Properties();
                registerService(bc, signorPathwayQuery, NetworkSearchTaskFactory.class, propsSearch);
            }
            {
                SignorInteractomeQueryFactory signorInteractomeQueryFactory = new SignorInteractomeQueryFactory(manager, false);
                Properties propsSearch = new Properties();
                registerService(bc, signorInteractomeQueryFactory, NetworkSearchTaskFactory.class, propsSearch);
            }
            {
                SignorInteractomePTMQueryFactory signorInteractomePTMQueryFactory = new SignorInteractomePTMQueryFactory(manager, true);
                Properties propsSearch = new Properties();
                registerService(bc, signorInteractomePTMQueryFactory, NetworkSearchTaskFactory.class, propsSearch);
            }
            if (haveGUI) {
              SignorPanelFactory showResults = new SignorPanelFactory(manager);
              showResults.reregister();
              manager.utils.setShowDetailPanelTaskFactory(showResults);

              //CyNetwork current = manager.data.getCurrentCyNetwork();
              
              try {
                CyNetwork cynet = manager.utils.getService(CyApplicationManager.class).getCurrentNetwork();
                if (DataUtils.isSignorNetwork(cynet)){
                    manager.utils.execute(showResults.createTaskIterator(), true);
                }
              }
              catch(Exception e){
                manager.utils.error("CyActivator haveGUI() "+e.toString());
              }
            }
            manager.utils.info("Signor App initialized");           
        }
        
}


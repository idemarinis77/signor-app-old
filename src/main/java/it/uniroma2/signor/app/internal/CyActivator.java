package it.uniroma2.signor.app.internal;


import org.cytoscape.application.swing.search.NetworkSearchTaskFactory;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.BundleContext;
import java.util.Properties;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import it.uniroma2.signor.app.internal.task.query.factories.SignorGenericQueryFactory;
import it.uniroma2.signor.app.internal.task.query.factories.SignorPanelFactory;
import it.uniroma2.signor.app.internal.task.query.factories.SignorPathwayQueryFactory;
import it.uniroma2.signor.app.internal.task.query.factories.SignorInteractomeQueryFactory;
import it.uniroma2.signor.app.internal.task.query.factories.SignorInteractomePTMQueryFactory;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import it.uniroma2.signor.app.internal.utils.DataUtils;
import org.cytoscape.model.CyNetwork;

public class CyActivator extends AbstractCyActivator {
	public CyActivator() {
		super();
	}
        @Override
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
//            {
//                Properties propsSettings = new Properties();
//                propsSettings.setProperty(PREFERRED_MENU, "Apps.Signor");
//                propsSettings.setProperty(TITLE, "Download Signor INTERACTOME (11Mb)");
//                propsSettings.setProperty(MENU_GRAVITY, "6.0");
//                propsSettings.setProperty(IN_MENU_BAR, "true");                
//
//                propsSettings.setProperty(COMMAND_NAMESPACE, "signor");
//                propsSettings.setProperty(COMMAND, "download interactome");
//                propsSettings.setProperty(COMMAND_DESCRIPTION, "This query download the complete database of Signor");
//                
//                registerService(bc, new SignorInteractomeFactory(manager, false), TaskFactory.class, propsSettings);
//            }
//            {
//                Properties propsSettings = new Properties();
//                propsSettings.setProperty(PREFERRED_MENU, "Apps.Signor");
//                propsSettings.setProperty(TITLE, "Download Signor INTERACTOME PTM (11Mb)");
//                propsSettings.setProperty(MENU_GRAVITY, "7.0");
//                propsSettings.setProperty(IN_MENU_BAR, "true");
//                propsSettings.setProperty(INSERT_SEPARATOR_AFTER, "true");
//                
//               
//                registerService(bc, new SignorInteractomeFactory(manager, true), TaskFactory.class, propsSettings);
//            }
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


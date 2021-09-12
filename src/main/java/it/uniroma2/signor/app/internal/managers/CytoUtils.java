package it.uniroma2.signor.app.internal.managers;

import org.apache.log4j.Logger;
import org.cytoscape.event.CyEvent;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.property.CyProperty;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.SynchronousTaskManager;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.TaskObserver;
import org.cytoscape.application.CyUserLog;
import it.uniroma2.signor.app.internal.ui.panels.legend.SignorLegendPanel;
import it.uniroma2.signor.app.internal.task.query.factories.SignorPanelFactory;
import javax.swing.*;
import java.util.Properties;

public class CytoUtils {
    final CyServiceRegistrar registrar;
    final CyEventHelper cyEventHelper;
    final Logger logger;
    final TaskManager<?, ?> dialogTaskManager;
    final SynchronousTaskManager<?> synchronousTaskManager;
    SignorPanelFactory signorPanelFactory;
    SignorLegendPanel signorLegendPanel;

    public CytoUtils(CyServiceRegistrar registrar) {
        this.logger = Logger.getLogger(CyUserLog.NAME);
        this.registrar = registrar;
        // Get our task managers
        this.dialogTaskManager = registrar.getService(TaskManager.class);
        this.synchronousTaskManager = registrar.getService(SynchronousTaskManager.class);
        this.cyEventHelper = registrar.getService(CyEventHelper.class);
    }

    public void flushEvents() {
        cyEventHelper.flushPayloadEvents();
    }

    public void execute(TaskIterator iterator) {
        execute(iterator, false);
    }

    public void execute(TaskIterator iterator, TaskObserver observer) {
        execute(iterator, observer, false);
    }

    public void execute(TaskIterator iterator, boolean synchronous) {
        if (synchronous) {
            synchronousTaskManager.execute(iterator);
        } else {
            dialogTaskManager.execute(iterator);
        }
    }

    public void execute(TaskIterator iterator, TaskObserver observer, boolean synchronous) {
        if (synchronous) {
            synchronousTaskManager.execute(iterator, observer);
        } else {
            dialogTaskManager.execute(iterator, observer);
        }
    }

    public void info(String info) {
        logger.info(info);
    }

    public void warn(String warn) {
        logger.warn(warn);
    }

    public void error(String error) {
        logger.error(error);
    }

    public void critical(String criticalError) {
        logger.error(criticalError);
        SwingUtilities.invokeLater(
                () -> JOptionPane.showMessageDialog(null, "<html><p style=\"width:200px;\">" + criticalError + "</p></html>", "Critical IntActApp error", JOptionPane.ERROR_MESSAGE)
        );
    }

    public void setShowDetailPanelTaskFactory(SignorPanelFactory factory) {
        signorPanelFactory = factory;
    }

    public void setDetailPanel(SignorLegendPanel detailPanel) {
        this.signorLegendPanel = detailPanel;
    }

    public void showResultsPanel() {
        if (signorLegendPanel == null) {
            execute(signorPanelFactory.createTaskIterator(), true);
        } else {
            // Make sure we show it
            signorLegendPanel.showCytoPanel();
        }
    }

    public void hideResultsPanel() {
        if (signorLegendPanel != null) {
            signorLegendPanel.hideCytoPanel();
        }
    }

    public <T> T getService(Class<? extends T> clazz) {
        return registrar.getService(clazz);
    }

    public <T> T getService(Class<? extends T> clazz, String filter) {
        return registrar.getService(clazz, filter);
    }

    public void registerService(Object service, Class<?> clazz, Properties props) {
        registrar.registerService(service, clazz, props);
    }

    public void registerAllServices(CyProperty<Properties> service, Properties props) {
        registrar.registerAllServices(service, props);
    }

    public void registerAllServices(Object service, Properties props) {
        registrar.registerAllServices(service, props);
    }

    public void unregisterService(Object service, Class<?> clazz) {
        registrar.unregisterService(service, clazz);
    }

    public void fireEvent(CyEvent<?> cyEvent) {
        cyEventHelper.fireEvent(cyEvent);
    }
}
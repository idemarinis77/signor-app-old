/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.task.query.factories;
import it.uniroma2.signor.internal.task.query.SignorPanelTask;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.managers.SignorManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import it.uniroma2.signor.internal.Config;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import org.cytoscape.application.CyApplicationManager;
import static org.cytoscape.work.ServiceProperties.IN_MENU_BAR;
import static org.cytoscape.work.ServiceProperties.MENU_GRAVITY;
import static org.cytoscape.work.ServiceProperties.PREFERRED_MENU;
import static org.cytoscape.work.ServiceProperties.TITLE;
import org.cytoscape.work.TaskFactory;

/**
 *
 * @author amministratore
 */
public class SignorPanelFactory extends AbstractTaskFactory{
    private SignorManager manager;
    boolean show = false;
    
    public SignorPanelFactory(SignorManager manager){
        this.manager = manager;
    }
    
    public TaskIterator createTaskIterator() {              
        return new TaskIterator(new SignorPanelTask(manager, this, show));
   }  

    public void reregister() {
        manager.utils.unregisterService(this, TaskFactory.class);
        Properties props = new Properties();
        props.setProperty(PREFERRED_MENU, "Apps.Signor");
        
        if (SignorPanelTask.isPanelRegistered(manager)) {
            props.setProperty(TITLE, "Hide results panel");
            show = false;
        } else {
            props.setProperty(TITLE, "Show results panel");
            show = true;
        }
        props.setProperty(MENU_GRAVITY, "7.0");
        props.setProperty(IN_MENU_BAR, "true");
        manager.utils.registerService(this, TaskFactory.class, props);
    }
    public boolean isReady() {
        // We always want to be able to shut it off
        if (!show) return true;

        return manager.utils.getService(CyApplicationManager.class).getCurrentNetwork() != null;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.app.internal.task.query.factories;
import it.uniroma2.signor.app.internal.task.query.SignorPanelTask;
import it.uniroma2.signor.app.internal.managers.SignorManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import java.util.Properties;
import static org.cytoscape.work.ServiceProperties.IN_MENU_BAR;
import static org.cytoscape.work.ServiceProperties.MENU_GRAVITY;
import static org.cytoscape.work.ServiceProperties.PREFERRED_MENU;
import static org.cytoscape.work.ServiceProperties.TITLE;
import org.cytoscape.work.TaskFactory;

public class SignorPanelFactory extends AbstractTaskFactory{
    final SignorManager manager;
    boolean show = false;
    boolean showBypanel = true;
    public SignorPanelFactory(SignorManager manager){
        this.manager = manager;
    }
    
    @Override
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
    
    @Override
    public boolean isReady() {
        if (!show) return true;
        return manager.presentationManager.getCurrentNetwork() !=null;
    }
}

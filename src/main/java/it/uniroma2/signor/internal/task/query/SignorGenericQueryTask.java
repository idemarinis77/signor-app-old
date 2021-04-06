/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.task.query;
import it.uniroma2.signor.internal.Config;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import java.util.HashMap;
import org.cytoscape.work.TaskFactory;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.ui.panels.result.SignorResultPanel;
import it.uniroma2.signor.internal.task.query.factories.SignorGenericRetrieveResultFactory;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author amministratore
 */
public class SignorGenericQueryTask extends AbstractTask {
        private final SignorManager manager;
        private final Network network;
        private final HashMap<String, ?> parameters;      
        private final String panelTitle;
        private final String terms;
        private Config CONFIG = new Config();
           
    
    public SignorGenericQueryTask(Network network,String panelTitle, HashMap<String, ?> parameters, String terms){
        this.network=network;
        this.manager=network.manager;
        this.panelTitle=panelTitle;
        this.parameters=parameters;
        this.terms = terms;
    }
    @Override
    public void run(TaskMonitor monitor) {
        Integer fakeresult = 5;
        monitor.setTitle("Querying Signor Database");
        String search="";        
        Boolean includefirstneighbor = false;
        String organism = CONFIG.SPECIES.get(parameters.get("SPECIES").toString());
        manager.utils.info(organism.toString());
        manager.utils.info(parameters.toString());
        //Parsing data
        if (parameters.get("SINGLESEARCH").equals(true)) {search = "SINGLESEARCH"; }
        if (parameters.get("ALLSEARCH").equals(true)) {search = "ALLSEARCH"; }
        if (parameters.get("CONNECTSEARCH").equals(true)) {search = "CONNECTSEARCH"; }
        if (parameters.get("SHORTESTPATHSEARCH").equals(true)) {search = "SHORTESTPATHSEARCH"; }
        if (parameters.get("INCFIRSTNEISEARCH").equals(true)) {includefirstneighbor = true; }            
        
        /*TaskFactory factory = new SignorGenericRetrieveResultFactory(search, includefirstneighbor, organism, terms,network);
        manager.utils.execute(factory.createTaskIterator());*/
        final Boolean fneighcopy = includefirstneighbor;
        final String finalsearch = search;
        SwingUtilities.invokeLater(() -> {
                JDialog d = new JDialog();
                d.setTitle(panelTitle);
                d.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                SignorResultPanel SRP=new SignorResultPanel(fakeresult, parameters.get("SPECIES").toString(), finalsearch, fneighcopy, terms, network);
                d.setContentPane(SRP);
                d.pack();
                d.setVisible(true);
            });        
    }    
}

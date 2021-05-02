/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.signor.internal.task.query;
import it.uniroma2.signor.internal.Config;
import it.uniroma2.signor.internal.ConfigResources;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import java.util.HashMap;
import org.cytoscape.work.TaskFactory;
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.Network;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.ui.panels.result.SignorResultPanel;
import it.uniroma2.signor.internal.task.query.factories.SignorGenericRetrieveResultFactory;
import it.uniroma2.signor.internal.utils.HttpUtils;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.util.ArrayList;
import org.cytoscape.work.ProvidesTitle;
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
           
    
    public SignorGenericQueryTask(Network network,String panelTitle, HashMap<String, ?> parameters, String terms){
        this.network=network;
        this.manager=network.manager;
        this.panelTitle=panelTitle;
        this.parameters=parameters;
        this.terms = terms;
    }
    @Override
    public void run(TaskMonitor monitor) {
        //Integer fakeresult = 5;
        
        String search="";        
        Boolean includefirstneighbor = false;
       /* String organism = CONFIG.SPECIES.get(parameters.get("SPECIES").toString());
        manager.utils.info(organism.toString());
        manager.utils.info(parameters.toString());*/
        //Parsing data
        if (parameters.get(Config.SINGLESEARCH).equals(true)) {search = Config.SINGLESEARCH; }
        if (parameters.get(Config.ALLSEARCH).equals(true)) {search = Config.ALLSEARCH; }
        if (parameters.get(Config.CONNECTSEARCH).equals(true)) {search = Config.CONNECTSEARCH; }
        if (parameters.get(Config.SHORTESTPATHSEARCH).equals(true)) {search = Config.SHORTESTPATHSEARCH; }
        if (parameters.get(Config.INCFIRSTNEISEARCH).equals(true)) {includefirstneighbor = true; }            
        String species = parameters.get(Config.SPECIES).toString();
        /*TaskFactory factory = new SignorGenericRetrieveResultFactory(search, includefirstneighbor, organism, terms,network);
        manager.utils.execute(factory.createTaskIterator());*/
        try{
            if (search == Config.SINGLESEARCH){
                final Boolean fneighcopy = includefirstneighbor;
                final String finalsearch = search;
                
                //Retrieve number of result
                //Config.WSSearchoptionMAP.get(search).queryFunction.apply(Config.SPECIESLIST.get(species), terms);
                manager.utils.info(ConfigResources.WSSearchoptionMAP.
                                               get("ENTITYINFOSEARCH").queryFunction.apply(terms, Config.SPECIESLIST.get(species)));
                BufferedReader br = HttpUtils.getHTTPSignor(ConfigResources.WSSearchoptionMAP.
                                              get("ENTITYINFOSEARCH").queryFunction.apply(terms,Config.SPECIESLIST.get(species)), manager);

                ArrayList<String> results= HttpUtils.parseWSNoheader(br);
                if(Config.SPECIESLIST.get(species) != "9060")
                   results.remove(0);
                if(results.size() >1 ){
                    SwingUtilities.invokeLater(() -> {
                            JDialog d = new JDialog();
                            d.setTitle(panelTitle);
                            d.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                            d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                            SignorResultPanel SRP=new SignorResultPanel(results.size(),results, parameters.get("SPECIES").toString(), finalsearch, fneighcopy, terms, network);
                            d.setContentPane(SRP);
                            d.pack();
                            d.setVisible(true);
                        }); 
                }
                else if (results.size() == 1){
                    //I take primaryId from first line, in this case is in the third field
                    String primaryID = results.get(0).split("\t")[2];                    
                    TaskFactory factory = new SignorGenericRetrieveResultFactory(search, includefirstneighbor, parameters.get("SPECIES").toString(), primaryID ,network);
                    manager.utils.execute(factory.createTaskIterator());
                }
                else if (results.size() == 0){             
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "No results for "+terms,
                        "No results", JOptionPane.ERROR_MESSAGE));
                    //No results
                    //monitor.setTitle("Results for Signor Network"); 
                    //monitor.showMessage(TaskMonitor.Level.ERROR, "No results for "+terms);                
                }
            }
            if (search == Config.CONNECTSEARCH || search == Config.ALLSEARCH){                
                String terms_for_all = terms.replace(" ", "%2C");
                manager.utils.info(terms_for_all+" "+search);
                TaskFactory factory = new SignorGenericRetrieveResultFactory(search, includefirstneighbor, parameters.get("SPECIES").toString(), terms_for_all, network);
                manager.utils.execute(factory.createTaskIterator());                
            }
        }
        catch (Exception e){
            manager.utils.error("SignorGenericQueryTask run() "+e.toString()+
                                  search+", "+includefirstneighbor+", "+parameters.get("SPECIES").toString()+", "+network.toString());
        }
    }   
    
    @ProvidesTitle
    public String getTitle() {
        return "Providing results";
    }
}

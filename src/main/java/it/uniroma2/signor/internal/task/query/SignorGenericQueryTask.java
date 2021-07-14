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
import it.uniroma2.signor.internal.conceptualmodel.logic.Network.NetworkField;
import it.uniroma2.signor.internal.managers.SignorManager;
import it.uniroma2.signor.internal.ui.panels.result.SignorResultPanel;
import it.uniroma2.signor.internal.task.query.factories.SignorGenericRetrieveResultFactory;
import it.uniroma2.signor.internal.utils.HttpUtils;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;

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

        //Parsing data
        if (parameters.get(NetworkField.SINGLESEARCH).equals(true)) {search = NetworkField.SINGLESEARCH; }
        if (parameters.get(NetworkField.ALLSEARCH).equals(true)) {search = NetworkField.ALLSEARCH; }
        if (parameters.get(NetworkField.CONNECTSEARCH).equals(true)) {search = NetworkField.CONNECTSEARCH; }
        if (parameters.get(NetworkField.SHORTESTPATHSEARCH).equals(true)) {search = NetworkField.SHORTESTPATHSEARCH; }
        if (parameters.get(NetworkField.INCFIRSTNEISEARCH).equals(true)) {includefirstneighbor = true; }            
        String species = parameters.get(NetworkField.SPECIES).toString();

        try{
            if (search == NetworkField.SINGLESEARCH){
                final Boolean fneighcopy = includefirstneighbor;
                final String finalsearch = search;
                final String terms_trimmed = terms.trim();
                Integer position_of_primary_id=0;
                //Retrieve number of result
                //Config.WSSearchoptionMAP.get(search).queryFunction.apply(Config.SPECIESLIST.get(species), terms);
                manager.utils.info(ConfigResources.WSSearchoptionMAP.
                                               get("ENTITYINFOSEARCH").queryFunction.apply(terms_trimmed, Config.SPECIESLIST.get(species)));
                BufferedReader br = HttpUtils.getHTTPSignor(ConfigResources.WSSearchoptionMAP.
                                              get("ENTITYINFOSEARCH").queryFunction.apply(terms_trimmed,Config.SPECIESLIST.get(species)), manager);

                ArrayList<String> results= HttpUtils.parseWSNoheader(br);
//                if(Config.SPECIESLIST.get(species) != "9606")
//                    results.remove(0);
//                
                if (results.size() == 0){             
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "No results for "+terms_trimmed,
                        "No results", JOptionPane.ERROR_MESSAGE));
                    return;                    
                }
                if(results.get(0).startsWith("name")){
                    //I'm reading the header, could be 1 entry for H.S. and many for the other species
                    String[] field = results.get(0).split("\t");
                    results.remove(0);
                    if (Arrays.toString(field).contains("mirna_db_id")){
                        position_of_primary_id = Arrays.asList(field).indexOf("mirna_db_id");
                    }
                    else if (Arrays.toString(field).contains("entity_db_id")){
                        position_of_primary_id = Arrays.asList(field).indexOf("entity_db_id");
                    }
                    else if (Arrays.toString(field).contains("sig_id")){
                        position_of_primary_id = Arrays.asList(field).indexOf("sig_id");
                    }
                }
                if(results.size() >1 ){
                    SwingUtilities.invokeLater(() -> {
                            JDialog d = new JDialog();
                            d.setTitle(panelTitle);
                            d.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                            d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                            SignorResultPanel SRP=new SignorResultPanel(results.size(),results, parameters.get("SPECIES").toString(), finalsearch, fneighcopy, terms_trimmed, network);
                            d.setContentPane(SRP);
                            d.pack();
                            d.setVisible(true);
                        }); 
                }
                if (results.size() == 1){
                    //I take primaryId from first line, in this case is in the third field
//                    String[] field = results.get(0).split("\t");
                    String primaryID=results.get(0).split("\t")[position_of_primary_id];
                    
                    TaskFactory factory = new SignorGenericRetrieveResultFactory(search, includefirstneighbor, parameters.get("SPECIES").toString(), primaryID ,network);
                    manager.utils.execute(factory.createTaskIterator());
                }
                
            }
            if (search == NetworkField.CONNECTSEARCH || search == NetworkField.ALLSEARCH || search == NetworkField.SHORTESTPATHSEARCH){                
                String terms_no_space = terms.replace(" ", "%2C").trim();
                String terms_for_all = terms_no_space.replace("\n", "%2C").trim();
                manager.utils.info(terms_for_all+" "+search);
                TaskFactory factory = new SignorGenericRetrieveResultFactory(search, includefirstneighbor, parameters.get("SPECIES").toString(), terms_for_all, network);
                manager.utils.execute(factory.createTaskIterator());                
            }
            
        }
        catch (Exception e){
            manager.utils.error("SignorGenericQueryTask run() "+e.toString()+
                                  search+", "+includefirstneighbor+", "+parameters.get("SPECIES").toString()+", "+network.toString()+" ,"+Config.SPECIESLIST.get(species));
        }
    }   
    
    @ProvidesTitle
    public String getTitle() {
        return "Providing results";
    }
}

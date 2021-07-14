package it.uniroma2.signor.internal.utils;


import org.cytoscape.io.util.StreamUtil;
/*import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;*/
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import org.cytoscape.model.CyNetwork;

import it.uniroma2.signor.internal.managers.SignorManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
public class HttpUtils {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    public static BufferedReader getHTTPSignor(String url, SignorManager manager) {
        URL trueURL;
        try {
                trueURL = new URL(url);
            
        } catch (MalformedURLException e) {
            manager.utils.error("Method getHTTPSignor URL malformed in getHTTPSignor "+url);
            return null;
        }

        BufferedReader DataFromURL=null;
        try {
            DataFromURL = new BufferedReader(new InputStreamReader(trueURL.openStream()));

        } catch (Exception e) {
            manager.utils.error("Method getHTTPSignor "+e.toString());
        }
        return DataFromURL;

    }
    public static String getStringArguments(Map<String, String> args) {
        StringBuilder s = null;
        try {
            for (String key : args.keySet()) {
                if (s == null)
                    s = new StringBuilder(key + "=" + URLEncoder.encode(args.get(key), StandardCharsets.UTF_8.displayName()));
                else
                    s.append("&").append(key).append("=").append(URLEncoder.encode(args.get(key), StandardCharsets.UTF_8.displayName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s != null ? s.toString() : "";
    }

    public static ArrayList<String> parseWS(BufferedReader br, String[] header, Boolean shortestpath, SignorManager manager){
        
        //Pare buffered streaming obtained by getHTTPSignor
        //Read streming until I find header
        String newheader = String.join("\t", header);
        String strCurrentLine;
        ArrayList<String> results = new ArrayList<String>();
//        // Create a map to save the nodes
//        Map<String, CyNode> nodeMap = new HashMap<>();
//        // Create a map to save the node names        
//        Map<String, String> nodeNameMap = new HashMap<>();
        Boolean header_found = true;
        try {
            while ((strCurrentLine = br.readLine()) != null) {
//                if (header_found){          
                    //If shortest path search is requested, remove some fields
                    if(shortestpath.equals(true)){
                        String[] fields = strCurrentLine.split("\t");
                        manager.utils.info(strCurrentLine);
                        int fields_length = fields.length;
                        String newfields = "";
                        for (int i=0; i< fields_length; i++){
                            if((i != (fields_length -1)) &&  (i!= (fields_length -3)) && (i!= (fields_length -4) ) && (i!= (fields_length -5) )){
                                newfields = newfields+fields[i]+"\t";
                            }
                        }
                        results.add(newfields);
                        continue;
                    }
                    results.add(strCurrentLine);
//                }
//                else if (br.equals(newheader)) {
//                    header_found = true;       
//                }

            }
        }
        catch (Exception e){
            return null;
        }
        return results;
    }
    public static ArrayList<String> parseWSNoheader(BufferedReader br){        
        String strCurrentLine;
        ArrayList<String> results = new ArrayList<String>();
        try {
            while ((strCurrentLine = br.readLine()) != null) {
                   
                    results.add(strCurrentLine);
                }
        }
        catch (Exception e){
            return null;
        }
        return results;
    }

}

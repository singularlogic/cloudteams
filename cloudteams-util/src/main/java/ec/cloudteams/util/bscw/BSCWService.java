/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.cloudteams.util.bscw;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
/**
 *
 * @author jled
 */
public class BSCWService {
    
//String userpass="gledakis:jXN3i9QFSv";    
    
    
public BSCWUser getUserRoleDetailsFromUsername(String username){
    
    
    //# -*- coding: iso-8859-15 -*-
    //USER_PASSWD = 'gledakis:jXN3i9QFSv'
    //SERVER_URL = 'https://cloudteams.fit.fraunhofer.de/bscw/bscw.cgi'
    //XAPI_TEST_FOLDER = '11010'
    String userpass="gledakis:paaswordhere";    

    
    HttpResponse<JsonNode> jsonResponse = null;
    String URI = "https://cloudteams.fit.fraunhofer.de/bscw/bscw.cgi";


    
    try {
        jsonResponse = Unirest.post(URI)
                .header("accept", "application/json")
                .body("{\"id\": \"1233\", \"method\":\"list_user\",\"jsonrpc\": \"2.0\",\"params\": {\"username\": \"" + username + "\",\"securityToken\": \"5E74E5D24C89594263B4C266BF3D4\",\"firstname\": \"" + username  + "\"}}")
                .asJson();
    } catch (UnirestException e) {
        e.printStackTrace();
    }

    // if(jsonResponse.getBody().getObject().has("error")) return false;
    //if(jsonResponse.getBody().getObject().getJSONObject("result").getString("code").equals("SUCCESS")) return true;
    // undefined behaviour
    JSONObject object = jsonResponse.getBody().getObject();
    
    System.out.println("object"+object.toString());
    
    return new BSCWUser();
}    

    public static void main(String[] args) {
       BSCWService bscwtester = new BSCWService();
       
       bscwtester.getUserRoleDetailsFromUsername("gledakis");
       
        
    }
    
    
}

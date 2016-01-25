package eu.cloudteams.github;

import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public final class GithubAuthHandler {

    private static final String CLIENT_ID = "fdd1aeb4d0737dc97652";
    private static final String CLIENT_SECRET = "9838e96c9067c4b466edbce380eda0e43c2a1a9d";

    public static void retrieveAccesToken(String code) {
        System.out.println("Code is: "+code);
        MultiValueMap<String, Object> parameteres = new LinkedMultiValueMap<>();
        //Set parameters of request
        parameteres.add("client_secret", CLIENT_SECRET);
        parameteres.add("client_id", CLIENT_ID);
        parameteres.add("code", code);
        RestTemplate restTemplate = new RestTemplate();
        //Make Rest call
        ResponseEntity<String>  response = restTemplate.postForEntity("https://github.com/login/oauth/access_token", parameteres, String.class);
        System.out.println("Github response is: "+response.getBody());
    }

}

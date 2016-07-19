package eu.cloudteams.util.bitbucket;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public final class BitbucketAuthHandler {

    // Production client id
    private static final String CLIENT_ID = "6xQqvPuY6ZgFcsGmDj";

    // Development client id
//    private static final String CLIENT_ID = "413dd46351dc0c48f306";
    
    // Production client secret
    private static final String CLIENT_SECRET = "8XUSDevvr8345HFcLWUYmHJSzn8YZ2Gk";

    // Development client secret
//    private static final String CLIENT_SECRET = "4fefd9a3d5111e6ec993bbf2958f15f2015da9c6";
    private static final String BITBUCKET_API_URL = "https://bitbucket.org/site/oauth2/access_token";

    public static BitbucketAuthResponse requestAccesToken(JSONObject jsonResponse) {
        BitbucketAuthResponse authResponse = new BitbucketAuthResponse();
        
        try {
            //Check if GithHub temporary code is received
            if (jsonResponse.has("code")) {
                //parameteres.add("client_id", CLIENT_ID);
                //parameteres.add("secret", CLIENT_SECRET);
                String plainCreds = CLIENT_ID+":"+CLIENT_SECRET;
                byte[] plainCredsBytes = plainCreds.getBytes();
                byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
                String base64Creds = new String(base64CredsBytes);

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Basic " + base64Creds);
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); 
                
                //example for GET
                //HttpEntity<String> request = new HttpEntity<String>(headers);

                //Set parameters of request
                MultiValueMap<String, String> parameteres = new LinkedMultiValueMap<>();
                String code =jsonResponse.get("code").toString();
                String cteamsusername =jsonResponse.get("username").toString();
                //String code ="RE7PTkYmJKK6wfeSZc";
                System.out.println("--code-->"+code );
                parameteres.add("code", code);
                parameteres.add("grant_type", "authorization_code");
                parameteres.add("redirect_uri", "https://cloudteams.euprojects.net/bitbucket/api/v1/bitbucket/auth?username="+cteamsusername);
                //parameteres.add("redirect_uri", "https://cloudteams.euprojects.net/bitbucket/api/v1/bitbucket/auth");
                
                          
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameteres, headers);
                
                RestTemplate restTemplate = new RestTemplate();
                System.out.println("headers:"+request.getHeaders().toString());
                System.out.println("body(str):"+request.getBody().toString());
                System.out.println("-----Request created");
                //Make Rest call to fetch AccessToken
                //ResponseEntity<Object> accesstokenResponse = restTemplate.postForEntity(BITBUCKET_API_URL, parameteres, Object.class);
                //ResponseEntity<String>  accesstokenResponse = restTemplate.postForEntity(BITBUCKET_API_URL, request, String.class);

                //String result = restTemplate.postForObject(BITBUCKET_API_URL, request, String.class);
                ResponseEntity<BitbucketAuthResponse> accesstokenResponse = restTemplate.postForEntity(BITBUCKET_API_URL, request, BitbucketAuthResponse.class);
                
                
                System.out.println( restTemplate.postForEntity(BITBUCKET_API_URL, request, BitbucketAuthResponse.class).toString());
                
                System.out.println("accesstokenResponse"+accesstokenResponse.toString()+"");
                System.out.println("accesstokenResponse code"+accesstokenResponse.getStatusCode()+"");
            return accesstokenResponse.getBody();

            }
            authResponse.setError("BitbucketException");
            authResponse.setError_description("Could not get temporary code from Bitbucket API");
        } catch (Exception ex) {
            System.out.println("EX: "+ex.getMessage());
            authResponse.setError("JSONException");
            authResponse.setError_description(ex.getMessage());
        }
        return authResponse;
    }
    
    
    public static void main(String... args){
        BitbucketAuthHandler.requestAccesToken(new JSONObject().put("code", "kxveVek3rTMmcDQcD8"));
    }
}

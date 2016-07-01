package eu.cloudteams.util.bitbucket;

import org.json.JSONException;
import org.json.JSONObject;
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
                MultiValueMap<String, Object> parameteres = new LinkedMultiValueMap<>();
                //Set parameters of request
                parameteres.add("client_id", CLIENT_ID);
                parameteres.add("secret", CLIENT_SECRET);

                parameteres.add("code", jsonResponse.get("code"));
                RestTemplate restTemplate = new RestTemplate();
                //Make Rest call to fetch AccessToken
                ResponseEntity<BitbucketAuthResponse> accesstokenResponse = restTemplate.postForEntity(BITBUCKET_API_URL, parameteres, BitbucketAuthResponse.class);
                return accesstokenResponse.getBody();
            }
            authResponse.setError("BitbucketException");
            authResponse.setError_description("Could not get temporary code from Bitbucket API");
        } catch (JSONException ex) {
            authResponse.setError("JSONException");
            authResponse.setError_description(ex.getMessage());
        }
        return authResponse;
    }
}

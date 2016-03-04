package eu.cloudteams.util.github;

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
public final class GithubAuthHandler {

    // Production client id

    private static final String CLIENT_ID = "9aababac7a8ec6c9659e";

    // Development client id
//    private static final String CLIENT_ID = "413dd46351dc0c48f306";
    // Production client secret
    private static final String CLIENT_SECRET = "77ed672ccb348b6332cc112a55038724713bc839";

    // Development client secret
//    private static final String CLIENT_SECRET = "4fefd9a3d5111e6ec993bbf2958f15f2015da9c6";
    private static final String GITHUB_API_URL = "https://github.com/login/oauth/access_token";

    public static GithubAuthResponse requestAccesToken(JSONObject jsonResponse) {
        GithubAuthResponse authResponse = new GithubAuthResponse();
        try {
            //Check if GithHub temporary code is received
            if (jsonResponse.has("code")) {
                MultiValueMap<String, Object> parameteres = new LinkedMultiValueMap<>();
                //Set parameters of request
                parameteres.add("client_secret", CLIENT_SECRET);
                parameteres.add("client_id", CLIENT_ID);
                parameteres.add("code", jsonResponse.get("code"));
                RestTemplate restTemplate = new RestTemplate();
                //Make Rest call to fetch AccessToken
                ResponseEntity<GithubAuthResponse> accesstokenResponse = restTemplate.postForEntity(GITHUB_API_URL, parameteres, GithubAuthResponse.class);
                return accesstokenResponse.getBody();
            }
            authResponse.setError("GitHubException");
            authResponse.setError_description("Could not get temporary code from GitHub API");
        } catch (JSONException ex) {
            authResponse.setError("JSONException");
            authResponse.setError_description(ex.getMessage());
        }
        return authResponse;
    }
}

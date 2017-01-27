package eu.cloudteams.util.paasport;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import eu.cloudteams.util.paasport.models.TDeveloperProfile;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @author Spyros Mantzouratos
 */
public class PaaSportService {

    private final static Logger logger = Logger.getLogger(PaaSportService.class.getName());

    private final String paasportUrl;
    private final String projectKey;

    public PaaSportService(String paasportUrl, String paasportKey) {
        this.paasportUrl = StringUtils.trimTrailingCharacter(paasportUrl, "/".charAt(0));
        this.projectKey = paasportKey;
    }

    public PaaSportService(String paasportUrl) {
        this(paasportUrl, "");
    }

    public String login(String username, String password) {

        logger.info("PaaSport URL for login: " + paasportUrl + "/api/v1/auth/login");

        List<String> tokens = new ArrayList<>();
        String url = paasportUrl + "/api/v1/auth/login";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);

        ResponseEntity<String> response = new RestTemplate().postForEntity(url, jsonObject.toString(), String.class);

        if (response.getStatusCode().equals(HttpStatus.OK)) {

            response.getHeaders().entrySet().stream().forEach(keySet -> {

                keySet.getValue().stream().forEach(value -> {

                    if (keySet.getKey().equals("Authorization")) {
                        logger.info("Token: " + value);

                        tokens.add(value);

                    }

                });

            });

        }

        if (!tokens.isEmpty()) {
            return tokens.get(0);
        } else {
            return null;
        }

    }

    public TDeveloperProfile getUserProfile(String username) {

        TDeveloperProfile developerProfile = new TDeveloperProfile();

        String url = paasportUrl + "/api/v1/external/";

        logger.info("PaaSport URL for fetching user profile: " + url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization", projectKey);

        ResponseEntity<String> response = new RestTemplate().exchange(url +  username, HttpMethod.GET, new HttpEntity<byte[]>(headers), String.class);

        if (response.getStatusCode().equals(HttpStatus.OK)) {

            JSONObject object = new JSONObject(response.getBody());
            try {
                developerProfile = new ObjectMapper().readValue(object.get("returnobject").toString(), TDeveloperProfile.class);
                return developerProfile;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        } else {
            return null;
        }

    }

}
package eu.cloudteams.util.bitbucket;

import eu.cloudteams.util.bitbucket.models.Repository;
import eu.cloudteams.util.bitbucket.models.RepositoryResponse;
import eu.cloudteams.util.bitbucket.models.UserResponse;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public final class BitbucketService {

    private static final String REST_USER_URL = "https://api.bitbucket.org/2.0/user";

    private static final String REPOSITORIES_USER_URL = "https://api.bitbucket.org/2.0/repositories/%s";

    private final String ACCESS_TOKEN;

    public BitbucketService(String accessToken) {
        this.ACCESS_TOKEN = accessToken;

    }

    public Optional<RepositoryResponse> getRepositories(String username) {
        try {
            ResponseEntity<RepositoryResponse> response = new RestTemplate().exchange(String.format(REPOSITORIES_USER_URL, username), HttpMethod.GET, getHttpEntity(), RepositoryResponse.class);
            return Optional.ofNullable(response.getBody());
        } catch (Exception ex) {
        }
        return Optional.empty();
    }

    public Optional<Repository> getRepository(String username, String repository) {
        try {
            ResponseEntity<Repository> response = new RestTemplate().exchange(String.format(REPOSITORIES_USER_URL, username) + "/" + repository, HttpMethod.GET, getHttpEntity(), Repository.class);
            return Optional.ofNullable(response.getBody());
        } catch (Exception ex) {
        }
        return Optional.empty();
    }

    public Optional<UserResponse> getUser() {
        try {
            ResponseEntity<UserResponse> response = new RestTemplate().exchange(REST_USER_URL, HttpMethod.GET, getHttpEntity(), UserResponse.class);
            return Optional.ofNullable(response.getBody());
        } catch (Exception ex) {
        }
        return Optional.empty();
    }

    private String getAccessToken() {
        return "Bearer " + this.ACCESS_TOKEN;
    }

    private HttpEntity getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return entity;
    }

    public static void main(String... args) {

    }

}

package eu.cloudteams.github;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public class GithubException {
    
    private final String error;
    private final String error_description;
    private final String error_uri;

    public GithubException(String error, String error_description, String error_uri) {
        this.error = error;
        this.error_description = error_description;
        this.error_uri = error_uri;
    }

    public String getError() {
        return error;
    }

    public String getErrorDesription() {
        return error_description;
    }

    public String getErrorUri() {
        return error_uri;
    }
       
}

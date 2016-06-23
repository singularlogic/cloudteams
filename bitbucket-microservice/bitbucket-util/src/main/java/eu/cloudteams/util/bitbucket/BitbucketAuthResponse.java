package eu.cloudteams.util.bitbucket;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public final class BitbucketAuthResponse {

    private String access_token;
    private String scope;
    private String token_type;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public String getError_uri() {
        return error_uri;
    }

    public void setError_uri(String error_uri) {
        this.error_uri = error_uri;
    }

    private String error;
    private String error_description;
    private String error_uri;

    public BitbucketAuthResponse() {

    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getScope() {
        return scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public boolean isValid() {
        return null == error;
    }
}

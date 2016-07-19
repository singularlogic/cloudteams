package eu.cloudteams.util.bitbucket;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public final class BitbucketAuthResponse {

    private String access_token;
    private String scopes;
    private String token_type;
    private String refresh_token;
    //private Integer expires_in;

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
/*
    public String getError_uri() {
        return error_uri;
    }

    public void setError_uri(String error_uri) {
        this.error_uri = error_uri;
    }
*/
    private String error;
    private String error_description;
   // private String error_uri;

    public BitbucketAuthResponse() {

    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setScope(String scope) {
        this.scopes = scope;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getScope() {
        return scopes;
    }

    public String getToken_type() {
        return token_type;
    }

    public boolean isValid() {
        return null == error;
    }

    /**
     * @return the refresh_token
     */
    public String getRefresh_token() {
        return refresh_token;
    }

    /**
     * @param refresh_token the refresh_token to set
     */
    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}

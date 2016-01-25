package eu.cloudteams.github;

import java.util.Optional;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public final class GithubAuthResponse {

    private  String access_token;
    private String scope;
    private  String token_type;

    private Optional<GithubException> exception = Optional.empty();

    public GithubAuthResponse(String access_token, String scope, String token_type, GithubException exception) {
        this.access_token = access_token;
        this.scope = scope;
        this.token_type = token_type;
        this.exception = Optional.ofNullable(exception);
    }

    public GithubAuthResponse() {

    }

    public String getAccessToken() {
        return access_token;
    }

    public String getScope() {
        return scope;
    }

    public String getTokenType() {
        return token_type;
    }

    public Optional<GithubException> getException() {
        return exception;
    }

    public boolean isValid() {
        return !this.exception.isPresent();
    }

    public void setException(Optional<GithubException> exception) {
        this.exception = exception;
    }

}

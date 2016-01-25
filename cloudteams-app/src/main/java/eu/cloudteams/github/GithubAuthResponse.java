package eu.cloudteams.github;

import java.util.Optional;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public final class GithubAuthResponse {

    private final String accessToken;
    private final String scope;
    private final String tokenType;

    private final Optional<GithubException> exception;

    public GithubAuthResponse(String accessToken, String scope, String tokenType, GithubException exception) {
        this.accessToken = accessToken;
        this.scope = scope;
        this.tokenType = tokenType;
        this.exception = Optional.ofNullable(exception);

    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getScope() {
        return scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Optional<GithubException> getException() {
        return exception;
    }

    public boolean isValid() {
        return !this.exception.isPresent();
    }

}

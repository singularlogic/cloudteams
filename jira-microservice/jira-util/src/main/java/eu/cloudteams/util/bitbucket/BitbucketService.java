package eu.cloudteams.util.bitbucket;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
import org.eclipse.egit.github.core.service.CollaboratorService;
import org.eclipse.egit.github.core.service.LabelService;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public final class BitbucketService {

    //GitHubClient
    private final GitHubClient bitbucketClient = new GitHubClient();
    private final UserService userService;
    private final RepositoryService repositoryService;
    private final LabelService labelService;
    private final CommitService commitService;
    private final CollaboratorService collaboratorService;

    public BitbucketService(String accessToken) {
        bitbucketClient.setOAuth2Token(accessToken);
        userService = new UserService(bitbucketClient);
        repositoryService = new RepositoryService(bitbucketClient);
        labelService = new LabelService(bitbucketClient);
        commitService = new CommitService(bitbucketClient);
        collaboratorService = new CollaboratorService(bitbucketClient);

    }

    public CollaboratorService getCollaboratorService() {
        return collaboratorService;
    }

    public LabelService getLabelService() {
        return this.labelService;
    }

    public CommitService getCommitService() {
        return this.commitService;
    }

    public UserService getUserService() {
        return this.userService;
    }

    public RepositoryService getBitbucketRepositoryService() {
        return this.repositoryService;
    }

}

package eu.cloudteams.util.github;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public final class GithubService {

    //GitHubClient
    private final GitHubClient githubClient = new GitHubClient();
    private final static Logger logger = Logger.getLogger(GithubService.class.getName());
    private final UserService userService;
    private final RepositoryService githubRepositoryService;
    
    public GithubService(String accessToken) {
        githubClient.setOAuth2Token(accessToken);
        userService = new UserService(githubClient);
        githubRepositoryService = new RepositoryService(githubClient);
    }
    
    public UserService getUserService() {
        return this.userService;
    }
    public RepositoryService getGithubRepositoryService() {
        return this.githubRepositoryService;
    }
    
    public void GitHubInfo() {
        logger.log(Level.INFO, "GitHub user is: {0}", this.githubClient.getUser());
    }
    
    public static void main(String[] args) throws IOException {
        GithubService github = new GithubService("f54ea306a9fcb97614ddc876ac4c28956970e428");
        System.out.println("Get login name:  " + github.getUserService().getUser().getLogin());
        RepositoryService repoService = new RepositoryService(github.githubClient);
        
        repoService.getRepositories().forEach(repo -> System.out.println(repo.getName()));
        System.out.println("Total repos: "+ repoService.getRepositories().size());
        
    }
}

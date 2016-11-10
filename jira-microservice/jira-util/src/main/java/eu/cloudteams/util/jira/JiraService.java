package eu.cloudteams.util.jira;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.auth.AnonymousAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.google.common.collect.Lists;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public final class JiraService {

    private final String JIRA_URL;
    private JiraRestClient restClient;

    private JiraService(String jiraURL) {
        this.JIRA_URL = jiraURL;
    }

    public static JiraService create(String jiraUrl) {
        return new JiraService(jiraUrl);
    }

    public JiraService authenticateAnonymous() throws URISyntaxException {
        restClient = new AsynchronousJiraRestClientFactory().create(new URI(JIRA_URL), new AnonymousAuthenticationHandler());
        return this;
    }

    public JiraService authenticateBasic(String username, String password) throws URISyntaxException {
        restClient = new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(new URI(JIRA_URL), username, password);
        return this;
    }

    public List<BasicProject> getProjects() {
        return Lists.newArrayList(restClient.getProjectClient().getAllProjects().claim());
    }

    public static void main(String... args) {
        try {
            new JiraService("https://cloudteams.atlassian.net").authenticateAnonymous().getProjects().forEach(project -> System.out.println(project.getName()));
        } catch (URISyntaxException ex) {
            Logger.getLogger(JiraService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

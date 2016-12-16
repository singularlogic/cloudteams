package eu.cloudteams.util.jira;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.auth.AnonymousAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.google.common.collect.Lists;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    public Optional<Project> getProject(String project) {
        try {
            return Optional.ofNullable(restClient.getProjectClient().getProject(project).get());
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(JiraService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Optional.empty();
    }

    public List<Issue> getIssues(String projectName) {
        return Lists.newArrayList(restClient.getSearchClient().searchJql(String.format("project=%s", projectName)).claim().getIssues());
    }

    public static void main(String... args) {
        try {
            JiraService jiraService = new JiraService("https://cloudteams.atlassian.net").authenticateAnonymous();

            //Get Project
            jiraService.getProjects().forEach(project -> System.out.println("Project name: " + project.getName()));

            //Get Issues
           Map<String,Long>  myMap = jiraService.getIssues("CloudTeams").stream().collect(Collectors.groupingBy(issue-> issue.getIssueType().getName() ,Collectors.counting()));
            
           
           myMap.keySet().forEach( type -> System.out.println(type + " count: "+ myMap.get(type)));
            
            
            

        } catch (URISyntaxException ex) {
            Logger.getLogger(JiraService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

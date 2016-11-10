package eu.cloudteams.controller;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.nimbusds.jose.JOSEException;
import eu.cloudteams.authentication.jwt.Token;
import eu.cloudteams.authentication.jwt.TokenHandler;
import static eu.cloudteams.controller.WebController.getCurrentUser;
import eu.cloudteams.repository.domain.JiraProject;
import eu.cloudteams.repository.domain.JiraUser;
import eu.cloudteams.repository.service.ProjectService;
import eu.cloudteams.repository.service.UserService;
import eu.cloudteams.util.jira.JiraService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Restful API for integration with CloudTeams Platform
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class JiraController {

    private static final Logger logger = Logger.getLogger(JiraController.class.getName());

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @CrossOrigin
    @RequestMapping(value = "/api/v1/jira/repository", method = RequestMethod.POST)
    public String getJiraProjectInfo(Model model, @RequestParam(value = "project_id", defaultValue = "0", required = true) int project_id) throws IOException {

        logger.info("Requesting info for repository assigned to project_id: " + project_id);

        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing jira sign-in fragment");
            //return jira-signin fragment
            return "jira::jira-no-auth";
        }

        JiraUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());
        JiraProject project = projectService.findByProjectIdAndUser(project_id, user);

        JiraService jiraService;
        try {
            jiraService = JiraService.create(user.getJiraUrl()).authenticateAnonymous();
        } catch (URISyntaxException ex) {
            Logger.getLogger(JiraController.class.getName()).log(Level.SEVERE, null, ex);
            return "jira::jira-error";
        }

        //Unassigned project
        if (null == project) {
            model.addAttribute("jiraProjects", jiraService.getProjects());
            return "jira::jira-no-project";
        }

        logger.info("Returning jira-info fragment for user:  " + getCurrentUser().getPrincipal() + " and project_id: " + project_id);

        try {
            jiraService = JiraService.create(user.getJiraUrl()).authenticateAnonymous();

        } catch (URISyntaxException ex) {
            Logger.getLogger(JiraController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Optional<Project> jiraProject = jiraService.getProject(project.getJiraProject());

        if (jiraProject.isPresent()) {
            //Get Jira Project
            model.addAttribute("jiraProject", jiraProject.get());
            List<Issue> issues = jiraService.getIssues(jiraProject.get().getName());
            //Get Issues Types    
            model.addAttribute("issuesTypes", issues.stream().collect(Collectors.groupingBy(issue -> issue.getIssueType().getName(), Collectors.counting())));
            //Get Issues Priorities
            model.addAttribute("issuesPriority", issues.stream().collect(Collectors.groupingBy(issue -> issue.getPriority().getName(), Collectors.counting())));
            //Get Issues Statuses
            model.addAttribute("issuesStatus", issues.stream().collect(Collectors.groupingBy(issue -> issue.getStatus().getName(), Collectors.counting())));
            return "Jira::Jira-auth-project";
        }
        return "jira::jira-error";
    }

    //Rest Controller
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/auth/token", method = RequestMethod.POST)
    public String getJWT(@RequestParam(value = "username", required = true) String username, @RequestParam(value = "jiraUrl", required = true) String jiraUrl, HttpServletRequest request) throws IOException {

        //Check if sonar service is running on the given url
//        if (!new JiraService(sonarUrl).getServerInfo().isPresent()) {
//            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Jira service is not available in url: " + sonarUrl).toString();
//        }
        //Check if user already exists
        JiraUser user = userService.findByUsername(username);

        //Print the status of user
        logger.info(null != user ? "User: " + user.getUsername() + " already exists with id: " + user.getId() : "Creating new user for username: " + username);

        jiraUrl = StringUtils.trimTrailingCharacter(jiraUrl, "/".charAt(0));
        if (null == user) {
            user = new JiraUser(null, username, "", jiraUrl, true);
        } else if (user.getJiraUrl().equalsIgnoreCase(jiraUrl) == false) {
            logger.log(Level.SEVERE, "You do not have access to this project");
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "You do not have access to this project").toString();
        }
        //Update/Set sonar url
        //user.setJiraUrl(sonarUrl);

        //If create new user was not success return
        if (false == userService.storeUser(user)) {
            logger.log(Level.SEVERE, "Could not store user: " + username + " to database.");
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Could not create user " + username + "  to Cloudteams Database ").toString();
        }

        //Create Access Token
        try {

            Token generatedToken = TokenHandler.createToken(request.getRemoteAddr(), username);
            //Save User
            user.setAccessToken(generatedToken.getToken());
        } catch (JOSEException ex) {
            Logger.getLogger(JiraController.class.getName()).log(Level.SEVERE, null, ex);
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Could not create access token for user: " + username).toString();
        }

        //Print the generated token
        logger.log(Level.INFO, "Generated Token: {0}", user.getAccessToken());
        //Success created user and accesstoken
        return new JSONObject().put("code", MESSAGES.SUCCESS).put("message", "Token has been created successfully!").put("token", "Bearer " + user.getAccessToken()).toString();

    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/jira/add", method = RequestMethod.POST)
    public String JiraAuth(Model model, @RequestParam(value = "project_id", defaultValue = "") long project_id, @RequestParam(value = "projectName", defaultValue = "") String projectName) {

        //Check if user is authenticated
        if (!WebController.hasAccessToken()) {
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "User not authenticated.").toString();
        }

        //Check if project is empty
        if (projectName.isEmpty()) {
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Project name is empty.").toString();
        }

        //Check if user exists
        JiraUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());

        //Check if user exists
        if (null == user) {
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Could not find user: " + getCurrentUser().getPrincipal().toString() + " to database.").toString();
        }

        //Check if project exists
        JiraProject project = projectService.findOne(project_id);

        //Create project
        if (null == project) {
            project = new JiraProject();
            project.setUser(user);
            project.setProjectId(project_id);
            project.setJiraProject(projectName);
            projectService.store(project);
        }

        return new JSONObject().put("code", MESSAGES.SUCCESS).put("message", "Project: " + projectName + " has been assigned!").toString();
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/jira/delete", method = RequestMethod.POST)
    public String unassignJiraProject(Model model, @RequestParam(value = "project_id", defaultValue = "0", required = true) int project_id) throws IOException {

        logger.info("Requesting unassign Jira project for project_id: " + project_id);

        //Check if user is authenticated
        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing jira sigin fragment");
            //return jira-signin fragment
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "User is not authenticated.").toString();
        }

        //Fetch the actual user based on JWT
        JiraUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());

        //Check if user exists
        if (null == user) {
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Could not find user: " + getCurrentUser().getPrincipal().toString() + " to database.").toString();
        }

        //Check if deletion is success
        if (0 == projectService.deleteByProjectIdAndUser(project_id, user)) {
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Could not unassign project.").toString();
        }

        return new JSONObject().put("code", MESSAGES.SUCCESS).put("message", "Project has been unassigned").toString();
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/jira/disconnect", method = RequestMethod.POST)
    public String disconnectJiraProject(Model model, @RequestParam(value = "project_id", defaultValue = "0", required = true) int project_id) throws IOException {

        logger.info("Requesting disconnect Jira project for project_id: " + project_id);

        //Check if user is authenticated
        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing jira sigin fragment");
            //return jira-signin fragment
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "User is not authenticated.").toString();
        }

        //Fetch the actual user based on JWT
        JiraUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());

        try {
            userService.deleteUser(user.getId());
        } catch (Exception ex) {
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Could not disconnect account.").toString();
        }

        return new JSONObject().put("code", MESSAGES.SUCCESS).put("message", "Account has been disconnected").toString();
    }

    private final class MESSAGES {

        final static String SUCCESS = "SUCCESS";
        final static String FAIL = "FAIL";

    }
}

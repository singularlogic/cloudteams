package eu.cloudteams.controller;

import static eu.cloudteams.controller.WebController.getCurrentUser;
import com.nimbusds.jose.JOSEException;
import eu.cloudteams.authentication.jwt.Token;
import eu.cloudteams.authentication.jwt.TokenHandler;
import eu.cloudteams.repository.domain.GithubProject;
import eu.cloudteams.repository.domain.GithubUser;
import eu.cloudteams.repository.service.ProjectService;
import eu.cloudteams.repository.service.UserService;
import eu.cloudteams.util.github.GithubAuthHandler;
import eu.cloudteams.util.github.GithubAuthResponse;
import eu.cloudteams.util.github.GithubService;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.egit.github.core.Repository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class GithubController {

    private static final Logger logger = Logger.getLogger(GithubController.class.getName());

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @RequestMapping(value = "/api/v1/github/auth", method = RequestMethod.GET)
    public String githubAuth(Model model, @RequestParam(value = "code", defaultValue = "") String code, @RequestParam(value = "username", defaultValue = "") String username, HttpServletRequest request, HttpServletResponse response) throws IOException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("username", username);

        logger.log(Level.INFO, "Request from ip: {0} Requestbody: {1}", new Object[]{request.getRemoteAddr(), jsonObject});

        //Actual request to GitHub API
        GithubAuthResponse gitAuthResponse = GithubAuthHandler.requestAccesToken(jsonObject);

        //Check if AccessToken is successfully fetched
        if (false == gitAuthResponse.isValid()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, gitAuthResponse.getError());
            logger.log(Level.SEVERE, "Fail to get Acess Token reason: {0}", gitAuthResponse.getError());
            return null;
        }

        //Check if user already exists
        GithubUser user = userService.findByUsername(username);

        //Print the status of user
        logger.info(null != user ? "User: " + user.getUsername() + " already exists with id: " + user.getId() : "Creating new user for username: " + username);

        //If create new user was not success return
        if (null == user && false == userService.storeUser(user = new GithubUser(null, username, "", gitAuthResponse.getAccess_token(), true))) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not create user to Cloudteams Database...");
            logger.log(Level.SEVERE, "Fail to get Acess Token reason: {0}", gitAuthResponse.getError());
            return null;
        }

        //Is first time ignore this
        if (!user.getAccessToken().isEmpty()) {            
            String userLogin = new GithubService(user.getGithubToken()).getUserService().getUser().getLogin();
            String userLoginToValidate = new GithubService(gitAuthResponse.getAccess_token()).getUserService().getUser().getLogin();
            if (userLogin.equalsIgnoreCase(userLoginToValidate)) {
                userService.setSynchStatus(true, user.getId());
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "");
                logger.log(Level.SEVERE, "Fail to get Acess Token reason: {0}", "User does not have access to this project");
                return null;
            }
        }

        //Create Access Token
        Token generatedToken = new Token();
        try {
            generatedToken = TokenHandler.createToken(request.getRemoteAddr(), username);
            //Save User
            user.setAccessToken(generatedToken.getToken());
        } catch (JOSEException ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not create Access Token...");
            Logger.getLogger(GithubController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Update user data
        if (false == userService.storeUser(user)) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not update user to Cloudteams Database...");
            logger.log(Level.SEVERE, "Fail to get Acess Token reason: {0}", "Could not update user to Cloudteams Database...");
            return null;
        }

        //Print the generated token
        logger.info("Generated Token: " + generatedToken.getToken());

        return "github::github-authentication";

    }

    @CrossOrigin
    @RequestMapping(value = "/api/v1/github/repository", method = RequestMethod.POST)
    public String getGithubRepositoryInfo(Model model, @RequestParam(value = "project_id", defaultValue = "0", required = true) int project_id) throws IOException {

        logger.info("Requesting info for repository assigned to project_id: " + project_id);

        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing github sigin fragment");
            //return github-signin fragment
            return "github::github-no-auth";
        }

        GithubUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());
        GithubProject project = projectService.findByProjectIdAndUser(project_id, user);

        GithubService github = new GithubService(user.getGithubToken());
        //Unassigned project
        if (null == project) {
            model.addAttribute("GetRepositories", github.getGithubRepositoryService().getRepositories());
            return "github::github-no-project";
        }

        logger.info("Returning github-info fragment for user:  " + getCurrentUser().getPrincipal() + " and project_id: " + project_id);

        Optional<Repository> repository = github.getGithubRepositoryService().getRepositories().stream().filter(repositoryTofind -> repositoryTofind.getName().equals(project.getGithubRepository())).findFirst();

        if (repository.isPresent()) {
            //Generate github statistics
            GithubStatisticsTO githubStatistics = new GithubStatisticsTO(github, repository.get());
            model.addAttribute("githubStats", githubStatistics);
            return "github::github-auth-project";
        }

        return "github::github-error";
    }

    //Rest Controller
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/auth/token", method = RequestMethod.POST)
    public String getJWT(@RequestParam(value = "username", required = true) String username) {

        JSONObject response = new JSONObject();
        GithubUser user;
        long waitingTime = 2000; //two seconds
        for (int cycle = 0; cycle < 60; cycle++) {
            try {
                logger.info("[GitHub Synchronization T#" + Thread.currentThread().getId() + "] synchronization cycle " + (cycle + 1) + " is in process for username: " + username);
                //Wait for some time
                Thread.sleep(waitingTime);
                //Fetch user
                user = userService.findByUsername(username);
                //Check if the user is created and a token is found
                if (null != user && !user.getAccessToken().isEmpty() && user.getIsSynch()) {
                    logger.info("[GitHub Synchronization T#" + Thread.currentThread().getId() + "] found JWT for user: " + username + " , synchronization success");
                    userService.setSynchStatus(false, user.getId());
                    response.put("token", "Bearer " + user.getAccessToken());
                    response.put("code", "SUCCESS");
                    return response.toString();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(GithubController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        logger.info("[GitHub Synchronization T#" + Thread.currentThread().getId() + "] could not found user:  " + username + " in database , synchronization failed");

        //Token not found return error message
        response.put("code", MESSAGES.FAIL);
        response.put("message", "Could not find access token for user: " + username);

        return response.toString();
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/github/add", method = RequestMethod.POST)
    public String githubAuth(Model model, @RequestParam(value = "project_id", defaultValue = "") long project_id, @RequestParam(value = "reponame", defaultValue = "") String githubRepository) {

        JSONObject jsonObject = new JSONObject();

        if (!WebController.hasAccessToken()) {
            jsonObject.put("code", MESSAGES.FAIL);
            jsonObject.put("message", "User is not authorized");
            return jsonObject.toString();
        }

        if (githubRepository.isEmpty()) {
            jsonObject.put("code", MESSAGES.FAIL);
            jsonObject.put("message", "Repository name is empty.");
            return jsonObject.toString();
        }

        GithubUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());

        if (null == user) {
            jsonObject.put("code", MESSAGES.FAIL);
            jsonObject.put("message", "User does not exist");
            return jsonObject.toString();
        }

        GithubProject project = projectService.findOne(project_id);

        if (null == project) {
            project = new GithubProject();
            project.setUser(user);
            project.setProjectId(project_id);
            project.setGithubRepository(githubRepository);
            projectService.store(project);
        }

        jsonObject.put("code", MESSAGES.SUCCESS);
        jsonObject.put("message", "Repository: " + githubRepository + " has been assigned!");

        return jsonObject.toString();
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/github/delete", method = RequestMethod.POST)
    public String unassignGithubRepository(Model model, @RequestParam(value = "project_id", defaultValue = "0", required = true) int project_id) throws IOException {

        logger.info("Requesting unassign github repository for project_id: " + project_id);

        //Check if user is authenticated
        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing github sigin fragment");
            //return github-signin fragment
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "User is not authenticated.").toString();
        }

        //Fetch the actual user based on JWT
        GithubUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());

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

    private final class MESSAGES {

        final static String SUCCESS = "SUCCESS";
        final static String FAIL = "FAIL";

    }

}

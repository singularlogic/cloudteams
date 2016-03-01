package eu.cloudteams.controller;

import eu.cloudteams.authentication.jwt.Token;
import com.nimbusds.jose.JOSEException;
import eu.cloudteams.authentication.jwt.TokenHandler;
import static eu.cloudteams.controller.WebController.getCurrentUser;
import eu.cloudteams.repository.domain.User;
import eu.cloudteams.repository.service.UserService;
import eu.cloudteams.util.github.GithubAuthHandler;
import eu.cloudteams.util.github.GithubAuthResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.cloudteams.util.github.GithubService;
import org.eclipse.egit.github.core.service.GitHubService;
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

    @RequestMapping(value = "/github/auth", method = RequestMethod.GET)
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
        User user = userService.findByUsername(username);

        //Print the status of user
        logger.info(null != user ? "User: " + user.getUsername() + " already exists with id: " + user.getId() : "Creating new user for username: " + username);

        //If create new user was not success return
        if (null == user && false == userService.storeUser(user = new User(null, username, "", gitAuthResponse.getAccess_token(), true))) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not create user to Cloudteams Database...");
            logger.log(Level.SEVERE, "Fail to get Acess Token reason: {0}", gitAuthResponse.getError());
            return null;
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
    @RequestMapping(value = "/github/repository", method = RequestMethod.POST)
    public String getGithubRepositoryInfo(Model model, @RequestParam(value = "project_id", defaultValue = "0", required = true) int project_id) throws IOException{

        logger.info("Requesting info for repository assigned to project_id: " + project_id);

        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing github sigin fragment");
            //return github-signin fragment
            return "github::github-no-auth";
        }

        logger.info("Returning github-info fragment for user:  " + getCurrentUser().getPrincipal() + " and project_id: " + project_id);

        //return github-repo with info
        User user = userService.findByUsername(getCurrentUser().getPrincipal().toString());
        GithubService github = new GithubService(user.getGithubToken());

        model.addAttribute("GetRepositories", github.getGithubRepositoryService().getRepositories());

        return "github::github-no-project";
    }

    //Rest Controller
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/auth/token", method = RequestMethod.POST)
    public String getJWT(@RequestParam(value = "username", required = true) String username) {

        JSONObject response = new JSONObject();
        User user;
        long waitingTime = 2000; //two seconds
        for (int cycle = 0; cycle < 10; cycle++) {
            try {
                logger.info("[GitHub Synchronization T#" + Thread.currentThread().getId() + "] synchronization cycle " + (cycle + 1) + " is in process for username: " + username);
                //Wait for some time
                Thread.sleep(waitingTime);
                //Fetch user
                user = userService.findByUsername(username);
                //Check if the user is created and a token is found
                if (null != user && !user.getAccessToken().isEmpty()) {
                    logger.info("[GitHub Synchronization T#" + Thread.currentThread().getId() + "] found JWT for user: " + username + " , synchronization success");
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
        response.put("code", "FAIL");
        response.put("message", "Could not find access token for user: " + username);

        return response.toString();
    }

}

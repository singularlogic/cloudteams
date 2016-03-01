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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Restful API for integration with CloudTeams Platform
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class GithubController {

    private static final Logger logger = Logger.getLogger(WebController.class.getName());

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
        //Create a new user
        User tmpUser = new User(null, "", "", gitAuthResponse.getAccess_token(), true);

        //If create new user was not success return
        if (false == userService.storeUser(tmpUser)) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not create user to Cloudteams Database...");
            logger.log(Level.SEVERE, "Fail to get Acess Token reason: {0}", gitAuthResponse.getError());
            return null;
        }

        //Create Access Token
        Token generatedToken = new Token();
        try {
            generatedToken = TokenHandler.createToken(request.getRemoteAddr(), tmpUser.getId());
            //Save User
            tmpUser.setAccessToken(generatedToken.getToken());
            tmpUser.setUsername(String.valueOf(tmpUser.getId()));
        } catch (JOSEException ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not create Access Token...");
            Logger.getLogger(GithubController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Update user data
        if (false == userService.storeUser(tmpUser)) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not update user to Cloudteams Database...");
            logger.log(Level.SEVERE, "Fail to get Acess Token reason: {0}", "Could not update user to Cloudteams Database...");
            return null;
        }

        logger.info("Generated Token: " + generatedToken.getToken());

        return "github::success-authentication";

    }

    @RequestMapping(value = "/github/repository", method = RequestMethod.GET)
    public String getGithubRepositoryInfo(Model model, @RequestParam(value = "project_id", defaultValue = "0", required = true) int project_id) {

        logger.info("Requesting info for repository assigned to project_id: " + project_id);

        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing github sigin fragment");
            //return github-signin fragment
            return "index";
        }

        logger.info("Returning github-info fragment for user:  " + getCurrentUser().getPrincipal() + " and project_id: " + project_id);

        //return github-repo with info
        return "index";
    }

//Rest Controller
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/auth/token", method = RequestMethod.POST)
    public String getJWT(@RequestParam(value = "username", required = true) String username) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(GithubController.class.getName()).log(Level.SEVERE, null, ex);
        }
        logger.info("Username is " + username);

        JSONObject tokenObj = new JSONObject();
        tokenObj.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1MyIsImlzcyI6IjE5Mi4xNjguNy4xMzEiLCJleHAiOjE0NTc5Njg5MjUsImlhdCI6MTQ1Njc1OTMyNX0.mZ8tBvoaMWQqVJ2Vt4Q3UZMVVrsILCwNm3D-5liaEtQ");
        tokenObj.put("code", "SUCCESS");
        return tokenObj.toString();
    }

    @Deprecated
    @RequestMapping(value = "/auth/github", method = RequestMethod.POST)
    public Token githubAuth(@RequestBody String requestbody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.log(Level.INFO, "Request from ip: {0} Requestbody: {1}", new Object[]{request.getRemoteAddr(), requestbody});

        //Actual request to GitHub API
        GithubAuthResponse gitAuthResponse = GithubAuthHandler.requestAccesToken(new JSONObject(requestbody));

        //Check if AccessToken is successfully fetched
        if (false == gitAuthResponse.isValid()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, gitAuthResponse.getError());
            logger.log(Level.SEVERE, "Fail to get Acess Token reason: {0}", gitAuthResponse.getError());
            return null;
        }
        //Create a new user
        User tmpUser = new User(null, "", "", gitAuthResponse.getAccess_token(), true);

        //If create new user was not success return
        if (false == userService.storeUser(tmpUser)) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not create user to Cloudteams Database...");
            logger.log(Level.SEVERE, "Fail to get Acess Token reason: {0}", gitAuthResponse.getError());
            return null;
        }

        //Create Access Token
        Token generatedToken = new Token();
        try {
            generatedToken = TokenHandler.createToken(request.getRemoteAddr(), tmpUser.getId());
            //Save User
            tmpUser.setAccessToken(generatedToken.getToken());
            tmpUser.setUsername(String.valueOf(tmpUser.getId()));
        } catch (JOSEException ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not create Access Token...");
            Logger.getLogger(GithubController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Update user data
        if (false == userService.storeUser(tmpUser)) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not update user to Cloudteams Database...");
            logger.log(Level.SEVERE, "Fail to get Acess Token reason: {0}", "Could not update user to Cloudteams Database...");
            return null;
        }

        return generatedToken;
    }

}

package eu.cloudteams.controller;

import static eu.cloudteams.controller.WebController.getCurrentUser;
import com.nimbusds.jose.JOSEException;
import eu.cloudteams.authentication.jwt.Token;
import eu.cloudteams.authentication.jwt.TokenHandler;
import eu.cloudteams.repository.domain.BitbucketProject;
import eu.cloudteams.repository.domain.BitbucketUser;
import eu.cloudteams.repository.service.ProjectService;
import eu.cloudteams.repository.service.UserService;
import eu.cloudteams.util.bitbucket.BitbucketAuthHandler;
import eu.cloudteams.util.bitbucket.BitbucketAuthResponse;
import eu.cloudteams.util.bitbucket.BitbucketService;
import eu.cloudteams.util.bitbucket.models.Repository;
import eu.cloudteams.util.bitbucket.models.RepositoryResponse;
import eu.cloudteams.util.bitbucket.models.UserResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class BitbucketController {

    private static final Logger logger = Logger.getLogger(BitbucketController.class.getName());

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @RequestMapping(value = "/api/v1/bitbucket/auth", method = RequestMethod.GET)
    public String bitbucketAuth(Model model, @RequestParam(value = "code", defaultValue = "") String code, @RequestParam(value = "username", defaultValue = "") String username, HttpServletRequest request, HttpServletResponse response) throws IOException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("username", username);

        logger.log(Level.INFO, "Request from ip: {0} Requestbody: {1}", new Object[]{request.getRemoteAddr(), jsonObject});

        //Actual request to GitHub API
        BitbucketAuthResponse bitbucketAuthResponse = BitbucketAuthHandler.requestAccesToken(jsonObject);

        logger.log(Level.INFO, "response error:{0}", bitbucketAuthResponse.getError() + " description " + bitbucketAuthResponse.getError_description());

        //Check if AccessToken is successfully fetched
        if (false == bitbucketAuthResponse.isValid()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, bitbucketAuthResponse.getError());
            logger.log(Level.SEVERE, "Fail to get Acess Token reason: {0}", bitbucketAuthResponse.getError());
            return null;
        }

        logger.log(Level.INFO, "response error:{0}", bitbucketAuthResponse.getError() + " description " + bitbucketAuthResponse.getError_description());
        logger.log(Level.INFO, "response token:{0}", bitbucketAuthResponse.getAccess_token());

        //Check if user already exists
        BitbucketUser user = userService.findByUsername(username);

        //Print the status of user
        logger.info(null != user ? "User: " + user.getUsername() + " already exists with id: " + user.getId() : "Creating new user for username: " + username);

        //If create new user was not success return
        if (null == user && false == userService.storeUser(user = new BitbucketUser(null, username, "", bitbucketAuthResponse.getAccess_token(), true))) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not create user to Cloudteams Database...");
            logger.log(Level.SEVERE, "Fail to get Acess Token reason: {0}", bitbucketAuthResponse.getError());
            return null;
        }

        //Is first time ignore this
        if (!user.getAccessToken().isEmpty()) {
            String userLogin;
            try {
                userLogin = new BitbucketService(user.getBitbucketToken()).getUser().orElseThrow(() -> new Exception("Could not fetch user via bitbucket toekn")).getUsername();
            } catch (Throwable ex) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "");
                logger.log(Level.SEVERE, "Fail to get Acess Token reason: Could not fetch bitbucket user");
                return null;
            }
            String userLoginToValidate;
            try {
                userLoginToValidate = new BitbucketService(bitbucketAuthResponse.getAccess_token()).getUser().orElseThrow(() -> new Exception("Could not fetch user via auth token")).getUsername();
            } catch (Exception ex) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "");
                logger.log(Level.SEVERE, "Fail to get Acess Token reason: Could not fetch bitbucket user via oauth token");
                return null;
            }
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
            Logger.getLogger(BitbucketController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Update user data
        if (false == userService.storeUser(user)) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not update user to Cloudteams Database...");
            logger.log(Level.SEVERE, "Fail to get Acess Token reason: {0}", "Could not update user to Cloudteams Database...");
            return null;
        }

        //Print the generated token
        logger.info("Generated Token: " + generatedToken.getToken());

        return "bitbucket::bitbucket-authentication";

    }

    @CrossOrigin
    @RequestMapping(value = "/api/v1/bitbucket/repository", method = RequestMethod.POST)
    public String getGithubRepositoryInfo(Model model, @RequestParam(value = "project_id", defaultValue = "0", required = true) int project_id) throws IOException {

        logger.info("Requesting info for repository assigned to project_id: " + project_id);

        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing Bitbucket sigin fragment");
            //return bitbucket-signin fragment
            return "bitbucket::bitbucket-no-auth";
        }

        BitbucketUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());
        BitbucketProject project = projectService.findByProjectIdAndUser(project_id, user);

        BitbucketService bitbucketService = new BitbucketService(user.getBitbucketToken());
        Optional<UserResponse> userResponse = bitbucketService.getUser();

        if (!userResponse.isPresent()) {
            logger.warning("Could not get bitbucket user");
            return "bitbucket::bitbucket-error";
        }

        //Unassigned project
        if (null == project) {

            Optional<RepositoryResponse> repositoryResponse = bitbucketService.getRepositories(userResponse.get().getUsername());

            if (!repositoryResponse.isPresent()) {
                logger.warning("Could not get repositories for user " + userResponse.get().getUsername());
                return "bitbucket::bitbucket-error";
            }

            model.addAttribute("GetRepositories", repositoryResponse.get());

            return "bitbucket::bitbucket-no-project";
        }

        logger.info("Returning bitbucket-info fragment for user:  " + getCurrentUser().getPrincipal() + " and project_id: " + project_id);

        Optional<Repository> repository = bitbucketService.getRepository(userResponse.get().getUsername(), project.getBitbucketRepository());

        if (repository.isPresent()) {

            //Generate bitbucket statistics
            BitbucketStatisticsTO bitbucketStatistics = new BitbucketStatisticsTO(bitbucketService, repository.get());
            model.addAttribute("bitbucketStats", bitbucketStatistics);
            return "bitbucket::bitbucket-auth-project";
        }

        return "bitbucket::bitbucket-error";
    }

    //Rest Controller
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/auth/token", method = RequestMethod.POST)
    public String getJWT(@RequestParam(value = "username", required = true) String username) {

        JSONObject response = new JSONObject();
        BitbucketUser user;
        long waitingTime = 2000; //two seconds
        for (int cycle = 0; cycle < 60; cycle++) {
            try {
                logger.info("[Bitbucket Synchronization T#" + Thread.currentThread().getId() + "] synchronization cycle " + (cycle + 1) + " is in process for username: " + username);
                //Wait for some time
                Thread.sleep(waitingTime);
                //Fetch user
                user = userService.findByUsername(username);
                //Check if the user is created and a token is found
                if (null != user && !user.getAccessToken().isEmpty() && user.getIsSynch()) {
                    logger.info("[Bitbucket Synchronization T#" + Thread.currentThread().getId() + "] found JWT for user: " + username + " , synchronization success");
                    userService.setSynchStatus(false, user.getId());
                    response.put("token", "Bearer " + user.getAccessToken());
                    response.put("code", "SUCCESS");
                    return response.toString();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(BitbucketController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        logger.info("[Bitbucket Synchronization T#" + Thread.currentThread().getId() + "] could not found user:  " + username + " in database , synchronization failed");

        //Token not found return error message
        response.put("code", MESSAGES.FAIL);
        response.put("message", "Could not find access token for user: " + username);

        return response.toString();
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/bitbucket/add", method = RequestMethod.POST)
    public String bitbucketAuth(Model model, @RequestParam(value = "project_id", defaultValue = "") long project_id, @RequestParam(value = "reponame", defaultValue = "") String bitbucketRepository) {

        JSONObject jsonObject = new JSONObject();

        if (!WebController.hasAccessToken()) {
            jsonObject.put("code", MESSAGES.FAIL);
            jsonObject.put("message", "User is not authorized");
            return jsonObject.toString();
        }

        if (bitbucketRepository.isEmpty()) {
            jsonObject.put("code", MESSAGES.FAIL);
            jsonObject.put("message", "Repository name is empty.");
            return jsonObject.toString();
        }

        BitbucketUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());

        if (null == user) {
            jsonObject.put("code", MESSAGES.FAIL);
            jsonObject.put("message", "User does not exist");
            return jsonObject.toString();
        }

        BitbucketProject project = projectService.findOne(project_id);

        if (null == project) {
            project = new BitbucketProject();
            project.setUser(user);
            project.setProjectId(project_id);
            project.setBitbucketRepository(bitbucketRepository);
            projectService.store(project);
        }

        jsonObject.put("code", MESSAGES.SUCCESS);
        jsonObject.put("message", "Repository: " + bitbucketRepository + " has been assigned!");

        return jsonObject.toString();
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/bitbucket/delete", method = RequestMethod.POST)
    public String unassignGithubRepository(Model model, @RequestParam(value = "project_id", defaultValue = "0", required = true) int project_id) throws IOException {

        logger.info("Requesting unassign Bitbucket repository for project_id: " + project_id);

        //Check if user is authenticated
        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing Bitbucket sigin fragment");
            //return bitbucket-signin fragment
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "User is not authenticated.").toString();
        }

        //Fetch the actual user based on JWT
        BitbucketUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());

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
    @RequestMapping(value = "/api/v1/bitbucket/disconnect", method = RequestMethod.POST)
    public String disconnectBitbucketProject(Model model, @RequestParam(value = "project_id", defaultValue = "0", required = true) int project_id) throws IOException {

        logger.info("Requesting disconnect sonarqube project for project_id: " + project_id);

        //Check if user is authenticated
        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing github sigin fragment");
            //return github-signin fragment
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "User is not authenticated.").toString();
        }

        //Fetch the actual user based on JWT
        BitbucketUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());

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

package eu.cloudteams.controller;

import com.nimbusds.jose.JOSEException;
import eu.cloudteams.authentication.jwt.Token;
import eu.cloudteams.authentication.jwt.TokenHandler;
import static eu.cloudteams.controller.WebController.getCurrentUser;

import eu.cloudteams.repository.domain.PaaSportProject;
import eu.cloudteams.repository.domain.PaaSportUser;
import eu.cloudteams.repository.service.ProjectService;
import eu.cloudteams.repository.service.UserService;
import eu.cloudteams.util.paasport.PaaSportLoginException;
import eu.cloudteams.util.paasport.PaaSportService;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

import eu.cloudteams.util.paasport.models.TDeveloperProfile;
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
 * @author Spyros Mantzouratos
 */
@Controller
public class PaaSportController {

    private static final Logger logger = Logger.getLogger(PaaSportController.class.getName());

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/auth/token", method = RequestMethod.POST)
    public String getJWT(@RequestParam(value = "username", required = true) String username, @RequestParam(value = "password", required = true) String password, HttpServletRequest request) throws IOException {

        // Check if user already exists
        PaaSportUser user = userService.findByUsername(username);
        // Print the status of user
        logger.info(null != user ? "User: " + user.getUsername() + " already exists with id: " + user.getId() : "Creating new user for username: " + username);
        String paasportUrl = "http://192.168.3.63:8080";

        if (null == user) {
            try {
                user = new PaaSportUser();

                Token generatedToken = TokenHandler.createToken(request.getRemoteAddr(), username);

                user.setAccessToken(generatedToken.getToken());
                user.setUsername(username);
                user.setRefreshToken(null);
                user.setTokenExpire(new Date());
                user.setIsSynch(true);

                //
                PaaSportService paaSportService = new PaaSportService(paasportUrl);
                String paasportToken = null;

                paasportToken = paaSportService.login(username, password);

                if (null != paasportToken && !paasportToken.isEmpty()) {
                    user.setPaasportToken(paasportToken);
                    if (!userService.storeUser(user)) {
                        logger.log(Level.SEVERE, "Could not store user: " + username + " to database.");
                        return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Could not create user " + username + "  to Cloudteams Database ").toString();
                    } else {
                        //Print the generated token
                        logger.log(Level.INFO, "Generated Token: {0}", user.getAccessToken());
                        //Success created user and accesstoken
                        return new JSONObject().put("code", MESSAGES.SUCCESS).put("message", "Token has been created successfully!").put("token", "Bearer " + user.getAccessToken()).toString();
                    }
                } else {
                    Logger.getLogger(PaaSportController.class.getName()).log(Level.INFO, "Could not create paasport token for user: " + username);
                    return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Could not create paasport token for user: " + username).toString();
                }
            } catch (PaaSportLoginException ex) {
                logger.log(Level.SEVERE, "User " + username + " is unauthorized.");
                return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Bad credentials for user " + username).toString();
            } catch (JOSEException ex) {
                Logger.getLogger(PaaSportController.class.getName()).log(Level.SEVERE, null, ex);
                return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Could not create access token for user: " + username).toString();
            }
        } else {
            //Print the generated token
            logger.log(Level.INFO, "Token Exists: {0}", user.getAccessToken());
            //Success created user and accesstoken
            return new JSONObject().put("code", MESSAGES.SUCCESS).put("message", "Token has been fetched successfully!").put("token", "Bearer " + user.getAccessToken()).toString();
        }

    }

    @CrossOrigin
    @RequestMapping(value = "/api/v1/paasport/project", method = RequestMethod.POST)
    public String getPaaSportProjectInfo(Model model, @RequestParam(value = "project_id", defaultValue = "0", required = true) int project_id) throws IOException {

        logger.info("Requesting info for repository assigned to project_id: " + project_id);

        String paasportUrl = "http://192.168.3.63:8080";

        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing paasport sign-in fragment");
            //return github-signin fragment
            return "paasport::paasport-no-auth";
        }

        PaaSportUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());

        model.addAttribute("user", user);

        PaaSportService paaSportService = new PaaSportService(paasportUrl, user.getPaasportToken());

        PaaSportProject project = projectService.findByProjectIdAndUser(project_id, user);

        //Unassigned project
        if (null == project) {

            project = new PaaSportProject();

            project.setPaasportProject("dummyPaaSportProject");
            project.setProjectId(Long.valueOf(project_id));
            project.setUser(user);

            projectService.store(project);

            logger.info("Project created successfully!");

        } else {

            logger.info("Project fetched successfully!");

        }

        // Fetch User Profile
        TDeveloperProfile developerProfile = paaSportService.getUserProfile(user.getUsername());

        if (null != developerProfile) {
            model.addAttribute("developerProfile", developerProfile);
            return "paasport::paasport-info";
        } else {
            return "paasport::paasport-error";
        }

    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/paasport/add", method = RequestMethod.POST)
    public String paasportAuth(Model model, @RequestParam(value = "project_id", defaultValue = "") long project_id, @RequestParam(value = "projectName", defaultValue = "") String projectName) {

        //Check if user is authenticated
        if (!WebController.hasAccessToken()) {
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "User not authenticated.").toString();
        }

        //Check if project is empty
        if (projectName.isEmpty()) {
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Project name is empty.").toString();
        }

        //Check if user exists
        PaaSportUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());

        //Check if user exists
        if (null == user) {
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Could not find user: " + getCurrentUser().getPrincipal().toString() + " to database.").toString();
        }

        //Check if project exists
        PaaSportProject project = projectService.findOne(project_id);

        //Create project
        if (null == project) {
            project = new PaaSportProject();
            project.setUser(user);
            project.setProjectId(project_id);
            project.setPaasportProject(projectName);
            projectService.store(project);
        }

        return new JSONObject().put("code", MESSAGES.SUCCESS).put("message", "Project: " + projectName + " has been assigned!").toString();
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/paasport/delete", method = RequestMethod.POST)
    public String unassignPaaSportProject(Model model, @RequestParam(value = "project_id", defaultValue = "0", required = true) int project_id) throws IOException {

        logger.info("Requesting unassign paasport project for project_id: " + project_id);

        //Check if user is authenticated
        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing paasport sigin fragment");
            //return github-signin fragment
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "User is not authenticated.").toString();
        }

        //Fetch the actual user based on JWT
        PaaSportUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());

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
    @RequestMapping(value = "/api/v1/paasport/disconnect", method = RequestMethod.POST)
    public String disconnectPaaSportProject(Model model) throws IOException {

        logger.info("Requesting disconnect PaaSPort widget..");

        //Check if user is authenticated
        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing paasport sigin fragment");
            //return github-signin fragment
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "User is not authenticated.").toString();
        }

        //Fetch the actual user based on JWT
        PaaSportUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());

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

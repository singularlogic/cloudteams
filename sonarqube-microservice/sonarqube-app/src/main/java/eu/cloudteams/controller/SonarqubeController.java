package eu.cloudteams.controller;

import com.nimbusds.jose.JOSEException;
import eu.cloudteams.authentication.jwt.Token;
import eu.cloudteams.authentication.jwt.TokenHandler;
import eu.cloudteams.repository.domain.SonarqubeProject;
import eu.cloudteams.repository.domain.SonarqubeUser;
import eu.cloudteams.repository.service.ProjectService;
import eu.cloudteams.repository.service.UserService;
import eu.cloudteams.util.sonarqube.SonarqubeService;
import eu.cloudteams.util.sonarqube.models.ProjectInfo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static eu.cloudteams.controller.WebController.getCurrentUser;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class SonarqubeController {

    private static final Logger logger = Logger.getLogger(SonarqubeController.class.getName());

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;


    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/sonarqube/projectcharts/{project_id}", method = RequestMethod.GET)
    public String getSonarJsonData(@PathVariable("project_id") int project_id) {


        logger.info("Requesting json for charts for repository assigned to project_id: " + project_id);

        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing sonarqube sign-in fragment");
            //return github-signin fragment
            return "sonarqube::sonarqube-no-auth";
        }

        SonarqubeUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());
        SonarqubeProject project = projectService.findByProjectIdAndUser(project_id, user);

        SonarqubeService sonarService = new SonarqubeService(user.getSonarqubeUrl());

        //Unassigned project
        // if (null == project) {
        //     sonarService = new SonarqubeService(user.getSonarqubeUrl());
        //model.addAttribute("sonarqubeProjects", sonarService.getProjects());
        ///    return "sonarqube::sonarqube-no-project";
        //}


        logger.info("Returning sonarqube-info fragment for user:  " + getCurrentUser().getPrincipal() + " and project_id: " + project_id);

        Optional<ProjectInfo> repository = sonarService.getProjectInfo();

        sonarService = new SonarqubeService(user.getSonarqubeUrl(), project.getSonarqubeProject());


        try {
            sonarService = new SonarqubeService(user.getSonarqubeUrl(), project.getSonarqubeProject());
        } catch (Exception ex) {
            Logger.getLogger(SonarqubeController.class.getName()).log(Level.SEVERE, null, ex);
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Sonar service failed to start").toString();
        }


        Optional<ProjectInfo> projectInfo = sonarService.getProjectInfo();

        if (projectInfo.isPresent()) {

            JSONObject projectDataJson = sonarService.getProjectDataJson();

            Map<String, String> metrics = projectInfo.get().getMetrics();

            metrics.getOrDefault("public_documented_api_density", "0");
            metrics.getOrDefault("test_success_density", "0");
            metrics.getOrDefault("coverage", "0");
            metrics.getOrDefault("duplicated_lines_density", "0");
            metrics.getOrDefault("comment_lines_density", "0");

            Float scaleDept = Float.parseFloat(metrics.getOrDefault("sqale_debt_ratio", "0"));
            Float hunderdMinusScaledept = 100 - scaleDept;


            JSONObject jsonTemp3 = new JSONObject();
            jsonTemp3.put("sqale_debt_ratio", scaleDept);
            jsonTemp3.put("-", hunderdMinusScaledept);

            logger.info("metricJson: " + jsonTemp3.toString());

            return new JSONObject().put("code", MESSAGES.SUCCESS).put("message", "Sonarcube data sent successfully!").put("returnobject", projectDataJson).toString();
        }

        return new JSONObject().put("code", MESSAGES.FAIL).put("message", "There are no information about this project").toString();
    }
    
    
    @CrossOrigin
    @RequestMapping(value = "/api/v1/sonarqube/project", method = RequestMethod.POST)
    public String getSonarqubeProjectInfo(Model model, @RequestParam(value = "project_id", defaultValue = "0", required = true) int project_id) throws IOException {

        logger.info("Requesting info for repository assigned to project_id: " + project_id);

        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing github sign-in fragment");
            //return github-signin fragment
            return "sonarqube::sonarqube-no-auth";
        }

        SonarqubeUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());
        SonarqubeProject project = projectService.findByProjectIdAndUser(project_id, user);

        SonarqubeService sonarService;

        //Unassigned project
        if (null == project) {
            sonarService = new SonarqubeService(user.getSonarqubeUrl());
            model.addAttribute("sonarqubeProjects", sonarService.getProjects());
            return "sonarqube::sonarqube-no-project";
        }

        logger.info("Returning sonarqube-info fragment for user:  " + getCurrentUser().getPrincipal() + " and project_id: " + project_id);

        sonarService = new SonarqubeService(user.getSonarqubeUrl(), project.getSonarqubeProject());

        Optional<ProjectInfo> repository = sonarService.getProjectInfo();

        if (repository.isPresent()) {
            model.addAttribute("projectInfo", repository.get());
            return "sonarqube::sonarqube-auth-project";
        }

        return "sonarqube::sonarqube-error";
    }

    //Rest Controller
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/auth/token", method = RequestMethod.POST)
    public String getJWT(@RequestParam(value = "username", required = true) String username, @RequestParam(value = "sonarUrl", required = true) String sonarUrl, HttpServletRequest request) throws IOException {

        //Check if sonar service is running on the given url
        
        if (!new SonarqubeService(sonarUrl).getServerInfo().isPresent()) {
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Sonarqube service is not available in url: " + sonarUrl).toString();
        }
        
        //Check if user already exists
        SonarqubeUser user = userService.findByUsername(username);

        //Print the status of user
        logger.info(null != user ? "User: " + user.getUsername() + " already exists with id: " + user.getId() : "Creating new user for username: " + username);

        sonarUrl = StringUtils.trimTrailingCharacter(sonarUrl, "/".charAt(0));
        if (null == user) {
            user = new SonarqubeUser(null, username, "", sonarUrl, true);
        } else if (user.getSonarqubeUrl().equalsIgnoreCase(sonarUrl) == false) {
            logger.log(Level.SEVERE, "You do not have access to this project");
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "You do not have access to this project").toString();
        }
        //Update/Set sonar url
        //user.setSonarqubeUrl(sonarUrl);

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
            Logger.getLogger(SonarqubeController.class.getName()).log(Level.SEVERE, null, ex);
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Could not create access token for user: " + username).toString();
        }

        //Print the generated token
        logger.log(Level.INFO, "Generated Token: {0}", user.getAccessToken());
        //Success created user and accesstoken
        return new JSONObject().put("code", MESSAGES.SUCCESS).put("message", "Token has been created successfully!").put("token", "Bearer " + user.getAccessToken()).toString();

    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/sonarqube/add", method = RequestMethod.POST)
    public String sonarqubeAuth(Model model, @RequestParam(value = "project_id", defaultValue = "") long project_id, @RequestParam(value = "projectName", defaultValue = "") String projectName) {

        //Check if user is authenticated
        if (!WebController.hasAccessToken()) {
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "User not authenticated.").toString();
        }

        //Check if project is empty
        if (projectName.isEmpty()) {
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Project name is empty.").toString();
        }

        //Check if user exists
        SonarqubeUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());

        //Check if user exists
        if (null == user) {
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "Could not find user: " + getCurrentUser().getPrincipal().toString() + " to database.").toString();
        }

        //Check if project exists
        SonarqubeProject project = projectService.findOne(project_id);

        //Create project
        if (null == project) {
            project = new SonarqubeProject();
            project.setUser(user);
            project.setProjectId(project_id);
            project.setSonarqubeProject(projectName);
            projectService.store(project);
        }

        return new JSONObject().put("code", MESSAGES.SUCCESS).put("message", "Project: " + projectName + " has been assigned!").toString();
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/v1/sonarqube/delete", method = RequestMethod.POST)
    public String unassignSonarqubeProject(Model model, @RequestParam(value = "project_id", defaultValue = "0", required = true) int project_id) throws IOException {

        logger.info("Requesting unassign sonarqube project for project_id: " + project_id);

        //Check if user is authenticated
        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing github sigin fragment");
            //return github-signin fragment
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "User is not authenticated.").toString();
        }

        //Fetch the actual user based on JWT
        SonarqubeUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());

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
    @RequestMapping(value = "/api/v1/sonarqube/disconnect", method = RequestMethod.POST)
    public String disconnectSonarqubeProject(Model model, @RequestParam(value = "project_id", defaultValue = "0", required = true) int project_id) throws IOException {

        logger.info("Requesting disconnect sonarqube project for project_id: " + project_id);

        //Check if user is authenticated
        if (!WebController.hasAccessToken()) {
            logger.warning("Unauthorized access returing github sigin fragment");
            //return github-signin fragment
            return new JSONObject().put("code", MESSAGES.FAIL).put("message", "User is not authenticated.").toString();
        }

        //Fetch the actual user based on JWT
        SonarqubeUser user = userService.findByUsername(getCurrentUser().getPrincipal().toString());

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

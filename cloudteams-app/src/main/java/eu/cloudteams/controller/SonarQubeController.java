package eu.cloudteams.controller;

import eu.cloudteams.util.sonarqube.SonarIssues;
import eu.cloudteams.util.sonarqube.SonarQubeService;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class SonarQubeController {

    private static final String SONAR_URL = "https://nemo.sonarqube.org/api/";
    private static final String PROJECT_NAME = "org.apache.tika";
    private static final String PROJECT_KEY = "org.apache.tika:tika";

    @ResponseBody
    @RequestMapping(value = "/api/v1/sonar", method = RequestMethod.GET)
    public HashMap<String, String> egtSonar() {

        SonarQubeService sonarService = new SonarQubeService();
        HashMap<String, String> projectMetrics = new HashMap<>();

        try {
            SonarIssues sonarIssues = sonarService.getProjectIssues(SONAR_URL, PROJECT_KEY);
            projectMetrics = sonarService.getProjectMetrics(SONAR_URL, PROJECT_KEY);

        } catch (Exception ex) {
            Logger.getLogger(SonarQubeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return projectMetrics;

    }

}

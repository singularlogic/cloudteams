package eu.cloudteams.controller;

import eu.cloudteams.util.sonarqube.SonarIssuesResponse;
import eu.cloudteams.util.sonarqube.SonarMetricsResponse;
import eu.cloudteams.util.sonarqube.SonarQubeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@RestController
@RequestMapping("/api/v1")
public class SonarQubeRestController {

    private static final String SONAR_URL="https://nemo.sonarqube.org/api/";
    private static final String PROJECT_NAME="org.apache.tika";
    private static final String PROJECT_KEY="org.apache.tika:tika";
    
    
    @RequestMapping(value = "/sonar", method = RequestMethod.GET)
    public String egtSonar() {
        
        
        SonarQubeService sonarService = new SonarQubeService();
        SonarIssuesResponse sonarIssues = sonarService.getSonarIssues(SONAR_URL, PROJECT_KEY);
        SonarMetricsResponse[] projectMetrics = sonarService.getProjectMetrics(SONAR_URL, PROJECT_KEY);
        
        
        return "Sonar Controller!";
    }

}

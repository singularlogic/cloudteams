package eu.cloudteams.util.sonarqube;

import eu.cloudteams.util.sonarqube.models.SonarIssues;
import eu.cloudteams.util.sonarqube.models.SonarIssuesResponse;
import eu.cloudteams.util.sonarqube.models.Msr;
import eu.cloudteams.util.sonarqube.models.Project;
import eu.cloudteams.util.sonarqube.models.ProjectInfo;
import eu.cloudteams.util.sonarqube.models.SonarMetricsResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public class SonarqubeService {

    //To be deleted
    private static final String SONAR_URL = "https://nemo.sonarqube.org";
    private static final String PROJECT_KEY = "org.apache.tika:tika";

    private final String sonarUrl;
    private final String projectKey;

    public SonarqubeService(String sonarUrl, String sonarKey) {
        this.sonarUrl = sonarUrl;
        this.projectKey = sonarKey;
    }

    public SonarqubeService(String sonarUrl) {
        this(sonarUrl, "");
    }

    //TODO: Handle possible exception
    public List<Project> getProjects() {
        RestTemplate restTemplate = new RestTemplate();
        return Arrays.asList(restTemplate.getForObject(getAPIUrl("projects/index?format=json"), Project[].class));
    }

    private String getAPIUrl(String name) {
        return this.sonarUrl + "/api/" + name;
    }

    /**
     *
     * @return @throws Exception
     */
    private SonarMetricsResponse getProjectMetrics() throws Exception {
        String technicalDebtMetric = "info_remediation_cost";
        String LinesOfCode = "ncloc";
        String uri = "resources?metrics=" + technicalDebtMetric + "," + LinesOfCode + "&format=json&resource=" + this.projectKey;
        
        SonarMetricsResponse[] result = new RestTemplate().getForObject(getAPIUrl(uri), SonarMetricsResponse[].class);

        if (result.length == 0) {
            throw new Exception("Metrics could not be found");
        }
        return result[0];
    }

    /**
     *
     * @return @throws Exception
     */
    private SonarIssues getProjectIssues() throws Exception {

        SonarIssuesResponse response = new RestTemplate().getForObject(getAPIUrl("issues/search?componentRoots=" + this.projectKey), SonarIssuesResponse.class);
        SonarIssues issues = new SonarIssues();
        if (null == response.getPaging().getTotal()) {
            throw new Exception("Total Issues not found");
        } else {
            issues.setNumberOfProjectIssues(response.getPaging().getTotal());

        }
        return issues;
    }

    public Optional<ProjectInfo> getProjectInfo() {
        try {
            ProjectInfo projectInfo = new ProjectInfo();
            SonarIssues sonarIssues = this.getProjectIssues();
            SonarMetricsResponse sonarMetrics = this.getProjectMetrics();
            //Set Total Project Issues
            projectInfo.setTotalIssues(String.valueOf(sonarIssues.getNumberOfProjectIssues()));
            //Set Project name
            projectInfo.setProjectName(sonarMetrics.getName());
            //Set Version
            projectInfo.setVersion(sonarMetrics.getVersion());
            //Set Description
            projectInfo.setDescription(sonarMetrics.getDescription());
            //Set Metrics
            projectInfo.setMetrics(sonarMetrics.getMsr().stream().collect(Collectors.toMap(Msr::getKey, Msr::getFrmtVal)));
            return Optional.of(projectInfo);

        } catch (Exception ex) {
            Logger.getLogger(SonarqubeService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Optional.empty();
    }

    public static void main(String[] args) throws Exception {
        SonarqubeService sonarqubeService = new SonarqubeService(SONAR_URL, PROJECT_KEY);
        sonarqubeService.getProjectInfo();
    }

}

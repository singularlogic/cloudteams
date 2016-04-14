package eu.cloudteams.util.sonarqube;

import eu.cloudteams.util.sonarqube.models.SonarIssues;
import eu.cloudteams.util.sonarqube.models.SonarIssuesResponse;
import eu.cloudteams.util.sonarqube.models.Msr;
import eu.cloudteams.util.sonarqube.models.Project;
import eu.cloudteams.util.sonarqube.models.ProjectInfo;
import eu.cloudteams.util.sonarqube.models.ServerInfo;
import eu.cloudteams.util.sonarqube.models.SonarMetricsResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public class SonarqubeService {

    //To be deleted
    private static final String SONAR_URL = "http://paasword.euprojects.net/sonar";//"https://nemo.sonarqube.org";
    private static final String PROJECT_KEY = "org.apache.tika:tika";

    //List of metrics to fetch for sonarqube ws
    private static final Map<String, String> metrics = new HashMap<>();

    //Prefill sonar metrics
    static {
        //Complexity
        metrics.put("complexity", "Project complexity");
        metrics.put("info_remediation_cost", "Remediation cost");

        //Documentation
        metrics.put("comment_lines", "Comment lines");
        metrics.put("public_documented_api_density", "Public documented API (%)");

        //Duplications
        metrics.put("duplicated_lines_density", "Duplicated lines (%)");

        //Issues
        metrics.put("new_violations", "New issues");
        metrics.put("new_blocker_violations", "New blocker issues");
        metrics.put("new_critical_violations", "New critical issues");
        metrics.put("open_issues", "Open issues");
        metrics.put("violations_density", "Rules compliance");

        //Quality Gates
        //metrics.put("alert_status"); //Quality Gate Status
        //metrics.put("quality_gate_details"); //Quality Gates Details	
        //Size
        metrics.put("lines", "Lines");
        metrics.put("ncloc", "Lines of code");
        metrics.put("files", "Files");
        metrics.put("functions", "Methods");
        metrics.put("projects", "Projects");

        //Technical Debt
        metrics.put("sqale_index", "Technical Debt");
        metrics.put("sqale_debt_ratio", "Technical Debt Ratio");

        //Tests
        metrics.put("coverage", "Coverage");
        metrics.put("skipped_tests", "Skipped unit tests");
        metrics.put("tests", "Unit tests");
        metrics.put("test_errors", "Unit test errors");
        metrics.put("test_failures", "Unit test failures");
        metrics.put("test_success_density", "Unit test success density (%)");

    }

    private static final String SONAR_SERVER_INFO = "server?format=json";

    private final String sonarUrl;
    private final String projectKey;

    public SonarqubeService(String sonarUrl, String sonarKey) {
        this.sonarUrl = StringUtils.trimTrailingCharacter(sonarUrl, "/".charAt(0));
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

        //Construct the Sonarqube service uri
        String uri = "resources?metrics=".concat(metrics.keySet().stream().collect(Collectors.joining(","))).concat("&format=json&resource=").concat(this.projectKey);

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
            projectInfo.setMetrics(sonarMetrics.getMsr().stream().collect(Collectors.toMap(msr -> metrics.get(msr.getKey()), Msr::getFrmtVal)));

            return Optional.of(projectInfo);

        } catch (Exception ex) {
            Logger.getLogger(SonarqubeService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Optional.empty();
    }

    public Optional<ServerInfo> getServerInfo() {

        try {
            ServerInfo serverInfo = new RestTemplate().getForObject(getAPIUrl(SONAR_SERVER_INFO), ServerInfo.class);
            return (!serverInfo.getId().isEmpty() ? Optional.of(serverInfo) : Optional.empty());
        } catch (Exception ex) {
            Logger.getLogger(SonarqubeService.class.getName()).log(Level.SEVERE, "Soanqube service not found in url: {0}", this.sonarUrl);
        }

        return Optional.empty();
    }

    public static void main(String[] args) throws Exception {
        SonarqubeService sonarqubeService = new SonarqubeService(SONAR_URL, PROJECT_KEY);
//        sonarqubeService.getProjectInfo();

        sonarqubeService.getServerInfo();
    }

}

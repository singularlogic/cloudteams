package eu.cloudteams.util.sonarqube;

import eu.cloudteams.util.sonarqube.models.Measure;
import eu.cloudteams.util.sonarqube.models.SonarIssues;
import eu.cloudteams.util.sonarqube.models.SonarIssuesResponse;
import eu.cloudteams.util.sonarqube.models.Msr;
import eu.cloudteams.util.sonarqube.models.Project;
import eu.cloudteams.util.sonarqube.models.ProjectInfo;
import eu.cloudteams.util.sonarqube.models.ServerInfo;
import eu.cloudteams.util.sonarqube.models.SonarComponentResponse;
import eu.cloudteams.util.sonarqube.models.SonarMeasuresResponse;
import eu.cloudteams.util.sonarqube.models.SonarMetricsResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public class SonarqubeService {

    //To be deleted
    private static final String SONAR_URL = "https://sonarqube.com";//"http://paasword.euprojects.net/sonar";//"https://nemo.sonarqube.org";
    private static final String PROJECT_KEY = "abap-sample-projet";

    //List of metrics to fetch for sonarqube ws
    private static final Map<String, String> metrics = new HashMap<>();
    private static final Logger logger = Logger.getLogger(SonarqubeService.class.getName());

    
    //Prefill sonar metrics
    static {
        //Complexity
        metrics.put("complexity", "Project complexity");
        metrics.put("info_remediation_cost", "Remediation cost");

        //Documentation
        metrics.put("comment_lines", "Comment lines");
        metrics.put("comment_lines_density", "Comment lines density(%)");
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
        metrics.put("new_sqale_debt_ratio", "New Code Technical Debt Ratio ");

        //Tests
        metrics.put("coverage", "Coverage");
        metrics.put("skipped_tests", "Skipped unit tests");
        metrics.put("tests", "Unit tests");
        metrics.put("test_errors", "Unit test errors");
        metrics.put("test_failures", "Unit test failures");
        metrics.put("test_success_density", "Unit test success density (%)");

    }

    private static final String SONAR_SERVER_INFO = "server/version";//"server?format=json";

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
     * @deprecated : not working properly after v6.3, use getProjectComponentAndMetrics instead
     * @return @throws Exception
     */
    private SonarMetricsResponse getProjectMetrics() throws Exception {

        //Construct the Sonarqube service uri
        
        //SonarQube API change: Deprecated since v5.4, removed since v6.3 https://sonarqube.com/web_api/api/resources
        //String uri = "resources?metrics=".concat(metrics.keySet().stream().collect(Collectors.joining(","))).concat("&format=json&resource=").concat(this.projectKey);
        String uri = "measures/component?metricKeys=".concat(metrics.keySet().stream().collect(Collectors.joining(","))).concat("&format=json&componentKey=").concat(this.projectKey);

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
    private SonarComponentResponse getProjectComponentAndMetrics() throws Exception {

        
        //SonarQube API change: Deprecated since v5.4, removed since v6.3 https://sonarqube.com/web_api/api/resources
        //String uri = "resources?metrics=".concat(metrics.keySet().stream().collect(Collectors.joining(","))).concat("&format=json&resource=").concat(this.projectKey);
        String uri = "measures/component?metricKeys=".concat(metrics.keySet().stream().collect(Collectors.joining(","))).concat("&format=json&componentKey=").concat(this.projectKey);

        SonarComponentResponse result = new RestTemplate().getForObject(getAPIUrl(uri), SonarComponentResponse.class);
   
        logger.log(Level.INFO, "get project component uri: {0}", getAPIUrl(uri));
       // logger.info("gert project component result: " + result.toString());

        
        if (null == result) {
            throw new Exception("Measures could not be found");
        }

        return result;
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
    /**
     *
     * @return @throws Exception
     * @param severity = MINOR / MAJOR / BLOCKER
     */
    private SonarIssues getProjectIssuesBySeverities(String severity) throws Exception {
       
        SonarIssuesResponse response = new RestTemplate().getForObject(getAPIUrl("issues/search?componentRoots=" + this.projectKey+"&severities="+severity), SonarIssuesResponse.class);
        SonarIssues issues = new SonarIssues();
        if (null == response.getPaging().getTotal()) {
            throw new Exception("Total Issues not found");
        } else {
            issues.setNumberOfProjectIssues(response.getPaging().getTotal());

        }
        return issues;
    }
    /**
     *
     * @return @throws Exception
     * @param status = OPEN 
     */
    private SonarIssues getProjectIssuesByStatus(String status) throws Exception {
       
        SonarIssuesResponse response = new RestTemplate().getForObject(getAPIUrl("issues/search?componentRoots=" + this.projectKey+"&statuses="+status), SonarIssuesResponse.class);
        SonarIssues issues = new SonarIssues();
        if (null == response.getPaging().getTotal()) {
            throw new Exception("Total Issues not found");
        } else {
            issues.setNumberOfProjectIssues(response.getPaging().getTotal());

        }
        return issues;
    }
    /**
     *
     * @return @throws Exception
     * @param type= OPEN 
     */
    private SonarIssues getProjectIssuesByType(String type) throws Exception {
       
        SonarIssuesResponse response = new RestTemplate().getForObject(getAPIUrl("issues/search?componentRoots=" + this.projectKey+"&types="+type), SonarIssuesResponse.class);
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
            SonarComponentResponse sonarMeasures = this.getProjectComponentAndMetrics();
            List<Measure> measures = sonarMeasures.getComponent().getMeasures();
            //Collections.replaceAll(measures, null, "N/A");

            
            //SonarMeasuresResponse sonarMeasures = this.getProjectMeasures();
            //SonarMetricsResponse sonarMetrics = this.getProjectMetrics();
            //Set Total Project Issues
            projectInfo.setTotalIssues(String.valueOf(sonarIssues.getNumberOfProjectIssues()));
            //Set Project name
             projectInfo.setProjectName(sonarMeasures.getComponent().getName());//projectInfo.setProjectName(sonarMetrics.getName());
            //Set Version
            projectInfo.setVersion("0.0.1");//projectInfo.setVersion(sonarMetrics.getVersion());
            //Set Description
            projectInfo.setDescription(sonarMeasures.getComponent().getDescription());//projectInfo.setDescription(sonarMetrics.getDescription());
            //Set Metrics
            //projectInfo.setMetrics(sonarMetrics.getMsr().stream().collect(Collectors.toMap(msr -> metrics.get(msr.getKey()), Msr::getFrmtVal)));
            projectInfo.setMetrics(measures.stream().filter(c->c.getValue()!= null).collect(Collectors.toMap(msr -> metrics.get(msr.getMetric()), Measure::getValue)));
           
           
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

    public JSONObject getProjectDataJson() {
        JSONObject jsonIssues = new JSONObject();


        try {
           //----ISSUES  ----  
           //TOTAL 
           int totalIssues = this.getProjectIssues().getNumberOfProjectIssues();
           
            
           //STATUS 
           int openIussues = this.getProjectIssuesByStatus("OPEN").getNumberOfProjectIssues();
           int reopenIussues = this.getProjectIssuesByStatus("REOPENED").getNumberOfProjectIssues();
           int closedIssues = this.getProjectIssuesByStatus("CLOSED").getNumberOfProjectIssues();
           int resolvedIssues = this.getProjectIssuesByStatus("RESOLVED").getNumberOfProjectIssues();
           int confirmedIssues = this.getProjectIssuesByStatus("CONFIRMED").getNumberOfProjectIssues();
           int otherIssues = totalIssues-reopenIussues-resolvedIssues-openIussues-closedIssues-confirmedIssues;
           //calcuate other?
           
           ///SEVERITY
           int blockerIssues = this.getProjectIssuesBySeverities("BLOCKER").getNumberOfProjectIssues();
           int majorIssues = this.getProjectIssuesBySeverities("MAJOR").getNumberOfProjectIssues();
           int minorIssues = this.getProjectIssuesBySeverities("MINOR").getNumberOfProjectIssues();
           int otherSeverity = totalIssues-blockerIssues-majorIssues-minorIssues;
           //TYPE
           int bugs = this.getProjectIssuesByType("BUG").getNumberOfProjectIssues();
           int codesmell = this.getProjectIssuesByType("CODE_SMELL").getNumberOfProjectIssues();
           int vulnerability = this.getProjectIssuesByType("VULNERABILITY").getNumberOfProjectIssues();
           int otherType = totalIssues-bugs-codesmell-vulnerability;
           
            System.out.println("total"+totalIssues);
            //System.out.println("totalusr"+TotalUserIssues);
            
            System.out.println("openIussues"+openIussues);
            System.out.println("reopemn"+reopenIussues);
            System.out.println("res"+resolvedIssues);
            System.out.println("clos"+closedIssues);
            System.out.println("conf"+confirmedIssues);
            System.out.println("otherIssues"+otherIssues);
            
            System.out.println("blocker"+blockerIssues);
            System.out.println("maj"+majorIssues);
            System.out.println("min"+minorIssues);
            System.out.println("other"+otherSeverity);
            
            System.out.println("1"+bugs);
            System.out.println("2"+codesmell);
            System.out.println("3"+vulnerability);
            System.out.println("min"+minorIssues);
            System.out.println("otherSever"+otherSeverity);
            //String issues = String.valueOf(sonarIssues.getNumberOfProjectIssues());

            //----METRICS----        
            SonarComponentResponse sonarMeasures = this.getProjectComponentAndMetrics();
            List<Measure> measures = sonarMeasures.getComponent().getMeasures();
            

            /*            Map<String, String> collect = measures.stream().filter(c->c.getMetric().equalsIgnoreCase("public_documented_api_density")).collect(Collectors.toMap(msr -> metrics.get(msr.getMetric()), Measure::getValue));
            //measures.stream().filter(c->c.getValue()!= null).collect(Collectors.toMap(msr -> metrics.get(msr.getMetric()), Measure::getValue)));
            System.out.println("collectsize"+collect.size());
            System.out.println("collect"+collect.toString());
            
            Map<String, String> collect2 = measures.stream().filter(c->c.getMetric().equalsIgnoreCase("sqale_debt_ratio")).collect(Collectors.toMap(msr -> metrics.get(msr.getMetric()), Measure::getValue));
            //measures.stream().filter(c->c.getValue()!= null).collect(Collectors.toMap(msr -> metrics.get(msr.getMetric()), Measure::getValue)));
            System.out.println("sqale_debt_ratio"+collect2.size());
            System.out.println("sqale_debt_ratio"+collect2.values().toString());*/
            
            //ProjectInfo projectInfo= this.getProjectInfo();
            Optional<Measure> findFirst = measures.stream().filter(c->c.getMetric().equalsIgnoreCase("sqale_debt_ratio")).findFirst();
            String metric = findFirst.get().getMetric();
            String value = findFirst.get().getValue();
            
            System.out.println("metric"+metric + " value"+ value);
            
            JSONObject jsonTemp = new JSONObject();
            JSONArray jsonIssueTypes = new JSONArray();
            JSONArray jsonIssueSeverity = new JSONArray();
            
            //Map.Entry pair = (Map.Entry)iterator.next();
            
            jsonTemp.put("name", "Open");
            jsonTemp.put("y", openIussues);
            jsonIssueTypes.put(jsonTemp);
            
            
            jsonTemp.put("open", openIussues);
            jsonTemp.put("closed", closedIssues);
            jsonTemp.put("reopen", reopenIussues);
            jsonTemp.put("resolved", resolvedIssues);
            jsonTemp.put("confirmend", confirmedIssues);
            jsonTemp.put("other", otherIssues);
            jsonIssueTypes.put(jsonTemp);
            //SEVERITY
            JSONObject jsonTemp2 = new JSONObject();
            jsonTemp2.put("blockerIssues", blockerIssues);
            jsonTemp2.put("majorIssues", majorIssues);
            jsonTemp2.put("minorIssues", minorIssues);
            jsonTemp2.put("otherSeverity", otherSeverity);
           //TYPE
            JSONObject jsonTemp3 = new JSONObject();
            jsonTemp3.put("bugs", bugs);
            jsonTemp3.put("codesmell", codesmell);
            jsonTemp3.put("vulnerability", vulnerability);
            jsonTemp3.put("minorIssues", minorIssues);
            jsonTemp3.put("otherSeverity", otherSeverity);
            
           jsonIssues.put("issuesByStatus", jsonTemp).put("issuesBySeverity", jsonTemp2).put("issueType", jsonTemp3);

            
            
            System.out.println("json:"+jsonTemp.toString());
            System.out.println("json:"+jsonIssues.toString());
            
            
            
            
            
           // jsonIssueTypes.add(jsonTemp);
            //iterator.remove();
            
           // jsonIssueTypes.p
            /*
            
            
            JSONArray jsonIssueTypes = new JSONArray();
            JSONArray jsonIssuePriority = new JSONArray();
            JSONArray jsonIssueStatus = new JSONArray();
            JSONArray jsonTechnicalDebtRatio = new JSONArray();
            JSONArray jsonTechnicalDebtRatioNewCode = new JSONArray();
            JSONArray jsonComments = new JSONArray();
            JSONArray jsonDuplicatedLines = new JSONArray();
           // List<Issue> issues = sonarService.getProjectInfo().(project.getProjectId());
            Map issueTypes = issues.stream().collect(Collectors.groupingBy(issue -> issue.getIssueType().getName(), Collectors.counting()));
            Map issuePriority = issues.stream().collect(Collectors.groupingBy(issue -> issue.getPriority().getName(), Collectors.counting()));
            Map issueStatus = issues.stream().collect(Collectors.groupingBy(issue -> issue.getStatus().getName(), Collectors.counting()));
            Iterator iterator = null;
            iterator = issueTypes.entrySet().iterator();
            while (iterator.hasNext()) {
            JSONObject jsonTemp = new JSONObject();
            Map.Entry pair = (Map.Entry)iterator.next();
            jsonTemp.put("name", pair.getKey());
            jsonTemp.put("y", pair.getValue());
            jsonIssueTypes.add(jsonTemp);
            iterator.remove();
            }
            iterator = issuePriority.entrySet().iterator();    
            while (iterator.hasNext()) {
            JSONObject jsonTemp = new JSONObject();
            Map.Entry pair = (Map.Entry)iterator.next();
            jsonTemp.put("name", pair.getKey());
            jsonTemp.put("y", pair.getValue());
            jsonIssuePriority.add(jsonTemp);
            iterator.remove();
            }
            iterator = issueStatus.entrySet().iterator();
            while (iterator.hasNext()) {
            JSONObject jsonTemp = new JSONObject();
            Map.Entry pair = (Map.Entry)iterator.next();
            jsonTemp.put("name", pair.getKey());
            jsonTemp.put("y", pair.getValue());
            jsonIssueStatus.add(jsonTemp);
            iterator.remove();
            }
           
            jsonIssues.put("issueTypes", jsonIssueTypes).put("issuePriority", jsonIssuePriority).put("issueStatus", jsonIssueStatus);
           
           
            return new JSONObject().put("code", MESSAGES.SUCCESS).put("message", "Jira data sent successfully!").put("returnobject", jsonIssues).toString();
            }
             */
            //return new JSONObject().put("code", MESSAGES.FAIL).put("message", "There are no information about this project").toString();
        } catch (Exception ex) {
            Logger.getLogger(SonarqubeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
 
        
        return jsonIssues;
        
        
    }

    public static void main(String[] args) throws Exception {
        SonarqubeService sonarqubeService = new SonarqubeService(SONAR_URL, PROJECT_KEY);
        Optional<ProjectInfo> projectInfo = sonarqubeService.getProjectInfo();
        
        System.out.println("projectInfo::"+ projectInfo.toString());
        
        HashMap<String, String> metrics1 = (HashMap<String, String>) projectInfo.get().getMetrics();
        String get = metrics1.get("sqale_debt_ratio");
        //String get2 = projectInfo.get().getMetrics().get("duplicated_lines_density");
        
        System.out.println("----"+get);
        //System.out.println("----"+get2);
        sonarqubeService.getServerInfo();
        
        sonarqubeService.getProjectDataJson();
        
        
    }

}

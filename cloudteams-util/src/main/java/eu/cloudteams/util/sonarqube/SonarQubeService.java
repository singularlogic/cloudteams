/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cloudteams.util.sonarqube;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public class SonarQubeService {
    
    private static final String SONAR_URL="https://nemo.sonarqube.org/api/";
    private static final String PROJECT_NAME="org.apache.tika";
    private static final String PROJECT_KEY="org.apache.tika:tika";
    

    
    /**
     *
     * @param URL
     * @param projectkey
     * @return
     * @throws Exception
     */
    public HashMap<String, String> getProjectMetrics(String URL, String projectkey) throws Exception{
        //SonarMetricsResponse sonarMeasure= new SonarMetricsResponse();
        //SonarMetrics metrics= new SonarMetrics();
        String technicalDebtMetric="info_remediation_cost";
        String LinesOfCode="ncloc";  //
        String uri= SONAR_URL+"/resources?metrics="+technicalDebtMetric+","+LinesOfCode+"&format=json&resource=org.apache.tika:tika";

        Map<String, String> params = new HashMap<String, String>();
        params.put("projectkey", projectkey);   

        RestTemplate restTemplate = new RestTemplate();
        //ResponseEntity<Object> responseEntity = restTemplate.getForEntity(uri, Object.class);
        SonarMetricsResponse[] result = restTemplate.getForObject(uri, SonarMetricsResponse[].class);
        //Object[] = responseEntity.getBody();

        //System.out.println("result"+ result.toString());
        //System.out.println("resultkey"+ result[0].getKey());
        //System.out.println("resultvalue"+ result[0].getMsr().get(0).getFrmtVal());
        //System.out.println("resultvalue"+ result[0].getMsr().get(1).getFrmtVal());
        HashMap<String, String> hashmetrics = new HashMap<>();
        if (result.length>0){
        List<Msr> metrics = result[0].getMsr();
        for (Msr metric : metrics) {

            hashmetrics.put(metric.getKey(), metric.getFrmtVal());
                }
        } else {
            throw new Exception ("no metrics found");
        }
        return hashmetrics;
        }
    
    /**
     *
     * @param URL
     * @param projectkey
     * @return
     * @throws Exception
     */
    public SonarIssues getProjectIssues(String URL, String projectkey) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        

        final String uri = SONAR_URL+"/issues/search?componentRoots={projectkey}";

        Map<String, String> params = new HashMap<String, String>();
        params.put("projectkey", projectkey);   
        SonarIssuesResponse response = restTemplate.getForObject(uri, SonarIssuesResponse.class,params);
        //ResponseEntity<String> result = restTemplate.exchange("https://nemo.sonarqube.org/api/issues/search?componentRoots=org.codehaus.sonar:sonar", HttpMethod.GET, entity, String.class);
        //String result = restTemplate.getForObject(uri,String.class, params);
        //String consumeJSONString = restTemplate.getForObject("https://nemo.sonarqube.org/api/issues/search?componentRoots=org.codehaus.sonar:sonar", String.class);
        //System.out.println("JSON String: "+response); 
        //System.out.println("total"+ response.getPaging().getTotal());
        //System.out.println("issue"+result.getIssues().get(0).getKey());
        
        SonarIssues issues = new SonarIssues();
        if(null==response.getPaging().getTotal()){
            throw new Exception("Total Issues not found");
        } else {
        issues.setNumberOfProjectIssues(response.getPaging().getTotal());

        }
        return issues;
    }

    
        public static void main(String[] args) {
        SonarQubeService sonarqubeService = new SonarQubeService();
        //sonarqubeService.getProjectIssues(SONAR_URL,PROJECT_KEY);
        HashMap<String, String> projectMetrics = null;            
        SonarIssues projectIssues = null;

        try {
            projectMetrics = sonarqubeService.getProjectMetrics(SONAR_URL,PROJECT_KEY);
            projectIssues = sonarqubeService.getProjectIssues(SONAR_URL,PROJECT_KEY);

        } catch (Exception ex) {
            Logger.getLogger(SonarQubeService.class.getName()).log(Level.SEVERE, null, ex);
        }
            System.out.println("projectMetrics: "+projectMetrics.toString());
            System.out.println("issues: "+projectIssues.getNumberOfProjectIssues());
        //sonarqubeService.getMeasureDebt(SONAR_URL,PROJECT_KEY);
                
    }

    
}

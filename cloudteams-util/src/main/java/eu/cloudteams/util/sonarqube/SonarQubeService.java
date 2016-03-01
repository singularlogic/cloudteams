/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cloudteams.util.sonarqube;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public class SonarQubeService {
    
    private static final String SONAR_URL="https://nemo.sonarqube.org/api/";
    private static final String PROJECT_NAME="org.apache.tika";
    private static final String PROJECT_KEY="org.apache.tika:tika";
    

    public void getProjectIssuesBackup(String URL, String projectkey){
        RestTemplate restTemplate = new RestTemplate();
        
                
     
       // HttpHeaders headers = new HttpHeaders();
       // headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
       // HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

       
        final String uri = SONAR_URL+"/issues/search?componentRoots={projectkey}";

        Map<String, String> params = new HashMap<String, String>();
        params.put("projectkey", projectkey);   
       
        //ResponseEntity<String> result = restTemplate.exchange("https://nemo.sonarqube.org/api/issues/search?componentRoots=org.codehaus.sonar:sonar", HttpMethod.GET, entity, String.class);
        String result = restTemplate.getForObject(uri,String.class, params);
        //String consumeJSONString = restTemplate.getForObject("https://nemo.sonarqube.org/api/issues/search?componentRoots=org.codehaus.sonar:sonar", String.class);
        System.out.println("JSON String: "+result); 
        
            JSONObject jsonResponse = new JSONObject(result);
            if (jsonResponse.has("total")) {
                SonarIssues issues = new SonarIssues();
                issues.setNumberOfProjectIssues((int) jsonResponse.get("total"));
                System.out.println("total:"+ jsonResponse.get("total"));
            }

    }
    
    public SonarMetrics getProjectMetricsJsonParsing(String URL, String projectkey){
        SonarMetrics sonarMetrics = new SonarMetrics();
        String uri= SONAR_URL+"/resources?metrics=info_remediation_cost&format=json&resource=org.apache.tika:tika";
        String technicalDebtMetric="info_remediation_cost";
        String LinesOfCode="generated_ncloc";  //
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("projectkey", projectkey);   

        //ResponseEntity<String> result = restTemplate.exchange("https://nemo.sonarqube.org/api/issues/search?componentRoots=org.codehaus.sonar:sonar", HttpMethod.GET, entity, String.class);
       // String result = restTemplate.getForObject(uri,String.class, params);
        //String consumeJSONString = restTemplate.getForObject("https://nemo.sonarqube.org/api/issues/search?componentRoots=org.codehaus.sonar:sonar", String.class);
        //System.out.println("JSON String: "+result); 
        JSONObject jsonResponse = new JSONObject();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> responseEntity = restTemplate.getForEntity(uri, Object.class);
        
        //Object[] = responseEntity.getBody();
        jsonResponse = new JSONObject(responseEntity);

       // jsonResponse.getJSONObject("body");
        System.out.println("json-->"+jsonResponse.toString());
        if(jsonResponse.has("msr")){
        JSONObject msrObject = jsonResponse.getJSONObject("msr");
            System.out.println("MSR foun"+msrObject.toString());
        if (msrObject.has("frmt_val")) {
                SonarIssues issues = new SonarIssues();
                issues.setNumberOfProjectIssues((int) msrObject.get("frmt_val"));
                System.out.println("dedt:"+ msrObject.get("frmt_val"));
            }
        
    }
        ///RestTemplate restTemplate = new RestTemplate();
        //Make Rest call to fetch AccessToken
  //SonarMetricsResponse[] forObject = restTemplate.getForObject(uri,SonarMetricsResponse[].class,params);
        
        //return response;
        //RestTemplate restTemplate = new RestTemplate();
        //Make Rest call to fetch AccessToken
        //ResponseEntity<SonarMetrics[]> accesstokenResponse = restTemplate.getForEntity(uri, SonarMetrics[].class);
        //System.out.println("JSON String: "+accesstokenResponse.toString()); 
       // System.out.println("---->"+forObject[0].toString());
        
        //System.out.println("keey:"+forObject[0].getKey());
   // sonarMetrics.setKey(forObject[0].getKey());
        //System.out.println("body::"+sonarMetrics.toString()); 
          
          return sonarMetrics;
        }
            //sonarMetrics.setError("GitHubException");
           // sonarMetrics.setError_description("Could not get temporary code from GitHub API");
    
        
        
        /*
        
        
            JSONObject jsonResponse = new JSONObject(result);
            if (jsonResponse.has(technicalDebtMetric)) {
                SonarMetrics metrics = new SonarMetrics();
                metrics.setTechnicalDebt((int) jsonResponse.get(technicalDebtMetric));
                System.out.println("debt:"+ jsonResponse.get(technicalDebtMetric));
            }
            
            
            if (jsonResponse.has(LinesOfCode)) {
                SonarMetrics metrics = new SonarMetrics();
                metrics.setLinesOfCode((int) jsonResponse.get(LinesOfCode));
                System.out.println("debt:"+ jsonResponse.get(LinesOfCode));
            }
        
         */   
            //  GithubAuthResponse authResponse = new GithubAuthResponse();

            

    
    public void getProjectMetricsBackup(String URL, String projectkey){
        RestTemplate restTemplate = new RestTemplate();

        //String uri = SONAR_URL+"/metrics/search?componentRoots={projectkey}";
        
        
        String uri= SONAR_URL+"/resources?metrics=info_remediation_cost&format=json&resource={projectkey}";

        String technicalDebtMetric="info_remediation_cost";
        String LinesOfCode="generated_ncloc";  //
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("projectkey", projectkey);   
       
        //ResponseEntity<String> result = restTemplate.exchange("https://nemo.sonarqube.org/api/issues/search?componentRoots=org.codehaus.sonar:sonar", HttpMethod.GET, entity, String.class);
        String result = restTemplate.getForObject(uri,String.class, params);
        //String consumeJSONString = restTemplate.getForObject("https://nemo.sonarqube.org/api/issues/search?componentRoots=org.codehaus.sonar:sonar", String.class);
        System.out.println("JSON String: "+result); 
        
        
        JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonResponse = jsonArray.getJSONObject(0);
            if (jsonResponse.has(technicalDebtMetric)) {
                SonarMetrics metrics = new SonarMetrics();
                metrics.setTechnicalDebt((int) jsonResponse.get(technicalDebtMetric));
                System.out.println("debt:"+ jsonResponse.get(technicalDebtMetric));
            }
            
            
            if (jsonResponse.has(LinesOfCode)) {
                SonarMetrics metrics = new SonarMetrics();
                metrics.setLinesOfCode((int) jsonResponse.get(LinesOfCode));
                System.out.println("lines of code:"+ jsonResponse.get(LinesOfCode));
            }
        
    
    }
    
    
    
    
    
        public SonarMetricsResponse[] getProjectMetrics(String URL, String projectkey){
        //SonarMetricsResponse sonarMeasure= new SonarMetricsResponse();
        SonarMetrics metrics= new SonarMetrics();
        String technicalDebtMetric="info_remediation_cost";
        String LinesOfCode="ncloc";  //
        String uri= SONAR_URL+"/resources?metrics="+technicalDebtMetric+","+LinesOfCode+"&format=json&resource=org.apache.tika:tika";

        Map<String, String> params = new HashMap<String, String>();
        params.put("projectkey", projectkey);   

        RestTemplate restTemplate = new RestTemplate();
        //ResponseEntity<Object> responseEntity = restTemplate.getForEntity(uri, Object.class);
        SonarMetricsResponse[] result = restTemplate.getForObject(uri, SonarMetricsResponse[].class);
        //Object[] = responseEntity.getBody();

        System.out.println("result"+ result.toString());
        System.out.println("resultkey"+ result[0].getKey());
        System.out.println("resultvalue"+ result[0].getMsr().get(0).getFrmtVal());
        System.out.println("resultvalue"+ result[0].getMsr().get(1).getFrmtVal());
      
       // if(result[0].getMsr().contains(uri)){
       //     result[0].getMsr().
        //}
        //result[0].getMsr().contains(uri)
       // metrics.setLinesOfCode(result[0].getMsr().get(0).getFrmtVal());
        
        return result;
        }
    
    
    
    
    
        public SonarIssuesResponse getSonarIssues(String URL, String projectkey){
        RestTemplate restTemplate = new RestTemplate();
        

        final String uri = SONAR_URL+"/issues/search?componentRoots={projectkey}";

        Map<String, String> params = new HashMap<String, String>();
        params.put("projectkey", projectkey);   
        SonarIssuesResponse response = restTemplate.getForObject(uri, SonarIssuesResponse.class,params);
        //ResponseEntity<String> result = restTemplate.exchange("https://nemo.sonarqube.org/api/issues/search?componentRoots=org.codehaus.sonar:sonar", HttpMethod.GET, entity, String.class);
        //String result = restTemplate.getForObject(uri,String.class, params);
        //String consumeJSONString = restTemplate.getForObject("https://nemo.sonarqube.org/api/issues/search?componentRoots=org.codehaus.sonar:sonar", String.class);
        System.out.println("JSON String: "+response); 
        System.out.println("total"+ response.getPaging().getTotal());
        //System.out.println("issue"+result.getIssues().get(0).getKey());


        return response;
    }
    

    
        public static void main(String[] args) {
        SonarQubeService sonarqubeService = new SonarQubeService();
        //sonarqubeService.getProjectIssues(SONAR_URL,PROJECT_KEY);
        sonarqubeService.getProjectMetrics(SONAR_URL,PROJECT_KEY);
        //sonarqubeService.getMeasureDebt(SONAR_URL,PROJECT_KEY);
                
    }
    
            
            
        
    
   
    
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cloudteams.util.sonarqube;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "paging",
})
/**
 *
 * @author jled
 */
public class SonarIssuesResponse {
    
    @JsonProperty("paging")
    private Paging paging;
    
    //commented as we don't need to fetch issues
    //@JsonProperty("issues")
    //private List<Issue> issues = new ArrayList<Issue>();

    /**
     * @return the paging
     */
    public Paging getPaging() {
        return paging;
    }

    /**
     * @param paging the paging to set
     */
    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    ////Commented as we currently don't need this
    /**
     * @return the issues
    
    public List<Issue> getIssues() {
        return issues;
    }

    /**
     * @param issues the issues to set
     
    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

   */

    
}

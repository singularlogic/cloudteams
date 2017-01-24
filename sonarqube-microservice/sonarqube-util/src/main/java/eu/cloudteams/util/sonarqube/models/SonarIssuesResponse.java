package eu.cloudteams.util.sonarqube.models;


import javax.annotation.Generated;
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

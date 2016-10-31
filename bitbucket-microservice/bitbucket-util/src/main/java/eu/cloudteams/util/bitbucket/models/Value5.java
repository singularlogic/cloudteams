
package eu.cloudteams.util.bitbucket.models;

import java.util.HashMap;
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
    "priority",
    "kind",
    "repository",
    "links",
    "reporter",
    "title",
    "component",
    "votes",
    "watches",
    "content",
    "assignee",
    "state",
    "version",
    "edited_on",
    "created_on",
    "milestone",
    "updated_on",
    "type",
    "id"
})
public class Value5 {

    @JsonProperty("priority")
    private String priority;
    @JsonProperty("kind")
    private String kind;
    @JsonProperty("repository")
    private Repository repository;
    @JsonProperty("links")
    private Links_ links;
    @JsonProperty("reporter")
    private Reporter reporter;
    @JsonProperty("title")
    private String title;
    @JsonProperty("component")
    private Object component;
    @JsonProperty("votes")
    private Integer votes;
    @JsonProperty("watches")
    private Integer watches;
    @JsonProperty("content")
    private Content content;
    @JsonProperty("assignee")
    private Assignee assignee;
    @JsonProperty("state")
    private String state;
    @JsonProperty("version")
    private Object version;
    @JsonProperty("edited_on")
    private Object editedOn;
    @JsonProperty("created_on")
    private String createdOn;
    @JsonProperty("milestone")
    private Object milestone;
    @JsonProperty("updated_on")
    private String updatedOn;
    @JsonProperty("type")
    private String type;
    @JsonProperty("id")
    private Integer id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The priority
     */
    @JsonProperty("priority")
    public String getPriority() {
        return priority;
    }

    /**
     * 
     * @param priority
     *     The priority
     */
    @JsonProperty("priority")
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * 
     * @return
     *     The kind
     */
    @JsonProperty("kind")
    public String getKind() {
        return kind;
    }

    /**
     * 
     * @param kind
     *     The kind
     */
    @JsonProperty("kind")
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * 
     * @return
     *     The repository
     */
    @JsonProperty("repository")
    public Repository getRepository() {
        return repository;
    }

    /**
     * 
     * @param repository
     *     The repository
     */
    @JsonProperty("repository")
    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    /**
     * 
     * @return
     *     The links
     */
    @JsonProperty("links")
    public Links_ getLinks() {
        return links;
    }

    /**
     * 
     * @param links
     *     The links
     */
    @JsonProperty("links")
    public void setLinks(Links_ links) {
        this.links = links;
    }

    /**
     * 
     * @return
     *     The reporter
     */
    @JsonProperty("reporter")
    public Reporter getReporter() {
        return reporter;
    }

    /**
     * 
     * @param reporter
     *     The reporter
     */
    @JsonProperty("reporter")
    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The component
     */
    @JsonProperty("component")
    public Object getComponent() {
        return component;
    }

    /**
     * 
     * @param component
     *     The component
     */
    @JsonProperty("component")
    public void setComponent(Object component) {
        this.component = component;
    }

    /**
     * 
     * @return
     *     The votes
     */
    @JsonProperty("votes")
    public Integer getVotes() {
        return votes;
    }

    /**
     * 
     * @param votes
     *     The votes
     */
    @JsonProperty("votes")
    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    /**
     * 
     * @return
     *     The watches
     */
    @JsonProperty("watches")
    public Integer getWatches() {
        return watches;
    }

    /**
     * 
     * @param watches
     *     The watches
     */
    @JsonProperty("watches")
    public void setWatches(Integer watches) {
        this.watches = watches;
    }

    /**
     * 
     * @return
     *     The content
     */
    @JsonProperty("content")
    public Content getContent() {
        return content;
    }

    /**
     * 
     * @param content
     *     The content
     */
    @JsonProperty("content")
    public void setContent(Content content) {
        this.content = content;
    }

    /**
     * 
     * @return
     *     The assignee
     */
    @JsonProperty("assignee")
    public Assignee getAssignee() {
        return assignee;
    }

    /**
     * 
     * @param assignee
     *     The assignee
     */
    @JsonProperty("assignee")
    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    /**
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public String getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 
     * @return
     *     The version
     */
    @JsonProperty("version")
    public Object getVersion() {
        return version;
    }

    /**
     * 
     * @param version
     *     The version
     */
    @JsonProperty("version")
    public void setVersion(Object version) {
        this.version = version;
    }

    /**
     * 
     * @return
     *     The editedOn
     */
    @JsonProperty("edited_on")
    public Object getEditedOn() {
        return editedOn;
    }

    /**
     * 
     * @param editedOn
     *     The edited_on
     */
    @JsonProperty("edited_on")
    public void setEditedOn(Object editedOn) {
        this.editedOn = editedOn;
    }

    /**
     * 
     * @return
     *     The createdOn
     */
    @JsonProperty("created_on")
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     * 
     * @param createdOn
     *     The created_on
     */
    @JsonProperty("created_on")
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * 
     * @return
     *     The milestone
     */
    @JsonProperty("milestone")
    public Object getMilestone() {
        return milestone;
    }

    /**
     * 
     * @param milestone
     *     The milestone
     */
    @JsonProperty("milestone")
    public void setMilestone(Object milestone) {
        this.milestone = milestone;
    }

    /**
     * 
     * @return
     *     The updatedOn
     */
    @JsonProperty("updated_on")
    public String getUpdatedOn() {
        return updatedOn;
    }

    /**
     * 
     * @param updatedOn
     *     The updated_on
     */
    @JsonProperty("updated_on")
    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    /**
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

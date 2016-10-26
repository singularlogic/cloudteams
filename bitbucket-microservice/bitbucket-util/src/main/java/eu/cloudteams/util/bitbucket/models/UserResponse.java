
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
    "username",
    "website",
    "display_name",
    "uuid",
    "links",
    "created_on",
    "location",
    "type"
})
public class UserResponse {

    @JsonProperty("username")
    private String username;
    @JsonProperty("website")
    private Object website;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("links")
    private Links links;
    @JsonProperty("created_on")
    private String createdOn;
    @JsonProperty("location")
    private Object location;
    @JsonProperty("type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The username
     */
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    /**
     * 
     * @param username
     *     The username
     */
    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 
     * @return
     *     The website
     */
    @JsonProperty("website")
    public Object getWebsite() {
        return website;
    }

    /**
     * 
     * @param website
     *     The website
     */
    @JsonProperty("website")
    public void setWebsite(Object website) {
        this.website = website;
    }

    /**
     * 
     * @return
     *     The displayName
     */
    @JsonProperty("display_name")
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 
     * @param displayName
     *     The display_name
     */
    @JsonProperty("display_name")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 
     * @return
     *     The uuid
     */
    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    /**
     * 
     * @param uuid
     *     The uuid
     */
    @JsonProperty("uuid")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * 
     * @return
     *     The links
     */
    @JsonProperty("links")
    public Links getLinks() {
        return links;
    }

    /**
     * 
     * @param links
     *     The links
     */
    @JsonProperty("links")
    public void setLinks(Links links) {
        this.links = links;
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
     *     The location
     */
    @JsonProperty("location")
    public Object getLocation() {
        return location;
    }

    /**
     * 
     * @param location
     *     The location
     */
    @JsonProperty("location")
    public void setLocation(Object location) {
        this.location = location;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

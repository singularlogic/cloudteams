
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
    "scm",
    "website",
    "has_wiki",
    "name",
    "links",
    "fork_policy",
    "uuid",
    "language",
    "created_on",
    "full_name",
    "has_issues",
    "owner",
    "updated_on",
    "size",
    "type",
    "slug",
    "is_private",
    "description"
})
public class Repository {

    @JsonProperty("scm")
    private String scm;
    @JsonProperty("website")
    private Object website;
    @JsonProperty("has_wiki")
    private Boolean hasWiki;
    @JsonProperty("name")
    private String name;
    @JsonProperty("links")
    private Links links;
    @JsonProperty("fork_policy")
    private String forkPolicy;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("language")
    private String language;
    @JsonProperty("created_on")
    private String createdOn;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("has_issues")
    private Boolean hasIssues;
    @JsonProperty("owner")
    private Owner owner;
    @JsonProperty("updated_on")
    private String updatedOn;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("type")
    private String type;
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("is_private")
    private Boolean isPrivate;
    @JsonProperty("description")
    private String description;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The scm
     */
    @JsonProperty("scm")
    public String getScm() {
        return scm;
    }

    /**
     * 
     * @param scm
     *     The scm
     */
    @JsonProperty("scm")
    public void setScm(String scm) {
        this.scm = scm;
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
     *     The hasWiki
     */
    @JsonProperty("has_wiki")
    public Boolean getHasWiki() {
        return hasWiki;
    }

    /**
     * 
     * @param hasWiki
     *     The has_wiki
     */
    @JsonProperty("has_wiki")
    public void setHasWiki(Boolean hasWiki) {
        this.hasWiki = hasWiki;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
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
     *     The forkPolicy
     */
    @JsonProperty("fork_policy")
    public String getForkPolicy() {
        return forkPolicy;
    }

    /**
     * 
     * @param forkPolicy
     *     The fork_policy
     */
    @JsonProperty("fork_policy")
    public void setForkPolicy(String forkPolicy) {
        this.forkPolicy = forkPolicy;
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
     *     The language
     */
    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    /**
     * 
     * @param language
     *     The language
     */
    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
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
     *     The fullName
     */
    @JsonProperty("full_name")
    public String getFullName() {
        return fullName;
    }

    /**
     * 
     * @param fullName
     *     The full_name
     */
    @JsonProperty("full_name")
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 
     * @return
     *     The hasIssues
     */
    @JsonProperty("has_issues")
    public Boolean getHasIssues() {
        return hasIssues;
    }

    /**
     * 
     * @param hasIssues
     *     The has_issues
     */
    @JsonProperty("has_issues")
    public void setHasIssues(Boolean hasIssues) {
        this.hasIssues = hasIssues;
    }

    /**
     * 
     * @return
     *     The owner
     */
    @JsonProperty("owner")
    public Owner getOwner() {
        return owner;
    }

    /**
     * 
     * @param owner
     *     The owner
     */
    @JsonProperty("owner")
    public void setOwner(Owner owner) {
        this.owner = owner;
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
     *     The size
     */
    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    /**
     * 
     * @param size
     *     The size
     */
    @JsonProperty("size")
    public void setSize(Integer size) {
        this.size = size;
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
     *     The slug
     */
    @JsonProperty("slug")
    public String getSlug() {
        return slug;
    }

    /**
     * 
     * @param slug
     *     The slug
     */
    @JsonProperty("slug")
    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     * 
     * @return
     *     The isPrivate
     */
    @JsonProperty("is_private")
    public Boolean getIsPrivate() {
        return isPrivate;
    }

    /**
     * 
     * @param isPrivate
     *     The is_private
     */
    @JsonProperty("is_private")
    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    /**
     * 
     * @return
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
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

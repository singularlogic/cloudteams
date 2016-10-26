
package eu.cloudteams.util.bitbucket.models;

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
    "watchers",
    "branches",
    "tags",
    "commits",
    "clone",
    "self",
    "html",
    "avatar",
    "hooks",
    "forks",
    "downloads",
    "pullrequests"
})
public class Links {

    @JsonProperty("watchers")
    private Watchers watchers;
    @JsonProperty("branches")
    private Branches branches;
    @JsonProperty("tags")
    private Tags tags;
    @JsonProperty("commits")
    private Commits commits;
    @JsonProperty("clone")
    private List<Clone> clone = new ArrayList<Clone>();
    @JsonProperty("self")
    private Self self;
    @JsonProperty("html")
    private Html html;
    @JsonProperty("avatar")
    private Avatar avatar;
    @JsonProperty("hooks")
    private Hooks hooks;
    @JsonProperty("forks")
    private Forks forks;
    @JsonProperty("downloads")
    private Downloads downloads;
    @JsonProperty("pullrequests")
    private Pullrequests pullrequests;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The watchers
     */
    @JsonProperty("watchers")
    public Watchers getWatchers() {
        return watchers;
    }

    /**
     * 
     * @param watchers
     *     The watchers
     */
    @JsonProperty("watchers")
    public void setWatchers(Watchers watchers) {
        this.watchers = watchers;
    }

    /**
     * 
     * @return
     *     The branches
     */
    @JsonProperty("branches")
    public Branches getBranches() {
        return branches;
    }

    /**
     * 
     * @param branches
     *     The branches
     */
    @JsonProperty("branches")
    public void setBranches(Branches branches) {
        this.branches = branches;
    }

    /**
     * 
     * @return
     *     The tags
     */
    @JsonProperty("tags")
    public Tags getTags() {
        return tags;
    }

    /**
     * 
     * @param tags
     *     The tags
     */
    @JsonProperty("tags")
    public void setTags(Tags tags) {
        this.tags = tags;
    }

    /**
     * 
     * @return
     *     The commits
     */
    @JsonProperty("commits")
    public Commits getCommits() {
        return commits;
    }

    /**
     * 
     * @param commits
     *     The commits
     */
    @JsonProperty("commits")
    public void setCommits(Commits commits) {
        this.commits = commits;
    }

    /**
     * 
     * @return
     *     The clone
     */
    @JsonProperty("clone")
    public List<Clone> getClone() {
        return clone;
    }

    /**
     * 
     * @param clone
     *     The clone
     */
    @JsonProperty("clone")
    public void setClone(List<Clone> clone) {
        this.clone = clone;
    }

    /**
     * 
     * @return
     *     The self
     */
    @JsonProperty("self")
    public Self getSelf() {
        return self;
    }

    /**
     * 
     * @param self
     *     The self
     */
    @JsonProperty("self")
    public void setSelf(Self self) {
        this.self = self;
    }

    /**
     * 
     * @return
     *     The html
     */
    @JsonProperty("html")
    public Html getHtml() {
        return html;
    }

    /**
     * 
     * @param html
     *     The html
     */
    @JsonProperty("html")
    public void setHtml(Html html) {
        this.html = html;
    }

    /**
     * 
     * @return
     *     The avatar
     */
    @JsonProperty("avatar")
    public Avatar getAvatar() {
        return avatar;
    }

    /**
     * 
     * @param avatar
     *     The avatar
     */
    @JsonProperty("avatar")
    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    /**
     * 
     * @return
     *     The hooks
     */
    @JsonProperty("hooks")
    public Hooks getHooks() {
        return hooks;
    }

    /**
     * 
     * @param hooks
     *     The hooks
     */
    @JsonProperty("hooks")
    public void setHooks(Hooks hooks) {
        this.hooks = hooks;
    }

    /**
     * 
     * @return
     *     The forks
     */
    @JsonProperty("forks")
    public Forks getForks() {
        return forks;
    }

    /**
     * 
     * @param forks
     *     The forks
     */
    @JsonProperty("forks")
    public void setForks(Forks forks) {
        this.forks = forks;
    }

    /**
     * 
     * @return
     *     The downloads
     */
    @JsonProperty("downloads")
    public Downloads getDownloads() {
        return downloads;
    }

    /**
     * 
     * @param downloads
     *     The downloads
     */
    @JsonProperty("downloads")
    public void setDownloads(Downloads downloads) {
        this.downloads = downloads;
    }

    /**
     * 
     * @return
     *     The pullrequests
     */
    @JsonProperty("pullrequests")
    public Pullrequests getPullrequests() {
        return pullrequests;
    }

    /**
     * 
     * @param pullrequests
     *     The pullrequests
     */
    @JsonProperty("pullrequests")
    public void setPullrequests(Pullrequests pullrequests) {
        this.pullrequests = pullrequests;
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

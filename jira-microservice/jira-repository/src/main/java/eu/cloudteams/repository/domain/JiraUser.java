package eu.cloudteams.repository.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Entity
@Table(name = "JiraUser")

public class JiraUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 250)
    @Column(unique = true, name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 1024)
    @Column(name = "access_token")
    private String accessToken;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "jira_token")
    private String jiraToken;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isSynch")
    private boolean isSynch;

    public JiraUser() {
    }

    public JiraUser(Long id) {
        this.id = id;
    }

    public JiraUser(Long id, String username, String accessToken, String bitbucketToken, boolean isSynch) {
        this.id = id;
        this.username = username;
        this.accessToken = accessToken;
        this.jiraToken = bitbucketToken;
        this.isSynch = isSynch;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }



    public boolean getIsSynch() {
        return isSynch;
    }

    public void setIsSynch(boolean isSynch) {
        this.isSynch = isSynch;
    }

    /**
     * @return the jiraToken
     */
    public String getJiraToken() {
        return jiraToken;
    }

    /**
     * @param jiraToken the jiraToken to set
     */
    public void setJiraToken(String jiraToken) {
        this.jiraToken = jiraToken;
    }

}

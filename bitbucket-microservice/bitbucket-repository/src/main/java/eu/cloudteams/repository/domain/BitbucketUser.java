package eu.cloudteams.repository.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Entity
@Table(name = "BitbucketUser")

public class BitbucketUser implements Serializable {

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
    @Column(name = "bitbucket_token")
    private String bitbucketToken;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "refresh_token")
    private String refreshToken;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isSynch")
    private boolean isSynch;
//    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "expire_token")
    private Date tokenExpire;

    public BitbucketUser() {
    }

    public BitbucketUser(Long id) {
        this.id = id;
    }

    public BitbucketUser(Long id, String username, String accessToken, String bitbucketToken, String refreshToken, boolean isSynch, Date tokenExpire) {
        this.id = id;
        this.username = username;
        this.accessToken = accessToken;
        this.bitbucketToken = bitbucketToken;
        this.refreshToken = refreshToken;
        this.isSynch = isSynch;
        this.tokenExpire = tokenExpire;
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
     * @return the bitbucketToken
     */
    public String getBitbucketToken() {
        return bitbucketToken;
    }

    /**
     * @param bitbucketToken the bitbucketToken to set
     */
    public void setBitbucketToken(String bitbucketToken) {
        this.bitbucketToken = bitbucketToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Date getTokenExpire() {
        return tokenExpire;
    }

    public void setTokenExpire(Date tokenExpire) {
        this.tokenExpire = tokenExpire;
    }

    public boolean hasTokenExpired() {
        return new Date().getTime() > this.getTokenExpire().getTime();
    }
}

package eu.cloudteams.util.sonarqube.models;

import java.io.Serializable;
import java.util.List;

/**
 * @author Spyros Mantzouratos
 */
public class TDeveloperProfile implements Serializable {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String userMonitoringKey;
    private List<TPaaSOffering> paaSOfferingList;
    private List<TExternalApplication> externalApplications;

    public TDeveloperProfile() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserMonitoringKey() {
        return userMonitoringKey;
    }

    public void setUserMonitoringKey(String userMonitoringKey) {
        this.userMonitoringKey = userMonitoringKey;
    }

    public List<TPaaSOffering> getPaaSOfferingList() {
        return paaSOfferingList;
    }

    public void setPaaSOfferingList(List<TPaaSOffering> paaSOfferingList) {
        this.paaSOfferingList = paaSOfferingList;
    }

    public List<TExternalApplication> getExternalApplications() {
        return externalApplications;
    }

    public void setExternalApplications(List<TExternalApplication> externalApplications) {
        this.externalApplications = externalApplications;
    }
}

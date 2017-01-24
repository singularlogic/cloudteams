package eu.cloudteams.util.sonarqube.models;

import java.util.Date;

/**
 * @author Spyros Mantzouratos
 */
public class TExternalApplication {

    private Long id;
    private String name;
    private String description;
    private String region;
    private String endpointURI;
    private String monitoringAppID;
    private String remoteGitURL;
    private TPaaSOffering paaSOffering;
    private String status;
    private Date dateCreated;
    //    private List<ApplicationStack> stacks;
//    private List<ApplicationService> services;
//    private PaaSOfferingRating paaSOfferingRating;

    public TExternalApplication() {
    }

    public enum Status {
        Creating,
        Created,
        Deploying,
        Deployed,
        Starting,
        Running,
        Stopping,
        Undeploying,
        Deleting,
        Error
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEndpointURI() {
        return endpointURI;
    }

    public void setEndpointURI(String endpointURI) {
        this.endpointURI = endpointURI;
    }

    public String getMonitoringAppID() {
        return monitoringAppID;
    }

    public void setMonitoringAppID(String monitoringAppID) {
        this.monitoringAppID = monitoringAppID;
    }

    public String getRemoteGitURL() {
        return remoteGitURL;
    }

    public void setRemoteGitURL(String remoteGitURL) {
        this.remoteGitURL = remoteGitURL;
    }

    public TPaaSOffering getPaaSOffering() {
        return paaSOffering;
    }

    public void setPaaSOffering(TPaaSOffering paaSOffering) {
        this.paaSOffering = paaSOffering;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}

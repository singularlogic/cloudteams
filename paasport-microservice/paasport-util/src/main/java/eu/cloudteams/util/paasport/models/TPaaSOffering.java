package eu.cloudteams.util.paasport.models;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Spyros Mantzouratos
 */
public class TPaaSOffering implements Serializable {

    private Long id;
    private String name;
    private String description;
    private String endpointURI;
    private String adapterImplementation;
    private String binaryName;
    private String deploymentType;
    private String authorizationType;
    private Date dateCreated;
    private int support = 0;
    private int service = 0;
    private int pricePerformanceRatio = 0;
    private int maintenance = 0;
    private int slaReliability = 0;
    private boolean authorized;
    private String username;
    private String password;
    private String privateKey;
    private String publicKey;

    public TPaaSOffering() {
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

    public String getEndpointURI() {
        return endpointURI;
    }

    public void setEndpointURI(String endpointURI) {
        this.endpointURI = endpointURI;
    }

    public String getAdapterImplementation() {
        return adapterImplementation;
    }

    public void setAdapterImplementation(String adapterImplementation) {
        this.adapterImplementation = adapterImplementation;
    }

    public String getBinaryName() {
        return binaryName;
    }

    public void setBinaryName(String binaryName) {
        this.binaryName = binaryName;
    }

    public String getDeploymentType() {
        return deploymentType;
    }

    public void setDeploymentType(String deploymentType) {
        this.deploymentType = deploymentType;
    }

    public String getAuthorizationType() {
        return authorizationType;
    }

    public void setAuthorizationType(String authorizationType) {
        this.authorizationType = authorizationType;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public int getPricePerformanceRatio() {
        return pricePerformanceRatio;
    }

    public void setPricePerformanceRatio(int pricePerformanceRatio) {
        this.pricePerformanceRatio = pricePerformanceRatio;
    }

    public int getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(int maintenance) {
        this.maintenance = maintenance;
    }

    public int getSlaReliability() {
        return slaReliability;
    }

    public void setSlaReliability(int slaReliability) {
        this.slaReliability = slaReliability;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}

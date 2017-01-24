package eu.cloudteams.util.paasport.models;

import java.util.Map;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public class ProjectInfo {

    private String projectName;
    private String version;
    private String description;
    private String totalIssues;
    private Map<String, String> metrics;

    public String getProjectName() {
        return getValue(projectName);
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getVersion() {
        return getValue(version);
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return getValue(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotalIssues() {
        return getValue(totalIssues);
    }

    public void setTotalIssues(String totalIssues) {
        this.totalIssues = totalIssues;
    }

    public Map<String, String> getMetrics() {
        return metrics;
    }

    public void setMetrics(Map<String, String> metrics) {
        this.metrics = metrics;
    }

    private String getValue(String value) {
        return (null == value || value.isEmpty()) ? "N/A" : value;
    }

}

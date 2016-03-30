package eu.cloudteams.repository.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cascade;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Entity
@Table(name = "cloudteams.SonarqubeProject")

public class SonarqubeProject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "projectId")
    @NotNull
    private Long projectId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 250)
    @Column(name = "sonarqubeProject")
    private String sonarqubeProject;
    @OneToOne
    @JoinColumn(name = "uid", referencedColumnName = "id")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private SonarqubeUser user;

    public SonarqubeProject() {
    }

    public SonarqubeProject(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getSonarqubeProject() {
        return sonarqubeProject;
    }

    public void setSonarqubeProject(String sonarqubeProject) {
        this.sonarqubeProject = sonarqubeProject;
    }

    public SonarqubeUser getUser() {
        return user;
    }

    public void setUser(SonarqubeUser user) {
        this.user = user;
    }

}

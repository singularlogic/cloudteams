package eu.cloudteams.repository.dao;

import eu.cloudteams.repository.domain.SonarqubeProject;
import eu.cloudteams.repository.domain.SonarqubeUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface ProjectRepository extends JpaRepository<SonarqubeProject, Long> {

    //Define CRUD operations
    /**
     *
     * @param projectId
     * @return
     */
    public SonarqubeProject findByProjectIdAndUser(long projectId,SonarqubeUser user);
}

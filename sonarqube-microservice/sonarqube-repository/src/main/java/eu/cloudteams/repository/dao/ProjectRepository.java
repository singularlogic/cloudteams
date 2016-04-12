package eu.cloudteams.repository.dao;

import eu.cloudteams.repository.domain.SonarqubeProject;
import eu.cloudteams.repository.domain.SonarqubeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Repository
public interface ProjectRepository extends JpaRepository<SonarqubeProject, Long> {

    //Define CRUD operations
    /**
     *
     * @param projectId
     * @param user
     * @return
     */
    public SonarqubeProject findByProjectIdAndUser(long projectId, SonarqubeUser user);

    @Transactional
    public long deleteByProjectIdAndUser(long projectId, SonarqubeUser user);

}

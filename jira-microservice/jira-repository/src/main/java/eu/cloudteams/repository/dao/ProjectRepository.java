package eu.cloudteams.repository.dao;

import eu.cloudteams.repository.domain.JiraProject;
import eu.cloudteams.repository.domain.JiraUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Repository
public interface ProjectRepository extends JpaRepository<JiraProject, Long> {

    //Define CRUD operations
    /**
     *
     * @param projectId
     * @param user
     * @return
     */
    public JiraProject findByProjectIdAndUser(long projectId, JiraUser user);

   @Transactional
    public long deleteByProjectIdAndUser(long projectId, JiraUser user);

}

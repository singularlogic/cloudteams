package eu.cloudteams.repository.dao;

import eu.cloudteams.repository.domain.Project;
import eu.cloudteams.repository.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

    //Define CRUD operations
    /**
     *
     * @param projectId
     * @return
     */
    public Project findByProjectIdAndUser(long projectId,User user);
}

package eu.cloudteams.repository.dao;

import eu.cloudteams.repository.domain.GithubProject;
import eu.cloudteams.repository.domain.GithubUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface ProjectRepository extends JpaRepository<GithubProject, Long> {

    //Define CRUD operations
    /**
     *
     * @param projectId
     * @return
     */
    public GithubProject findByProjectIdAndUser(long projectId,GithubUser user);
}

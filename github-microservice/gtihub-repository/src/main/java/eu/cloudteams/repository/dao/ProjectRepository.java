package eu.cloudteams.repository.dao;

import eu.cloudteams.repository.domain.GithubProject;
import eu.cloudteams.repository.domain.GithubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Repository
public interface ProjectRepository extends JpaRepository<GithubProject, Long> {

    //Define CRUD operations
    /**
     *
     * @param projectId
     * @param user
     * @return
     */
    public GithubProject findFirst1ByProjectIdAndUser(long projectId, GithubUser user);

   @Transactional
    public long deleteByProjectIdAndUser(long projectId, GithubUser user);

}

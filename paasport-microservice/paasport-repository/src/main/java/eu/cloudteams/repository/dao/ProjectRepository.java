package eu.cloudteams.repository.dao;

import eu.cloudteams.repository.domain.PaaSportProject;
import eu.cloudteams.repository.domain.PaaSportUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Repository
public interface ProjectRepository extends JpaRepository<PaaSportProject, Long> {

    //Define CRUD operations
    /**
     *
     * @param projectId
     * @param user
     * @return
     */
    public PaaSportProject findByProjectIdAndUser(long projectId, PaaSportUser user);

    @Transactional
    public long deleteByProjectIdAndUser(long projectId, PaaSportUser user);

}

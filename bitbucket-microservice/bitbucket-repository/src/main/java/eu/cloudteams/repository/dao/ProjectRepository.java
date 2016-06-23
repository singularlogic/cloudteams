package eu.cloudteams.repository.dao;

import eu.cloudteams.repository.domain.BitbucketProject;
import eu.cloudteams.repository.domain.BitbucketUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Repository
public interface ProjectRepository extends JpaRepository<BitbucketProject, Long> {

    //Define CRUD operations
    /**
     *
     * @param projectId
     * @param user
     * @return
     */
    public BitbucketProject findByProjectIdAndUser(long projectId, BitbucketUser user);

   @Transactional
    public long deleteByProjectIdAndUser(long projectId, BitbucketUser user);

}

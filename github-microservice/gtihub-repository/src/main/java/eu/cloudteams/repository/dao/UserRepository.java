package eu.cloudteams.repository.dao;

import eu.cloudteams.repository.domain.GithubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface UserRepository extends JpaRepository<GithubUser, Long> {

    //Define CRUD operations
    /**
     *
     * @param username
     * @return
     */
    public GithubUser findByUsername(String username);

    @Transactional
    @Modifying
    @Query("update GithubUser u set u.isSynch=?1 where u.id=?2")
    public int setSynchStatus(boolean isSynch, long uid);
}

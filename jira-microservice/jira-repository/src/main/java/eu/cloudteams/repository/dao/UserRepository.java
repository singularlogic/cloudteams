package eu.cloudteams.repository.dao;

import eu.cloudteams.repository.domain.JiraUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface UserRepository extends JpaRepository<JiraUser, Long> {

    //Define CRUD operations
    /**
     *
     * @param username
     * @return
     */
    public JiraUser findByUsername(String username);

    @Transactional
    @Modifying
    @Query("update JiraUser u set u.isSynch=?1 where u.id=?2")
    public int setSynchStatus(boolean isSynch, long uid);
}

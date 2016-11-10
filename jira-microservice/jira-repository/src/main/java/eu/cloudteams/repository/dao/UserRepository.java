package eu.cloudteams.repository.dao;

import eu.cloudteams.repository.domain.JiraUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Repository
public interface UserRepository extends JpaRepository<JiraUser, Long> {

    //Define CRUD operations
    /**
     *
     * @param username
     * @return
     */
    public JiraUser findByUsername(String username);
}

package eu.cloudteams.repository.dao;

import eu.cloudteams.repository.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface UserRepository extends JpaRepository<User, Long> {

    //Define CRUD operations
    /**
     *
     * @param username
     * @return
     */
    public User findByUsername(String username);
}

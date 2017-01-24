package eu.cloudteams.repository.dao;

import eu.cloudteams.repository.domain.PaaSportUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface UserRepository extends JpaRepository<PaaSportUser, Long> {

    //Define CRUD operations
    /**
     *
     * @param username
     * @return
     */
    public PaaSportUser findByUsername(String username);
}

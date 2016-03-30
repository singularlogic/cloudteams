package eu.cloudteams.repository.dao;

import eu.cloudteams.repository.domain.GithubUser;
import org.springframework.data.jpa.repository.JpaRepository;

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
}

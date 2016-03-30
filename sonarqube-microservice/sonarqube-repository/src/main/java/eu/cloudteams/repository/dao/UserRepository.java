package eu.cloudteams.repository.dao;

import eu.cloudteams.repository.domain.SonarqubeUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface UserRepository extends JpaRepository<SonarqubeUser, Long> {

    //Define CRUD operations
    /**
     *
     * @param username
     * @return
     */
    public SonarqubeUser findByUsername(String username);
}

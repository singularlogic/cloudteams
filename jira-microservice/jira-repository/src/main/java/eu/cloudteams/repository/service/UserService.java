package eu.cloudteams.repository.service;

import eu.cloudteams.repository.dao.UserRepository;
import eu.cloudteams.repository.domain.JiraUser;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    public boolean storeUser(JiraUser user) {
        try {
            userRepository.save(user);
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            return false;
        }
        return user.getId() > 0;
    }

    public boolean deleteUser(long id) {
        try {
            userRepository.delete(id);
        } catch (Exception ex) {
            Logger.getLogger(UserService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return true;
    }

    public JiraUser fetchUserById(long id) {
        return userRepository.findOne(id);
    }

    /**
     * Fetch a user from database , given a username and a password
     *
     * @param username The username
     * @return A User object (null if no user is found)
     */
    public JiraUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void setSynchStatus(boolean isSynch, long id) {
        this.userRepository.setSynchStatus(isSynch, id);
    }

}

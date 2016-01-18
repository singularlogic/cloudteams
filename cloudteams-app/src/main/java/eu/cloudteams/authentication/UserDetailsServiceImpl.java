package eu.cloudteams.authentication;

import eu.cloudteams.repository.domain.User;
import eu.cloudteams.repository.service.UserService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = Logger.getLogger(UserDetailsServiceImpl.class.getName());
    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {

        this.userService = userService;
    }

    /**
     * Fetch a user from database on a username
     *
     * @param username The username to match for
     * @return An instance of CurrentUser object
     * @throws UsernameNotFoundException
     */
    @Override
    public CurrentUser loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.log(Level.INFO, "Authenticating user with username={0}", username);
        //Fetch user by username
        User user = userService.findByUsername(username);
        //Check if user exists
        if (null == user) {
            logger.log(Level.WARNING, "User with username={0} has not been found to the database...", username);
            throw new UsernameNotFoundException("User with username=" + username + "has not been found to the database...");
        }

        return new CurrentUser(user.getId(), user.getUsername(), user.getPassword(),  user.getUserRole()  , user.getFirstName(), user.getLastName());
    }
}

package eu.cloudteams.controller;

import eu.cloudteams.repository.domain.User;
import eu.cloudteams.repository.service.UserService;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Restful API for integration with Openproject
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@RestController
@RequestMapping("/cloudteams/api/v1")
public class RestAPIController {

    private final Logger restLogger = Logger.getLogger(RestAPIController.class.getName());

    @Autowired
    UserService userService;


    @RequestMapping(value = "/user/{user_id}", method = RequestMethod.GET)
    public User fetchUserByID(@PathVariable long user_id) {
        return userService.fetchUserById(user_id);
    }

}

package eu.cloudteams.controller;

import eu.cloudteams.authentication.Token;
import com.nimbusds.jose.JOSEException;
import eu.cloudteams.authentication.AuthUtils;
import eu.cloudteams.github.GithubAuthHandler;
import eu.cloudteams.github.GithubAuthResponse;
import eu.cloudteams.repository.domain.User;
import eu.cloudteams.repository.service.UserService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Restful API for integration with CloudTeams
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@RestController
@RequestMapping("/api/v1")
public class RestAPIController {

    private static final Logger restLogger = Logger.getLogger(RestAPIController.class.getName());

    @Autowired
    UserService userService;

    @RequestMapping(value = "/user/{user_id}", method = RequestMethod.GET)
    public User fetchUserByID(@PathVariable long user_id) {
        return userService.fetchUserById(user_id);
    }

    @RequestMapping(value = "/auth/github", method = RequestMethod.POST)
    public Token githubAuth(@RequestBody String requestbody, HttpServletRequest request) {
        restLogger.log(Level.INFO, "Request from ip: {0} Requestbody: {1}", new Object[]{request.getRemoteAddr(), requestbody});
        Token generatedToken = new Token();
        try {
            generatedToken = AuthUtils.createToken(request.getRemoteAddr(), 01L);
        } catch (JOSEException ex) {
            Logger.getLogger(RestAPIController.class.getName()).log(Level.SEVERE, null, ex);
        }

        GithubAuthResponse gitAuthResponse = GithubAuthHandler.requestAccesToken(requestbody);

        if (gitAuthResponse.isValid()) {
            restLogger.info("Success get AccessToken: " + gitAuthResponse.getAccess_token());
        } else {
            restLogger.severe("Fail to get Acess Token reason: " + gitAuthResponse.getError());
        }

        return generatedToken;
    }

}

package eu.cloudteams.controller;

import eu.cloudteams.authentication.Token;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import eu.cloudteams.authentication.AuthUtils;
import eu.cloudteams.repository.domain.User;
import eu.cloudteams.repository.service.UserService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Restful API for integration with Openproject
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@RestController
@RequestMapping("/api/v1")
public class RestAPIController {

    private final Logger restLogger = Logger.getLogger(RestAPIController.class.getName());

    @Autowired
    UserService userService;

    @RequestMapping(value = "/user/{user_id}", method = RequestMethod.GET)
    public User fetchUserByID(@PathVariable long user_id) {
        return userService.fetchUserById(user_id);
    }

    @RequestMapping(value = "/auth/github", method = RequestMethod.POST)
    public Token githubAuth(@RequestBody String bodyContent, HttpServletRequest request) {
        restLogger.info("Request from ip: " + request.getRemoteAddr() + " Requestbody: " + bodyContent);
        Token generatedToken = new Token();
        try {
            generatedToken = AuthUtils.createToken(request.getRemoteAddr(), 01L);
        } catch (JOSEException ex) {
            Logger.getLogger(RestAPIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return generatedToken;
    }

}

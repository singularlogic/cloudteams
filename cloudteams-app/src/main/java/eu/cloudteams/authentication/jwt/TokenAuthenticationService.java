package eu.cloudteams.authentication.jwt;

import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public class TokenAuthenticationService {


    private final TokenHandler tokenHandler;

    
    public TokenAuthenticationService(UserDetailsServiceImpl userDetailsService) {
        tokenHandler = new TokenHandler(userDetailsService);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(TokenHandler.AUTH_HEADER_KEY);
        if (token != null) {
            try {
                final User user = tokenHandler.parseToken(token);
                if (user != null) {
                     return new UserAuthentication(user);
                }
            } catch (JOSEException | ParseException ex) {
                Logger.getLogger(TokenAuthenticationService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
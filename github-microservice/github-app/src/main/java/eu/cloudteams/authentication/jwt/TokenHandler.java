package eu.cloudteams.authentication.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public class TokenHandler {

    private static final JWSHeader JWT_HEADER = new JWSHeader(JWSAlgorithm.HS256);
    private static final String TOKEN_SECRET = "thecakeisalie";
    public static final String AUTH_HEADER_KEY = "Authorization";

    private final UserDetailsServiceImpl userDetailsService;

    public TokenHandler(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     *
     * @param host The request host
     * @param sub The subscriber
     * @return A Token object
     * @throws JOSEException
     */
//    @Autowired
    public static Token createToken(String host, String username) throws JOSEException {
        JWTClaimsSet claim = new JWTClaimsSet();
        claim.setSubject(username);
        claim.setIssuer(host);
//        claim.setIssueTime(DateTime.now().toDate());
//        claim.setExpirationTime(DateTime.now().plusDays(14).toDate());
        JWSSigner signer = new MACSigner(TOKEN_SECRET);
        SignedJWT jwt = new SignedJWT(JWT_HEADER, claim);
        jwt.sign(signer);
        return new Token(jwt.serialize());
    }

    public User parseToken(String token) throws JOSEException, ParseException {
        Logger.getLogger(TokenHandler.class.getName()).info("Token is: " + token.substring(7, token.length()));
        SignedJWT signedJWT = SignedJWT.parse(token.substring(7, token.length()));
        JWSVerifier verifier = new MACVerifier(TOKEN_SECRET);
        //Check the signature of the JWT token
        if (false == signedJWT.verify(verifier)) {
            Logger.getLogger(TokenHandler.class.getName()).severe("Invalid Signature for Token: " + token);
            return null;
        }

        return userDetailsService.loadUserByUsername(signedJWT.getJWTClaimsSet().getSubject());

    }

}

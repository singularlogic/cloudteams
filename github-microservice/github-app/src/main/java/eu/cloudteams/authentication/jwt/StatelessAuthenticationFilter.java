package eu.cloudteams.authentication.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public class StatelessAuthenticationFilter extends GenericFilterBean {

    private final TokenAuthenticationService tokenAuthenticationService;

    public StatelessAuthenticationFilter(UserDetailsServiceImpl userDetailsService) {
        this.tokenAuthenticationService = new TokenAuthenticationService(userDetailsService);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(tokenAuthenticationService.getAuthentication((HttpServletRequest) request));
        // always continue
        filterChain.doFilter(request, response);
    }

}

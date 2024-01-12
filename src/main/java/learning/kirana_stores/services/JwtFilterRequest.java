package learning.kirana_stores.services;

import learning.kirana_stores.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilterRequest extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    //private final TokenBucket tokenBucket;

    @Autowired
    public JwtFilterRequest (JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        //this.tokenBucket = tokenBucket;
    }

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader ("Authorization");
        String username = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith ("BEARER ")) {
            jwtToken = authorizationHeader.substring (6);
            username = jwtUtils.extractUsername (jwtToken);
        }

        if (username != null && SecurityContextHolder.getContext ().getAuthentication () == null) {
            UserDetails currentUserDetails = userService.loadUserByUsername (username);
            Boolean tokenValidated = jwtUtils.validateToken (jwtToken, currentUserDetails);

            if (tokenValidated) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken (currentUserDetails, null, currentUserDetails.getAuthorities ());
                usernamePasswordAuthenticationToken.setDetails (new WebAuthenticationDetailsSource ().buildDetails (request));
                SecurityContextHolder.getContext ().setAuthentication (usernamePasswordAuthenticationToken);


            }

        }
        filterChain.doFilter (request, response);

    }

    /**
     * @param request
     * @return
     * @throws ServletException
     */
    @Override
    protected boolean shouldNotFilter (HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI ();
        boolean isauth = path.equals ("/auth");
        boolean issignup = path.equals (("/signup"));
        return isauth || issignup;
    }
}

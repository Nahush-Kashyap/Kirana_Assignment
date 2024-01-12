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

    /**
     * Constructor for JwtFilterRequest.
     * @param jwtUtils The utility class for handling JWT operations.
     * @param userService The custom user service.
     */
    @Autowired
    public JwtFilterRequest(JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        //this.tokenBucket = tokenBucket;
    }

    /**
     * Filters incoming requests to validate and extract JWT tokens.
     * If a valid token is found, sets the authentication details in the SecurityContextHolder.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param filterChain The filter chain to proceed with.
     * @throws ServletException If an exception occurs during the filter process.
     * @throws IOException If an I/O exception occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("BEARER ")) {
            jwtToken = authorizationHeader.substring(6);
            username = jwtUtils.extractUsername(jwtToken);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails currentUserDetails = userService.loadUserByUsername(username);
            Boolean tokenValidated = jwtUtils.validateToken(jwtToken, currentUserDetails);

            if (tokenValidated) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(currentUserDetails, null, currentUserDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Decides whether the filter should be applied to the given request.
     * It does not filter requests to "/auth" and "/signup" endpoints.
     * @param request The HTTP request.
     * @return true if the filter should not be applied, false otherwise.
     * @throws ServletException If an exception occurs during the check.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        boolean isAuth = path.equals("/auth");
        boolean isSignup = path.equals("/signup");
        return isAuth || isSignup;
    }
}

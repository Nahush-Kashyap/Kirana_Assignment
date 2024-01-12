package learning.kirana_stores.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtils {
    /**
     * Secret key used for JWT generation and validation.
     */
    private static final String SECRET_KEY = "secretkey";

    /**
     * Generates a JWT token for the given UserDetails.
     *
     * @param userDetails The UserDetails for which the token is generated.
     * @return JWT token as a String.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Validates a JWT token against the provided UserDetails.
     *
     * @param token       The JWT token to be validated.
     * @param userDetails The UserDetails against which the token is validated.
     * @return True if the token is valid for the provided UserDetails, false otherwise.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        String userName = extractUsername(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Creates a JWT token with the specified claims and subject.
     *
     * @param claims  The claims to be included in the token.
     * @param subject The subject (typically username) for whom the token is created.
     * @return JWT token as a String.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date(System.currentTimeMillis());
        Date until = new Date(System.currentTimeMillis() + 1000 * 60 * 5); // 5 mins
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(now).setExpiration(until)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    /**
     * Extracts a specific claim from the JWT token using a claims resolver function.
     *
     * @param token           The JWT token.
     * @param claimsResolver  The function to resolve the desired claim from the claims.
     * @param <T>             The type of the claim.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token The JWT token.
     * @return Claims object containing all claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    /**
     * Extracts the username claim from the JWT token.
     *
     * @param token The JWT token.
     * @return The extracted username.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token The JWT token.
     * @return The expiration date.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token The JWT token.
     * @return True if the token is expired, false otherwise.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}

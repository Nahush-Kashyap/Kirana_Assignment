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
     *
     */
    private static final String SECRET_KEY = "secretkey";

    public String generateToken (UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<> ();
        return createToken (claims, userDetails.getUsername ());
    }

    /**
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken (String token, UserDetails userDetails) {
        String userName = extractUsername (token);
        return userName.equals (userDetails.getUsername ()) && !isTokenExprired (token);
    }

    /**
     * @param claims
     * @param subject
     * @return
     */

    private String createToken (Map<String, Object> claims, String subject) {
        Date now = new Date (System.currentTimeMillis ());
        Date until = new Date (System.currentTimeMillis () + 1000 * 60 * 5); //5 mins
        return Jwts.builder ().setClaims (claims).setSubject (subject).setIssuedAt (now).setExpiration (until)
                .signWith (SignatureAlgorithm.HS256, SECRET_KEY).compact ();

    }

    /**
     * @param token
     * @param claimsResolver
     * @param <T>
     * @return
     */
    public <T> T extractClaim (String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims (token);
        return claimsResolver.apply (claims);
    }

    /**
     * @param token
     * @return
     */
    private Claims extractAllClaims (String token) {
        return Jwts.parser ().setSigningKey (SECRET_KEY).parseClaimsJws (token).getBody ();
    }

    /**
     * @param token
     * @return
     */
    public String extractUsername (String token) {
        return extractClaim (token, Claims::getSubject);
    }

    public Date extractExpiration (String token) {
        return extractClaim (token, Claims::getExpiration);
    }

    private Boolean isTokenExprired (String token) {
        return extractExpiration (token).before (new Date ());
    }
}


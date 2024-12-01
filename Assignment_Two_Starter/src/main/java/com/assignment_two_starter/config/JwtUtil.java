package com.assignment_two_starter.config;

import com.assignment_two_starter.model.entities.Customer;
import com.assignment_two_starter.model.entities.Role;
import com.assignment_two_starter.model.services.CustomerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

/**
 * Utility class for handling JSON Web Tokens (JWT), including token creation, validation,
 * and extraction of claims such as username and expiration date.
 *
 * A claim is a piece of information embedded within a JWT that conveys details about the user or token, such as username, expiration.
 *
 * <p>This class is responsible for creating a JWT for a given username, extracting claims
 * like username and expiration from a token, and validating tokens against a stored secret key.</p>
 *
 * <p><strong>Note:</strong> The secret key should be stored securely and not directly in the code.</p>
 */

@Component
public class JwtUtil {
    //256 Bit Base64 string. Ideally it shouldn't be stored in the code at all
    private String secret = "do5bf5EfC9fSTccODsZwLawNOS7iLWZG4W2V3jY9e7g=";

    private SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

    @Autowired
    private CustomerService customerService;

    /**
     * Extracts the username from the JWT.
     *
     * @param token the JWT from which to extract the username
     * @return the username embedded in the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the JWT.
     *
     * @param token the JWT from which to extract the expiration date
     * @return the expiration date of the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT using a provided function.
     *
     * @param <T> the type of the claim
     * @param token the JWT from which to extract the claim
     * @param claimsResolver a function to resolve the desired claim from the token
     * @return the resolved claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT.
     *
     * @param token the JWT from which to extract all claims
     * @return all claims present in the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    /**
     * Checks if the token is expired.
     *
     * @param token the JWT to check for expiration
     * @return true if the token is expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a JWT for the given username with a set expiration time.
     *
     * @param username the username to embed in the token
     * @return the generated JWT
     */
    public String generateToken(String username) {
        Customer customer = customerService.getCustomerByEmail(username);

        Map<String, Object> claims = new HashMap<>();

        if(customer.getRoles().isEmpty()) {
            customer.setRoles(new HashSet<>(Collections.singletonList(new Role(1L, "CUSTOMER"))));
        }

        List<String> roles = customerService.getAuthorities(customer.getRoles()).stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put("authorities", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) //sets it that the token will expire 7 Days after it has been issued
                //.setExpiration(new Date(System.currentTimeMillis() + 1000 * 10))  //sets it that the token will expire 10 seconds after it has been issued
                .signWith(secretKey, SignatureAlgorithm.HS256).compact();
    }

    /**
     * Validates the token by checking the username and expiration date.
     *
     * @param token the JWT to validate
     * @param username the username to compare against the token's embedded username
     * @return true if the token is valid and not expired, false otherwise
     */
    public Boolean validateToken(String token, String username) {
        final String usernameFromToken = extractUsername(token);
        return (username.equals(usernameFromToken) && !isTokenExpired(token));
    }
}

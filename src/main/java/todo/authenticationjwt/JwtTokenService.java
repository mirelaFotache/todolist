package todo.authenticationjwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenService {
    private String secret;

    @Autowired
    public JwtTokenService(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Optional<Boolean> validateToken(String token) {
        return isTokenNotExpired(token) ? Optional.of(Boolean.TRUE) : Optional.empty();
    }

    public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String token) {
        String username = getUsernameFromToken(token);
        List<GrantedAuthority> grantedRoles = getGrantedAuthorities(token);
        return new UsernamePasswordAuthenticationToken(username, null, grantedRoles);
    }

    private List<GrantedAuthority> getGrantedAuthorities(String authToken) {
        List<LinkedHashMap<String, String>> roles = getRolesFromToken(authToken);
        List<GrantedAuthority> grantedRoles = new ArrayList<>();
        roles.forEach(map -> {
            grantedRoles.add(new SimpleGrantedAuthority(map.get("authority")));
        });
        return grantedRoles;
    }

    @SuppressWarnings("unchecked")
    private List<LinkedHashMap<String, String>> getRolesFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return (List<LinkedHashMap<String, String>>) claims.get("auth");
    }

    private Boolean isTokenNotExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.after(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}

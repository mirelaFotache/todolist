package todo.authenticationjwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import todo.service.exceptions.OAuth2Exception;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenService {
    private static final String CERTIFICATE_ALIAS = "jwtcert";
    private static final String CERTIFICATE_NAME = "data/keystore.jks";
    public static final String KEYSTORE_FILE_NOT_AVAILABLE = "exception.keystore.file.not.available";

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
                //.setSigningKey(secret) // Use symmetric key retrieved with @Value to validate token
                .setSigningKey(getPublicKey()) // Use public key to validate token
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Get public key from certificate.
     * NOTE: It will be used to parse and validate the token that was signed with a private key
     *
     * @return public key
     */
    private PublicKey getPublicKey() {
        try {
            ClassLoader classLoader = JwtTokenService.class.getClassLoader();

            final URL resource = classLoader.getResource(CERTIFICATE_NAME);
            if (resource != null) {
                File file = new File(resource.getFile());
                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                keystore.load(new FileInputStream(file), secret.toCharArray());
                Certificate cert = keystore.getCertificate(CERTIFICATE_ALIAS);
                return cert.getPublicKey();
            } else {
                throw new OAuth2Exception(KEYSTORE_FILE_NOT_AVAILABLE);
            }
        } catch (Exception e) {
            throw new OAuth2Exception(KEYSTORE_FILE_NOT_AVAILABLE);
        }
    }


}

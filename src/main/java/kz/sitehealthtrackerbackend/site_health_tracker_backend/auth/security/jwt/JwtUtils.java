package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.security.jwt;

import io.jsonwebtoken.*;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.security.MyUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
@SuppressWarnings("FieldCanBeLocal")
public class JwtUtils {
    private final String access = "access";
    private final String refresh = "refresh";
    private final String typeClaim = "typ";
    @Value("${app.auth.jwt.public-key}")
    private String jwtSecret;
    @Value("${app.auth.jwt.expiration-ms}")
    private Long jwtExpirationMs;
    @Value("${app.auth.jwt.refresh.expiration-ms}")
    private Long jwtRefreshTokenExpirationMs;

    public String generateAccessToken(Authentication authentication) {
        MyUserDetails userPrincipal = (MyUserDetails) authentication.getPrincipal();
        return generateAccessToken(userPrincipal);
    }

    public String generateRefreshToken(MyUserDetails userPrincipal) {
        return generateJwt(userPrincipal, jwtRefreshTokenExpirationMs, refresh);
    }

    public String generateAccessToken(MyUserDetails userPrincipal) {
        return generateJwt(userPrincipal, jwtExpirationMs, access);
    }

    private String generateJwt(MyUserDetails userPrincipal, Long expiration, String type) {
        return Jwts.builder()
                .setSubject((userPrincipal.getEmail()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .claim(typeClaim, type)
                .compact();
    }


    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateAccessJWT(String accessToken) {
        return validateJWT(accessToken, access);
    }

    public boolean validateRefreshJWT(String refreshToken) {
        return validateJWT(refreshToken, refresh);
    }

    public boolean validateJWT(String authToken, String type) {
        try {
            var claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return claims.getBody().get(typeClaim).equals(type);
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}


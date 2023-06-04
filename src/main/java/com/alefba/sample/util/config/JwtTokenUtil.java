package com.alefba.sample.util.config;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.secret-key}")
    private String jwtSecret;

    @Value("${jwt.expired-time}")
    private Long expiredTime;

    @Value("${jwt.issuer}")
    private String issuer;

    public String generateAccessToken(String username) {
        final var now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(expiredTime)))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }


    public void validate(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        } catch (MalformedJwtException ex) {
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
    }
}

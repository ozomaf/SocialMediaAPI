package com.azatkhaliullin.socialmediaapi.service;

import com.azatkhaliullin.socialmediaapi.dto.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@UtilityClass
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private Integer jwtExpiration;

    public String generateToken(User user) {
        try {
            return Jwts
                    .builder()
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(jwtExpiration))
                    .signWith(generateSigningKey())
                    .compact();
        } catch (Exception e) {
            log.error("Token generation error", e);
            return null;
        }
    }

    public boolean isTokenValid(String token,
                                User user) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(generateSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return isTokenFormatValid(claims, user);
        } catch (JwtException e) {
            log.error("Invalid JWT", e);
            return false;
        }
    }

    private boolean isTokenFormatValid(Jws<Claims> claims,
                                       User user) {
        Claims tokenClaims = claims.getBody();

        Date expiration = tokenClaims.getExpiration();
        if (expiration != null && expiration.before(new Date())) {
            log.error("JWT has expired");
            return false;
        }
        Long id = tokenClaims.get("id", Long.class);
        if (id == null || !id.equals(user.getId())) {
            log.error("Invalid user id");
            return false;
        }
        String username = tokenClaims.get("username", String.class);
        if (username == null || !username.equals(user.getUsername())) {
            log.error("Invalid username");
            return false;
        }
        return true;
    }

    private Key generateSigningKey() {
        byte[] decode = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(decode);
    }

}
package com.azatkhaliullin.socialmediaapi.service;

import com.azatkhaliullin.socialmediaapi.Exception.TokenGenerationException;
import com.azatkhaliullin.socialmediaapi.dto.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;

@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private Integer jwtExpiration;

    public String generateToken(User user) {
        try {
            return Jwts
                    .builder()
                    .setSubject(user.getUsername())
                    .claim("userId", user.getId())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(generateSigningKey())
                    .compact();
        } catch (InvalidKeyException e) {
            throw new TokenGenerationException();
        }
    }

    public boolean isTokenValid(String token,
                                User user) {
        Claims claims = getClaimsJws(token);
        if (claims == null) {
            return false;
        }
        Date expiration = claims.getExpiration();
        if (expiration != null && expiration.before(new Date())) {
            log.error("JWT has expired");
            return false;
        }
        Long id = claims.get("userId", Long.class);
        if (id == null || !id.equals(user.getId())) {
            log.error("Invalid user ID");
            return false;
        }
        String username = claims.getSubject();
        if (username == null || !username.equals(user.getUsername())) {
            log.error("Invalid username");
            return false;
        }
        return true;
    }

    public String extractUsername(String token) {
        Claims claims = getClaimsJws(token);
        if (claims != null) {
            return claims.getSubject();
        } else {
            return null;
        }
    }

    private Claims getClaimsJws(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(generateSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.error("Invalid JWT", e);
            return null;
        }
    }

    private Key generateSigningKey() {
        byte[] decode = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(decode);
    }

}
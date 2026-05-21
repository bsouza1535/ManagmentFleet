package com.managment.fleet.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final Key key;
    private final long jwtExpirationMs;

    public JwtUtil(@Value("${app.jwt.secret:changeit}") String secret,
                   @Value("${app.jwt.expiration-ms:3600000}") long jwtExpirationMs) {
        // If secret provided, use it as key material; otherwise generate one (not for prod)
        if (secret == null || "changeit".equals(secret)) {
            this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        } else {
            this.key = Keys.hmacShaKeyFor(secret.getBytes());
        }
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateToken(String username, Set<Role> roles) {
        String rolesStr = roles.stream().map(Enum::name).collect(Collectors.joining(","));
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", rolesStr)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        Date exp = parseClaims(token).getExpiration();
        return exp.before(new Date());
    }
}

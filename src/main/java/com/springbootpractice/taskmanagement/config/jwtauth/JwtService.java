package com.springbootpractice.taskmanagement.config.jwtauth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    @Value("${taskmanager.jwt.secretkey}")
    private String SECRET_KEY;

    @Value("${taskmanager.jwt.issuer}")
    private String ISSUER;

    public boolean isTokenValid(JwtUserDetail user, String token) {

        return (user.getUsername().equals(extractUsername(token)))
                && getExpiration(token).before(new Date());
    }

    public String generateToken(JwtUserDetail user) {
        Map<String, String> newClaims = new HashMap<>();
        newClaims.put("typ", "access");

        return Jwts
                .builder()
                .setSubject(user.getUsername())
                .setIssuer(ISSUER)
                .setClaims(newClaims)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(JwtUserDetail user) {
        Map<String, String> newClaims = new HashMap<>();
        newClaims.put("typ", "refresh");

        final long refreshTokenExpiration = 1000L * 60 * 60 * 24 * 30;
        return Jwts
                .builder()
                .setSubject(user.getUsername())
                .setIssuer(ISSUER)
                .setClaims(newClaims)
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Date getExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] result = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(result);
    }
}

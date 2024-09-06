package com.springbootpractice.taskmanagement.config.jwtauth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    private static final Logger logger =  LoggerFactory.getLogger(JwtService.class);

    @Value("${taskmanager.jwt.secretkey}")
    private String SECRET_KEY;

    @Value("${taskmanager.jwt.issuer}")
    private String ISSUER;

    public boolean isTokenValid(JwtUserDetail user, String token) {

        final String username = extractUsername(token);

        logger.info("USERNAME FROM user: " + user.getUsername());
        logger.info("USERNAME FROM TOKEN: " + extractUsername(token));
        logger.info("EXPIRATION: " + getExpiration(token));
        logger.info(String.valueOf((user.getUsername().equals(extractUsername(token)))
                && getExpiration(token).before(new Date())));
        return (username.equals(user.getUsername()))
                && getExpiration(token).after(new Date());
    }

    public String generateToken(JwtUserDetail user) {
        Map<String, String> newClaims = new HashMap<>();
        newClaims.put("typ", "access");

        logger.info("FROM JWT: {}", user.getUsername());

        return Jwts
                .builder()
                .setClaims(newClaims)
                .setSubject(user.getUsername())
                .setIssuer(ISSUER)
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
                .setClaims(newClaims)
                .setSubject(user.getUsername())
                .setIssuer(ISSUER)
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

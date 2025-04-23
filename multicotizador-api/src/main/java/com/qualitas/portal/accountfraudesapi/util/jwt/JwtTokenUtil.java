package com.qualitas.portal.accountfraudesapi.util.jwt;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    private static final String SECRET = "cualquieClaveSecreta32Caracteres1234567890";
    private static final long EXPIRATION = 86400000; // 24 horas en ms
    private final Key key = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());

    public String generateToken(String username, String role, String nombreUsuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.startsWith("ROLE_") ? role : "ROLE_" + role);
        claims.put("nombreUsuario", nombreUsuario);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String getRoleFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("role", String.class));
    }
    public static boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        final String tokenRole = getRoleFromToken(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token)
                && userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(tokenRole));
    }

    private static boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public static String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private static Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private static Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
package com.qualitas.portal.accountfraudesapi.util.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {
    private static final String SECRET_KEY = "v0tAh5i3VUIfyCbNvcgAY5z2hB1x1WlTAMcm+6dAfWs=";

    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        List<String> rolesAndPermissions = authorities.stream()
                .map(GrantedAuthority::getAuthority) // Obtener el valor textual de cada autoridad
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(username)
                .claim("authorities", rolesAndPermissions) // Agregar roles y permisos al token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 día
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        List<String> rolesAndPermissions = (List<String>) Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("authorities");

        return rolesAndPermissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

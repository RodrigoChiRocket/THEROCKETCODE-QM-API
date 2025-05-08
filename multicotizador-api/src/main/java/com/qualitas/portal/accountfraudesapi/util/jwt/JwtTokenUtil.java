package com.qualitas.portal.accountfraudesapi.util.jwt;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
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

    // Método actualizado para recibir roleId
    // Método actualizado para recibir roleId como String
    public String generateToken(String username, String roleId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roleId", roleId.toString());
        // Agregar el rol como claim adicional para facilitar el acceso
        claims.put("role", roleId.equals(new BigDecimal(2)) ? "ROLE_ADMIN" : "ROLE_USER");
        return createToken(claims, username);
    }

    // Método para obtener el roleId del token como String
    public static String getRoleIdFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.get("roleId", String.class);
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

    public static boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
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
    public String getRoleFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("role", String.class));
    }

    public String getNameFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("name", String.class));
    }

    /**
     * Decodifica un token JWT y devuelve todos sus claims
     * @param token Token JWT (sin "Bearer ")
     * @return Mapa con todos los claims del token incluyendo el role
     * @throws ExpiredJwtException Si el token está expirado
     * @throws MalformedJwtException Si el token está malformado
     * @throws JwtException Si hay otros errores con el token
     */
    public Map<String, Object> decodeToken(String token) throws JwtException {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        Claims claims = claimsJws.getBody();

        Map<String, Object> result = new HashMap<>();
        result.put("subject", claims.getSubject());
        result.put("issuedAt", claims.getIssuedAt());
        result.put("expiration", claims.getExpiration());
        result.put("role", claims.get("roleId", String.class)); // Extraer el roleId como String
        result.put("claims", new HashMap<>(claims));

        return result;
    }
}
package com.apirest.JwtAuthService.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${security.jwt.key.private}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGeneratorToken;

    @Value("${security.jwt.expire}")
    private int durationToken;


    public String generateToken(Authentication authentication){
        return Jwts.builder()
                .issuer(userGeneratorToken)
                .subject(authentication.getPrincipal().toString())
                .claim("authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .issuedAt(new Date())// Fecha de creacion
                .expiration(new Date(System.currentTimeMillis() + durationToken))// Tiempo de expiracion del token
                .id(UUID.randomUUID().toString())//Se crea un ID unico para el token, esto puede ser utilizado para invalidar el token si es necesario
                .notBefore(new Date())// Fecha en la que el token es valido, en este caso es el momento de creacion
                .signWith(getSecretKey(), Jwts.SIG.HS256) // Algoritmo de firma
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        try{
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);

        }catch (JwtException e){
            throw new JwtException("El token es invalido o ha expirado: " + e.getMessage());
        }
    }

    public String getUsernameFromToken(Jws<Claims> jws) {
        return jws.getPayload().getSubject();
    }

    public String getSpecificClaim (Jws<Claims> jws, String nameClaim) {
        return jws.getPayload().get(nameClaim, String.class);
    }

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
    }
}

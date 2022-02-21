package com.example.wunderlist.service;

import com.couchbase.client.java.json.JsonObject;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    public void verifyAuthenticationHeader(String authentication, String expectedUsername) {
        String token = authentication.replaceFirst("Bearer ", "");
        String tokenName = verifyJwt(token);

        if (!expectedUsername.equals(tokenName)) {
            throw new IllegalStateException("Token and username don't match");
        }
    }

    public String buildToken(String username) {
        return Jwts.builder().signWith(SignatureAlgorithm.HS512, secret)
                .setPayload(JsonObject.create()
                        .put("user", username)
                        .toString())
                .compact();
    }

    private String verifyJwt(String token) {
        try {
            String username = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("user", String.class);
            return username;
        } catch (JwtException e) {
            throw new IllegalStateException("Could not verify JWT token", e);
        }
    }
}
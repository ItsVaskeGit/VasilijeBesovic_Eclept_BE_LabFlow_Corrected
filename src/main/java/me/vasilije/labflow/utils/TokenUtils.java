package me.vasilije.labflow.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Scope("singleton")
public class TokenUtils {

    @Value("${jwt-key}")
    private String JWT_SECRET;

    public TokenUtils() {}

    public String fetchToken(String username) {
        return Jwts.builder().subject(username).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes())).compact();
    }

    public String getUsername(String jwtToken) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes())).build().parseSignedClaims(jwtToken).getPayload().getSubject();
    }

    public boolean stillValid(String jwtToken) {
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes())).build().parseSignedClaims(jwtToken)
                    .getPayload().getExpiration();
            return true;
        }catch (ExpiredJwtException e) {
            return false;
        }
    }

    public boolean requestHasToken(HttpServletRequest req) {
        return req.getHeader("Authorization") != null;
    }

    public String getToken(HttpServletRequest req) {
        return req.getHeader("Authorization").split(" ")[1];
    }
}

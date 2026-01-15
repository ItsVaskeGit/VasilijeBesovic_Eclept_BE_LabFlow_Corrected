package me.vasilije.labflow.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import me.vasilije.labflow.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenUtils {

    @Value("${jwt.key}")
    private String JWT_SECRET;

    private UserService userService;

    public TokenUtils(UserService userService) {
        this.userService = userService;
    }

    public TokenUtils() {}

    public String fetchToken(String username) {
        return Jwts.builder().subject(username).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes())).compact();
    }

    public String getUsername(String jwtToken) {
        return Jwts.parser().setSigningKey(JWT_SECRET.getBytes()).build().parseClaimsJws(jwtToken).getBody().getSubject();
    }

    public boolean checkToken(String jwtToken) {
        return (userService.checkUserExists(getUsername(jwtToken)) && stillValid(jwtToken));
    }

    private boolean stillValid(String jwtToken) {
        var expirationDate = Jwts.parser().setSigningKey(JWT_SECRET.getBytes()).build().parseSignedClaims(jwtToken)
                .getBody().getExpiration();
        return expirationDate.after(new Date());
    }
}

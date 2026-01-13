package me.vasilije.labflow.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import me.vasilije.labflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class TokenUtils {

    @Value("${jwt-key}")
    private String JWT_SECRET;

    @Autowired
    private UserService userService;

    public String fetchToken(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes()), SignatureAlgorithm.ES512).compact();
    }

    public String getUsername(String jwtToken) {
        return Jwts.parser().setSigningKey(JWT_SECRET.getBytes()).build().parseClaimsJws(jwtToken).getBody().getSubject();
    }

    public boolean checkToken(String jwtToken) {
        return (userService.checkUserExists(getUsername(jwtToken)) && stillValid(jwtToken));
    }

    private boolean stillValid(String jwtToken) {
        var expirationDate = Jwts.parser().setSigningKey(JWT_SECRET.getBytes()).build().parseClaimsJws(jwtToken)
                .getBody().getExpiration();
        return expirationDate.after(new Date());
    }
}

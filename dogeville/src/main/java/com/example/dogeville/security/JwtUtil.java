package com.example.dogeville.security;

import com.example.dogeville.model.Wingman;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtUtil {
    //Valid for 24 hours
    private static final long ACCESS_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;
    private static final String SIGNING_KEY = "dogeville";

    public static String getUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public static Date getExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token).getBody();
    }

    private static Boolean isTokenExpired(String token) {
        final Date exp = getExpiration(token);
        return exp.before(new Date());
    }

    public String generateToken(Wingman wm) {
        return performGenerateToken(wm.getWmmail());
    }

    public String performGenerateToken(String subject) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority("WINGMAN")));

        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY).compact();
    }

    public static Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

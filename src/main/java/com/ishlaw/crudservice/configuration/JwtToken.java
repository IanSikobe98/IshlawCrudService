package com.ishlaw.crudservice.configuration;

import com.ishlaw.crudservice.entity.StaffDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtToken implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 1 * 60 * 60;

    @Value("${datasource.ishlaw.jwtSecret:1shL@W2ys}")
    private String secret;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }


    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateMainToken(StaffDetails staffDetails, HashSet<String> permissions, HashSet<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        Map<String, Object> team = new HashMap<>();
        team.put("name",roles);
        team.put("Permissions",permissions);
        claims.put("msisdn", staffDetails.getPhoneNumber());
        claims.put("firstName", staffDetails.getFirstName());
        claims.put("secondName", staffDetails.getSurname());
        claims.put("emailAddress", staffDetails.getEmailaddress());
        claims.put("id", staffDetails.getId());
        claims.put("Team", team);
        return doGenerateMainToken(claims, staffDetails.getPhoneNumber());
    }
    private String doGenerateMainToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

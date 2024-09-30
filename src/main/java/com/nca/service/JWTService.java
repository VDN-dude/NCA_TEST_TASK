package com.nca.service;

import com.nca.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class JWTService {

    @Value("#{appProperties.jwt.accessExpiration}")
    private Long accessExpiration;
    @Value("#{appProperties.jwt.accessSecret}")
    private String accessSecret;

    @Autowired
    private UserDetailsService userDetailsService;

    public String generateToken(String username, Set<UserRole> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", getUserRoleNamesFromJWT(roles));

        Date now = new Date();
        Date validity = new Date(now.getTime() + accessExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, accessSecret)
                .compact();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserUsernameFromJWT(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
    }

    public String getUserUsernameFromJWT(String token){
        return Jwts.parser().setSigningKey(accessSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token){
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(accessSecret).parseClaimsJws(token);

        return !claimsJws.getBody().getExpiration().before(new Date());
    }

    private List<String> getUserRoleNamesFromJWT(Set<UserRole> roles){
        List<String> result = new ArrayList<>();
        roles.forEach(role -> result.add(role.name()));
        return result;
    }
}
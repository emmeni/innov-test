package com.emmeni.innov.security;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.emmeni.innov.config.InnovProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenProvider {

    // private static final String AUTHORITIES_KEY = "auth";
	
	@Value("${token.signing.key}")
    private String jwtSigningKey;

    @Value("${innov.security.authentication.jwt.secret}")
    private String secretKey;

    @Value("${innov.security.authentication.jwt.tokenValidityInSeconds}")
    private long tokenValidityInSeconds;

    @Value("${innov.security.authentication.jwt.tokenValidityInSecondsForRememberMe}")
    private long tokenValidityInSecondsForRememberMe;

    @Autowired
    private InnovProperties innovProperties;

    public TokenProvider() {
    }

    @PostConstruct
    public void init() {
        this.secretKey = innovProperties.getSecurity().getAuthentication().getJwt().getSecret();
        this.tokenValidityInSeconds = 1000 * innovProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInSecondsForRememberMe = 1000 * innovProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSecondsForRememberMe();
    }

    public String generateToken(Authentication authentication, boolean rememberMe) {
    	
    	long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInSecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInSeconds);
        }
        
        String subject = authentication.getPrincipal() instanceof String ? (String) authentication.getPrincipal() :  authentication.getName();

        return Jwts.builder()
                // .setSubject(authentication.getName())
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // .signWith(SignatureAlgorithm.HS512, secretKey)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
    	
        // Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
                /*Arrays.asList(claims.get(AUTHORITIES_KEY).toString().split(",")).stream()
                        .map(authority -> new SimpleGrantedAuthority(authority))
                        .collect(Collectors.toList());*/

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            // Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
        	Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (SecurityException e) {
            log.info("Invalid JWT signature: " + e.getMessage());
            return false;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

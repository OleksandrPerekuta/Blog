package com.blog.security;

import com.blog.user.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final static String SECRET_KEY="DA64E47BD7AB83282D1B1566BAE8A2C32B3C7B52CCAC848F9B52FC78BCC84AA712C234F5DC39AA1AD4DC04D741B16F951097EAC457ABC81C7A7A1455951977A285CC6AFEA73E5D6F512397F0B8617530D2432B9AF618FE3B502D97CDFCE1772E5DF15F63B12E1668AFE2C575006FE1255BF3F0E98C70622A04FDD16CAFF22119D071721A6403DF5519281F544B19D8B08200CD50BE8885CCBDD46F8B259E7D9928C164F8A2A7A99012AB9E01D2CE222929BDBFAAF353959DD18CE3C206F6E545FE04253A5BAC68783299F3647C77FAFD4EDA5610453ECE93DF6AA4FC000CF789792213A294216B64B46B6F5A1580197979EE601D0718456010558D378E6D113A";

    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        final Claims claims=extractAllClaims(token);
        return resolver.apply(claims);
    }
    public String extractUsername(String jwt) {
        return extractClaim(jwt,Claims::getSubject);
    }
    public String extractEmail(String jwt) {
        return extractClaim(jwt, claims -> claims.get("email", String.class));
    }
    public String generateToken(Map<String, Object> extraClaims, UserEntity user){
        extraClaims.put("email",user.getEmail());
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60* 24)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateToken(UserEntity user){
        return generateToken(new HashMap<>(),user);
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username=extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

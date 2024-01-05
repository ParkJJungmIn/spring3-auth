package com.board.api.confiq;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.board.api.confiq.AppConstants.SECRET_KEY;
@Service
public class JwtService {

    // 토큰에서 사용자 이름을 추출하는 메소드
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    // 토큰에서 주장(claims)을 추출하는 범용 메소드
    public <T> T extractClaims(String token, Function<Claims, T> claimsTResolver){
        final Claims claims = extractAllClaims(token);
        return claimsTResolver.apply(claims);
    }

    // 사용자 세부 정보를 기반으로 토큰을 생성하는 메소드
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    // 토큰이 유효한지 확인하는 메소드
    public boolean isTokenValid( String token, UserDetails userDetails ){
        final String username = extractUsername(token);
        return (username.equals( userDetails.getUsername() )) && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    // 토큰에서 만료 시간을 추출하는 메소드
    private Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }


    // 사용자 세부 정보와 추가 클레임을 사용하여 JWT를 생성하는 메소드
    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){
        return Jwts.builder().setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 *  24  ))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256 )
                .compact();
    }
    private Claims extractAllClaims(String token){
//        return Jwts.builder().seSig
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}

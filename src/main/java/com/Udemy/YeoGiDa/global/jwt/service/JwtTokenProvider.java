package com.Udemy.YeoGiDa.global.jwt.service;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.global.jwt.dto.TokenInfo;
import com.Udemy.YeoGiDa.global.jwt.exception.TokenHasExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@Slf4j
public class JwtTokenProvider {

    private final Key key;

    long accessTokenPeriod = 1000L * 60L * 60L * 24L; //24시간
//    long accessTokenPeriod = 1000L * 30L; //30초
    long refreshTokenPeriod = 1000L * 60L * 60L * 24L * 30L; //1달
//    long refreshTokenPeriod = 1000L * 60L; //3분

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public TokenInfo generateToken(Member member) {

        long now = (new Date()).getTime();
        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(member.getEmail())
                .setExpiration(new Date(now + accessTokenPeriod))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(member.getEmail())
                .setExpiration(new Date(now + refreshTokenPeriod))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String getEmailFromAccessToken(String accessToken){
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(accessToken)
                .getBody();

        return claims.getSubject();
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    public String validateRefreshTokenAndReissueAccessToken(String refreshToken) {
        //검증
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);

        //refresh 토큰의 만료시간이 지나지 않았을 경우, 새로운 access 토큰을 생성합니다.
        if (!claims.getBody().getExpiration().before(new Date())) {
            return recreationAccessToken(claims.getBody().get("sub").toString(), claims.getBody().get("roles"));
        } else {
            throw new TokenHasExpiredException();
        }
    }

    public String recreationAccessToken(String email, Object roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();

        //AccessToken
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenPeriod))
                .signWith(key)
                .compact();

        return accessToken;
    }
}

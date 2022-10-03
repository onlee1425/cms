package com.zb.domain.config;

import com.zb.domain.domain.common.UserVo;
import com.zb.domain.domain.common.UserType;
import com.zb.domain.domain.util.Aes256Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Objects;

public class JwtAuthenticationProvider {
    private String secretKey = "secretKey";

    private long tokenValidTime = 1000L * 60 * 60 * 24; // 1 day

    public String createToken(String userPk, Long id, UserType userType) {
        Claims claims = Jwts.claims().setSubject(Aes256Util.encrypt(userPk)).setId(Aes256Util.encrypt(id.toString()));
        claims.put("roles",userType);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+tokenValidTime))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    //token validation check
    public boolean validateToken(String jwtToken){
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken); //token parse
            return !claimsJws.getBody().getExpiration().before(new Date()); //token Expiration이 현재시간보다 적을경우 가능
        }catch (Exception e){
            return false;
        }
    }

    //토큰을 받아서 user로 변환
    public UserVo getUserVo(String token){
        Claims c = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return new UserVo(Long.valueOf(Objects.requireNonNull(Aes256Util.decrypt(c.getId())))
                ,Aes256Util.decrypt(c.getSubject()));
    }
}

package com.example.util.jwt;




import com.example.dto.UserDTO;
import io.jsonwebtoken.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class JwtUtil {
    private final String secret_key;
    private long accessTokenValidity = 1*60*24;
    private final JwtParser jwtParser;
    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    private static JwtUtil instance;

    private JwtUtil() {
        this.secret_key = "my-secret";
        this.jwtParser = Jwts.parser().setSigningKey(secret_key);
    }

    public String createToken(UserDTO user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("userName",user.getName());
        claims.put("email",user.getEmail());
        claims.put("role",user.getType());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        String token =  Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();

        return token;
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(String token) {
        try {
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired");
            throw ex;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }



    public boolean validateClaims(Claims claims) throws Exception {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

    public String getEmail(Claims claims) {
        return claims.getSubject();
    }

    public List<String> getRoles(Claims claims) {
        return  (ArrayList<String>) claims.get("role");
    }

    public static JwtUtil getInstance(){
        return instance==null ? instance=new JwtUtil() : instance;
    }


}
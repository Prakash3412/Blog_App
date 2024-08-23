package com.springboot.blog.security;

import com.springboot.blog.exception.BlogAPIException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {


    //read key from the application properties file
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    //generate jwt token method   ----> from the authentication we get user name
    public String generateJwtToken(Authentication authentication){

          String username = authentication.getName();

         Date currentDate = new Date();

         Date expireDate = new Date(currentDate.getTime() +jwtExpirationDate);

         String token = Jwts.builder()
                 .subject(username)
                 .issuedAt(new Date())
                 .expiration(expireDate)
                 .signWith(key())
                 .compact();

                 return token;


    }


    //method for return key the user secret that will pas in signWith()
    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //get user name from the jwtToken

    public String getUserName(String token){

         return   Jwts.parser()
                  .verifyWith((SecretKey) key())
                  .build()
                  .parseEncryptedClaims(token)
                  .getPayload()
                  .getSubject();
    }

    //validate jwt token

    public boolean validateToken(String token){

        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parse(token);
                    return true;
        }catch (MalformedJwtException malformedJwtException){
           throw  new BlogAPIException(HttpStatus.BAD_REQUEST,"Invalid Jwt token");
        }catch (ExpiredJwtException expiredJwtException){
           throw  new BlogAPIException(HttpStatus.BAD_REQUEST,"Expire jwt token");
        }catch (UnsupportedJwtException unsupportedJwtException){
             throw  new BlogAPIException(HttpStatus.BAD_REQUEST,"unsupported Jwt token");
        }catch (IllegalArgumentException illegalArgumentException){
             throw  new BlogAPIException(HttpStatus.BAD_REQUEST,"jwt claims String null or empty");
        }
    }
}

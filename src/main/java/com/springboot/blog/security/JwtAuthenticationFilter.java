package com.springboot.blog.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//to authenticate the jwt token

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;

    private UserDetailsService userDetailsService;

    //inject the both dependency by constructor based
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    //to execute once per request
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //get jwt token from http request

        //called here the method that will created  getTokenFromRequest to get the jwt token form the request

        String token = getTokenFromRequest(request);



        //before validating we have to check whether the token empty or null
        // --> with the help of StringUtils class present in org.springframework.util.StringUtils & have method hasText()

        //now validate the jwt token ---> call the method of validate that will created in jwtTokenProvider class

        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){


            // just call the method getUsername that already created in jwtTokenProvider class
            //get the username from the token

            String username=jwtTokenProvider.getUserName(token);

            //load the user associated with token --> loadUserByUsername() method
            // to load the user from the database by using username

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);


            //create the authentication userName password with the help of UsernamePasswordAuthenticationToken class
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            //now set the authentication into the SecurityContestHolder
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }


        filterChain.doFilter(request,response);
    }




    //get jwt token from http request now this method used above
    private String getTokenFromRequest(HttpServletRequest request){

        String bearerToken = request.getHeader(" Authorization");

        // 1st check the user empty or null with this StringUtils.hasText(bearerToken)
        // that present in org.springframework.util.StringUtils

        // $$  and then extract jwt token form the header
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){

            return  bearerToken.substring(7,bearerToken.length());
        }

        return null;
    }
}

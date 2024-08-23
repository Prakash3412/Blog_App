package com.springboot.blog.securityConfig;

import com.springboot.blog.security.JwtAuthenticationEntryPoint;
import com.springboot.blog.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {


    private UserDetailsService userDetailsService;

    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    private JwtAuthenticationFilter authenticationFilter;

    //injecting tha userDetails service  And authenticationEntryPoint using constructor based
    public SecurityConfig(UserDetailsService userDetailsService ,
                          JwtAuthenticationEntryPoint authenticationEntryPoint,
                          JwtAuthenticationFilter authenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint=authenticationEntryPoint;
        this.authenticationFilter=authenticationFilter;
    }

    //we need to Encode password used PasswordEncoder class and BCryptPasswordEncoder() using Bcrypt algorithm
    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                 http.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorize)->

                        //authorize.anyRequest().authenticated())
                        authorize.requestMatchers(HttpMethod.GET,"/api/**").permitAll()  //get k lie sb koi ko access he
                             //   .requestMatchers(HttpMethod.GET,"/api/categories").permitAll() //also we can provide above also
                                .requestMatchers("/api/auth/**").permitAll()             //login koi bhi user kr skta h
                                .anyRequest().authenticated())     //uske alawa sb lock

                              // .httpBasic(Customizer.withDefaults());

                         .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
                         .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                 //execute before spring security filter
                  http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
                      return http.build();
    }

     // now data base authentication
    // 5.2 onwards the spring security .here now authenticationManager automatically provide the userDetailsService and passwordEncoder
    // and it will do the database authentication
    @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

         return configuration.getAuthenticationManager();
  }












  //in required when we are doing database authentication so commented all

    //Used UserDetailsService interface
//    @Bean
//     public UserDetailsService userDetailsService(){
//         UserDetails user= User.builder()
//                 .username("ratish")
//                 .password(passwordEncoder().encode("ratish"))
//                 .roles("USER")
//                 .build();
//
//         UserDetails admin =User.builder()
//                 .username("admin")
//                 .password(passwordEncoder().encode("admin"))
//                 .roles("ADMIN")
//                 .build();
//
//         return  new InMemoryUserDetailsManager(user,admin);
//         //InMemoryUserDetailsManager ->implementation class of UserDetailsService interface we need object and interface we cant create object
//
//     }
}

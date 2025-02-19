package com.springboot.blog.security;

import com.springboot.blog.entity.User;
import com.springboot.blog.reposistory.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    //injecting the UserRepository
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

                //user can either login username or email;

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("user not found with username Or Email" + usernameOrEmail));

        //converted  user set of Role into the Set of GrantedAuthority using stream Api

        //Converted because of spring security expect  set Granted Authority

        Set<GrantedAuthority> authorities = user.getRoles()    //get setRole
                .stream()
                .map((role)-> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword()
                 ,authorities);
    }
}

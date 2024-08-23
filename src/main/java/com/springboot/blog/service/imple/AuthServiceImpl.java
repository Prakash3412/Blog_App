package com.springboot.blog.service.imple;

import com.springboot.blog.Dtos.LoginDto;
import com.springboot.blog.Dtos.RegisterDto;
import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.reposistory.RoleRepository;
import com.springboot.blog.reposistory.UserRepository;
import com.springboot.blog.security.JwtTokenProvider;
import com.springboot.blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private JwtTokenProvider jwtTokenProvider;

    //injecting all value
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider)
    {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider=jwtTokenProvider;
    }

//    public AuthServiceImpl(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }

    @Override
    public String login(LoginDto loginDto) {

        //store to authentication .because SecurityContestHolder need authentication
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail()
                , loginDto.getPassword() ));


        SecurityContextHolder.getContext().setAuthentication(authenticate);

        //call the generateJwtToken to get the token
        String token =jwtTokenProvider.generateJwtToken(authenticate);

        return token;
    }

    @Override
    public String register(RegisterDto registerDto) {

        //check for  user exists in database

        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw  new BlogAPIException( HttpStatus.BAD_REQUEST,"userName is already exists!: TRY with another user name");
        }

        //check for email already exists in database or not

        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw  new BlogAPIException(HttpStatus.BAD_REQUEST,"email  is already exists Please provide a new Email to register ");
        }

        //set user that comes from the register

        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

         Set<Role> roles = new HashSet<>();

         //USER_ROLE me set hoga
         Role roleUser = roleRepository.findByName("ROLE_USER").get();
         roles.add(roleUser);

         //set role into the user
        user.setRoles(roles);

        //now save the user

        userRepository.save(user);

        return "user successfully registered !:";
    }
}

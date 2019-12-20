package com.javamentors.rest;

import com.javamentors.DTO.AppUserDto;
import com.javamentors.DTO.AuthenticationRequestDto;
import com.javamentors.entity.AppUser;
import com.javamentors.repository.UserRepository;
import com.javamentors.security.jwt.JwtTokenProvider;
import com.javamentors.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "/api/v1/login")
public class LoginRestControllerV1 {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Autowired
    public LoginRestControllerV1(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<AppUserDto> login (@RequestBody AuthenticationRequestDto requestDto){
        try {
            String userName = requestDto.getName();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, requestDto.getPassword()));
            AppUser appUser = userRepository.findByName(userName);

            if (appUser == null){
                throw new UsernameNotFoundException("User with username: " + userName + " not found");
            }

            String token = jwtTokenProvider.createToken(userName, appUser.getRoles());
            AppUserDto appUserDto = new AppUserDto(appUser, token);
            Map<Object, Object> response = new HashMap<>();
            response.put("user", appUserDto);
            response.put("token", token);

            return new ResponseEntity<>(appUserDto, HttpStatus.OK);
        }catch (AuthenticationException e){
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}

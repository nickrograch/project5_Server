package com.javamentors.service;

import com.javamentors.entity.AppUser;
import com.javamentors.repository.UserRepository;
import com.javamentors.security.jwt.JwtAppUser;
import com.javamentors.security.jwt.JwtAppUserFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByName(name);

        if (appUser == null){
            throw new UsernameNotFoundException("User " + name + " was not found in the database");
        }

        return JwtAppUserFactory.create(appUser);
    }
}

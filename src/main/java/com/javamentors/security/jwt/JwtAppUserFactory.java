package com.javamentors.security.jwt;

import com.javamentors.entity.AppUser;
import com.javamentors.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtAppUserFactory {

    public JwtAppUserFactory(){}

    public static JwtAppUser create(AppUser appUser){
        return new JwtAppUser(
                appUser.getId(),
                appUser.getUsername(),
                appUser.getPassword(),
                appUser.getEmail(),
                mapToGrantedAuthority(new ArrayList<>(appUser.getRoles()))
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthority(List<Role> userRoles){
        return userRoles.stream()
                .map(role ->
                    new SimpleGrantedAuthority(role.getRole())
                ).collect(Collectors.toList());
    }
}

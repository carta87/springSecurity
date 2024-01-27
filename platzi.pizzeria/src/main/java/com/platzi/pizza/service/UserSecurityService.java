package com.platzi.pizza.service;

import com.platzi.pizza.persistence.entity.UserEntity;
import com.platzi.pizza.persistence.entity.UserRoleEntity;
import com.platzi.pizza.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity =  userRepository
                .findById(username).orElseThrow(() -> new UsernameNotFoundException("User " +username +"noencontrado"));

        String[] roles = userEntity.getRoles().stream().map(UserRoleEntity::getRole).toArray(String[]::new);
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(grantedAuthority(roles))//.roles(roles)
                .accountLocked(userEntity.getLocked())
                .disabled(userEntity.getDisabled())
                .build();
    }

    private String[] getAuthorities(String role){
        if ("ADMIN".equals(role) || "CLIENTE".equals(role) ){
            return new String[]{ "random_order"};
        }
        return new String[] {};
    }

    private List<GrantedAuthority> grantedAuthority(String[] roles){
        List<GrantedAuthority> authoritiess = new ArrayList<>(roles.length);
        for (String role: roles){
            authoritiess.add(new SimpleGrantedAuthority("ROLE_" +role));
            for (String authority: getAuthorities(role)){
                authoritiess.add(new SimpleGrantedAuthority(authority));
            }
        }
        return authoritiess;
    }
}

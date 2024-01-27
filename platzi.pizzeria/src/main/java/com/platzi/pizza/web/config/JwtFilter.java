package com.platzi.pizza.web.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/**
 * Cada vez que entre una solicitud se validara
 * **/
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private  final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //1. validar que sea un authorization valido
        String athHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (athHeader ==null || athHeader.isEmpty() || !athHeader.startsWith("Bearer") ){
            filterChain.doFilter(request, response);
            return;
        }

        //2. Validar que el JWT sea valido.
        String jwt = athHeader.split(" ")[1].trim();
        if (!jwtUtil.isValid(jwt)){
            filterChain.doFilter(request, response);
            return;
        }

        //3. cargar el usuario del USerDetalsService
        String username = jwtUtil.getUsername(jwt);
        User user = (User) userDetailsService.loadUserByUsername(username);

        //4.carga el usuario en el contexto de seguridad
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword(), user.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        System.out.println(authenticationToken);

        filterChain.doFilter(request, response);
    }
}

package com.buana.itemdata.auth.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.buana.itemdata.dto.CustomResponse;
import com.buana.itemdata.service.user.CustomUserServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j



public class CustomAuthentificationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    private final CustomUserServices customUserServices;


    public CustomAuthentificationFilter(AuthenticationManager authenticationManager, CustomUserServices customUserServices) {
        this.authenticationManager = authenticationManager;
        this.customUserServices = customUserServices;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        com.buana.itemdata.model.user.User userData = customUserServices.getUser(username);

        if (customUserServices.unlockWhenTimeExpired(userData) && userData.isAccountNonLocked()) {
            customUserServices.resetFailedAttempts(username);
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            String access_token = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 1))
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(algorithm);
            String refresh_token = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 6))
                    .withIssuer(request.getRequestURL().toString())
                    .sign(algorithm);

            HashMap<Object, Object> map = new HashMap<>();
            map.put("access_token",access_token);
            map.put("refresh_token",refresh_token);
            CustomResponse response1 = new CustomResponse(HttpStatus.OK.value(), 200, "SUCCESS", map);
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), response1);
        }
        CustomResponse response1 = new CustomResponse();
        response1.setHttpCode(HttpStatus.FORBIDDEN.value());
        response1.setResponseCode(403);
        response1.setResponseMessage("Your account locked,please try again later.");
        response1.setData(null);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), response1);
    }




}

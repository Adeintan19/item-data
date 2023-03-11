package com.buana.itemdata.auth.security;


import com.buana.itemdata.auth.filter.CustomAuthentificationFilter;
import com.buana.itemdata.auth.filter.CustomAuthorizationFilter;
import com.buana.itemdata.repository.user.UserRepository;
import com.buana.itemdata.service.user.CustomUserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private String allowedOrigins="localhost:8080";

    private final UserRepository userDao;

    private final CustomUserServices customUserServices;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setMaxAge(1800L);
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        CustomAuthentificationFilter customAuthentificationFilter = new CustomAuthentificationFilter(authenticationManagerBean(), customUserServices);
        customAuthentificationFilter.setFilterProcessesUrl("/api/login");
        customAuthentificationFilter.setUsernameParameter("username");
        customAuthentificationFilter.setAuthenticationFailureHandler(loginFailureHandler);
        http.csrf().ignoringAntMatchers("/api/**");
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        //POST

        http.authorizeHttpRequests().antMatchers(POST, "/api/users/save").permitAll();
        http.authorizeHttpRequests().antMatchers(POST, "/api/role/save").permitAll();
        http.authorizeHttpRequests().antMatchers(POST, "/api/role/addtouser").permitAll();
        http.authorizeHttpRequests().antMatchers(POST, "/api/login/**", "/api/token/refresh").permitAll();
        http.authorizeHttpRequests().antMatchers(POST, "/api/product/insert").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeHttpRequests().antMatchers(POST, "/api/cart/finalize").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeHttpRequests().antMatchers(POST, "/api/cart/remove").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeHttpRequests().antMatchers(POST, "/api/cart/insert").hasAnyAuthority("ROLE_ADMIN");




        //GET
        http.authorizeHttpRequests().antMatchers(GET, "/api/users").permitAll();
        http.authorizeHttpRequests().antMatchers(GET, "/api/product/**").hasAnyAuthority("ROLE_ADMIN");

        http.authorizeHttpRequests().anyRequest().authenticated();
        http.addFilter(customAuthentificationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    private CustomLoginFailureHandler loginFailureHandler;

}

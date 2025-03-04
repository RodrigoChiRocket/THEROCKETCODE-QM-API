package com.qualitas.portal.accountfraudesapi.configuracion.security;

import com.qualitas.portal.accountfraudesapi.util.exception.UnauthorizedException;
import com.qualitas.portal.accountfraudesapi.util.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            throw new UnauthorizedException("Access Denied: You do not have the necessary permissions.");
        };
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/usuarios/**").hasRole("USER")
                .antMatchers("/roles/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/usuarios/**").hasAuthority("GET")
                .antMatchers(HttpMethod.POST, "/usuarios/**").hasAuthority("POST")
                .antMatchers(HttpMethod.PUT, "/usuarios/**").hasAuthority("PUT")
                .antMatchers(HttpMethod.DELETE, "/usuarios/**").hasAuthority("DELETE")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

package com.berge.ratenow.testapplication.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.berge.ratenow.testapplication.constants.SecurityConstants;
import com.berge.ratenow.testapplication.service.impl.AppUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private AppUserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(AppUserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] angular = new String[]{
                "/**styles**.css",
                "/**runtime**.js",
                "/**polyfills**.js",
                "/**main**.js",
                "/**favicon.ico",
                "/**fontawesome**",
                "/assets/**",
                "/**.webp",
                "/**.png"
        };

        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/actuator/**", "/api-docs/**", "/v2/api-docs/**").permitAll()
                .antMatchers("/doc/**", "/index.html").permitAll()
                .antMatchers(angular).permitAll()
                .antMatchers("/swagger-ui**", "/webjars/**", "/swagger-resources/**").permitAll()
                .antMatchers("/").permitAll()
				/////////////
                .antMatchers("/ratenow**", "/ratenow/**", "/ratenow/services**", "/ratenow/services/**").permitAll()
                .antMatchers("/").permitAll()
                /////////////
                .antMatchers(HttpMethod.GET,"/api/revisionConfig").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), userDetailsService, bCryptPasswordEncoder))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), userDetailsService))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new Http401AuthenticationEntryPoint("headerValue"));
        ;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration x = new CorsConfiguration();
        x.applyPermitDefaultValues();
        x.addExposedHeader(SecurityConstants.HEADER_STRING);
        x.addAllowedMethod(HttpMethod.PUT.toString());
        x.addAllowedMethod(HttpMethod.DELETE.toString());
        source.registerCorsConfiguration("/**", x);
        return source;
    }
}

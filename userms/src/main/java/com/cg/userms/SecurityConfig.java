package com.cg.userms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * setting up users
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    /**
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        http.cors().configurationSource(corsConfiguration()).
                and().
                csrf().disable().httpBasic().
                and().
                authorizeRequests().
                antMatchers("/p/**").permitAll().
                antMatchers("/").permitAll().
                antMatchers("/login").permitAll().
                antMatchers("/c/**").authenticated().
                antMatchers("/a/**").hasAnyRole("ADMIN").
                and().
                formLogin().permitAll().failureForwardUrl("/loginfail").defaultSuccessUrl("/success").
                and().
                logout().clearAuthentication(true).deleteCookies("JSESSIONID").
                invalidateHttpSession(true).logoutSuccessUrl("/logoutsuccess");
    }

    /**
     *
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfiguration()
    {
        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        src.registerCorsConfiguration("/**",config);
        return src;
    }

    @Bean
    public PasswordEncoder encoder()
    {
        //no encoding on password
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        return encoder;
    }
}

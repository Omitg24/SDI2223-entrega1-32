package com.uniovi.sdi.sdi2223entrega132;

import com.uniovi.sdi.sdi2223entrega132.handlers.FailureLoginHandler;
import com.uniovi.sdi.sdi2223entrega132.handlers.SuccessLoginHandler;
import com.uniovi.sdi.sdi2223entrega132.handlers.SuccessLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

/**
 * Configuración de la seguridad de la aplicación
 *
 * @author David Leszek Warzynski Abril, Israel Solís Iglesias y Omar Teixeira González
 * @version 18/03/2023
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SuccessLoginHandler successHandler;
    @Autowired
    private SuccessLogoutHandler successLogoutHandler;
    @Autowired
    private FailureLoginHandler failureLoginHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringSecurityDialect securityDialect() {
        return new SpringSecurityDialect();
    }

    /**
     * Configuración de los accesos a las urls de la aplicación
     *
     * @param http seguridad HTTP
     * @throws Exception Excepción a lanzar
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/css/**", "/images/**", "/script/**", "/", "/signup", "/login/**").permitAll()
                .antMatchers("/user/**").hasRole("ADMIN")
                .antMatchers("/log/**").hasRole("ADMIN")
                .antMatchers("/offer/add", "/offer/purchasedList", "/offer/ownedList").hasRole("USER")
                .antMatchers("/offer/searchList").hasAnyRole("USER", "ADMIN")
                .antMatchers("/conversation/**").hasRole("USER")
                .antMatchers("/pictures/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .successHandler(successHandler)
                .failureHandler(failureLoginHandler)
                .and()
                .logout()
                .logoutSuccessHandler(successLogoutHandler)
                .permitAll();
    }
}

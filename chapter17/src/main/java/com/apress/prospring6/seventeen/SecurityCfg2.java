/*
Freeware License, some rights reserved

Copyright (c) 2023 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy 
of this software and associated documentation files (the "Software"), 
to work with the Software within the limits of freeware distribution and fair use. 
This includes the rights to use, copy, and modify the Software for personal use. 
Users are also allowed and encouraged to submit corrections and modifications 
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for 
commercial use in any way, or for a user's educational materials such as books 
or blog articles without prior permission from the copyright holder. 

The above copyright notice and this permission notice need to be included 
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospring6.seventeen;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

/**
 * Created by iuliana on 27/02/2023
 * Comment {@link SecurityCfg} and {@link SecurityCfg3}, remove comments from the annotation configurations, start up the container
 * as instructed in CHAPTER17.adoc on this class and restart the Apache Tomcat launcher.
 */
/*@Configuration
@EnableWebSecurity
@EnableMethodSecurity*/
public class SecurityCfg2 {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers( "/styles/**", "/images/**").permitAll()
                        .anyRequest().authenticated())
                //.httpBasic(Customizer.withDefaults()) // or .httpBasic
                // .logout(Customizer.withDefaults()) // or .logout()
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .logoutUrl("/exit")
                        .permitAll()
                        .clearAuthentication(true))
                //.formLogin(Customizer.withDefaults())   // or .formLogin()
                .formLogin(loginConfigurer -> loginConfigurer
                        .loginPage("/auth")
                        .loginProcessingUrl("/auth")
                        .usernameParameter("user")
                        .passwordParameter("pass")
                        .defaultSuccessUrl("/home")
                        .permitAll())
                //.csrf().disable(); // deprecated starting with Spring 6.1
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
     @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    UserDetailsManager users(DataSource dataSource) {
       User.UserBuilder users  = User.builder().passwordEncoder(encoder()::encode);
        var joe = users
                .username("john")
                .password("doe")
                .roles("USER")
                .build();
        var jane = users
                .username("jane")
                .password("doe")
                .roles("USER", "ADMIN")
                .build();
        var admin = users
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();
        var manager = new JdbcUserDetailsManager(dataSource);
        manager.createUser(joe);
        manager.createUser(jane);
        manager.createUser(admin);
        return manager;

        //return new JdbcUserDetailsManager(dataSource);
    }
}

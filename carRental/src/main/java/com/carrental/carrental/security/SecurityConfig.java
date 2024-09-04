package com.carrental.carrental.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {


    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // username - username password - password
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT user_name AS username, password, enabled FROM user WHERE user_name = ?"
        );


        // authorities - role
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.user_name AS username, r.role_name AS authority " +
                        "FROM user_roles ur " +
                        "JOIN user u ON ur.user_id = u.id " +
                        "JOIN roles r ON ur.role_id = r.id " +
                        "WHERE u.user_name = ?"
        );

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                                configurer
                                        .requestMatchers("/").permitAll()
                                        .requestMatchers("/login").permitAll()
                                        .requestMatchers("/register").permitAll()
                                        .requestMatchers("/css/**").permitAll()
                                        .requestMatchers("/photos/**").permitAll()
                                        .requestMatchers("/car/**").hasAnyRole("SELLER", "ADMIN")
                                        .requestMatchers("/admin/**").hasAnyRole("SELLER", "ADMIN")
//                                .requestMatchers("leaders/**").hasRole("MANAGER")
//                                .requestMatchers("/systems/**").hasRole("ADMIN")
                                        .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form.loginPage("/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .defaultSuccessUrl("/", true)
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                                .permitAll()
                )
                .exceptionHandling(exception -> exception.accessDeniedPage("/"));
        return http.build();
    }
}
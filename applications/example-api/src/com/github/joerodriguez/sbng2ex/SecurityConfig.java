package com.github.joerodriguez.sbng2ex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(getPasswordEncoder())
                .usersByUsernameQuery("select email as username, password, true from users where email=?")
                .authoritiesByUsernameQuery("SELECT ? as username, 'ROLE_USER' as role;");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/sessions/**").permitAll()
                .antMatchers("/pub/**").permitAll()
                .antMatchers("/v2/api-docs/**", "/swagger-resources/**", "/swagger-ui.html", "/webjars/springfox-swagger-ui/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable()
                .csrf().disable();
    }
}
package com.robogames.RoboCupMS.Auth;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

        private static final String[] NOT_SECURED = new String[] {
                        GlobalConfig.AUTH_PREFIX + "/login",
                        GlobalConfig.AUTH_PREFIX + "/register"
        };

        @Autowired
        private UserRepository repository;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
                http.csrf().disable()
                                .addFilterAfter(
                                                new TokenAuthorizationFilter(GlobalConfig.HEADER_FIELD_TOKEN,
                                                                repository, NOT_SECURED),
                                                UsernamePasswordAuthenticationFilter.class)
                                .authorizeRequests()
                                .antMatchers(NOT_SECURED)
                                .permitAll()
                                .anyRequest().authenticated();
        }

}

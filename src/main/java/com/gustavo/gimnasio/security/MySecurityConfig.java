package com.gustavo.gimnasio.security;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.gustavo.gimnasio.services.CustomUserDetailsService;

import net.bytebuddy.build.Plugin.Engine.Source.InMemory;

@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                // .csrf().disable()
                // .authorizeRequests()
                // .antMatchers("/register","/login").permitAll()
                // .anyRequest().authenticated()
                // .and()
                // .httpBasic();

                // .authorizeRequests()
                // .antMatchers("/dashboard").authenticated()
                // .antMatchers("/home").permitAll()
                // .and()
                // .formLogin()
                // .and()
                // .httpBasic();

                // .authorizeRequests()
                // .anyRequest()
                // .denyAll()
                // .and()
                // .httpBasic();

                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/register", "/login").permitAll()
                .antMatchers("/uploadFile").permitAll()
                .antMatchers("/uploadMultipleFiles").permitAll()
                .antMatchers("/downloadFile/**").permitAll()
                .antMatchers("/get_users", "/delete/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/login?logout");
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // auth.inMemoryAuthentication()
        // .withUser("gustavo").password("1234").authorities("admin")
        // .and()
        // .withUser("gustavoUser").password("1234").authorities("user")
        // .and()
        // .passwordEncoder(NoOpPasswordEncoder.getInstance());

        // InMemoryUserDetailsManager userDetailsService= new
        // InMemoryUserDetailsManager();
        // UserDetails user1 =
        // User.withUsername("adminGustavo").password("1234").authorities("admin").build();
        // UserDetails user2 =
        // User.withUsername("userGustavo").password("1234").authorities("user").build();
        // userDetailsService.createUser(user1);
        // userDetailsService.createUser(user2);
        // auth.userDetailsService(userDetailsService);

        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception{
    // auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    // }
}

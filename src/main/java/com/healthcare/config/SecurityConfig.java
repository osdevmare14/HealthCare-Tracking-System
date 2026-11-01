package com.healthcare.config;

import com.healthcare.service.AccountUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final AccountUserDetailsService userDetailsService;

    public SecurityConfig(AccountUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/assets/**", "/login", "/login/**", "/signup").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/reports/**").hasRole("DOCTOR")
                .antMatchers("/patients/me").hasRole("PATIENT")
                .antMatchers("/patients/**").hasAnyRole("PATIENT", "DOCTOR")
                .antMatchers("/dashboard").authenticated()
                .anyRequest().authenticated()
            .and()
                .formLogin()
                    .loginPage("/login/patient")
                    .loginProcessingUrl("/login")
                    .successHandler((request, response, authentication) -> {
                        String portal = request.getParameter("portal");
                        String expectedRole = "doctor".equals(portal) ? "ROLE_DOCTOR"
                                : "admin".equals(portal) ? "ROLE_ADMIN" : "ROLE_PATIENT";
                        boolean matchesPortal = authentication.getAuthorities().stream()
                                .anyMatch(authority -> expectedRole.equals(authority.getAuthority()));
                        if (!matchesPortal) {
                            SecurityContextHolder.clearContext();
                            if (request.getSession(false) != null) {
                                request.getSession(false).invalidate();
                            }
                            response.sendRedirect(request.getContextPath() + "/login/"
                                    + ("doctor".equals(portal) || "admin".equals(portal) ? portal : "patient")
                                    + "?error");
                            return;
                        }
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                    })
                    .failureHandler((request, response, exception) -> {
                        String portal = request.getParameter("portal");
                        String loginPage = "doctor".equals(portal) || "admin".equals(portal) ? portal : "patient";
                        response.sendRedirect(request.getContextPath() + "/login/" + loginPage + "?error");
                    })
                    .permitAll()
            .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll();
    }
}
